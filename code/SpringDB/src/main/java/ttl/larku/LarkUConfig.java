package ttl.larku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryClassDAO;
import ttl.larku.dao.inmemory.InMemoryCourseDAO;
import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.dao.jpahibernate.JPAClassDAO;
import ttl.larku.dao.jpahibernate.JPACourseDAO;
import ttl.larku.dao.jpahibernate.JPAStudentDAO;
import ttl.larku.dao.repository.ClassRepo;
import ttl.larku.dao.repository.CourseRepo;
import ttl.larku.domain.*;
import ttl.larku.service.*;

@Configuration
@PropertySource({"classpath:/larkUContext.properties"})
//@EnableAspectJAutoProxy
public class LarkUConfig {

    @Autowired
    private Environment env;

    @Value("${larku.profile.active}")
    private String profile;

    @Bean
    @Profile("development")
    public BaseDAO<Student> studentDAO() {
        //return inMemoryStudentDAO();
        return jpaStudentDAO();
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
        return jpaCourseDAO();
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
        return jpaClassDAO();
    }

    @Bean(name = "classDAO")
    @Profile("production")
    public BaseDAO<ScheduledClass> classDAOJPA() {
        return jpaClassDAO();
    }

    @Bean
    public CourseService courseService() {
        CourseDaoService cc = new CourseDaoService();
        cc.setCourseDAO(courseDAO());

        return cc;
    }

    @Autowired
    private CourseRepo courseRepo;

    @Bean
    public CourseRepoService courseRepoService() {
        CourseRepoService cc = new CourseRepoService();
        cc.setCourseDAO(courseRepo);
        return cc;
    }

    @Bean
    public ClassService classService() {
        ClassDaoService cs = new ClassDaoService();
        cs.setClassDAO(classDAO());
        cs.setCourseService(courseService());
        return cs;
    }

    @Autowired
    private ClassRepo classRepo;

    @Bean
    public ClassRepoService classRepoService() {
        ClassRepoService cs = new ClassRepoService();
        cs.setClassDAO(classRepo);
        cs.setCourseService(courseService());
        return cs;
    }

    @Autowired
    private StudentDaoService studentDaoService;

    @Bean
    public RegistrationService registrationService() {
        RegistrationService rs = new RegistrationService();
        rs.setStudentService(studentDaoService);
        rs.setCourseService(courseService());
        rs.setClassService(classService());

        return rs;
    }

    @Autowired
    private StudentRepoService studentRepoService;

    @Bean
    public RegistrationService registrationRepoService() {
        RegistrationService rs = new RegistrationService();
        rs.setStudentService(studentRepoService);
        rs.setCourseService(courseRepoService());
        rs.setClassService(classRepoService());

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
     * @return
     */

    @Bean
    @ConfigurationProperties("ttl.timer.config")
    public DummyTimerConfigFromProps dummyTimerConfigFromProps() {
        return new DummyTimerConfigFromProps();
    }
}