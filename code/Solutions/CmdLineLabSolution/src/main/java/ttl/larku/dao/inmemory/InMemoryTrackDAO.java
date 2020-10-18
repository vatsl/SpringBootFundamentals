package ttl.larku.dao.inmemory;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTrackDAO implements BaseDAO<Track> {

    private Map<Integer, Track> tracks = new ConcurrentHashMap<Integer, Track>();
    private static AtomicInteger nextId = new AtomicInteger(0);

    @Override
    public void update(Track updateObject) {
        tracks.computeIfPresent(updateObject.getId(), (key, uo) -> updateObject);
    }

    @Override
    public void delete(Track deleteObject) {
        tracks.remove(deleteObject.getId());
    }

    @Override
    public Track create(Track newObject) {
        //Create a new Id
        int newId = nextId.incrementAndGet();
        newObject.setId(newId);
        tracks.put(newId, newObject);

        return newObject;
    }

    @Override
    public Track get(int id) {
        return tracks.get(id);
    }

    @Override
    public List<Track> getAll() {
        return new ArrayList<Track>(tracks.values());
    }

    @Override
    public void deleteStore() {
        tracks = null;
    }

    @Override
    public void createStore() {
        tracks = new HashMap<Integer, Track>();
        nextId.set(0);
    }

    public Map<Integer, Track> getTracks() {
        nextId.set(0);
        return tracks;
    }

    public void setTracks(Map<Integer, Track> tracks) {
        this.tracks = tracks;
    }
}
