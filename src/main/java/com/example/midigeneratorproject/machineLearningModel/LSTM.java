package com.example.midigeneratorproject.machineLearningModel;

import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Setter
@Getter
public class LSTM {

    Thread t;
    String output;
    static long id;

    public LSTM(long id) {
        this.id = id;
        startModel(id);
    }

    //Starts the thread that runs the prediction script
    private void startModel(long id) {
        t = new Thread() {
            public void run() {
                //Retrieves the string path of the root directory
                String s = System.getProperty("user.home");
                //From pc run this:
                //String fetching = "python " + s + "\\Desktop\\predict.py";

                //From laptop run this:
                String fetching = "python " + s + "\\IdeaProjects\\MidiGeneratorProject\\predict.py";

                System.out.println("Before executing file");

                ProcessBuilder pb = new ProcessBuilder();
                pb.command("cmd.exe", "/c", fetching + " " + id);

                try {
                    Process p = pb.start();
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;


                    while ((line = inputReader.readLine()) != null) {
                        output += line + "<br>";
                    }
                    System.out.println(output);
                    int exitCode = p.waitFor();
                    System.out.println(exitCode);
                } catch (IOException e) {

                    throw new RuntimeException(e);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        };

        t.start();
    }


}
