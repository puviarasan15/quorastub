package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.*;
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
public class AnswerController {

    @Autowired
    AnswerBusinessService answerBusinessService;

    /*
     * Path = "/question/{questionId}/answer/create"
     * Action = Post
     * Return Type = Json with Uuid of the answer created and the corresponding message along with HTTP status
     * Parameters = access token as request header, userid as pathvariable and the answerrequest with its content
     * Description = This method will accept the parameters and then create the answer in the database by sending this values to the
     * service layer and then return the answer response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @RequestHeader("authorization") final String authorization, @PathVariable final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(now);
        AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(answerEntity, authorization, questionId);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    /*
     * Path = "/answer/edit/{answerId}"
     * Action = Put
     * Return Type = Json with Uuid of the answer edited and the corresponding message along with HTTP status
     * Parameters = access token as request header, answerId as pathvariable and the answerEditRequest with its content
     * Description = This method will accept the parameters and then edit the answer in the database by sending this values to the
     * service layer and then return the answer response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest, @PathVariable final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException, InvalidQuestionException {
        AnswerEntity answerEntity = answerBusinessService.editAnswerContent(authorization, answerEditRequest.getContent(), answerId);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    /*
     * Path = "/answer/delete/{answerId}"
     * Action = Delete
     * Return Type = Json with Uuid of the answer created and the corresponding message along with HTTP status
     * Parameters = access token as request header, answerId as pathvariable
     * Description = This method will accept the parameters and then delete the answer in the database by sending this values to the
     * service layer and then return the deleted answer response along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization, @PathVariable final String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerBusinessService.deleteAnswer(authorization, answerId);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    /*
     * Path = "answer/all/{questionId}"
     * Action = Get
     * Return Type = Json with list of Uuid of the answer created along with its content to a specific question and the corresponding message along with HTTP status
     * Parameters = access token as request header, questionId as pathvariable
     * Description = This method will accept the parameters and then gives the list of answers for a specific question by sending this values to the
     * service layer and then return the answer details response in a list along with its parameters
     *  */
    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@RequestHeader("authorization") final String authorization, @PathVariable final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        List<AnswerEntity> createdAnswerEntityList = answerBusinessService.getAllAnswersToQuestion(authorization, questionId);
        List<AnswerDetailsResponse> questionDetailsResponseList = getAnswerDetailsResponseList(createdAnswerEntityList);
        return new ResponseEntity<List<AnswerDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    /*
     * Return Type = List of answer details response
     * Parameters = List of answer entity
     * Description = This method will accept the parameters by iterating through it and then create the list of answerdetailsresponse
     *  */
    private List<AnswerDetailsResponse> getAnswerDetailsResponseList(List<AnswerEntity> createdAnswerEntityList) {
        List<AnswerDetailsResponse> answerDetailsResponseList = new ArrayList<>();
        for (AnswerEntity answerEntity : createdAnswerEntityList) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(answerEntity.getUuid()).
                    answerContent(answerEntity.getAns()).questionContent(answerEntity.getQuestion().getContent());
            answerDetailsResponseList.add(answerDetailsResponse);
        }
        return answerDetailsResponseList;
    }
}
