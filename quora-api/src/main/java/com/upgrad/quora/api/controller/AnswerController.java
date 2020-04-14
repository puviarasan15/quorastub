package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
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


    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest, @PathVariable final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException, InvalidQuestionException {
        AnswerEntity answerEntity = answerBusinessService.editAnswerContent(authorization,answerEditRequest.getContent(), answerId);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization, @PathVariable final String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerBusinessService.deleteAnswer(authorization, answerId);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@RequestHeader("authorization") final String authorization, @PathVariable final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        List<AnswerEntity> createdAnswerEntityList = answerBusinessService.getAllAnswersToQuestion(authorization, questionId);
        List<AnswerDetailsResponse> questionDetailsResponseList = getAnswerDetailsResponseList(createdAnswerEntityList);
        return new ResponseEntity<List<AnswerDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    private List<AnswerDetailsResponse> getAnswerDetailsResponseList(List<AnswerEntity> createdAnswerEntityList){
        List<AnswerDetailsResponse> answerDetailsResponseList = new ArrayList<>();
        for (AnswerEntity answerEntity : createdAnswerEntityList) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(answerEntity.getUuid()).
                    answerContent(answerEntity.getAns()).questionContent(answerEntity.getQuestion().getContent());
            answerDetailsResponseList.add(answerDetailsResponse);
        }
        return answerDetailsResponseList;
    }
}
