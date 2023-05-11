package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@JsonIgnoreType(value = true)
public class PromotorResponse {
	
	private Integer idPromotor;
	
	private String nombrePromotor;
}
