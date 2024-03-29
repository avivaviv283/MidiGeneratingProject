package com.example.midigeneratorproject.controller;

import com.example.midigeneratorproject.entity.Genre;
import com.example.midigeneratorproject.entity.MidiFile;
import com.example.midigeneratorproject.machineLearningModel.LSTM;
import com.example.midigeneratorproject.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Controller
public class MidiController {
    private FileStorageService fileStorageService;
    List<Genre> genres;
    LSTM musicModel;


    @Autowired
    public MidiController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.genres = fileStorageService.getAllGenres();
    }


    //When I enter this link or press a button that redirects me to this link return the add_midifile HTML page
    @GetMapping("/AddMidifiles")
    public String addNewMidiFile(Model model) {
        MidiFile midiFile = new MidiFile();
        Genre genre = new Genre();
        model.addAttribute("midifile", midiFile);
        model.addAttribute("genre", genre);
        model.addAttribute("genres", genres);
        return "add_midifile";
    }

    @GetMapping("/Generator")
    public String GeneratorPage(Model model) {
        Genre genre = new Genre();
        model.addAttribute("genre", genre);
        model.addAttribute("genres", genres);
        return "generate_page";
    }

    @GetMapping("/index")
    public String IndexPage() {
        return "index";
    }


    @PostMapping("/midifiles")
    public String saveMidiFile(@ModelAttribute("midifile") MidiFile midiFile,
                               @RequestParam("dropOperator") Long genreId,
                               @RequestParam("multiPartFile") MultipartFile file) {
        Genre requestedGenre = fileStorageService.findById(genreId);
        midiFile.setGenre(requestedGenre);

        fileStorageService.saveMidiFile(midiFile, file);

        return "redirect:/index";
    }

    @GetMapping("/getConsole")
    @ResponseBody
    public String getConsole() {
        if (musicModel != null) {
            return musicModel.getOutput();
        }
        return "";
    }

    @PostMapping("/generate")
    public String generateNewFile(@ModelAttribute("genre") Genre genre, Model model) throws SQLException, IOException, MidiUnavailableException, InvalidMidiDataException, LineUnavailableException {
        //Need to delete previous file from database, so it won't be the one showcased to the user before the prediction process has ended
        fileStorageService.deleteFileByName("output_file" + genre.getId());
        //Creating a LSTM instance runs python predict script
        musicModel = new LSTM(genre.getId());

        while (musicModel.getT().isAlive()) {

            //System.out.println(musicModel.getOutput());
        }
        MidiFile output = fileStorageService.findMidiFileByName("output_file" + genre.getId());

        Blob midiBlob = output.getFile();

        byte[] midiBytes = midiBlob.getBytes(1, (int) midiBlob.length());
        String base64String = Base64.getEncoder().encodeToString(midiBytes);
        String mimeType = "audio/midi";
        String base64Url = "data:" + mimeType + ";base64," + base64String;
        System.out.println(base64Url);

        model.addAttribute("midiURL", base64Url);

        return "generate_page";
    }


}
