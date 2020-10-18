package ttl.larku.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
public class ClassDAOTest {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";

    private String code1 = "BOT-101";
    private String code2 = "BOT-202";
    private String title1 = "Intro To Botany";
    private String title2 = "Advanced Basket Weaving";

    private String startDate1 = "10/10/2012";
    private String startDate2 = "10/10/2013";
    private String endDate1 = "05/10/2013";
    private String endDate2 = "05/10/2014";

    private Course course1;
    private Course course2;
    private ScheduledClass class1;
    private ScheduledClass class2;

    @Resource(name = "classDAO")
    private BaseDAO<ScheduledClass> dao;

    @Resource(name = "courseDAO")
    private BaseDAO<Course> courseDAO;

    @BeforeEach
    public void setup() {
        course1 = new Course(code1, title1);
        course1 = courseDAO.create(course1);
        course2 = new Course(code2, title2);
        course2 = courseDAO.create(course2);

        class1 = new ScheduledClass(course1, startDate1, endDate1);
        class2 = new ScheduledClass(course2, startDate2, endDate2);
    }

    @Test
    public void testGetAll() {
        List<ScheduledClass> classes = dao.getAll();
        classes.forEach(System.out::println);
        assertEquals(3, classes.size());

    }

    @Test
    public void testCreate() {

        int newId = dao.create(class1).getId();

        ScheduledClass result = dao.get(newId);

        assertEquals(newId, result.getId());
    }

    @Test
    public void testUpdate() {
        int newId = dao.create(class1).getId();

        ScheduledClass result = dao.get(newId);

        assertEquals(newId, result.getId());

        result.setCourse(course2);
        dao.update(result);

        result = dao.get(newId);
        assertEquals(title2, result.getCourse().getTitle());
    }

    @Test
    public void testDelete() {
        int id1 = dao.create(class1).getId();

        ScheduledClass resultClass = dao.get(id1);
        assertEquals(resultClass.getId(), id1);

        dao.delete(resultClass);

        resultClass = dao.get(id1);

        assertEquals(null, resultClass);
    }
}
