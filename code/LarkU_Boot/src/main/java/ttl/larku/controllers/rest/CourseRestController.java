package ttl.larku.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.domain.Course;
import ttl.larku.service.CourseService;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/adminrest/course")
public class CourseRestController {

    @Resource
    private CourseService courseService;

    @GetMapping
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable("id") int id) {
        Course c = courseService.getCourse(id);
        if (c == null) {
            return ResponseEntity.badRequest().body(new RestResultGeneric("Course with id: " + id + " not found"));
        }
        return ResponseEntity.ok(new RestResultGeneric<Course>(c));
    }


    @GetMapping("/code/{courseCode}")
    public ResponseEntity<?> getCourseByCode(@PathVariable("courseCode") String courseCode) {
        Course c = courseService.getCourseByCode(courseCode);
        if (c == null) {
            return ResponseEntity.badRequest().body(new RestResultGeneric("Course with code: " + courseCode + " not found"));
        }
        return ResponseEntity.ok(new RestResultGeneric<Course>(c));
    }

    @PostMapping
    public ResponseEntity<?> addCourse(Course course) {
        Course newCourse = courseService.createCourse(course);

        URI newResource = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCourse.getId())
                .toUri();

        return ResponseEntity.created(newResource).body(new RestResultGeneric<Course>(newCourse));
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") int id) {
        courseService.deleteCourse(id);
    }
}