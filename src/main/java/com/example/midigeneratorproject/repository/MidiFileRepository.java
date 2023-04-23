package com.example.midigeneratorproject.repository;

import com.example.midigeneratorproject.entity.MidiFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidiFileRepository extends JpaRepository<MidiFile, Long> {
    MidiFile findByFileName(String name);
}
