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
import java.util.Optional;

@Controller
public class MidiController {
    private FileStorageService fileStorageService;
    List<Genre> genres;


    @Autowired
    public MidiController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.genres = fileStorageService.getAllGenres();
    }

//    @RequestMapping(method = RequestMethod.GET, value = {"/index", "/"})
//    public String getIndex() {
//        return "registration";
//    }


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
    @GetMapping("/index")
    public String IndexPage( ) {
        return "index";
    }


    @PostMapping("/midifiles")
    public String saveMidiFile(@ModelAttribute("midifile") MidiFile midiFile,
                               @RequestParam("dropOperator") Long genreId,
                               @RequestParam("multiPartFile") MultipartFile file) {
        Genre requestedGenre = fileStorageService.findById(genreId);
        midiFile.setGenre(requestedGenre);

        fileStorageService.saveMidiFile(midiFile, file);

        return "redirect:/";
    }


    @PostMapping("/generate")
    public String generateNewFile(@ModelAttribute("genre") Genre genre){
        LSTM musicModel = new LSTM(genre.getId());
        return "generate_page";
    }


}
