package ttl.larku.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@Service
@Transactional
public class RegistrationService {

    @Resource
    private CourseService courseService;
    @Resource
    private StudentService studentService;
    @Resource
    private ClassService classService;

    public RegistrationService() {
    }

    public ScheduledClass addNewClassToSchedule(String courseCode, String startDate, String endDate) {
        ScheduledClass sClass = classService.createScheduledClass(courseCode, startDate, endDate);
        return sClass;
    }

    public void registerStudentForClass(int studentId, String courseCode, String startDate) {
        Student student = studentService.getStudent(studentId);
        if (student != null) {
            List<ScheduledClass> classes = classService.getScheduledClassesByCourseCode(courseCode);
            for (ScheduledClass sc : classes) {
                if (sc.getStartDate().equals(startDate)) {
                    sc.addStudent(student);
                    student.addClass(sc);

                    break;
                }
            }
        }
    }

    public void dropStudentFromClass(int studentId, String courseCode, String startDate) {
        Student student = studentService.getStudent(studentId);
        List<ScheduledClass> classes = classService.getScheduledClassesByCourseCode(courseCode);
        for (ScheduledClass sc : classes) {
            if (sc.getStartDate().equals(startDate)) {
                sc.removeStudent(student);
                student.dropClass(sc);
                break;
            }
        }
    }

    public List<Student> getStudentsForClass(String courseCode, String startDate) {
        List<ScheduledClass> classes = classService.getScheduledClassesByCourseCode(courseCode);
        for (ScheduledClass sc : classes) {
            if (sc.getStartDate().equals(startDate)) {
                return sc.getStudents();
            }
        }
        return new ArrayList<Student>();
    }

    public List<ScheduledClass> getScheduledClasses() {
        return classService.getAllScheduledClasses();
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public ClassService getClassService() {
        return classService;
    }

    public void setClassService(ClassService classService) {
        this.classService = classService;
    }
}
