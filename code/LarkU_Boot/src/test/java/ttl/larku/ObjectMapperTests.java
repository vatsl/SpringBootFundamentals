package ttl.larku;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ttl.larku.domain.ScheduledClass;

public class ObjectMapperTests {

    @Test
    public void testUnserializeMap() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"classes\":["
                + "{\"id\":2,\"startDate\":\"2012-10-10\",\"endDate\":\"2013-08-10\",\"course\":{\"id\":2,\"title\":\"Yet more Botany\",\"code\":\"BOT-202\",\"credits\":2.0}}, "
                + "{\"id\":3,\"startDate\":\"2012-10-10\",\"endDate\":\"2013-08-10\",\"course\":{\"id\":2,\"title\":\"Yet more Botany\",\"code\":\"BOT-202\",\"credits\":2.0}}]}";
        Map<String, List<Map<String, Object>>> result = mapper.readValue(jsonString,
                new TypeReference<Map<String, List<Map<String, Object>>>>() {
                });

        List<Map<String, Object>> lsc = result.get("classes");


        List<ScheduledClass> really = mapper.convertValue(lsc, new TypeReference<List<ScheduledClass>>() {
        });
        really.forEach(System.out::println);
        assertEquals(2, really.size());
    }

}
