package ttl.larku.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ttl.larku.dao.repository.SimpleStudentRepo;
import ttl.larku.dao.repository.StudentRepo;
import ttl.larku.domain.Student;
import ttl.larku.domain.StudentCourseCodeSummary;
import ttl.larku.domain.StudentPhoneSummary;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
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
public class StudentRepoTest {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Different Bloke";
    private Student student1;
    private Student student2;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private SimpleStudentRepo simple;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void setup() {

        student1 = new Student(name1);
        student2 = new Student(name2);

        for(String name: context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println(context.getBeanDefinitionCount() + " beans");
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

    @Test
    public void testPaging() {
        // first add a bunch of student so we have something to page through
        for (int i = 0; i < 50; i++) {
            Student s = new Student("Fake #" + i);
            studentRepo.save(s);
        }

        int currPage = 0;
        int size = 20;
        Sort sort = Sort.by("name").descending();
        Page<Student> page = studentRepo.findAll(PageRequest.of(currPage++, size, sort));
        System.out.println("Number: " + page.getNumber() + ", numElements: " + page.getNumberOfElements());
        page.forEach(System.out::println);
        while (page.hasNext()) {
            page = studentRepo.findAll(PageRequest.of(currPage++, size, sort));
            System.out.println("Number: " + page.getNumber() + ", numElements: " + page.getNumberOfElements());
            page.forEach(System.out::println);
        }
    }

    @Test
    public void testProjectionPhoneSummary() {
        List<StudentPhoneSummary> l = studentRepo.findAllStudentPhoneSummariesBy();
        assertEquals(4, l.size());

        l.forEach(sp -> System.out.println(sp.getId() + ": " + sp.getName() + ", " + sp.getPhoneNumber()));

    }

    @Test
    public void testStudentCourseCodeSummary() {
        List<StudentCourseCodeSummary> l = studentRepo.findStudentCourseCodesBy();
        assertEquals(4, l.size());

        l.forEach(sp -> System.out.println(sp.getId() + ": " + sp.getName() + ", " + sp.getClasses()));

    }

    /**
     * An example of using a Pageable with a Projection
     */
    @Test
    public void testProjectionStudentClassCourseCode() {
        // first add a bunch of student so we have something to page through
        for (int i = 0; i < 50; i++) {
            Student s = new Student("Fake #" + i);
            studentRepo.save(s);
        }

        int currPage = 0;
        int size = 20;
        Sort sort = Sort.by("name").descending();
        Page<StudentCourseCodeSummary> page = studentRepo.findPageCourseCodeBy(PageRequest.of(currPage++, size, sort));
        System.out.println("Number: " + page.getNumber() + ", numElements: " + page.getNumberOfElements());
        dumpPage(page);
        while (page.hasNext()) {
            page = studentRepo.findPageCourseCodeBy(PageRequest.of(currPage++, size, sort));
            System.out.println("Number: " + page.getNumber() + ", numElements: " + page.getNumberOfElements());
            dumpPage(page);
        }

    }

    public void dumpPage(Page<StudentCourseCodeSummary> page) {
        page.forEach(sp -> {
            System.out.println(sp.getId() + ": " + sp.getName());
            sp.getClasses().forEach(s -> System.out.println("     " + s.getCourse() + ", " + s.getStartDate()));
        });

    }
}
