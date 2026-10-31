package com.mx.cbtis.gestor.dto;

import com.mx.cbtis.gestor.modelo.Asignaciones;

/**
 * Datos que recibe la API para Asignaciones. Se usa en lugar de la entidad JPA para que el cliente
 * no pueda asignar directamente campos de la entidad persistente (regla java:S4684).
 */
public record AsignacionesDto(
		Integer id_asignacion,
		Integer id_usuario,
		Integer id_tarea,
		String fecha_asignacion) {

	public Asignaciones toEntity() {
		return new Asignaciones(id_asignacion, id_usuario, id_tarea, fecha_asignacion);
	}
}
