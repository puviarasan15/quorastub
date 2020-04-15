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

    /*
     * Path = "/userprofile/{userId}"
     * Action = Get
     * Return Type = Json with UserDetailsResponse created and the corresponding message along with HTTP status
     * Parameters = access token as request header, userid as pathvariable
     * Description = This method will accept the parameters and then create the user profile in the database by sending this values to the
     * service layer and then return the user details response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<UserDetailsResponse> userProfile(@RequestHeader("authorization") final String authorization, @PathVariable final String userId) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity userEntity = userBusinessService.userProfile(authorization, userId);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().userName(userEntity.getUserName())
                .aboutMe(userEntity.getAboutMe()).contactNumber(userEntity.getContactNumber()).country(userEntity.getCountry())
                .dob(userEntity.getDob()).emailAddress(userEntity.getEmail()).firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
