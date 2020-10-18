package ttl.larku.controllers.rest.client;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ttl.larku.MyProfileValueSource;
import ttl.larku.controllers.rest.RestResult;
import ttl.larku.controllers.rest.RestResultGeneric;
import ttl.larku.controllers.rest.RestResultGeneric.Status;
import ttl.larku.controllers.rest.TemplateErrorHandler;
import ttl.larku.domain.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ProfileValueSourceConfiguration(MyProfileValueSource.class)
@IfProfileValue(name = "larku.runclient", value = "true")
public class RestClientTest {

    // GET with url parameters
    String rootUrl = "http://localhost:8080/adminrest/student";
    String oneStudentUrl = rootUrl + "/{id}";
    static RestTemplate rt;
    ObjectMapper mapper = new ObjectMapper();

    public static class JsonMimeInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            return execution.execute(request, body);
        }
    }

    @BeforeClass
    public static void uberInit() {
        rt = new RestTemplateBuilder().errorHandler(new TemplateErrorHandler()).build();
        rt.setInterceptors(Collections.singletonList(new JsonMimeInterceptor()));
    }

    @Before
    public void setup() {
    }

    @Test
    public void testGetOneStudentUsingAutoUnmarshalling() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<RestResult> response = rt.exchange(oneStudentUrl,
                HttpMethod.GET, entity, RestResult.class, 2);
        assertEquals(200, response.getStatusCodeValue());

        RestResult rr = response.getBody();
        RestResult.Status status = rr.getStatus();
        assertTrue(status == RestResult.Status.Ok);

        //Still need the mapper to convert the entity Object
        //which should be represented by a map of student properties
        Student s = mapper.convertValue(rr.getEntity(), Student.class);
        System.out.println("Student is " + s);
        assertTrue(s.getName().contains("Ana"));
    }

    @Test
    public void testGetOneStudentWithManualJson() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> headerEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(oneStudentUrl,
                HttpMethod.GET, headerEntity, String.class, 2);
        assertEquals(200, response.getStatusCodeValue());

        String raw = response.getBody();
        JsonNode root = mapper.readTree(raw);
        Status status = Status.valueOf(root.path("status").asText());
        assertTrue(status == Status.Ok);

        JsonNode entity = root.path("entity");
        Student s = mapper.treeToValue(entity, Student.class);
        System.out.println("Student is " + s);
        assertTrue(s.getName().contains("Ana"));
    }

    @Test
    public void testGetOneStudentBadId() throws IOException {
        ResponseEntity<RestResultGeneric> response = rt.getForEntity(oneStudentUrl, RestResultGeneric.class, 10000);
        assertEquals(400, response.getStatusCodeValue());

        RestResultGeneric raw = response.getBody();
        List<String> errors = raw.getErrors();
        assertTrue(errors != null);

        StringBuffer sb = new StringBuffer(100);
        errors.forEach(error -> {
            sb.append(error);
        });
        String reo = sb.toString();
        System.out.println("Error is " + reo);
        assertTrue(reo.contains("not found"));
    }

    @Test
    public void testGetAllUsingAutoUnmarshalling() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<RestResult> response = rt.exchange(rootUrl,
                HttpMethod.GET, entity, RestResult.class);

        assertEquals(200, response.getStatusCodeValue());

        RestResult rr = response.getBody();
        RestResult.Status status = rr.getStatus();
        assertTrue(status == RestResult.Status.Ok);

        //Jackson mechanism to represent a Generic Type List<Student>
        CollectionType listType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Student.class);
        List<Student> students = mapper.convertValue(rr.getEntity(), listType);
        System.out.println("l2 is " + students);

        assertEquals(4, students.size());
    }

    /**
     * Here we test getting the response as Json and then
     * picking our way through it using the ObjectMapper
     * We use RestResultGeneric here
     *
     * @throws IOException
     */
    @Test
    public void testGetAllWithJsonUsingRestResultGeneric() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> headerEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(rootUrl,
                HttpMethod.GET, headerEntity, String.class);

        assertEquals(200, response.getStatusCodeValue());
        String raw = response.getBody();
        JsonNode root = mapper.readTree(raw);

        Status status = Status.valueOf(root.path("status").asText());
        assertTrue(status == Status.Ok);

        //Have to make this complicated mapping to get
        //ResutResultGeneric<List<Student>>
        CollectionType listType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Student.class);
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(RestResultGeneric.class, listType);

        //We could unmarshal the whole entity
        RestResultGeneric<List<Student>> rr = mapper.readerFor(type).readValue(root);
        System.out.println("List is " + rr.getEntity());

        List<Student> l1 = rr.getEntity();

        // Create the collection type (since it is a collection of Authors)

        //Or we could step through the json to the entity and just unmarshal that
        JsonNode entity = root.path("entity");
        List<Student> l2 = mapper.readerFor(listType).readValue(entity);
        System.out.println("l2 is " + l2);

        assertEquals(4, l2.size());

    }

    /**
     * Here we are using RestResultGeneric having Jackson
     * do all the unmarshalling and give us the correct object
     *
     * @throws IOException
     */
    @Test
    public void testGetAllUsingRestResultGeneric() throws IOException {
        //This is the Spring REST mechanism to create a paramterized type
        ParameterizedTypeReference<RestResultGeneric<List<Student>>>
                ptr = new ParameterizedTypeReference<RestResultGeneric<List<Student>>>() {
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<RestResultGeneric<List<Student>>> response = rt.exchange(rootUrl,
                HttpMethod.GET, entity, ptr);

        assertEquals(200, response.getStatusCodeValue());
        RestResultGeneric<List<Student>> rr = response.getBody();

        Status status = rr.getStatus();
        assertTrue(status == Status.Ok);

        List<Student> l1 = rr.getEntity();
        assertEquals(4, l1.size());
    }
}
