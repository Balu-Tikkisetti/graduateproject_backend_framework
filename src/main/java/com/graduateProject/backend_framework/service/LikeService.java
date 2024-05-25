package com.graduateProject.backend_framework.service;


import com.graduateProject.backend_framework.model.Likes;
import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.model.posts;
import com.graduateProject.backend_framework.repository.LikesRepository;
import com.graduateProject.backend_framework.repository.PostRepository;
import com.graduateProject.backend_framework.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    private LikesRepository likesRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public LikeService(LikesRepository likesRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likesRepository = likesRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public String updateLikeService(Long post_id, Long user_id){
        Likes like=new Likes();
        Optional<posts> optionalpost=postRepository.findById(post_id);
        Optional<Users> optionaluser=userRepository.findById(user_id);
        if(optionaluser.isPresent()&&optionalpost.isPresent()){
            like.setPost(optionalpost.get());
            like.setUser(optionaluser.get());
            posts post =optionalpost.get();
//
            if (post.getLikes_count() == 0) {
                post.setLikes_count(1);
            } else {
                int likesCount = post.getLikes_count();
                likesCount++;
                post.setLikes_count(likesCount);
            }

//
            postRepository.save(post);
            likesRepository.save(like);
            return "Succesfully done";
        }
        return null;
    }

    public String deleteLikeService(Long post_id, Long user_id){
        Optional<posts> optionalpost=postRepository.findById(post_id);
        Optional<Users> optionaluser=userRepository.findById(user_id);
        if(optionaluser.isPresent()&&optionalpost.isPresent()){
            List<Likes>alllikes=likesRepository.findAll();
            for(Likes like:alllikes){
                if(like.getPost().getPost_id()==post_id&&like.getUser().getUser_id()==user_id){
                    likesRepository.deleteById(like.getLike_id());
                    posts post=optionalpost.get();
                    int l=post.getLikes_count();
                    l--;
                    post.setLikes_count(l);
                    postRepository.save(post);
                    return "Succesfully unliked";
                }
            }
        }
        return null;

    }


    public List<Long> likedPostsService( Long user_id){
        List<Likes>allLikes=likesRepository.findAll();
        List<Long>allPostsId=new ArrayList<>();
        for(Likes like:allLikes){
            if(like.getUser().getUser_id()==user_id){
                allPostsId.add(like.getPost().getPost_id());
            }
        }

        return allPostsId;
    }
}
