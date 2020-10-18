package ttl.larku.service;

import java.util.List;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;

public class CourseDaoService implements CourseService {

    private BaseDAO<Course> courseDAO;

    @Override
    public Course createCourse(String code, String title) {
        Course course = new Course(code, title);
        course = courseDAO.create(course);

        return course;
    }

    @Override
    public Course createCourse(Course course) {
        course = courseDAO.create(course);

        return course;
    }

    @Override
    public void deleteCourse(int id) {
        Course course = courseDAO.get(id);
        if (course != null) {
            courseDAO.delete(course);
        }
    }

    @Override
    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    @Override
    public Course getCourseByCode(String code) {
        List<Course> courses = courseDAO.getAll();
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    @Override
    public Course getCourse(int id) {
        return courseDAO.get(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }

    public BaseDAO<Course> getCourseDAO() {
        return courseDAO;
    }

    public void setCourseDAO(BaseDAO<Course> courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    public void clear() {
        courseDAO.deleteStore();
        courseDAO.createStore();
    }
}
