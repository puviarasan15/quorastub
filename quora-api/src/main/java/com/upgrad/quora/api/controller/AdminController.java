package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
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
public class AdminController {

    @Autowired
    UserBusinessService userBusinessService;

    /*
     * Path = "/admin/user/{userId}"
     * Action = Delete
     * Return Type = Json with Uuid of the user deleted and the corresponding message along with HTTP status
     * Parameters = access token as request header, userid as pathvariable
     * Description = This method will accept the parameters and then delete the user by sending this values to the
     * service layer and then return the user delete response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<UserDeleteResponse> userDelete(@RequestHeader("authorization") final String authorization, @PathVariable final String userId) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity userEntity = userBusinessService.userDelete(authorization, userId);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(userEntity.getUuid()).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }
}
