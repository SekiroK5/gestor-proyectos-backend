package com.mx.cbtis.gestor.dto;

import com.mx.cbtis.gestor.modelo.Usuario;

/**
 * Datos que recibe la API para Usuario. Se usa en lugar de la entidad JPA para que el cliente
 * no pueda asignar directamente campos de la entidad persistente (regla java:S4684).
 */
public record UsuarioDto(
		Integer id_usuario,
		String nombre,
		String correo_electronico,
		String contrasena,
		String rol,
		String imagen_usuario) {

	public Usuario toEntity() {
		return new Usuario(id_usuario, nombre, correo_electronico, contrasena, rol, imagen_usuario);
	}
}
