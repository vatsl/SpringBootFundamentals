package ttl.larku.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ttl.larku.dao.jpahibernate.TrackRepository;
import ttl.larku.domain.Track;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@ExtendWith(SpringExtension.class)
//Note - We are doing a 'slice' test here.  Spring will only create those
//parts of the ApplicationContext that are necessary to run JPA tests.
@DataJpaTest
public class TrackRepositoryTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TrackRepository dao;

    private Track track1, track2;

    @BeforeEach
    public void setup() {
        track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-01-01").build();
        track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-01-01").build();

        dao.save(track1);
        dao.save(track2);
    }


    @Test
    public void testGetAll() {
        List<Track> customers = dao.findAll();
        assertEquals(8, customers.size());
    }

    @Test
    public void testCreate() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();

        int newId = dao.saveAndFlush(track).getId();

        Track result = dao.findById(newId).orElse(null);

        assertEquals(newId, result.getId());
    }

    @Test
    public void testUpdate() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        int newId = dao.saveAndFlush(track).getId();

        Track result = dao.findById(newId).orElse(null);

        assertEquals(newId, result.getId());

        result.setTitle("Who do");
        dao.saveAndFlush(result);

        result = dao.findById(result.getId()).orElse(null);
        assertEquals("Who do", result.getTitle());
    }

    @Test
    public void testDelete() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        int newId = dao.saveAndFlush(track).getId();

        Track result = dao.findById(newId).orElse(null);

        assertEquals(newId, result.getId());

        int beforeSize = dao.findAll().size();

        dao.delete(result);

        result = dao.findById(result.getId()).orElse(null);

        assertEquals(beforeSize - 1, dao.findAll().size());
        assertNull(result);
    }

    /**
     * Test Query by Example
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
     */
    @Test
    public void testQueryByExample() {
        //This is the 'probe', i.e. your example entity object
        Track probe = Track.artist("Big John Patton").build();

        Track probe2 = Track.artist("Big John Patton").album("Moonlight in Vermont").build();

        //Here we can set up rules on how to do the matching
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                //ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("artist", contains())
                .withIgnorePaths("id")
                .withIgnoreCase()
                .withIgnoreNullValues();

        //Now we make our Example from the probe and the matcher
        Example<Track> example = Example.of(probe2, matcher);
        List<Track> results = dao.findAll(example);

        assertEquals(3, results.size());
    }
}
