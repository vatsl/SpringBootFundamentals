package ttl.larku.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class CourseDAOTest {

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

    @Resource(name = "courseDAO")
    private BaseDAO<Course> dao;

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
    public void testGetAll() {
        List<Course> courses = dao.getAll();
        assertEquals(3, courses.size());
    }

    @Test
    public void testCreate() {
        assertEquals(3, dao.getAll().size());
        int newId = dao.create(course1).getId();

        Course resultCourse = dao.get(newId);

        assertEquals(newId, resultCourse.getId());
        assertEquals(4, dao.getAll().size());
    }

    @Test(expected = NullPointerException.class)
    public void testDelete() {
        assertEquals(3, dao.getAll().size());
        int id1 = dao.create(course1).getId();

        Course resultCourse = dao.get(id1);
        assertEquals(resultCourse.getId(), id1);

        dao.delete(resultCourse);

        resultCourse = dao.get(id1);

        assertEquals(title1, dao.get(id1).getTitle());
        assertEquals(3, dao.getAll().size());
    }

    @Test
    public void testUpdate() {
        assertEquals(3, dao.getAll().size());

        int newId = 1;
        Course resultCourse = dao.get(newId);

        assertEquals(newId, resultCourse.getId());

        resultCourse.setTitle(title2);
        dao.update(resultCourse);

        resultCourse = dao.get(resultCourse.getId());
        assertEquals(title2, resultCourse.getTitle());
        assertEquals(3, dao.getAll().size());
    }
}
