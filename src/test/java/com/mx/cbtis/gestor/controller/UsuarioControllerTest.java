package com.mx.cbtis.gestor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mx.cbtis.gestor.config.SeguridadConfig;
import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@WebMvcTest(UsuarioController.class)
@Import(SeguridadConfig.class)
class UsuarioControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	PasswordEncoder encoder;

	@MockitoBean
	UsuarioRepository repository;

	private Usuario ejemplo() {
		return new Usuario(131, "Ana Paola", "ana@cbtis75.edu.mx", encoder.encode("1234"), "Estudiante", "");
	}

	private Usuario guardado() {
		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(repository).save(captor.capture());
		return captor.getValue();
	}

	@Test
	@DisplayName("buscarUsuario devuelve 200 con el registro y sin la contrasena cuando el id existe")
	void buscarExistente() throws Exception {
		when(repository.findById(131)).thenReturn(Optional.of(ejemplo()));

		mvc.perform(get("/buscarUsuario").param("id", "131"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id_usuario").value(131))
			.andExpect(jsonPath("$.contrasena").isEmpty());
	}

	@Test
	@DisplayName("buscarUsuario devuelve 404 cuando el id no existe")
	void buscarInexistente() throws Exception {
		when(repository.findById(99999)).thenReturn(Optional.empty());

		mvc.perform(get("/buscarUsuario").param("id", "99999"))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("buscarUsuario devuelve 400 cuando el id no es numerico")
	void buscarIdInvalido() throws Exception {
		mvc.perform(get("/buscarUsuario").param("id", "abc"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("addUsuario guarda la contrasena como hash BCrypt y responde 1")
	void agregar() throws Exception {
		mvc.perform(post("/addUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Prueba\",\"correo_electronico\":\"p@cbtis75.edu.mx\",\"contrasena\":\"1234\",\"rol\":\"Estudiante\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		Usuario u = guardado();
		assertTrue(u.getContrasena().startsWith("$2a$12$"), "debe guardarse como BCrypt de costo 12");
		assertTrue(encoder.matches("1234", u.getContrasena()));
	}

	@Test
	@DisplayName("addUsuario responde 0 y no guarda cuando falta la contrasena")
	void agregarSinContrasena() throws Exception {
		mvc.perform(post("/addUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Prueba\",\"contrasena\":\" \"}"))
			.andExpect(content().string("0"));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("updateUsuario con contrasena nueva la guarda como hash y responde 1")
	void actualizarConContrasena() throws Exception {
		mvc.perform(post("/updateUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":131,\"contrasena\":\"nueva\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		assertTrue(encoder.matches("nueva", guardado().getContrasena()));
	}

	@Test
	@DisplayName("updateUsuario sin contrasena conserva el hash guardado")
	void actualizarSinContrasena() throws Exception {
		Usuario actual = ejemplo();
		when(repository.findById(131)).thenReturn(Optional.of(actual));

		mvc.perform(post("/updateUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":131,\"nombre\":\"Ana\"}"))
			.andExpect(content().string("1"));

		assertEquals(actual.getContrasena(), guardado().getContrasena());
	}

	@Test
	@DisplayName("updateUsuario sin contrasena responde 0 si el usuario no existe o no trae id")
	void actualizarSinContrasenaInexistente() throws Exception {
		when(repository.findById(99999)).thenReturn(Optional.empty());

		mvc.perform(post("/updateUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":99999}"))
			.andExpect(content().string("0"));
		mvc.perform(post("/updateUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Sin id\"}"))
			.andExpect(content().string("0"));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("deleteUsuario elimina y responde 1 cuando el registro existe")
	void eliminarExistente() throws Exception {
		when(repository.existsById(131)).thenReturn(true);

		mvc.perform(delete("/deleteUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":131}"))
			.andExpect(content().string("1"));

		verify(repository).deleteById(131);
	}

	@Test
	@DisplayName("deleteUsuario responde 0 y no elimina cuando el registro no existe")
	void eliminarInexistente() throws Exception {
		when(repository.existsById(99999)).thenReturn(false);

		mvc.perform(delete("/deleteUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":99999}"))
			.andExpect(content().string("0"));

		verify(repository, never()).deleteById(any());
	}

	@Test
	@DisplayName("buscarUsuarios devuelve la lista completa sin contrasenas")
	void listar() throws Exception {
		when(repository.findAll()).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/buscarUsuarios"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].contrasena").isEmpty());
	}

	@Test
	@DisplayName("getUsuarioLogin devuelve el usuario sin hash cuando la contrasena coincide con el BCrypt")
	void loginCorrecto() throws Exception {
		when(repository.findByCorreo("ana@cbtis75.edu.mx")).thenReturn(Optional.of(ejemplo()));

		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"ana@cbtis75.edu.mx\",\"contrasena\":\"1234\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("Ana Paola"))
			.andExpect(jsonPath("$.contrasena").isEmpty());
	}

	@Test
	@DisplayName("getUsuarioLogin marca nombre 000 con contrasena incorrecta, vacia o correo inexistente")
	void loginIncorrecto() throws Exception {
		when(repository.findByCorreo("ana@cbtis75.edu.mx")).thenReturn(Optional.of(ejemplo()));
		when(repository.findByCorreo("nadie@cbtis75.edu.mx")).thenReturn(Optional.empty());

		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"ana@cbtis75.edu.mx\",\"contrasena\":\"mal\"}"))
			.andExpect(jsonPath("$.nombre").value("000"));
		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"ana@cbtis75.edu.mx\"}"))
			.andExpect(jsonPath("$.nombre").value("000"));
		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"nadie@cbtis75.edu.mx\",\"contrasena\":\"1234\"}"))
			.andExpect(jsonPath("$.nombre").value("000"));
	}

	@Test
	@DisplayName("Inyeccion SQL en el login no autentica: el correo se pasa como parametro, no como SQL")
	void loginInyeccionSql() throws Exception {
		String ataque = "' OR '1'='1";
		when(repository.findByCorreo(ataque)).thenReturn(Optional.empty());

		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"' OR '1'='1\",\"contrasena\":\"' OR '1'='1\"}"))
			.andExpect(jsonPath("$.nombre").value("000"));

		verify(repository).findByCorreo(ataque);
	}
}
