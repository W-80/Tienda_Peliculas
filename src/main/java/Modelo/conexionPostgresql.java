
package Modelo;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Wil
 */
public class conexionPostgresql {
    Connection conectar=null;
    String usuario="ffqvhahibheqxk";
    String clave="1e1fb3ca42da0e2fc02cabbbd3c783cc8b7d9de257d7e9de97caa36e60635c1f";
    String baseDatos="dchedemqeofb50";
    String host="ec2-44-209-186-51.compute-1.amazonaws.com";
    String puerto="5432";
    String url="jdbc:postgresql://"+host+":"+puerto+"/"+baseDatos;
    
    public Connection crearConexion(){
        try {
            Class.forName("org.postgresql.Driver");
            conectar=DriverManager.getConnection(url,usuario,clave);
            //JOptionPane.showMessageDialog(null, "Se conectó correctamente a la base de datos", "Conexión", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos, error: "+e.toString(), "Conexión", JOptionPane.ERROR_MESSAGE);
        }
        
        return conectar;
    }
    
    public Connection cerrarConexion(){
        try {
            conectar.close();
            JOptionPane.showMessageDialog(null, "Desconexión exitosa", "Desconexión", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desconexión fallida, error: "+e.toString(), "Descoenxión", JOptionPane.ERROR_MESSAGE);
        }
        return conectar;
    }
}
