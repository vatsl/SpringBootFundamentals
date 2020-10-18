package ttl.larku.jconfig;

import org.springframework.context.annotation.Bean;
import ttl.larku.dao.inmemory.InMemoryCourseDAO;

//@Configuration
public class TwoConfig {

    @Bean
    public InMemoryCourseDAO courseDAO() {
        return new InMemoryCourseDAO("TwoConfig");
    }

}
