package com.imss.sivimss.ordservicios.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.OrdenesServicioUtil;
import com.imss.sivimss.ordservicios.model.request.SituarServicio;
import com.imss.sivimss.ordservicios.util.QueryHelper;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

@Service
public class ReglasNegocioAdministrarOperacionODSRepository {

	private static final String ID_USUARIO_ALTA = "ID_USUARIO_ALTA";

	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";

	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";

	private static final String IND_ACTIVO = "IND_ACTIVO";

	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	
	private String query;
	
	@Value("${formato_fecha}")
	private String fecha;
	
	private static final Logger log = LoggerFactory.getLogger(ReglasNegocioAdministrarOperacionODSRepository.class);

	public String insertarSituarServicio(SituarServicio situarServicio, Integer idUsuario) {
		QueryHelper queryHelper= new QueryHelper("INSERT INTO SVC_ORDENES_HISTORIAL_SERVICIOS ");
		queryHelper.addColumn("ID_ORDEN_SERVICIO", ""+situarServicio.getIdOrdenServicio()+"");
		queryHelper.addColumn("DES_CERTIFICADO",""+situarServicio.getIndCertificado()+"");
		queryHelper.addColumn("ID_TIPO_SERVICIO", ""+situarServicio.getIdTipoServicio()+"");
		queryHelper.addColumn("FEC_SERVICIO", OrdenesServicioUtil.setValor(situarServicio.getFechaSolicitud()));
		queryHelper.addColumn("DES_NOTAS", OrdenesServicioUtil.setValor(situarServicio.getDesNotas()));
		queryHelper.addColumn(ID_USUARIO_ALTA, ""+idUsuario+"");
	
		query=queryHelper.obtenerQueryInsertar();
		log.info(query);
		return query;
	}
	
	public String consultarTotalServiciosPorIdOrdenServicio(Integer idOrden) {
		SelectQueryUtil selectQueryUtilOrdenes= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilServicios= new SelectQueryUtil();
		
		selectQueryUtilServicios.select("STO.ID_TIPO_SERVICIO ")
		.from("SVC_ORDENES_HISTORIAL_SERVICIOS STO")
		.where("STO.ID_ORDEN_SERVICIO = "+idOrden)
		.and("STO.IND_ACTIVO =1");
		
		selectQueryUtilOrdenes.select("IFNULL(COUNT(STS.ID_TIPO_SERVICIO),0)")
		.from("SVC_ORDEN_SERVICIO ST")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = ST.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP", "SDCP.ID_CARACTERISTICAS_PRESUPUESTO = SCP.ID_CARACTERISTICAS_PRESUPUESTO ")
		.innerJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO = SDCP.ID_SERVICIO ")
		.innerJoin("SVC_TIPO_SERVICIO STS", "STS.ID_TIPO_SERVICIO = SS.ID_TIPO_SERVICIO")
		.where("ST.ID_ORDEN_SERVICIO="+idOrden)
		.and("ST.ID_ESTATUS_ORDEN_SERVICIO IN (2,3) ")
		.and("STS.ID_TIPO_SERVICIO NOT IN("+selectQueryUtilServicios.build()+")");

		query=selectQueryUtilOrdenes.build();
		log.info(query);
		return query;
	}
	
	public String consultarServiciosPorIdOrdenServicio(Integer idOrden) {
		SelectQueryUtil selectQueryUtilOrdenes= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilServicios= new SelectQueryUtil();
		
		selectQueryUtilServicios.select("STO.ID_TIPO_SERVICIO ")
		.from("SVC_ORDENES_HISTORIAL_SERVICIOS STO")
		.where("STO.ID_ORDEN_SERVICIO = "+idOrden)
		.and("STO.IND_ACTIVO =1");
		
		selectQueryUtilOrdenes.select("STS.ID_TIPO_SERVICIO AS idTipoServicio , STS.DES_TIPO_SERVICIO AS nombreServicio")
		.from("SVC_ORDEN_SERVICIO ST")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = ST.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP", "SDCP.ID_CARACTERISTICAS_PRESUPUESTO = SCP.ID_CARACTERISTICAS_PRESUPUESTO ")
		.innerJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO = SDCP.ID_SERVICIO ")
		.innerJoin("SVC_TIPO_SERVICIO STS", "STS.ID_TIPO_SERVICIO = SS.ID_TIPO_SERVICIO")
		.where("ST.ID_ORDEN_SERVICIO="+idOrden)
		.and("ST.ID_ESTATUS_ORDEN_SERVICIO IN (2,3) ")
		.and("STS.ID_TIPO_SERVICIO NOT IN("+selectQueryUtilServicios.build()+")");

		query=selectQueryUtilOrdenes.build();
		log.info(query);
		return query;
	}
	
	
	public String consultarContratanteFinado(String folio,Integer idVelatorio) {
		SelectQueryUtil selectQueryUtilOrdenes= new SelectQueryUtil();
	
	
		selectQueryUtilOrdenes.select(
				"DISTINCT STO.ID_ORDEN_SERVICIO AS idOrden",
				"IFNULL(CONCAT(SPC.NOM_PERSONA,' ', SPC.NOM_PRIMER_APELLIDO, ' ', SPC.NOM_SEGUNDO_APELLIDO),'') AS contratante",
				"IFNULL(CONCAT(SPF.NOM_PERSONA,' ', SPF.NOM_PRIMER_APELLIDO, ' ', SPF.NOM_SEGUNDO_APELLIDO),'') AS finado"
				)
		.from("SVC_ORDEN_SERVICIO STO ")
		.innerJoin("SVC_CONTRATANTE SC", "SC.ID_CONTRATANTE = STO.ID_CONTRATANTE ")
		.innerJoin("SVC_PERSONA SPC", "SPC.ID_PERSONA = SC.ID_PERSONA ")
		.innerJoin("SVC_FINADO STF", "STF.ID_ORDEN_SERVICIO = STO.ID_ORDEN_SERVICIO ")
		.leftJoin("SVC_PERSONA SPF", "SPF.ID_PERSONA = STF.ID_PERSONA  ")
		.where("STO.CVE_FOLIO = '"+folio+"'")
		.and("STO.ID_ESTATUS_ORDEN_SERVICIO IN (2) ")
		.and("STO.ID_VELATORIO = "+idVelatorio);
		
		query=selectQueryUtilOrdenes.build();
		log.info(query);
		return query;
	}
	
