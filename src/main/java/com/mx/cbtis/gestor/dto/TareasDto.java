package com.mx.cbtis.gestor.dto;

import com.mx.cbtis.gestor.modelo.Tareas;

/**
 * Datos que recibe la API para Tareas. Se usa en lugar de la entidad JPA para que el cliente
 * no pueda asignar directamente campos de la entidad persistente (regla java:S4684).
 */
public record TareasDto(
		Integer id_tarea,
		String nombre,
		String descripcion,
		String fecha_vencimiento,
		String lista,
		String estado,
		Integer id_proyecto) {

	public Tareas toEntity() {
		return new Tareas(id_tarea, nombre, descripcion, fecha_vencimiento, lista, estado, id_proyecto);
	}
}
