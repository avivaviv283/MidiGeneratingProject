package com.example.midigeneratorproject.controller;

import com.example.midigeneratorproject.entity.MidiFile;
import com.example.midigeneratorproject.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MidiController {
    private FileStorageService fileStorageService;

    @Autowired
    public MidiController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/index", "/"})
    public String getIndex() {
        return "index";
    }


    //When I enter this link or press a button that redirects me to this link return the add_midifile HTML page
    @GetMapping("/AddMidifiles")
    public String addNewMidiFile(Model model) {
        MidiFile midiFile = new MidiFile();
        model.addAttribute("midifile", midiFile);
        return "add_midifile";
    }
    @PostMapping("/midifiles")
    public String saveMidiFile(@ModelAttribute("midifile") MidiFile midiFile,
                               @RequestParam("multiPartFile")MultipartFile file) {
        fileStorageService.saveMidiFile(midiFile, file);
        return "redirect:/";

    }
}
