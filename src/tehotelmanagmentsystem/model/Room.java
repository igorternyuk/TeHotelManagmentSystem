package tehotelmanagmentsystem.model;

import java.util.Objects;

/**
 *
 * @author igor
 */
public class Room {
    int id;
    RoomType type;
    boolean isFree;

    public Room() {
    }

    public Room(RoomType type, boolean isFree) {
        this.type = type;
        this.isFree = isFree;
    }

    public Room(int id, RoomType type, boolean isFree) {
        this.id = id;
        this.type = type;
        this.isFree = isFree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public boolean isIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        } else {
            Room other = (Room)o;
            return this.id == other.getId() && this.type.equals(other.getType()) &&
                   this.isFree == other.isIsFree();
        }
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.id + Objects.hashCode(this.type) + 
               (this.isFree ? 1 : 0);
//        hash = 37 * hash + Objects.hashCode(this.type);
//        hash = 37 * hash + (this.isFree ? 1 : 0);
        return hash;
    }
    @Override
    public String toString(){
        return id + " (" + type.toString() + ")";
    }
}
