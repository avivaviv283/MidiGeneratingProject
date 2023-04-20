package com.example.midigeneratorproject.service;

import com.example.midigeneratorproject.dto.UserDto;
import com.example.midigeneratorproject.entity.Role;
import com.example.midigeneratorproject.entity.User;
import com.example.midigeneratorproject.entity.UserRole;
import com.example.midigeneratorproject.repository.RoleRepository;
import com.example.midigeneratorproject.repository.UserRepository;
import com.example.midigeneratorproject.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    The "saveUser()" method converts "UserDto" to "User" and saves it in the database (MySql).
     */
    @Override
    public void saveUser(UserDto userDto) {
        Role role = null;
        if (userDto.getUserRole() == UserRole.USER) {
            role = roleRepository.findByName(Constants.Roles.USER);
            if (role == null)
                role = roleRepository.save(new Role(Constants.Roles.USER));
        }
        else if (userDto.getUserRole() == UserRole.ADMIN) {
            role = roleRepository.findByName(Constants.Roles.ADMIN);
            if (role == null)
                role = roleRepository.save(new Role(Constants.Roles.ADMIN));
        }


        User user = new User(userDto.getFirstName() + " " + userDto.getLastName()
                , userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),role);
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        // this is a function in UserRepository class, see in the class
        // use custom Query !!!
        List<String> usersWithRoles = userRepository.getRoles();

        List<UserDto> userDtoList = new ArrayList<>();
        for (int i = 0; i < usersWithRoles.size(); i++) {
            UserDto userDto = new UserDto();
            String[] str = usersWithRoles.get(i).split(" ");
            userDto.setFirstName(str[0]);
            userDto.setLastName(str[1]);
            userDto.setEmail(str[2]);
            if (str[3].equals("ROLE_ADMIN"))
                userDto.setUserRole(UserRole.ADMIN);
            else
                userDto.setUserRole(UserRole.USER);
            userDtoList.add(userDto);

        }

        return userDtoList;
    }

    public List<String> getRoleList() {
        return roleRepository.getRoleList();
    }
}

