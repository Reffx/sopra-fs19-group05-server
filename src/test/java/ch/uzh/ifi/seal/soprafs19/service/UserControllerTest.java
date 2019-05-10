package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;


    @Test
    public void createUser() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.username", equalTo("testUser")));
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(409));

        userRepository.deleteAll();

    }
    //checks for not registered user that tries to log in
    @Test
    public void UserNotFound() throws Exception {

        this.mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser3\", \"password\": \"secret\"}"))
                .andExpect(status().is(404)).andDo(print());
    }
    // creates user and then uses get method to ask for user
    @Test
    public void getUser() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"));

        this.mvc.perform(get("/users/22"))
                .andExpect(status().is(404));
    }
    //checks if the the online status is set correctly after registration resp. login
    @Test
    public void changeOnlineStatus() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.username", equalTo("testUser")))
                .andExpect(jsonPath("$.status", equalTo("OFFLINE")));

        this.mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(202))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo("ONLINE")));
    }

    @Test
//Checks if a User with the same username can be created
    public void UserAlreadyExistsCheck() throws Exception {

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");



        String creation = "{\"username\" : \"testUser\",\"password\" : \"testPassword\"}";

        this.mvc.perform(post("/users")
                .content(creation)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));

    }
}

