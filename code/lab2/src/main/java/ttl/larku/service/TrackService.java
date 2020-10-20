package ttl.larku.service;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Track;

import java.util.List;

public class TrackService {

    private BaseDAO<Track> trackDAO;

    public Track createTrack(String title) {
        Track track = Track.title(title).build();
        track = trackDAO.create(track);

        return track;
    }

    public Track createTrack(String title, String artist, String album, String duration, String date) {
        return Track.title(title).artist(artist).album(album).duration(duration).date(date).build();
    }

    public Track createTrack(Track track) {
        track = trackDAO.create(track);

        return track;
    }

    public void deleteTrack(int id) {
        Track track = trackDAO.get(id);
        if (track != null) {
            trackDAO.delete(track);
        }
    }

    public void updateTrack(Track track) {
        trackDAO.update(track);
    }

    public Track getTrack(int id) {
        return trackDAO.get(id);
    }

    public List<Track> getAllTracks() {
        return trackDAO.getAll();
    }

    public BaseDAO<Track> getTrackDAO() {
        return trackDAO;
    }

    public void setTrackDAO(BaseDAO<Track> trackDAO) {
        this.trackDAO = trackDAO;
    }

    public void clear() {
        trackDAO.deleteStore();
        trackDAO.createStore();
    }

}
