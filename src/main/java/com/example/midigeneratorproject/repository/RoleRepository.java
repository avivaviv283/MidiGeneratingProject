package com.example.midigeneratorproject.repository;

import com.example.midigeneratorproject.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    /*
        The "findByName" method gets the "Role" object for the associated "role" name.
         */
    Role findByName(String name);

    @Query(value = "select name from roles", nativeQuery = true)
    List<String> getRoleList();
}
