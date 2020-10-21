package ttl.larku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

import java.util.List;

//@SpringBootApplication(scanBasePackages = {"ttl.larku.jconfig", "ttl.larku.sbdemo"})
@SpringBootApplication //(scanBasePackages = {"ttl.larku"})
@EnableJpaRepositories
public class SbdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbdemoApplication.class, args);
    }

}

//@Component
class MyRunner implements CommandLineRunner
{

	public final StudentService studentService;

    public MyRunner(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from MyRunner");

        List<Student> students = studentService.getAllStudents();
        System.out.println("students:");
        students.forEach(System.out::println);

    }
}
