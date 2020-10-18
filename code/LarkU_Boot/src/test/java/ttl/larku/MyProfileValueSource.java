package ttl.larku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.ProfileValueSource;

import java.util.Properties;

/**
 * A Custom profile value source.  Strangely enough, application.properties is
 * not on the default path, @IfProfileValue does not find properties defined there.
 * We add this class to pick up properties from application.properties.
 * <p>
 * This is only available in the test scope.
 *
 * @author whynot
 */
@Component
public class MyProfileValueSource implements ProfileValueSource {

    @Autowired
    static Environment env;


    private static Properties props;

    static {
        try {
            Resource resource = new ClassPathResource("application.properties");
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String key) {
        String result = props.getProperty(key);
        return result;
    }

}
