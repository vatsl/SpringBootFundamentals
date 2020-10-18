package ttl.larku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import ttl.larku.controllers.rest.RestResult;
import ttl.larku.controllers.rest.RestResultGeneric;
import ttl.larku.domain.Student;

public class RestResultTest {

    @Test
    public void testOneGeneric() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Student s = new Student("Mack", "282 888 9292", Student.Status.FULL_TIME);
        RestResult rr = new RestResult().entity(s);

        String val = mapper.writeValueAsString(rr);

        JavaType type = mapper.getTypeFactory()
                .constructParametricType(RestResultGeneric.class, Student.class);

        RestResultGeneric<Student> rr2 = mapper.readValue(val, type);

        Student s2 = rr2.getEntity();
        System.out.println(s2);
        assertEquals("Mack", s2.getName());
    }

    @Test
    public void testOneNoGeneric() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Student s = new Student("Mack", "282 888 9292", Student.Status.FULL_TIME);
        RestResult rr = new RestResult().entity(s);

        String val = mapper.writeValueAsString(rr);
        //JsonNode root = mapper.readTree(val);

        RestResult rr2 = mapper.readValue(val, RestResult.class);
        Student s3 = mapper.convertValue(rr2.getEntity(), Student.class);

		/*
		JsonNode entity = root.path("entity");
		Student s2 = mapper.readerFor(Student.class).readValue(entity);
		*/
        System.out.println(s3);
        assertEquals("Mack", s3.getName());
    }

    @Test
    public void testGetAllNonGeneric() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Student> students = Arrays.asList(new Student("Mack", "282 888 9292", Student.Status.FULL_TIME),
                new Student("Zack", "282 875 9292", Student.Status.FULL_TIME),
                new Student("Spiro", "282 888 8765", Student.Status.HIBERNATING));
        RestResult rr = new RestResult().entity(students);

        String raw = mapper.writeValueAsString(rr);

        RestResult rr2 = mapper.readValue(raw, RestResult.class);

        CollectionType listType =
                mapper.getTypeFactory().constructCollectionType(List.class, Student.class);

        List<Student> l2 = mapper.convertValue(rr2.getEntity(), listType);
        System.out.println("l2: " + l2);


        System.out.println(rr2);
        assertEquals(3, l2.size());
    }

    @Test
    public void testGetAllNonGenericDeleteThis() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Student> students = Arrays.asList(new Student("Mack", "282 888 9292", Student.Status.FULL_TIME),
                new Student("Zack", "282 875 9292", Student.Status.FULL_TIME),
                new Student("Spiro", "282 888 8765", Student.Status.HIBERNATING));
        RestResult rr = new RestResult().entity(students);

        String raw = mapper.writeValueAsString(rr);
        JsonNode root = mapper.readTree(raw);

        JavaType type = mapper.getTypeFactory().constructParametricType(RestResultGeneric.class, Student.class);

        CollectionType listType =
                mapper.getTypeFactory().constructCollectionType(List.class, Student.class);

        JsonNode entity = root.path("entity");
        List<Student> l2 = mapper.readerFor(listType).readValue(entity);
        System.out.println("l2: " + l2);

        RestResult rr2 = mapper.readValue(raw, RestResult.class);

        System.out.println(rr2);
        assertEquals(3, l2.size());
    }

    public static void main(String[] args) throws IOException {
        new RestResultTest().testOneGeneric();
    }
}
