package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.controller.NonExistentUserException;
import ch.uzh.ifi.seal.soprafs19.controller.DuplicateException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("test");
        testUser.setBirthday("16.03.1994");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
        userRepository.deleteAll();
    }

    @Test(expected = NonExistentUserException.class)
    public void checkUserErr() {
        userRepository.deleteAll();
        User testUser = new User();

        userService.checkUser(testUser); //try to log in a user that wasn't created before
    }

    @Test(expected = DuplicateException.class)
    public void createUserErr() {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("test");
        testUser.setBirthday("16.03.1994");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUsername");
        testUser2.setPassword("test2");
        testUser2.setBirthday("16.03.1995");
        User createdUser2 = userService.createUser(testUser2);
        userRepository.deleteAll();
    }

    //this test will check the logout method from the user service (mainly if the status is set to offline)
    @Test
    public void logoutUser(){
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("test");
        testUser.setBirthday("16.03.1994");

        User createdUser = userService.createUser(testUser);
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);

        User onlineUser = userService.checkUser(createdUser);
        Assert.assertEquals(onlineUser.getStatus(),UserStatus.ONLINE);

        User offlineUser = userService.logoutUser(onlineUser);
        Assert.assertEquals(offlineUser.getStatus(),UserStatus.OFFLINE);

        userRepository.deleteAll();
    }


     @Test(expected = NullPointerException.class)
    public void logoutUserErr(){
         userRepository.deleteAll();
         User testUser = new User();
         testUser.setUsername("testUsername");
         testUser.setPassword("test");
         testUser.setBirthday("16.03.1994");
         User createdUser = userService.createUser(testUser);
         User onlineUser1 = userService.checkUser(createdUser);
         User offlineUser1 = userService.logoutUser(onlineUser1);
         userRepository.deleteAll();
         User testUser2 = new User();
         testUser2.setUsername("testUsername2");
         testUser2.setPassword("test2");
         testUser2.setBirthday("16.03.1995");
         User createdUser2 = userService.createUser(testUser2);
         User onlineUser2 = userService.checkUser(createdUser2);
         User offlineUser2 = userService.logoutUser(onlineUser2);
         userRepository.deleteAll();
         User testUser3 = new User();
         userService.logoutUser(testUser3);

         userRepository.deleteAll();
     }

     @Test
    public void getUsers(){
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         User testUser = new User();
         testUser.setUsername("testUsername");
         testUser.setPassword("test");
         testUser.setBirthday("16.03.1994");

         User createdUser = userService.createUser(testUser);

         User testUser2 = new User();
         testUser2.setUsername("testUsername2");
         testUser2.setPassword("test2");
         testUser2.setBirthday("16.03.1995");
         User createdUser2 = userService.createUser(testUser2);

         Assert.assertNotNull(createdUser.getToken());
         Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
         Assert.assertNotNull(createdUser2.getToken());
         Assert.assertEquals(createdUser2.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdUser2, userRepository.findByToken(createdUser2.getToken()));

         Assert.assertNotNull(userService.getUsers());
         userRepository.deleteAll();
         //check if getUsers will return an empty arraylist after userRepo is cleared
         Assert.assertEquals(userService.getUsers(), new ArrayList<>());
     }

     @Test
    public void getUser(){
        userRepository.deleteAll();
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         User testUser = new User();
         testUser.setUsername("testUsername");
         testUser.setPassword("test");
         testUser.setBirthday("16.03.1994");

         User createdUser = userService.createUser(testUser);

         Assert.assertNotNull(createdUser.getToken());
         Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));

         User foundUser = userService.getUser(userRepository.findByUsername(createdUser.getUsername()).getId());
         Assert.assertNotNull(foundUser);
         Assert.assertEquals(createdUser, foundUser);
         userRepository.deleteAll();
     }

     @Test(expected = NullPointerException.class)
    public void getUserErr(){
         userRepository.deleteAll();
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         User testUser = new User();
         testUser.setUsername("testUsername");
         testUser.setPassword("test");
         testUser.setBirthday("16.03.1994");

         User createdUser = userService.createUser(testUser);

         Assert.assertNotNull(createdUser.getToken());
         Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));

         User foundUser = userService.getUser(userRepository.findByUsername(createdUser.getUsername()).getId());
         Assert.assertNotNull(foundUser);
         Assert.assertEquals(createdUser, foundUser);
         userRepository.deleteAll();
         //check if createdUser is deleted from repo --> findBy methods should return null pointer exception
         userService.getUser(userRepository.findByUsername(createdUser.getUsername()).getId());
     }

     @Test
    public void updateUser(){
         userRepository.deleteAll();
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         User testUser = new User();
         testUser.setUsername("testUsername");
         testUser.setPassword("test");
         testUser.setBirthday("16.03.1994");

         User createdUser = userService.createUser(testUser);
         Assert.assertNotNull(createdUser.getToken());
         Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));

         createdUser.setUsername("newUsername");
         createdUser.setBirthday("24.12.1998");
         User updatedUser = userService.updateUser(createdUser);
         Assert.assertNotNull(updatedUser.getToken());
         Assert.assertEquals(updatedUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(updatedUser, userRepository.findByToken(updatedUser.getToken()));
         //check if new username equals the "newUsername" and not the old one "testUsername"
         Assert.assertEquals(updatedUser.getUsername(), "newUsername");
         Assert.assertNotEquals(updatedUser.getUsername(), "testUsername");
         userRepository.deleteAll();
     }
}
