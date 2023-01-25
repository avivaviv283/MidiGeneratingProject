package com.example.midigeneratorproject.service;

import com.example.midigeneratorproject.entity.MidiFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileStorageInterface {

    public MidiFile saveMidiFile(MidiFile midiFile, MultipartFile file);

    public Optional<MidiFile> getAllFilesList (Long fileId);
}
