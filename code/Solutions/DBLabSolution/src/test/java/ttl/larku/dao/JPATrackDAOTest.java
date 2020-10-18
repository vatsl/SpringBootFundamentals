package ttl.larku.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ttl.larku.domain.Track;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JPATrackDAOTest {


    @Autowired
    private BaseDAO<Track> trackDAO;

    private Track track1, track2;

    @BeforeEach
    public void setup() {
        trackDAO.createStore();

        track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-02-03").build();
        track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-02-03").build();

        trackDAO.create(track1);
        trackDAO.create(track2);
    }


    @Test
    public void testGetAll() {
        List<Track> tracks = trackDAO.getAll();
        assertTrue(tracks.size() > 0);
    }

    @Test
    public void testCreate() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();

        int newId = trackDAO.create(track).getId();

        Track result = trackDAO.get(newId);

        assertEquals(newId, result.getId());
    }

    @Test
    public void testUpdate() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        int newId = trackDAO.create(track).getId();

        Track result = trackDAO.get(newId);

        assertEquals(newId, result.getId());

        result.setTitle("Who do");
        trackDAO.update(result);

        result = trackDAO.get(result.getId());
        assertEquals("Who do", result.getTitle());
    }

    @Test
    @Transactional
    public void testDelete() {
        Track track = Track.title("What's New").artist("John Coltrane")
                .album("Ballads").duration("03:47").build();
        int newId = trackDAO.create(track).getId();

        Track result = trackDAO.get(newId);

        assertEquals(newId, result.getId());

        int beforeSize = trackDAO.getAll().size();

        trackDAO.delete(result);

        result = trackDAO.get(result.getId());

        assertEquals(beforeSize - 1, trackDAO.getAll().size());
        assertNull(result);

    }
}
