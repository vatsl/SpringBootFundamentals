package ttl.larku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ttl.larku.domain.Track;
import ttl.larku.service.TrackService;

import java.util.List;

@RestController
@RequestMapping("/track")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping
    public List<Track> getAllTracks() {
       List<Track> tracks = trackService.getAllTracks();
       return tracks;
    }

    @GetMapping("/{id}")
    public Track getTrack(@PathVariable("id") int id) {
       Track track = trackService.getTrack(id);
       return track;
    }

}
