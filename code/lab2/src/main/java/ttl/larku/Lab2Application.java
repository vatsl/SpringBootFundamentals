package ttl.larku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import ttl.larku.domain.Track;
import ttl.larku.service.TrackService;

import java.util.List;

@SpringBootApplication
public class Lab2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }

}

@Component
class Runner implements CommandLineRunner
{
    private final TrackService service;

    public Runner(TrackService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Track> tracks = service.getAllTracks();
        System.out.println("Tracks: ");
        tracks.forEach(System.out::println);

    }
}
