package ttl.larku.service;

import ttl.larku.dao.repository.ClassRepo;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;

import java.util.List;

public class ClassRepoService implements ClassService {

    private CourseService courseService;
    private ClassRepo classDAO;


    public ScheduledClass createScheduledClass(String courseCode, String startDate, String endDate) {
        Course course = courseService.getCourseByCode(courseCode);
        if (course != null) {
            ScheduledClass sClass = new ScheduledClass(course, startDate, endDate);
            sClass = classDAO.save(sClass);
            return sClass;
        }
        return null;
    }

    public void deleteScheduledClass(int id) {
        ScheduledClass course = classDAO.findById(id).orElse(null);
        if (course != null) {
            classDAO.delete(course);
        }
    }

    public void updateScheduledClass(ScheduledClass course) {
        classDAO.save(course);
    }

    public List<ScheduledClass> getScheduledClassesByCourseCode(String code) {
        List<ScheduledClass> result = classDAO.getByCourseCode(code);
        return result;
    }

    @Override
    public List<ScheduledClass> getScheduledClassesByCourseCodeAndStartDate(String code, String startDate) {
        List<ScheduledClass> result = classDAO.getByCourseCodeAndStartDateForStudents(code, startDate);
        return result;
    }

    public ScheduledClass getScheduledClass(int id) {
        return classDAO.findById(id).orElse(null);
    }

    public List<ScheduledClass> getAllScheduledClasses() {
        return classDAO.findAll();
    }

    public ClassRepo getClassDAO() {
        return classDAO;
    }

    public void setClassDAO(ClassRepo classDAO) {
        this.classDAO = classDAO;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void clear() {
        classDAO.deleteAll();
    }
}
