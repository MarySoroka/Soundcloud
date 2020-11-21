package com.soundcloud.command;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CommandUtilTest {

    @Test
    public void getAudioName_isRight_whenRightString(){
        Assert.assertEquals("IDG",CommandUtil.getAudioName("IDG.mp3"));
    }
    @Test
    public void getAudioName_isRight_whenStringWithoutExtension(){
        Assert.assertEquals("IDG",CommandUtil.getAudioName("IDG"));
    }
}