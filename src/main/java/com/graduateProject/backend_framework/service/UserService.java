package com.graduateProject.backend_framework.service;

import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public static class SearchUserView{
        private Long user_id;
        private String username;

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        private  String profile_pic;
    }




    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(Users users) {

        userRepository.save(users);
    }

    public Users getUserByEmail(String gmail){
        return  userRepository.findByGmail(gmail);
    }
    public List<Users> getAllUsers(){

        List<Users> users=userRepository.findAll();
        return  users;
    }

    @Transactional(rollbackFor = {IOException.class, SQLException.class})
    public String updateProfileservice(MultipartFile file, Long userId, String firstName, String lastName, String username,
                                       String email, String dob, String gender) {
        try {
            Optional<Users> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                if(file!=null) {
                    Blob blob = new SerialBlob(file.getBytes());
                    user.setProfile_pic(blob);
                }
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setUsername(username);
                user.setDob(dob);
                user.setGmail(email);
                user.setGender(gender);
                userRepository.save(user);
                return "Profile updated successfully!";
            } else {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
        } catch (IOException ex) {
            return "Error occurred while processing the file: " + ex.getMessage();
        } catch (SQLException ex) {
            return "SQL Exception occurred: " + ex.getMessage();
        }
    }

    public String getDpservice(Long userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            Blob blob = user.getProfile_pic();
            if (blob != null) {
                try {
                    // Read the bytes from the Blob
                    byte[] bytes = blob.getBytes(1, (int) blob.length());
                    // Convert bytes to Base64
                    String base64Image = Base64.getEncoder().encodeToString(bytes);
                    return base64Image;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Optional<Users> getUserById(Long user_id){
        return  userRepository.findById(Long.valueOf(user_id));
    }


    public List<SearchUserView> getUserSearchService(String keyWord) throws SQLException {
        List<Users>allusers=userRepository.findAll();
        List<SearchUserView>matchingusers=new ArrayList<>();


        for(Users user:allusers){
            if(user.getUsername().contains(keyWord)){
                SearchUserView suv=new SearchUserView();
                suv.setUser_id(user.getUser_id());
                suv.setUsername(user.getUsername());
                Blob blob=user.getProfile_pic();
                if(blob!=null){
                    byte[] bytes = blob.getBytes(1, (int) blob.length());
                    // Convert bytes to Base64
                    String base64Image = Base64.getEncoder().encodeToString(bytes);
                    suv.setProfile_pic(base64Image);
                }

                matchingusers.add(suv);
            }
        }

        return matchingusers;
    }

}
