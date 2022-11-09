package Modelo;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wil
 */
public class crudPeliculas {

    int id;
    int idTitulo;
    int idUsuario;
    Date fechaAdqusicion;
    Date fechaAlquilado;
    String disponible;
    int idEstado;

    conexionPostgresql conectar = new conexionPostgresql();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTitulo() {
        return idTitulo;
    }

    public void setIdTitulo(int idTitulo) {
        this.idTitulo = idTitulo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaAdqusicion() {
        return fechaAdqusicion;
    }

    public void setFechaAdqusicion(Date fechaAdqusicion) {
        this.fechaAdqusicion = fechaAdqusicion;
    }

    public Date getFechaAlquilado() {
        return fechaAlquilado;
    }

    public void setFechaAlquilado(Date fechaAlquilado) {
        this.fechaAlquilado = fechaAlquilado;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int estado) {
        this.idEstado = estado;
    }

    public void mostrarPeliculas(JTable listaPeliculas) {

        try {

            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultTableModel modeloTabla = new DefaultTableModel();

            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Nombre película");
            modeloTabla.addColumn("Fecha de adquisición");
            modeloTabla.addColumn("Usuario actual");
            modeloTabla.addColumn("Fecha de alquiler");
            modeloTabla.addColumn("Disponible");
            modeloTabla.addColumn("Estado");

            listaPeliculas.setModel(modeloTabla);

            String sql = "SELECT \n"
                    + "f3_id_pelicula\n"
                    + ", f2_nombre_titulo\n"
                    + ", f3_fecha_adquisicion\n"
                    + ", CASE WHEN f3_id_usuario IS NULL THEN 'NINGUNO' WHEN f3_id_usuario = 0 THEN 'NINGUNO' ELSE f1_nombres || ' ' || f1_apellidos END AS usuario\n"
                    + ", f3_fecha_ultimo_alquiler\n"
                    + ", CASE f3_disponible WHEN 1 THEN 'SI' ELSE 'NO' END AS disponible\n"
                    + ", f5_descripcion\n"
                    + "FROM t3_peliculas\n"
                    + "LEFT JOIN t2_titulos ON t2_titulos.f2_id_titulo = t3_peliculas.f3_id_titulo\n"
                    + "LEFT JOIN t1_usuarios ON t1_usuarios.f1_id_usuario = t3_peliculas.f3_id_usuario\n"
                    + "LEFT JOIN t5_estados ON t5_estados.f5_id_estado = t3_peliculas.f3_id_estado\n"
                    + "WHERE f3_activo = 1\n"
                    + "ORDER BY f3_id_pelicula";
            String[] datos = new String[7];

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {

                datos[0] = resultado.getString(1);
                datos[1] = resultado.getString(2);
                datos[2] = resultado.getString(3);
                datos[3] = resultado.getString(4);
                datos[4] = resultado.getString(5);
                datos[5] = resultado.getString(6);
                datos[6] = resultado.getString(7);

                modeloTabla.addRow(datos);

            }
            listaPeliculas.setModel(modeloTabla);
            declaracion.close();
            conexion1.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar información, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void comboVoxTitulos(JComboBox parComboBoxTitulos) {

        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultComboBoxModel modeloCB = new DefaultComboBoxModel();

            String sql = "SELECT f2_id_titulo, f2_nombre_titulo FROM t2_titulos WHERE f2_activo = 1 ORDER BY f2_id_categoria";
            ResultSet resultado = declaracion.executeQuery(sql);
            parComboBoxTitulos.setModel(modeloCB);

            while (resultado.next()) {
                modeloCB.addElement(new jComboBoxDouble(resultado.getString(2), resultado.getString(1)));
            }
            resultado.close();
            conexion1.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar la lista, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void comboVoxUsuario(JComboBox parComboBoxUsuarios) {

        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultComboBoxModel modeloCB = new DefaultComboBoxModel();

            String sql = "SELECT f1_id_usuario, CASE WHEN f1_id_usuario IS NULL THEN 'NINGUNO' WHEN f1_id_usuario = 0 THEN 'NINGUNO' ELSE f1_nombres || ' ' || f1_apellidos END AS usuario FROM t1_usuarios WHERE f1_activo = 1 ORDER BY f1_id_usuario";
            ResultSet resultado = declaracion.executeQuery(sql);
            parComboBoxUsuarios.setModel(modeloCB);

            while (resultado.next()) {
                modeloCB.addElement(new jComboBoxDouble(resultado.getString(2), resultado.getString(1)));
            }
            resultado.close();
            conexion1.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar la lista, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void comboVoxEstados(JComboBox parComboBoxEstados) {

        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultComboBoxModel modeloCB = new DefaultComboBoxModel();

            String sql = "SELECT f5_id_estado, f5_descripcion FROM t5_estados ORDER BY f5_id_estado";
            ResultSet resultado = declaracion.executeQuery(sql);
            parComboBoxEstados.setModel(modeloCB);

            while (resultado.next()) {
                modeloCB.addElement(new jComboBoxDouble(resultado.getString(2), resultado.getString(1)));
            }
            resultado.close();
            conexion1.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar la lista, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int buscarIndice(JComboBox CB, String descripcion) {

        int totalItems = CB.getItemCount();
        int index = 0;
        while (index < totalItems) {
            if (CB.getItemAt(index).toString().equals(descripcion)) {
                break;
            } else {
                index = index + 1;
            }

        }
        return index;

    }

    public void insertarPelicula(JComboBox parNombreTitulo, JDateChooser parFechaAdquirido, JComboBox parUsuario, JDateChooser parFechaAlquilada, JComboBox parEstado) {

        try {
            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parNombreTitulo.getSelectedItem();
            jComboBoxDouble cajaUsuario = (jComboBoxDouble) parUsuario.getSelectedItem();
            jComboBoxDouble cajaEstado = (jComboBoxDouble) parEstado.getSelectedItem();

            if (parFechaAdquirido.getDate() == null) {
                setFechaAdqusicion(null);
            } else {
                java.util.Date fechaA = parFechaAdquirido.getDate();
                long fA = fechaA.getTime();
                Date fechaSQLA = new Date(fA);
                setFechaAdqusicion(fechaSQLA);
            }
            if (parFechaAlquilada.getDate() == null) {
                setFechaAlquilado(null);
            } else {
                java.util.Date fechaB = parFechaAlquilada.getDate();
                long fB = fechaB.getTime();
                Date fechaSQLB = new Date(fB);
                setFechaAlquilado(fechaSQLB);
            }
            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));
            setIdUsuario(Integer.parseInt(cajaUsuario.getID()));
            setIdEstado(Integer.parseInt(cajaEstado.getID()));

            String sql = "insert into t3_peliculas(f3_id_titulo, f3_id_usuario, f3_fecha_adquisicion, f3_fecha_ultimo_alquiler, f3_id_estado) values(?,?,?,?,?)";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getIdTitulo());
            cs.setInt(2, getIdUsuario());
            cs.setDate(3, getFechaAdqusicion());
            cs.setDate(4, getFechaAlquilado());
            cs.setInt(5, getIdEstado());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Información guardada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO guardada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertarPorDefecto(JTextField cantidad) {

        try {
            Connection conexion1 = conectar.crearConexion();
            String sql = "do $$\n"
                    + "begin\n"
                    + "for r in 1.."+Integer.parseInt(cantidad.getText())+" loop\n"
                    + "insert into t3_peliculas(f3_id_titulo) values((select max(f2_id_titulo) from t2_titulos)"
                    + ");\n"
                    + "end loop;\n"
                    + "end;\n"
                    + "$$;";
            CallableStatement cs = conexion1.prepareCall(sql);
            System.out.println(Integer.parseInt(cantidad.getText()));
            //cs.setInt(0, Integer.parseInt(cantidad.getText()));
            cs.execute();
            conexion1.close();
            JOptionPane.showMessageDialog(null, "Se adicionaron "+cantidad.getText()+" películas del título.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "NO se adicionaron las películas, error: "+ e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sumarCantidad(JComboBox parTitulo, int parNumeroPeliculas) {

        try {
            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parTitulo.getSelectedItem();

            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));

            String sql = "UPDATE t2_titulos SET "
                    + "f2_cantidad = f2_cantidad + ?"
                    + " where f2_id_titulo = ? ";
            CallableStatement cs = conexion1.prepareCall(sql);

            cs.setInt(1, parNumeroPeliculas);
            cs.setInt(2, getIdTitulo());
            cs.execute();
            conexion1.close();
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "cantidad NO modificada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void restarCantidad(JTable listaPeliculas, JComboBox parTitulo, int parNumeroPeliculas) {

        try {
            Connection conexion1 = conectar.crearConexion();

            int fila = listaPeliculas.getSelectedRow();

            int indiceTitulo = buscarIndice(parTitulo, listaPeliculas.getValueAt(fila, 1).toString());
            parTitulo.setSelectedIndex(indiceTitulo);
            jComboBoxDouble titulo = (jComboBoxDouble) parTitulo.getSelectedItem();
            setIdTitulo(Integer.parseInt(titulo.getID()));

            String sql = "UPDATE t2_titulos SET "
                    + "f2_cantidad = f2_cantidad + ?"
                    + " where f2_id_titulo = ? ";
            CallableStatement cs = conexion1.prepareCall(sql);

            cs.setInt(1, parNumeroPeliculas);
            cs.setInt(2, getIdTitulo());
            cs.execute();
            conexion1.close();
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "cantidad NO modificada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void seleccionarPelicula(JTable listaPeliculas, JComboBox parNombreTitulo, JDateChooser parFechaAdquirido, JComboBox parUsuario, JDateChooser parFechaAlquiler, JTextField parDisponible, JComboBox parEstado) {

        try {
            int fila = listaPeliculas.getSelectedRow();
            int indiceCBTitulo = buscarIndice(parNombreTitulo, listaPeliculas.getValueAt(fila, 1).toString());
            int indiceCBUsuario = buscarIndice(parUsuario, listaPeliculas.getValueAt(fila, 3).toString());
            int indiceCBEstado = buscarIndice(parEstado, listaPeliculas.getValueAt(fila, 6).toString());

            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-mm-dd");

            if (fila >= 0) {

                parNombreTitulo.setSelectedIndex(indiceCBTitulo);
                try {
                    if (listaPeliculas.getValueAt(fila, 2) == null) {
                    } else {
                        java.util.Date fechaA = formatoFecha.parse(listaPeliculas.getValueAt(fila, 2).toString());
                        long fA = fechaA.getTime();
                        Date fechaSQLA = new Date(fA);
                        parFechaAdquirido.setDate(fechaSQLA);
                    }
                    if (listaPeliculas.getValueAt(fila, 4) == null) {
                    } else {
                        java.util.Date fechaB = formatoFecha.parse(listaPeliculas.getValueAt(fila, 4).toString());
                        long fB = fechaB.getTime();
                        Date fechaSQLB = new Date(fB);
                        parFechaAdquirido.setDate(fechaSQLB);
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(crudPeliculas.class.getName()).log(Level.SEVERE, null, ex);
                }
                parUsuario.setSelectedIndex(indiceCBUsuario);
                parDisponible.setText(listaPeliculas.getValueAt(fila, 5).toString());
                parEstado.setSelectedIndex(indiceCBEstado);

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    public void editarPelicula(JTable listaPeliculas, JComboBox parNombreTitulo, JDateChooser parFechaAdquirido, JComboBox parUsuario, JDateChooser parFechaAlquilada, JComboBox parEstado) {

        try {

            int fila = listaPeliculas.getSelectedRow();

            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble cajaTitulo = (jComboBoxDouble) parNombreTitulo.getSelectedItem();
            jComboBoxDouble cajaUsuario = (jComboBoxDouble) parUsuario.getSelectedItem();
            jComboBoxDouble cajaEstado = (jComboBoxDouble) parEstado.getSelectedItem();

            setId(Integer.parseInt(listaPeliculas.getValueAt(fila, 0).toString()));
            if (parFechaAdquirido.getDate() == null) {
                setFechaAdqusicion(null);
            } else {
                java.util.Date fechaA = parFechaAdquirido.getDate();
                long fA = fechaA.getTime();
                Date fechaSQLA = new Date(fA);
                setFechaAdqusicion(fechaSQLA);
            }
            if (parFechaAlquilada.getDate() == null) {
                setFechaAlquilado(null);
            } else {
                java.util.Date fechaB = parFechaAlquilada.getDate();
                long fB = fechaB.getTime();
                Date fechaSQLB = new Date(fB);
                setFechaAlquilado(fechaSQLB);
            }
            setIdTitulo(Integer.parseInt(cajaTitulo.getID()));
            setIdUsuario(Integer.parseInt(cajaUsuario.getID()));
            setIdEstado(Integer.parseInt(cajaEstado.getID()));

            String sql = "UPDATE t3_peliculas SET "
                    + "                    f3_id_titulo = ?"
                    + "                    ,f3_id_usuario = ?"
                    + "                    ,f3_fecha_adquisicion = ?"
                    + "			   ,f3_fecha_ultimo_alquiler = ?"
                    + "		           ,f3_id_estado = ?"
                    + "                    where f3_id_pelicula = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getIdTitulo());
            cs.setInt(2, getIdUsuario());
            cs.setDate(3, getFechaAdqusicion());
            cs.setDate(4, getFechaAlquilado());
            cs.setInt(5, getIdEstado());
            cs.setInt(6, getId());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Información actualizada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO actualizada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String[] validarEliminarPelicula(JTable listaPeliculas) {

        String[] totales = new String[3];
        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            int fila = listaPeliculas.getSelectedRow();
            setId(Integer.parseInt(listaPeliculas.getValueAt(fila, 0).toString()));

            String sql = "select\n"
                    + "f3_id_pelicula\n"
                    + ",f3_id_usuario\n"
                    + ",f1_nombres || ' ' || f1_apellidos as usuario\n"
                    + "from  t3_peliculas\n"
                    + "left join t1_usuarios on f1_id_usuario = f3_id_usuario\n"
                    + "where f3_id_pelicula = ? and f3_activo = 1";

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {
                totales[0] = resultado.getString(1);
                totales[1] = resultado.getString(2);
                totales[2] = resultado.getString(3);
            }
            declaracion.close();
            conexion1.close();
        } catch (Exception e) {
        }
        return totales;
    }

    public void eliminarPelicula(JTable listaPeliculas) {
        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaPeliculas.getSelectedRow();

            setId(Integer.parseInt(listaPeliculas.getValueAt(fila, 0).toString()));

            String sql = "UPDATE t3_peliculas SET f3_activo = 0 where f3_id_pelicula = ?";
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
