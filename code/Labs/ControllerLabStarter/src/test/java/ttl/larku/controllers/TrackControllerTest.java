package ttl.larku.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
//TODO - Add an annotation to make this a spring boot test


//TODO - uncomment out the next line to enable MockMVC
//@AutoConfigureMockMvc


//TODO - set up a way to recreate the context after tests that change
// the state of the database.
public class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() { }

    @Test
    public void testGetAll() throws Exception {

        //TODO - Initialize ResultActions with the result of a mockMvc call to get one track
        //     - Look at the main line projects for examples
        ResultActions actions = null;

        //TODO - Add an expectation to check that the status is Ok


        //TODO - Add an expectation to check the size of the returned collection



        MvcResult result = actions.andReturn();
        System.out.println("result is " + result);
    }

    //Etc. Etc.
}
