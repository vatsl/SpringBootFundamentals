package ttl.larku.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.ScheduledClass;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class RegistrationControllerTest {

    @Resource(name = "courseController")
    private CourseController courseController;

    @Resource(name = "registrationController")
    private RegistrationController regController;

    @Resource
    private ApplicationContext appContext;

    @Test
    public void testGetAllClasses() {
        ModelAndView result = regController.getAllClasses();
        assertEquals("showClasses", result.getViewName());
    }

    @Test
    public void testShowAddClassForm() {
        ModelAndView result = regController.showAddClassForm(new ScheduledClass());
        assertEquals("addClass", result.getViewName());
    }

    @Test
    public void testAddClass() {
        ModelAndView result = regController.addClass("MATH-101", "10/10/2016", "10/10/2017");
        assertEquals("redirect:getAllClasses", result.getViewName());
    }


    @Test
    public void testShowRegisterStudentForm() {
        ModelAndView result = regController.showRegisterStudentForm();
        assertEquals("addRegistration", result.getViewName());
    }

    @Test
    public void testRegisterStudent() {
        ModelAndView result = regController.registerStudent(1, 1);
        assertEquals("showStudent", result.getViewName());
    }
}
