package com.imss.sivimss.ordservicios.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.CodigoPostalRequest;
import com.imss.sivimss.ordservicios.model.request.PanteonRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.QueryHelper;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@NoArgsConstructor
@Setter
@Getter
public class Panteon {
	@Value("${formato_fecha}")
	private String formatoFecha;
	
	public DatosRequest buscar(PanteonRequest panteonRequest) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		SelectQueryUtil selectQuery= new SelectQueryUtil();

		selectQuery.select("SP.ID_PANTEON AS idPanteon",
				"SP.NOM_PANTEON AS desPanteon","STD.DES_CALLE AS desCalle","STD.NUM_EXTERIOR AS numExterior",
				"STD.NUM_INTERIOR AS numInterior","SP.NOM_CONTACTO AS desContacto","SP.NUM_TELEFONO AS numTelefono",
				"STD.ID_CP AS idCodigoPostal",
				"STD.DES_COLONIA desColonia","SC.CVE_CODIGO_POSTAL AS codigoPostal",
				"SC.DES_MNPIO AS desMunicipio","SC.DES_ESTADO AS desEstado","SC.DES_CIUDAD AS desCiudad")
		.from("SVT_PANTEON SP")
		.join("SVT_DOMICILIO STD", "SP.ID_DOMICILIO = STD.ID_DOMICILIO")
		.join("SVC_CP SC", "STD.ID_CP = SC.ID_CODIGO_POSTAL")
		.where("SP.NOM_PANTEON LIKE '%"+panteonRequest.getDesPanteon()+"%'")
		.and("SP.IND_ACTIVO = 1")
		.orderBy("desPanteon ASC");
		
		String query=selectQuery.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
	
	public DatosRequest insertar(PanteonRequest panteonRequest, UsuarioDto dto) {
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro= new HashMap<>();
		
		final QueryHelper q= new QueryHelper("INSERT INTO SVT_PANTEON");
		q.agregarParametroValues("NOM_PANTEON", "'"+panteonRequest.getDesPanteon()+"'");
		q.agregarParametroValues("ID_DOMICILIO", "idTabla");
		q.agregarParametroValues("NOM_CONTACTO", "'"+panteonRequest.getDesContacto()+"'");
		q.agregarParametroValues("NUM_TELEFONO", ""+panteonRequest.getNumTelefono()+"");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +dto.getIdUsuario() + "");
		q.agregarParametroValues("IND_ACTIVO", "1");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
		String query;
		if (panteonRequest.getCp().getIdCodigoPostal()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "El codigo postal es obligatorio");
		} else {
			query=generarInsert(panteonRequest, dto)+" $$ "+ q.obtenerQueryInsertar();
		}
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("separador", "$$");
        parametro.put("replace", "idTabla");
        request.setDatos(parametro);
		return request;
	}
	
	private String generarInsert(PanteonRequest panteonRequest, UsuarioDto dto) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'"+panteonRequest.getDesCalle()+"'");
		q.agregarParametroValues("NUM_EXTERIOR", "'"+panteonRequest.getNumExterior()+"'");
		q.agregarParametroValues("NUM_INTERIOR", "'"+panteonRequest.getNumInterior()+"'");
		q.agregarParametroValues("ID_CP", ""+panteonRequest.getCp().getIdCodigoPostal()+"");
		q.agregarParametroValues("DES_COLONIA", "'"+panteonRequest.getNumInterior()+"'");
		q.agregarParametroValues("ID_USUARIO_ALTA", "" +dto.getIdUsuario() + "");
		//q.agregarParametroValues("CVE_ESTATUS", "1");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
		return q.obtenerQueryInsertar();
	}
	
	
}
