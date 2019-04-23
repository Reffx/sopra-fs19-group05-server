package ch.uzh.ifi.seal.soprafs19.REST;

import ch.uzh.ifi.seal.soprafs19.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.core.IsNull.notNullValue;
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
public class CreatePlayerTest {

    @Autowired
    private MockMvc mvc;

    //first version of create player mvc test
    @Test
    public void createPlayer() throws Exception {

        this.mvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"1\"}"))
                .andExpect(status().is(201)).andDo(print());
    }
}
