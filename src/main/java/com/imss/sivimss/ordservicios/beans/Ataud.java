package com.imss.sivimss.ordservicios.beans;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;

import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class Ataud {

	private static Ataud instancia;
	
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
		selectQueryUtil.select("STA.ID_ARTICULO AS idArticulo","STA.DES_MODELO_ARTICULO AS nombreArticulo")
		.from("SVT_ARTICULO STA")
		.innerJoin("SVC_CATEGORIA_ARTICULO SCA", "SCA.ID_CATEGORIA_ARTICULO = STA .ID_CATEGORIA_ARTICULO")
		//.innerJoin("SVT_INVENTARIO STI", "STA.ID_ARTICULO = STI .ID_ARTICULO")
		.innerJoin("SVC_TIPO_ARTICULO STA2", "STA .ID_TIPO_ARTICULO = STA2.ID_TIPO_ARTICULO ")
		.where("STA.ID_CATEGORIA_ARTICULO = 1")
		//.and("STI.CAN_STOCK > 0")
		.and("STA.IND_ACTIVO =1")
		.and("STA.ID_TIPO_ARTICULO =1");
		String query= selectQueryUtil.build();
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
		.innerJoin("SVT_CONTRATO sc", "sve.ID_CONTRATO = sve.ID_CONTRATO")
		.innerJoin("SVT_CONTRATO_ARTICULOS sca", "sc.ID_CONTRATO =sca.ID_CONTRATO")
		.innerJoin("SVT_ARTICULO sva", "svia.ID_ARTICULO = sva.ID_ARTICULO")
		.where("sva.ID_CATEGORIA_ARTICULO =1")
		.and("svia.ID_TIPO_ASIGNACION_ART = :idTipoAsignacion")
		.setParameter("idTipoAsignacion", idTipoAsignacion)
		.and("sva.CAN_UNIDAD > 0 ");
		if (idTipoAsignacion==5) {
			selectQueryUtil.and("sc.MON_MAX <= (".concat(selectQueryUtilCosto.build()).concat(")"));
		}
		return selectQueryUtil.build();
	}
	
	
}
