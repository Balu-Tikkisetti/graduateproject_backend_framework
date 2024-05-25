package com.graduateProject.backend_framework.repository;


import com.graduateProject.backend_framework.model.Likes;
import com.graduateProject.backend_framework.model.posts;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikesRepository extends CrudRepository<Likes, Long> {
    List<Likes> findAll();
}
