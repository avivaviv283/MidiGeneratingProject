package com.example.midigeneratorproject.machineLearningModel;

import java.io.IOException;

public class LSTM {




    public static void main(String[] args) {
        String fetching = "python " + "C:\\Users\\WIN10\\Desktop\\test.py";
        String[] commandToExecute = new String[]{"cmd.exe", "/c", fetching};
        try {
            Runtime.getRuntime().exec(commandToExecute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
