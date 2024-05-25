package com.graduateProject.backend_framework.controller;

import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.repository.UserRepository;
import com.graduateProject.backend_framework.service.GeneratingPostsService;
import com.graduateProject.backend_framework.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.0.92:8080"})
public class UserController {

    private final UserService userService;
    private final GeneratingPostsService gpService;

    @Autowired
    public UserController(UserService userService, GeneratingPostsService gpService) {
        this.userService = userService;
        this.gpService = gpService;
    }



    @PostMapping("/signup")
    public ResponseEntity<Long> addUser(@RequestBody Users users) {
        // Save the user to the database
        userService.saveUser(users);
        return ResponseEntity.ok(users.getUser_id());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users loginCredentials) throws SQLException, IOException {
        Users user = userService.getUserByEmail(loginCredentials.getGmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        // Check if the retrieved user's password matches the provided password
        if (!user.getPassword().equals(loginCredentials.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }
        // Login successful, return the user_id
        if (user.getUser_id() != null && user.getUser_id().equals(9L)) {
            gpService.postGenerator();
        }

        return ResponseEntity.ok(user.getUser_id());
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("Invalid user_id");
        }
        Optional<Users> user = userService.getUserById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @PostMapping("/updateProfile")
    public String uploadDp(@RequestParam(value = "file",required = false) MultipartFile file,
                           @RequestParam("user_id") Long user_id,
                           @RequestParam("first_name") String first_name,
                           @RequestParam("last_name") String last_name,
                           @RequestParam("username") String username,
                           @RequestParam("gmail") String gmail,
                           @RequestParam("dob") String dob,
                           @RequestParam("gender") String gender){
        try {
                String response=userService.updateProfileservice(file,user_id,first_name,last_name,username,gmail
                ,dob,gender);
                return response;
        }catch(Exception e){
            e.printStackTrace();
            return "profile picture is not updated";
        }

    }




    @GetMapping("/getDp/{userId}")
    public ResponseEntity<?> getDp(@PathVariable Long userId){
        try{
            String profile_pic=userService.getDpservice(userId);
            return ResponseEntity.ok(profile_pic);
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    @GetMapping("/searchUsers/{searchUserWord}")
    public ResponseEntity<?> getSearchUsers(@PathVariable String searchUserWord){

        try{
            List<UserService.SearchUserView> users=userService.getUserSearchService(searchUserWord);
            return ResponseEntity.ok(users);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }



}
