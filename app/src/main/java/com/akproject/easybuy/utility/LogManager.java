package com.akproject.easybuy.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Allan on 22/1/2016.
 */
public class LogManager {
    
    public static void logFile(String message) {
        //String path = "E:\\htdocs\\AKProject\\AKTest\\log\\log.txt";
        File storageFile = FileManager.getStorageFile(String.format("log_%s.csv", "1"));
        if (storageFile != null) {
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(storageFile));
                output.write(FormatManager.displayCurrentDateTime() + "\t" + message);
                output.newLine();
                output.close();
            } catch (FileNotFoundException e) {
                logSystem("File " + " is not found!");
            } catch (IOException e) {
                logSystem("IOException on ");
            }
        }
    }
    
    public static void logSystem(String message) {
        System.out.println(FormatManager.displayCurrentDateTime() + "\t" + "~~~~~" + message + "~~~~~");
        //logFile(message);
    }

}
