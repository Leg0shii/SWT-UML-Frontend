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
import de.swt.manager.UserCommandMananger;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;
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
    private final UserCommandMananger userCommandMananger;
    private LinkedBlockingQueue<CommandObject> serverCommandQueue;


    public ServerCommandWorker(LinkedBlockingQueue<CommandObject> serverCommandQueue) {
        this.serverCommandQueue = serverCommandQueue;
        this.server = Server.getInstance();
        this.userManager = server.getUserManager();
        this.courseManager = server.getCourseManager();
        this.groupManager = server.getGroupManager();
        this.sessionManager = server.getSessionManager();
        this.dbManager = server.getDbManager();
        this.userCommandMananger = server.getUserCommandMananger();
    }

    @Override
    public void run() {
        while (serverCommandQueue.peek() != null){
            System.out.println(serverCommandQueue);
            evaluateCommand(serverCommandQueue.poll());
        }
    }

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
            case "UU" -> {
                User user = (User) updatedObject;
                dbManager.updateUser(user);
            }
            case "CU" -> {
                Course course = (Course) updatedObject;
                dbManager.updateCourse(course);
            }
            case "SU" -> {
                Session session = (Session) updatedObject;
                if (session.getMasterIds().size() > 0) {
                    dbManager.updateSession(session);
                } else {
                    CommandObject serverCommand = new CommandObject();
                    serverCommand.setCommand("DS:" + session.getSessionId());
                    serverCommandQueue.add(serverCommand);
                }
            }
            case "GU" -> {
                Group group = (Group) updatedObject;
                int groupId = dbManager.updateGroup(group);
                try {
                    Session session = server.getSessionManager().load(group.getSessionId());
                    session.getGroupIds().add(groupId);
                    dbManager.updateSession(session);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
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
                                userCommandQueue.get(ids).add(commandObject);
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
                                userCommandQueue.get(ids).add(commandObject);
                            }
                        }
                    }
                }
            }
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
            case "AN" -> {
                int destinationId = Integer.parseInt(args[0]);
                int answer = Integer.parseInt(args[1]);

                HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
                CommandObject userCommand = new CommandObject();
                if (userManager.getHashMap().containsKey(destinationId)) {
                    System.out.println("[" + originId + ", " + destinationId + "]: teacher to user accept ping.");
                    userCommand.setCommand("AN:" + answer);
                    userCommandQueue.get(originId).add(userCommand);
                }
            }
            case "DG" -> {
                int groupId = Integer.parseInt(args[0]);

                dbManager.deleteGroup(groupId);
            }
            case "DS" -> {
                int sessionId = Integer.parseInt(args[0]);

                dbManager.deleteSession(sessionId);
            }
            case "DC" -> {
                int courseId = Integer.parseInt(args[0]);

                dbManager.deleteCourse(courseId);
            }
            case "ST" -> {
                HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
                CommandObject userCommand = new CommandObject();
                Session session = sessionManager.getSessionFromTeacherId(originId);
                for (int userId : session.getUserIds()) {
                    if (userId == originId) {
                        continue;
                    }
                    System.out.println("[" + originId + "]: sending task ping.");
                    userCommand.setCommand("ST:");
                    userCommand.setWorkspaceFileBytes(workspaceBytes);
                    userCommand.setTaskBytes(taskBytes);
                    userCommandQueue.get(userId).add(userCommand);
                }
            }
        }
    }
}
