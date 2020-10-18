package ttl.larku.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.domain.Student;
import ttl.larku.domain.StudentListHolder;
import ttl.larku.service.RegistrationService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adminrest/student")
public class StudentRestController {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Resource
    private RegistrationService regService;

    @RequestMapping(value = "/getStudent", method = RequestMethod.GET, consumes = {"text/html"})
    public ModelAndView getStudentWithForm(@RequestParam int id) {
        Student student = regService.getStudentService().getStudent(id);
        if (student == null) {
            return new ModelAndView("error/studentNotFound");
        }
        ModelAndView mav = new ModelAndView("showStudent", "student", student);
        return mav;
    }

    @RequestMapping(value = "/addStudents", method = RequestMethod.GET)
    public ModelAndView addStudentsResty() {
        ModelAndView mav = new ModelAndView("addStudentsJQueryAjax", "statusList", Student.Status.values());
        return mav;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = {"application/json", "application/xml", "application/csv", "application/x-serialized-object"})
    public @ResponseBody
    ResponseEntity<?> getStudentXJWithPathVariable(HttpServletRequest request, @PathVariable("id") int id) {
        Student student = regService.getStudentService().getStudent(id);
        if (student == null) {
            return new ResponseEntity<RestResultGeneric<Student>>(new RestResultGeneric<Student>("Student with id: " + id + " not found"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RestResultGeneric<Student>>(
                new RestResultGeneric<Student>(student), HttpStatus.OK);
    }

    @RequestMapping(value = "/direct/{id}", method = RequestMethod.GET,
            produces = {"application/json", "application/xml", "application/csv", "application/x-serialized-object"})
    public @ResponseBody
    Student getStudentDirect(HttpServletRequest request, @PathVariable("id") int id) {
        Student student = regService.getStudentService().getStudent(id);
        return student;
    }

    @RequestMapping(value = "/nongen/{id}", method = RequestMethod.GET,
            produces = {"application/xml", "application/json",
                    "application/csv", "application/x-serialized-object"})
    public @ResponseBody
    ResponseEntity<?> getStudentNonGen(@PathVariable("id") int id) {
        Student student = regService.getStudentService().getStudent(id);
        if (student == null) {
            return new ResponseEntity<RestResult>(new RestResult("Student with id: " + id + " not found"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RestResult>(
                new RestResult(student), HttpStatus.OK);
    }

    @RequestMapping(value = "/studentsParam", method = RequestMethod.GET, produces = {"application/xml",
            "application/json"})
    public @ResponseBody
    ResponseEntity<?> getStudentXJWithRequestParam(@RequestParam("id") int id) {
        Student student = regService.getStudentService().getStudent(id);
        if (student == null) {
            return ResponseEntity.badRequest().body(new RestResultGeneric<Void>("Student with id: " + id + " not found"));
        }
        return ResponseEntity.ok(new RestResultGeneric<Student>(student));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {"application/xml", "application/json"},
            produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity<?> addStudent(@Valid @RequestBody Student student) {

        Student student2 = regService.getStudentService().createStudent(student);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student2.getId())
                .toUri();

        return ResponseEntity.created(newResource).body(new RestResultGeneric<Student>(student2));

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/xml",
            "application/json"}, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity<?> updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
        regService.getStudentService().updateStudent(student);
        return ResponseEntity.noContent().build();
    }

    /**
     * Do partial updates.  The data comes to us as a map of property, value.
     * We use reflection in studentService.updateStudentPartial to do the updates.
     *
     * @param id
     * @param props
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = {"application/xml",
            "application/json"}, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity<?> updateStudentPartial(@PathVariable("id") int id, @RequestBody Map<String, Object> props) {
//        regService.getStudentService().updateStudentPartial(id, props);
        regService.getStudentService().updateStudentPartialBeanWrapper(id, props);
        return ResponseEntity.noContent().build();

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = {"application/xml",
            "application/json"}, produces = {"application/xml", "application/json"})
    public @ResponseBody
    void deleteStudent(@PathVariable("id") int id) {
        regService.getStudentService().deleteStudent(id);
    }

    @RequestMapping(value = "/showStudents", method = RequestMethod.GET)
    public ModelAndView showStudents() {
        List<Student> students = regService.getStudentService().getAllStudents();
        return new ModelAndView("showStudentsJQuery", "students", students);
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity<?> getStudentsXmlJson() {
        List<Student> students = regService.getStudentService().getAllStudents();
        return ResponseEntity.ok(new RestResultGeneric<List<Student>>(students));
    }

    @RequestMapping(value = "/nongen", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    ResponseEntity<?> getStudentsNonGen() {
        List<Student> students = regService.getStudentService().getAllStudents();
        return ResponseEntity.ok(new RestResult(students));
    }


    @RequestMapping(value = "/heldstudents", method = RequestMethod.GET, produces = {"application/xml",
            "application/json"})
    public @ResponseBody
    StudentListHolder getStudentsXmlJsonHolder() {
        List<Student> students = regService.getStudentService().getAllStudents();
        return new StudentListHolder(students);
    }
}
