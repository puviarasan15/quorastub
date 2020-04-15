package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    UserBusinessService userBusinessService;

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    /*
     * Return Type = QuestionEntity along with their corresponding values
     * Parameters = QuestionEntity questionEntity, String accessToken
     * Description = This method will accept the parameters and then creates the question by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity, String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        questionEntity.setUser(userAuthEntity.getUser());
        return questionDao.createQuestion(questionEntity);
    }

    /*
     * Return Type = List of QuestionEntity along with their corresponding values
     * Parameters = String accessToken
     * Description = This method will accept the parameters and then gives all the questions by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        return questionDao.getAllQuestions();
    }

    /*
     * Return Type = QuestionEntity along with their corresponding values
     * Parameters = String accessToken, String questionContent, String questionId
     * Description = This method will accept the parameters and then edits the question by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(String accessToken, String questionContent, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        QuestionEntity questionEntity = getQuestionEntity(questionId);
        if (!(questionEntity.getUser().getUserName().equals(userAuthEntity.getUser().getUserName()))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        questionEntity.setContent(questionContent);
        questionDao.updateQuestion(questionEntity);
        return questionEntity;
    }

    /*
     * Return Type = QuestionEntity along with their corresponding values
     * Parameters =  String accessToken, String questionId
     * Description = This method will accept the parameters and then deletes the question by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity questionDelete(String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        QuestionEntity questionEntity = getQuestionEntity(questionId);
        UserEntity userEntity = userAuthEntity.getUser();
        if (!(questionEntity.getUser().getUserName().equals(userEntity.getUserName())) || userEntity.getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        questionDao.deleteQuestion(questionEntity);
        return questionEntity;
    }

    /*
     * Return Type = QuestionEntity along with their corresponding values
     * Parameters = String questionId
     * Description = This method will accept the parameters and then gives the question with its id by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestionEntity(String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        return questionEntity;
    }

    /*
     * Return Type = List of QuestionEntity along with their corresponding values
     * Parameters = String accessToken, String userId
     * Description = This method will accept the parameters and then gives the list of questions by sending this values to the
     * data access layer
     *  */
    public List<QuestionEntity> getAllQuestionsByUser(String accessToken, String userId) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        UserEntity userEntity = userDao.getUserByUuid(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(userEntity);
    }
}
