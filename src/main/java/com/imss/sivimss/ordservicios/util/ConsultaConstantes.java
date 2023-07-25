package com.imss.sivimss.ordservicios.util;

public class ConsultaConstantes {
	
	public static final String RESPONSABLE_ALMACEN = "responsableAlmacen";
	public static final String SVC_CONTRATANTE_SC = "SVC_CONTRATANTE SC";
	public static final String SVC_VELATORIO_SV = "SVC_VELATORIO SV";
	public static final String TIPO_REPORTE = "tipoReporte";
	
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
