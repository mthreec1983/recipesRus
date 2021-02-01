package org.launchcode.recipeapp.controllers;

import com.sun.xml.bind.util.AttributesImpl;
import org.launchcode.recipeapp.models.data.UserRepository;
import org.launchcode.recipeapp.models.dto.LoginFormDTO;
import org.launchcode.recipeapp.models.User;
import org.launchcode.recipeapp.models.dto.RegistrationFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class
AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(userSessionKey);
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user);
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegistrationFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(HttpSession session, @ModelAttribute @Valid RegistrationFormDTO registrationFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        User existingUser = userRepository.findByUsername(registrationFormDTO.getUsername());
        String access = registrationFormDTO.getAccess();

        if (existingUser != null) {
            errors.rejectValue("username", "username.already exists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registrationFormDTO.getPassword();
        String verifyPassword = registrationFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        registrationFormDTO.setAccess("2");
        User newUser = new User(registrationFormDTO.getUsername(), registrationFormDTO.getPassword(), registrationFormDTO.getAccess());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        if (registrationFormDTO.getAccess().equals("2")) {
            return "redirect:/login";
        }
        return "redirect:/register";
    }

    @GetMapping("/adminregister")
    public String displayRegisterAdminForm(Model model) {
        model.addAttribute(new RegistrationFormDTO());
        model.addAttribute("title", "Register");
        return "adminregister";
    }

    @PostMapping("/adminregister")
    public String processRegisterAdminForm(HttpSession session, @ModelAttribute @Valid RegistrationFormDTO registrationFormDTO,
                                           Errors errors, HttpServletRequest request,
                                           Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "/adminregister";
        }

        User existingUser = userRepository.findByUsername(registrationFormDTO.getUsername());
        String access = registrationFormDTO.getAccess();

        if (existingUser != null) {
            errors.rejectValue("username", "username.already exists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "/adminregister";

        }

        registrationFormDTO.setAccess("1");
        User newUser = new User(registrationFormDTO.getUsername(), registrationFormDTO.getPassword(), registrationFormDTO.getAccess());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);


        return "redirect:/admin";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(HttpSession session, @ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }


        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());
        String password = loginFormDTO.getPassword();

        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }

        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }

        session.setAttribute("userId", theUser.getId());
        setUserInSession(request.getSession(), theUser);


        if (theUser.getAccess().equals("1")) {
            return "redirect:admin";
        }

        if (theUser.getAccess().equals("2")){
            return "redirect:users/profile";

        } else {
        return "redirect:/home";
    }

}

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }

}
