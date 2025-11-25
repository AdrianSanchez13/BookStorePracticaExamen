package org.example.persistence.dao.jpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.domain.exception.ResourceNotFoundException;
import org.example.persistence.dao.jpa.BookJpaDao;
import org.example.persistence.dao.jpa.entity.BookJpaEntity;

import java.util.List;
import java.util.Optional;

@Transactional
public class BookJpaDaoImpl implements BookJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BookJpaEntity> findByIsbn(String isbn) {
        String jpql = "SELECT b FROM BookJpaEntity b WHERE b.isbn = :isbn";
        try {
            BookJpaEntity bookjpaentity = entityManager.createQuery(jpql, BookJpaEntity.class)
                    .setParameter("isbn", isbn)
                    .getSingleResult();
            return Optional.of(bookjpaentity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByIsbn(String isbn) {
        String jpql = "DELETE FROM BookJpaEntity b WHERE b.isbn = :isbn";
        entityManager.createQuery(jpql)
                .setParameter("isbn", isbn)
                .executeUpdate();
    }

    @Override
    public List findAll(int page, int size) {
        String jpql = "SELECT b FROM BookJpaEntity b";
        return entityManager.createQuery(jpql, BookJpaEntity.class)
                .setFirstResult((page -1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<BookJpaEntity> findById(Long id) {
        String jpql = "SELECT b FROM BookJpaEntity b WHERE b.id = :id";
        try {
            BookJpaEntity bookjpaentity = entityManager.createQuery(jpql, BookJpaEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(bookjpaentity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public BookJpaEntity insert(BookJpaEntity jpaEntity) {
        entityManager.persist(jpaEntity);
        return jpaEntity;
    }

    @Override
    public BookJpaEntity update(BookJpaEntity jpaEntity) {
        BookJpaEntity managed = entityManager.find(BookJpaEntity.class, jpaEntity.getId());
        if(managed == null) {
            throw new ResourceNotFoundException("Book with id " + jpaEntity.getId() + " not found");
        }
        entityManager.flush();
        return entityManager.merge(jpaEntity);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(BookJpaEntity.class, id));


    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(b) FROM BookJpaEntity b", Long.class)
                .getSingleResult();
    }
}
