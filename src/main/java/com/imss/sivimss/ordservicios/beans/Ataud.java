package com.imss.sivimss.ordservicios.beans;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Ataud {

	private static Ataud instancia;
	
	
	private static final Logger log = LoggerFactory.getLogger(Ataud.class);

	private Ataud() {}
	
	public static Ataud obtenerInstancia() {
		if (instancia == null) {
			instancia = new Ataud();
		}
		
		return instancia;
	}
	
	public DatosRequest obtenerAtaudes() {
		DatosRequest request = new DatosRequest();
		Map<String, Object>paramtero= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SA.ID_ARTICULO AS idArticulo, SA.DES_MODELO_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO SA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SA.ID_CATEGORIA_ARTICULO = SCA.ID_CATEGORIA_ARTICULO")
		.innerJoin("SVC_TIPO_ARTICULO STA", "SA .ID_TIPO_ARTICULO =STA.ID_TIPO_ARTICULO")
		.where("SA.ID_CATEGORIA_ARTICULO = 1")
		.and("SA.CAN_UNIDAD > 0")
		.and("SA.IND_ACTIVO = 1")
		.and("SA.ID_TIPO_ARTICULO =1");
		String query= selectQueryUtil.build();
		
		log.info(query);

		String encoded= DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		paramtero.put(AppConstantes.QUERY, encoded);
		request.setDatos(paramtero);
		return request;
	}
	
	/*public DatosRequest obtenerAtaudesTipoAsignacion(List<Integer> idTipoAsignaciones) {
		DatosRequest request = new DatosRequest();
		ReglasNegocioRepository reglasNegocioRepository= new ReglasNegocioRepository();
		Map<String, Object>paramtero= new HashMap<>();
		for (Integer idTipoAsignacion : idTipoAsignaciones) {
			switch (idTipoAsignacion) {
			case 1:
				paramtero.put("Consigando", reglasNegocioRepository.obtenerAtaudConsignado(idTipoAsignacion));
				break;
				
			case 3:
				paramtero.put("Donado", reglasNegocioRepository.obtenerAtaudDonado(idTipoAsignacion));
				break;
			case 5:
				paramtero.put("Economico", reglasNegocioRepository.obtenerAtaudEconomico(idTipoAsignacion));
				break;

			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_CONSULTAR);
			}
		}
		return request;
		
	}*/
	
	
	public String obtenerAtaudTipoAsignacion(Integer idTipoAsignacion) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilCosto= new SelectQueryUtil()
				.select("IFNULL(sps.TIP_PARAMETRO ,0)")
				.from("SVC_PARAMETRO_SISTEMA sps")
				.where("sps.DES_PARAMETRO='COSTO ATAUD'");
		selectQueryUtil.select("svia.ID_INVE_ARTICULO as idArticulo","CONCAT(svia.FOLIO_ARTICULO,'-',sva.DES_MODELO_ARTICULO) as nombreArticulo")
		.from("SVT_INVENTARIO_ARTICULO svia")
		.innerJoin("SVT_ORDEN_ENTRADA sve", "svia .ID_ODE =sve.ID_ODE")
		.innerJoin("SVT_CONTRATO sc", "sc.ID_CONTRATO = sve.ID_CONTRATO")
		.innerJoin("SVT_CONTRATO_ARTICULOS sca", "sc.ID_CONTRATO =sca.ID_CONTRATO")
		.innerJoin("SVT_ARTICULO sva", "svia.ID_ARTICULO = sva.ID_ARTICULO")
		.where("sva.ID_CATEGORIA_ARTICULO =1")
		.and("svia.ID_TIPO_ASIGNACION_ART = :idTipoAsignacion")
		.setParameter("idTipoAsignacion", idTipoAsignacion)
		.and("sva.CAN_UNIDAD > 0 ")
		.and("svia.IND_ESTATUS = 0");
		if (idTipoAsignacion==5) {
			selectQueryUtil.and("sc.MON_MAX <= (".concat(selectQueryUtilCosto.build()).concat(")"));
		}
		String query= selectQueryUtil.build();
		log.info(query);

		return query;
	}

	public DatosRequest obtenerProveedorAtaud(Integer idAtaudInventario) throws UnsupportedEncodingException {
		DatosRequest datosRequest= new DatosRequest();
		Map<String, Object>parametros= new HashMap<>();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select("SP.ID_PROVEEDOR as idProveedor","SP.NOM_PROVEEDOR as nombreProveedor")
		.from("SVT_INVENTARIO_ARTICULO SVIA")
		.innerJoin("SVT_ORDEN_ENTRADA SVE", "SVIA .ID_ODE =SVE.ID_ODE")
		.innerJoin("SVT_CONTRATO SC", "SVE.ID_CONTRATO = SVE.ID_CONTRATO")
		.innerJoin("SVT_PROVEEDOR SP", "SC.ID_PROVEEDOR = SP.ID_PROVEEDOR")
		.innerJoin("SVT_CONTRATO_ARTICULOS SCA", "SC.ID_CONTRATO =SCA.ID_CONTRATO")
		.innerJoin("SVT_ARTICULO SVA", "SVIA.ID_ARTICULO = SVA.ID_ARTICULO")
		.where("SVIA.ID_INVE_ARTICULO  = :idAtaudInventario")
		.setParameter("idAtaudInventario", idAtaudInventario)
		.and("SVA.CAN_UNIDAD > 0 ")
		.groupBy("nombreProveedor");
		
		String query = selectQueryUtil.encrypt(selectQueryUtil.build());
		String decoded=new String(DatatypeConverter.parseBase64Binary(query));
		log.info(decoded);
		parametros.put(AppConstantes.QUERY, query);
		datosRequest.setDatos(parametros);
		return datosRequest;
	}
	
	
}
