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

    @RequestMapping(method = RequestMethod.GET, value = {"/", "/index", "/home"})
    public String getIndex() {
        return "index";
    }


    @GetMapping("/getUsers")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }

    // see - https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @RequestMapping("/user/")
    public String loginFormUser() {
        return "user";
    }

    /*
    TO registration.html
     */

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        model.addAttribute("rolesList", userService.getRoleList());
        return "registration";
    }

    /*
    FROM registration.html
    */
    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result, Model model) {

        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User already registered !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/registration";
        }

        if (userDto.getUserRoleStr().equals("ROLE_ADMIN"))
            userDto.setUserRole(UserRole.ADMIN);
        else if (userDto.getUserRoleStr().equals("ROLE_USER"))
            userDto.setUserRole(UserRole.USER);

        userService.saveUser(userDto);
        return "redirect:/registration?success";
    }

    @GetMapping("/user_home_page")
    public String UserHomePage(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "user_page";
    }


}
