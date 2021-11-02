package com.subscriptions.subscriptions.controller;

import com.subscriptions.subscriptions.model.*;
import com.subscriptions.subscriptions.userdetails.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.subscriptions.subscriptions.services.UserService;
import com.subscriptions.subscriptions.userdetails.MyUserDetailsService;
import com.subscriptions.subscriptions.util.JwtUtil;

import java.util.List;


@RestController
public class SubscriptionController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserService userService;



    @RequestMapping({"/"})
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();

    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);

    }

    @PostMapping("/signup")
    public void addUser(@RequestBody User user) {

        userService.addUser(user);
        //userService.updateUser(user);
    }

    @PostMapping("/signupmore")
    public void addUsers(@RequestBody List<User> users){
        userService.addUsers(users);

    }

    @PutMapping("/add/{amount}")
    public void addMoney(Authentication authentication, @PathVariable int amount) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        userService.addMoney(myUserDetails.getUsername(), amount);
    }

    @PostMapping("/subscription")
    public void addNewSubscriptionChannel(Authentication authentication, @RequestBody SubscribedChannelsDTO subscribedChannelsDTO){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        userService.addNewSubscriptionChannel( myUserDetails.getUsername(), subscribedChannelsDTO);
    }


    @PutMapping("/pay")
    public void deductMonthlySubscription(Authentication authentication){

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        userService.deductMonthlySubscription( myUserDetails.getUsername());

    }

    @PutMapping("/cancel/{channelid}")
    public String removeSubscriptionChannel(Authentication authentication, @PathVariable int channelid){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        return userService.removeSubscriptionChannel(myUserDetails.getUsername(), channelid);
    }

    @GetMapping("/statement/{from}/{to}")
    public List<Transactions> getStatement(Authentication authentication, @PathVariable String from, @PathVariable String to){
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        return userService.getStatement(myUserDetails.getUsername(), from, to);
    }

//    @PutMapping("/users/{id}/cancel")
//    public void removeSubscriptionChannel(@PathVariable int id) {
//
//        userService.removeSubscriptionChannel(id);
//
//
//    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }   catch (BadCredentialsException e) {

            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);


        return  ResponseEntity.ok(new AuthenticationResponse((jwt)));
    }

}






