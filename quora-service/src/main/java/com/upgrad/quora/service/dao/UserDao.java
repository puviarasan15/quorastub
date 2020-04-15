package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    /*
     * Return Type = UserEntity along with their corresponding values
     * Parameters = UserEntity userEntity
     * Description = This method will save the userEntity in the database as a new row in the table and return the same
     *  */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /*
     * Return Type = UserEntity along with their corresponding values
     * Parameters = String email
     * Description = This method will get the UserEntity from the database for the specific email and return the same
     *  */
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = UserEntity along with their corresponding values
     * Parameters = String userName
     * Description = This method will get the UserEntity from the database for the specific userName and return the same
     *  */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = UserEntity along with their corresponding values
     * Parameters = String uuid
     * Description = This method will get the UserEntity from the database for the specific uuid and return the same
     *  */
    public UserEntity getUserByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = UserAuthEntity along with their corresponding values
     * Parameters = UserAuthEntity userAuthTokenEntity
     * Description = This method will save the userAuthTokenEntity in the database as a new row in the table and return the same
     *  */
    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    /*
     * Return Type = no return value
     * Parameters = UserAuthEntity updatedUserEntity
     * Description = This method will update the updatedUserEntity in the database in the existing row in the table
     *  */
    public void updateUserAuth(final UserAuthEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    /*
     * Return Type = UserAuthEntity along with their corresponding values
     * Parameters = String accesstoken
     * Description = This method will get the UserAuthEntity from the database for the specific accesstoken and return the same
     *  */
    public UserAuthEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /*
     * Return Type = no return value
     * Parameters = UserEntity userEntity
     * Description = This method will delete the userEntity in the database
     *  */
    public void deleteUser(UserEntity userEntity) {
        entityManager.remove(userEntity);
    }

    /*
     * Return Type = no return value
     * Parameters = UserAuthEntity userAuthEntity
     * Description = This method will delete the userAuthEntity in the database
     *  */
    public void deleteUserAuth(UserAuthEntity userAuthEntity) {
        entityManager.remove(userAuthEntity);
    }
}
