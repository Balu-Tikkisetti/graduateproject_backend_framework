package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.comments;
import com.graduateProject.backend_framework.model.posts;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface commentsRepository extends CrudRepository<comments, Long> {
    comments save(comments comment);
    List<comments> findAll();
    void deleteById(Long comment_id);
}
