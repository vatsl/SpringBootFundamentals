package ttl.larku.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
//Populate your DB.  From Most Expensive to least expensive

//This will make recreate the context after every test.
//In conjunction with appropriate 'schema[-XXX].sql' and 'data[-XXX].sql' files
//it will also drop and recreate the DB before each test.
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)

//Or you can just re-run the sql files before each test method
//@Sql(scripts = { "/ttl/larku/db/createDB-h2.sql", "/ttl/larku/db/populateDB-h2.sql" }, executionPhase=ExecutionPhase.BEFORE_TEST_METHOD)

//This next one will roll back the transaction after
//each test, so the database will actually stay the
//same for the next test.
@Transactional
public class CourseServiceTest {

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

    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;
    private Student student1;
    private Student student2;

    @Resource
    private CourseService courseService;

    @BeforeEach
    public void setup() {
        student1 = new Student(name1);
        student2 = new Student(name2);
        course1 = new Course(code1, title1);
        course2 = new Course(code2, title2);

        class1 = new ScheduledClass(course1, startDate1, endDate1);
        class2 = new ScheduledClass(course2, startDate2, endDate2);

        // courseService.clear();
    }

    @Test
    public void testCreateCourse() {
        Course newCourse = courseService.createCourse(code1, title1);

        Course result = courseService.getCourse(newCourse.getId());

        assertEquals(title1, result.getTitle());
        assertEquals(4, courseService.getAllCourses().size());
    }

    @Test
    public void testDeleteCourse() {
        Course course1 = courseService.createCourse(code1, title1);
        Course course2 = courseService.createCourse(code2, title2);

        assertEquals(5, courseService.getAllCourses().size());

        courseService.deleteCourse(course1.getId());

        List<Course> courses = courseService.getAllCourses();
        assertEquals(4, courses.size());
        assertEquals(title2, courseService.getCourse(course2.getId()).getTitle());
    }

    @Test
    public void testDeleteNonExistentCourse() {
        Course course1 = courseService.createCourse(code1, title1);
        Course course2 = courseService.createCourse(code2, title2);

        assertEquals(5, courseService.getAllCourses().size());

        // Nonexistent id
        courseService.deleteCourse(9999);

        assertEquals(5, courseService.getAllCourses().size());
    }

    @Test
    public void testUpdateCourse() {
        Course course1 = courseService.createCourse(code1, title1);

        assertEquals(4, courseService.getAllCourses().size());

        course1.setTitle(title2);
        courseService.updateCourse(course1);

        assertEquals(4, courseService.getAllCourses().size());
        assertEquals(title2, courseService.getCourse(course1.getId()).getTitle());
    }
}
