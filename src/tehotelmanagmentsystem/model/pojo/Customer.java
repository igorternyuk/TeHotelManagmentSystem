package tehotelmanagmentsystem.model.pojo;

import java.sql.Date;
import tehotelmanagmentsystem.model.Meal;

/**
 *
 * @author igor
 */
public class Customer {
    int id;
    String firstName, surname, address, postCode, mobile, email, nationality;
    Date dateOfBirth;
    int id_doc;
    Gender gender;
    Date checkInDate, checkOutDate;
    Meal meal;
    int room;
    
    public Customer() {
    }

    public Customer(String firstName, String surname, String address,
            String postCode, String mobile, String email, String nationality,
            Date dateOfBirth, int idType, Gender gender, Date checkInDate, 
            Date checkOutDate, Meal meal, int room) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.postCode = postCode;
        this.mobile = mobile;
        this.email = email;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.id_doc = idType;
        this.gender = gender;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.meal = meal;
        this.room = room;
    }

    public Customer(int id, String firstName, String surname, String address,
            String postCode, String mobile, String email, String nationality,
            Date dateOfBirth, int idType, Gender gender, Date checkInDate,
            Date checkOutDate, Meal meal, int room) {
        this(firstName, surname, address, postCode, mobile, email, nationality,
            dateOfBirth, idType, gender, checkInDate, checkOutDate, meal, room);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getId_doc() {
        return id_doc;
    }

    public void setId_doc(int id_doc) {
        this.id_doc = id_doc;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }
    
//    @Override
//    public String toString(){
//        return dateOfBirth.toString() + " " + checkInDate.toString() + " " +
//                checkOutDate.toString();
//    }
}
