package ttl.larku.ttl.larku;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import ttl.larku.controllers.rest.RestResult;
import ttl.larku.controllers.rest.StudentRestController;
import ttl.larku.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StraightTestNoSpring {

    @Autowired
    private StudentRestController studentRestController;

//    @Test
    public void testCreateStudent() {
        UriComponentsBuilder uc = UriComponentsBuilder.fromPath("xyz");
        ResponseEntity<?> result = studentRestController.getStudent(2);
        RestResult rs = (RestResult) result.getBody();
        Student s = (Student) rs.getEntity();
        assertEquals(2, s.getId());

        studentRestController.createStudent(s);
    }
}


