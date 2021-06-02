package de.swt.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandObject implements Serializable {

    private int originId;
    private String command;
    private Object updatedObject;
    private byte[] workspaceFileBytes;
    private byte[] taskBytes;

}
