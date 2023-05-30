package com.imss.sivimss.ordservicios.repository;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

@Service
public class ReglasNegocioRepository {

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
	
	

}
