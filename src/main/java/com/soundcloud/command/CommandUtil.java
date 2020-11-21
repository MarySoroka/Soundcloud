package com.soundcloud.command;

/**
 * Command util class
 */
public class CommandUtil {
    private CommandUtil(){}

    /**
     * method return audio name without extension
     * @param filename audio file name
     * @return audio file name without extension
     */
    public static String getAudioName(String filename){
        int dotIndex = filename.indexOf(".");
        if (dotIndex==-1){
            return filename;
        }
        return filename.substring(0,dotIndex);
    }
}
