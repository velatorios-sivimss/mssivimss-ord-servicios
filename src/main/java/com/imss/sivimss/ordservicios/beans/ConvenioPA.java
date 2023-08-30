package com.imss.sivimss.ordservicios.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

public class ConvenioPA {

	private static ConvenioPA convenioPA;
	
	private String query;
	
	
	private static final Logger log = LoggerFactory.getLogger(ConvenioPA.class);

	private ConvenioPA () {
		
	}
	
	public static ConvenioPA getInstance() {
		if (convenioPA==null) {
			convenioPA= new ConvenioPA();
		}
		return convenioPA;
	}
	
	public String obtenerTitularConvenio(String folio) {
		SelectQueryUtil selectQueryUtil= new SelectQueryUtil();
		selectQueryUtil.select(
				"SPSA.ID_PLAN_SFPA AS idConvenioPa",
				"SPSA.NUM_FOLIO_PLAN_SFPA AS folio",
				"SV.ID_VELATORIO AS idVelatorio",
				"SV.DES_VELATORIO AS nombreVelatorio",
				"SPC.ID_PERSONA AS idPersona",
				"SC.ID_CONTRATANTE AS idContratante",
				"IFNULL(SC.CVE_MATRICULA,'') AS matricula",
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
				"IFNULL(SC.ID_DOMICILIO,'') AS idDomicilio",
				"IFNULL(SVD.DES_CALLE,'') AS calle",
				"IFNULL(SVD.NUM_EXTERIOR,'') AS numExterior",
				"IFNULL(SVD.NUM_INTERIOR,'') AS numInterior",
				"IFNULL(SVD.DES_CP,'') AS cp",
				"IFNULL(SVD.DES_COLONIA,'') AS colonia",
				"IFNULL(SVD.DES_MUNICIPIO,'') AS municipio",
				"IFNULL(SVD.DES_ESTADO,'') AS estado")
		.from("SVT_PLAN_SFPA SPSA ")
		.innerJoin("SVC_VELATORIO SV ", "SPSA.ID_VELATORIO = SV.ID_VELATORIO ")
		.innerJoin("SVC_CONTRATANTE SC ", "SPSA.ID_TITULAR = SC.ID_CONTRATANTE ")
		.innerJoin("SVC_PERSONA SPC ", "SC.ID_PERSONA = SPC.ID_PERSONA ")
		.innerJoin("SVT_DOMICILIO SVD  ", "SC.ID_DOMICILIO = SVD.ID_DOMICILIO ")
		.where("SPSA.ID_ESTATUS_PLAN_SFPA = 4 ")
		.and("SPSA .NUM_FOLIO_PLAN_SFPA ='"+folio+"'");
		
		query=selectQueryUtil.build();
		log.info(query);
		return query;
	}
}
