package ttl.larku.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.controllers.rest.RestResultGeneric.Status;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.ClassService;
import ttl.larku.service.RegistrationService;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/adminrest/class")
public class RegistrationRestController {

    @Resource
    private RegistrationService regService;

    @Resource
    private ClassService classService;

    @GetMapping
    public ResponseEntity<?> getAllClasses() {
        List<ScheduledClass> classes = classService.getAllScheduledClasses();
        return ResponseEntity.ok().body(new RestResultGeneric<List<ScheduledClass>>(classes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScheduledClass(@PathVariable("id") int id) {
        ScheduledClass c = classService.getScheduledClass(id);
        if (c == null) {
            RestResultGeneric rr = new RestResultGeneric<Void>(Status.Error, "ScheduledClass with id: " + id + " not found");
            return ResponseEntity.badRequest().body(rr);
        }
        return ResponseEntity.ok(new RestResultGeneric<ScheduledClass>(c));
    }

    @GetMapping("/{courseCode}/{startDate}/{endDate}")
    public ResponseEntity<?> getScheduledClassPath(@PathVariable("courseCode") String courseCode, @PathVariable("startDate") String startDate,
                                                   @PathVariable("endDate") String endDate) {
        List<ScheduledClass> cl = classService.getScheduledClassesByCourseCode(courseCode);
        if (cl == null || cl.size() == 0) {
            RestResultGeneric<Void> r1 = new RestResultGeneric<Void>("ScheduledClass with code: " +
                    courseCode + " not found");
            return ResponseEntity.badRequest().body(r1);
        }
        Optional<ScheduledClass> optional = cl.stream()
                .filter(c -> c.getStartDate().equals(startDate) &&
                        c.getEndDate().equals(endDate))
                .findFirst();

        RestResultGeneric<?> o = optional.isPresent() ?
                new RestResultGeneric<ScheduledClass>(optional.get()) :
                new RestResultGeneric<Void>("ScheduledClass with code: " +
                        courseCode + " not found");

        return ResponseEntity.ok(o);
    }


    @PostMapping
    public ResponseEntity<?> addClass(@RequestParam("courseCode") String courseCode,
                                      @RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate) {

        ScheduledClass sc = regService.addNewClassToSchedule(courseCode, startDate, endDate);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sc.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new RestResultGeneric<ScheduledClass>(sc));
    }

    @PostMapping(value = "/{courseCode}/{startDate}/{endDate}")
    public ResponseEntity<?> addClassPathParams(@PathVariable("courseCode") String courseCode,
                                                @PathVariable("startDate") String startDate,
                                                @PathVariable("endDate") String endDate) {

        return addClass(courseCode, startDate, endDate);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerStudent(@RequestParam int studentId, @RequestParam int classId) {

        ScheduledClass sClass = regService.getClassService().getScheduledClass(classId);
        regService.registerStudentForClass(studentId, sClass.getCourse().getCode(), sClass.getStartDate());

        Student student = regService.getStudentService().getStudent(studentId);

        return ResponseEntity.ok(new RestResultGeneric<Student>(student));
    }

    @RequestMapping(value = "/register/{studentId}/{classId}", method = RequestMethod.POST)
    public ResponseEntity<?> registerStudentWithPath(@PathVariable int studentId, @PathVariable int classId) {

        return registerStudent(studentId, classId);
    }
}
