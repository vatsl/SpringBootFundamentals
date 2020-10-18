package ttl.larku.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.dao.WhichDB;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ProfileValueSourceConfiguration(MyProfileValueSource.class)
//@IfProfileValue(name = "spring.profiles.active", value = "development")
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class RegistrationServiceTest {

    private String name1 = "Manoj";
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

    private String badStartDate = "12/12/2099";
    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;
    private Student student1;
    private Student student2;
	
	/*
	private ClassService classService;
	private CourseService courseService;
	private StudentService studentService;
	*/

    @Resource
    private RegistrationService regService;

    @Resource
    private ApplicationContext appContext;

    @Before
    public void setup() {
        student1 = new Student(name1);
        student2 = new Student(name2);
        course1 = new Course(code1, title1);
        course2 = new Course(code2, title2);

        class1 = new ScheduledClass(course1, startDate1, endDate1);
        class2 = new ScheduledClass(course2, startDate2, endDate2);

    }

    @Test
    public void testAddClassAndStudent() {
        ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
        Student student1 = regService.getStudentService().createStudent("Josephine");


        regService.registerStudentForClass(student1.getId(), code1, startDate1);

        List<Student> students = regService.getStudentsForClass(code1, startDate1);

        assertEquals(1, students.size());

        assertEquals(1, regService.getStudentService().getStudent(student1.getId()).getClasses().size());
        assertEquals(code1, students.get(0).getClasses().get(0).getCourse().getCode());
    }

    @Test
    public void testDropStudent() {
        ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
        Student student1 = regService.getStudentService().getStudent(1);
        Student student2 = regService.getStudentService().getStudent(2);

        regService.registerStudentForClass(student1.getId(), code1, startDate1);
        regService.registerStudentForClass(student2.getId(), code1, startDate1);

        List<Student> students = regService.getStudentsForClass(code1, startDate1);

        assertEquals(2, students.size());
        assertTrue(regService.getStudentService().getStudent(student1.getId()).getName().contains(name1));

        regService.dropStudentFromClass(student1.getId(), code1, startDate1);

        students = regService.getStudentsForClass(code1, startDate1);

        assertEquals(1, students.size());
        assertEquals(student2.getName(), students.get(0).getName());
    }

    @Test
    public void testRegisterForNonExistentClass() {
        ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);

        regService.registerStudentForClass(student1.getId(), code1, badStartDate);

        List<Student> students = regService.getStudentsForClass(code1, startDate1);

        assertEquals(0, students.size());
    }

    @Test
    public void testDropStudentFromNonExistentClass() {
        ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);
        Student student1 = regService.getStudentService().getStudent(1);
        Student student2 = regService.getStudentService().getStudent(2);
        regService.registerStudentForClass(student1.getId(), code1, startDate1);
        regService.registerStudentForClass(student2.getId(), code1, startDate1);

        List<Student> students = regService.getStudentsForClass(code1, startDate1);

        assertEquals(2, students.size());
        regService.dropStudentFromClass(student1.getId(), code1, badStartDate);

        students = regService.getStudentsForClass(code1, startDate1);

        assertEquals(2, students.size());
    }

    @Test
    public void testGetStudentsFromNonExistentClass() {

        List<Student> students = regService.getStudentsForClass(code1, badStartDate);

        assertEquals(0, students.size());
    }

    @Test
    public void testGetAllScheduledClasses() {
        ScheduledClass sClass = regService.addNewClassToSchedule(code1, startDate1, endDate1);

        List<ScheduledClass> classes = regService.getScheduledClasses();

        assertEquals(4, classes.size());

        assertEquals(sClass.getStartDate(), regService.getClassService().getScheduledClass(sClass.getId()).getStartDate());
    }
}
