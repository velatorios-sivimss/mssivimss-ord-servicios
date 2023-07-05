package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreType(value = true)
public class PersonaPfResponse {

	private Integer idPersona;
	private String matricula;
	private String rfc;
	private String curp;
	private String nss;
	private String nomPersona;
	private String primerApellido;
	private String segundoApellido;
	private String sexo;
	private String otroSexo;
	private String fechaNac;
	private String nacionalidad;
	private String idPais;
	private String idEstado;
	private String telefono;
	private String correo;
	

}
