package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class StudentDAO implements GenericDAO<Student, Long> {

    private final EntityManager entityManager;

    public StudentDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void save(Student entity) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            entityManager.persist(entity);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            throw new RuntimeException("Error while saving student: " + e.getMessage(), e);
        }
    }



    @Override
    public Student findByID(Long id) {
            return entityManager.find(Student.class, id);
        }


    @Override
    public Student findByEmail(String email) {
            return entityManager
                    .createQuery("SELECT s FROM Student s WHERE s.email = :email", Student.class)
                    .setParameter("email", email)
                    .getSingleResult();
    }

    @Override
    public List<Student> findAll() {
            return entityManager
                    .createQuery("SELECT s FROM Student s", Student.class)
                    .getResultList();
    }


    @Override
    public Student update(Student entity) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();

            Student updatedStudent = entityManager.merge(entity);

            et.commit();
            return updatedStudent;
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            throw new RuntimeException("Student's update error: " + e.getMessage(), e);
        }
    }



    @Override
    public boolean deleteById(Long id) {
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            if (student != null) {
                entityManager.remove(student);
                entityManager.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error while deleting student: " + e.getMessage());
        }
    }
}
