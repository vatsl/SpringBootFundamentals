package ttl.larku.controllers.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.ClassService;
import ttl.larku.service.StudentService;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class RegistrationRestControllerTest {

    @Resource
    private RegistrationRestController controller;

    @Resource
    private StudentService studentService;

    @Resource
    private ClassService classService;

    @Autowired
    private WebApplicationContext wac;

    //@Autowired
    private MockMvc mockMvc;
    private DateTimeFormatter dtFormatter;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    }

    @Test
    public void testGetAll() throws Exception {
        ResultActions actions = mockMvc
                .perform(get("/adminrest/class/").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.entity", hasSize(3)));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    public void testGetOne() throws Exception {
        ResultActions actions = mockMvc
                .perform(get("/adminrest/class/1").accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.entity.startDate").value("2012-10-10"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    public void testGetOnePath() throws Exception {
        ResultActions actions = mockMvc
                .perform(get("/adminrest/class/MATH-101/2012-10-10/2013-10-10")
                        .accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        actions = actions.andExpect(status().isOk());

        actions = actions.andExpect(jsonPath("$.entity.startDate").value("2012-10-10"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    public void addOneQueryParams() throws Exception {
        ResultActions actions = mockMvc
                .perform(post("/adminrest/class/")
                        .param("courseCode", "BKTW-101")
                        .param("startDate", "2019-05-05")
                        .param("endDate", "2019-10-05")
                        .accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        actions = actions.andExpect(status().isCreated());

        actions = actions.andExpect(jsonPath("$.entity.startDate").value("2019-05-05"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    public void addOneQueryPath() throws Exception {
        ResultActions actions = mockMvc
                .perform(post("/adminrest/class/{courseCode}/{startDate}/{endDate}",
                        "BKTW-101",
                        "2019-05-05",
                        "2019-10-05")
                        .accept(MediaType.APPLICATION_JSON));
        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        actions = actions.andExpect(status().isCreated());

        actions = actions.andExpect(jsonPath("$.entity.startDate").value("2019-05-05"));

        MvcResult mvcr = actions.andReturn();
        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }

    @Test
    public void testRegisterStudentPath() throws Exception {
        List<Student> students = studentService.getAllStudents();
        List<ScheduledClass> classes = classService.getAllScheduledClasses();
        int studentId = students.get(0).getId();
        int classId = classes.get(0).getId();

        ResultActions actions = mockMvc
                .perform(post("/adminrest/class/register/{studentId}/{classId}", studentId, classId)
                        .accept(MediaType.APPLICATION_JSON));

        actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        actions = actions.andExpect(status().isOk());

        //Get the student and make sure they have to class
        Student s = studentService.getStudent(studentId);
        List<ScheduledClass> sClasses = s.getClasses();

        boolean hasIt = sClasses.stream().anyMatch(sc -> sc.getId() == classId);
        assertTrue(hasIt);

        MvcResult mvcr = actions.andReturn();

        String reo = (String) mvcr.getResponse().getContentAsString();
        System.out.println("Reo is " + reo);
    }
}
