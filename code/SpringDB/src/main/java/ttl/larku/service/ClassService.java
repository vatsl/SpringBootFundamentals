package ttl.larku.service;

import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.ScheduledClass;

import java.util.List;

@Transactional
public interface ClassService {
    ScheduledClass createScheduledClass(String courseCode, String startDate, String endDate);

    void deleteScheduledClass(int id);

    void updateScheduledClass(ScheduledClass course);

    List<ScheduledClass> getScheduledClassesByCourseCode(String code);

    List<ScheduledClass> getScheduledClassesByCourseCodeAndStartDate(String code, String startDate);

    ScheduledClass getScheduledClass(int id);

    List<ScheduledClass> getAllScheduledClasses();

    void clear();
}
