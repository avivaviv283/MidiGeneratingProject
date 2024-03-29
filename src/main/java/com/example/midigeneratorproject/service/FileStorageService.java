package com.example.midigeneratorproject.service;

import com.example.midigeneratorproject.entity.Genre;
import com.example.midigeneratorproject.entity.MidiFile;
import com.example.midigeneratorproject.repository.GenreRepository;
import com.example.midigeneratorproject.repository.MidiFileRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class FileStorageService implements FileStorageInterface {
    @Autowired
    private MidiFileRepository fileRepository;
    @Autowired
    private GenreRepository genreRepository;


    //This service is used to insert a new row into the database.
    @Override
    public MidiFile saveMidiFile(MidiFile midiFile, MultipartFile file) {
        MidiFile temp = new MidiFile();
        temp.setGenre(midiFile.getGenre());
        temp.setFileName(file.getOriginalFilename());

        // Make sure uploaded file is a midi file
        if (file.getContentType() == "audio/mid" || file.getContentType() == "audio/midi") {
            System.out.println(file.getContentType());
            try {

                makeBlobForDatabase(temp, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MidiFile duplicate = fileRepository.findByFileName(temp.getFileName());
        if (duplicate == null) {
            return fileRepository.save(temp);
        } else return null;
    }

    public List<MidiFile> getFiles() {
        return fileRepository.findAll();
    }

    public void deleteFileByName(String name) {
        MidiFile file = findMidiFileByName(name);
        if (file != null) {
            fileRepository.delete(file);
        }
    }

    public MidiFile findMidiFileByName(String name) {
        return fileRepository.findByFileName(name);
    }

    //This service is able to translate a file into a Byte array. we can use the blob field in the database to store the file
    private static void makeBlobForDatabase(MidiFile temp, MultipartFile file) {
        byte[] imageData;
        try {
            imageData = file.getBytes();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Blob blob;
        try {
            blob = new SerialBlob(imageData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        temp.setFile(blob);
    }

    @Override
    public Optional<MidiFile> getAllFilesList(Long fileId) {
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre findById(Long id) {
        Genre returnedGenre = genreRepository.findById(id).orElse(null);
        return returnedGenre;
    }
}
