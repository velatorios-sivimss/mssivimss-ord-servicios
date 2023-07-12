package com.imss.sivimss.ordservicios.util;

public class ConsultaConstantes {
	
	private ConsultaConstantes() {
		super();
	}

	public static String validar(String valor) {
		if (valor != null) {
			return valor;
		}
		return "";
	}

}
