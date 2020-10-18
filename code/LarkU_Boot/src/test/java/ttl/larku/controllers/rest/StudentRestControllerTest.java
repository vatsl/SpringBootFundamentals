package ttl.larku.controllers.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.Student;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentRestControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private final int goodStudentId = 1;
    private final int badStudentId = 10000;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetOneStudentGoodJson() throws Exception {
        MediaType accept = MediaType.APPLICATION_JSON_UTF8;
        MockHttpServletRequestBuilder request = get("/adminrest/student/{id}", goodStudentId);

        List<ResultMatcher> matchers = Arrays.asList(status().isOk(), content().contentType(accept),
                jsonPath("$.status").value("Ok"),
                jsonPath("$.entity.name", Matchers.containsString("Manoj")));

        String jsonString = makeCall(request, accept, accept, matchers, null);
        System.out.println("One student good resp = " + jsonString);
    }

	/*-
	@Test
	public void testGetOneStudentGoodXml() throws Exception {
		MediaType accept = MediaType.APPLICATION_XML;
	
		MockHttpServletRequestBuilder request = get("/adminrest/student/{id}", goodStudentId);
	
		List<ResultMatcher> matchers = Arrays.asList(status().isOk(), 
				content().contentType(accept),
				xpath("/entity/name").string("Manoj"));
	
		String jsonString = makeCall(request, accept, accept, matchers, null);
		System.out.println("resp = " + jsonString);
	
	}
	*/

    @Test
    public void testGetOneStudentBadId() throws Exception {

        ResultActions actions = mockMvc
                .perform(get("/adminrest/student/{id}", badStudentId).accept(MediaType.APPLICATION_JSON));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
        assertTrue(reo.matches(".*Student with id: " + badStudentId + ".*"));
    }

    @Test
    public void testAddStudentGood() throws Exception {

        Student student = new Student("Yogita");
        student.setPhoneNumber("202 383-9393");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(student);

        ResultActions actions = mockMvc.perform(post("/adminrest/student/").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(jsonString));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        actions = actions.andExpect(status().isCreated());
        actions = actions.andExpect(jsonPath("$.entity.name").value("Yogita"));

        MvcResult result = actions.andReturn();
        MockHttpServletResponse response = result.getResponse();

        jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);

    }

    @Test
    public void testUpdatePartial() throws Exception {
        ResultActions actions = mockMvc
                .perform(get("/adminrest/student/{id}", goodStudentId).accept(MediaType.APPLICATION_JSON));

        MvcResult mvcr = actions.andReturn();
        String origString = mvcr.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        //Convert to object
        RestResult origStudent = mapper.readValue(origString, RestResult.class);

        String newNameString = "{ \"name\":\"Cassandra\" }";


        actions = mockMvc.perform(patch("/adminrest/student/{id}", goodStudentId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newNameString));

        actions = actions.andExpect(status().isNoContent());

        MvcResult result = actions.andReturn();

        actions = mockMvc
                .perform(get("/adminrest/student/{id}", goodStudentId).accept(MediaType.APPLICATION_JSON));

        mvcr = actions.andReturn();
        String newString = mvcr.getResponse().getContentAsString();
        JsonNode newRoot = mapper.readTree(newString);
        JsonNode entity = newRoot.path("entity");
        //Convert to object to update
        Student newStudent = mapper.readerFor(Student.class).readValue(entity);
        System.out.println("newStudent = " + newStudent);
        assertEquals("Cassandra", newStudent.getName());

    }


    @Test
    public void testGetAllStudentsGood() throws Exception {

        ResultActions actions = mockMvc.perform(get("/adminrest/student/").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.entity", hasSize(4)));
        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();

        String jsonString = response.getContentAsString();
        System.out.println("resp = " + jsonString);
    }

    private String makeCall(MockHttpServletRequestBuilder builder, MediaType accept, MediaType contentType,
                            List<ResultMatcher> matchers, String content) throws Exception {
        if (accept != null) {
            builder = builder.accept(accept);
        }
        if (contentType != null) {
            builder = builder.contentType(contentType);
        }
        if (content != null) {
            builder = builder.content(content);
        }

        ResultActions actions = mockMvc.perform(builder);

        for (ResultMatcher m : matchers) {
            actions = actions.andExpect(m);
        }

        // Get the result and return it
        MvcResult result = actions.andReturn();

        MockHttpServletResponse response = result.getResponse();
        String jsonString = response.getContentAsString();

        return jsonString;
    }
}
