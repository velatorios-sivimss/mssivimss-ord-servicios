package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	
	private static final Logger log = LoggerFactory.getLogger(Panteon.class);

	
	public DatosRequest consultarPanteones(PanteonRequest panteonRequest) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQuery= new SelectQueryUtil();

		selectQuery.select("SP.ID_PANTEON AS idPanteon",
				"SP.DES_PANTEON AS nombrePanteon","STD.REF_CALLE AS desCalle","STD.NUM_EXTERIOR AS numExterior",
				"STD.NUM_INTERIOR AS numInterior","SP.NOM_CONTACTO AS nombreContacto","SP.NUM_TELEFONO AS numTelefono",
				"STD.REF_COLONIA desColonia","STD.REF_CP AS codigoPostal",
				"STD.REF_MUNICIPIO AS desMunicipio","STD.REF_ESTADO AS desEstado")
		.from("SVT_PANTEON SP")
		.join("SVT_DOMICILIO STD", "SP.ID_DOMICILIO = STD.ID_DOMICILIO")
		.where("SP.DES_PANTEON LIKE '%"+panteonRequest.getNombrePanteon()+"%'")
		.and("SP.IND_ACTIVO = 1")
		.orderBy("nombrePanteon ASC");
	
		String query=selectQuery.build();
		log.info(query);

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
				"SP.DES_PANTEON AS nombrePanteon","STD.REF_CALLE AS desCalle","STD.NUM_EXTERIOR AS numExterior",
				"STD.NUM_INTERIOR AS numInterior","SP.NOM_CONTACTO AS nombreContacto","SP.NUM_TELEFONO AS numTelefono",
				"STD.REF_COLONIA desColonia","STD.REF_CP AS codigoPostal",
				"STD.REF_MUNICIPIO AS desMunicipio","STD.REF_ESTADO AS desEstado")
		.from("SVT_PANTEON SP")
		.join("SVT_DOMICILIO STD", "SP.ID_DOMICILIO = STD.ID_DOMICILIO")
		.where("SP.DES_PANTEON = '"+panteonRequest.getNombrePanteon()+"'")
		.and("SP.IND_ACTIVO = 1")
		.orderBy("nombrePanteon ASC");
	
		String query=selectQuery.build();
		log.info(query);

		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
	
	public String insertar(PanteonRequest panteonRequest, UsuarioDto dto) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVT_PANTEON");
		q.agregarParametroValues("DES_PANTEON", "'"+panteonRequest.getNombrePanteon()+"'");
		q.agregarParametroValues("ID_DOMICILIO",  "'"+panteonRequest.getDomicilio().getIdDomicilio() +"'");
		q.agregarParametroValues("NOM_CONTACTO", "'"+panteonRequest.getNombreContacto()+"'");
		q.agregarParametroValues("NUM_TELEFONO", ""+panteonRequest.getNumTelefono()+"");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +dto.getIdUsuario() + "");
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_DATE()");
		String query=q.obtenerQueryInsertar();
		log.info(query);
				
		return query;
	}
	
	
	public DatosRequest buscarPanteonPorId(PanteonRequest panteonRequest) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQuery= new SelectQueryUtil();

		selectQuery.select("SP.ID_PANTEON AS idPanteon",
				"SP.DES_PANTEON AS nombrePanteon","STD.REF_CALLE AS desCalle","STD.NUM_EXTERIOR AS numExterior",
				"STD.NUM_INTERIOR AS numInterior","SP.NOM_CONTACTO AS nombreContacto","SP.NUM_TELEFONO AS numTelefono",
				"STD.REF_COLONIA desColonia","STD.REF_CP AS codigoPostal",
				"STD.REF_MUNICIPIO AS desMunicipio","STD.REF_ESTADO AS desEstado")
		.from("SVT_PANTEON SP")
		.join("SVT_DOMICILIO STD", "SP.ID_DOMICILIO = STD.ID_DOMICILIO")
		.where("SP.ID_PANTEON = '"+panteonRequest.getIdPanteon()+"'")
		.and("SP.IND_ACTIVO = 1")
		.orderBy("nombrePanteon ASC");
	
		String query=selectQuery.build();
		log.info(query);

		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
	
	
	
}
