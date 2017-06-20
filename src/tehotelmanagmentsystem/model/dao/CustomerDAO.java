package tehotelmanagmentsystem.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tehotelmanagmentsystem.connector.DBManager;
import tehotelmanagmentsystem.exceptions.NoSuchCustomerException;
import tehotelmanagmentsystem.model.pojo.Customer;
import tehotelmanagmentsystem.model.pojo.Gender;
import tehotelmanagmentsystem.model.Meal;

/**
 *
 * @author igor
 */
public class CustomerDAO implements DAO<Customer>{
    private static CustomerDAO instance = null;
    private final DBManager manager;
    private final Connection con;
    private PreparedStatement cmd;
    private ResultSet rs;
    private String lastSQL = "select * from customers;";
    public static CustomerDAO getInstance() throws ClassNotFoundException,
            SQLException, IOException{
        if(instance == null){
            instance = new CustomerDAO();
        }
        return instance;
    }
    
    private CustomerDAO() throws ClassNotFoundException, IOException,
           SQLException {
        manager = DBManager.getInstance();
        con = manager.getConnection();
    }

    @Override
    public void insert(Customer c) throws SQLException {
        cmd = con.prepareStatement("insert into customers values("
                + "default,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
        cmd.setString(1, c.getFirstName());
        cmd.setString(2, c.getSurname());
        cmd.setString(3, c.getAddress());
        cmd.setString(4, c.getPostCode());
        cmd.setString(5, c.getMobile());
        cmd.setString(6, c.getEmail());
        cmd.setString(7, c.getNationality());
        java.sql.Date dob = new java.sql.Date(c.getDateOfBirth().getTime());
        cmd.setDate(8, dob);
        cmd.setInt(9, c.getId_doc());
        cmd.setString(10, c.getGender().toString());
        java.sql.Date checkInDate = new java.sql.Date(c.getCheckInDate().getTime());
        cmd.setDate(11, checkInDate);
        java.sql.Date checkOutDate = new java.sql.Date(c.getCheckOutDate().getTime());
        cmd.setDate(12, checkOutDate);
        cmd.setString(13, c.getMeal().toString());
        cmd.setInt(14, c.getRoom());
        cmd.executeUpdate();
        cmd.close();
    }

    @Override
    public void update(Customer c) throws SQLException {
        cmd = con.prepareStatement("update customers set first_name = ?,"
            + "surname = ?, address = ?, post_code = ?, mobile = ?,"
            + "email = ?, nationality = ?, date_of_birth = ?,"
            + "id_doc = ?, gender = ?, check_in_date = ?,"
            + "check_out_date = ?, meal = ?, room = ? where id_customer = ?;");
        cmd.setString(1, c.getFirstName());
        cmd.setString(2, c.getSurname());
        cmd.setString(3, c.getAddress());
        cmd.setString(4, c.getPostCode());
        cmd.setString(5, c.getMobile());
        cmd.setString(6, c.getEmail());
        cmd.setString(7, c.getNationality());
        cmd.setDate(8, c.getDateOfBirth());
        cmd.setInt(9, c.getId_doc());
        cmd.setString(10, c.getGender().toString());
        cmd.setDate(11, c.getCheckInDate());
        cmd.setDate(12, c.getCheckOutDate());
        cmd.setString(13, c.getMeal().toString());
        cmd.setInt(14, c.getRoom());
        cmd.setInt(15, c.getId());
        cmd.executeUpdate();
        cmd.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        manager.executeUpdate("delete from customers where id_customer = " + id + ";");
        manager.disconnect();
    }

    @Override
    public List<Customer> getCompleteList() throws SQLException {
        List<Customer> list = new ArrayList<>();
        rs = manager.executeSelect("select * from customers;");
        while(rs.next()){
            list.add(getCustomerFromResultSet(rs));
        }
        lastSQL = "select * from customers;";
        return list;
    }

    @Override
    public Customer get(int id) throws SQLException, NoSuchCustomerException {
        rs = manager.executeSelect("select * from customers where id = " + id + ";");
        if(rs.next()){
            return getCustomerFromResultSet(rs);
        } else {
            throw new NoSuchCustomerException("There is no elements with id = "+ id +
                " in the database!");
        }
    }
    
    public List<Customer> search(String regExp, boolean considerAge, int ageMin,
            int ageMax) throws SQLException{
        List<Customer> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder("select * from customers where"
                + " (first_name like '%"+ regExp +"%' or surname like '%" +
                regExp+ "%') ");
        if(considerAge){
            sb.append(" and (calc_customer_age(date_of_birth) >= ").append(ageMin).append(" and calc_customer_age(date_of_birth) <= ").append(ageMax).append(");");
        } else {
            sb.append(";");
        }
        cmd = con.prepareStatement(sb.toString());
        lastSQL = sb.toString();
        System.out.println("SQL = " + sb.toString());
        rs = cmd.executeQuery();
        while(rs.next()){
            list.add(getCustomerFromResultSet(rs));
        }
        return list;
    }
    
    public List<Customer> repeatLastSearch() throws SQLException{
        List<Customer> list = new ArrayList<>();
        cmd = con.prepareStatement(lastSQL);
        rs = cmd.executeQuery();
        while(rs.next()){
            list.add(getCustomerFromResultSet(rs));
        }
        return list;
    }
    
    private Customer getCustomerFromResultSet(ResultSet rs) throws SQLException{
        Customer c = new Customer();
        c.setId(rs.getInt(1));
        c.setFirstName(rs.getString(2));
        c.setSurname(rs.getString(3));
        c.setAddress(rs.getString(4));
        c.setPostCode(rs.getString(5));
        c.setMobile(rs.getString(6));
        c.setEmail(rs.getString(7));
        c.setNationality(rs.getString(8));
        c.setDateOfBirth(rs.getDate(9));
        c.setId_doc(rs.getInt(10));
        c.setGender(Gender.valueOf(rs.getString(11)));
        c.setCheckInDate(rs.getDate(12));
        c.setCheckOutDate(rs.getDate(13));
        c.setMeal(Meal.valueOf(rs.getString(14)));
        c.setRoom(rs.getInt(15));
        return c;
    }
    
}
