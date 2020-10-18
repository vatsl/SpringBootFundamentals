package ttl.larku.dao.jpahibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttl.larku.domain.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {
}
