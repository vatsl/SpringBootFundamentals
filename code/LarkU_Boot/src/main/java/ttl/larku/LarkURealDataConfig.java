package ttl.larku;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.jpahibernate.JPAClassDAO;
import ttl.larku.dao.jpahibernate.JPACourseDAO;
import ttl.larku.dao.jpahibernate.JPAStudentRepoDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@Configuration
@Profile("production")
public class LarkURealDataConfig {

    @Bean
    public BaseDAO<Student> studentDAO() {
        //return new JPAStudentDAO();
        return new JPAStudentRepoDAO();
    }

    @Bean
    public BaseDAO<Course> courseDAO() {
        return new JPACourseDAO();
    }

    @Bean
    public BaseDAO<ScheduledClass> classDAO() {
        return new JPAClassDAO();
    }

	/*
	@Bean
	public LocalEntityManagerFactoryBean entityManagerFactory() {
		LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
		factoryBean.setPersistenceUnitName("LarkUPU_SE");
		return factoryBean;
	}
	*/

}