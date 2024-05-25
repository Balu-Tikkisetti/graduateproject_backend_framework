package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.picUploader;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PicUploaderRepository extends CrudRepository<picUploader, Long> {
     List<picUploader> findAll();
}
