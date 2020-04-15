package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
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

    /*
     * Path = "/question/create"
     * Action = Post
     * Return Type = Json with Uuid of the question created and the corresponding message along with HTTP status
     * Parameters = access token as request header and the questionRequest with its content
     * Description = This method will accept the parameters and then create the question in the database by sending this values to the
     * service layer and then return the question response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    /*
     * Path = "/question/all"
     * Action = Get
     * Return Type = Json with Uuid of the list of answer created along with its content and the corresponding message along with HTTP status
     * Parameters = access token as request header
     * Description = This method will accept the parameters and then gives the list of questions available in the database by sending this values to the
     * service layer and then return the question details response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> createdQuestionEntityList = questionBusinessService.getAllQuestions(authorization);
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        for (QuestionEntity createdQuestionEntity : createdQuestionEntityList) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(createdQuestionEntity.getUuid()).
                    content(createdQuestionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    /*
     * Path = "/question/edit/{questionId}"
     * Action = Put
     * Return Type = Json with Uuid of the question edited and the corresponding message along with HTTP status
     * Parameters = access token as request header, questionId as pathvariable and the questionEditRequest with its content
     * Description = This method will accept the parameters and then edit the question in the database by sending this values to the
     * service layer and then return the question edit response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionEditRequest, @PathVariable final String questionId, @RequestHeader("authorization") final String authorization) throws SignOutRestrictedException, AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity createdQuestionEntity = questionBusinessService.editQuestionContent(authorization, questionEditRequest.getContent(), questionId);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(createdQuestionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    /*
     * Path = "/question/delete/{questionId}"
     * Action = Delete
     * Return Type = Json with Uuid of the question deleted and the corresponding message along with HTTP status
     * Parameters = access token as request header, questionId as pathvariable
     * Description = This method will accept the parameters and then delete the question in the database by sending this values to the
     * service layer and then return the question delete response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionBusinessService.questionDelete(authorization, questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    /*
     * Path = "question/all/{userId}"
     * Action = Get
     * Return Type = Json with list of Uuid of the questions by user and the corresponding message along with HTTP status
     * Parameters = access token as request header, userid as pathvariable
     * Description = This method will accept the parameters and then gives the list of questions in the database for a specific user by sending this values to the
     * service layer and then return the question details response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable final String userId) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> createdQuestionEntityList = questionBusinessService.getAllQuestionsByUser(authorization, userId);
        List<QuestionDetailsResponse> questionDetailsResponseList = getQuestionDetailsResponseList(createdQuestionEntityList);
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    /*
     * Return Type = List of QuestionDetailsResponse
     * Parameters = List of QuestionEntity
     * Description = This method will accept list of question entity and iterate it to form a list of QuestionDetailsResponse
     * then return the QuestionDetailsResponse along with its parameters
     *  */
    private List<QuestionDetailsResponse> getQuestionDetailsResponseList(List<QuestionEntity> createdQuestionEntityList) {
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        for (QuestionEntity createdQuestionEntity : createdQuestionEntityList) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(createdQuestionEntity.getUuid()).
                    content(createdQuestionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return questionDetailsResponseList;
    }
}
