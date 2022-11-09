package Modelo;

import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Wil
 */
public class ventaAlquiler {

    int idUsuario;
    int idTitulo;

    conexionPostgresql conectar = new conexionPostgresql();

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTitulo() {
        return idTitulo;
    }

    public void setIdTitulo(int idTitulo) {
        this.idTitulo = idTitulo;
    }

    public String[] peliculaDisponible(JComboBox parTitulos) {
        
        String[] datos = new String[1];
        try {

            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parTitulos.getSelectedItem();
            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));

            String sql = "select \n"
                    + "min(f3_id_pelicula)\n"
                    + "from t3_peliculas\n"
                    + "where f3_id_titulo = " + getIdTitulo() + " and f3_disponible = 1";
            ResultSet resultado = declaracion.executeQuery(sql);

            while (resultado.next()) {
                datos[0] = resultado.getString(1);
            }
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No disponibilidad, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return datos;
    }

    public void alquilarPelicula(JComboBox parUsuarios, JComboBox parTitulos) {
        try {

            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parTitulos.getSelectedItem();
            jComboBoxDouble cajaUsuario = (jComboBoxDouble) parUsuarios.getSelectedItem();

            LocalDate todaysDate = LocalDate.now();
            Date fechaActual = Date.valueOf(todaysDate);

            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));
            setIdUsuario(Integer.parseInt(cajaUsuario.getID()));

            String sql = "UPDATE t3_peliculas SET \n"
                    + "f3_id_usuario = ?\n"
                    + ",f3_fecha_ultimo_alquiler = ?\n"
                    + ",f3_id_estado = 2\n"
                    + ",f3_disponible = 0   \n"
                    + "where f3_id_pelicula =\n"
                    + "(select  \n"
                    + "min(f3_id_pelicula)\n"
                    + "from t3_peliculas\n"
                    + "where f3_id_titulo = ? and f3_disponible = 1) ";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getIdUsuario());
            cs.setDate(2, fechaActual);
            cs.setInt(3, getIdTitulo());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Registro de alquiler exitoso.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Registro de alquiler NO tomado, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void venderPelicula(JComboBox parUsuarios, JComboBox parTitulos) {
        try {

            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parTitulos.getSelectedItem();
            jComboBoxDouble cajaUsuario = (jComboBoxDouble) parUsuarios.getSelectedItem();

            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));
            setIdUsuario(Integer.parseInt(cajaUsuario.getID()));

            String sql = "UPDATE t3_peliculas SET \n"
                    + "f3_id_usuario = ?\n"
                    + ",f3_id_estado = 3\n"
                    + ",f3_disponible = 0 \n"
                    + ",f3_activo = 0\n"
                    + "where f3_id_pelicula =\n"
                    + "(select  \n"
                    + "min(f3_id_pelicula)\n"
                    + "from t3_peliculas\n"
                    + "where f3_id_titulo = ? and f3_disponible = 1) ";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getIdUsuario());
            cs.setInt(2, getIdTitulo());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Registro de venta exitoso.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Registro de venta NO tomado, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void restarVendida(JComboBox parTitulos) {
        try {

            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parTitulos.getSelectedItem();

            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));

            String sql = "UPDATE t2_titulos SET "
                    + "                    f2_cantidad = f2_cantidad - 1"
                    + "                    where f2_id_titulo = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getIdTitulo());
            cs.execute();
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se descontó la pelicula, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }
}
