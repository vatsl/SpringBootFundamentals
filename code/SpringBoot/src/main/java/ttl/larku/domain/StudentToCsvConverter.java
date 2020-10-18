package ttl.larku.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import ttl.larku.domain.Student;

@Component
public class StudentToCsvConverter extends AbstractHttpMessageConverter<Student> {

    public StudentToCsvConverter() {
        super(new MediaType("application", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Student.class.isAssignableFrom(clazz);
    }

    @Override
    protected Student readInternal(Class<? extends Student> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        String requestBody = toString(inputMessage.getBody());

        Student s = new Student();
        return s;
    }

    @Override
    protected void writeInternal(Student student, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        try {
            OutputStream outputStream = outputMessage.getBody();
            String body = student.getId() + "," + student.getName() + "," + student.getPhoneNumber() + "," + student.getStatus();
            outputStream.write(body.getBytes());
            outputStream.close();
        } catch (Exception e) {
        }
    }

    private static String toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        return scanner.useDelimiter("\\A").next();
    }
}