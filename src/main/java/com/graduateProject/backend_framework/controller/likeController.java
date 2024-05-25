package com.graduateProject.backend_framework.controller;


import com.graduateProject.backend_framework.service.LikeService;
import com.graduateProject.backend_framework.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class likeController {
    private LikeService likeService;

    @Autowired

    public likeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like/{post_id}/{user_id}")
    public ResponseEntity<?> liking(@PathVariable Long post_id,@PathVariable Long user_id){
        try{
             String response = likeService.updateLikeService(post_id, user_id);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/unlike/{post_id}/{user_id}")
    public ResponseEntity<?>unliking(@PathVariable Long post_id, @PathVariable Long user_id){
        try{
            String response=likeService.deleteLikeService(post_id,user_id);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/likedPosts/{user_id}")
    public ResponseEntity<?> likedPosts(@PathVariable Long user_id){

        try{
            List<Long>response=likeService.likedPostsService(user_id);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
