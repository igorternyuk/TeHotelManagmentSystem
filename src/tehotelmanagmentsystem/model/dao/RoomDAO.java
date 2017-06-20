package tehotelmanagmentsystem.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tehotelmanagmentsystem.connector.DBManager;
import tehotelmanagmentsystem.exceptions.NoSuchRoomException;
import tehotelmanagmentsystem.model.Room;
import tehotelmanagmentsystem.model.RoomType;

/**
 *
 * @author igor
 */
public class RoomDAO implements DAO<Room>{
    private static RoomDAO instance = null;
    private final DBManager manager;
    private final Connection con;
    private PreparedStatement cmd;
    private ResultSet rs;
    public static RoomDAO getInstance() throws ClassNotFoundException,
            IOException, SQLException{
        if(instance == null){
            instance = new RoomDAO();
        }
        return instance;
    }
    private RoomDAO() throws ClassNotFoundException, IOException, SQLException{
        manager = DBManager.getInstance();
        con = manager.getConnection();
    }
    @Override
    public void insert(Room r) throws SQLException {
        cmd = con.prepareStatement("insert into rooms values(default,?,?);");
        cmd.setString(1, r.getType().toString());
        cmd.setBoolean(2, r.isIsFree());
        cmd.executeUpdate();
        cmd.close();
    }

    @Override
    public void update(Room r) throws SQLException {
        cmd = con.prepareStatement("update rooms set room_type = ?, isFree = ? where id = ?;");
        cmd.setString(1, r.getType().toString());
        cmd.setBoolean(2, r.isIsFree());
        cmd.setInt(3, r.getId());
        cmd.executeUpdate();
        cmd.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        manager.executeUpdate("delete from rooms where id = " + id + ";");
        manager.disconnect();
    }

    @Override
    public List<Room> getCompleteList() throws SQLException {
        List<Room> list = new ArrayList<>();
        rs = manager.executeSelect("select * from rooms;");
        while(rs.next()){
            list.add(getRoomFromResultSet(rs));
        }
        return list;
    }

    @Override
    public Room get(int id) throws SQLException, NoSuchRoomException {
        rs = manager.executeSelect("select * from rooms where id = " + id + ";");
        if(rs.next()){
            return getRoomFromResultSet(rs);
        } else {
            throw new NoSuchRoomException("There is no room with id = " + id +
                        " in the database!");
        }
    }
    
    private Room getRoomFromResultSet(ResultSet rs) throws SQLException{
        Room r = new Room();
        r.setId(rs.getInt(1));
        r.setType(RoomType.valueOf(rs.getString(2)));
        r.setIsFree(rs.getBoolean(3));
        return r;
    }
    
}
