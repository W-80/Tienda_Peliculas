package Modelo;

import java.awt.HeadlessException;
import java.awt.JobAttributes;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wil
 */
public class crudUsuarios {

    int id;
    long cedula;
    String Nombres;
    String Apellidos;
    long celular;
    String direccion;

    conexionPostgresql conectar = new conexionPostgresql();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public long getCelular() {
        return celular;
    }

    public void setCelular(long celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    public void mostrarUsuarios(JTable listaUsuarios) {

        try {

            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultTableModel modeloTabla = new DefaultTableModel();

            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Cédula");
            modeloTabla.addColumn("Nombres");
            modeloTabla.addColumn("Apellidos");
            modeloTabla.addColumn("Celular");
            modeloTabla.addColumn("Dirección");

            listaUsuarios.setModel(modeloTabla);

            String sql = "SELECT f1_id_usuario, f1_cedula, f1_nombres, f1_apellidos, f1_numero_celular, f1_direccion FROM t1_usuarios WHERE f1_activo = 1 ORDER BY f1_id_usuario";
            String[] datos = new String[6];

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {

                datos[0] = resultado.getString(1);
                datos[1] = resultado.getString(2);
                datos[2] = resultado.getString(3);
                datos[3] = resultado.getString(4);
                datos[4] = resultado.getString(5);
                datos[5] = resultado.getString(6);

                modeloTabla.addRow(datos);

            }
            listaUsuarios.setModel(modeloTabla);
            declaracion.close();
            conexion1.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar información, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void insertarUsuario(JTextField parCedula, JTextField parNombres, JTextField parApellidos, JTextField parCelular, JTextField parDireccion) {

        try {
            Connection conexion1 = conectar.crearConexion();

            System.out.println(isNumeric(parCedula.getText()));
            System.out.println(isNumeric(parCelular.getText()));

            setCedula(Long.parseLong(parCedula.getText()));
            setNombres(parNombres.getText());
            setApellidos(parApellidos.getText());
            setCelular(Long.parseLong(parCelular.getText()));
            setDireccion(parDireccion.getText());

            String sql = "insert into t1_usuarios (f1_cedula, f1_nombres, f1_apellidos, f1_numero_celular, f1_direccion) values(?,?,?,?,?)";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setLong(1, getCedula());
            cs.setString(2, getNombres());
            cs.setString(3, getApellidos());
            cs.setLong(4, getCelular());
            cs.setString(5, getDireccion());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Información guardada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO guardada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void seleccionarUsuario(JTable listaUsuarios, JTextField parCedula, JTextField parNombres, JTextField parApellidos, JTextField parCelular, JTextField parDireccion) {

        try {
            int fila = listaUsuarios.getSelectedRow();

            if (fila >= 0) {

                parCedula.setText(listaUsuarios.getValueAt(fila, 1).toString());
                parNombres.setText(listaUsuarios.getValueAt(fila, 2).toString());
                parApellidos.setText(listaUsuarios.getValueAt(fila, 3).toString());
                parCelular.setText(listaUsuarios.getValueAt(fila, 4).toString());
                parDireccion.setText(listaUsuarios.getValueAt(fila, 5).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    public void editarUsuario(JTable listaUsuarios, JTextField parCedula, JTextField parNombres, JTextField parApellidos, JTextField parCelular, JTextField parDireccion) {

        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaUsuarios.getSelectedRow();

            setId(Integer.parseInt(listaUsuarios.getValueAt(fila, 0).toString()));
            setCedula(Long.parseLong(parCedula.getText()));
            setNombres(parNombres.getText());
            setApellidos(parApellidos.getText());
            setCelular(Long.parseLong(parCelular.getText()));
            setDireccion(parDireccion.getText());

            String sql = "UPDATE t1_usuarios SET "
                    + "f1_cedula = ?"
                    + ", f1_nombres = ?"
                    + ", f1_apellidos = ?"
                    + ", f1_numero_celular = ?"
                    + ", f1_direccion = ?"
                    + " where f1_id_usuario = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setLong(1, getCedula());
            cs.setString(2, getNombres());
            cs.setString(3, getApellidos());
            cs.setLong(4, getCelular());
            cs.setString(5, getDireccion());
            cs.setInt(6, getId());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Información actualizada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO actualizada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarUsuario(JTable listaUsuarios) {
        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaUsuarios.getSelectedRow();

            setId(Integer.parseInt(listaUsuarios.getValueAt(fila, 0).toString()));
            
            String sql = "UPDATE t1_usuarios SET f1_activo = 0 where f1_id_usuario = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getId());
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Registro eliminado con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Registro NO eliminado, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }
}
