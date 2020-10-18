package ttl.larku.app;

import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ttl.larku.LarkU_SE_Config;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.RegistrationService;
import ttl.larku.service.StudentService;

public class RegistrationApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        /*Make the Spring Container*/
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("development", "se");
        context.register(LarkU_SE_Config.class);
        context.refresh();
        // ApplicationContext context = new
        // ClassPathXmlApplicationContext("larkUSEContext.xml");

        RegistrationService registrationService = context.getBean("registrationService", RegistrationService.class);
        StudentService studentService = registrationService.getStudentService();
        System.out.println("ss is " + studentService);

        Student student = new Student("Vivek");
        student = studentService.createStudent(student);
        System.out.println("new student = " + student);

        ScheduledClass sc = registrationService.addNewClassToSchedule("Math-101", "10/10/2014", "10/10/2015");

        registrationService.registerStudentForClass(student.getId(), "Math-101", "10/10/2014");

        List<Student> students = registrationService.getStudentsForClass("Math-101", "10/10/2014");
        for (Student s : students) {
            System.out.println("Student for Math-101: " + s);
        }

        dumpAllStudents(registrationService);
        dumpAllSClasses(registrationService);

        ((ConfigurableApplicationContext) context).close();
    }

    public static void dumpAllStudents(RegistrationService rs) {
        List<Student> students = rs.getStudentService().getAllStudents();
        for (Student s : students) {
            System.out.println("All Students: " + s);
        }
    }

    public static void dumpAllSClasses(RegistrationService rs) {
        List<ScheduledClass> classes = rs.getClassService().getAllScheduledClasses();
        System.out.println("Scheduled Classes");
        for (ScheduledClass sc : classes) {
            System.out.println(sc);
            List<Student> students = sc.getStudents();
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }
}
