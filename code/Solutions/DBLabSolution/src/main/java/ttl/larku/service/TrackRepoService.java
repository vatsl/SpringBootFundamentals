package ttl.larku.service;

import org.springframework.data.domain.Example;
import ttl.larku.dao.jpahibernate.TrackRepository;
import ttl.larku.domain.Track;

import java.util.List;

public class TrackRepoService {

    private TrackRepository trackDAO;

    public TrackRepoService(TrackRepository trackDAO) {
        this.trackDAO = trackDAO;
    }

    public Track createTrack(String title) {
        Track track = Track.title(title).build();
        track = trackDAO.save(track);

        return track;
    }

    public Track createTrack(String title, String artist, String album, String duration, String date) {
        return createTrack(Track.title(title).artist(artist).album(album).duration(duration).date(date).build());
    }

    public Track createTrack(Track track) {
        track = trackDAO.save(track);

        return track;
    }

    public void deleteTrack(int id) {
        Track track = trackDAO.findById(id).orElse(null);
        if (track != null) {
            trackDAO.deleteById(id);
        }
    }

    public void updateTrack(Track track) {
        trackDAO.save(track);
    }

    public Track getTrack(int id) {
        return trackDAO.findById(id).orElse(null);
    }

    public List<Track> getTracksByTitle(String title) {
        String lc = title.toLowerCase();
        Track t = Track.title(title).build();

        return getTracksByExample(t);
    }

    public List<Track> getTracksByExample(Track probe) {
        Example<Track> example = Example.of(probe);

        List<Track> found = trackDAO.findAll(example);

        return found;
    }

//	/**
//	 * We use Predicate to create a general query mechanism
//	 * @param pred
//	 * @return
//	 */
//	public List<Track> getTracksBy(Predicate<Track> pred) {
//		List<Track> found = trackDAO.findBy(pred);
//
//		return found;
//	}

    public List<Track> getAllTracks() {
        return trackDAO.findAll();
    }

    public TrackRepository getTrackDAO() {
        return trackDAO;
    }

    public void setTrackDAO(TrackRepository trackDAO) {
        this.trackDAO = trackDAO;
    }

    public void clear() {
        trackDAO.deleteAll();
    }

}
