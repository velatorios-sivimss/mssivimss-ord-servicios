package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imss.sivimss.ordservicios.beans.ordeservicio.Persona;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinadoRequest extends Persona{

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
