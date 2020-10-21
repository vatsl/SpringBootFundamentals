package ttl.larku.app;

import ttl.larku.domain.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JPADemo {

    public static void main(String[] args) {
        JPADemo jp = new JPADemo();
//        jp.addStudent();
        jp.updateStudent();
        jp.dumpStudents();
    }

    private EntityManagerFactory factory;
    public JPADemo() {
       factory = Persistence.createEntityManagerFactory("LarkUPU_SE");
    }

    public void updateStudent() {
       EntityManager manager = factory.createEntityManager();
       manager.getTransaction().begin();

       Student s = manager.find(Student.class, 19);
       s.setPhoneNumber("8769 747 999");

       manager.getTransaction().commit();
    }

    public void addStudent() {
        Student newStudent = new Student("Nelson", "3830 999 888383", Student.Status.HIBERNATING);
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        manager.persist(newStudent);

        manager.getTransaction().commit();
    }

    public void dumpStudents() {
        EntityManager manager = factory.createEntityManager();
        TypedQuery<Student> query = manager.createQuery("select s from Student s", Student.class);
        List<Student> students = query.getResultList();

        System.out.println("students:");
        students.forEach(System.out::println);
    }


}
