package com.dxc.user.service;


import com.dxc.user.api.model.User;
import com.dxc.user.common.UserError;
import com.dxc.user.entity.UserEntity;
import com.dxc.user.exception.UserException;
import com.dxc.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String addUser(User user) {
        UserEntity userEntity = new UserEntity();
        String idCheck = userRepository.findByUserExist(user.getId());
        if (idCheck != null) {
            return "Id exist!!";
        } else {
            userEntity.setId(user.getId());
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setEmail(user.getEmail());
            userEntity.setAddress(user.getAddress());
            userEntity.setActivated(false);
            userEntity.setDeleted(false);
            userEntity.setCreateDate(new Date());
            userEntity.setModifiedDate(new Date());
            userEntity.setLimited(0);
            userRepository.saveAndFlush(userEntity);
            return userEntity.getId();
        }
    }

    public List<User> getAllUser() {
        List<UserEntity> userEntities = userRepository.findAllUser();
        if (userEntities == null) {
            throw new UserException(UserError.NOT_FOUND);
        }
        return userEntities.stream().map(this::entity2User).collect(Collectors.toList());
    }

    public User entity2User(UserEntity uE) {
        User user = new User();
        user.setId(uE.getId());
        user.setFirstName(uE.getFirstName());
        user.setLastName(uE.getLastName());
        user.setEmail(uE.getEmail());
        user.setAddress(uE.getAddress());
        user.setActivated(uE.isActivated());
        user.setLimited(uE.getLimited());
        return user;
    }

    public User searchUserById(String id) {
        UserEntity uE = userRepository.findByUserId(id);
        if (uE == null) {
            throw new UserException(UserError.USER_INVALID, id);
        }
        return entity2User(uE);
    }

    @Transactional
    public String updateUser(String id, User user) {
        UserEntity uE = userRepository.findByUserId(id);
        if (uE != null) {
            uE.setFirstName(user.getFirstName());
            uE.setLastName(user.getLastName());
            uE.setEmail(user.getEmail());
            uE.setAddress(user.getAddress());
            uE.setModifiedDate(new Date());
            userRepository.saveAndFlush(uE);
            return "update successfully" + " at " + id;
        }
        return "Id of user does not exist!!";
    }

    @Transactional
    public String deleteUser(String id) {
        int i = userRepository.markDeletedByUser(id);
        if (i <= 0) {
            return "Id of user does not exist or deleted before";
        }
        return "delete successfully " + id;
    }

    public List<User> searchByName(String firstName, String lastName) {
        List<UserEntity> userEntity = userRepository.findByFirstNameStartingWithAndLastNameEndingWith(firstName, lastName);
        if (userEntity == null) {
            throw new UserException(UserError.NOT_FOUND, firstName, lastName);
        }
        return userEntity.stream().map(this::entity2User).collect(Collectors.toList());
    }

    //admin
    @Transactional
    public String updateActiveUser(String userId) {
        int mark = userRepository.markActivedByUser(userId);
        if (mark <= 0) {
            return "User does not exist or userId activated";
        }
        return userId;
    }

    @Transactional
    public String updateDeactiveUser(String userId) {
        int mark = userRepository.markDeActivedByUser(userId);
        if (mark <= 0) {
            return "User does not exist or userId DeActivated";
        }
        return userId;

    }

    @Transactional
    public String setLimitedMonthly(String id, String limit) {
        UserEntity uE = userRepository.findByUserId(id);
        if (uE != null) {
            uE.setLimited(Integer.valueOf(limit));
            userRepository.saveAndFlush(uE);
            return "set the monthly limited at " + id;
        }
        return "Id of user does not exist";
    }
}
