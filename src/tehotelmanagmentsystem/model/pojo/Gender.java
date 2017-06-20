package tehotelmanagmentsystem.model.pojo;

/**
 *
 * @author igor
 */
public enum Gender {
    M("M"),
    F("F");
    
    private final String type;
    
    private Gender(String type){
        this.type = type;
    }
    
    @Override
    public String toString(){
        return type;
    }
    
    public String format(){
        String result;
        switch (type){
            case "M":
                result = "Male";
                break;
            case "F":
                result = "Female";
                break;
            default :
                result = "";                
        }
        return result;
    }

}
