package tehotelmanagmentsystem.model.tablemodels;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import tehotelmanagmentsystem.model.dao.DocumentDAO;
import tehotelmanagmentsystem.model.dao.RoomDAO;
import tehotelmanagmentsystem.exceptions.NoSuchRoomException;
import tehotelmanagmentsystem.model.pojo.Customer;
import tehotelmanagmentsystem.model.pojo.Document;
import tehotelmanagmentsystem.model.Room;

/**
 *
 * @author igor
 */
public class TModelCustomer implements TableModel{
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_FIRST_NAME = 1;
    private static final int COLUMN_SURNAME = 2;
    private static final int COLUMN_ADDRESS = 3;
    private static final int COLUMN_POST_CODE = 4;
    private static final int COLUMN_MOBILE = 5;
    private static final int COLUMN_EMAIL = 6;
    private static final int COLUMN_NATIONALITY = 7;
    private static final int COLUMN_DATE_OF_BIRTH = 8;
    private static final int COLUMN_ID_TYPE = 9;
    private static final int COLUMN_GENDER = 10;
    private static final int COLUMN_CHECK_IN_DATE = 11;
    private static final int COLUMN_CHECK_OUT_DATE = 12;
    private static final int COLUMN_MEAL = 13;
    private static final int COLUMN_ROOM = 14;
    private final String[] titles = {
        "Customer_Ref",
        "First_name",
        "Surname",
        "Address",
        "Post_code",
        "Mobile",
        "E-mail",
        "Nationality",
        "Date_of_birth",
        "ID_type",
        "Gender",
        "Check_in_date",
        "Check_out_date",
        "Meal",
        "Room type"
    };
    private List<Customer> list;
    private DocumentDAO docDAO;
    private RoomDAO roomDAO;
    
    public TModelCustomer(List<Customer> list) throws ClassNotFoundException,
            SQLException, IOException {
        this.list = list;
        docDAO = DocumentDAO.getInstance();
        roomDAO = RoomDAO.getInstance();
    }
    
    public Customer getCustomer(int row){
        return list.get(row);
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return titles.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return titles[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(list.isEmpty()){
            return Object.class;
        } else {
            return getValueAt(0, columnIndex).getClass();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer c = list.get(rowIndex);
        Object result = null;
        switch(columnIndex){
            case COLUMN_ID :
                result = c.getId();
                break;
            case COLUMN_FIRST_NAME :
                result = c.getFirstName();
                break;
            case COLUMN_SURNAME :
                result = c.getSurname();
                break;
            case COLUMN_ADDRESS :
                result = c.getAddress();
                break;
            case COLUMN_POST_CODE :
                result = c.getPostCode();
                break;
            case COLUMN_MOBILE :
                result = c.getMobile();
                break;
            case COLUMN_EMAIL :
                result = c.getEmail();
                break;
            case COLUMN_NATIONALITY :
                result = c.getNationality();
                break;
            case COLUMN_DATE_OF_BIRTH :
                result = c.getDateOfBirth().toString();
                break;
            case COLUMN_ID_TYPE :
                Document id_type;
                try {
                    id_type = docDAO.get(c.getId_doc());
                    result = id_type.getName();
                } catch (Exception ex) {
                    Logger.getLogger(TModelCustomer.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case COLUMN_GENDER :
                result = c.getGender().toString();
                break;
            case COLUMN_CHECK_IN_DATE :
                result = c.getCheckInDate().toString();
                break;
            case COLUMN_CHECK_OUT_DATE :
                result = c.getCheckOutDate().toString();
                break;
            case COLUMN_MEAL :
                result = c.getMeal().toString();
                break;
            case COLUMN_ROOM :
                Room room;
                try {
                    room = roomDAO.get(c.getRoom());
                    result = room.toString();
                } catch (SQLException ex) {
                    Logger.getLogger(TModelCustomer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchRoomException ex) {
                    Logger.getLogger(TModelCustomer.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default :
                throw new IllegalArgumentException("Invalid column index");
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
    
}
