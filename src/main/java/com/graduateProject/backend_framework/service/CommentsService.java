package com.graduateProject.backend_framework.service;

import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.model.comments;
import com.graduateProject.backend_framework.model.postCommentsDTO;
import com.graduateProject.backend_framework.model.posts;
import com.graduateProject.backend_framework.repository.PostRepository;
import com.graduateProject.backend_framework.repository.UserRepository;
import com.graduateProject.backend_framework.repository.commentsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {

    public commentsRepository cRepo;
    public UserRepository usersRepository;
    public PostRepository postRepository;

    public CommentsService(commentsRepository cRepo, UserRepository usersRepository, PostRepository postRepository) {
        this.cRepo = cRepo;
        this.usersRepository = usersRepository;
        this.postRepository = postRepository;
    }


    public void saveComment(Long user_id, Long post_id, String comment_text,String timestamp){
        Optional<Users> optionalUser = usersRepository.findById(user_id);
        Optional<posts> optionalPost=postRepository.findById(post_id);
        comments comment=new comments();
        if(optionalUser.isPresent()&&optionalPost.isPresent()){
            comment.setComment_text(comment_text);
            comment.setTimestamp(timestamp);
            comment.setPosts(optionalPost.get());
            comment.setUser(optionalUser.get());
            posts post=optionalPost.get();
            int count=post.getComments_count();
            count++;
            post.setComments_count(count);
            postRepository.save(post);
            cRepo.save(comment);
        }

    }

    public List<postCommentsDTO> findingAllComments(Long post_id){
        Optional<posts> optionalPost=postRepository.findById(post_id);
        List<Users> users=usersRepository.findAll();
        List<comments> allcomments=cRepo.findAll();

        List<postCommentsDTO> postComments=new ArrayList<>();
        for(comments comment:allcomments){
            if(comment.getPosts().getPost_id()==post_id){
                for(Users user:users){
                    if(user.getUser_id()==comment.getUser().getUser_id()){
                       postCommentsDTO pcommentdto=new postCommentsDTO();
                       pcommentdto.setComment_text(comment.getComment_text());
                       pcommentdto.setUsername(user.getUsername());
                       pcommentdto.setTimestamp(comment.getTimestamp());
                       pcommentdto.setUser_id(user.getUser_id());
                       pcommentdto.setPost_id(post_id);
                       postComments.add(pcommentdto);
                    }
                }
            }
        }

        return postComments;


    }
}
