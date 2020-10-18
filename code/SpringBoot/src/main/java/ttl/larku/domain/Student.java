package ttl.larku.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;


public class Student {

    public enum Status {
        FULL_TIME,
        PART_TIME,
        HIBERNATING
    }

    ;

    private int id;
    private String name;
    private String phoneNumber;

    private Status status = Status.FULL_TIME;

    private List<ScheduledClass> classes;

    private static int nextId = 0;

    public Student() {
        this("Unknown");
    }

    public Student(String name) {
        this(name, null, Status.FULL_TIME, new ArrayList<ScheduledClass>());
    }

    public Student(String name, String phoneNumber, Status status) {
        this(name, phoneNumber, status, new ArrayList<ScheduledClass>());
    }

    public Student(String name, String phoneNumber, Status status, List<ScheduledClass> classes) {
        super();
        this.name = name;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.classes = classes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Status[] getStatusList() {
        return Status.values();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public List<ScheduledClass> getClasses() {
        return classes;
    }

    public void setClasses(List<ScheduledClass> classes) {
        this.classes = classes;
    }


    public void addClass(ScheduledClass sClass) {
        classes.add(sClass);
    }

    public void dropClass(ScheduledClass sClass) {
        classes.remove(sClass);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", phoneNumber="
                + phoneNumber + ", status=" + status + ", classes=" + classes
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classes == null) ? 0 : classes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (classes == null) {
            if (other.classes != null)
                return false;
        } else if (!classes.equals(other.classes))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
