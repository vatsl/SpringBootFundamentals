package ttl.larku.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
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
public class StudentDAOTest {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";
    private Student student1;
    private Student student2;

    @Resource(name = "studentDAO")
    private BaseDAO<Student> dao;

    @Before
    public void setup() {
        student1 = new Student(name1);
        student2 = new Student(name2);
    }


    @Test
    public void testGetAll() {
        List<Student> students = dao.getAll();
        assertEquals(4, students.size());
    }

    @Test
    public void testCreate() {

        int newId = dao.create(student1).getId();

        Student resultstudent = dao.get(newId);

        assertEquals(newId, resultstudent.getId());
    }

    @Test
    public void testUpdate() {
        int newId = dao.create(student1).getId();

        Student resultStudent = dao.get(newId);

        assertEquals(newId, resultStudent.getId());

        resultStudent.setName(newName);
        dao.update(resultStudent);

        resultStudent = dao.get(resultStudent.getId());
        assertEquals(newName, resultStudent.getName());
    }

    @Test
    public void testDelete() {
        int id1 = dao.create(student1).getId();

        Student resultStudent = dao.get(id1);
        assertEquals(resultStudent.getId(), id1);

        dao.delete(resultStudent);

        resultStudent = dao.get(id1);

        assertEquals(null, resultStudent);

    }

}
