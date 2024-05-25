package com.graduateProject.backend_framework.service;

import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.model.hashtags;
import com.graduateProject.backend_framework.model.posts;
import com.graduateProject.backend_framework.repository.PostRepository;
import com.graduateProject.backend_framework.repository.UserRepository;
import com.graduateProject.backend_framework.repository.hashtagRepository;
import org.apache.catalina.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneratingPostsService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private FirebaseService firebaseService;
    private hashtagRepository hrepo;
    private PostService postService;
    private Set<File> processedFiles;

    public GeneratingPostsService(PostRepository postRepository, FirebaseService firebaseService,UserRepository userRepository, hashtagRepository hrepo, PostService postService) {
        this.postRepository = postRepository;
        this.firebaseService = firebaseService;
        this.userRepository=userRepository;
        this.hrepo=hrepo;
        this.postService=postService;
        this.processedFiles = new HashSet<>();
    }

//    @Scheduled(fixedDelay = 10000) // Run every 10 seconds
//    public void checkAndProcessNewFiles() throws IOException, SQLException {
//        postGenerator();
//
//    }


//    public void checkForNewFiles() {
//        String localImageFolder = "/Users/balutikkisetti/Downloads/posts_generator";
//        File folder = new File(localImageFolder);
//        String[] filesInFolder = folder.list();
//        if (filesInFolder != null) {
//
//            // Convert file names to File objects and add them to processedFiles
//            Arrays.stream(filesInFolder)
//                    .map(fileName -> new File(localImageFolder, fileName))
//                    .filter(file -> !processedFiles.contains(file))
//                    .forEach(processedFiles::add);
//        }
//    }

//    public void storePhotosIntoMySQL() throws IOException, SQLException {
//        Long userId=9L;
//        String localImageFolder = "/Users/balutikkisetti/Downloads/testing_photos"; // Path to your local image folder
//
//        Optional<Users> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            File folder = new File(localImageFolder);
//            File[] files = folder.listFiles();
//            if (files != null) {
//                long startTime = System.currentTimeMillis();
//                int filesUploaded = 0;
//                for (File file : files) {
//                    if (file.isFile()) {
//                        String fileName = file.getName();
//                        String description = "Description for " + fileName; // Example description
//                        String hashtag = "TestingPhoto"; // Example hashtag
//                        String timestamp = String.valueOf(System.currentTimeMillis()); // Example timestamp
//
//                        // Save post to MySQL
//                        posts post = new posts();
//                        post.setPost_text(description);
//                        Users user = optionalUser.get();
//                        post.setUser(user);
//                        post.setTimestamp(timestamp);
//                        try (FileInputStream fis = new FileInputStream(file)) {
//                            byte[] imageData = new byte[(int) file.length()];
//                            fis.read(imageData);
//                            Blob blob = new SerialBlob(imageData);
//                            post.setPost_image_blob(blob);
//                            postRepository.save(post);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (SerialException e) {
//                            throw new RuntimeException(e);
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        // Save hashtag to MySQL
//                        hashtags hash = new hashtags();
//                        hash.setPosts(post);
//                        hash.setTimestamp(timestamp);
//                        hash.setHashtag_text(hashtag);
//                        hrepo.save(hash);
//                        filesUploaded++;
//                        System.out.print("\rFiles uploaded: " + filesUploaded + "/" + files.length);
//                    }
//                }
//
//                long endTime = System.currentTimeMillis();
//                long executionTime = endTime - startTime;
//                System.out.println("Time taken to save all photos: " + executionTime + "ms");
//            }
//        } else {
//            throw new RuntimeException("User with id " + userId + " not found");
//        }
//    }



    public void postGenerator() throws IOException, SQLException {
        Long user_id = 9L;

        String localImageFolder = "/Users/balutikkisetti/Downloads/posts_generator";
        Optional<Users> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {

            String hashtag = "SystemGeneratedPost";
            String description = "Generated by the system";
            long timestampMillis = System.currentTimeMillis();
            Instant instant = Instant.ofEpochMilli(timestampMillis);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = localDateTime.format(formatter);
            File folder = new File(localImageFolder);
            File[] files = folder.listFiles();
            System.out.println(files.length);
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file)) {

                        posts post = new posts();
                        Path path = file.toPath();

                        byte[] content = Files.readAllBytes(path);
                        Blob blob = new SerialBlob(content);
                        post.setPost_text(description);
                        post.setPost_image_blob(blob);
                        post.setUser(optionalUser.get());
                        post.setTimestamp(timestamp);
                        postRepository.save(post);

                        hashtags hash = new hashtags();
                        hash.setPosts(post);
                        hash.setTimestamp(timestamp);
                        hash.setHashtag_text(hashtag);
                        hrepo.save(hash);
                        processedFiles.add(file);
                    }
                }
            }
        }
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        String extension = name.substring(name.lastIndexOf(".") + 1);
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif"};
        for (String ext : imageExtensions) {
            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

//    public void storePhotosIntoFirebase(Long user_id){
//
//    }
//
//    public void getPhotosFromMySql(Long user_id){
//
//    }
//
//    public void getPhotosFromFirebase(Long user_id){
//
//    }
}
