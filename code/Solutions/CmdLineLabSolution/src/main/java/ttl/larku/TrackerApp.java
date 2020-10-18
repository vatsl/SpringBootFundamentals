package ttl.larku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ttl.larku.domain.Track;
import ttl.larku.service.TrackService;

import java.util.List;

@SpringBootApplication
public class TrackerApp {
    /**
     * An example of main that you could use to run a Spring boot
     * application as a JavaSE application.  No Web Environment.
     * A CommandLineRunner is useful in this context to actually
     * get some work done.
     * @param args
     */
    public static void main(String [] args) {
        ConfigurableApplicationContext ctx =
                new SpringApplicationBuilder(TrackerApp.class)
                        .web(WebApplicationType.NONE)
                        .run(args);

        ctx.close();
    }
}

@Component
class AppRunner implements CommandLineRunner
{
    private final TrackService trackService;

    public AppRunner(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Track> tracks = trackService.getAllTracks();
        System.out.println("Tracks:");
        tracks.forEach(System.out::println);
    }
}
