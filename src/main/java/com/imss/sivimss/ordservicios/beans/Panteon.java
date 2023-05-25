package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.ordservicios.model.request.PanteonRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.QueryHelper;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class Panteon {
	
	public DatosRequest consultarPanteones(PanteonRequest panteonRequest) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQuery= new SelectQueryUtil();

		selectQuery.select("SP.ID_PANTEON AS idPanteon",
				"SP.DES_PANTEON AS nombrePanteon","STD.DES_CALLE AS desCalle","STD.NUM_EXTERIOR AS numExterior",
				"STD.NUM_INTERIOR AS numInterior","SP.NOM_CONTACTO AS nombreContacto","SP.NUM_TELEFONO AS numTelefono",
				"STD.DES_COLONIA desColonia","STD.DES_CP AS codigoPostal",
				"STD.DES_MUNICIPIO AS desMunicipio","STD.DES_ESTADO AS desEstado")
		.from("SVT_PANTEON SP")
		.join("SVT_DOMICILIO STD", "SP.ID_DOMICILIO = STD.ID_DOMICILIO")
		.where("SP.DES_PANTEON LIKE '%"+panteonRequest.getNombrePanteon()+"%'")
		.and("SP.IND_ACTIVO = 1")
		.orderBy("nombrePanteon ASC");
	
		String query=selectQuery.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
	
	public DatosRequest buscarPanteon(PanteonRequest panteonRequest) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQuery= new SelectQueryUtil();

		selectQuery.select("SP.ID_PANTEON AS idPanteon",
				"SP.DES_PANTEON AS nombrePanteon","STD.DES_CALLE AS desCalle","STD.NUM_EXTERIOR AS numExterior",
				"STD.NUM_INTERIOR AS numInterior","SP.NOM_CONTACTO AS nombreContacto","SP.NUM_TELEFONO AS numTelefono",
				"STD.DES_COLONIA desColonia","STD.DES_CP AS codigoPostal",
				"STD.DES_MUNICIPIO AS desMunicipio","STD.DES_ESTADO AS desEstado")
		.from("SVT_PANTEON SP")
		.join("SVT_DOMICILIO STD", "SP.ID_DOMICILIO = STD.ID_DOMICILIO")
		.where("SP.DES_PANTEON = '"+panteonRequest.getNombrePanteon()+"'")
		.and("SP.IND_ACTIVO = 1")
		.orderBy("nombrePanteon ASC");
	
		String query=selectQuery.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
	
	public DatosRequest insertar(PanteonRequest panteonRequest, UsuarioDto dto) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		
		final QueryHelper q= new QueryHelper("INSERT INTO SVT_PANTEON");
		q.agregarParametroValues("DES_PANTEON", "'"+panteonRequest.getNombrePanteon()+"'");
		q.agregarParametroValues("ID_DOMICILIO", "idTabla");
		q.agregarParametroValues("NOM_CONTACTO", "'"+panteonRequest.getNombreContacto()+"'");
		q.agregarParametroValues("NUM_TELEFONO", ""+panteonRequest.getNumTelefono()+"");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +dto.getIdUsuario() + "");
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
		String query;
		query=generarInsert(panteonRequest, dto)+" $$ "+ q.obtenerQueryInsertar();
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("separador", "$$");
        parametro.put("replace", "idTabla");
        request.setDatos(parametro);
		return request;
	}
	
	private String generarInsert(PanteonRequest panteonRequest, UsuarioDto dto) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'"+panteonRequest.getDomicilio().getDesCalle()+"'");
		q.agregarParametroValues("NUM_EXTERIOR", "'"+panteonRequest.getDomicilio().getNumExterior()+"'");
		q.agregarParametroValues("NUM_INTERIOR", "'"+panteonRequest.getDomicilio().getNumInterior()+"'");
		q.agregarParametroValues("DES_CP", ""+panteonRequest.getDomicilio().getCodigoPostal()+"");
		q.agregarParametroValues("DES_COLONIA", "'"+panteonRequest.getDomicilio().getDesColonia()+"'");
		q.agregarParametroValues("DES_MUNICIPIO", "'"+panteonRequest.getDomicilio().getDesColonia()+"'");
		q.agregarParametroValues("DES_ESTADO", "'"+panteonRequest.getDomicilio().getDesColonia()+"'");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +dto.getIdUsuario() + "");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
		return q.obtenerQueryInsertar();
	}
	
	
}
