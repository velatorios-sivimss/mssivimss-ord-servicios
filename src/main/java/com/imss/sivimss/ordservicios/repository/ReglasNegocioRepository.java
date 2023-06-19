package com.imss.sivimss.ordservicios.repository;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;
import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.FinadoRequest;
import com.imss.sivimss.ordservicios.model.request.PersonaRequest;
import com.imss.sivimss.ordservicios.util.QueryHelper;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

@Service
public class ReglasNegocioRepository {

	private static final String ID_USUARIO_ALTA="ID_USUARIO_ALTA";
	
	private static final String FEC_ALTA="FEC_ALTA";
	
	public String obtenerDatosContratanteRfc(String rfc) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("SVC.ID_CONTRATANTE AS idContratante",
				"IFNULL(SPE.CVE_RFC,'') AS rfc",
				"IFNULL(SPE.CVE_CURP,'') AS curp",
				"IFNULL(SPE.CVE_NSS,'') AS nss",
				"IFNULL(SPE.NOM_PERSONA,'') AS nombre",
				"IFNULL(SPE.NOM_PRIMER_APELLIDO,'') AS primerApellido",
				"IFNULL(SPE.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido",
				"IFNULL(SPE.NUM_SEXO,'') AS sexo","IFNULL(SPE.DES_OTRO_SEXO,'') AS otroSexo",
				"IFNULL(SPE .FEC_NAC,'') AS fechaNac","IFNULL(SVP.ID_PAIS,'') AS idPais","IFNULL(SVE.ID_ESTADO,'') AS idEstado",
				"IFNULL(SPE.DES_TELEFONO,'') AS telefono","IFNULL(SPE.DES_CORREO,'') AS correo",
				"IFNULL(SVD.DES_CALLE,'') AS calle",
				"IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
				"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior",
				"IFNULL(SVD.DES_CP,'') AS cp",
				"IFNULL(SVD.DES_COLONIA,'') AS colonia",
				"IFNULL(SVD.DES_MUNICIPIO,'') AS municipio",
				"IFNULL(SVD.DES_ESTADO,'') AS estado")
		.from("SVC_PERSONA SPE")
		.leftJoin("SVC_PAIS SVP", "SPE.ID_PAIS = SVP.ID_PAIS")
		.leftJoin("SVC_ESTADO SVE", "SPE.ID_ESTADO = SVE.ID_ESTADO")
		.innerJoin("SVC_CONTRATANTE SVC", "SPE.ID_PERSONA = SVC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SVD", "SVC.ID_DOMICILIO = SVD.ID_DOMICILIO")
		.where("SPE.CVE_RFC = :rfc")
		.setParameter("rfc", rfc);
		
