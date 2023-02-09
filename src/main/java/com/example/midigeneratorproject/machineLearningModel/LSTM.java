package com.example.midigeneratorproject.machineLearningModel;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Setter
@Getter
public class LSTM {
    static long id;

    public LSTM(long id){
        this.id=id;
        System.out.println("Reached function with id:"+ id);
        startModel(id);
    }

    private void startModel(long id) {
        String fetching = "python " + "C:\\Users\\WIN10\\Desktop\\modeltest.py";
        String[] commandToExecute = new String[]{"cmd.exe", "/c", fetching , Long.toString(id)};

        try {
            System.out.println("before opening file with id:"+ id);
            Runtime.getRuntime().exec(commandToExecute);
            System.out.println("after opening file with id:"+ id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
