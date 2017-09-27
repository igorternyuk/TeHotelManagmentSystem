package tehotelmanagmentsystem.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import tehotelmanagmentsystem.exceptions.NoSuchRoomException;
import tehotelmanagmentsystem.gui.inputvalidators.KeyListenerType;
import tehotelmanagmentsystem.gui.inputvalidators.TextFieldInputValidator;
import tehotelmanagmentsystem.model.dao.CustomerDAO;
import tehotelmanagmentsystem.model.dao.DocumentDAO;
import tehotelmanagmentsystem.model.dao.RoomDAO;
import tehotelmanagmentsystem.model.pojo.Customer;
import tehotelmanagmentsystem.model.pojo.Gender;
import tehotelmanagmentsystem.model.pojo.Document;
import tehotelmanagmentsystem.model.Meal;
import tehotelmanagmentsystem.model.Room;
import tehotelmanagmentsystem.model.prices.PriceCalculator;
import tehotelmanagmentsystem.model.tablemodels.TModelCustomer;

/**
 *
 * @author igor
 */
public class MainWindow extends javax.swing.JFrame {
    //private DBManager dbManager;
    private final String TITLE_OF_PROGRAM = "Hotel manager";
    private final int ROW_HEIGHT = 30;
    private CustomerDAO customerDAO;
    private DocumentDAO docTypeDAO;
    private RoomDAO roomDAO;
    private PriceCalculator priceCalculator;
    public MainWindow() {
    try {
        initComponents();
        customerDAO = CustomerDAO.getInstance();
        docTypeDAO = DocumentDAO.getInstance();
        roomDAO = RoomDAO.getInstance();
        priceCalculator = PriceCalculator.getInstance();
        configureGUI();
        fillCombos();
        loadCompleteCustomerList();

    } catch (ClassNotFoundException | IOException ex) {
        showErrorMessage(ex);
        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        showSQLErrorMessage(ex);
        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    private void configureGUI(){
        this.setTitle(TITLE_OF_PROGRAM);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        tableCustomers.setRowHeight(ROW_HEIGHT);
        tableCustomers.setAutoCreateRowSorter(true);
        configureSpinners();
        txtCustomerRef.addKeyListener(new TextFieldInputValidator(
                KeyListenerType.INTEGER_VALUES_ONLY));
    }
    
    private void configureSpinners(){
        setSpinnerNumericValidator(spinnerDiscount, 0.0, 0.0, 1000000000.0, 1.0);
        setSpinnerNumericValidator(spinnerAgeMIN, 5, 5, 120, 1);
        setSpinnerNumericValidator(spinnerAgeMAX, 5, 5, 120, 1);
    }
    
    private void setSpinnerNumericValidator(JSpinner spinner, double val,
            double min, double max, double step){
        SpinnerNumberModel model = new SpinnerNumberModel(val, min, max, step);
        spinner.setModel(model);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().addKeyListener(
                new TextFieldInputValidator(KeyListenerType.FLOAT_VALUES_ONLY)
        );
    }
    
    private void setSpinnerNumericValidator(JSpinner spinner, int val,
            int min, int max, int step){
        SpinnerNumberModel model = new SpinnerNumberModel(val, min, max, step);
        spinner.setModel(model);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().addKeyListener(
                new TextFieldInputValidator(KeyListenerType.INTEGER_VALUES_ONLY)
        );
    }
    
    private void fillCombos() throws SQLException{
        comboGender.removeAllItems();
        comboGender.addItem(Gender.M.toString());
        comboGender.addItem(Gender.F.toString());
        
        comboMeal.removeAllItems();
        comboMeal.addItem(Meal.NO_MEAL);
        comboMeal.addItem(Meal.ONE_MEAL);
        comboMeal.addItem(Meal.TWO_MEALS);
        comboMeal.addItem(Meal.THREE_MEALS);
        
        comboDocument.removeAllItems();
        List<Document> listDocTypes = docTypeDAO.getCompleteList();
        for(Document doc_type : listDocTypes){
            comboDocument.addItem(doc_type);
        }
        
        comboRoomNumber.removeAllItems();
        List<Room> listRooms = roomDAO.getCompleteList();
        for(Room r : listRooms){
            comboRoomNumber.addItem(r);
        }
    }
    
    private void loadLastSearchResults() throws SQLException, ClassNotFoundException, IOException{
        tableCustomers.setModel(new TModelCustomer(customerDAO.repeatLastSearch()));
    }
    
    private void loadCompleteCustomerList() throws
            SQLException, ClassNotFoundException, IOException{
        List<Customer> list = customerDAO.getCompleteList();
        TModelCustomer model = new TModelCustomer(list);
        tableCustomers.setModel(model);
    }
    
    private boolean checkInputFields(){
        //txtCustomerRef.getText().isEmpty() ||
        return !(txtFirstName.getText().isEmpty() ||
        txtSurname.getText().isEmpty() ||
        txtAdress.getText().isEmpty() ||
        txtPostcode.getText().isEmpty() ||
        txtMobile.getText().isEmpty() ||
        txtEmail.getText().isEmpty() ||
        txtNationality.getText().isEmpty());
    }
    
    private Customer getCustomerDataFromInputFields(){
        Customer c = new Customer();
        if(!txtCustomerRef.getText().isEmpty()){
            c.setId(Integer.parseInt(txtCustomerRef.getText()));
        }
        c.setFirstName(txtFirstName.getText());
        c.setSurname(txtSurname.getText());
        c.setAddress(txtAdress.getText());
        c.setPostCode(txtPostcode.getText());
        c.setMobile(txtMobile.getText());
        c.setEmail(txtEmail.getText());
        c.setNationality(txtNationality.getText());
        c.setDateOfBirth(new java.sql.Date(txtDateOfBirth.getDate().getTime()));
        if(comboDocument.getItemCount() > 0){
            Document doc = (Document)comboDocument.getSelectedItem();
            c.setId_doc(doc.getId());
        }
        c.setGender(Gender.valueOf((String)comboGender.getSelectedItem()));
        c.setCheckInDate(new java.sql.Date(txtCheckInDate.getDate().getTime()));
        c.setCheckOutDate(new java.sql.Date(txtCheckOutDate.getDate().getTime()));
        Meal meal = (Meal)comboMeal.getSelectedItem();
        c.setMeal(meal);
        if(comboRoomNumber.getItemCount() > 0){
            Room room = (Room)(comboRoomNumber.getSelectedItem());
            c.setRoom(room.getId());
        }
        return c;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        txtCheckOutDate = new com.toedter.calendar.JDateChooser();
        txtCheckInDate = new com.toedter.calendar.JDateChooser();
        txtDateOfBirth = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        txtCustomerRef = new javax.swing.JTextField();
        txtFirstName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSurname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAdress = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPostcode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMobile = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNationality = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCustomers = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        comboDocument = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        comboMeal = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        comboGender = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        comboRoomNumber = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        txtTax = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnExit = new javax.swing.JButton();
        btnTotal = new javax.swing.JButton();
        txtUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        rbtnPercentageDiscount = new javax.swing.JRadioButton();
        rbtnDiscountAbsolute = new javax.swing.JRadioButton();
        btnReset = new javax.swing.JButton();
        spinnerDiscount = new javax.swing.JSpinner();
        btnInsert = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JToggleButton();
        btnPrevious = new javax.swing.JToggleButton();
        btnNext = new javax.swing.JToggleButton();
        txtSearchCustomer = new javax.swing.JTextField();
        checkBoxConsiderAge = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        spinnerAgeMIN = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        spinnerAgeMAX = new javax.swing.JSpinner();
        btnSearchCustomer = new javax.swing.JButton();
        btnLast = new javax.swing.JToggleButton();
        txtEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCheckOutDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtCheckOutDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 369, 145, 25));

        txtCheckInDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtCheckInDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 332, 145, 25));

        txtDateOfBirth.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtDateOfBirth, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 236, 145, 25));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Customer:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 14, -1, -1));

        txtCustomerRef.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtCustomerRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 12, 145, -1));

        txtFirstName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 39, 145, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("First name:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 41, -1, -1));

        txtSurname.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtSurname, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 66, 145, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Surname:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 66, -1, -1));

        txtAdress.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtAdress, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 93, 145, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Adress:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 95, -1, -1));

        txtPostcode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtPostcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 122, 145, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Postcode:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 124, -1, -1));

        txtMobile.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 149, 145, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Customer mobile:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 151, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("E-mail");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 178, -1, -1));

        txtNationality.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtNationality, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 210, 150, -1));

        tableCustomers.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableCustomersMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableCustomers);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 12, 880, 330));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Nationality:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 210, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Date of birth:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 236, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Document:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 267, -1, -1));

        comboDocument.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(comboDocument, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 267, 145, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Gender:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 308, -1, -1));

        comboMeal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(comboMeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 400, 145, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Check in date:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 337, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Check in out:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 377, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Meal:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 405, -1, -1));

        comboGender.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(comboGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 299, 145, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setText("Room number:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 438, -1, -1));

        comboRoomNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(comboRoomNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 432, 145, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        txtTax.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Tax:");

        txtSubtotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Subtotal:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Total:");

        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnExit.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnExit.setText("EXIT");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnTotal.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnTotal.setText("TOTAL");
        btnTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalActionPerformed(evt);
            }
        });

        txtUpdate.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txtUpdate.setText("UPDATE");
        txtUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUpdateActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnPercentageDiscount);
        rbtnPercentageDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtnPercentageDiscount.setSelected(true);
        rbtnPercentageDiscount.setText("percentage");
        rbtnPercentageDiscount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbtnPercentageDiscountMouseReleased(evt);
            }
        });

        buttonGroup1.add(rbtnDiscountAbsolute);
        rbtnDiscountAbsolute.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rbtnDiscountAbsolute.setText("absolute");
        rbtnDiscountAbsolute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rbtnDiscountAbsoluteMouseReleased(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnReset.setText("RESET");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        spinnerDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnInsert.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnInsert.setText("INSERT");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        jLabel15.setText("Discount:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTax, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(rbtnDiscountAbsolute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbtnPercentageDiscount)
                        .addComponent(spinnerDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addComponent(btnTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUpdate)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtUpdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnInsert, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbtnPercentageDiscount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbtnDiscountAbsolute)
                                .addGap(5, 5, 5)
                                .addComponent(spinnerDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(txtTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))))
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 470, 1170, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnFirst.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFirst.setText("First <<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevious.setText("Previous<");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNext.setText("Next>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        txtSearchCustomer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        checkBoxConsiderAge.setText("consider age");

        jLabel17.setText("MIN:");

        spinnerAgeMIN.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel21.setText("MAX");

        spinnerAgeMAX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnSearchCustomer.setText("Search customer");
        btnSearchCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCustomerActionPerformed(evt);
            }
        });

        btnLast.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLast.setText(">> Last");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(265, 265, 265)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrevious)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(txtSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxConsiderAge)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerAgeMIN, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerAgeMAX, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFirst)
                    .addComponent(btnPrevious)
                    .addComponent(btnNext)
                    .addComponent(btnLast))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(spinnerAgeMAX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearchCustomer))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(spinnerAgeMIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxConsiderAge)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 354, 880, -1));

        txtEmail.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        getContentPane().add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 176, 145, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalActionPerformed
        if(checkInputFields() && !txtCustomerRef.getText().isEmpty()){
            Room room = (Room)comboRoomNumber.getSelectedItem();
            Meal meal = (Meal)comboMeal.getSelectedItem();
            double subtotal = priceCalculator.calcSubTotal(
                    txtCheckInDate.getDate(),
                    txtCheckOutDate.getDate(),
                    room.getType(),
                    meal
            );
            double tax = priceCalculator.getTax();
            double total;
            double discount = (double)spinnerDiscount.getValue();
            if(rbtnDiscountAbsolute.isSelected()){
                total = priceCalculator.calcTotal(
                txtCheckInDate.getDate(),
                txtCheckOutDate.getDate(),
                room.getType(), 
                meal,
                discount
                );
            } else {
                total = priceCalculator.calcTotalWithPercentageDiscount(
                txtCheckInDate.getDate(),
                txtCheckOutDate.getDate(),
                room.getType(), 
                meal,
                discount
                );
            }
            txtSubtotal.setText(String.valueOf(subtotal));
            txtTax.setText(String.valueOf(tax));
            txtTotal.setText(String.valueOf(total));
        } else {
            JOptionPane.showMessageDialog(
                null,
                "There are no selected users",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnTotalActionPerformed

    private void txtUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUpdateActionPerformed
        if(checkInputFields() && !txtCustomerRef.getText().isEmpty()){
            try {
                Customer c = getCustomerDataFromInputFields();
                customerDAO.update(c);
                loadCompleteCustomerList();
                JOptionPane.showMessageDialog(
                    null,
                    "Customer with id = " + c.getId() + " was successfuly updated",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException | IOException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if(!txtCustomerRef.getText().isEmpty()){
            int id = Integer.parseInt(txtCustomerRef.getText());
            try {
                int res = JOptionPane.showConfirmDialog(
                    null,
                    "Do you really want delete selected customer?",
                    "Delete conformation",
                    JOptionPane.YES_NO_OPTION
                );
                if(res == JOptionPane.YES_OPTION){
                    customerDAO.delete(id);
                    loadLastSearchResults();
                    JOptionPane.showMessageDialog(
                    null,
                    "Customer with id = " + id + " was successfuly deleted",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                }

            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException | IOException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                "There are no selected users",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tableCustomersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCustomersMouseReleased
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            try {
                showSelectedItem();
            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchRoomException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_tableCustomersMouseReleased

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        try {
            clearForm();
            loadCompleteCustomerList();
            rbtnDiscountAbsolute.setSelected(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                "Database error",
                JOptionPane.INFORMATION_MESSAGE
            );
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException | IOException ex) {
            showErrorMessage(ex);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        if(checkInputFields()){
            try {
                Customer c = getCustomerDataFromInputFields();
                customerDAO.insert(c);
                loadLastSearchResults();
                JOptionPane.showMessageDialog(
                    null,
                    "Customer was successfuly added to the database",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException | IOException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                "One or more input fields are empty",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnSearchCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCustomerActionPerformed
        String regExp = txtSearchCustomer.getText();
        boolean considerAge = checkBoxConsiderAge.isSelected();
        int ageMin = (int)spinnerAgeMIN.getValue();
        int ageMax = (int)spinnerAgeMAX.getValue();
        if(ageMax < ageMin){
            ageMax = ageMin;
        }
        spinnerAgeMAX.setValue(ageMax);
        try {
            List<Customer> list = customerDAO.search(regExp, considerAge,
                    ageMin, ageMax);
            tableCustomers.setModel(new TModelCustomer(list));
            if(list.isEmpty()){
               JOptionPane.showMessageDialog(
                null,
                "No customers found",
                "Search results",
                JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                null,
                list.size() + " customers found",
                "Search results",
                JOptionPane.INFORMATION_MESSAGE
                ); 
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null,
                    ex);
        } catch (ClassNotFoundException | IOException ex) {
            showErrorMessage(ex);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }//GEN-LAST:event_btnSearchCustomerActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int res = JOptionPane.showConfirmDialog(null, "Do you really want to"
                + " close appliation",
                "Close conformation", JOptionPane.YES_NO_OPTION);
        if(res == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void rbtnPercentageDiscountMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtnPercentageDiscountMouseReleased
        setSpinnerNumericValidator(spinnerDiscount, 0.0, 0.0, 100.0, 1.0);
    }//GEN-LAST:event_rbtnPercentageDiscountMouseReleased

    private void rbtnDiscountAbsoluteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtnDiscountAbsoluteMouseReleased
        setSpinnerNumericValidator(spinnerDiscount, 0.0, 0.0, 1000000000.0, 1.0);
    }//GEN-LAST:event_rbtnDiscountAbsoluteMouseReleased

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        if(tableCustomers.getRowCount() > 0){
            tableCustomers.setRowSelectionInterval(0, 0);
            try {
                showSelectedItem();
            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchRoomException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        int total = tableCustomers.getRowCount();
        if(total > 0){
            int row = tableCustomers.getSelectedRow();
            --row;
            row = (row < 0) ? total - 1 : row;
            tableCustomers.setRowSelectionInterval(row, row);
            try {
                showSelectedItem();
            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchRoomException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        int total = tableCustomers.getRowCount();
        if(total > 0){
            int row = tableCustomers.getSelectedRow();
            ++row;
            row = (row >= total) ? 0 : row;
            tableCustomers.setRowSelectionInterval(row, row);
            try {
                showSelectedItem();
            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchRoomException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        int total = tableCustomers.getRowCount();
        if(total > 0){
            tableCustomers.setRowSelectionInterval(total - 1, total - 1);
            try {
                showSelectedItem();
            } catch (SQLException ex) {
                showSQLErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchRoomException ex) {
                showErrorMessage(ex);
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnLastActionPerformed
    
    private void showSelectedItem() throws SQLException, NoSuchRoomException{
        TModelCustomer modelCustomer = (TModelCustomer)tableCustomers.getModel();
        int row = tableCustomers.getSelectedRow();
        Customer c = modelCustomer.getCustomer(row);
        
        txtCustomerRef.setText(String.valueOf(c.getId()));
        txtFirstName.setText(c.getFirstName());
        txtSurname.setText(c.getSurname());
        txtAdress.setText(c.getAddress());
        txtPostcode.setText(c.getPostCode());
        txtMobile.setText(c.getMobile());
        txtEmail.setText(c.getEmail());
        txtNationality.setText(c.getNationality());

        Calendar cal = Calendar.getInstance();
        java.util.Date dob = new java.util.Date(c.getDateOfBirth().getTime());
        txtDateOfBirth.setDate(dob);
        
        if(comboDocument.getItemCount() > 0){
            comboDocument.setSelectedIndex(0);
        }
        
        for(int i = 0; i < comboDocument.getItemCount(); ++i){
            int id_doc = c.getId_doc();
            Document doc = (Document)comboDocument.getItemAt(i);
            if(id_doc == doc.getId()){
                comboDocument.setSelectedIndex(i);
                break;
            }
        }
        if(c.getGender() == Gender.M){
            comboGender.setSelectedIndex(0);
        } else {
            comboGender.setSelectedIndex(1);
        }
        
        java.util.Date checkInDate = new java.util.Date(c.getCheckInDate().getTime());
        txtCheckInDate.setDate(checkInDate);
        
        java.util.Date checkOutDate = new java.util.Date(c.getCheckOutDate().getTime());
        txtCheckOutDate.setDate(checkOutDate);
        
        for(int i = 0; i < comboMeal.getItemCount(); ++i){
            Meal m = c.getMeal();
            if((comboMeal.getItemAt(i)).equals(m)){
                comboMeal.setSelectedIndex(i);
                break;
            }
        }
        if(comboRoomNumber.getItemCount() > 0){
            comboRoomNumber.setSelectedIndex(0);
        }
        
        for(int i = 0; i < comboRoomNumber.getItemCount(); ++i){
            Room r = roomDAO.get(c.getRoom());
            if((comboRoomNumber.getItemAt(i)).equals(r)){
                comboRoomNumber.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void clearForm(){
        txtCustomerRef.setText(null);
        txtFirstName.setText(null);
        txtSurname.setText(null);
        txtAdress.setText(null);
        txtPostcode.setText(null);
        txtMobile.setText(null);
        txtEmail.setText(null);
        txtNationality.setText(null);
        Calendar cal = Calendar.getInstance();
        txtDateOfBirth.setDate(cal.getTime());
        if(comboDocument.getItemCount() > 0){
            comboDocument.setSelectedIndex(0);
        }
        comboGender.setSelectedIndex(0);
        txtCheckInDate.setDate(cal.getTime());
        txtCheckOutDate.setDate(cal.getTime());
        comboMeal.setSelectedIndex(0);
        if(comboRoomNumber.getItemCount() > 0){
            comboRoomNumber.setSelectedIndex(0);
        }
        txtSubtotal.setText(null);
        txtTax.setText(null);
        txtTotal.setText(null);
        spinnerDiscount.setValue(0.0);
        txtSearchCustomer.setText(null);
    }

    private void showErrorMessage(Exception ex){
        JOptionPane.showMessageDialog(
            null,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private void showSQLErrorMessage(SQLException ex){
        JOptionPane.showMessageDialog(
            null,
            ex.getMessage(),
            "Database error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExit;
    private javax.swing.JToggleButton btnFirst;
    private javax.swing.JButton btnInsert;
    private javax.swing.JToggleButton btnLast;
    private javax.swing.JToggleButton btnNext;
    private javax.swing.JToggleButton btnPrevious;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSearchCustomer;
    private javax.swing.JButton btnTotal;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox checkBoxConsiderAge;
    private javax.swing.JComboBox comboDocument;
    private javax.swing.JComboBox comboGender;
    private javax.swing.JComboBox comboMeal;
    private javax.swing.JComboBox comboRoomNumber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbtnDiscountAbsolute;
    private javax.swing.JRadioButton rbtnPercentageDiscount;
    private javax.swing.JSpinner spinnerAgeMAX;
    private javax.swing.JSpinner spinnerAgeMIN;
    private javax.swing.JSpinner spinnerDiscount;
    private javax.swing.JTable tableCustomers;
    private javax.swing.JTextField txtAdress;
    private com.toedter.calendar.JDateChooser txtCheckInDate;
    private com.toedter.calendar.JDateChooser txtCheckOutDate;
    private javax.swing.JTextField txtCustomerRef;
    private com.toedter.calendar.JDateChooser txtDateOfBirth;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtNationality;
    private javax.swing.JTextField txtPostcode;
    private javax.swing.JTextField txtSearchCustomer;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtSurname;
    private javax.swing.JTextField txtTax;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JButton txtUpdate;
    // End of variables declaration//GEN-END:variables
}
