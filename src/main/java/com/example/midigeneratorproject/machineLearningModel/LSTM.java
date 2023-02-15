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
        startModel(id);
    }

    private void startModel(long id) {
        // change path when running from laptop
        String s = System.getProperty("user.home");
        System.out.println(s);
        //From pc run this:
        //String fetching = "python " + "C:\\Users\\WIN10\\Desktop\\modeltest.py";
        //From laptop run this:
        String fetching = "python " + "C:\\Users\\IMOE001\\IdeaProjects\\MidiGeneratorProject\\modeltest.py";
        String[] commandToExecute = new String[]{"cmd.exe", "/c", fetching , Long.toString(id)};

        try {
            Runtime.getRuntime().exec(commandToExecute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
