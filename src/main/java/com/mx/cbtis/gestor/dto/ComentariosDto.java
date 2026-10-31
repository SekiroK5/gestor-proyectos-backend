package com.mx.cbtis.gestor.dto;

import com.mx.cbtis.gestor.modelo.Comentarios;

/**
 * Datos que recibe la API para Comentarios. Se usa en lugar de la entidad JPA para que el cliente
 * no pueda asignar directamente campos de la entidad persistente (regla java:S4684).
 */
public record ComentariosDto(
		Integer id_comentario,
		String id_usuario,
		Integer id_tarea,
		String texto_comentario,
		String fecha_hora) {

	public Comentarios toEntity() {
		return new Comentarios(id_comentario, id_usuario, id_tarea, texto_comentario, fecha_hora);
	}
}
