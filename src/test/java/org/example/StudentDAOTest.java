package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

class StudentDAOTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private StudentDAO studentDAO;

    @BeforeAll
    static void setUpEnvironment() {;
        System.setProperty("DB_USER", System.getenv("DB_USER"));
        System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));
        System.setProperty("DB_TEST_DATABASE", System.getenv("DB_TEST_DATABASE"));
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-learn-unit-test");
    }

    @BeforeEach
    public void start() {
        entityManager = entityManagerFactory.createEntityManager();

        studentDAO = new StudentDAO(entityManager);
    }

    @AfterEach
    public void end() {
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("DELETE FROM homework").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM student").executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void save() {
        Student student = new Student();
        student.setEmail("save@test.com");
        student.setFirstName("Save");
        student.setLastName("Test");

        studentDAO.save(student);

        Student actual = studentDAO.findByEmail("save@test.com");

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(student.getEmail(), actual.getEmail());
    }

    @Test
    void findByID() {
        Student student = new Student();
        student.setEmail("findByID@test.com");
        student.setFirstName("findByID");
        student.setLastName("Test");

        studentDAO.save(student);

        Student actual = studentDAO.findByID(student.getId());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(student.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail() {
        Student student = new Student();
        student.setEmail("findByEmail@test.com");
        student.setFirstName("findByEmail");
        student.setLastName("Test");

        studentDAO.save(student);

        Student actual = studentDAO.findByEmail(student.getEmail());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(student.getEmail(), actual.getEmail());
    }


    @Test
    void findAll() {
        Student student1 = new Student();
        student1.setEmail("findAll1@test.com");
        student1.setFirstName("findAll");
        student1.setLastName("Test");
        studentDAO.save(student1);

        Student student2 = new Student();
        student2.setEmail("findAll2@test.com");
        student2.setFirstName("findAll2");
        student2.setLastName("Test2");
        studentDAO.save(student2);

        List<Student> students = studentDAO.findAll();

        Assertions.assertNotNull(students);
        Assertions.assertTrue(students.size() > 1);
    }


    @Test
    void update() {
        Student student = new Student();
        student.setEmail("update@test.com");
        student.setFirstName("Update");
        student.setLastName("Test");

        studentDAO.save(student);

        student.setEmail("NewUpdate@test.com");
        studentDAO.update(student);

        Student updatedStudent = studentDAO.findByEmail("NewUpdate@test.com");

        Assertions.assertNotNull(updatedStudent);
        Assertions.assertEquals(student.getEmail(), updatedStudent.getEmail());
    }


    @Test
    void deleteById() {
        Student student = new Student();
        student.setEmail("deleteById@test.com");
        student.setFirstName("Delete");
        student.setLastName("Test");

        studentDAO.save(student);

        Student savedStudent = studentDAO.findByEmail("deleteById@test.com");
        Assertions.assertNotNull(savedStudent);

        studentDAO.deleteById(savedStudent.getId());

        Student deletedStudent = studentDAO.findByID(savedStudent.getId());
        Assertions.assertNull(deletedStudent);
    }
}