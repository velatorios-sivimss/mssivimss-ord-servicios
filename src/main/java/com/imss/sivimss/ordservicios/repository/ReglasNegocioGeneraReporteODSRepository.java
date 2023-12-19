package com.imss.sivimss.ordservicios.repository;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.ReporteDto;

@Service
public class ReglasNegocioGeneraReporteODSRepository {


	private static final String JOIN = " JOIN ";
	private static final String LEFT_JOIN = " LEFT JOIN ";
	private static final String AND_SEOS_ID_ESTATUS = " AND seos.ID_ESTATUS_ORDEN_SERVICIO = ";
	private static final String AND_DATE_MAYOR = " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '";
	private static final String AND_DATE_MENOR = " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '";
	
	public String generaReporteODSCU025(ReporteDto reporteDto) {
		String query ="SELECT DISTINCT(sos.ID_ORDEN_SERVICIO) "
				+ ",IFNULL(DATE_FORMAT(sos.FEC_ALTA,'%d/%m/%Y'),'') AS fechaods "
				+ ",IFNULL(sd.DES_DELEGACION,'') AS delegacion "
				+ ",IFNULL(sv.DES_VELATORIO,'') AS velatorio "
				+ ",IFNULL(stos.DES_TIPO_ORDEN_SERVICIO,'') AS tipoODS "
				+ ",CONCAT('$ ' , IFNULL(FORMAT(sdcp.IMP_CARAC_PRESUP,2),0.00)) AS montoTotalODS "
				+ ",IFNULL(seos.DES_ESTATUS,'') AS estatus "
				+ ",IFNULL(sf2.ID_FACTURA,'') AS folioFactura "
				+ " FROM SVC_ORDEN_SERVICIO sos "
				+ LEFT_JOIN + " SVC_CARAC_PRESUPUESTO scp  on scp.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO AND scp.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "
				+ LEFT_JOIN + " SVC_DETALLE_CARAC_PRESUP sdcp on sdcp.ID_CARAC_PRESUPUESTO = scp.ID_CARAC_PRESUPUESTO "
				+ JOIN + " SVC_VELATORIO sv ON sv.ID_VELATORIO = sos.ID_VELATORIO "
				+ JOIN + " SVC_DELEGACION sd ON sd.ID_DELEGACION = sv.ID_DELEGACION "
				+ LEFT_JOIN + " SVC_FINADO sf ON sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "
				+ JOIN + " SVC_TIPO_ORDEN_SERVICIO stos ON stos.ID_TIPO_ORDEN_SERVICIO = sf.ID_TIPO_ORDEN "
				+ JOIN + " SVC_ESTATUS_ORDEN_SERVICIO seos ON seos.ID_ESTATUS_ORDEN_SERVICIO = sos.ID_ESTATUS_ORDEN_SERVICIO "
				+ LEFT_JOIN + " SVT_PAGO_BITACORA spb ON spb.ID_REGISTRO = sos.ID_ORDEN_SERVICIO "
				+ LEFT_JOIN +" SVC_FACTURA sf2 ON sf2.ID_PAGO = spb.ID_PAGO_BITACORA  AND sf2.CVE_FOLIO = sos.CVE_FOLIO "
		        + "WHERE sos.ID_ORDEN_SERVICIO > 0 ";
		
				if(reporteDto.getIdVelatorio() != null) 
					query += " AND sv.ID_VELATORIO = " + reporteDto.getIdVelatorio();
				if(reporteDto.getIdDelegacion() != null)
					query += " AND sd.ID_DELEGACION = " + reporteDto.getIdDelegacion();
				if(reporteDto.getIdTipoODS() != null)
					query += reporteDto.getIdTipoODS()==3?" AND stos.ID_TIPO_ORDEN_SERVICIO IN(1,2) ":" AND stos.ID_TIPO_ORDEN_SERVICIO = "+reporteDto.getIdTipoODS()+" ";
				if(reporteDto.getIdEstatusODS() != null)
					query += AND_SEOS_ID_ESTATUS + reporteDto.getIdEstatusODS();
				if(reporteDto.getFechaIni() != null)
					query += AND_DATE_MAYOR + reporteDto.getFechaIni() + "' "; 
				if(reporteDto.getFechaFin() != null)
					query += AND_DATE_MENOR + reporteDto.getFechaFin() + "'";
				if(reporteDto.getFechaIni() != null && reporteDto.getFechaFin()== null)
					query +=  ")";
				if(reporteDto.getFechaIni() != null && reporteDto.getFechaFin()!= null)
					query +=  ")";
				
			
		return query;
	}

	public String generaReporteODSCU086(ReporteDto reporteDto) {
		String query= "";
		if(reporteDto.getId_velatorio() != null) {
			query = "WHERE sos.ID_VELATORIO = " + reporteDto.getId_velatorio();
			if(reporteDto.getId_delegacion() != null)
				query = query + " AND  sd.ID_DELEGACION = " +reporteDto.getId_delegacion();
			if(reporteDto.getFecha_inicial() != null)
				query = query + " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFecha_inicial() + "'"; 
			if(reporteDto.getFecha_final() != null)
				query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFecha_final()+ "'";
			if (reporteDto.getFecha_inicial() != null )
				query = query + ")";
		}else if(reporteDto.getId_delegacion() != null) {
				query = query + " WHERE  sd.ID_DELEGACION = " +reporteDto.getId_delegacion();
				if(reporteDto.getFecha_inicial() != null)
					query = query + " AND (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFecha_inicial() + "'"; 
				if(reporteDto.getFecha_final() != null)
					query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFecha_final()+ "'";
				if (reporteDto.getFecha_inicial() != null )
					query = query + ")";
		} else if(reporteDto.getFecha_inicial() != null) {
				query = query + " WHERE (DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') >= '" + reporteDto.getFecha_inicial() + "'"; 
				if(reporteDto.getFecha_final() != null)
					query = query + " AND DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFecha_final()+ "'";
				if (reporteDto.getFecha_inicial() != null )
					query = query + ")";
		}else if(reporteDto.getFecha_final() != null)
				query = query + " WHERE DATE_FORMAT(sos.FEC_ALTA,'%Y-%m-%d') <= '" + reporteDto.getFecha_final()+ "'";
		query = query + " GROUP BY sos.CVE_FOLIO";
		return query;
	}
}
