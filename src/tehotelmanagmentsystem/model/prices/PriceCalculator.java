package tehotelmanagmentsystem.model.prices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import tehotelmanagmentsystem.model.Meal;
import tehotelmanagmentsystem.model.RoomType;

/**
 *
 * @author igor
 */
public class PriceCalculator {
    private static PriceCalculator instance;
    private static final String PATH_TO_CONFIG_FILE = "prices.dat";
    private double singeRoomCostPerDay, doubleRoomCostPerDay,
        familyRoomCostPerDay;
    private double oneMealCostPerDay, twoMealsCostPerDay,
        threeMealsCostPerDay;
    private double tax;
    public static PriceCalculator getInstance() throws IOException{
        if(instance == null){
            instance = new PriceCalculator();
        }
        return instance;
    }
    private PriceCalculator() throws IOException{
        readFromFile(PATH_TO_CONFIG_FILE);
    }
    
    public void readSettingsFromFile(String pathToConfigFile) throws IOException{
       readFromFile(pathToConfigFile); 
    }
    public double getRoomCostPerDay(RoomType roomType){
        double result = 0;
        switch(roomType){
            case Single :
                result = singeRoomCostPerDay;
                break;
            case Double :
                result = doubleRoomCostPerDay;
                break;
            case Family :
                result = familyRoomCostPerDay;
                break;
        }
        return result;
    }
    
    public double getMealCostPerDay(Meal meal){
        double result = 0;
        switch(meal){
            case NO_MEAL :
                result = 0;
                break;
            case ONE_MEAL :
                result = oneMealCostPerDay;
                break;
            case TWO_MEALS :
                result = twoMealsCostPerDay;
                break;
            case THREE_MEALS :
                result = threeMealsCostPerDay;
                break;
        }
        return result;
    }
    
    public double getTax(){
        return tax;
    }
    
    public double calcSubTotal(int numDays, RoomType roomType, Meal meal){
        return numDays * (getRoomCostPerDay(roomType) + getMealCostPerDay(meal));
    } 
    
    public double calcSubTotal(java.util.Date checkInDate,
            java.util.Date checkOutDate, RoomType roomType, Meal meal){
        int numDays = calcDayDifference(checkInDate, checkOutDate);
        return calcSubTotal(numDays, roomType, meal);        
    }
    
    public double calcTotal(int numDays, RoomType roomType, Meal meal, double discount){
        return tax + calcSubTotal(numDays, roomType, meal) - discount;
    }
    
    public double calcTotalWithPercentageDiscount(int numDays, RoomType roomType,
            Meal meal, double discount){
        if(discount < 0){
            discount = 0;
        } else if(discount > 100){
            discount = 100;
        }
        return tax + calcSubTotal(numDays, roomType, meal) * (100 - discount) / 100;
    }
    
    public double calcTotal(java.util.Date checkInDate,
            java.util.Date checkOutDate, RoomType roomType, Meal meal,
            double discount){
        return tax + calcSubTotal(checkInDate, checkOutDate, roomType, meal) - 
            discount;
    }
    
    public double calcTotalWithPercentageDiscount(java.util.Date checkInDate,
            java.util.Date checkOutDate, RoomType roomType, Meal meal,
            double discount){
        if(discount < 0){
            discount = 0;
        } else if(discount > 100){
            discount = 100;
        }
        return tax + calcSubTotal(checkInDate, checkOutDate, roomType, meal) *
               (100 - discount) / 100;
    }
    
    private int calcDayDifference(java.util.Date date1, java.util.Date date2){
        long date1_ms = date1.getTime();
        long date2_ms = date2.getTime();
        return (int)Math.round((date2_ms - date1_ms) / 1000 / 60 / 60 / 24.0);
    }
    
    private enum LoadState{
        TAX,
        SINGLE_ROOM,
        DOUBLE_ROOM,
        FAMILY_ROOM,
        ONE_MEAL,
        TWO_MEALS,
        THREE_MEALS,
        STOP
    }
    
    private void readFromFile (String pathToFile) throws
            UnsupportedEncodingException, IOException{
        InputStream in = getClass().getResourceAsStream(pathToFile);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in,
                "UTF-8"))){
            String str;
            LoadState ls;
            label: while((str = br.readLine()) != null){
                if(str.equalsIgnoreCase("[Tax]")){
                    ls = LoadState.TAX;                    
                } else if(str.equalsIgnoreCase("[SingleRoom]")){
                    ls = LoadState.SINGLE_ROOM;
                } else if(str.equalsIgnoreCase("[DoubleRoom]")){
                    ls = LoadState.DOUBLE_ROOM;
                } else if(str.equalsIgnoreCase("[FamilyRoom]")){
                    ls = LoadState.FAMILY_ROOM;
                } else if(str.equalsIgnoreCase("[OneMeal]")){
                    ls = LoadState.ONE_MEAL;
                } else if(str.equalsIgnoreCase("[TwoMeals]")){
                    ls = LoadState.TWO_MEALS;
                } else if(str.equalsIgnoreCase("[ThreeMeals]")){
                    ls = LoadState.THREE_MEALS;
                } else {
                    ls = LoadState.STOP;
                }
                
                str = br.readLine();
                
                switch(ls){
                    case TAX :
                        tax = Double.parseDouble(str);
                        break;
                    case SINGLE_ROOM :
                        singeRoomCostPerDay = Double.parseDouble(str);
                        break;
                    case DOUBLE_ROOM :
                        doubleRoomCostPerDay = Double.parseDouble(str);
                        break;
                    case FAMILY_ROOM :
                        familyRoomCostPerDay = Double.parseDouble(str);
                        break;
                    case ONE_MEAL :
                        oneMealCostPerDay = Double.parseDouble(str);
                        break;
                    case TWO_MEALS :
                        twoMealsCostPerDay = Double.parseDouble(str);
                        break;
                    case THREE_MEALS :
                        threeMealsCostPerDay = Double.parseDouble(str);
                        break;
                    case STOP :
                        break label;
                }
            }
        }
    }
    
//    public static void main(String[] args) {
//        try {
//            PriceCalculator pc = new PriceCalculator();
//            System.out.println("tax = " + pc.getTax());
//            System.out.println("singleRoom = " + pc.getRoomCostPerDay(RoomType.SINGLE));
//            System.out.println("doubleRoom = " + pc.getRoomCostPerDay(RoomType.DOUBLE));
//            System.out.println("familyRoom = " + pc.getRoomCostPerDay(RoomType.FAMILY));
//            System.out.println("oneMeal = " + pc.getMealCostPerDay(Meal.ONE_MEAL));
//            System.out.println("twoMeals = " + pc.getMealCostPerDay(Meal.TWO_MEALS));
//            System.out.println("threeMeal = " + pc.getMealCostPerDay(Meal.THREE_MEALS));
//            Calendar cal = Calendar.getInstance();
//            java.util.Date date1 = cal.getTime();
//            cal.add(Calendar.DAY_OF_MONTH, 15);
//            java.util.Date date2 = cal.getTime();
//            System.out.println("costForTenDays = " + 
//                    pc.calcTotalWithPercentageDiscount(date1, date2, 
//                    RoomType.DOUBLE, Meal.THREE_MEALS, 10));
//            System.out.println("costForTenDays = " + pc.calcTotal(15, 
//                    RoomType.DOUBLE, Meal.THREE_MEALS, 50));
//        } catch (IOException ex) {
//            Logger.getLogger(PriceCalculator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
