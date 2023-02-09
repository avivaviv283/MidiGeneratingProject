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

import java.util.List;

@Controller
public class MidiController {
    private FileStorageService fileStorageService;
    List<Genre> genres;


    @Autowired
    public MidiController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.genres = fileStorageService.getAllGenres();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/index", "/"})
    public String getIndex() {
        return "index";
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
        model.addAttribute("genre",genre);
        model.addAttribute("genres", genres);
        return "generate_page";
    }


    @PostMapping("/midifiles")
    public String saveMidiFile(@ModelAttribute("midifile") MidiFile midiFile,
                               @RequestParam("multiPartFile") MultipartFile file) {
        fileStorageService.saveMidiFile(midiFile, file);
        return "redirect:/";

    }

    @PostMapping("/generate")
    public String generateNewFile(@ModelAttribute("genre") Genre genre){
        LSTM musicModel = new LSTM(genre.getId());
        return "generate_page";
    }


}
