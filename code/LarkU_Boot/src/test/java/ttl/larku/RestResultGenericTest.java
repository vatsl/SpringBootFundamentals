package ttl.larku;

import java.io.IOException;

import javax.xml.crypto.Data;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ttl.larku.controllers.rest.RestResultGeneric;
import ttl.larku.domain.Student;

public class RestResultGenericTest {

    @Test
    public void testConvertToJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Student s = new Student("Mack", "282 888 9292", Student.Status.FULL_TIME);
        RestResultGeneric<Student> rr = new RestResultGeneric().entity(s);

        String val = mapper.writeValueAsString(rr);

        JavaType type = mapper.getTypeFactory()
                .constructParametricType(RestResultGeneric.class, Student.class);
        RestResultGeneric<Student> rr2 = mapper.readValue(val, type);

        System.out.println(rr2);
        Student s2 = rr2.getEntity();
        System.out.println("Student: " + s2);
    }

    public static void main(String[] args) throws IOException {
        new RestResultGenericTest().testConvertToJson();
    }
}
