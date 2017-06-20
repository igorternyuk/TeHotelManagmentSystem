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
public enum RoomType {
    SINGLE("SINGLE"),
    DOUBLE("DOUBLE"),
    FAMILY("FAMILY");
    
    private final String type;
    
    private RoomType(String type){
        this.type = type;
    }
    
    @Override
    public String toString(){
        return type;
    }
}
