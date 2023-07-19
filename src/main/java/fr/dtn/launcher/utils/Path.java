package fr.dtn.launcher.utils;

import fr.flowarg.flowcompat.Platform;

import java.nio.file.Paths;

public class Path {
    public java.io.File get(String fileName){
        java.nio.file.Path path;

        switch(Platform.getCurrentPlatform()){
            case WINDOWS:
                path = Paths.get(System.getenv("APPDATA"));
                break;
            case MAC:
                path = Paths.get(System.getProperty("user.home"), "/Library/Application Support/");
                break;
            case LINUX:
                path = Paths.get(System.getProperty("user.home"), ".local/share");
                break;
            default:
                path = Paths.get(System.getProperty("user.home"));
        };

        if(fileName == null || fileName.equals(""))
            return Paths.get(path.toString(), ".eurolauncher").toFile();

        path = Paths.get(path.toString(), ".eurolauncher", fileName);
        return path.toFile();
    }
}