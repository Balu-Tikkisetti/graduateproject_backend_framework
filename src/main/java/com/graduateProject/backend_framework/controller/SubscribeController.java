package com.graduateProject.backend_framework.controller;


import com.graduateProject.backend_framework.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SubscribeController {
    private SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping("/updateSubscription/{subscriber_user_id}/{subscribed_user_id}")
    public ResponseEntity<String> updateSubcription(@PathVariable Long subscriber_user_id,
                                            @PathVariable Long subscribed_user_id){

        try{
            subscribeService.updateSubscription(subscriber_user_id,subscribed_user_id);
            return ResponseEntity.ok("Saved the record");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/deleteSubscription/{unsubscriber_user_id}/{unsubscribed_user_id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable Long unsubscriber_user_id,@PathVariable Long unsubscribed_user_id){

        try{
            subscribeService.deleteSubscriptionService(unsubscriber_user_id,unsubscribed_user_id);
            return ResponseEntity.ok("deleted the subscription");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/isSubscriber/{user_id}/{search_user_id}")
    public ResponseEntity<Boolean> isSubscriber(@PathVariable Long user_id,
                                                @PathVariable Long search_user_id){
        try{
            boolean response=subscribeService.isSubscriberService(user_id,search_user_id);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }
}
