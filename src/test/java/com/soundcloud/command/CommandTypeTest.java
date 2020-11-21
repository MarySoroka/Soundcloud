package com.soundcloud.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class CommandTypeTest {
    @Test
    public void getByNameCommandTypeDefaultIsEmpty() {
        String type = "DEFAULT";
        CommandType commandType = CommandType.of(type);
        assertNotNull(commandType);
    }
    @Test
    public void getByNameCommandTypeIsRight() {
        for (CommandType type:
             CommandType.values()) {
            CommandType commandType = CommandType.of(type.name());
            assertNotNull(commandType);
        }
    }

}