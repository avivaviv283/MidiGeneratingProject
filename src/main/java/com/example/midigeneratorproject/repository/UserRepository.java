package com.example.midigeneratorproject.repository;

import com.example.midigeneratorproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /*
    For the purpose of retrieving a user associated with an email,
    we will create a DAO (Data Access Object) class using Spring Data by extending the JpaRepository interface
     */
    User findByEmail(String email);


    /*
    We have tell Spring to treat that query as native one --> ** nativeQuery = true **.
    Otherwise, it will try to validate it according to the JPA specification.
     */
    @Query(value = "SELECT CONCAT(users.name, ' ', users.email, ' ', roles.name) " +
            "FROM users " +
            "INNER JOIN roles ON users.role = roles.id", nativeQuery = true)
    List<String> getRoles();

}