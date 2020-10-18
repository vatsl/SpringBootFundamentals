package ttl.larku.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

//Use this to conditionally include a test.  Look at MyProfileValueSource.java.
//@ProfileValueSourceConfiguration(MyProfileValueSource.class)
//@IfProfileValue(name = "spring.profiles.active", values = {"development"})

//Populate your DB.  You can either do this or use the schema[-XXX].sql and data[-XXX].sql files
//and @DirtiesContext as below
//@Sql(scripts = { "/ttl/larku/db/createDB-h2.sql", "/ttl/larku/db/populateDB-h2.sql" }, executionPhase=ExecutionPhase.BEFORE_TEST_METHOD)

//This will recreate the context after every test.
//In conjunction with appropriate 'schema[-XXX].sql' and 'data[-XXX].sql' files
//it will also drop and recreate the DB before each test.
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
//@Sql(scripts = {"/schema-h2production.sql", "/data-h2production.sql"},
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Transactional
public class StudentDaoServiceTest {

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

    private String badStartDate = "12/12/2099";

    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;
    private Student student1;
    private Student student2;

    @Resource
    private StudentService studentDaoService;

    @BeforeEach
    public void setup() {
        student1 = new Student(name1);
        student2 = new Student(name2);
        course1 = new Course(code1, title1);
        course2 = new Course(code2, title2);

        class1 = new ScheduledClass(course1, startDate1, endDate1);
        class2 = new ScheduledClass(course2, startDate2, endDate2);

    }

    @Test
    public void testGetAll() {
        List<Student> students = studentDaoService.getAllStudents();

        students.forEach(System.out::println);

        assertEquals(4, students.size());
    }

    @Test
    public void testCreateStudent() {
        Student newStudent = studentDaoService.createStudent(name1);

        Student result = studentDaoService.getStudent(newStudent.getId());

        assertEquals(name1, result.getName());
        assertEquals(5, studentDaoService.getAllStudents().size());
    }

    @Test
    public void testDeleteStudent() {
        Student student1 = studentDaoService.createStudent(name1);
        Student student2 = studentDaoService.createStudent(name2);

        assertEquals(6, studentDaoService.getAllStudents().size());

        studentDaoService.deleteStudent(student1.getId());

        assertEquals(5, studentDaoService.getAllStudents().size());
    }

    @Test
    public void testDeleteNonExistentStudent() {
        Student student1 = studentDaoService.createStudent(name1);
        Student student2 = studentDaoService.createStudent(name2);

        assertEquals(6, studentDaoService.getAllStudents().size());

        //Non existent Id
        studentDaoService.deleteStudent(9999);

        assertEquals(6, studentDaoService.getAllStudents().size());
    }

    @Test
    public void testUpdateStudent() {
        Student student1 = studentDaoService.createStudent(name1);

        assertEquals(5, studentDaoService.getAllStudents().size());

        student1.setName(name2);
        studentDaoService.updateStudent(student1);

        assertEquals(5, studentDaoService.getAllStudents().size());
        assertEquals(name2, studentDaoService.getStudent(student1.getId()).getName());
    }

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void testDeleteClassFromUnderneath() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student student1 = studentDaoService.getStudent(1);

        List<ScheduledClass> classes = student1.getClasses();
        classes.get(0).setStartDate("2222-10-10");
        String name = student1.getName();
        student1.setName("What will happen");
        em.getTransaction().commit();

    }
}
