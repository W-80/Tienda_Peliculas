package Modelo;

import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class crudTitulos {

    int id;
    String nombre;
    int id_categoria;
    int cantidad;

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    conexionPostgresql conectar = new conexionPostgresql();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void mostrarTitulos(JTable listaTitulos) {

        try {

            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultTableModel modeloTabla = new DefaultTableModel();

            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Título");
            modeloTabla.addColumn("Categoria");
            modeloTabla.addColumn("Cantidad");

            listaTitulos.setModel(modeloTabla);

            String sql = "SELECT "
                    + "t2.f2_id_titulo"
                    + ", t2.f2_nombre_titulo"
                    + ", t4.f4_nombre "
                    + ", t2.f2_cantidad "
                    + "FROM t2_titulos t2 "
                    + "LEFT JOIN t4_categorias t4 ON t4.f4_id_categoria = t2.f2_id_categoria "
                    + "WHERE t2.f2_activo = 1 "
                    + "ORDER BY t2.f2_id_titulo";
            String[] datos = new String[4];

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {

                datos[0] = resultado.getString(1);
                datos[1] = resultado.getString(2);
                datos[2] = resultado.getString(3);
                datos[3] = resultado.getString(4);

                modeloTabla.addRow(datos);

            }
            listaTitulos.setModel(modeloTabla);
            declaracion.close();
            resultado.close();
            conexion1.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar información, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void comboVoxCategorias(JComboBox parComboBoxCategorias) {

        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultComboBoxModel modeloCB = new DefaultComboBoxModel();

            String sql = "SELECT f4_id_categoria, f4_nombre FROM t4_categorias WHERE f4_activo = 1 ORDER BY f4_id_categoria";
            ResultSet resultado = declaracion.executeQuery(sql);
            parComboBoxCategorias.setModel(modeloCB);

            while (resultado.next()) {
                System.out.println(resultado.getString("f4_nombre") + resultado.getString("f4_id_categoria"));
                modeloCB.addElement(new jComboBoxDouble(resultado.getString("f4_nombre"), resultado.getString("f4_id_categoria")));
            }
            resultado.close();
            conexion1.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar la lista, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertarTitulo(JTextField parNombre, JComboBox parCategoria, JTextField parCantidad) {

        try {
            Connection conexion1 = conectar.crearConexion();
            jComboBoxDouble caja;
            caja = (jComboBoxDouble) parCategoria.getSelectedItem();

            setNombre(parNombre.getText());
            setId_categoria(Integer.parseInt(caja.getID()));
            setCantidad(Integer.parseInt(parCantidad.getText()));

            String sql = "INSERT INTO t2_titulos (f2_nombre_titulo, f2_id_categoria, f2_cantidad) VALUES (?,?,?)";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setString(1, getNombre());
            cs.setInt(2, getId_categoria());
            cs.setInt(3, getCantidad());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Información guardada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO guardada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int buscarIndice(JComboBox CB, String descripcion) {

        int totalItems = CB.getItemCount();
        int index = 0;
        while (index < totalItems) {
            System.out.println(descripcion);
            System.out.println(CB.getItemAt(index).toString());

            if (CB.getItemAt(index).toString().equals(descripcion)) {
                break;
            } else {
                index = index + 1;
            }

        }
        return index;

    }

    public void seleccionarTitulo(JTable listaTitulos, JTextField parNombre, JComboBox parCategoria, JTextField parCantidad) {

        try {
            int fila = listaTitulos.getSelectedRow();
            int indiceCB = buscarIndice(parCategoria, listaTitulos.getValueAt(fila, 2).toString());

            if (fila >= 0) {

                parNombre.setText(listaTitulos.getValueAt(fila, 1).toString());
                parCategoria.setSelectedIndex(indiceCB);
                parCantidad.setText(listaTitulos.getValueAt(fila, 3).toString());

                System.out.println(parCategoria.toString());

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    public void editarTitulo(JTable listaTitulos, JTextField parNombre, JComboBox parCategoria, JTextField parCantidad) {

        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaTitulos.getSelectedRow();
            jComboBoxDouble caja;
            caja = (jComboBoxDouble) parCategoria.getSelectedItem();

            setId(Integer.parseInt(listaTitulos.getValueAt(fila, 0).toString()));
            setNombre(parNombre.getText());
            setId_categoria(Integer.parseInt(caja.getID()));
            setCantidad(Integer.parseInt(parCantidad.getText()));

            String sql = "UPDATE t2_titulos SET "
                    + "f2_nombre_titulo = ? "
                    + ",f2_id_Categoria = ? "
                    + ",f2_cantidad = ?"
                    + " where f2_id_titulo = ? ";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setString(1, getNombre());
            cs.setInt(2, getId_categoria());
            cs.setInt(3, getCantidad());
            cs.setInt(4, getId());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Información actualizada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO actualizada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarTitulo(JTable listaTitulos) {
        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaTitulos.getSelectedRow();

            setId(Integer.parseInt(listaTitulos.getValueAt(fila, 0).toString()));

            String sql = "UPDATE t2_titulos SET f2_activo = 0 where f2_id_titulo = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getId());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Registro eliminado con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Registro NO eliminado, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    public String[] validarEliminacionTitulo(JTable listaTitulos) {

        String[] totales = new String[2];
        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            int fila = listaTitulos.getSelectedRow();
            setId(Integer.parseInt(listaTitulos.getValueAt(fila, 0).toString()));

            String sql = "SELECT\n"
                    + "(SELECT\n"
                    + "count(f3_id_usuario)\n"
                    + "from t2_titulos\n"
                    + "left join t3_peliculas ON f3_id_titulo = f2_id_titulo\n"
                    + "left join t1_usuarios on f1_id_usuario = f3_id_usuario\n"
                    + "where f2_id_titulo = "+getId()+" and f2_activo = 1) as total\n"
                    + ",count(f3_id_usuario) as alquilado\n"
                    + "from t2_titulos\n"
                    + "left join t3_peliculas ON f3_id_titulo = f2_id_titulo\n"
                    + "left join t1_usuarios on f1_id_usuario = f3_id_usuario\n"
                    + "where f3_id_usuario != 0 and f2_id_titulo = "+getId()+" and f2_activo = 1";

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {
                totales[0] = resultado.getString(1);
                totales[1] = resultado.getString(2);
            }
            declaracion.close();
            conexion1.close();
        } catch (Exception e) {
        }
        return totales;
    }

    public void reasignarCategoria(int parIdTitulo) {

        try {
            Connection conexion1 = conectar.crearConexion();
            String sql = "UPDATE t2_titulos SET "
                    + "f2_id_Categoria = 0 "
                    + " where f2_id_titulo = ? ";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, parIdTitulo);
            cs.execute();

            JOptionPane.showMessageDialog(null, "Información actualizada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO actualizada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }
}
