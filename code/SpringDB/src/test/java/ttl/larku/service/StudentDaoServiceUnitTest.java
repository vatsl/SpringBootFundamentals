package ttl.larku.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
public class StudentDaoServiceUnitTest {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Karl Jung";
    private String phoneNumber1 = "290 298 4790";
    private String phoneNumber2 = "3838 939 93939";

    @InjectMocks
    private StudentDaoService studentDaoService;

    @Mock
    private BaseDAO<Student> studentDAO;

    @Mock
    private List<Student> allStudents;

    @BeforeEach
    public void setup() {
        studentDaoService.clear();
    }

    @Test
    public void testCreateStudent() {
        Student s = new Student(name1, phoneNumber1, Status.FULL_TIME);
        s.setId(1);

        Mockito.when(studentDAO.create(s)).thenReturn(s);
        Mockito.when(studentDAO.get(1)).thenReturn(s);
        Mockito.when(studentDAO.getAll()).thenReturn(allStudents);
        Mockito.when(allStudents.size()).thenReturn(1);

        Student newStudent = studentDaoService.createStudent(name1, phoneNumber1, Status.FULL_TIME);

        Student result = studentDaoService.getStudent(newStudent.getId());

        assertTrue(result.getName().contains(name1));
        assertEquals(1, studentDaoService.getAllStudents().size());
    }

    @Test
    public void testDeleteStudent() {
        Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student1.setId(1);
        Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student2.setId(2);

        Mockito.when(studentDAO.create(student1)).thenReturn(student1);
        Mockito.when(studentDAO.create(student2)).thenReturn(student2);
        Mockito.when(studentDAO.get(1)).thenReturn(student1);

        //different style
        Mockito.doReturn(allStudents).when(studentDAO).getAll();

        Mockito.when(allStudents.size()).thenReturn(2).thenReturn(1);
        Mockito.when(allStudents.get(0)).thenReturn(student2);

        student1 = studentDaoService.createStudent(student1);
        student2 = studentDaoService.createStudent(student2);

        assertEquals(2, studentDaoService.getAllStudents().size());

        studentDaoService.deleteStudent(student1.getId());

        assertEquals(1, studentDaoService.getAllStudents().size());
        assertTrue(studentDaoService.getAllStudents().get(0).getName().contains(name1));
    }

    @Test
    public void testDeleteNonExistentStudent() {
        Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student1.setId(1);
        Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student2.setId(2);
        Mockito.when(studentDAO.create(student1)).thenReturn(student1);
        Mockito.when(studentDAO.create(student2)).thenReturn(student2);
        Mockito.when(studentDAO.get(1)).thenReturn(student1);

        //different style
        Mockito.doReturn(allStudents).when(studentDAO).getAll();

        Mockito.when(allStudents.size()).thenReturn(2).thenReturn(2);
        Mockito.when(allStudents.get(0)).thenReturn(student2);

        student1 = studentDaoService.createStudent(student1);
        student2 = studentDaoService.createStudent(student2);

        assertEquals(2, studentDaoService.getAllStudents().size());

        //Non existent Id
        studentDaoService.deleteStudent(9999);

        assertEquals(2, studentDaoService.getAllStudents().size());

    }

    @Test
    public void testUpdateStudent() {
        Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student1.setId(1);
        Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student2.setId(2);

        Mockito.when(studentDAO.create(student1)).thenReturn(student1);
        Mockito.when(studentDAO.create(student2)).thenReturn(student2);
        Mockito.when(studentDAO.get(1)).thenReturn(student1);

        //different style
        Mockito.doReturn(allStudents).when(studentDAO).getAll();

        Mockito.when(allStudents.size()).thenReturn(1).thenReturn(1);
        Mockito.when(allStudents.get(0)).thenReturn(student2);

        student1 = studentDaoService.createStudent(student1);

        assertEquals(1, studentDaoService.getAllStudents().size());

        student1.setName(name2);
        studentDaoService.updateStudent(student1);

        assertEquals(1, studentDaoService.getAllStudents().size());
        assertTrue(studentDaoService.getAllStudents().get(0).getName().contains(name2));

    }
}
