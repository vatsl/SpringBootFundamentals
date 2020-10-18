package ttl.larku.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;

import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.CourseService;
import ttl.larku.service.RegistrationService;

@RunWith(MockitoJUnitRunner.class)
public class CourseControllerUnitTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private RegistrationService regService;

    @Mock
    private CourseService courseService;


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

    List<Course> allCourses;

    @Before
    public void init() {
        Course[] cs = {course1 = new Course("MATH-101", "Intro to Math"),
                course2 = new Course("MATH-102", "Yet more Math")};
        //allCourses = Arrays.asList(cs);
        allCourses = Arrays.asList(cs);
        Mockito.when(regService.getCourseService()).thenReturn(courseService);
        Mockito.when(courseService.getAllCourses()).thenReturn(allCourses);
        Mockito.when(courseService.getCourse(1)).thenReturn(course1);

    }

    @Test
    public void testGetCourses() {

        ModelAndView result = courseController.getCourses();

        List<Course> courses = (List) result.getModel().get("courses");

        assertTrue(courses.size() == 2);
        assertEquals("showCourses", result.getViewName());
    }

    @Test
    public void testGetCourseById() {

        ModelAndView result = courseController.getCourse(1);

        assertEquals("MATH-101", ((Course) (result.getModel().get("course"))).getCode());
        assertEquals("showCourse", result.getViewName());
    }

    @Test
    public void testShowAddCourseForm() {

        ModelAndView result = courseController.showAddCourseForm(new Course());

        assertEquals("addCourse", result.getViewName());
    }

    @Test
    public void testAddCourse() {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/registration/addCourse");
        request.setParameter("title", title1);
        request.setParameter("code", code1);

        Course course = new Course();

        // give it to the binder
        WebDataBinder binder = new WebDataBinder(course);

        // And have the binder set the values on the object based on the
        // parameter of the "Post"
        binder.bind(new MutablePropertyValues(request.getParameterMap()));

        ModelAndView result = courseController.addCourse((Course) binder.getTarget(), binder.getBindingResult());

        assertEquals("redirect:getCourses", result.getViewName());
    }
}
