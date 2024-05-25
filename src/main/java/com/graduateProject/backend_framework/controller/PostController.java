package com.graduateProject.backend_framework.controller;

import com.graduateProject.backend_framework.model.EditPostDTO;
import com.graduateProject.backend_framework.model.SubscriberPostsDTO;
import com.graduateProject.backend_framework.model.UserPostDTO;
import com.graduateProject.backend_framework.model.showPostDTO;
import com.graduateProject.backend_framework.service.PostService;
import com.graduateProject.backend_framework.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {
    private PostService postservice;

    @Autowired
    public PostController(PostService postservice){this.postservice=postservice;}




    @PostMapping("/postings")
    public String addPost(@RequestParam("user_id") Long userId,
                          @RequestParam("hashtags") String hashtags,
                          @RequestParam("description") String description,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("timestamp") String timestamp) {
        try {
            postservice.savePost(userId, hashtags, description, file,timestamp);
            return "Post is added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Post could not be added";
        }
    }

    @GetMapping("/getPosts/{user_id}")
    public ResponseEntity<?> gettingUserPosts(@PathVariable Long user_id) throws IOException, SQLException {
        List<UserPostDTO> userPostDTOs = postservice.findPostByUserId(user_id);

        return ResponseEntity.ok(userPostDTOs);
    }

    @GetMapping("/getpost/{post_id}")
    public ResponseEntity<?> getEditPostDetails(@PathVariable Long post_id) throws IOException {
        try {
            EditPostDTO post = postservice.getEditPostService(post_id);

            return ResponseEntity.ok(post);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/updatepost")
    public String updatePost(@RequestParam("post_id") Long post_id,
                             @RequestParam("post_text") String post_text,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("hashtags") String hashtags,
                             @RequestParam("changed_on") String changed_on) {
        try{
           postservice.updatePostService(post_id,post_text,file,hashtags,changed_on);
           return "Succesfully updated";
        }catch(Exception e){
            e.printStackTrace();
        }
        return "failed to update";
    }

    @GetMapping("/searchPosts/{searchPostWord}")
    public ResponseEntity<?> getSearchPost(@PathVariable String searchPostWord){

        try{
           List<showPostDTO> showposts=postservice.getSearchPostsService(searchPostWord);
           return ResponseEntity.ok(showposts);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/subscribedPosts/{user_id}")
    public ResponseEntity<?> getSubscribedPosts(@PathVariable Long user_id){

        try{
       List<SubscriberPostsDTO> allspDTO=postservice.getSubscribedPostsService(user_id);
       return ResponseEntity.ok(allspDTO);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getRandomPosts/{user_id}")
    public ResponseEntity<?> getRandomPosts(@PathVariable Long user_id){
        try{
          List<SubscriberPostsDTO>allspDTO= postservice.getRandomPostsService(user_id);
          return ResponseEntity.ok(allspDTO);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }


    @DeleteMapping("/deletePost/{post_id}")
    public ResponseEntity<?>deletePost(@PathVariable Long post_id){
        try{
            String response=postservice.deletePostService(post_id);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


}
