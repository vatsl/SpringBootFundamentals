package ttl.larku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentRepoService;
import ttl.larku.service.StudentService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sbdemo/student")
public class StudentController {

//    @Autowired
//    private StudentService studentService;
    @Autowired
    private StudentRepoService studentService;

    @GetMapping
    public List<Student> getStudents() {
        List<Student> students = studentService.getAllStudents();
        return students;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneStudent(@PathVariable("id") int id) {
        Student s = studentService.getStudent(id);
        if(s == null) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Student with id: " + id);
        }
        return ResponseEntity.ok(s);
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student, Student studentFromParams) {
       Student newStudent = studentService.createStudent(student);

        URI newResourceURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudent.getId())
                .toUri();

//       return ResponseEntity.created(newResourceURI).body(newStudent);
        return ResponseEntity.created(newResourceURI).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") int id) {
       studentService.deleteStudent(id);

       return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateStudent(@RequestBody Student student) {
        studentService.updateStudent(student);

        return ResponseEntity.noContent().build();
    }
}
