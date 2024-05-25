package com.graduateProject.backend_framework.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;


@Service
public class FirebaseService {

    public static class FileData {
        private String content;
        private String type;



        public String getContent() {
            return content;
        }

        public void setContent(String content){
            this.content=content;
        }

        public  void setType(String type){
            this.type=type;
        }

        public String getType() {
            return type;
        }
    }

    public String uploadFileToFirebaseStorage(MultipartFile file, String fileName) throws IOException {
        // Upload file to Firebase Storage
        Bucket bucket = StorageClient.getInstance().bucket();
        byte[] bytes = file.getBytes();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName).build();
        Blob blob=bucket.create(fileName, bytes, file.getContentType());

        // Get the download URL of the uploaded file
        return fileName;
    }

    public FileData getFileFromFirebaseStorage(String fileName) throws IOException {
        String[] parts = fileName.split("/", 3);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./serviceAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("graduate-project-fca2f.appspot.com", fileName));

        String[] partData = parts[0].split("-", 3);

        if (blob != null) {
            byte[] content = blob.getContent();
            String encodedContent = Base64.getEncoder().encodeToString(content);
            String type = partData[2];
            FileData file=new FileData();
            file.setContent(encodedContent);
            file.setType(type);
            return file;
        }
        return null;
    }
}
