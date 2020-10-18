package ttl.larku.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ProfileValueSourceConfiguration(MyProfileValueSource.class)
//@IfProfileValue(name = "spring.profiles.active", value = "development")
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ClassServiceTest {

    @Resource(name = "classService")
    private ClassService classService;
    @Resource(name = "courseService")
    private CourseService courseService;
    @Resource(name = "studentService")
    private StudentService studentService;

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";

    private String code1 = "MATH-101";
    private String code2 = "BOT-202";
    private String badCode = "BADNESS";
    private String title1 = "Intro To Botany";
    private String title2 = "Outtro To Botany";

    private String startDate1 = "10/10/2012";
    private String startDate2 = "10/10/2012";
    private String endDate1 = "08/10/2013";
    private String endDate2 = "10/10/2013";

    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;
    private Student student1;
    private Student student2;

    @Before
    public void setup() {
        student1 = new Student(name1);
        student2 = new Student(name2);
        course1 = new Course(code1, title1);
        course2 = new Course(code2, title2);

        class1 = new ScheduledClass(course1, startDate1, endDate1);
        class2 = new ScheduledClass(course2, startDate2, endDate2);

        //Clean up the DAO's we will be using
        //classService = new ClassService();
        //courseService = new CourseService();
        //studentService = new StudentService();
        //Empty out our databases

		/*
		studentService.clear();
		courseService.clear();
		classService.clear();
		//Initialize courseService
		courseService.createCourse(code1, title1);
		courseService.createCourse(code2, title2);
		*/

    }

    @Test
    public void testCreateScheduledClass() {

        ScheduledClass newScheduledClass = classService.createScheduledClass(code1,
                startDate1, endDate1);

        ScheduledClass result = classService.getScheduledClass(newScheduledClass.getId());

        assertEquals(code1, result.getCourse().getCode());
        assertEquals(4, classService.getAllScheduledClasses().size());
    }

    @Test
    public void testDeleteScheduledClass() {
        ScheduledClass sClass1 = classService.createScheduledClass(code1, startDate1, endDate1);
        ScheduledClass sClass2 = classService.createScheduledClass(code2, startDate2, endDate2);

        assertEquals(5, classService.getAllScheduledClasses().size());

        classService.deleteScheduledClass(sClass1.getId());

        assertEquals(4, classService.getAllScheduledClasses().size());
    }


    @Test
    public void testSearchForNonExistentCode() {
        ScheduledClass sClass1 = classService.createScheduledClass(code1, startDate1, endDate1);

        List<ScheduledClass> result = classService.getScheduledClassesByCourseCode(badCode);

        assertEquals(0, result.size());
    }

    @Test
    public void testForMissingCodeOnCreate() {
        ScheduledClass sClass1 = classService.createScheduledClass(badCode, startDate1, endDate1);

        List<ScheduledClass> result = classService.getScheduledClassesByCourseCode(code1);

        assertEquals(3, classService.getAllScheduledClasses().size());
    }

    @Test
    public void testBadIdOnDelete() {
        ScheduledClass sClass1 = classService.createScheduledClass(code1, startDate1, endDate1);

        classService.deleteScheduledClass(Integer.MIN_VALUE);

        List<ScheduledClass> result = classService.getScheduledClassesByCourseCode(code1);
        assertEquals(4, classService.getAllScheduledClasses().size());
    }
}
