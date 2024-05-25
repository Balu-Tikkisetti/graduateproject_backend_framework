package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.hashtags;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface hashtagRepository extends CrudRepository<hashtags,Long> {
    hashtags save(hashtags hashtag);
    List<hashtags> findAll();
}
