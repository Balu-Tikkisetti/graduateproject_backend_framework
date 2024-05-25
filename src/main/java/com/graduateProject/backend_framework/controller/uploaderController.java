package com.graduateProject.backend_framework.controller;

import com.graduateProject.backend_framework.service.picuploaderservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class uploaderController {

    private final picuploaderservice pservice;

    @Autowired
    public uploaderController(picuploaderservice pservice) {
        this.pservice = pservice;
    }

    @GetMapping("/push/{message}")
    public ResponseEntity<?> pushImages(@PathVariable String message){
        if("run".equals(message)) {
            try {
                picuploaderservice.uploadData response=pservice.uploading();
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.ok("Notging to dpo");
        }
    }

    @GetMapping("/get/{message}")
    public ResponseEntity<?> getImages(@PathVariable String message){
        if("run".equals(message)){
            try{
                picuploaderservice.downloadData response = pservice.downloading();
                return ResponseEntity.ok(response);
            }catch (Exception e){
                return ResponseEntity.notFound().build();
            }
        }else{
            return ResponseEntity.ok("Notging to dpo");
        }

    }



}
