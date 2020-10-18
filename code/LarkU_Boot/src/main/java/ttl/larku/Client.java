package ttl.larku;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ttl.larku.controllers.rest.RestResult;
import ttl.larku.controllers.rest.RestResultGeneric;
import ttl.larku.controllers.rest.TemplateErrorHandler;
import ttl.larku.controllers.rest.RestResultGeneric.Status;
import ttl.larku.domain.Student;

public class Client {

    public static void main(String[] args) throws IOException {
        // variousGets();
        Student student = new Student("Marnie");
        //student.setPhoneNumber("124-45-333");
        //postStudent(student);
        //getOneStudentCsv();
        getOneStudentRestResult();
        //getOneStudentRestResultNonGeneric();
        //getOneAllStudentsRestResult();

    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static RestResultGeneric<List<Student>> getOneAllStudentsRestResult() throws IOException {
        String root = "http://localhost:8080/adminrest/student";
        RestTemplate rt = new RestTemplateBuilder()
                .errorHandler(new TemplateErrorHandler()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAccept(Arrays.asList(new MediaType("application", "json")));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResultGeneric<List<Student>>> ptr =
                new ParameterizedTypeReference<RestResultGeneric<List<Student>>>() {
                };

        ResponseEntity<RestResultGeneric<List<Student>>> response = rt.exchange(root,
                HttpMethod.GET, entity, ptr);

        RestResultGeneric<List<Student>> rr = response.getBody();

        List<Student> s = rr.getEntity();
        System.out.println("Students " + s);
        return rr;
    }

    public static void getOneStudentRestResultNonGeneric() throws IOException {
        String rootUrl = "http://localhost:8080/adminrest/student/nongen/1";
        RestTemplate rt = new RestTemplateBuilder()
                .errorHandler(new TemplateErrorHandler()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(rootUrl,
                HttpMethod.GET, entity, String.class);

        String raw = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(raw);
        Status status = Status.valueOf(root.path("status").asText());
        if (status == Status.Error) {
            System.out.println("Eeeeek");
        } else {
            JsonNode en = root.path("entity");
            Student s = mapper.readerFor(Student.class).readValue(en);
            System.out.println("Student raw is " + s);
        }
    }

    public static RestResultGeneric getOneStudentRestResult() throws IOException {
        String root = "http://localhost:8080/adminrest/student/nongen/1";
        RestTemplate rt = new RestTemplateBuilder()
                .errorHandler(new TemplateErrorHandler()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAccept(Arrays.asList(new MediaType("application", "json")));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<RestResultGeneric<Student>> ptr =
                new ParameterizedTypeReference<RestResultGeneric<Student>>() {
                };

        ResponseEntity<RestResultGeneric<Student>> response = rt.exchange(root,
                HttpMethod.GET, entity, ptr);

        RestResultGeneric<Student> rr = response.getBody();

        Student s = rr.getEntity();
        System.out.println("Student raw is " + s);
        return rr;
    }

    public static String getOneStudentCsv() throws IOException {
        String root = "http://localhost:8080/adminrest/student/1";
        RestTemplate rt = new RestTemplateBuilder()
                .errorHandler(new TemplateErrorHandler()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAccept(Arrays.asList(new MediaType("application", "csv")));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(root, HttpMethod.GET, entity, String.class);
        String raw = response.getBody();
        System.out.println("Student csv is " + raw);
        return raw;
    }

    public static Student postStudent(Student student) throws IOException {
        String root = "http://localhost:8080/adminrest/student";
        RestTemplate rt = new RestTemplateBuilder().errorHandler(new TemplateErrorHandler()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAccept(Arrays.asList(new MediaType("application", "csv")));
        HttpEntity<Student> entity = new HttpEntity<Student>(student, headers);

        ResponseEntity<String> response = rt.postForEntity(root, entity, String.class);
        String raw = response.getBody();
        JsonNode jsonNode = mapper.readTree(raw);
        Student newStudent = null;
        if (response.getStatusCodeValue() != 201) {
            System.out.println("Bad Status: " + response.getStatusCodeValue());
            ArrayNode jn = (ArrayNode) jsonNode.findValue("errors");
            if (jn != null) {

                StringBuffer sb = new StringBuffer(100);
                jn.forEach(node -> {
                    sb.append(node.asText());
                });
                String reo = sb.toString();
                System.out.println("Error is " + reo);
            }
        } else {
            newStudent = mapper.treeToValue(jsonNode, Student.class);
            System.out.println("new Student is " + newStudent);
        }

        return newStudent;
    }

    public static void variousGets() {
        RestTemplate rt = new RestTemplate();
        // Simple GET
        ResponseEntity<Student> response = rt.getForEntity("http://localhost:8080/adminrest/student/1", Student.class);
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Student: " + response.getBody());
        }

        // GET a List
        ResponseEntity<List<Student>> response2 = rt.exchange("http://localhost:8080/adminrest/student", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Student>>() {
                });
        System.out.println(response2.getBody());

        // GET with url parameters
        String rootUrl = "http://localhost:8080/adminrest/student";
        String oneStudentUrl = rootUrl + "/{id}";

        ResponseEntity<Student> response3 = rt.getForEntity(oneStudentUrl, Student.class, 1);
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Student: " + response3.getBody());
        }

        // GET with parameters a different way
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("id", 2);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(oneStudentUrl);
        ResponseEntity<Student> response4 = rt.getForEntity(builder.buildAndExpand(pathParams).toUri(), Student.class);
        if (response.getStatusCodeValue() == 200) {
            System.out.println("Student: " + response4.getBody());
        }

    }

}
