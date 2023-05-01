package com.example.midigeneratorproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class MidiFile {
    @Id
    //This annotation provides a unique ID to every file in the database.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    @Column(nullable = false)
    //the Lob annotation allows to store a byte array (such as a file) in a sql database
    @Lob
    private Blob file;

    @ManyToOne
    @JoinColumn(name = "genreID", nullable = false,
    foreignKey = @ForeignKey(name = "FK_Genre_ID"))
    private Genre genre;


}
