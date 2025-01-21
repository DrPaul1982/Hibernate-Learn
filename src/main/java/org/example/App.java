package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class App {
    public static void main(String[] args) {
        Map<String, String> jpaConfig = new HashMap<>();
        jpaConfig.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
        jpaConfig.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("hibernate-learn-unit", (java.util.Map) jpaConfig);

        try (entityManagerFactory;
             EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            StudentDAO studentDAO = new StudentDAO(entityManager);

            Student student = new Student();
            student.setEmail("jane.doe3@test.com");
            student.setFirstName("Jane");
            student.setLastName("Doe");

            studentDAO.save(student);

            Homework homework = new Homework();
            homework.setDeadline(LocalDate.now().plusDays(5));
            homework.setDescription("Math Homework");

            Homework homework1 = new Homework();
            homework1.setDeadline(LocalDate.now().plusDays(2));
            homework1.setDescription("Read a 5 paragraph from 'Astronomy' book");

            student.addHomework(homework);
            student.addHomework(homework1);

            studentDAO.update(student);

            student.getHomeworks().forEach(hw -> {
                if ("Math Homework".equals(hw.getDescription())) {
                    hw.setMark(90);
                } else if ("Read a 5 paragraph from 'Astronomy' book".equals(hw.getDescription())) {
                    hw.setMark(95);
                }
            });

            studentDAO.update(student);

            System.out.println("DB_USER: " + System.getenv("DB_USER"));
            System.out.println("DB_PASSWORD: " + System.getenv("DB_PASSWORD"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
