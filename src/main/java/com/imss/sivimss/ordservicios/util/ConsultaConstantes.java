package com.imss.sivimss.ordservicios.util;

public class ConsultaConstantes {
	
	public static final String ELT_MONTH_NOW_ENERO_FEBRERO_MARZO_ABRIL_MAYO_JUNIO_JULIO_AGOSTO_SEPTIEMBRE_OCTUBRE_NOVIEMBRE_DICIEMBRE_AS_MES = "ELT(MONTH(NOW()), \"ENERO\", \"FEBRERO\", \"MARZO\", \"ABRIL\", \"MAYO\", \"JUNIO\", \"JULIO\", \"AGOSTO\", \"SEPTIEMBRE\", \"OCTUBRE\", \"NOVIEMBRE\", \"DICIEMBRE\") as mes";
	public static final String CONCAT_WS_SV_DES_VELATORIO_SD_DES_DELEGACION_AS_LUGAR = "CONCAT_WS(',',SV.DES_VELATORIO,SD.DES_DELEGACION) AS lugar";
	public static final String SU_CVE_MATRICULA_AS_CLAVE_ADMINISTRADOR = "SU.CVE_MATRICULA AS claveAdministrador";
	public static final String CONCAT_WS_SU_NOM_USUARIO_SU_NOM_APELLIDO_PATERNO_SU_NOM_APELLIDO_MATERNO_AS_NOM_ADMINISTRADOR = "CONCAT_WS(' ',SU.NOM_USUARIO,SU.NOM_APELLIDO_PATERNO,SU.NOM_APELLIDO_MATERNO) AS nomAdministrador";
	public static final String STA_CVE_FOLIO_ARTICULO_AS_NUM_INVENTARIOS = "STA. CVE_FOLIO_ARTICULO AS numInventarios";
	public static final String TM_DES_TIPO_MATERIAL_AS_TIPO_ATAUD = "TM.DES_TIPO_MATERIAL AS tipoAtaud";
	public static final String SV_ID_VELATORIO_AS_VELATORIO_ID = "SV.ID_VELATORIO AS velatorioId";
	public static final String SD_DES_DELEGACION_AS_OOAD_NOM = "SD.DES_DELEGACION AS ooadNom";
	public static final String STVA_ID_TIPO_MATERIAL_TM_ID_TIPO_MATERIAL = "STVA.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL";
	public static final String SPE_ID_PERSONA_SC_ID_PERSONA = "SPE.ID_PERSONA = SC.ID_PERSONA";
	public static final String STVA_ID_CATEGORIA_ARTICULO_1 = "STVA.ID_CATEGORIA_ARTICULO = 1";
	public static final String SVT_ARTICULO_STVA = "SVT_ARTICULO STVA";
	public static final String SVC_TIPO_MATERIAL_TM = "SVC_TIPO_MATERIAL TM";
	public static final String SVT_INVENTARIO_ARTICULO_STA = "SVT_INVENTARIO_ARTICULO STA";
	public static final String SPE2_ID_PERSONA_STF_ID_PERSONA = "SPE2.ID_PERSONA =STF.ID_PERSONA";
	public static final String STF_ID_ORDEN_SERVICIO_SOS_ID_ORDEN_SERVICIO = "STF.ID_ORDEN_SERVICIO = SOS.ID_ORDEN_SERVICIO";
	public static final String SVC_FINADO_STF = "SVC_FINADO STF";
	public static final String SVC_DELEGACION_SD = "SVC_DELEGACION SD";
	public static final String SD_ID_DELEGACION_SV_ID_DELEGACION = "SD.ID_DELEGACION = SV.ID_DELEGACION";
	public static final String SOS_ID_VELATORIO_SV_ID_VELATORIO = "SOS.ID_VELATORIO = SV.ID_VELATORIO";
	public static final String SVC_PERSONA_SPE2 = "SVC_PERSONA SPE2";
	public static final String SOS_ID_ORDEN_SERVICIO_ID_ORDEN_SERVICIO = "SOS.ID_ORDEN_SERVICIO = :idOrdenServicio";
	public static final String SC_ID_CONTRATANTE_SOS_ID_CONTRATANTE = "SC.ID_CONTRATANTE = SOS.ID_CONTRATANTE";
	public static final String SU_ID_USUARIO_SV_ID_USUARIO_ADMIN = "SU.ID_USUARIO = SV.ID_USUARIO_ADMIN";
	public static final String SVC_ORDEN_SERVICIO_SOS = "SVC_ORDEN_SERVICIO SOS";
	public static final String ID_ORDEN_SERVICIO = "idOrdenServicio";
	public static final String SVT_USUARIOS_SU = "SVT_USUARIOS SU";
	public static final String SVC_PERSONA_SPE = "SVC_PERSONA SPE";
	public static final String RESPONSABLE_ALMACEN = "responsableAlmacen";
	public static final String SVC_CONTRATANTE_SC = "SVC_CONTRATANTE SC";
	public static final String YEAR_NOW_AS_ANIO = "YEAR(NOW()) as anio";
	public static final String SVC_VELATORIO_SV = "SVC_VELATORIO SV";
	public static final String DAY_NOW_AS_DIA = "DAY(NOW()) as dia";
	public static final String TIPO_REPORTE = "tipoReporte";
	
	public ConsultaConstantes() {
		super();
	}

	public static String validar(String valor) {
		if (valor != null) {
			return valor;
		}
		return "";
	}

}
