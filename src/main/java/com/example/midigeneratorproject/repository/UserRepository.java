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
    @Query(value = "select  CONCAT(database1.users.name,' '" +
            " ,database1.users.email,' '," +
            " database1.roles.name)\n" +
            "from roles,\n" +
            "     users,\n" +
            "     users_roles\n" +
            "where roles.id = users_roles.role_id\n" +
            "  and users.id = users_roles.user_id", nativeQuery = true)
    List<String> getRoles();

}