package tehotelmanagmentsystem.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tehotelmanagmentsystem.connector.DBManager;
import tehotelmanagmentsystem.exceptions.NoSuchIdTypeException;
import tehotelmanagmentsystem.model.pojo.Document;

/**
 *
 * @author igor
 */

public class DocumentDAO implements DAO<Document>{
    private static DocumentDAO instance = null;
    private final DBManager manager;
    private final Connection con;
    private PreparedStatement cmd;
    private ResultSet rs;
    public static DocumentDAO getInstance() throws ClassNotFoundException,
            SQLException, IOException{
        if(instance == null){
            instance = new DocumentDAO();
        }
        return instance;
    }
    private DocumentDAO() throws ClassNotFoundException, SQLException, IOException {
        this.manager = DBManager.getInstance();
        con = manager.getConnection();
    }
    
    @Override
    public void insert(Document t) throws SQLException {
        manager.executeUpdate("isert into doc_types values(default," +
              t.getName() +");");
    }

    @Override
    public void update(Document doc) throws SQLException {
        manager.executeUpdate("update doc_types set name = " +
              doc.getName() +" where id = " + doc.getId() + ";");
    }

    @Override
    public void delete(int id) throws SQLException {
        manager.executeUpdate("delete from doc_types where id = " + id);
    }

    @Override
    public List<Document> getCompleteList() throws SQLException {
        List<Document> list = new ArrayList<>();
        rs = manager.executeSelect("select * from doc_types;");
        while(rs.next()){
            Document doc_type = new Document();
            doc_type.setId(rs.getInt(1));
            doc_type.setName(rs.getString(2));
            list.add(doc_type);
        }
        return list;
    }

    @Override
    public Document get(int id) throws Exception {
        rs = manager.executeSelect("select * from doc_types where id = " + id + ";");
        if(rs.next()){
            return getIDTypeFromResultSet(rs);
        } else {
            throw new NoSuchIdTypeException("There is no element with id = " +
                    id + " in the database!");
        }
    }
    
    private Document getIDTypeFromResultSet(ResultSet rs) throws SQLException{
        Document doc_type = new Document();
            doc_type.setId(rs.getInt(1));
            doc_type.setName(rs.getString(2));
            return doc_type;
    }
}
