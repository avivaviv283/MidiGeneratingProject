package com.example.midigeneratorproject.repository;

import com.example.midigeneratorproject.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenereRepository extends JpaRepository<Genre, Long> {
}
