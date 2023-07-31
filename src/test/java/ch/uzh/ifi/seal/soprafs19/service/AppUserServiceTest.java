package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Main;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;

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
@SpringBootTest(classes= Main.class)
public class AppUserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));
        AppUser testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setPassword("test");
        testAppUser.setBirthday("16.03.1994");

        AppUser createdAppUser = userService.createUser(testAppUser);

        Assert.assertNotNull(createdAppUser.getToken());
        Assert.assertEquals(createdAppUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdAppUser, userRepository.findByToken(createdAppUser.getToken()));
        userRepository.deleteAll();
    }

    @Test(expected = DuplicateException.class)
    public void createUserErr() {
        userRepository.deleteAll();
        AppUser testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setPassword("test");
        testAppUser.setBirthday("16.03.1994");
        AppUser createdAppUser = userService.createUser(testAppUser);

        AppUser testAppUser2 = new AppUser();
        testAppUser2.setUsername("testUsername");
        testAppUser2.setPassword("test2");
        testAppUser2.setBirthday("16.03.1995");
        AppUser createdAppUser2 = userService.createUser(testAppUser2);
        userRepository.deleteAll();
    }

    //this test will check the logout method from the user service (mainly if the status is set to offline)
    @Test
    public void logoutUser(){
        userRepository.deleteAll();
        AppUser testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setPassword("test");
        testAppUser.setBirthday("16.03.1994");

        AppUser createdAppUser = userService.createUser(testAppUser);
        Assert.assertEquals(createdAppUser.getStatus(),UserStatus.OFFLINE);

        AppUser onlineAppUser = userService.checkUser(createdAppUser);
        Assert.assertEquals(onlineAppUser.getStatus(),UserStatus.ONLINE);

        AppUser offlineAppUser = userService.logoutUser(onlineAppUser);
        Assert.assertEquals(offlineAppUser.getStatus(),UserStatus.OFFLINE);

        userRepository.deleteAll();
    }

    @Test(expected = NullPointerException.class)
    public void logoutUserErr(){
         userRepository.deleteAll();
         AppUser testAppUser = new AppUser();
         testAppUser.setUsername("testUsername");
         testAppUser.setPassword("test");
         testAppUser.setBirthday("16.03.1994");
         AppUser createdAppUser = userService.createUser(testAppUser);
         AppUser onlineAppUser1 = userService.checkUser(createdAppUser);
         AppUser offlineAppUser1 = userService.logoutUser(onlineAppUser1);
         userRepository.deleteAll();
         AppUser testAppUser2 = new AppUser();
         testAppUser2.setUsername("testUsername2");
         testAppUser2.setPassword("test2");
         testAppUser2.setBirthday("16.03.1995");
         AppUser createdAppUser2 = userService.createUser(testAppUser2);
         AppUser onlineAppUser2 = userService.checkUser(createdAppUser2);
         AppUser offlineAppUser2 = userService.logoutUser(onlineAppUser2);
         userRepository.deleteAll();
         AppUser testAppUser3 = new AppUser();
         userService.logoutUser(testAppUser3);

         userRepository.deleteAll();
     }

     @Test
    public void getUsers(){
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         AppUser testAppUser = new AppUser();
         testAppUser.setUsername("testUsername");
         testAppUser.setPassword("test");
         testAppUser.setBirthday("16.03.1994");

         AppUser createdAppUser = userService.createUser(testAppUser);

         AppUser testAppUser2 = new AppUser();
         testAppUser2.setUsername("testUsername2");
         testAppUser2.setPassword("test2");
         testAppUser2.setBirthday("16.03.1995");
         AppUser createdAppUser2 = userService.createUser(testAppUser2);

         Assert.assertNotNull(createdAppUser.getToken());
         Assert.assertEquals(createdAppUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdAppUser, userRepository.findByToken(createdAppUser.getToken()));
         Assert.assertNotNull(createdAppUser2.getToken());
         Assert.assertEquals(createdAppUser2.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdAppUser2, userRepository.findByToken(createdAppUser2.getToken()));

         Assert.assertNotNull(userService.getUsers());
         userRepository.deleteAll();
         //check if getUsers will return an empty arraylist after userRepo is cleared
         Assert.assertEquals(userService.getUsers(), new ArrayList<>());
     }

     @Test
    public void getUser(){
        userRepository.deleteAll();
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         AppUser testAppUser = new AppUser();
         testAppUser.setUsername("testUsername");
         testAppUser.setPassword("test");
         testAppUser.setBirthday("16.03.1994");

         AppUser createdAppUser = userService.createUser(testAppUser);

         Assert.assertNotNull(createdAppUser.getToken());
         Assert.assertEquals(createdAppUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdAppUser, userRepository.findByToken(createdAppUser.getToken()));

         AppUser foundAppUser = userService.getUser(userRepository.findByUsername(createdAppUser.getUsername()).getId());
         Assert.assertNotNull(foundAppUser);
         Assert.assertEquals(createdAppUser, foundAppUser);
         userRepository.deleteAll();
     }

     @Test(expected = NullPointerException.class)
    public void getUserErr(){
         userRepository.deleteAll();
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         AppUser testAppUser = new AppUser();
         testAppUser.setUsername("testUsername");
         testAppUser.setPassword("test");
         testAppUser.setBirthday("16.03.1994");

         AppUser createdAppUser = userService.createUser(testAppUser);

         Assert.assertNotNull(createdAppUser.getToken());
         Assert.assertEquals(createdAppUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdAppUser, userRepository.findByToken(createdAppUser.getToken()));

         AppUser foundAppUser = userService.getUser(userRepository.findByUsername(createdAppUser.getUsername()).getId());
         Assert.assertNotNull(foundAppUser);
         Assert.assertEquals(createdAppUser, foundAppUser);
         userRepository.deleteAll();
         //check if createdUser is deleted from repo --> findBy methods should return null pointer exception
         userService.getUser(userRepository.findByUsername(createdAppUser.getUsername()).getId());
     }

     @Test
    public void updateUser(){
         userRepository.deleteAll();
         Assert.assertNull(userRepository.findByUsername("testUsername"));
         AppUser testAppUser = new AppUser();
         testAppUser.setUsername("testUsername");
         testAppUser.setPassword("test");
         testAppUser.setBirthday("16.03.1994");

         AppUser createdAppUser = userService.createUser(testAppUser);
         Assert.assertNotNull(createdAppUser.getToken());
         Assert.assertEquals(createdAppUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(createdAppUser, userRepository.findByToken(createdAppUser.getToken()));

         createdAppUser.setUsername("newUsername");
         createdAppUser.setBirthday("24.12.1998");
         AppUser updatedAppUser = userService.updateUser(createdAppUser);
         Assert.assertNotNull(updatedAppUser.getToken());
         Assert.assertEquals(updatedAppUser.getStatus(),UserStatus.OFFLINE);
         Assert.assertEquals(updatedAppUser, userRepository.findByToken(updatedAppUser.getToken()));
         //check if new username equals the "newUsername" and not the old one "testUsername"
         Assert.assertEquals(updatedAppUser.getUsername(), "newUsername");
         Assert.assertNotEquals(updatedAppUser.getUsername(), "testUsername");
         userRepository.deleteAll();
     }

    @Test(expected = DuplicateException.class)
    public void updateUserErr() {
        userRepository.deleteAll();
        Assert.assertNull(userRepository.findByUsername("testUsername"));
        AppUser testAppUser = new AppUser();
        testAppUser.setUsername("testUsername");
        testAppUser.setPassword("test");
        testAppUser.setBirthday("16.03.1994");

        AppUser createdAppUser = userService.createUser(testAppUser);
        Assert.assertNotNull(createdAppUser.getToken());
        Assert.assertEquals(createdAppUser.getStatus(), UserStatus.OFFLINE);
        Assert.assertEquals(createdAppUser, userRepository.findByToken(createdAppUser.getToken()));

        AppUser testAppUser2 = new AppUser();
        testAppUser2.setUsername("testUsername2");
        testAppUser2.setPassword("test2");
        testAppUser2.setBirthday("16.03.1995");
        AppUser createdAppUser2 = userService.createUser(testAppUser2);
        Assert.assertNotNull(createdAppUser2.getToken());
        Assert.assertEquals(createdAppUser2.getStatus(), UserStatus.OFFLINE);
        Assert.assertEquals(createdAppUser2, userRepository.findByToken(createdAppUser2.getToken()));
        //seeting username to testUsername2 which is already used by testUser2 --> updating user will lead to error
        createdAppUser.setUsername("testUsername2");
        createdAppUser.setBirthday("24.12.1998");

        AppUser updatedAppUser = userService.updateUser(createdAppUser);

        Assert.assertNotNull(updatedAppUser.getToken());
        Assert.assertEquals(updatedAppUser.getStatus(), UserStatus.OFFLINE);
        Assert.assertEquals(updatedAppUser, userRepository.findByToken(updatedAppUser.getToken()));
        //check if new username equals the "newUsername" and not the old one "testUsername"
        userRepository.deleteAll();
    }
}

