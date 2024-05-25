package com.graduateProject.backend_framework.controller;


import com.graduateProject.backend_framework.model.postCommentsDTO;
import com.graduateProject.backend_framework.service.CommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class commentController {
  private CommentsService commentsservice;

    public commentController(CommentsService commentsservice) {
        this.commentsservice = commentsservice;
    }

    @PostMapping("/saveComment")
  public String addComment(@RequestParam("user_id") Long user_id,
                             @RequestParam("post_id") Long post_id,
                             @RequestParam("comment_text") String comment_text,
                             @RequestParam("timestamp") String timestamp
                             ){
    try{
        commentsservice.saveComment(user_id,post_id,comment_text,timestamp);
        return "comment is saved";
    }catch(Exception e){
        e.printStackTrace();
        return "comment could not be saved";
      }
  }

  @GetMapping("/getComments/{post_id}")
    public ResponseEntity<?> getComments(@PathVariable Long post_id){
      List<postCommentsDTO> postComments=commentsservice.findingAllComments(post_id);
      return ResponseEntity.ok(postComments);
  }
}
