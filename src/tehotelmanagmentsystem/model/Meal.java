/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tehotelmanagmentsystem.model;

/**
 *
 * @author igor
 */
public enum Meal {
    NO_MEAL("NO_MEAL"),
    ONE_MEAL("ONE_MEAL"),
    TWO_MEALS("TWO_MEALS"),
    THREE_MEALS("THREE_MEALS");
    
    private final String type;
    
    private Meal(String type){
        this.type = type;
    }
    
    @Override
    public String toString(){
        return type;
    }
}
