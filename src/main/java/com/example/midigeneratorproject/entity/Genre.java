package com.example.midigeneratorproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
public class Genre {
    @Id
    //This annotation provides a unique ID to every file in the database.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String genreName;

    @OneToMany(mappedBy = "genre")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MidiFile> midiFileList;
 }
