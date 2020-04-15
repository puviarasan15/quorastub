package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    /*
     * Return Type = AnswerEntity along with their corresponding values
     * Parameters = AnswerEntity answerEntity
     * Description = This method will save the answerEntity in the database as a new row in the table and return the same
     *  */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /*
     * Return Type = AnswerEntity along with their corresponding values
     * Parameters = String answerId
     * Description = This method will get the answerEntity from the database by passing the namedquery with answerid and return the same
     *  */
    public AnswerEntity getAnswerById(String answerId) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = no return value
     * Parameters = AnswerEntity answerEntity
     * Description = This method will update the answerEntity in the database in the existing row in the table
     *  */
    public void updateAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }

    /*
     * Return Type = no return value
     * Parameters = AnswerEntity answerEntity
     * Description = This method will delete the answerEntity in the database
     *  */
    public void deleteAnswer(AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
    }

    /*
     * Return Type = List of AnswerEntity along with their corresponding values
     * Parameters = QuestionEntity questionEntity
     * Description = This method will gets the list of answerEntity from the database for the specific questionentity and return the same
     *  */
    public List<AnswerEntity> getAllAnswersToQuestion(QuestionEntity questionEntity) {
        try {
            return entityManager.createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class).setParameter("question", questionEntity).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
