package com.example.midigeneratorproject.controller;

import com.example.midigeneratorproject.dto.UserDto;
import com.example.midigeneratorproject.entity.User;
import com.example.midigeneratorproject.entity.UserRole;
import com.example.midigeneratorproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = {"/", "/home"})
    public String getIndex(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "registration";
    }

   @GetMapping("/registration")
    public String getRegistration(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "registration";
    }


    @GetMapping("/UserList")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user_list";
    }

    // see - https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @RequestMapping("/index/")
    public String loginFormUser() {
        return "index";
    }



    /*
    FROM registration.html
    */
    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result, Model model) {

        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null) {
            result.rejectValue("email", null,
                    "User already registered !!!");
            System.out.println("Existing user reached");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            System.out.println("result has errors reached");
            return "/registration";
        }
        //Default for everyone, admin set manually
        userDto.setUserRole(UserRole.USER);
        System.out.println("saving user reached");

        userService.saveUser(userDto);
        return "index";
    }

}
