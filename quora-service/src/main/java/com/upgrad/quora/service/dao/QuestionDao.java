package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * Return Type = QuestionEntity along with their corresponding values
     * Parameters = QuestionEntity questionEntity
     * Description = This method will save the questionEntity in the database as a new row in the table and return the same
     *  */
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /*
     * Return Type = List of QuestionEntity along with their corresponding values
     * Parameters = none
     * Description = This method will gets the entire list of questionEntity from the database and return the same
     *  */
    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = QuestionEntity along with their corresponding values
     * Parameters = String questionId
     * Description = This method will get the QuestionEntity from the database for the specific questionId and return the same
     *  */
    public QuestionEntity getQuestionById(String questionId) {
        try {
            return entityManager.createNamedQuery("getQuestionById", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = no return value
     * Parameters = QuestionEntity questionEntity
     * Description = This method will update the questionEntity in the database in the existing row in the table
     *  */
    public void updateQuestion(QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    /*
     * Return Type = no return value
     * Parameters = QuestionEntity questionEntity
     * Description = This method will delete the questionEntity in the database
     *  */
    public void deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
    }

    /*
     * Return Type = List of QuestionEntity along with their corresponding values
     * Parameters = UserEntity userEntity
     * Description = This method will gets the entire list of questionEntity from the database for the specific user and return the same
     *  */
    public List<QuestionEntity> getAllQuestionsByUser(UserEntity userEntity) {
        try {
            return entityManager.createNamedQuery("getAllQuestionsByUser", QuestionEntity.class).setParameter("user", userEntity).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
