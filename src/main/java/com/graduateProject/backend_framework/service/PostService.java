package com.graduateProject.backend_framework.service;


import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BlobInfo;
import com.google.firebase.cloud.StorageClient;
import com.graduateProject.backend_framework.model.*;
import com.graduateProject.backend_framework.repository.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Blob;

import java.sql.SQLException;
import java.util.Optional;

import com.graduateProject.backend_framework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import com.graduateProject.backend_framework.service.FirebaseService;
import java.util.concurrent.TimeUnit;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserRepository usersRepository;
    private SubscribeService subscribeService;
    private SubscribeRepository subscribeRepository;
    private hashtagRepository hrepo;
    private FirebaseService firebaseService;
    private commentsRepository crepo;
    private LikesRepository lrepo;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository usersRepository, FirebaseService firebaseService, hashtagRepository hrepo, SubscribeService subscribeService, SubscribeRepository subscribeRepository, commentsRepository crepo, LikesRepository lrepo) {
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
        this.firebaseService=firebaseService;
        this.hrepo=hrepo;
        this.subscribeService=subscribeService;
        this.subscribeRepository=subscribeRepository;
        this.crepo=crepo;
        this.lrepo=lrepo;

    }

    public void savePost(Long user_id, String hashtag, String description, MultipartFile file, String timestamp) throws IOException {
        Optional<Users> optionalUser = usersRepository.findById(user_id);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            posts post = new posts();
            post.setUser(user);
            post.setPost_text(description);
            post.setTimestamp(timestamp);

            if (!file.isEmpty()) {
                String fileType = file.getContentType().startsWith("image") ? "images" : "videos";
                String fileUrl = saveFileAndGetUrl(file, fileType, user_id);
                if (fileType.equals("images")) {
                    post.setPost_pic_url(fileUrl);
                } else {
                    post.setPost_video_url(fileUrl);
                }
            }

            postRepository.save(post);

            hashtags hash=new hashtags();
            hash.setPosts(post);
            hash.setTimestamp(timestamp);
            hash.setHashtag_text(hashtag);
            hrepo.save(hash);
        } else {
            throw new RuntimeException("User with id " + user_id + " not found");
        }
    }




    private String getFileExtension(String fileType) {
        // Extract file extension from content type
        if (fileType.startsWith("image")) {
            return "jpg"; // Change to appropriate extension for images
        } else {
            return "mp4"; // Change to appropriate extension for videos
        }
    }


    public List<UserPostDTO> findPostByUserId(Long userId) throws IOException, SQLException {
        List<posts> allPosts = postRepository.findAll();
        List<UserPostDTO> userPosts = new ArrayList<>();

        for (posts post : allPosts) {
            if (post.getUser().getUser_id() == userId) {
                // Get the file from Firebase Storage
                FirebaseService.FileData file = new FirebaseService.FileData();
                if (post.getPost_pic_url() != null) {
                    file = firebaseService.getFileFromFirebaseStorage(post.getPost_pic_url());
                } else if (post.getPost_video_url() != null) {
                    file = firebaseService.getFileFromFirebaseStorage(post.getPost_video_url());
                }else if(post.getPost_image_blob()!=null){
                    Blob blob=post.getPost_image_blob();
                    file.setType("image");
                    byte[] content =blob.getBytes(1, (int) blob.length());
                    String encodedContent = Base64.getEncoder().encodeToString(content);
                    file.setContent(encodedContent);
                }



                // Create a DTO object with necessary post details and file
                UserPostDTO userPostDTO = new UserPostDTO();
                userPostDTO.setPostId(post.getPost_id());
                userPostDTO.setPostText(post.getPost_text());
                userPostDTO.setFilebytes(file.getContent());
                userPostDTO.set_type(file.getType());
                userPostDTO.setTimestamp(post.getTimestamp());
                userPostDTO.setLikes_count(post.getLikes_count());

                List<hashtags> hash=hrepo.findAll();
                for(hashtags ht:hash){
                    if(ht.getPosts().getPost_id()== post.getPost_id()){
                     userPostDTO.setHastags(ht.getHashtag_text());
                    }
                }

                // Add the DTO object to the list
                userPosts.add(userPostDTO);
            }
        }

        return userPosts;
    }

    public EditPostDTO getEditPostService(Long post_id) throws IOException, SQLException {
        Optional<posts>optionalPost=postRepository.findById(post_id);
        EditPostDTO postDTO=new EditPostDTO();
        if(optionalPost.isPresent()){
            FirebaseService.FileData file = new FirebaseService.FileData();
            posts post=optionalPost.get();
            if (post.getPost_pic_url() != null) {
                file = firebaseService.getFileFromFirebaseStorage(post.getPost_pic_url());
            } else if (post.getPost_video_url() != null) {
                file = firebaseService.getFileFromFirebaseStorage(post.getPost_video_url());
            }else if(post.getPost_image_blob()!=null){
                Blob blob=post.getPost_image_blob();
                file.setType("images");
                byte[] content =blob.getBytes(1, (int) blob.length());
                String encodedContent = Base64.getEncoder().encodeToString(content);
                file.setContent(encodedContent);
            }

            postDTO.setPost_id(post.getPost_id());
            postDTO.setUser_id(post.getUser().getUser_id());
            postDTO.setPost_text(post.getPost_text());
            postDTO.setTimestamp(post.getTimestamp());
            postDTO.setPost_base64(file.getContent());
            postDTO.setUsername(post.getUser().getUsername());
            postDTO.setLikes_count(post.getLikes_count());
            Blob blob = post.getUser().getProfile_pic();
            if (blob != null) {
                try {
                    // Read the bytes from the Blob
                    byte[] bytes = blob.getBytes(1, (int) blob.length());
                    // Convert bytes to Base64
                    String base64Image = Base64.getEncoder().encodeToString(bytes);
                    postDTO.setProfile_pic(base64Image);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            List<hashtags> hash=hrepo.findAll();
            for(hashtags ht:hash){
                if(ht.getPosts().getPost_id()== post.getPost_id()){
                    postDTO.setHashtags(ht.getHashtag_text());
                }
            }

          return postDTO;
        }

        return null;
    }

    public String updatePostService(Long post_id, String post_text, MultipartFile file,String hashtags, String changed_on) throws IOException {
        Optional<posts>optionalpost=postRepository.findById(post_id);
        if(optionalpost.isPresent()){
           posts post=optionalpost.get();
           post.setPost_text(post_text);
           post.setChanged_on(changed_on);
          Long user_id=post.getUser().getUser_id();
            if (!file.isEmpty()) {
                String fileType = file.getContentType().startsWith("image") ? "images" : "videos";
                String fileUrl = saveFileAndGetUrl(file, fileType, user_id);
                if (fileType.equals("images")) {
                    post.setPost_pic_url(fileUrl);
                } else {
                    post.setPost_video_url(fileUrl);
                }
            }

            hashtags hash=new hashtags();
            hash.setPosts(post);
            hash.setHashtag_text(hashtags);
            hrepo.save(hash);
        }

        return  null;
    }

    public List<showPostDTO> getSearchPostsService(String keyWord) throws SQLException, IOException {
        List<hashtags>allhashtags=hrepo.findAll();
        List<showPostDTO>matchingPosts=new ArrayList<>();


        for(hashtags hash:allhashtags){
            String[] hashWords = hash.getHashtag_text().split("\\s+|\\,|\\;|\\-");
            for(String st:hashWords){
                if(st.contains(keyWord)){
                  posts post=hash.getPosts();
                  showPostDTO postDTO=new showPostDTO();
                  postDTO.setPost_id(post.getPost_id());
                  postDTO.setUser_id(post.getUser().getUser_id());
                  postDTO.setUsername(post.getUser().getUsername());
                  Blob blob=post.getUser().getProfile_pic();
                  if(blob!=null){
                      byte[] bytes = blob.getBytes(1, (int) blob.length());
                      // Convert bytes to Base64
                      String base64Image = Base64.getEncoder().encodeToString(bytes);
                      postDTO.setProfile_pic(base64Image);
                  }
                  postDTO.setPost_text(post.getPost_text());
                  postDTO.setHashtags(hash.getHashtag_text());

                    FirebaseService.FileData file = new FirebaseService.FileData();
                    if (post.getPost_pic_url() != null) {
                        file = firebaseService.getFileFromFirebaseStorage(post.getPost_pic_url());
                    } else if (post.getPost_video_url() != null) {
                        file = firebaseService.getFileFromFirebaseStorage(post.getPost_video_url());
                    }else if(post.getPost_image_blob()!=null){
                        Blob pblob=post.getPost_image_blob();
                        file.setType("images");
                        byte[] content =pblob.getBytes(1, (int) pblob.length());
                        String encodedContent = Base64.getEncoder().encodeToString(content);
                        file.setContent(encodedContent);
                    }
                   postDTO.setPost_image(file.getContent());
                    postDTO.setType(file.getType());
                    matchingPosts.add(postDTO);

                  break;
                }
            }
        }

        return matchingPosts;
    }


    public List<SubscriberPostsDTO> getSubscribedPostsService(Long user_id) throws IOException, SQLException {
        Optional<Users>optionaluser=usersRepository.findById(user_id);
        List<SubscriberPostsDTO>AllspDTO=new ArrayList<>();

        if(optionaluser.isPresent()){
            List<Users>allusers=usersRepository.findAll();

            for(Users user:allusers){
                 Long u1=user.getUser_id();
                 Long u2=user_id;
                Optional<Subscribe>su=subscribeRepository.findById(new SubscribeId(u2,u1));
                if(su.isPresent()){

//                }

//                if(subscribeService.isSubscriberService(u2, u1)){

                   List<UserPostDTO>AllupDTO= findPostByUserId(user.getUser_id());
                   for(UserPostDTO upDTO:AllupDTO){
                       SubscriberPostsDTO spDTO=new SubscriberPostsDTO();
                       spDTO.setPost_id(upDTO.getPostId());
                       spDTO.setUser_id(user.getUser_id());
                       spDTO.setUsername(user.getUsername());
                       spDTO.setPost_text(upDTO.getPostText());
                       spDTO.setPost_image(upDTO.getFilebyte());
                       spDTO.setLikes_count(upDTO.getLikes_count());
                       spDTO.setHashtags(upDTO.getHastags());
                       Blob blob = user.getProfile_pic();
                       if (blob != null) {
                           try {

                               byte[] bytes = blob.getBytes(1, (int) blob.length());
                               String base64Image = Base64.getEncoder().encodeToString(bytes);
                               spDTO.setProfile_pic(base64Image);
                           } catch (SQLException e) {
                               e.printStackTrace();
                           }
                       }
                       spDTO.setTimestamp(upDTO.getTimestamp());
                       spDTO.setType(upDTO.get_type());
                       AllspDTO.add(spDTO);

                   }
                }
            }
        }
        return AllspDTO;
    }

    public List<SubscriberPostsDTO> getRandomPostsService(Long user_id) throws IOException, SQLException {
        Optional<Users> optionalUser = usersRepository.findById(user_id);
        List<posts> allPosts = postRepository.findAll();
        List<SubscriberPostsDTO> AllspDTO = new ArrayList<>();

        if (optionalUser.isPresent()) {
            int totalPosts = allPosts.size();
            int maxRandomPosts = (int) (totalPosts * 0.7);

            // Factor in the present time for random number generation
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            long currentTimeInMillis = calendar.getTimeInMillis();

            // Generate random indices to select random posts
            Random random = new Random(currentTimeInMillis);
            List<Integer> randomIndices = new ArrayList<>();
            int numberOfRandomPosts = Math.min(maxRandomPosts, totalPosts);
            while (randomIndices.size() < numberOfRandomPosts) {
                int randomIndex = random.nextInt(totalPosts);
                if (!randomIndices.contains(randomIndex)) {
                    randomIndices.add(randomIndex);
                }
            }

            // Iterate over all posts and add randomly selected posts to AllspDTO
            for (int index : randomIndices) {
                posts post = allPosts.get(index);
                SubscriberPostsDTO spDTO = new SubscriberPostsDTO();
                spDTO.setPost_id(post.getPost_id());
                spDTO.setUser_id(post.getUser().getUser_id());
                spDTO.setUsername(post.getUser().getUsername());
                spDTO.setPost_text(post.getPost_text());

                FirebaseService.FileData file = new FirebaseService.FileData();
                if (post.getPost_pic_url() != null) {
                    file = firebaseService.getFileFromFirebaseStorage(post.getPost_pic_url());
                } else if (post.getPost_video_url() != null) {
                    file = firebaseService.getFileFromFirebaseStorage(post.getPost_video_url());
                }else if(post.getPost_image_blob()!=null){
                    Blob blob=post.getPost_image_blob();
                    file.setType("images");
                    byte[] content =blob.getBytes(1, (int) blob.length());
                    String encodedContent = Base64.getEncoder().encodeToString(content);
                    file.setContent(encodedContent);
                }
                spDTO.setPost_image(file.getContent());
                spDTO.setLikes_count(post.getLikes_count());

                // Set hashtags for the post
                List<hashtags> hash = hrepo.findAll();
                for (hashtags ht : hash) {
                    if (ht.getPosts().getPost_id().equals(post.getPost_id())) {
                        spDTO.setHashtags(ht.getHashtag_text());
                        break; // Once found, exit the loop
                    }
                }

                // Set profile picture for the user
                Blob blob = post.getUser().getProfile_pic();
                if (blob != null) {
                    try {
                        byte[] bytes = blob.getBytes(1, (int) blob.length());
                        String base64Image = Base64.getEncoder().encodeToString(bytes);
                        spDTO.setProfile_pic(base64Image);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                spDTO.setTimestamp(post.getTimestamp());
                spDTO.setType(file.getType());
                AllspDTO.add(spDTO);
            }
        }
        return AllspDTO;
    }


    public String deletePostService(Long post_id){
        Optional<posts>optionalpost=postRepository.findById(post_id);
        if(optionalpost.isPresent()){
            List<hashtags> allhash=hrepo.findAll();
            for(hashtags hash:allhash){
                if(hash.getPosts().getPost_id()==post_id){
                    hrepo.deleteById(hash.getHashtag_id());
                }
            }
            List<comments>allComments=crepo.findAll();
            for(comments comment:allComments){
                if(comment.getPosts().getPost_id()==post_id){
                    crepo.deleteById(comment.getComment_id());
                }
            }

            List<Likes>alllikes=lrepo.findAll();
            for(Likes like:alllikes){
                if(like.getPost().getPost_id()==post_id){
                    lrepo.deleteById(like.getLike_id());
                }
            }
            postRepository.deleteById(post_id);
            return "succefully deleted the post";
        }
        return "Not deleted the post";
    }




    private String saveFileAndGetUrl(MultipartFile file, String fileType, Long userId) throws IOException {
        // Generate a unique file name with user ID
        String fileName = generateFileName(fileType, userId);

        // Upload the file to Firebase Storage
        String fileUrl = firebaseService.uploadFileToFirebaseStorage(file, fileName);

        return fileUrl;
    }

    private String generateFileName(String fileType, Long userId) {
        String mainFolder = fileType.equals("images") ? "college-media-images" : "college-media-videos";
        return mainFolder + "/" + userId.toString() + "/" + UUID.randomUUID().toString();
    }



}


