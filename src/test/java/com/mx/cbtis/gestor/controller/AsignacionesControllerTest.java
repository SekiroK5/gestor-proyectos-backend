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

import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;

@WebMvcTest(AsignacionesController.class)
class AsignacionesControllerTest {

	@Autowired
	MockMvc mvc;

	@MockitoBean
	AsignacionesRepository repository;

	private Asignaciones ejemplo() {
		return new Asignaciones(21, 131, 199, "2026-10-17");
	}

	@Test
	@DisplayName("buscarAsignacion devuelve 200 con el registro cuando el id existe")
	void buscarExistente() throws Exception {
		when(repository.findById(21)).thenReturn(Optional.of(ejemplo()));

		mvc.perform(get("/buscarAsignacion").param("id", "21"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id_asignacion").value(21));
	}

	@Test
	@DisplayName("buscarAsignacion devuelve 404 cuando el id no existe")
	void buscarInexistente() throws Exception {
		when(repository.findById(99999)).thenReturn(Optional.empty());

		mvc.perform(get("/buscarAsignacion").param("id", "99999"))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("buscarAsignacion devuelve 400 cuando el id no es numerico")
	void buscarIdInvalido() throws Exception {
		mvc.perform(get("/buscarAsignacion").param("id", "abc"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("addAsignacion guarda el registro y responde 1")
	void agregar() throws Exception {
		mvc.perform(post("/addAsignacion").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":131,\"id_tarea\":199,\"fecha_asignacion\":\"2026-10-17\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Asignaciones.class));
	}

	@Test
	@DisplayName("updateAsignacion actualiza el registro y responde 1")
	void actualizar() throws Exception {
		mvc.perform(post("/updateAsignacion").contentType(MediaType.APPLICATION_JSON).content("{\"id_asignacion\":21}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Asignaciones.class));
	}

	@Test
	@DisplayName("deleteAsignacion elimina y responde 1 cuando el registro existe")
	void eliminarExistente() throws Exception {
		when(repository.existsById(21)).thenReturn(true);

		mvc.perform(delete("/deleteAsignacion").contentType(MediaType.APPLICATION_JSON).content("{\"id_asignacion\":21}"))
			.andExpect(content().string("1"));

		verify(repository).delete(any(Asignaciones.class));
	}

	@Test
	@DisplayName("deleteAsignacion responde 0 y no elimina cuando el registro no existe")
	void eliminarInexistente() throws Exception {
		when(repository.existsById(99999)).thenReturn(false);

		mvc.perform(delete("/deleteAsignacion").contentType(MediaType.APPLICATION_JSON).content("{\"id_asignacion\":99999}"))
			.andExpect(content().string("0"));

		verify(repository, never()).delete(any(Asignaciones.class));
	}

	@Test
	@DisplayName("buscarAsignaciones devuelve la lista completa")
	void listar() throws Exception {
		when(repository.findAll()).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/buscarAsignaciones"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1));
	}
}
