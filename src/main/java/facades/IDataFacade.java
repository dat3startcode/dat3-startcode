package facades;
import java.util.List;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IDataFacade<T> {
    T create(T t);
    T getById(int id) throws EntityNotFoundException;
    List<T> getAll();
    T update(T t) throws EntityNotFoundException;
    T delete(int id) throws EntityNotFoundException;
}
