package com.graduateProject.backend_framework.service;


import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.graduateProject.backend_framework.model.picUploader;
import com.graduateProject.backend_framework.repository.PicUploaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class picuploaderservice {

    public static class uploadData{
        private Long first_SQL;
        private Long second_SQL;
        private Long third_SQL;
        private Long fourth_SQL;
        private Long fifth_SQL;

        private Long first_firebase;
        private Long second_firebase;
        private Long third_firebase;
        private Long fourth_firebase;
        private Long fifth_firebase;

        public Long getFirst_SQL() {
            return first_SQL;
        }

        public void setFirst_SQL(Long first_SQL) {
            this.first_SQL = first_SQL;
        }

        public Long getSecond_SQL() {
            return second_SQL;
        }

        public void setSecond_SQL(Long second_SQL) {
            this.second_SQL = second_SQL;
        }

        public Long getThird_SQL() {
            return third_SQL;
        }

        public void setThird_SQL(Long third_SQL) {
            this.third_SQL = third_SQL;
        }

        public Long getFourth_SQL() {
            return fourth_SQL;
        }

        public void setFourth_SQL(Long fourth_SQL) {
            this.fourth_SQL = fourth_SQL;
        }

        public Long getFifth_SQL() {
            return fifth_SQL;
        }

        public void setFifth_SQL(Long fifth_SQL) {
            this.fifth_SQL = fifth_SQL;
        }

        public Long getFirst_firebase() {
            return first_firebase;
        }

        public void setFirst_firebase(Long first_firebase) {
            this.first_firebase = first_firebase;
        }

        public Long getSecond_firebase() {
            return second_firebase;
        }

        public void setSecond_firebase(Long second_firebase) {
            this.second_firebase = second_firebase;
        }

        public Long getThird_firebase() {
            return third_firebase;
        }

        public void setThird_firebase(Long third_firebase) {
            this.third_firebase = third_firebase;
        }

        public Long getFourth_firebase() {
            return fourth_firebase;
        }

        public void setFourth_firebase(Long fourth_firebase) {
            this.fourth_firebase = fourth_firebase;
        }

        public Long getFifth_firebase() {
            return fifth_firebase;
        }

        public void setFifth_firebase(Long fifth_firebase) {
            this.fifth_firebase = fifth_firebase;
        }
    }

    public static class downloadData{
        private Long first_SQL;
        private Long second_SQL;
        private Long third_SQL;
        private Long fourth_SQL;
        private Long fifth_SQL;

        private Long first_firebase;
        private Long second_firebase;
        private Long third_firebase;
        private Long fourth_firebase;
        private Long fifth_firebase;

        public Long getFirst_SQL() {
            return first_SQL;
        }

        public void setFirst_SQL(Long first_SQL) {
            this.first_SQL = first_SQL;
        }

        public Long getSecond_SQL() {
            return second_SQL;
        }

        public void setSecond_SQL(Long second_SQL) {
            this.second_SQL = second_SQL;
        }

        public Long getThird_SQL() {
            return third_SQL;
        }

        public void setThird_SQL(Long third_SQL) {
            this.third_SQL = third_SQL;
        }

        public Long getFourth_SQL() {
            return fourth_SQL;
        }

        public void setFourth_SQL(Long fourth_SQL) {
            this.fourth_SQL = fourth_SQL;
        }

        public Long getFifth_SQL() {
            return fifth_SQL;
        }

        public void setFifth_SQL(Long fifth_SQL) {
            this.fifth_SQL = fifth_SQL;
        }

        public Long getFirst_firebase() {
            return first_firebase;
        }

        public void setFirst_firebase(Long first_firebase) {
            this.first_firebase = first_firebase;
        }

        public Long getSecond_firebase() {
            return second_firebase;
        }

        public void setSecond_firebase(Long second_firebase) {
            this.second_firebase = second_firebase;
        }

        public Long getThird_firebase() {
            return third_firebase;
        }

        public void setThird_firebase(Long third_firebase) {
            this.third_firebase = third_firebase;
        }

        public Long getFourth_firebase() {
            return fourth_firebase;
        }

        public void setFourth_firebase(Long fourth_firebase) {
            this.fourth_firebase = fourth_firebase;
        }

        public Long getFifth_firebase() {
            return fifth_firebase;
        }

        public void setFifth_firebase(Long fifth_firebase) {
            this.fifth_firebase = fifth_firebase;
        }
    }

    FirebaseService firebaseService;


    PicUploaderRepository picUploaderRepository;


    @Autowired
    public picuploaderservice(FirebaseService firebaseService,PicUploaderRepository picUploaderRepository ) {
        this.firebaseService = firebaseService;
        this.picUploaderRepository = picUploaderRepository;

    }

    public uploadData uploading() throws IOException, SQLException {
        String localImageFolder = "/Users/balutikkisetti/Downloads/pictures_2000";
        String mainFolder = "college-media-test3-images";
        String userId = "test";
        File folder = new File(localImageFolder);
        File[] files = folder.listFiles();
        uploadData ud=new uploadData();
        if (files != null) {
            long totalUploadTimeMySQL = 0;
            long totalUploadTimeFirebase = 0;
            int totalPhotos = files.length;
            System.out.println(totalPhotos);

            // Calculate indices for different percentage limits
            int[] limits = new int[]{totalPhotos * 20 / 100, totalPhotos * 40 / 100, totalPhotos * 60 / 100,
                    totalPhotos * 80 / 100, totalPhotos};

            for (int i = 0; i < files.length; i++) {
                String fileName = mainFolder + "/" + userId + "/" + UUID.randomUUID().toString(); // Generate a unique fileName for each photo
                Path path = files[i].toPath();
                byte[] content = Files.readAllBytes(path);

                // Upload to MySQL
                long startTimeMySQL = System.currentTimeMillis();
                picUploader pu = new picUploader();
                Blob blob = new SerialBlob(content);
                pu.setImage_data(blob);
                picUploaderRepository.save(pu);
                long endTimeMySQL = System.currentTimeMillis();
                long uploadTimeMySQL = endTimeMySQL - startTimeMySQL;
                totalUploadTimeMySQL += uploadTimeMySQL;



                // Upload to Firebase
                long startTimeFirebase = System.currentTimeMillis();
                Bucket bucket = StorageClient.getInstance().bucket();
                byte[] bytes = content;
                BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName).build();
                com.google.cloud.storage.Blob b = bucket.create(fileName, bytes, "images");
                long endTimeFirebase = System.currentTimeMillis();
                long uploadTimeFirebase = endTimeFirebase - startTimeFirebase;
                totalUploadTimeFirebase += uploadTimeFirebase;


                    if(i+1==limits[0]){
                        ud.setFirst_SQL(totalUploadTimeMySQL);
                        ud.setFirst_firebase(totalUploadTimeFirebase);
                        System.out.println(totalUploadTimeMySQL);
                        System.out.println(totalUploadTimeFirebase);
                    }else if(i+1==limits[1]){
                        ud.setSecond_SQL(totalUploadTimeMySQL);
                        ud.setSecond_firebase(totalUploadTimeFirebase);
                        System.out.println(totalUploadTimeMySQL);
                        System.out.println(totalUploadTimeFirebase);
                    }else if(i+1==limits[2]){
                        ud.setThird_SQL(totalUploadTimeMySQL);
                        ud.setThird_firebase(totalUploadTimeFirebase);
                        System.out.println(totalUploadTimeMySQL);
                        System.out.println(totalUploadTimeFirebase);
                    }else if(i+1==limits[3]){
                        ud.setFourth_SQL(totalUploadTimeMySQL);
                        ud.setFourth_firebase(totalUploadTimeFirebase);
                        System.out.println(totalUploadTimeMySQL);
                        System.out.println(totalUploadTimeFirebase);
                    }else if(i+1==limits[4]){
                        ud.setFifth_SQL(totalUploadTimeMySQL);
                        ud.setFifth_firebase(totalUploadTimeFirebase);
                        System.out.println(totalUploadTimeMySQL);
                        System.out.println(totalUploadTimeFirebase);
                    }


            }
            System.out.println("Total time for MySQL upload: " + totalUploadTimeMySQL + " milliseconds");
            System.out.println("Total time for Firebase upload: " + totalUploadTimeFirebase + " milliseconds");

        }
        return ud;
    }



    public downloadData downloading() throws SQLException, IOException {
        // Downloading from MySQL
        long startTimeMySQL = System.currentTimeMillis();

        long totalDpwnloadTimeMySQL = 0;
        long totalDownloadTimeFirebase = 0;
        downloadData dd=new downloadData();
        List<picUploader> allpic = picUploaderRepository.findAll();
        int totalPhotos = allpic.size();
        int[] limits = new int[]{totalPhotos * 20 / 100, totalPhotos * 40 / 100, totalPhotos * 60 / 100,
                totalPhotos * 80 / 100, totalPhotos};

        for (int i = 0; i < allpic.size(); i++) {
            long stMySQL = System.currentTimeMillis();
            Blob blob = allpic.get(i).getImage_data();
            byte[] content = blob.getBytes(1, (int) blob.length());
            long etMYSql=System.currentTimeMillis();
            totalDpwnloadTimeMySQL+=etMYSql-stMySQL;

                if(i+1==limits[0]){
                    dd.setFirst_SQL(totalDpwnloadTimeMySQL);

                }else if(i+1==limits[1]){
                    dd.setSecond_SQL(totalDpwnloadTimeMySQL);

                }else if(i+1==limits[2]){
                    dd.setThird_SQL(totalDpwnloadTimeMySQL);

                }else if(i+1==limits[3]){
                    dd.setFourth_SQL(totalDpwnloadTimeMySQL);

                }else if(i+1==limits[4]){
                    dd.setFifth_SQL(totalDpwnloadTimeMySQL);

                }



        }

        long endTimeMySQL = System.currentTimeMillis();
        long totalTimeMySQL = endTimeMySQL - startTimeMySQL;
        System.out.println("Total time for MySQL download: " + totalTimeMySQL + " milliseconds");

        // Downloading from Firebase
        long startTimeFirebase = System.currentTimeMillis();

        // Initialize Firebase credentials and storage client
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./serviceAccountKey.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        // Specify the bucket name and directory path in Firebase storage
        String bucketName = "graduate-project-fca2f.appspot.com";
        String directoryPath = "college-media-test3-images/test";

        // List all objects in the specified directory
        Page<com.google.cloud.storage.Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(directoryPath + "/"));

        // Initialize a list to store downloaded image contents
        List<byte[]> imageContents = new ArrayList<>();

        int totalSize = 0;


        for (com.google.cloud.storage.Blob blob : blobs.iterateAll()) {
            // Add the size of each blob to the total size
            totalSize++;
        }
        System.out.println(totalSize);

        int[] limitsf = new int[]{totalSize * 20 / 100, totalSize * 40 / 100, totalSize * 60 / 100,
                totalSize * 80 / 100, totalSize};

        System.out.println(limitsf[0]);
        System.out.println(limitsf[1]);
        System.out.println(limitsf[2]);
        System.out.println(limitsf[3]);
        System.out.println(limitsf[4]);
        long count=0;
        // Iterate through each object (image) in the directory
        for (com.google.cloud.storage.Blob blob : blobs.iterateAll()) {
            long stFirebase = System.currentTimeMillis();
            byte[] content = blob.getContent();
            long etFirebase = System.currentTimeMillis();
            imageContents.add(content);
            totalDownloadTimeFirebase += etFirebase - stFirebase;

            count++;

                if (count==limitsf[0]) {
                    dd.setFirst_firebase(totalDownloadTimeFirebase);
                } else if (count==limitsf[1]) {
                    dd.setSecond_firebase(totalDownloadTimeFirebase);
                } else if (count==limitsf[2]) {
                    dd.setThird_firebase(totalDownloadTimeFirebase);
                } else if (count==limitsf[3]) {
                    dd.setFourth_firebase(totalDownloadTimeFirebase);
                } else if (count==limitsf[4]) {
                    dd.setFifth_firebase(totalDownloadTimeFirebase);
                }




        }

        long endTimeFirebase = System.currentTimeMillis();
        long totalTimeFirebase = endTimeFirebase - startTimeFirebase;
        System.out.println("Total time for Firebase download: " + totalTimeFirebase + " milliseconds");
        return dd;
    }

}
