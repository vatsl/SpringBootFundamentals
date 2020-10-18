package ttl.larku.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ttl.larku.domain.Track;
import ttl.larku.service.TrackService;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/track")
public class TrackController {

    @Resource
    private TrackService trackService;

    @GetMapping
    public ResponseEntity<?> getAllTracks() {
        List<Track> tracks = trackService.getAllTracks();
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrack(@PathVariable("id") int id) {
        Track track = trackService.getTrack(id);
        if (track == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(track);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> getTrack(@PathVariable("title") String title) {
        List<Track> tracks = trackService.getTracksByTitle(title);
        return ResponseEntity.ok(tracks);
    }

    @PostMapping
    public ResponseEntity<?> addTrack(@RequestBody Track track) {
        Track newTrack = trackService.createTrack(track);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTrack.getId())
                .toUri();

        return ResponseEntity.created(uri).body(newTrack);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrack(@PathVariable("id") int id) {
        Track track = trackService.getTrack(id);
        if (track == null) {
            return ResponseEntity.badRequest().build();
        }
        trackService.deleteTrack(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateTrack(@RequestBody Track track) {
        trackService.updateTrack(track);
        return ResponseEntity.noContent().build();
    }
}
