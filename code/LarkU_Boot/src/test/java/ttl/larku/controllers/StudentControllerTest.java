package ttl.larku.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentControllerTest {

    @Resource(name = "courseController")
    private CourseController courseController;

    @Resource(name = "studentController")
    private StudentController studentController;

    @Resource
    private ApplicationContext appContext;

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
        // Set up the Student Service with some students;
        StudentService ss = appContext.getBean("studentService", StudentService.class);
        ss.clear();
        student1 = ss.createStudent(name1);
        student2 = ss.createStudent(name2);
    }

    @Test
    public void testGetStudent() {
        ModelAndView mav = studentController.getStudent(student1.getId());

        assertEquals("showStudent", mav.getViewName());
    }

    @Test
    public void testStudentNotFoundGet() {
        ModelAndView mav = studentController.getStudent(Integer.MAX_VALUE);

        assertEquals("error/studentNotFound", mav.getViewName());
    }

    @Test
    public void testGetStudents() {
        ModelAndView mav = studentController.getStudents();

        assertEquals("showStudents", mav.getViewName());
        List<Student> students = (List) mav.getModel().get("students");
        assertEquals(2, students.size());
    }

    @Test
    public void testGetStudentTheHardWay() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addStudent");
        request.setParameter("id", student1.getId() + "");

        ModelAndView mav = studentController.getStudentTheHardWay(request);

        assertEquals("showStudent", mav.getViewName());
    }

    @Test
    public void testGetNonExistingStudentTheHardWay() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addStudent");
        request.setParameter("id", "9999");

        ModelAndView mav = studentController.getStudentTheHardWay(request);

        assertEquals("error/studentNotFound", mav.getViewName());
    }

    @Test
    public void testShowAddForm() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/registration/addStudent");
        // new instance of "Post" object
        Student student = new Student();

        // give it to the binder
        WebDataBinder binder = new WebDataBinder(student);

        // And have the binder set the values on the object based on the
        // parameter of the "Post"
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        ModelAndView result = studentController.showAddStudentForm((Student) binder.getTarget());

        assertEquals("addStudent", result.getViewName());

    }

    @Test
    public void testAddStudent() {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addStudent");
        request.setParameter("name", postedName);

        // new instance of "Post" object
        Student student = new Student();

        // give it to the binder
        WebDataBinder binder = new WebDataBinder(student);

        // And have the binder set the values on the object based on the
        // parameter of the "Post"
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        ModelAndView result = studentController.addStudent((Student) binder.getTarget(), binder.getBindingResult());

        assertEquals("redirect:getStudents", result.getViewName());
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

    @Test
    public void testAddStudentWithSessionAttributes() {

        completeCalled = false;

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addStudent2");
        request.setParameter("name", postedName);
        request.setParameter("addStudentDone", "true");

        SessionStatus sessionStatus = new SessionStatus() {

            @Override
            public void setComplete() {
                completeCalled = true;
            }

            @Override
            public boolean isComplete() {
                return false;
            }

        };

        // new instance of "Post" object
        Student student = new Student();

        // give it to the binder
        WebDataBinder binder = new WebDataBinder(student);

        // And have the binder set the values on the object based on the
        // parameter of the "Post"
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        ModelMap mm = new ModelMap();
        mm.addAttribute("sessionAttr", "sessionValue");
        // fake the initializing of the ModelMap in the controller

        ModelAndView result = studentController.addStudentWithSessionAttributes((Student) binder.getTarget(),
                binder.getBindingResult(), sessionStatus, "Boo");

        assertEquals("redirect:getStudents", result.getViewName());
        assertTrue(completeCalled);
    }

    @Test
    public void testAddStudentWithSessionAttributesNotCompleted() {

        completeCalled = false;

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addStudent2");
        request.setParameter("name", postedName);

        SessionStatus sessionStatus = new SessionStatus() {

            @Override
            public void setComplete() {
                completeCalled = true;
            }

            @Override
            public boolean isComplete() {
                return false;
            }

        };

        // new instance of "Post" object
        Student student = new Student();

        // give it to the binder
        WebDataBinder binder = new WebDataBinder(student);

        // And have the binder set the values on the object based on the
        // parameter of the "Post"
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        ModelMap mm = new ModelMap();
        mm.addAttribute("sessionAttr", "sessionValue");
        // fake the initializing of the ModelMap in the controller
        ModelAndView result = studentController.addStudentWithSessionAttributes((Student) binder.getTarget(),
                binder.getBindingResult(), sessionStatus, null);

        assertEquals("redirect:getStudents", result.getViewName());
        assertTrue(!completeCalled);
    }

}
