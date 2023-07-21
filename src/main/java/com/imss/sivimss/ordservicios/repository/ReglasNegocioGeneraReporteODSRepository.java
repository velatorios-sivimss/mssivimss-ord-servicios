package com.imss.sivimss.ordservicios.repository;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.ReporteDto;

@Service
public class ReglasNegocioGeneraReporteODSRepository {


	private static final String JOIN = " JOIN ";
	
	public String generaReporteODSCU025(ReporteDto reporteDto) {
		String query ="SELECT DISTINCT(sos.ID_ORDEN_SERVICIO) "
				+ ",IFNULL(DATE_FORMAT(sos.FEC_ALTA,'%d/%m/%Y'),'') AS fechaods "
				+ ",IFNULL(sd.DES_DELEGACION,'') AS delegacion "
				+ ",IFNULL(sv.DES_VELATORIO,'') AS velatorio "
				+ ",IFNULL(stos.DES_TIPO_ORDEN_SERVICIO,'') AS tipoODS "
				+ ",CONCAT('$ ' ,IFNULL(FORMAT((SELECT SUM (sdcp1.IMP_IMPORTE) FROM SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO sdcp1 WHERE sdcp1.ID_CARACTERISTICAS_PRESUPUESTO = scp.ID_CARACTERISTICAS_PRESUPUESTO "
				+ " GROUP BY sdcp1.ID_CARACTERISTICAS_PRESUPUESTO),2),0.00)) AS montoTotalODS "
				+ ",IFNULL(seos.DES_ESTATUS,'') AS estatus "
				+ ",CONCAT('$ ', IFNULL(FORMAT(scp.CAN_PRESUPUESTO,2),0.00)) AS folioFactura "
				+ " FROM SVC_ORDEN_SERVICIO sos "
				+ JOIN + " SVC_CARACTERISTICAS_PRESUPUESTO scp  on scp.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO AND scp.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "
				+ JOIN + " SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO sdcp on sdcp.ID_CARACTERISTICAS_PRESUPUESTO = scp.ID_CARACTERISTICAS_PRESUPUESTO "
				+ JOIN + " SVC_VELATORIO sv ON sv.ID_VELATORIO = sos.ID_VELATORIO "
				+ JOIN + " SVC_DELEGACION sd ON sd.ID_DELEGACION = sv.ID_DELEGACION "
				+ JOIN + " SVC_FINADO sf ON sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "
				+ JOIN + " SVC_TIPO_ORDEN_SERVICIO stos ON stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN "
				+ JOIN + " SVC_ESTATUS_ORDEN_SERVICIO seos ON seos.ID_ESTATUS_ORDEN_SERVICIO = sos.ID_ESTATUS_ORDEN_SERVICIO ";
				if(reporteDto.getIdVelatorio() != null) {
					query = query + "WHERE sv.ID_VELATORIO = " + reporteDto.getIdVelatorio();
					if(reporteDto.getIdDelegacion() != null)
						query = query + " AND sd.ID_DELEGACION = " + reporteDto.getIdDelegacion();
					if(reporteDto.getIdTipoODS() != null)
						query = query + " AND stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
					if(reporteDto.getIdEstatusODS() != null)
						query = query + " AND seos.ID_ESTATUS_ORDEN_SERVICIO = " + reporteDto.getIdEstatusODS();
					if(reporteDto.getFechaIni() != null)
						query = query + " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFechaIni() + "' "; 
					if(reporteDto.getFechaFin() != null && reporteDto.getFechaIni() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "' )";
					else if(reporteDto.getFechaFin() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "'";
				}
				if(reporteDto.getIdDelegacion() != null) {
					query = query + "WHERE sd.ID_DELEGACION = " + reporteDto.getIdDelegacion();
					if(reporteDto.getIdTipoODS() != null)
						query = query + " AND stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
					if(reporteDto.getIdEstatusODS() != null)
						query = query + " AND seos.ID_ESTATUS_ORDEN_SERVICIO = " + reporteDto.getIdEstatusODS();
					if(reporteDto.getFechaIni() != null)
						query = query + " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFechaIni() + "' "; 
					if(reporteDto.getFechaFin() != null && reporteDto.getFechaIni() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "' )";
					else if(reporteDto.getFechaFin() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "'";
				}
				if(reporteDto.getIdTipoODS() != null) {
					query = query + " WHERE stos.ID_TIPO_ORDEN_SERVICIO = " + reporteDto.getIdTipoODS();
					if(reporteDto.getIdEstatusODS() != null)
						query = query + " AND seos.ID_ESTATUS_ORDEN_SERVICIO = " + reporteDto.getIdEstatusODS();
					if(reporteDto.getFechaIni() != null)
						query = query + " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFechaIni() + "' "; 
					if(reporteDto.getFechaFin() != null && reporteDto.getFechaIni() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "' )";
					else if(reporteDto.getFechaFin() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "'";
				}
				if(reporteDto.getIdEstatusODS() != null) {
					query = query + " WHERE seos.ID_ESTATUS_ORDEN_SERVICIO = " + reporteDto.getIdEstatusODS();
					if(reporteDto.getFechaIni() != null)
						query = query + " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFechaIni() + "' "; 
					if(reporteDto.getFechaFin() != null && reporteDto.getFechaIni() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "' )";
					else if(reporteDto.getFechaFin() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "'";
				}
				if(reporteDto.getFechaIni() != null) {
					query = query + " WHERE (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFechaIni() + "' "; 
					if(reporteDto.getFechaFin() != null && reporteDto.getFechaIni() != null)
						query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "' )";
					else 
						query = query + ")";
				}
				if(reporteDto.getFechaFin() != null) 
					query = query + " WHERE DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFechaIni() + "'"; 
		return query;
	}

}
