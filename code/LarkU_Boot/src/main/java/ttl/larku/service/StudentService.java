package ttl.larku.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Resource(name = "studentDAO")
    private BaseDAO<Student> studentDAO;

    @Autowired
    private ObjectMapper mapper;

    public Student createStudent(String name) {
        Student student = new Student(name);
        student = studentDAO.create(student);

        return student;
    }

    public Student createStudent(String name, String phoneNumber, Student.Status status) {
        Student student = new Student(name, phoneNumber, status);
        student = studentDAO.create(student);

        return student;
    }

    public Student createStudent(Student student) {
        student = studentDAO.create(student);

        return student;
    }

    public void deleteStudent(int id) {
        Student student = studentDAO.get(id);
        if (student != null) {
            studentDAO.delete(student);
        }
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }


    public void updateStudentPartial(int id, Map<String, Object> props) {
        Student student = studentDAO.get(id);
        if (student != null) {
            Class<?> clazz = Student.class;
            Map<String, Method> methods = Arrays
                    .asList(clazz.getMethods()).stream()
                    .filter(m -> m.getName().startsWith("set"))
                    .collect(Collectors.toMap(m -> m.getName(), m -> m));

            props.forEach((name, value) -> {
                if (!name.equals("id")) {
                    String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    Method m = methods.get(setMethodName);
                    if (m != null) {
                        Class<?>[] paramTypes = m.getParameterTypes();
                        if (paramTypes.length == 1) {
                            Object param = value;
                            try {
                                Class<?> pClass = paramTypes[0];
                                //We only handle String and Student.Status for now
                                //Or List<Scheduled> class at this point
                                if (pClass.equals(String.class)) {
                                    param = String.valueOf(value);
                                }
                                //Handle Status
                                else if (pClass.isAssignableFrom(Student.Status.class)) {
                                    param = Student.Status.valueOf(String.valueOf(value));
                                }
                                //List<ScheduledClass>.
                                //We filter based on methodName just for variation
                                else if (setMethodName.equals("setClasses")) {
                                    List<Map<String, Object>> valueMaps = (List) value;
                                    List<ScheduledClass> really = mapper.convertValue(valueMaps,
                                            new TypeReference<List<ScheduledClass>>() {
                                            });
                                    param = really;

                                }
                                m.invoke(student, param);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            int i = 0;
        }
    }

    public void updateStudentPartialBeanWrapper(int id, Map<String, Object> props) {
        Student student = studentDAO.get(id);
        if (student != null) {

//            BeanWrapper bw = new BeanWrapperImpl(student);
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(student);
            props.forEach((name, value) -> {
                if (!name.equals("id")) {
                    if (bw.isWritableProperty(name)) {
                        Class<?> pClass = bw.getPropertyType(name);
                        bw.setPropertyValue(name, bw.convertIfNecessary(value, pClass));
                    }
                }
            });
        }
    }

    public Student getStudent(int id) {
        return studentDAO.get(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    public BaseDAO<Student> getStudentDAO() {
        return studentDAO;
    }

    public void setStudentDAO(BaseDAO<Student> studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void clear() {
        studentDAO.deleteStore();
        studentDAO.createStore();
    }
}
