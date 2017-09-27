package tehotelmanagmentsystem.model.pojo;

/**
 *
 * @author igor
 */
public class Document {
    int id;
    String name;

    public Document() {
    }

    public Document(String name) {
        this.name = name;
    }

    public Document(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
