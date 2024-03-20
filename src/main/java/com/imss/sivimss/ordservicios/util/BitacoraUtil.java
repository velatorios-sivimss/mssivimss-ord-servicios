package com.imss.sivimss.ordservicios.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitacoraUtil {

    private static ResultSet resultSet;
	private static Statement statement;

    private BitacoraUtil() {
    	
    }
	private static final Logger log = LoggerFactory.getLogger(BitacoraUtil.class);

	public static String consultarInformacion(Connection conne, String tabla, String condicion) throws SQLException {

        List<String> informacion = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM " + tabla + " WHERE " + condicion;
            statement=conne.createStatement();           
            resultSet = (ResultSet) statement.executeQuery(consulta);
            ResultSetMetaData rsMd = resultSet.getMetaData();
            //La cantidad de columnas que tiene la consulta
            int cantidadColumnas = rsMd.getColumnCount();
            int colum=1;
            //Establecer como cabezeras el nombre de las colimnas
            HashMap hashMap= new HashMap();
            while (resultSet.next()) {
                Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    hashMap.put(rsMd.getColumnLabel(colum), resultSet.getObject(i + 1));
                    colum++;
                }
                informacion.add(hashMap.toString());
            }
            if (!resultSet.next()) {
            	informacion.add("");
			}
            
            
        } catch (Exception e) {
           log.info("BitacoraUtil consultarInformacion {}", e.getMessage());
        } finally {
           
            if (resultSet!=null) {
                resultSet.close();
            }
            if (statement!=null) {
                statement.close();
            }
        }
        return informacion.get(0).toString();
    }
    
    public static void insertarInformacion(Connection conne, String tabla, Integer idTipoTransaccion, String datoAfectado, String datoActual,Integer idUsuario) throws SQLException {
        try {
        	String valor=datoAfectado;
        	if (valor==null || valor.equals("")) {
    			valor = "NULL";
    		}else {
    			valor = "'"+valor+"'";
    		}
            String insert = "INSERT INTO SVH_BITACORA (ID_TIPO_TRANSACCION, "
                    + "DES_TABLA, DES_DATO_AFECTADO, DES_DATO_ACTUAL, ID_USUARIO) "
                    + "VALUES("+idTipoTransaccion+", '"+tabla+"', "+valor+", '"+datoActual+"',"+idUsuario+");";
            statement=conne.createStatement();   
            statement.executeUpdate(insert,
					Statement.RETURN_GENERATED_KEYS);
            /*connection.prepareStatement(insert);
            statement.setInt(1, idTipoTransaccion);
            statement.setString(2, tabla);
            statement.setString(3, datoAfectado);
            statement.setString(4, datoActual);
            statement.setInt(5, idUsuario);
            statement.executeUpdate();*/
            
        } catch (Exception e) {
            log.info("BitacoraUtil insertarInformacion {}", e.getMessage());
        } finally {
        	 if (resultSet!=null) {
                 resultSet.close();
             }
             if (statement!=null) {
                 statement.close();
             }
        }
      
    }
    
}
