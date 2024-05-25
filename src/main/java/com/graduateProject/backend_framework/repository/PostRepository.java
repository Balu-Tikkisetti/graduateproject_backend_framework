package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.posts;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface PostRepository extends CrudRepository<posts, Long> {
    posts save(posts post);

    // Method to read all posts
    List<posts> findAll();

    void deleteById(Long post_id);

}
