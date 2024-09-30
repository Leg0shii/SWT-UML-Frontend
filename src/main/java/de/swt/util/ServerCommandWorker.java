package de.swt.util;

import de.swt.Server;
import de.swt.database.DBManager;
import de.swt.logic.course.Course;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.manager.CommandObject;
import de.swt.manager.UserCommandManager;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class ServerCommandWorker extends TimerTask {

    private final Server server;
    private final CourseManager courseManager;
    private final UserManager userManager;
    private final GroupManager groupManager;
    private final SessionManager sessionManager;
    private final DBManager dbManager;
    private final UserCommandManager userCommandMananger;
    private LinkedBlockingQueue<CommandObject> serverCommandQueue;


    /**
     * Initializes the ServerCommandWorker with its required classes.
     * @param serverCommandQueue Is used to store the commands that the client has to work through.
     */
    public ServerCommandWorker(LinkedBlockingQueue<CommandObject> serverCommandQueue) {
        this.serverCommandQueue = serverCommandQueue;
        this.server = Server.getInstance();
        this.userManager = server.getUserManager();
        this.courseManager = server.getCourseManager();
        this.groupManager = server.getGroupManager();
        this.sessionManager = server.getSessionManager();
        this.dbManager = server.getDbManager();
        this.userCommandMananger = server.getUserCommandManager();
    }

    /**
     * Pushes new commands into the LinkedQueue which the client then can get.
     */
    @Override
    public void run() {
        while (serverCommandQueue.peek() != null) {
            evaluateCommand(serverCommandQueue.poll());
        }
    }

    /**
     * Evaluates a command object which commands the client to do something.
     * @param command Is the command that the client receives indirectly from the server.
     */
    private void evaluateCommand(CommandObject command) {

        Object updatedObject = command.getUpdatedObject();
        int originId = command.getOriginId();
        byte[] workspaceBytes = command.getWorkspaceFileBytes();
        byte[] taskBytes = command.getTaskBytes();
        String[] keyArgs = command.getCommand().split(":");
        String[] args = new String[0];

        if (keyArgs.length > 1) {
            args = keyArgs[1].split(" ");
        }

        switch (keyArgs[0]) {
            // UU:id - Update User: updates a user object with a certain id.
            case "UU" -> {
                User user = (User) updatedObject;
                userManager.cacheSingleData(dbManager.updateUser(user));
            }
            // CU:id - Update Course: updates a course object with a certain id.
            case "CU" -> {
                Course course = (Course) updatedObject;
                courseManager.cacheSingleData(dbManager.updateCourse(course));
            }
            // SU:id - Update Session: updates a session object with a certain id.
            case "SU" -> {
                Session session = (Session) updatedObject;
                if (session.getMasterIds().size() > 0) {
                    sessionManager.cacheSingleData(dbManager.updateSession(session));
                } else {
                    CommandObject serverCommand = new CommandObject();
                    serverCommand.setCommand("DS:" + session.getSessionId());
                    serverCommandQueue.add(serverCommand);
                }
            }
            // GU:id - Update Group: updates a group object with a certain id.
            case "GU" -> {
                Group group = (Group) updatedObject;
                groupManager.cacheSingleData(dbManager.updateGroup(group));
                try {
                    sessionManager.cacheSingleData(dbManager.updateSession(sessionManager.load(group.getSessionId())));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            // FU:bytes - Update Workspace Data: updates workspace data.
            case "FU" -> {
                HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
                CommandObject commandObject = new CommandObject();

                for (Group group : groupManager.getHashMap().values()) {
                    if (group.getUserIds().contains(originId)) {
                        for (int ids : group.getUserIds()) {
                            if (originId != ids) {
                                System.out.println("[" + originId + "]: workspace update group ping.");
                                commandObject.setCommand("FU:");
                                commandObject.setWorkspaceFileBytes(workspaceBytes);
                                try {
                                    userCommandQueue.get(ids).put(commandObject);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return;
                    }
                }

                for (Session session : sessionManager.getHashMap().values()) {
                    if (session.getMasterIds().contains(originId)) {
                        for (int ids : session.getUserIds()) {
                            if (originId != ids) {
                                System.out.println("[" + originId + "]: workspace update session ping.");
                                commandObject.setCommand("FU:");
                                commandObject.setWorkspaceFileBytes(workspaceBytes);
                                try {
                                    userCommandQueue.get(ids).put(commandObject);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            // RE:id - Request Ping: sends a request ping with a teacher id.
            case "RE" -> {
                int teacherId = Integer.parseInt(args[0]);

                HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
                CommandObject userCommand = new CommandObject();
                if (userManager.getHashMap().containsKey(teacherId)) {
                    System.out.println("[" + originId + ", " + teacherId + "]: user to teacher request ping.");
                    userCommand.setCommand("RE:" + originId);
                    userCommandQueue.get(teacherId).add(userCommand);
                }
            }
            // AN:id1;id2 - Answer Ping: sends an answer from user id1 to teacher id2.
            case "AN" -> {
                int destinationId = Integer.parseInt(args[0]);
                int answer = Integer.parseInt(args[1]);

                HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
                CommandObject userCommand = new CommandObject();
                if (userManager.getHashMap().containsKey(destinationId)) {
                    System.out.println("[" + originId + ", " + destinationId + "]: teacher to user accept ping.");
                    userCommand.setCommand("AN:" + answer);
                    userCommand.setOriginId(originId);
                    userCommandQueue.get(destinationId).add(userCommand);
                }
            }
            // DG:id - Delete Group: deletes group based on id.
            case "DG" -> {
                int groupId = Integer.parseInt(args[0]);

                dbManager.deleteGroup(groupId);
                groupManager.deleteSingleData(groupId);
            }
            // DS:id - Delete Session: deletes session based on id.
            case "DS" -> {
                int sessionId = Integer.parseInt(args[0]);
                try {
                    Session session = sessionManager.load(sessionId);
                    for (int groupId : session.getGroupIds()) {
                        dbManager.deleteGroup(groupId);
                        groupManager.deleteSingleData(groupId);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                dbManager.deleteSession(sessionId);
                sessionManager.deleteSingleData(sessionId);
            }
            // DC:id - Delete Course: deletes course based on id.
            case "DC" -> {
                int courseId = Integer.parseInt(args[0]);

                dbManager.deleteCourse(courseId);
                courseManager.deleteSingleData(courseId);
            }
            // ST:bytes1;bytes2 - Send Task: send workspace bytes and task bytes.
            case "ST" -> {
                HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
                CommandObject userCommand = new CommandObject();
                Session session = sessionManager.getSessionFromTeacherId(originId);
                for (int userId : session.getUserIds()) {
                    if (userId == originId) {
                        continue;
                    }
                    System.out.println("[" + originId + ", " + userId + "]: sending task ping.");
                    userCommand.setCommand("ST:");
                    userCommand.setWorkspaceFileBytes(workspaceBytes);
                    userCommand.setTaskBytes(taskBytes);
                    userCommandQueue.get(userId).add(userCommand);
                }
            }
            // WU - Workspace State Ping: sends a ping to change current workspace state.
            case "WU" -> {
                String state = args[0];
                Session session = sessionManager.getSessionFromTeacherId(originId);
                for (int userId : session.getUserIds()) {
                    if (userId == originId) {
                        continue;
                    }
                    System.out.println("[" + userId + "]: sending Workspace State ping.");
                    CommandObject userCommand = new CommandObject();
                    userCommand.setCommand("WU:" + state);
                    userCommandMananger.getUserCommandQueue().get(userId).add(userCommand);
                    if (!state.equals("VIEWING")) {
                        if (!session.getMasterIds().contains(userId)) {
                            session.getMasterIds().add(userId);
                            dbManager.updateSession(session);
                        }
                    } else {
                        if (session.getMasterIds().contains(userId)) {
                            session.getMasterIds().remove((Integer) userId);
                            dbManager.updateSession(session);
                        }
                    }
                }
            }
            // DO:id1;id2 - Delete Object: deletes an object based on id.
            case "DO" -> {
                int[] id = new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1])};
                Session session = sessionManager.getSessionFromTeacherId(originId);
                for (int userId : session.getUserIds()) {
                    if (userId == originId) {
                        continue;
                    }
                    System.out.println("[" + userId + "]: sending Delete Object ping.");
                    CommandObject userCommand = new CommandObject();
                    userCommand.setCommand("DO:" + id[0] + " " + id[1]);
                    userCommandMananger.getUserCommandQueue().get(userId).add(userCommand);
                }
            }
        }
    }
}
