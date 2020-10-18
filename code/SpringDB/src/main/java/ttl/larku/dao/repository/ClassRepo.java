package ttl.larku.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.ScheduledClass;

import java.util.List;

@Repository
@Transactional
//Uncomment next line to not expose this repo as a REST resource
//@RepositoryRestResource(exported = false)
public interface ClassRepo extends JpaRepository<ScheduledClass, Integer> {

    public List<ScheduledClass> getByCourseCode(String code);

    @Query("select sc from ScheduledClass sc left join fetch sc.students where " +
            "sc.startDate = :startDate and sc.course.code = :code")
    public List<ScheduledClass> getByCourseCodeAndStartDateForStudents(String code, String startDate);


}
