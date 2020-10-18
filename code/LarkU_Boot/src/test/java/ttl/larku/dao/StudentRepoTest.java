package ttl.larku.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.dao.repository.StudentRepo;
import ttl.larku.domain.Student;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentRepoTest {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";
    private Student student1;
    private Student student2;

    @Autowired
    private StudentRepo studentRepo;

    @Before
    public void setup() {
        student1 = new Student(name1);
        student2 = new Student(name2);
    }


    @Test
    public void testGetAll() {
        List<Student> students = studentRepo.findAll();
        assertEquals(4, students.size());
    }

    @Test
    public void testCreate() {

        int newId = studentRepo.save(student1).getId();

        Student resultStudent = studentRepo.findById(newId).orElse(null);

        assertEquals(newId, resultStudent.getId());
    }

    @Test
    public void testUpdate() {
        int newId = studentRepo.save(student1).getId();

        Student resultStudent = studentRepo.findById(newId).orElse(null);

        assertEquals(newId, resultStudent.getId());

        resultStudent.setName(newName);
        studentRepo.save(resultStudent);

        resultStudent = studentRepo.findById(newId).orElse(null);
        assertEquals(newName, resultStudent.getName());
    }

    @Test
    public void testDelete() {
        int newId = studentRepo.save(student1).getId();

        Student resultStudent = studentRepo.findById(newId).orElse(null);
        assertEquals(newId, resultStudent.getId());

        studentRepo.delete(resultStudent);

        resultStudent = studentRepo.findById(newId).orElse(null);

        assertEquals(null, resultStudent);
    }

    @Test
    public void testFindByName() {
        Student newManoj = new Student("Manoj");
        studentRepo.save(newManoj);
        List<Student> manojes = studentRepo.findByNameLike("%Manoj%");

        assertEquals(2, manojes.size());
    }
}
