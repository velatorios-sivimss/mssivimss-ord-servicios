package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinadoResponse extends  Persona{

	private Integer idFinado;
	private Integer idTipoOrden;
	private String extremidad;
	private String esobito;
	private String matricula;
	private Integer idContratoPrevision;
	private Integer idVelatorioContratoPrevision;
	private DomicilioRequest cp;
	private String fechaDeceso;
	private String causaDeceso;
	private String lugarDeceso;
	private String hora;
	private String idClinicaAdscripcion;
	private String idUnidadProcedencia;
	private String procedenciaFinado;
	private Integer idTipoPension;
}
