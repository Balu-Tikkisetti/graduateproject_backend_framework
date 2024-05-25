package com.graduateProject.backend_framework;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;


@SpringBootApplication

@ComponentScan("com.graduateProject.backend_framework")
@EntityScan("com.graduateProject.backend_framework.model")
public class BackendFrameworkApplication {

	public static Storage storage;
	public static void main(String[] args) throws IOException {
		// Load service account JSON file from resources directory
		Resource resource = new ClassPathResource("src/serviceAccountKey.json");

		FileInputStream serviceAccount =
				new FileInputStream("./serviceAccountKey.json");
		// Initialize Firebase App with service account credentials
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setStorageBucket("graduate-project-fca2f.appspot.com")
				.build();
		FirebaseApp.initializeApp(options);

		Bucket bucket = StorageClient.getInstance().bucket();

		SpringApplication.run(BackendFrameworkApplication.class, args);
	}


}
