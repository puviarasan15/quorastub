package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    QuestionBusinessService questionBusinessService;


    @RequestMapping(method = RequestMethod.POST, path = "/question/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUuid(UUID.randomUUID().toString());
        ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);
        QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(questionEntity, authorization);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions (@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> createdQuestionEntityList = questionBusinessService.getAllQuestions(authorization);
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        for (QuestionEntity createdQuestionEntity: createdQuestionEntityList) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(createdQuestionEntity.getUuid()).
                    content(createdQuestionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionEditRequest, @PathVariable final String questionId, @RequestHeader("authorization") final String authorization) throws SignOutRestrictedException, AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity createdQuestionEntity = questionBusinessService.editQuestionContent(authorization,questionEditRequest.getContent(),questionId);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(createdQuestionEntity.getUser().getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionBusinessService.questionDelete(authorization,questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable final String userId) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> createdQuestionEntityList = questionBusinessService.getAllQuestionsByUser(authorization, userId);
        List<QuestionDetailsResponse> questionDetailsResponseList = getQuestionDetailsResponseList(createdQuestionEntityList);
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    private List<QuestionDetailsResponse> getQuestionDetailsResponseList(List<QuestionEntity> createdQuestionEntityList){
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        for (QuestionEntity createdQuestionEntity: createdQuestionEntityList) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(createdQuestionEntity.getUuid()).
                    content(createdQuestionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return questionDetailsResponseList;
    }
}
