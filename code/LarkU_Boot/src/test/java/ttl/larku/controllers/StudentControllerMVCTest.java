package ttl.larku.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentControllerMVCTest {

    @Resource(name = "studentController")
    private StudentController studentController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";

    private String code1 = "BOT-101";
    private String code2 = "BOT-202";
    private String title1 = "Intro To Botany";
    private String title2 = "Outtro To Botany";

    private String startDate1 = "10/10/2012";
    private String startDate2 = "10/10/2013";
    private String endDate1 = "05/10/2013";
    private String endDate2 = "05/10/2014";

    private int id1, id2;

    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;
    private Student student1;
    private Student student2;

    private String postedName = "postedName";

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetStudent() throws Exception {
        ResultActions actions = mockMvc.perform(get("/admin/getStudent").param("id", "1"));

        actions = actions.andExpect(status().isOk());
        actions = actions.andExpect(view().name("showStudent"));
        actions = actions.andExpect(model().attribute("student",
                hasProperty("name", containsString("Manoj"))));
    }

    @Test
    public void testStudentNotFoundGet() throws Exception {
        ResultActions actions = mockMvc.perform(get("/admin/getStudent").param("id", Integer.MAX_VALUE + ""));

        actions = actions.andExpect(status().isOk());
        actions = actions.andExpect(view().name("error/studentNotFound"));
    }

    @Test
    public void testGetStudentTheHardWay() throws Exception {
        ResultActions actions = mockMvc.perform(get("/admin/getStudentTheHardWay").param("id", "1"));

        actions = actions.andExpect(status().isOk());
        actions = actions.andExpect(view().name("showStudent"));
        actions = actions.andExpect(model().attribute("student", hasProperty("name",
                containsString("Manoj"))));
    }

    @Test
    public void testGetNonExistingStudentTheHardWay() throws Exception {
        ResultActions actions = mockMvc.perform(get("/admin/getStudent").param("id", Integer.MAX_VALUE + ""));

        actions = actions.andExpect(status().isOk());
        actions = actions.andExpect(view().name("error/studentNotFound"));
    }

    @Test
    public void testGetStudents() throws Exception {
        ResultActions actions = mockMvc.perform(get("/admin/getStudents"));

        actions = actions.andExpect(status().isOk());
        actions = actions.andExpect(view().name("showStudents"));
        actions = actions.andExpect(model().attribute("students", hasSize(greaterThanOrEqualTo(3))));
    }


    @Test
    public void testShowAddForm() throws Exception {
        ResultActions actions = mockMvc.perform(get("/admin/addStudent"));

        actions = actions.andExpect(status().isOk());
        actions = actions.andExpect(view().name("addStudent"));
        actions = actions.andExpect(model().attribute("student", hasProperty("name", equalTo("No Name"))));
    }

    @Test
    public void testAddStudent() throws Exception {
        String newStudentName = "Jay Jay";
        ResultActions actions = mockMvc.perform(post("/admin/addStudent").param("name", newStudentName));

        actions = actions.andExpect(status().is3xxRedirection());
        actions = actions.andExpect(view().name("redirect:getStudents"));

        actions = mockMvc.perform(get("/admin/getStudents"));

        MvcResult result = actions.andReturn();
        ModelAndView mav = result.getModelAndView();
        List<Student> students = (List) mav.getModel().get("students");
        boolean foundNew = false;
        for (Student student : students) {
            if (student.getName().equals(newStudentName)) {
                foundNew = true;
                break;
            }
        }

        assertTrue("Did not find New Student", foundNew);
    }

    @Test
    public void testAddStudentWithErrors() {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addStudent");
        request.setParameter("name", postedName);

        // new instance of "Post" object
        Student student = new Student();

        // give it to the binder
        WebDataBinder binder = new WebDataBinder(student);

        // And have the binder set the values on the object based on the
        // parameter of the "Post"
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        BindingResult errors = new AbstractBindingResult("blah") {

            @Override
            protected Object getActualFieldValue(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object getTarget() {
                // TODO Auto-generated method stub
                return null;
            }

        };

        errors.addError(new ObjectError("Boo", "hoo"));
        ModelAndView result = studentController.addStudent((Student) binder.getTarget(), errors);

        assertEquals("errorPage", result.getViewName());
    }

    boolean completeCalled = false;

	/*
	@Test
	public void testAddStudentWithSessionAttributes() throws Exception {

		String newStudentName = "Janice Jay";
		String sAttrName = "sessionAttr";
		String sAttrValue = "sessionValue";
		MockHttpServletRequestBuilder builder = post("/admin/addStudent2")
				.param("name", newStudentName)
				.sessionAttr(sAttrName, sAttrValue);

		ResultActions actions = mockMvc.perform(builder);

		actions = actions.andExpect(status().is3xxRedirection());
		actions = actions.andExpect(view().name("redirect:getStudents"));
		actions = actions.andExpect(model().attributeExists(sAttrName));
	}

	@Test
	public void testAddStudentWithSessionAttributesCompleted() throws Exception {

		String newStudentName = "Janice Jay";
		String sAttrName = "sessionAttr";
		String sAttrValue = "sessionValue";
		MockHttpServletRequestBuilder builder = post("/admin/addStudent2")
				.param("name", newStudentName)
				.param("addStudentDone", "true")
				.sessionAttr(sAttrName, sAttrValue);

		ResultActions actions = mockMvc.perform(builder);

		actions = actions.andExpect(status().is3xxRedirection());
		actions = actions.andExpect(view().name("redirect:getStudents"));
		actions = actions.andExpect(model().attributeDoesNotExist(sAttrName));
	}
	*/

}
