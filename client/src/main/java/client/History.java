package client;

import javafx.scene.shape.Path;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class History {
    private static PrintWriter printWriter;

    private static String getHistoryByLogin(String login) {
        return "chatHistory/history_" + login + ".txt";
    }
    public static void start(String login) {
        try {
            printWriter = new PrintWriter(new FileOutputStream(getHistoryByLogin(login), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void tearsDown() {
        if (printWriter != null) {
            printWriter.close();
        }
    }
    public static void writeLine(String message) {
        printWriter.println(message);
    }
    public static String get100Messages(String login) {
        if (!Files.exists(Paths.get(getHistoryByLogin(login)))) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(getHistoryByLogin(login)));
            int begin = 0;
            if (lines.size() > 100) {
                begin = lines.size() - 100;
            }
            for (int i = begin; i < lines.size(); i++) {
                stringBuilder.append(lines.get(i)).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
