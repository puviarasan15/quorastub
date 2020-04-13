package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {
    @Autowired
    UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<UserDetailsResponse> userProfile(@RequestHeader("authorization") final String authorization, @PathVariable final String userId) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity userEntity = userBusinessService.userProfile(authorization,userId);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().userName(userEntity.getUserName())
                .aboutMe(userEntity.getAboutMe()).contactNumber(userEntity.getContactNumber()).country(userEntity.getCountry())
                .dob(userEntity.getDob()).emailAddress(userEntity.getEmail()).firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
