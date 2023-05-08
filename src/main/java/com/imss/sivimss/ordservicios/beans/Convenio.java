package com.imss.sivimss.ordservicios.beans;

import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Convenio {

	private ProviderServiceRestTemplate providerServiceRestTemplate;

	public Convenio(ProviderServiceRestTemplate providerServiceRestTemplate) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
	}
	
	public Response<?> peticionOrden(DatosRequest request,UsuarioDto usuario){
		return null;
	}
}
