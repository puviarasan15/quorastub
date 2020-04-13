package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerBusinessService {
    @Autowired
    UserBusinessService userBusinessService;

    @Autowired
    AnswerDao answerDao;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        return answerDao.createAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(String accessToken, String answerContent, String answerId) throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        AnswerEntity answerEntity = getAnswerEntity(answerId);
        if(!(answerEntity.getUser().getUserName().equals(userAuthEntity.getUser().getUserName()))){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }
        answerEntity.setAns(answerContent);
        answerDao.updateAnswer(answerEntity);
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String accessToken, String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        AnswerEntity answerEntity = getAnswerEntity(answerId);
        UserEntity userEntity = userAuthEntity.getUser();
        if(userEntity.getRole().equals("admin") || !(answerEntity.getUser().equals(userEntity))){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
        }
        answerDao.deleteAnswer(answerEntity);
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity getAnswerEntity(String answerId) throws AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        return answerEntity;
    }

    public List<AnswerEntity> getAllAnswersToQuestion(String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAllAnswersToQuestion(questionEntity);
    }
}
