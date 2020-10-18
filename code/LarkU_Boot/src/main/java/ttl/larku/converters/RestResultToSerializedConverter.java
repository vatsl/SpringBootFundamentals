package ttl.larku.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import ttl.larku.controllers.rest.RestResult;
import ttl.larku.domain.Student;

/**
 * You could use this converter to read/write a Java serialzed representation of
 * an object.  You would have to make sure that the object and all of it's descendants
 * are Serializable
 *
 * @author whynot
 */
@Component
public class RestResultToSerializedConverter extends AbstractHttpMessageConverter<RestResult> {

    public RestResultToSerializedConverter() {
        super(new MediaType("application", "x-serialized-object"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return RestResult.class.isAssignableFrom(clazz);
    }

    @Override
    protected RestResult readInternal(Class<? extends RestResult> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        String requestBody = toString(inputMessage.getBody());

        Student s = new Student();
        return null;
    }

    @Override
    protected void writeInternal(RestResult student, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        try {
            OutputStream outputStream = outputMessage.getBody();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(student);
            oos.close();
        } catch (Exception e) {
        }
    }

    private static String toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        return scanner.useDelimiter("\\A").next();
    }
}