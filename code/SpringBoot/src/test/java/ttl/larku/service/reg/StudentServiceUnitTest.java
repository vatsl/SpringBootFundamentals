package ttl.larku.service.reg;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;
import ttl.larku.service.StudentService;
import ttl.larku.service.props.ServiceThatWeDontOwn;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class StudentServiceUnitTest {

    private String name1 = "Bloke";
    private String name2 = "Blokess";
    private String newName = "Karl Jung";
    private String phoneNumber1 = "290 298 4790";
    private String phoneNumber2 = "3838 939 93939";

    @InjectMocks
    private StudentService studentService;

    @Mock
    private BaseDAO<Student> studentDAO;

    @Mock
    private ServiceThatWeDontOwn ctwdo;

    @Mock
    private List<Student> allStudents;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    public void setup() {
        studentService.clear();
    }

    @Test
    public void testCreateStudent() {
        Student s = new Student(name1, phoneNumber1, Status.FULL_TIME);
        s.setId(1);

        Mockito.when(studentDAO.create(s)).thenReturn(s);
        Mockito.when(studentDAO.get(1)).thenReturn(s);
        Mockito.when(studentDAO.getAll()).thenReturn(allStudents);
        Mockito.when(allStudents.size()).thenReturn(1);
        Mockito.when(ctwdo.getTimeout()).thenReturn(Duration.ZERO);

        Mockito.doNothing().when(publisher).publishEvent(any());

        Student newStudent = studentService.createStudent(name1, phoneNumber1, Status.FULL_TIME);

        Student result = studentService.getStudent(newStudent.getId());

        assertTrue(result.getName().contains(name1));
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    public void testDeleteStudent() {
        Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student1.setId(1);
        Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student2.setId(2);

        Mockito.when(studentDAO.create(student1)).thenReturn(student1);
        Mockito.when(studentDAO.create(student2)).thenReturn(student2);
        
        //different style
        Mockito.doReturn(allStudents).when(studentDAO).getAll();

        Mockito.when(allStudents.size()).thenReturn(2).thenReturn(1);
        Mockito.when(allStudents.get(0)).thenReturn(student2);

        student1 = studentService.createStudent(student1);
        student2 = studentService.createStudent(student2);

        assertEquals(2, studentService.getAllStudents().size());

        studentService.deleteStudent(student1.getId());

        assertEquals(1, studentService.getAllStudents().size());
        assertTrue(studentService.getAllStudents().get(0).getName().contains(name1));
    }

    @Test
    public void testDeleteNonExistentStudent() {
        Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student1.setId(1);
        Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student2.setId(2);
        Mockito.when(studentDAO.create(student1)).thenReturn(student1);
        Mockito.when(studentDAO.create(student2)).thenReturn(student2);

        //different style
        Mockito.doReturn(allStudents).when(studentDAO).getAll();

        Mockito.when(allStudents.size()).thenReturn(2).thenReturn(2);

        student1 = studentService.createStudent(student1);
        student2 = studentService.createStudent(student2);

        assertEquals(2, studentService.getAllStudents().size());

        //Non existent Id
        studentService.deleteStudent(9999);

        assertEquals(2, studentService.getAllStudents().size());

    }

    @Test
    public void testUpdateStudent() {
        Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student1.setId(1);
        Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
        student2.setId(2);

        Mockito.when(studentDAO.create(student1)).thenReturn(student1);
        Mockito.when(studentDAO.create(student2)).thenReturn(student2);

        //different style
        Mockito.doReturn(allStudents).when(studentDAO).getAll();

        Mockito.when(allStudents.size()).thenReturn(1).thenReturn(1);
        Mockito.when(allStudents.get(0)).thenReturn(student2);

        student1 = studentService.createStudent(student1);

        assertEquals(1, studentService.getAllStudents().size());

        student1.setName(name2);
        studentService.updateStudent(student1);

        assertEquals(1, studentService.getAllStudents().size());
        assertTrue(studentService.getAllStudents().get(0).getName().contains(name2));

    }
}
