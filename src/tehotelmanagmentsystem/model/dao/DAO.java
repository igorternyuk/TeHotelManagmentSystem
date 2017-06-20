package tehotelmanagmentsystem.model.dao;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author igor
 * @param <T> type of the database object
 */
public interface DAO<T> {
    void insert(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;
    T get(int id) throws Exception;
    List<T> getCompleteList()  throws SQLException;
}
