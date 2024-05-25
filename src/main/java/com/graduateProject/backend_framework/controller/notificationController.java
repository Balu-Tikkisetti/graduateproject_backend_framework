package com.graduateProject.backend_framework.controller;


import com.graduateProject.backend_framework.model.NotificationDTO;
import com.graduateProject.backend_framework.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class notificationController {
    private NotificationsService notificationsService;

    @Autowired

    public notificationController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestParam("post_id")Long post_id,
                                              @RequestParam("user_id")Long user_id,
                                              @RequestParam("timestamp")String timestamp,
                                              @RequestParam("recieving_user_id")Long recieving_user_id){
        try{
            String response=notificationsService.notificationSaveService(post_id,recieving_user_id,user_id,timestamp);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getNotifications/{user_id}")
    public ResponseEntity<?> getNotifications(@PathVariable Long user_id){

        try{
            List<NotificationDTO>notificationDTOS=notificationsService.getNotificationsService(user_id);
            return ResponseEntity.ok(notificationDTOS);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }
}
