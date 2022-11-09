package Modelo;

import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wil
 */
public class crudCategorias {

    int id;
    String nombre;

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

    public void mostrarCategorias(JTable listaCategorias) {

        try {

            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            DefaultTableModel modeloTabla = new DefaultTableModel();

            modeloTabla.addColumn("Código");
            modeloTabla.addColumn("Nombre categoria");

            listaCategorias.setModel(modeloTabla);

            String sql = "SELECT f4_id_categoria, f4_nombre  FROM t4_categorias WHERE f4_activo = 1 ORDER BY f4_id_categoria";
            String[] datos = new String[2];

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {

                datos[0] = resultado.getString(1);
                datos[1] = resultado.getString(2);

                modeloTabla.addRow(datos);

            }
            listaCategorias.setModel(modeloTabla);
            declaracion.close();
            conexion1.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "NO se puede mostrar información, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void insertarCategoria(JTextField parNombre) {

        try {
            Connection conexion1 = conectar.crearConexion();

            setNombre(parNombre.getText());

            String sql = "INSERT INTO t4_categorias (f4_nombre) VALUES (?)";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setString(1, getNombre());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Información guardada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO guardada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void seleccionarCategoria(JTable listaCategorias, JTextField parNombre) {

        try {
            int fila = listaCategorias.getSelectedRow();

            if (fila >= 0) {

                parNombre.setText(listaCategorias.getValueAt(fila, 1).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    public void editarCategorias(JTable listaCategorias, JTextField parNombre) {

        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaCategorias.getSelectedRow();

            setId(Integer.parseInt(listaCategorias.getValueAt(fila, 0).toString()));
            setNombre(parNombre.getText());

            String sql = "UPDATE t4_categorias SET f4_nombre = ? WHERE f4_id_categoria = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setString(1, getNombre());
            cs.setInt(2, getId());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Información actualizada con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Información NO actualizada, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarCategoria(JTable listaCategorias) {
        try {
            Connection conexion1 = conectar.crearConexion();
            int fila = listaCategorias.getSelectedRow();

            setId(Integer.parseInt(listaCategorias.getValueAt(fila, 0).toString()));

            String sql = "UPDATE t4_categorias SET f4_activo = 0 WHERE f4_id_categoria = ?";
            CallableStatement cs = conexion1.prepareCall(sql);
            cs.setInt(1, getId());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Registro eliminado con éxito", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            conexion1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Registro NO eliminado, error: " + e.toString(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        public String imprimirLista(DefaultTableModel tabla){
        
        String lista = "";
        int n = tabla.getRowCount();
        int i = 0;
        while (i<n){
            lista=lista+"Título \""+tabla.getValueAt(i, 2)+"\" con "+tabla.getValueAt(i, 3)+ " películas\n";
            i=i+1;
        }
        return lista;
    }


    public DefaultTableModel validarEliminacionCategoria(JTable listaCategorias) {

        DefaultTableModel modeloTabla = new DefaultTableModel();
        try {
            Connection conexion1 = conectar.crearConexion();
            java.sql.Statement declaracion = conexion1.createStatement();
            int fila = listaCategorias.getSelectedRow();
            setId(Integer.parseInt(listaCategorias.getValueAt(fila, 0).toString()));
           
            modeloTabla.addColumn("Categoria");
            modeloTabla.addColumn("Titulo");
            modeloTabla.addColumn("Nombre titulo");
            modeloTabla.addColumn("Peliculas");

            String sql = "SELECT \n"
                    + "f4_id_categoria\n"
                    + ", f2_id_titulo\n"
                    + ", f2_nombre_titulo\n"
                    + ", count(*) peliculas\n"
                    + "FROM t4_categorias\n"
                    + "LEFT JOIN t2_titulos ON f2_id_categoria = f4_id_categoria\n"
                    + "LEFT JOIN t3_peliculas ON f3_id_titulo = f2_id_titulo\n"
                    + "WHERE f4_id_categoria = "+getId()+" and f4_activo = 1\n"
                    + "GROUP BY f4_id_categoria, f2_id_titulo, f2_nombre_titulo\n"
                    + "HAVING COUNT(*)>1";
            String[] datos = new String[4];
            var registros = new ArrayList<String[]>();

            ResultSet resultado = declaracion.executeQuery(sql);
            while (resultado.next()) {

                datos[0] = resultado.getString(1);
                datos[1] = resultado.getString(2);
                datos[2] = resultado.getString(3);
                datos[3] = resultado.getString(4);
                
                modeloTabla.addRow(datos);
                
            }
            declaracion.close();
            conexion1.close();
        } catch (Exception e) {
        }
        return modeloTabla;
    }
}
