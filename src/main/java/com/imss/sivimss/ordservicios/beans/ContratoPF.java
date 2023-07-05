package com.imss.sivimss.ordservicios.beans;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imss.sivimss.ordservicios.model.request.ContratoPfRequest;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContratoPF {
	
	private static ContratoPF instancia;
	
	private String query;
	
	
	private static final Logger log = LoggerFactory.getLogger(ContratoPF.class);

	private ContratoPF() {}
	
	public static ContratoPF getInstancia() {
		if (instancia==null) {
			instancia= new ContratoPF();
		}
		
		return instancia;
	}

	public String consultarContratantesSiniestros(Integer idContrato) {
		SelectQueryUtil selectQueryUtilCountOrdenes= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilContrato= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilCo= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtilFrom= new SelectQueryUtil();
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		
		selectQueryUtilCountOrdenes.select("COUNT(SO.ID_ORDEN_SERVICIO )")
		.from("SVC_ORDEN_SERVICIO SO")
		.innerJoin("SVC_FINADO SF", "SF.ID_ORDEN_SERVICIO = SO.ID_ORDEN_SERVICIO")
		.innerJoin("SVT_CONVENIO_PF SCP2", "SCP2.ID_CONVENIO_PF = CONTRATO.ID_CONVENIO_PF AND SF.ID_TIPO_ORDEN =2 ")
		.where("SO.ID_CONTRATANTE_PF= CONTRATO.ID_CONTRATANTE")
		.and("SO.ID_ESTATUS_ORDEN_SERVICIO IN (4,5,6,7)")
		.and("SF.ID_CONTRATO_PREVISION ="+idContrato);
		
		selectQueryUtilContrato.select("SCP.ID_CONVENIO_PF", "SC.ID_PERSONA", "SC.ID_CONTRATANTE" , 
				"SCP.IND_TIPO_CONTRATACION", "SCP.ID_TIPO_PREVISION", "CONCAT(SP.NOM_PERSONA,' ',SP.NOM_PRIMER_APELLIDO,' ',SP.NOM_SEGUNDO_APELLIDO) AS CONTRATANTE")
		.from("SVT_CONVENIO_PF SCP")
		.innerJoin("SVT_CONTRATANTE_PAQUETE_CONVENIO_PF SCPCP", "SCPCP.ID_CONVENIO_PF = SCP.ID_CONVENIO_PF")
		.innerJoin("SVC_CONTRATANTE SC", "SC.ID_CONTRATANTE = SCPCP.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA=SC.ID_PERSONA ")
		.where("SCP.ID_CONVENIO_PF ="+idContrato)
		.and("SCPCP.IND_ACTIVO=1");
		
		selectQueryUtilCo.select("DISTINCT TF.ID_CONTRATO_PREVISION")
		.from("SVC_ORDEN_SERVICIO OD")
		.innerJoin("SVC_FINADO TF", "TF.ID_ORDEN_SERVICIO = OD.ID_ORDEN_SERVICIO")
		.where("TF.ID_CONTRATO_PREVISION ="+idContrato)
		.and("TF.ID_TIPO_ORDEN =2")
		.and("OD.ID_ESTATUS_ORDEN_SERVICIO IN (4,5,6,7)");
		
		selectQueryUtilFrom.select("CONTRATO.ID_CONTRATANTE","CONTRATO.ID_PERSONA", "CONTRATO.ID_TIPO_PREVISION", "CONTRATO.IND_TIPO_CONTRATACION","CONTRATO.CONTRATANTE",
				"("+selectQueryUtilCountOrdenes.build()+") AS numSiniestros")
		.from("("+selectQueryUtilContrato.build()+") CONTRATO")
		.leftJoin("("+selectQueryUtilCo.build()+") CO ","CO.ID_CONTRATO_PREVISION=CONTRATO.ID_CONVENIO_PF " );
		
		//query=selectQueryUtilFrom.build();
		//log.info(query);
		
		selectQueryUtil.select("TEMP.ID_CONTRATANTE AS idContratante","TEMP.ID_PERSONA", "TEMP.CONTRATANTE AS contratante",
				"CASE WHEN TEMP.ID_TIPO_PREVISION = 1 AND TEMP.numSiniestros < 2 "+
				"THEN 1 "+
				"WHEN TEMP.ID_TIPO_PREVISION = 2 "+
				"THEN 2 "+
				"ELSE 0	END AS validaSiniestro ",
				"TEMP.IND_TIPO_CONTRATACION AS idTipoContratacion")
		.from("("+selectQueryUtilFrom.build()+") TEMP");
		
		
	
		 query= selectQueryUtil.build();
		 log.info(query);

		return query;
	}
	
	public String consultarFolio(ContratoPfRequest contratoPfRequest){
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("STC.ID_CONVENIO_PF AS idContratoPF", "STC.ID_TIPO_PREVISION AS tipoPrevision", "STC.IND_TIPO_CONTRATACION AS idTipoContrato",
				"STC.ID_VELATORIO AS idVelatorio", "SV.DES_VELATORIO AS nombreVelatorio", "DATE_FORMAT(STC.FEC_VIGENCIA ,'%Y-%m-%d') AS vigencia")
		.from("SVT_CONVENIO_PF STC")
		.innerJoin("SVC_VELATORIO SV", "SV.ID_VELATORIO = STC.ID_VELATORIO")
		.where("STC.DES_FOLIO = :folio")
		.and("DATE_FORMAT(STC.FEC_VIGENCIA ,'YY-MM-DD') >= DATE_FORMAT(CURRENT_DATE(),'YY-MM-DD')")
		.and("STC.ID_ESTATUS_CONVENIO in (2,4) ")
		.setParameter("folio", contratoPfRequest.getFolio());
		 query= queryUtil.build();
		 log.info(query);
		return query;
	}

	public String consultarContratantesBeneficiarios(ContratoPfRequest contratoPfRequest){
		SelectQueryUtil queryUtil= new SelectQueryUtil();
		queryUtil.select("SP.ID_PERSONA as idPersona", "CONCAT(SP.NOM_PERSONA,' ',SP.NOM_PRIMER_APELLIDO,' ',SP.NOM_SEGUNDO_APELLIDO) AS nombreBeneficiario")
		.from("SVT_CONVENIO_PF STC")
		.innerJoin("SVT_CONTRATANTE_PAQUETE_CONVENIO_PF CPA", "CPA.ID_CONVENIO_PF = STC.ID_CONVENIO_PF")
		.innerJoin("SVT_CONTRATANTE_BENEFICIARIOS SCB", "SCB.ID_CONTRATANTE_PAQUETE_CONVENIO_PF = CPA.ID_CONTRATANTE_PAQUETE_CONVENIO_PF  ")
		.innerJoin("SVC_PERSONA SP", "SP.ID_PERSONA = SCB.ID_PERSONA ")
		.where("STC.ID_CONVENIO_PF = :idContrato")
		.and("CPA.ID_CONTRATANTE="+contratoPfRequest.getIdContratante())
		.setParameter("idContrato", contratoPfRequest.getIdContrato());
		query= queryUtil.build();
		log.info(query);
		return query;
	}
	
	public String consultarPersona(Integer idPersona){
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("SPC.ID_PERSONA AS idPersona",
				"IFNULL(SC.CVE_MATRICULA,'') AS matricula",
				"IFNULL(SPC.CVE_RFC,'') AS rfc",
				"IFNULL(SPC.CVE_CURP,'') AS curp",
				"IFNULL(SPC.CVE_NSS,'') AS nss",
				"IFNULL(SPC.NOM_PERSONA,'') AS nomPersona",
				"IFNULL(SPC.NOM_PRIMER_APELLIDO,'') AS primerApellido",
				"IFNULL(SPC.NOM_SEGUNDO_APELLIDO,'') AS segundoApellido",
				"IFNULL(SPC.NUM_SEXO,'') AS sexo",
				"IFNULL(SPC.DES_OTRO_SEXO,'') AS otroSexo",
				"SPC.FEC_NAC AS fechaNac",
				"(CASE WHEN SPC.ID_PAIS = NULL OR SPC.ID_PAIS = 119  THEN 1 ELSE 2 END) AS nacionalidad",
				"SPC.ID_PAIS AS idPais",
				"SPC.ID_ESTADO AS idEstado",
				"SPC.DES_TELEFONO AS telefono",
				"SPC.DES_CORREO AS correo")
		.from("SVC_PERSONA SPC")
		.leftJoin("SVC_CONTRATANTE SC", "SPC.ID_PERSONA =SC.ID_PERSONA")
		.where("SPC.ID_PERSONA = " + idPersona);

		query = selectQueryUtil.build();
		log.info(query);
		return query;
	}
	
	
	
}
