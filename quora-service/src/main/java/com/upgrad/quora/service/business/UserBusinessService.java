package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        UserEntity userEntityByEmail = userDao.getUserByEmail(userEntity.getEmail());
        if (userEntityByEmail != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        UserEntity userEntityByUserName = userDao.getUserByUserName(userEntity.getUserName());
        if (userEntityByUserName != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }
        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthTokenEntity = new UserAuthEntity();
            userAuthTokenEntity.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthTokenEntity.setUuid(userEntity.getUuid());
            userAuthTokenEntity.setLoginAt(now);
            userAuthTokenEntity.setExpiresAt(expiresAt);

            UserAuthEntity createdUserAuthEntity = userDao.createAuthToken(userAuthTokenEntity);

            return createdUserAuthEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(final String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthEntity == null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        userAuthEntity.setLogoutAt(now);
        userDao.updateUserAuth(userAuthEntity);
        return userAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity userProfile(final String accessToken, final String userId) throws AuthorizationFailedException, UserNotFoundException {
        getUserAuthEntity(accessToken);
        return getUserEntity(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity userDelete(final String accessToken, final String userId) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = getUserEntity(userId);
        if(userEntity.getRole().equals("noadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }
        UserAuthEntity userAuthEntity = getUserAuthEntity(accessToken);
        userDao.deleteUserAuth(userAuthEntity);
        userDao.deleteUser(userEntity);
        return userEntity;
    }

    private UserEntity getUserEntity(String userId) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = userDao.getUserByUuid(userId);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        return userEntity;
    }

    private UserAuthEntity getUserAuthEntity(String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not Signed in");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        long difference = now.compareTo(userAuthEntity.getLogoutAt());

        if(difference > 0) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        return userAuthEntity;
    }

}