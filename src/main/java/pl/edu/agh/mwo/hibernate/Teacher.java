package pl.edu.agh.mwo.hibernate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private long pesel;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "schoolClasses_teachers",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "schoolClass_id")
    )
    private Set<SchoolClass> classes;

    public Teacher() {
    }

    public Teacher(String name, String surname, long pesel) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
    }

    public void addSchoolClass(SchoolClass schoolClass){
        classes.add(schoolClass);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    public Set<SchoolClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<SchoolClass> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return String.format("Teacher: %s %s (%s)", name, surname, pesel);
    }
}
