package com.mx.cbtis.gestor.dto;

import com.mx.cbtis.gestor.modelo.Proyectos;

/**
 * Datos que recibe la API para Proyectos. Se usa en lugar de la entidad JPA para que el cliente
 * no pueda asignar directamente campos de la entidad persistente (regla java:S4684).
 */
public record ProyectosDto(
		Integer id_proyecto,
		String nombre,
		String descripcion,
		String fecha_inicio,
		String fecha_finalizacion,
		String estado_proyecto,
		String fondo) {

	public Proyectos toEntity() {
		return new Proyectos(id_proyecto, nombre, descripcion, fecha_inicio, fecha_finalizacion, estado_proyecto, fondo);
	}
}
