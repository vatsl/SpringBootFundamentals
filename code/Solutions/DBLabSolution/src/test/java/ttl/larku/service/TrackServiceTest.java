package ttl.larku.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ttl.larku.domain.Track;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrackServiceTest {

    @Resource
    private TrackService trackService;

    @BeforeEach
    public void setup() {
        trackService.clear();
    }

    @Test
    public void testCreateTrack() {
        Track newTrack = trackService.createTrack("You Stepped Out Of A Dream", "Herb Ellis", "Three guitars in Bossa Nova Time",
                "04:19", "1963-02-02");
        newTrack = trackService.createTrack(newTrack);

        Track result = trackService.getTrack(newTrack.getId());

        assertTrue(result.getTitle().contains(newTrack.getTitle()));
        assertEquals(1, trackService.getAllTracks().size());
    }

    @Test
    public void testDeleteTrack() {
        Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-02-02").build();
        track1 = trackService.createTrack(track1);
        Track track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-02-02").build();
        track2 = trackService.createTrack(track2);

        assertEquals(2, trackService.getAllTracks().size());

        trackService.deleteTrack(track1.getId());

        assertEquals(1, trackService.getAllTracks().size());
        assertTrue(trackService.getAllTracks().get(0).getTitle().contains("April"));
    }

    @Test
    public void testDeleteNonExistentTrack() {
        Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-02-02").build();
        track1 = trackService.createTrack(track1);
        Track track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-02-02").build();
        track2 = trackService.createTrack(track2);

        assertEquals(2, trackService.getAllTracks().size());

        trackService.deleteTrack(9999);

        assertEquals(2, trackService.getAllTracks().size());
    }

    @Test
    public void testUpdateTrack() {
        Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-02-02").build();

        track1 = trackService.createTrack(track1);

        assertEquals(1, trackService.getAllTracks().size());

        track1.setTitle("A Shadowy Smile");
        trackService.updateTrack(track1);

        assertEquals(1, trackService.getAllTracks().size());
        assertTrue(trackService.getAllTracks().get(0).getTitle().contains("Shadowy"));
    }

    @Test
    public void testGetBy() {
        Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-02-02").build();
        track1 = trackService.createTrack(track1);
        Track track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-02-02").build();
        track2 = trackService.createTrack(track2);

        List<Track> aprils = trackService.getTracksByTitle("April");

        System.out.println("aprils: " + aprils);
        assertEquals(1, aprils.size());
        assertEquals("05:54", aprils.get(0).getDuration());
    }

    @Test
    public void testGetByPredicates() {
        Track track1 = Track.title("The Shadow Of Your Smile").artist("Big John Patton")
                .album("Let 'em Roll").duration("06:15").date("1965-02-02").build();
        track1 = trackService.createTrack(track1);
        Track track2 = Track.title("I'll Remember April").artist("Jim Hall and Ron Carter")
                .album("Alone Together").duration("05:54").date("1972-02-02").build();
        track2 = trackService.createTrack(track2);

        Predicate<Track> artist = (t) -> t.getArtist().equals("Big John Patton");
        Predicate<Track> durationGreaterThan6 = (t) -> {
            int dur = Integer.valueOf(t.getDuration().replace(":", ""));
            return dur < 600;
        };

        List<Track> result = trackService.getTracksBy(artist);

        System.out.println("result: " + result);
        assertEquals(1, result.size());
        assertEquals("06:15", result.get(0).getDuration());

        result = trackService.getTracksBy(durationGreaterThan6);
        System.out.println("result: " + result);
    }
}
