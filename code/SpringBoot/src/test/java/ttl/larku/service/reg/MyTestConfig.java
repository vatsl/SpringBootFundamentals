package ttl.larku.service.reg;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import ttl.larku.LarkUConfig;
import ttl.larku.SpringBootApp;

/**
 * An Example of a Test Configuration.
 *
 * If you run this without the 'excludeFilters', the test will run, but it will also pick
 * the SpringBootApp, which will end up making the entire context, i.e. as if you had just
 * said @SpringBootTest to run the test.
 *
 * We could point at individual packages, but we have a bunch of stuff defined in LarkUConfig,
 * which is in the ttl.larku package, so we need to scan from "ttl.larku".  Which
 * means we need the exclude filter to filter out the SpringBootApp class so we only
 * pick up our config files from ttl.larku
 *
 * Also, if we want to use @ConfigurationProperties, then we need to bring in more beans,
 * but exactly which ones is not clear.  At that point, @SpringBootTest(webenvironment = NONE)
 * is probably your best bet.
 */
@TestConfiguration
//@ComponentScan(basePackages = {"ttl.larku"}) //, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".* SpringBootApp.*"))
//@ComponentScan(basePackages = {"ttl.larku"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = SpringBootApplication.class))
@ComponentScan(basePackages = {"ttl.larku"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SpringBootApp.class))
@ComponentScan(basePackages = {"ttl.larku.service", "ttl.larku.dao"})
@Import(LarkUConfig.class)
//@EnableConfigurationProperties
public class MyTestConfig{
}
