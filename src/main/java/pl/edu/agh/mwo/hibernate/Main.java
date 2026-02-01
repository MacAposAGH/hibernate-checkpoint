package pl.edu.agh.mwo.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class Main {

    Session session;

    public static void main(String[] args) {
        Main main = new Main();

//		main.addNewData();
//		main.printSchools();
        main.executeQueries();
//		main.updateObject();

//        main.cascadeTest();
        main.deleteById(School.class, 3);

        main.printTeachers();
        main.printClasses();

        main.close();
    }

    public Main() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }

    private void executeQueries() {
        query0();
//		 query1();
//		 query2();
//		 query3();
//		 query4();
//		 query5();
//		 query6();
    }

    private void query0() {
        String hql = "from School";
        Query<School> query = session.createQuery(hql, School.class);
        List<School> results = query.list();
        System.out.println(results);
    }

    private void query1() {
        String hql = "from School s where s.name='UE'";
        Query<School> query = session.createQuery(hql, School.class);
        List<School> results = query.list();
        System.out.println(results);
    }

    private void query2() {
        String hql = "from School s where s.name='UE'";
        Query<School> query = session.createQuery(hql, School.class);
        List<School> results = query.list();
        Transaction transaction = session.beginTransaction();
        for (School s : results) {
            session.delete(s);
        }
        transaction.commit();
    }

    private <T> void deleteById(Class<T> clazz, int id) {
        Query<T> query = session.createQuery(String.format("from %s where id=%s", clazz.getSimpleName(), id), clazz);
        Transaction transaction = session.beginTransaction();
        for (Object s : query.list()) {
            session.delete(s);
        }
        transaction.commit();
    }

    private void query3() {
        String hql = "select count(s) from School s";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long schoolsCount = query.uniqueResult();
        System.out.println("Schools count: " + schoolsCount);
    }

    private void query4() {
        String hql = "select count(s) from Student s";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long schoolsCount = query.uniqueResult();
        System.out.println("Students count: " + schoolsCount);
    }

    private void query5() {
        String hql = "select count(s) from School s where size(s.classes) >= 2";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long schoolsCount = query.uniqueResult();
        System.out.println("Schools count: " + schoolsCount);
    }

    private void query6() {
        String hql = "select s from School s inner join s.classes c where c.profile = 'mat-fiz' and c.currentYear >= 2";
        Query<School> query = session.createQuery(hql, School.class);
        List<School> results = query.list();
        System.out.println(results);
    }

    private void updateObject() {
        Query<School> query = session.createQuery("from School where id = :id", School.class);
        query.setParameter("id", 2L);
        School school = query.uniqueResult();
        school.setAddress("Nowy adres2");

        Transaction transaction = session.beginTransaction();
        session.save(school);
        transaction.commit();
    }

    private void addNewData() {
        School newSchool = new School();
        newSchool.setName("Nowa szkoła");
        newSchool.setAddress("ul. Ulica 0, Miasto");

        SchoolClass newClass = new SchoolClass();
        newClass.setProfile("profil");
        newClass.setStartYear(2020);
        newClass.setCurrentYear(1);

        Student newStudent = new Student();
        newStudent.setName("Jan");
        newStudent.setSurname("Kowalski");
        newStudent.setPesel("12345678901");

        newClass.addStudent(newStudent);
        newSchool.addClass(newClass);

        Transaction transaction = session.beginTransaction();
        session.save(newSchool);
        transaction.commit();
    }

    private void cascadeTest() {
        School newSchool = new School();
        newSchool.setName("Stara szkoła");
        newSchool.setAddress("ul. Ulica 100, Małe Miasto");

        SchoolClass newClass = new SchoolClass();
        newClass.setProfile("przedmiot");
        newClass.setStartYear(2020);
        newClass.setCurrentYear(1);

        Student newStudent = new Student();
        newStudent.setName("Zbigniew");
        newStudent.setSurname("Nowak");
        newStudent.setPesel("12345678901");

        Teacher teacher = new Teacher();
        teacher.setName("Adam");
        teacher.setSurname("Aniedam");
        teacher.setPesel(123456789);

        newClass.addStudent(newStudent);
        newClass.addTeacher(teacher);
        newSchool.addClass(newClass);

        Transaction transaction = session.beginTransaction();
        session.save(newSchool);
        transaction.commit();
    }

    private void addTeacher() {

    }

    private void printSchools() {
        Query<School> query = session.createQuery("from School", School.class);
        List<School> schools = query.list();

        System.out.println("### Schools");
        for (School school : schools) {
            System.out.println(school);
            System.out.println("    ### SchoolClasses");
            for (SchoolClass schoolClass : school.getClasses()) {
                System.out.println("    " + schoolClass);
                System.out.println("        ### Students");
                for (Student student : schoolClass.getStudents()) {
                    System.out.println("        " + student);
                }
            }
        }
    }

    private void printTeachers() {
        Query<Teacher> query = session.createQuery("from Teacher", Teacher.class);
        List<Teacher> teachers = query.list();

        System.out.println("### Teachers");
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
            Set<SchoolClass> schoolClasses = teacher.getClasses();
            if (schoolClasses.isEmpty()) {
                System.out.println("\tNo classes");
                continue;
            }
            System.out.println("\t### SchoolClasses");
            for (SchoolClass schoolClass : schoolClasses) {
                System.out.println("\t" + schoolClass);
            }
        }
    }

    private void printClasses() {
        Query<SchoolClass> query = session.createQuery("from SchoolClass", SchoolClass.class);
        List<SchoolClass> schoolClasses = query.list();

        System.out.println("### Classes");
        for (SchoolClass schoolClass : schoolClasses) {
            System.out.println(schoolClass);
            Set<Teacher> teachers = schoolClass.getTeachers();
            if (teachers.isEmpty()) {
                System.out.println("\tNo teachers");
                continue;
            }
            System.out.println("\t### Teachers");
            for (Teacher teacher : teachers) {
                System.out.println("\t" + teacher);
            }
        }
    }
}
