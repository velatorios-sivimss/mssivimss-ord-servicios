package com.imss.sivimss.ordservicios.beans;


import java.util.HashMap;
import java.util.Map;

import com.imss.sivimss.ordservicios.model.request.ReportePagoProveedorDto;
import com.imss.sivimss.ordservicios.util.AppConstantes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportePagoProveedor {
	
	
	public Map<String, Object> generarReporte(ReportePagoProveedorDto reporte, String reportePagoProv) {
		Map<String, Object> envioDatos = new HashMap<>();
		StringBuilder condition= new StringBuilder();
		if(reporte.getId_delegacion()!=null) {
			condition.append(" AND SVEL.ID_DELEGACION = "+reporte.getId_delegacion()+"");
		}
		if(reporte.getId_velatorio()!=null) {
			condition.append(" AND SVEL.ID_VELATORIO = "+reporte.getId_velatorio()+"");
		}
		if(reporte.getFecha_inicial()!=null) {
			condition.append(" AND SOS.FEC_ALTA >= '"+reporte.getFecInicioConsulta()+ "'");
			envioDatos.put("fecInicio", reporte.getFecha_inicial());
		}
		if(reporte.getFecha_final()!=null) {
			condition.append(" AND SOS.FEC_ALTA <= '"+reporte.getFecFinConsulta()+"'");
			envioDatos.put("fecFin", reporte.getFecha_final());
		}
		//condition.append(" ORDER BY SOS.FEC_ALTA ASC");
		log.info("reporte -> "+condition.toString());
		envioDatos.put("condition", condition.toString());
		envioDatos.put("rutaNombreReporte", reportePagoProv);
		envioDatos.put("tipoReporte", reporte.getTipoReporte());
		if(reporte.getTipoReporte().equals("xls")) { 
			envioDatos.put("IS_IGNORE_PAGINATION", true); 
			}
		return envioDatos;
	}
	

}
