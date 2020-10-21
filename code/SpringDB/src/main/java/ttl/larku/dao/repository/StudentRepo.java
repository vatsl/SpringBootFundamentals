package ttl.larku.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.Student;
import ttl.larku.domain.StudentCourseCodeSummary;
import ttl.larku.domain.StudentPhoneSummary;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface StudentRepo extends JpaRepository<Student, Integer> {

    //This is a more efficient way of fetching the student *and* all their classes
    //than the default N + 1 approach that Hibernate/Spring will use.
//    @Query("select s from Student s left join fetch s.classes sc left join fetch sc.course")
//    public List<Student> findAll();

    //@Query("select s from Student s where s.name = :name")
    public List<Student> findByName(@Param("name") String name);

    public List<Student> findByNameLike(@Param("name") String name);

    // Change method visibility by overriding them and
    // setting exported to false
    // Comment out the following two lines to hide the findById method
    @RestResource(exported = false)
    Optional<Student> findById(Integer primaryKey);

    public StudentPhoneSummary findPhoneSummaryById(Integer id);

    //Methods that return a Projection as a List do NOT
    //work with Spring Data Rest - IllegalArgumentException is thrown
    //So you would *have* to call these from Controller/Service code.
    public List<StudentPhoneSummary> findAllStudentPhoneSummariesBy();

    public List<StudentCourseCodeSummary> findStudentCourseCodesBy();

    // For example usage, see StudentRepoTest::testProjectionStudentClassCourseCode
    public Page<StudentCourseCodeSummary> findPageCourseCodeBy(Pageable p);
}
