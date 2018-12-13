package com.dxc.user.Service;


import com.dxc.user.api.model.User;
import com.dxc.user.common.UserError;
import com.dxc.user.entity.UserEntity;
import com.dxc.user.exception.UserException;
import com.dxc.user.repository.UserRepository;
import com.dxc.user.service.UserService;
import javafx.beans.binding.When;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

//    {
//        "activated": false,
//        "address": "70 lth gv",
//        "email": "nhanzks165@gmail.com",
//        "firstName": "Nhan",
//        "id": "001",
//        "lastName": "Phan"
//    }
    private static final String ID = "001";
    private static final String FIRST_NAME = "Nhan";
    private static final String LAST_NAME = "Phan";
    private static final String EMAIL = "nhanzks165@gmail.com";
    private static final String ADDRESS = "lth";
    private static final Integer LIMIT = 20000000;
    private static final boolean ACTIVATED = false;
    private static final List<UserEntity> listUserEntiry = new ArrayList<>();
    //    private static final String JSON = "[{\"id\":001,\"firstName\":Nhan,\"lastName\":Phan,\"email\":nhan@,\"address\":lth,\"activated\":false}]";
    @InjectMocks
    UserService userService;


    @Mock
    UserEntity userEntity;

    @Mock
    UserRepository userRepository;

    @Mock
    User user;

//    @Rule
//    public UserException userException = new UserException(UserError.USER_INVALID);

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setId(ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setAddress(ADDRESS);
        user.setActivated(ACTIVATED);
        user.setLimited(LIMIT);

    }

    @Test
    public  void addUser(){
        doAnswer(invocation -> {
            userEntity = invocation.getArgument(0);
            userEntity.setId(ID);
            userEntity.setFirstName(FIRST_NAME);
            userEntity.setLastName(LAST_NAME);
            userEntity.setEmail(EMAIL);
            userEntity.setAddress(ADDRESS);
            userEntity.setActivated(ACTIVATED);
            userEntity.setLimited(LIMIT);
            return null;
        }).when(userRepository).saveAndFlush(any(UserEntity.class));
        when(userRepository.findByUserExist(ID)).thenReturn(null);
        String res = userService.addUser(user);
        Assert.assertEquals(userEntity.getId(), res);
    }

    @Test
    public  void addUserExist(){
        when(userRepository.findByUserExist(ID)).thenReturn(ID);
        Assert.assertEquals("Id exist!!",userService.addUser(user));
    }

    @Test
    public  void readUser(){
        when(userRepository.findByUserId(ID)).thenReturn(userEntity);
        User userActual = userService.searchUserById(ID);
        user = entity2User(userEntity);
       if(userService.searchUserById(ID) != null){
           Assert.assertEquals(user,userActual);
       }

    }
    @Test(expected = UserException.class)
    public  void readUserNull() {
        when(userRepository.findByUserId(ID)).thenThrow(new UserException(UserError.USER_INVALID));
        User user = userService.searchUserById(ID);
        Assert.assertEquals(new UserException(UserError.USER_INVALID),user);
    }

    @Test(expected = UserException.class)
    public  void readAllNull(){
        when(userRepository.findAllUser()).thenThrow(new UserException(UserError.NOT_FOUND));
        Assert.assertEquals(new UserException(UserError.NOT_FOUND),userService.getAllUser());
    }

    @Test
    public  void readAllList()  {
        when(userRepository.findAllUser()).thenReturn(listUserEntiry);
        List<User> userT = userService.getAllUser();
        Assert.assertEquals(listUserEntiry,userT);
    }

    @Test
    public  void updateUserFail(){
        when(userRepository.findByUserId(ID)).thenReturn(null);
        String actual = userService.updateUser(ID,user);
        Assert.assertEquals("Id of user does not exist!!",actual);
    }

    @Test
    public  void  updateUserSuccess(){
        when(userRepository.findByUserId(ID)).thenReturn(userEntity);
        String actual = userService.updateUser(ID,user);
        Assert.assertEquals("update successfully" +" at " +ID,actual);
    }

    @Test
    public  void deleteFail(){
        when(userRepository.markDeletedByUser(ID)).thenReturn(0);
        String  actual =userService.deleteUser(ID);
        Assert.assertEquals("Id of user does not exist or deleted before",actual);
    }

    @Test
    public void deleteSuccess(){
        when(userRepository.markDeletedByUser(ID)).thenReturn(1);
        String actual = userService.deleteUser(ID);
        Assert.assertEquals("delete successfully " + ID,actual);
    }

    @Test
    public  void searchByNameList(){
        when(userRepository.findByFirstNameStartingWithAndLastNameEndingWith(FIRST_NAME,LAST_NAME)).thenReturn(listUserEntiry);
        List<User> listActual =userService.searchByName(FIRST_NAME,LAST_NAME);
        Assert.assertEquals(listUserEntiry,listActual);
    }

    @Test(expected = UserException.class)
    public  void searchByNameNull(){
        when(userRepository.findByFirstNameStartingWithAndLastNameEndingWith(FIRST_NAME,LAST_NAME)).thenThrow(new UserException(UserError.NOT_FOUND));
        Assert.assertEquals(new UserException(UserError.NOT_FOUND),userService.searchByName(FIRST_NAME,LAST_NAME));
    }

    @Test
    public  void activateUserSucess(){
        when(userRepository.markActivedByUser(ID)).thenReturn(1);
        String actual = userService.updateActiveUser(ID);
        Assert.assertEquals(ID,actual);
    }

    @Test
    public  void activateUserFail(){
        when(userRepository.markActivedByUser(ID)).thenReturn(0);
        String actual = userService.updateActiveUser(ID);
        Assert.assertEquals("User does not exist or userId activated",actual);
    }

    @Test
    public  void deActivateUserSuccess(){
        when(userRepository.markDeActivedByUser(ID)).thenReturn(1);
        String actual = userService.updateDeactiveUser(ID);
        Assert.assertEquals(ID,actual);
    }

    @Test
    public  void deActivateUserFail(){
        when(userRepository.markDeActivedByUser(ID)).thenReturn(0);
        String actual = userService.updateDeactiveUser(ID);
        Assert.assertEquals("User does not exist or userId DeActivated",actual);
    }

   @Test
   public  void setLimitedMonthlySuccess(){
        when(userRepository.findByUserId(ID)).thenReturn(userEntity);
        String actual = userService.setLimitedMonthly(ID,String.valueOf(LIMIT));
        Assert.assertEquals("set the monthly limited at " + ID, actual );
   }

   @Test
   public  void setLimitMonthlyFail(){
        when(userRepository.findByUserId(ID)).thenReturn(null);
        String actual = userService.setLimitedMonthly(ID,String.valueOf(LIMIT));
        Assert.assertEquals("Id of user does not exist",actual);
   }


    private User entity2User(UserEntity uE){
        User user = new User();
        user.setId(uE.getId());
        user.setFirstName(uE.getFirstName());
        user.setLastName(uE.getLastName());
        user.setEmail(uE.getEmail());
        user.setAddress(uE.getAddress());
        user.setActivated(uE.isActivated());
        user.setLimited(uE.getLimited());
        return  user;
    }
}
