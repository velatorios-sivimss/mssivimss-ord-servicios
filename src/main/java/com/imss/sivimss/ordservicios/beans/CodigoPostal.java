package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class CodigoPostal {

	private static CodigoPostal instancia;

	private CodigoPostal() {
	}

	public static CodigoPostal getInstancia() {
		if (instancia == null) {
			instancia = new CodigoPostal();
		}
		return instancia;
	}
	
	
	public DatosRequest buscar(Integer codigo) {
		DatosRequest request= new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		SelectQueryUtil selectQuery= new SelectQueryUtil();
		/*selectQuery.select("SC.ID_CODIGO_POSTAL AS id","SC.CVE_CODIGO_POSTAL AS cp",
				"SC.DES_COLONIA AS colonia","SC.DES_MNPIO AS municipio","SC.DES_ESTADO AS estado")
		.from("SVC_CP SC")
		.where("SC.CVE_CODIGO_POSTAL = :codigoPostal LIMIT 1;")
		.setParameter("codigoPostal", codigo);*/
		
		String query= "SELECT SC.ID_CODIGO_POSTAL AS id, SC.CVE_CODIGO_POSTAL AS cp ,SC.DES_COLONIA AS colonia, SC.DES_MNPIO AS municipio, SC.DES_ESTADO AS estado "
				+ "FROM SVC_CP SC "
				+ "WHERE SC.CVE_CODIGO_POSTAL = "+codigo+ " GROUP BY SC.DES_COLONIA "
				+ "ORDER BY SC.DES_COLONIA ASC LIMIT 1; ";
		//String query=selectQuery.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
		
	}

}
