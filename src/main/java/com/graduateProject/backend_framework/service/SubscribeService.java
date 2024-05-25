package com.graduateProject.backend_framework.service;


import com.graduateProject.backend_framework.model.Subscribe;

import com.graduateProject.backend_framework.model.SubscribeId;
import com.graduateProject.backend_framework.model.Users;
import com.graduateProject.backend_framework.repository.SubscribeRepository;
import com.graduateProject.backend_framework.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class SubscribeService {
    private SubscribeRepository subscribeRepository;
    private UserRepository userRepository;

    public SubscribeService(SubscribeRepository subscribeRepository, UserRepository userRepository) {
        this.subscribeRepository = subscribeRepository;
        this.userRepository = userRepository;
    }

    public void updateSubscription(Long subscriber_user_id,Long subscribed_user_id){
        Optional<Users> optionaluser1=userRepository.findById(subscribed_user_id);
        Optional<Users> optionaluser2=userRepository.findById(subscriber_user_id);
        Long usersCount=userRepository.count();
        if(optionaluser1.isPresent()&&optionaluser2.isPresent()){
            Users user1=optionaluser1.get();
            Users user2=optionaluser2.get();
            Subscribe su=new Subscribe();
            su.setSubscribed_user_id(user1.getUser_id());
            su.setSubscriber_user_id(user2.getUser_id());
            subscribeRepository.save(su);
            if(user1.getSubscribers()==null){
                user1.setSubscribers(1);
            }
            else{
                int u1=user1.getSubscribers()+1;
                user1.setSubscribers(u1);
            }
            if(user2.getSubscribed()==null){
                user2.setSubscribed(1);
            }
            else{
                int u2=user2.getSubscribed()+1;
                user2.setSubscribed(u2);
            }
            userRepository.save(user1);
            userRepository.save(user2);

        }
    }

    public void deleteSubscriptionService(Long unsubscriber_user_id, Long unsubscribed_user_id){
        Optional<Users>optionaluser1=userRepository.findById(unsubscriber_user_id);
        Optional<Users>optionaluser2=userRepository.findById(unsubscribed_user_id);
        if(optionaluser1.isPresent()&&optionaluser2.isPresent()){
            Users user1=optionaluser1.get();
            Users user2=optionaluser2.get();

            try {
                subscribeRepository.deleteById(new SubscribeId(unsubscribed_user_id,unsubscriber_user_id));
                if(user1.getSubscribed()==null){
                    user1.setSubscribed(0);
                }else{
                    int u1=user1.getSubscribed()-1;

                    user1.setSubscribed(u1);
                }
                if(user2.getSubscribers()==null){
                    user2.setSubscribers(0);
                }else{
                    int u2=user2.getSubscribers()-1;
                    user2.setSubscribers(u2);
                }
                userRepository.save(user1);
                userRepository.save(user2);
            }catch(Exception e){
               e.printStackTrace();
            }
        }
    }

    public boolean isSubscriberService(Long user_id, Long search_user_id){

        Optional<Subscribe>su=subscribeRepository.findById(new SubscribeId(search_user_id,user_id));
        if(su.isPresent()){
            return true;
        }
        return false;
    }
}
