package com.mx.cbtis.gestor.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

	@Autowired
	MockMvc mvc;

	@MockitoBean
	UsuarioRepository repository;

	private Usuario ejemplo() {
		return new Usuario(131, "Ana Paola", "ana@cbtis75.edu.mx", "1234", "Estudiante", "");
	}

	@Test
	@DisplayName("buscarUsuario devuelve 200 con el registro cuando el id existe")
	void buscarExistente() throws Exception {
		when(repository.findById(131)).thenReturn(Optional.of(ejemplo()));

		mvc.perform(get("/buscarUsuario").param("id", "131"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id_usuario").value(131));
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
	@DisplayName("addUsuario guarda el registro y responde 1")
	void agregar() throws Exception {
		mvc.perform(post("/addUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Prueba\",\"correo_electronico\":\"p@cbtis75.edu.mx\",\"contrasena\":\"1234\",\"rol\":\"Estudiante\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Usuario.class));
	}

	@Test
	@DisplayName("updateUsuario actualiza el registro y responde 1")
	void actualizar() throws Exception {
		mvc.perform(post("/updateUsuario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":131}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Usuario.class));
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
	@DisplayName("buscarUsuarios devuelve la lista completa")
	void listar() throws Exception {
		when(repository.findAll()).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/buscarUsuarios"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	@DisplayName("getUsuarioLogin devuelve el usuario con credenciales correctas")
	void loginCorrecto() throws Exception {
		when(repository.findByCorreoPass("ana@cbtis75.edu.mx", "1234")).thenReturn(Optional.of(ejemplo()));

		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"ana@cbtis75.edu.mx\",\"contrasena\":\"1234\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("Ana Paola"));
	}

	@Test
	@DisplayName("getUsuarioLogin marca nombre 000 con credenciales incorrectas")
	void loginIncorrecto() throws Exception {
		when(repository.findByCorreoPass(any(), any())).thenReturn(Optional.empty());

		mvc.perform(post("/getUsuarioLogin").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo_electronico\":\"ana@cbtis75.edu.mx\",\"contrasena\":\"mal\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("000"));
	}
}
