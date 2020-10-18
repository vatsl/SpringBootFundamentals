package ttl.larku.service;

import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.Course;

import java.util.List;

@Transactional
public interface CourseService {
    Course createCourse(String code, String title);

    Course createCourse(Course course);

    void deleteCourse(int id);

    void updateCourse(Course course);

    Course getCourseByCode(String code);

    Course getCourse(int id);

    List<Course> getAllCourses();

    void clear();
}
