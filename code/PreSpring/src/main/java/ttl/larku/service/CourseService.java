package ttl.larku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;

import javax.annotation.Resource;
import java.util.List;

//@Component
@Service
public class CourseService {

    //    @Resource(name = "jpaCourseDAO")
    @Autowired
    private BaseDAO<Course> courseDAO;

    public CourseService() {
        int i = 0;
    }

    public Course createCourse(String code, String title) {
        Course course = new Course(code, title);
        course = courseDAO.create(course);

        return course;
    }

    public Course createCourse(Course course) {
        course = courseDAO.create(course);

        return course;
    }

    public void deleteCourse(int id) {
        Course course = courseDAO.get(id);
        if (course != null) {
            courseDAO.delete(course);
        }
    }

    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    public Course getCourseByCode(String code) {
        List<Course> courses = courseDAO.getAll();
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    public Course getCourse(int id) {
        return courseDAO.get(id);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }

    public BaseDAO<Course> getCourseDAO() {
        return courseDAO;
    }

    public void setCourseDAO(BaseDAO<Course> courseDAO) {
        this.courseDAO = courseDAO;
    }
}
