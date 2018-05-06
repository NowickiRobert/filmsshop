package net.stawrul.services;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.UUID;
import java.util.function.Function;

/**
 * Klasa bazowa dla serwisów biznesowych realizujących logikę operacji na obiektach encyjnych.
 * <p>
 * Dostarcza podstawowe metody potrzebne w każdym serwisie biznesowym:
 * find - do wyszukiwania obiektów encyjnych w oparciu o id,
 * save - do zapisywania obiektów encyjnych (może być używana zarówno do zapisywania nowych elementów w bazie danych
 * jak i aktualizowania istniejących).
 *
 * @param <T> typ obiektów encyjnych, na których operuje serwis biznesowy
 */
abstract public class EntityService<T> {
    final EntityManager em;
    private final Class<T> entityClass;
    private final Function<T, Object> idSupplier;

    /**
     * @param em instancja klasy EntityManager
     * @param entityClass klasa encyjna, na której mają być realizowane operacje
     * @param idSupplier referencja na metodę umożliwiającą pobranie identyfikatora obiektów encyjnych klasy entityClass
     */
    public EntityService(EntityManager em, Class<T> entityClass, Function<T, Object> idSupplier) {
        this.em = em;
        this.entityClass = entityClass;
        this.idSupplier = idSupplier;
    }

    /**
     * Zapisanie stanu obiektu encyjnego w bazie danych.
     *
     * Może być używana zarówno do zapisywania nowych elementów w bazie danych jak i aktualizowania istniejących.
     *
     * @param entity
     */
    @Transactional
    public void save(T entity) {
        if (em.find(entityClass, idSupplier.apply(entity)) == null) {
            //Jeśli identyfikator nie występuje w bazie danych, obiekt encyjny jest w stanie new
            em.persist(entity);
        } else {
            //Jeśli identyfikator występuje w bazie danych, należy przeprowadzić obiekt do stanu managed, aby
            //wprowadzone w nim modyfikacje zostały zarejestrowane w ramach kontekstu trwałości (ang. persistence
            //context). Zmiany zostaną zapisane w bazie danych, gdy bieżąca transakcja zostanie zatwierdzona.
            em.merge(entity);
        }
    }

    /**
     * Wyszukiwanie obiektów encyjnych na podstawie identyfikatora.
     *
     * @param id identyfikator obiektu encyjnego
     * @return odnaleziony obiekt encyjny lub null jeśli nie znaleziono żadnego pasującego do parametru id
     */
    public T find(UUID id) {
        return em.find(entityClass, id);
    }
}
