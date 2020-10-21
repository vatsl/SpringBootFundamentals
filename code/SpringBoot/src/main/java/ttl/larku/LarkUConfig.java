package ttl.larku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryClassDAO;
import ttl.larku.dao.inmemory.InMemoryCourseDAO;
import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.dao.jpahibernate.JPAClassDAO;
import ttl.larku.dao.jpahibernate.JPACourseDAO;
import ttl.larku.dao.jpahibernate.JPAStudentDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;
import ttl.larku.service.*;
import ttl.larku.service.props.ServiceThatWeDontOwn;

@Configuration
@PropertySource({"classpath:/larkUContext.properties"})
public class LarkUConfig {

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private LarkUTestDataConfig testDataProducer;

    @Value("${larku.profile.active}")
    private String profile;

    @Bean
    @Profile("development")
    public BaseDAO<Student> studentDAO() {
        //return inMemoryStudentDAO();
        BaseDAO<Student> bs = testDataProducer.studentDAOWithInitData();
        return bs;
    }

    @Bean(name = "studentDAO")
    @Profile("production")
    public BaseDAO<Student> studentDAOJpa() {
        return jpaStudentDAO();
    }

    @Bean
    @Profile("development")
    public BaseDAO<Course> courseDAO() {
        //return inMemoryCourseDAO();
        return testDataProducer.courseDAOWithInitData();
    }

    @Bean(name = "courseDAO")
    @Profile("production")
    public BaseDAO<Course> courseDAOJPA() {
        return jpaCourseDAO();
    }

    @Bean
    @Profile("development")
    public BaseDAO<ScheduledClass> classDAO() {
        //return inMemoryClassDAO();
        return testDataProducer.classDAOWithInitData();
    }

    @Bean(name = "classDAO")
    @Profile("production")
    public BaseDAO<ScheduledClass> classDAOJPA() {
        return jpaClassDAO();
    }

    @Bean
    public CourseService courseService() {
        CourseService cc = new CourseService();
        cc.setCourseDAO(courseDAO());

        return cc;
    }

    @Bean
    public ClassService classService() {
        ClassService cs = new ClassService();
        cs.setClassDAO(classDAO());
        cs.setCourseService(courseService());
        return cs;
    }

    @Autowired
    private StudentService studentService;

    @Bean
    public RegistrationService registrationService() {
        RegistrationService rs = new RegistrationService();
        rs.setStudentService(studentService);
        rs.setCourseService(courseService());
        rs.setClassService(classService());

        return rs;
    }

    @Bean
    public BaseDAO<Student> inMemoryStudentDAO() {
        return new InMemoryStudentDAO();
    }

    @Bean
    public BaseDAO<Student> jpaStudentDAO() {
        return new JPAStudentDAO();
    }

    @Bean
    public BaseDAO<Course> inMemoryCourseDAO() {
        return new InMemoryCourseDAO();
    }

    @Bean
    public BaseDAO<Course> jpaCourseDAO() {
        return new JPACourseDAO();
    }

    @Bean
    public BaseDAO<ScheduledClass> inMemoryClassDAO() {
        return new InMemoryClassDAO();
    }

    @Bean
    public BaseDAO<ScheduledClass> jpaClassDAO() {
        return new JPAClassDAO();
    }

    /**
     * You can use @ConfigurationProperties on an @Bean method.
     * Can be  useful when you want to initialize classes you don't
     * own.
     *
     * @return
     */

    @Bean
    @ConfigurationProperties("ttl.stwdo.config")
    public ServiceThatWeDontOwn serviceThatWeDontOwn() {
        return new ServiceThatWeDontOwn();
    }

    /**
     * Validator
     <bean id="validator"
     class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>
     */
//    @Bean
//    public LocalValidatorFactoryBean validator() {
//        return new LocalValidatorFactoryBean();
//    }
}
