package ttl.larku.service.props;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ttl.larku.service.reg.MyTestConfig;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = MyTestConfig.class)
public class ServiceThatWeDontOwnTest {

    @Autowired
    private ServiceThatWeDontOwn stwdo;

    @Test
    public void testServiceThatWeDontOwn() {
        boolean cd = stwdo.isCountDown();
        Duration dur = stwdo.getTimeout();

        System.out.println("cd: " + cd + ", dur: " + dur);

        assertFalse(cd);
        assertEquals(Duration.ofSeconds(10), dur);

    }

}
