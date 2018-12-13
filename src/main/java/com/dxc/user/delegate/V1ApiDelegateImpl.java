package com.dxc.user.delegate;

import com.dxc.user.api.V1ApiDelegate;
import com.dxc.user.api.model.User;
import com.dxc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class V1ApiDelegateImpl implements V1ApiDelegate {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> addUser(User body) {
        return ResponseEntity.ok(userService.addUser(body));
    }

    @Override
    public ResponseEntity<String> deleteUser(String id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @Override
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }


    @Override
    public ResponseEntity<User> searchUserByID(String id) {
        return ResponseEntity.ok(userService.searchUserById(id));
    }

    @Override
    public ResponseEntity<List<User>> searchUserByName(String firstName, String lastName) {
        return ResponseEntity.ok(userService.searchByName(firstName, lastName));
    }

    @Override
    public ResponseEntity<String> setLimitedMonthly(String userId, String limit) {
        return ResponseEntity.ok(userService.setLimitedMonthly(userId, limit));
    }

    @Override
    public ResponseEntity<String> updateActivateUser(String userId) {
        return ResponseEntity.ok(userService.updateActiveUser(userId));
    }

    @Override
    public ResponseEntity<String> updateDeactivateUser(String userId) {
        return ResponseEntity.ok(userService.updateDeactiveUser(userId));
    }

    @Override
    public ResponseEntity<String> updateUser(String id, User body) {
        return ResponseEntity.ok(userService.updateUser(id, body));
    }
}
