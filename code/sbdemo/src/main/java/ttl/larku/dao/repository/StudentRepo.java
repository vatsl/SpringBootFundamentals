package ttl.larku.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttl.larku.domain.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
}
