package com.imss.sivimss.ordservicios.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AsignacionesAtaudRequest {
	List<Integer>asignaciones= new ArrayList<>();
}
