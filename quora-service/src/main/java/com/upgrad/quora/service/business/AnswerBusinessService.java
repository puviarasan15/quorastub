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

    /*
     * Return Type = AnswerEntity along with their corresponding values
     * Parameters = AnswerEntity answerEntity, String accessToken, String questionId
     * Description = This method will accept the parameters and then create the answer by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        return answerDao.createAnswer(answerEntity);
    }

    /*
     * Return Type = AnswerEntity along with their corresponding values
     * Parameters = AnswerEntity answerEntity, String answerContent, String answerId
     * Description = This method will accept the parameters and then edit the answer by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(String accessToken, String answerContent, String answerId) throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        AnswerEntity answerEntity = getAnswerEntity(answerId);
        if (!(answerEntity.getUser().getUserName().equals(userAuthEntity.getUser().getUserName()))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setAns(answerContent);
        answerDao.updateAnswer(answerEntity);
        return answerEntity;
    }

    /*
     * Return Type = AnswerEntity along with their corresponding values
     * Parameters = String accessToken, String answerId
     * Description = This method will accept the parameters and then delete the answer by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String accessToken, String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        AnswerEntity answerEntity = getAnswerEntity(answerId);
        UserEntity userEntity = userAuthEntity.getUser();

        if (!(answerEntity.getUser().getUserName().equals(userEntity.getUserName())) || userEntity.getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
        answerDao.deleteAnswer(answerEntity);
        return answerEntity;
    }

    /*
     * Return Type = AnswerEntity along with their corresponding values
     * Parameters = String answerId
     * Description = This method will accept the parameters and then gives the answerentity by sending this values to the
     * data access layer
     *  */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity getAnswerEntity(String answerId) throws AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        return answerEntity;
    }

    /*
     * Return Type = List of AnswerEntity along with their corresponding values
     * Parameters = String accessToken, String questionId
     * Description = This method will accept the parameters and then gives all the answers to a question by sending this values to the
     * data access layer
     *  */
    public List<AnswerEntity> getAllAnswersToQuestion(String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuthEntity(accessToken);
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAllAnswersToQuestion(questionEntity);
    }
}
