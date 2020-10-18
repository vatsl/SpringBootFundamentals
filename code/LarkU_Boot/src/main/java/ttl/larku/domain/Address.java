package ttl.larku.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Address {

    @XmlElement
    private String street;
    @XmlElement
    private String city;
    @XmlElement
    private String state;
    @XmlElement
    private String zip;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	/*
	@XmlTransient
	@JsonBackReference
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Student> students = new ArrayList<Student>();
	*/

    private String startDate;
    private String endDate;


    private Course course;


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
