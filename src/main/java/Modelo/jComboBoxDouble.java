
package Modelo;

/**
 *
 * @author Wil
 */
public class jComboBoxDouble {

    private String descripcion;
    private String id;

    public jComboBoxDouble(String descripcion, String id) {
        this.descripcion = descripcion;
        this.id = id;
    }

    public String getID() {
        return id;
    }
 
    public String toString() {
        return descripcion;
    }
}