	public String consultarHistorialServicios(String folio, Integer idVelatorio) {
		SelectQueryUtil selectQueryUtilOrdenes= new SelectQueryUtil();
	
	
		selectQueryUtilOrdenes.select("SSO.ID_ORDENES_HISTORIAL_SERVICIO AS idHistorialServicio ",
				"SS.DES_NOM_SERVICIO AS nombreServicio  ",
				"DATE_FORMAT(SSO.FEC_SERVICIO,'"+fecha+" %H:%i:%s') AS fechaSolicitud",
				" SSO.DES_NOTAS AS desNotas", "SSO.IND_ACTIVO AS estatus")
		.from("SVC_ORDENES_HISTORIAL_SERVICIOS SSO ")
		.innerJoin("SVC_ORDEN_SERVICIO STO", "STO.ID_ORDEN_SERVICIO = SSO.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO SCP", "SCP.ID_ORDEN_SERVICIO = STO.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO SDCP", "SDCP.ID_CARACTERISTICAS_PRESUPUESTO = SCP.ID_CARACTERISTICAS_PRESUPUESTO ")
		.innerJoin("SVT_SERVICIO SS", "SS.ID_SERVICIO = SDCP.ID_SERVICIO ")
		.innerJoin("SVC_TIPO_SERVICIO STS", "STS.ID_TIPO_SERVICIO = SS.ID_TIPO_SERVICIO AND SSO.ID_TIPO_SERVICIO = STS.ID_TIPO_SERVICIO ")
		.where("STO.CVE_FOLIO = '"+folio+"'")
		.and("STO.ID_ESTATUS_ORDEN_SERVICIO IN (2) ")
		.and("SSO.IND_ACTIVO =1")
		.and("STO.ID_VELATORIO = "+idVelatorio);

		
		query=selectQueryUtilOrdenes.build();
		log.info(query);
		return query;
	}
	
	public String consultarRegistro(Integer idOrden, Integer idTipoServicio) {
		SelectQueryUtil selectQueryUtilOrdenes= new SelectQueryUtil();
	
	
		selectQueryUtilOrdenes.select("IFNULL(COUNT(SSO.ID_TIPO_SERVICIO),0)")
		.from("SVC_ORDENES_HISTORIAL_SERVICIOS SSO ")
		.where("SSO.ID_ORDEN_SERVICIO = "+idOrden)
		.and("SSO.IND_ACTIVO =1")
		.and("SSO.ID_TIPO_SERVICIO = "+idTipoServicio);

		
		query=selectQueryUtilOrdenes.build();
		log.info(query);
		return query;
	}
	
	// actualizar orden de servicio
	public String actualizarOrdenServicio(Integer idOrdenServicio, Integer idEstatus, Integer idUsuario) {
		final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO ");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO", "" + idEstatus + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "" + idUsuario + "");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere("ID_ORDEN_SERVICIO=" + idOrdenServicio);

		query = q.obtenerQueryActualizar();
		log.info(query);
		return query;
	}
	
	// actualizar historial de servicio
	public String actualizarHistorialOrdenServicio(Integer idHistorialServicio, Integer idEstatus, Integer idUsuario) {
			final QueryHelper q = new QueryHelper("UPDATE SVC_ORDENES_HISTORIAL_SERVICIOS ");
			q.agregarParametroValues(IND_ACTIVO, "" + idEstatus + "");
			q.agregarParametroValues("ID_USUARIO_BAJA", "" + idUsuario + "");
			q.agregarParametroValues("FEC_BAJA", CURRENT_TIMESTAMP);
			q.addWhere("ID_ORDENES_HISTORIAL_SERVICIO=" + idHistorialServicio);

			query = q.obtenerQueryActualizar();
			log.info(query);
			return query;
	}
}