		return selectQueryUtil.build();
	}

	public String obtenerDatosContratanteCurp(String curp) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("SVC.ID_CONTRATANTE AS idContratante",
				"IFNULL(SPE.CVE_RFC,'') AS rfc",
				"IFNULL(SPE.CVE_CURP,'') AS curp",
				"IFNULL(SPE.CVE_NSS,'') AS nss",
				"IFNULL(SPE.NOM_PERSONA,'') AS nombre",
				"IFNULL(SPE.NOM_PRIMER_APELLIDO,'') AS primerApellido",
				"IFNULL(SPE.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido",
				"IFNULL(SPE.NUM_SEXO,'') AS sexo","IFNULL(SPE.DES_OTRO_SEXO,'') AS otroSexo",
				"IFNULL(SPE .FEC_NAC,'') AS fechaNac","IFNULL(SVP.ID_PAIS,'') AS idPais","IFNULL(SVE.ID_ESTADO,'') AS idEstado",
				"IFNULL(SPE.DES_TELEFONO,'') AS telefono","IFNULL(SPE.DES_CORREO,'') AS correo",
				"IFNULL(SVD.DES_CALLE,'') AS calle",
				"IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
				"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior",
				"IFNULL(SVD.DES_CP,'') AS cp",
				"IFNULL(SVD.DES_COLONIA,'') AS colonia",
				"IFNULL(SVD.DES_MUNICIPIO,'') AS municipio",
				"IFNULL(SVD.DES_ESTADO,'') AS estado")
		.from("SVC_PERSONA SPE")
		.leftJoin("SVC_PAIS SVP", "SPE.ID_PAIS = SVP.ID_PAIS")
		.leftJoin("SVC_ESTADO SVE", "SPE.ID_ESTADO = SVE.ID_ESTADO")
		.innerJoin("SVC_CONTRATANTE SVC", "SPE.ID_PERSONA = SVC.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO SVD", "SVC.ID_DOMICILIO = SVD.ID_DOMICILIO")
		.where("SPE.CVE_CURP = :curp")
		.setParameter("curp", curp);
		return selectQueryUtil.build();
	}
	
	// obtener folio
	public String obtenerFolio(Integer idVelatorio) {
		String query="";
		SelectQueryUtil velatorio= new SelectQueryUtil();
		velatorio.select("SUBSTRING(sv.DES_VELATORIO ,1,3)")
		.from("SVC_VELATORIO sv")
		.where("sv.ID_VELATORIO = :idVelatorio")
		.setParameter("idVelatorio", idVelatorio);
		SelectQueryUtil ordenServicio= new SelectQueryUtil();
		ordenServicio.select("COUNT(*)")
		.from("SVC_ORDEN_SERVICIO")
		.where("ID_VELATORIO = :idVelatorio")
		.setParameter("idVelatorio", idVelatorio);
		
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("CONCAT(("+velatorio.build()+")","'-'","LPAD(("+ordenServicio.build()+"),7,'0')"+") as folio")
		.from("dual");
		query=selectQueryUtil.build();
		return query;
	}
	// insertar persona
	public String insertarPersona(
			Persona personaRequest, Integer idUsuarioAlta) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVC_PERSONA");
		q.agregarParametroValues("CVE_RFC", "'"+personaRequest.getRfc()+"'");
		q.agregarParametroValues("CVE_CURP", "'"+personaRequest.getCurp()+"'");
		q.agregarParametroValues("CVE_NSS", "'"+personaRequest.getNss()+"'");
		q.agregarParametroValues("NOM_PERSONA", "'"+personaRequest.getNomPersona()+"'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'"+personaRequest.getPrimerApellido()+"'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'"+personaRequest.getSegundoApellido()+"'");
		q.agregarParametroValues("NUM_SEXO", ""+personaRequest.getSexo()+"");
		q.agregarParametroValues("DES_OTRO_SEXO", "'"+personaRequest.getOtroSexo()+"'");
		q.agregarParametroValues("FEC_NAC", "'"+personaRequest.getFechaNac()+"'");
		q.agregarParametroValues("ID_PAIS", ""+personaRequest.getIdPais()+"");
		q.agregarParametroValues("ID_ESTADO", ""+personaRequest.getIdEstado()+"");
		q.agregarParametroValues("DES_TELEFONO", "'"+personaRequest.getTelefono()+"'");
		q.agregarParametroValues("DES_CORREO", "'"+personaRequest.getCorreo()+"'");
		q.agregarParametroValues(ID_USUARIO_ALTA, ""+idUsuarioAlta+"");
		q.agregarParametroValues(FEC_ALTA, "CURRENT_TIMESTAMP()");
		return q.obtenerQueryInsertar();
	}
	// insertar contratante
	public String insertarContratante(ContratanteRequest contratanteRequest, Integer idUsuarioAlta) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", ""+contratanteRequest.getIdPersona()+"");
		q.agregarParametroValues("CVE_MATRICULA", "'"+contratanteRequest.getMatricula()+"'");
		q.agregarParametroValues("ID_DOMICILIO", ""+contratanteRequest.getCp().getIdDomicilio()+"");
		q.agregarParametroValues(ID_USUARIO_ALTA, ""+idUsuarioAlta+"");
		q.agregarParametroValues(FEC_ALTA, "CURRENT_TIMESTAMP()");
		return q.obtenerQueryInsertar();
	}
	
	public String insertarDomicilio(DomicilioRequest domicilioRequest,Integer idUsuarioAlta) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'"+domicilioRequest.getDesCalle()+"'");
		q.agregarParametroValues("NUM_EXTERIOR",  "'"+domicilioRequest.getNumExterior()+"'");
		q.agregarParametroValues("NUM_INTERIOR", "'"+domicilioRequest.getNumInterior()+"'");
		q.agregarParametroValues("DES_CP",  "'"+domicilioRequest.getCodigoPostal()+"'");
		q.agregarParametroValues("DES_COLONIA",  "'"+domicilioRequest.getDesColonia()+"'");
		q.agregarParametroValues("DES_MUNICIPIO",  "'"+domicilioRequest.getDesMunicipio()+"'");
		q.agregarParametroValues("DES_ESTADO",  "'"+domicilioRequest.getDesEstado()+"'");
		q.agregarParametroValues(ID_USUARIO_ALTA,  ""+idUsuarioAlta+"");
		q.agregarParametroValues(FEC_ALTA, "CURRENT_TIMESTAMP()");
		return q.obtenerQueryInsertar();
	}
	
	// insertar orden de servicio
	public String insertarOrdenServicio(String folio, Integer idContratante, Integer idParentesco, Integer idVelatorio,	Integer idEstatus,Integer idUsuarioAlta) {
		final QueryHelper q= new QueryHelper("INSERT INTO SVC_ORDEN_SERVICIO");
		q.agregarParametroValues("CVE_FOLIO",  "'"+folio+"'");
		q.agregarParametroValues("ID_CONTRATANTE",  ""+idContratante+"");
		q.agregarParametroValues("ID_PARENTESCO",  ""+idParentesco+"");
		q.agregarParametroValues("ID_VELATORIO",  ""+idVelatorio+"");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO",  ""+idEstatus+"");
		q.agregarParametroValues(ID_USUARIO_ALTA,  ""+idUsuarioAlta+"");
		q.agregarParametroValues(FEC_ALTA, "CURRENT_TIMESTAMP()");
		return q.obtenerQueryInsertar();
	}
	
	// insertar finado
	public String insertarFinado(FinadoRequest finadoRequest,Integer idOrdenServicio, Integer idUsuarioAlta) {
			final QueryHelper q= new QueryHelper("INSERT INTO SVC_FINADO");
			q.agregarParametroValues("ID_PERSONA", ""+finadoRequest.getIdPersona()+"");
			q.agregarParametroValues("ID_TIPO_ORDEN", ""+finadoRequest.getIdTipoOrden()+"");
			q.agregarParametroValues("DES_EXTREMIDAD", "'"+finadoRequest.getExtremidad()+"'");
			q.agregarParametroValues("DES_OBITO", "'"+finadoRequest.getEsobito()+"'");
			q.agregarParametroValues("CVE_MATRICULA", "'"+finadoRequest.getMatricula()+"'");
			q.agregarParametroValues("ID_CONTRATO_PREVISION", ""+finadoRequest.getIdContratoPrevision()+"");
			q.agregarParametroValues("ID_DOMICILIO", ""+finadoRequest.getCp().getIdDomicilio()+"");
			q.agregarParametroValues("FEC_DECESO", "'"+finadoRequest.getFechaDeceso()+"'");
			q.agregarParametroValues("DES_CAUSA_DECESO", "'"+finadoRequest.getCausaDeceso()+"'");
			q.agregarParametroValues("DES_LUGAR_DECESO", "'"+finadoRequest.getLugarDeceso()+"'");
			q.agregarParametroValues("TIM_HORA", "'"+finadoRequest.getHora()+"'");
			q.agregarParametroValues("ID_CLINICA_ADSCRIPCION",""+finadoRequest.getIdClinicaAdscripcion()+"");
			q.agregarParametroValues("ID_UNIDAD_PROCEDENCIA", ""+finadoRequest.getIdUnidadProcedencia()+"");
			q.agregarParametroValues("DES_PROCEDENCIA_FINADO", "'"+finadoRequest.getProcedenciaFinado()+"'");
			q.agregarParametroValues("ID_TIPO_PENSION", ""+finadoRequest.getIdTipoPension()+"");
			q.agregarParametroValues("ID_ORDEN_SERVICIO",""+idOrdenServicio+"");
			q.agregarParametroValues(ID_USUARIO_ALTA, ""+idUsuarioAlta+"");
			q.agregarParametroValues(FEC_ALTA, "CURRENT_TIMESTAMP()");
			return q.obtenerQueryInsertar();
	}
	// insertar caracteristicas paquete temp
	// insertar detalle caracteristicas paquete temp
	// insertar caracteristicas paquete traslado temp
	// insertar informacion servicio
	// insertar informacion servicio velacion
	
	

}
