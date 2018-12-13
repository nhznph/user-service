package com.dxc.user.delegate;

import com.dxc.user.api.model.User;
import com.dxc.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDelegateTest {
    private static final String USER_ID = "{\"userId\":001}";
    @InjectMocks
    V1ApiDelegateImpl v1ApiDelegate;

    @Mock
    UserService userService;

    @Test
    public  void deleteUser(){
        when(userService.deleteUser(anyString())).thenReturn(USER_ID);
        ResponseEntity<String> res = v1ApiDelegate.deleteUser(USER_ID);
        Assert.assertEquals(ResponseEntity.ok(userService.deleteUser(USER_ID)),res);
    }

    @Test
    public  void getAllUser(){
        User user1 = new User();
        User user2 = new User();
        List<User> list = Arrays.asList(user1, user2);
        when(userService.getAllUser()).thenReturn(list);
        ResponseEntity<List<User>> actual = v1ApiDelegate.getAllUser();
        Assert.assertEquals(ResponseEntity.ok(userService.getAllUser()),actual);

    }
}
