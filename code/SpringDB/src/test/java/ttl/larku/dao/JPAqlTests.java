package ttl.larku.dao;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/ttl/larku/db/createDB-" + WhichDB.value + ".sql",
        "/ttl/larku/db/populateDB-" + WhichDB.value + ".sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class JPAqlTests {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testLeftJoinFetchStudents() {
        TypedQuery<Student> query =
                em.createQuery("select s from Student s left join fetch s.classes sc left join fetch sc.course" , Student.class);
//        em.createQuery("select sc from ScheduledClass sc where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);


        List<Student> result = query.getResultList();
        result.forEach(System.out::println);
        assertEquals(4, result.size());
    }

    @Test
    public void testLeftJoinFetchScheduledClasses() {
        TypedQuery<ScheduledClass> query =
                em.createQuery("select sc from ScheduledClass sc left join fetch sc.students where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);
//        em.createQuery("select sc from ScheduledClass sc where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);

        query.setParameter("startDate", "2012-10-10");
        query.setParameter("code", "BOT-202");

        List<ScheduledClass> result = query.getResultList();
        result.forEach(System.out::println);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStudents().size());
    }

    @Test
    public void testLazyInstantionException() {
        assertThrows(LazyInitializationException.class, () -> {
            //Note - Leaving the 'fetch' out of the left join gives ut the LIE
            TypedQuery<ScheduledClass> query =
                    em.createQuery("select sc from ScheduledClass sc left join sc.students where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);
//        em.createQuery("select sc from ScheduledClass sc where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);

            query.setParameter("startDate", "2012-10-10");
            query.setParameter("code", "BOT-202");

            List<ScheduledClass> result = query.getResultList();
            result.forEach(System.out::println);
            assertEquals(1, result.size());
            assertEquals(1, result.get(0).getStudents().size());
        });
    }

    /**
     * Here, we make the test method Transactional, so the Transaction will
     * begin before this method get's called.  In this case the call to
     * getStudents() happens in a Transaction, so no LIE.
     */
    @Test
    @Transactional
    public void testTransactions() {
        //No join fetch here
        TypedQuery<ScheduledClass> query =
                em.createQuery("select sc from ScheduledClass sc where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);
//        em.createQuery("select sc from ScheduledClass sc where sc.startDate = :startDate and sc.course.code = :code", ScheduledClass.class);

        query.setParameter("startDate", "2012-10-10");
        query.setParameter("code", "BOT-202");

        List<ScheduledClass> result = query.getResultList();
        result.forEach(System.out::println);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStudents().size());
    }

    /**
     * In this test, we only fetch the ScheduledClasses first.  Then
     * we iterate over them and do fetch the Students for each one.
     * This would normally give us a LazyInstantionException, but we
     * are spared this time because we are using a local EntityManager
     * and fetching the Students in a transaction.
     */
    @Test
    public void testNPlusOneIssue() {
        EntityManager localManager = emf.createEntityManager();
        //No join fetch here
        TypedQuery<ScheduledClass> query = localManager.createQuery("select sc from " +
                "ScheduledClass sc where sc.startDate = :startDate and " +
                "sc.course.code = :code", ScheduledClass.class);

        query.setParameter("startDate", "2012-10-10");
        query.setParameter("code", "BOT-202");

        //Works happens in a transaction, so no LIE.
        //But can be costly, because you make 1 SQL call for
        //each Student in the collection
        localManager.getTransaction().begin();
        List<ScheduledClass> result = query.getResultList();
        for (ScheduledClass sc : result) {
            List<Student> students = sc.getStudents();
            students.forEach(System.out::println);
        }
        localManager.getTransaction().commit();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStudents().size());
    }
}
