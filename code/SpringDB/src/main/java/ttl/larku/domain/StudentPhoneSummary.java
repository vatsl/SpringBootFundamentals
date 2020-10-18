package ttl.larku.domain;

import org.springframework.data.rest.core.config.Projection;

/**
 * A projection to get only student id, name and phoneNumber
 *
 * @author whynot
 */
@Projection(types = {Student.class})
public interface StudentPhoneSummary {

    int getId();

    String getName();

    String getPhoneNumber();
}
