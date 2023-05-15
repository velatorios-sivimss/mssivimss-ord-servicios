package com.imss.sivimss.ordservicios.repository;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.util.SelectQueryUtil;

@Service
public class ReglasNegocioRepository {

	public String obtenerDatosContratanteRfc(String rfc) {
		String consulta="";
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("*")
		.from("SVC_PERSONA SPE")
		.where("SPE.CVE_RFC = :rfc")
		.setParameter("rfc", rfc);
		consulta=selectQueryUtil.build();
		return consulta;
	}

	public String obtenerDatosContratanteCurp(String curp) {
		SelectQueryUtil selectQueryUtil = new SelectQueryUtil();
		selectQueryUtil.select("*")
		.from("SVC_PERSONA SPE")
		.where("SPE.CVE_CURP = :curp")
		.setParameter("curp", curp);
		return selectQueryUtil.build();
	}

}
