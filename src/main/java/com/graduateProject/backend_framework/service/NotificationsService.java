package com.graduateProject.backend_framework.service;


import com.graduateProject.backend_framework.model.NotificationDTO;
import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.model.notifications;
import com.graduateProject.backend_framework.model.posts;
import com.graduateProject.backend_framework.repository.PostRepository;
import com.graduateProject.backend_framework.repository.UserRepository;
import com.graduateProject.backend_framework.repository.notificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationsService {
    private notificationsRepository notificationRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private FirebaseService firebaseService;

    @Autowired

    public NotificationsService(notificationsRepository notificationRepository, PostRepository postRepository, UserRepository userRepository, FirebaseService firebaseService) {
        this.notificationRepository = notificationRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.firebaseService=firebaseService;
    }

    public String notificationSaveService(Long post_id, Long user_id, Long sent_user_id, String timestamp){
        Optional<posts> optionalpost=postRepository.findById(post_id);
        Optional<Users> optionaluser=userRepository.findById(user_id);
        Optional<Users>optionalSentUser=userRepository.findById(sent_user_id);
        if(optionalpost.isPresent()&&optionaluser.isPresent()&&optionalSentUser.isPresent()){
            notifications notification=new notifications();
            notification.setPosts(optionalpost.get());
            notification.setUser(optionaluser.get());
            notification.setSent_user(optionalSentUser.get());
            notification.setTimestamp(timestamp);
            notificationRepository.save(notification);
        }
        return null;
    }

    public List<NotificationDTO> getNotificationsService(Long user_id) throws IOException, SQLException {
        Optional<Users>optionaluser=userRepository.findById(user_id);
        List<notifications>allnotifications=notificationRepository.findAll();
        List<NotificationDTO>allnDTOS=new ArrayList<>();
        if(optionaluser.isPresent()){
             for(notifications notification:allnotifications){
                 if(notification.getUser().getUser_id()==user_id){
                     NotificationDTO nDTO=new NotificationDTO();
                     FirebaseService.FileData file = null;
                     if (notification.getPosts().getPost_pic_url() != null) {
                         file = firebaseService.getFileFromFirebaseStorage(notification.getPosts().getPost_pic_url());
                     } else if (notification.getPosts().getPost_video_url() != null) {
                         file = firebaseService.getFileFromFirebaseStorage(notification.getPosts().getPost_video_url());
                     }
                     nDTO.setPost_image(file.getContent());
                     nDTO.setUsername(notification.getSent_user().getUsername());
                     nDTO.setPost_id(notification.getPosts().getPost_id());
                     nDTO.setUser_id(notification.getSent_user().getUser_id());
                     Blob blob=notification.getSent_user().getProfile_pic();
                     if(blob!=null){
                         byte[] bytes = blob.getBytes(1, (int) blob.length());
                         // Convert bytes to Base64
                         String base64Image = Base64.getEncoder().encodeToString(bytes);
                         nDTO.setProfile_pic(base64Image);
                     }

                     allnDTOS.add(nDTO);
                 }
             }
        }


        return allnDTOS;
    }
}
