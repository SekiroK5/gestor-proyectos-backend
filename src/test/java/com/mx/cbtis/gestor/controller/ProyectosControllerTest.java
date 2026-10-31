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

import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.repository.ProtectosRepository;

@WebMvcTest(ProyectosController.class)
class ProyectosControllerTest {

	@Autowired
	MockMvc mvc;

	@MockitoBean
	ProtectosRepository repository;

	private Proyectos ejemplo() {
		return new Proyectos(197, "My API Rest", "Api del gestor", "2024-05-31", "2024-06-29", "En proceso", "");
	}

	@Test
	@DisplayName("buscarProyecto devuelve 200 con el registro cuando el id existe")
	void buscarExistente() throws Exception {
		when(repository.findById(197)).thenReturn(Optional.of(ejemplo()));

		mvc.perform(get("/buscarProyecto").param("id_proyecto", "197"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id_proyecto").value(197));
	}

	@Test
	@DisplayName("buscarProyecto devuelve 404 cuando el id no existe")
	void buscarInexistente() throws Exception {
		when(repository.findById(99999)).thenReturn(Optional.empty());

		mvc.perform(get("/buscarProyecto").param("id_proyecto", "99999"))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("buscarProyecto devuelve 400 cuando el id no es numerico")
	void buscarIdInvalido() throws Exception {
		mvc.perform(get("/buscarProyecto").param("id_proyecto", "abc"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("addProyecto guarda el registro y responde 1")
	void agregar() throws Exception {
		mvc.perform(post("/addProyecto").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Proyecto QA\",\"estado_proyecto\":\"En proceso\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Proyectos.class));
	}

	@Test
	@DisplayName("updateProyecto actualiza el registro y responde 1")
	void actualizar() throws Exception {
		mvc.perform(post("/updateProyecto").contentType(MediaType.APPLICATION_JSON).content("{\"id_proyecto\":197}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Proyectos.class));
	}

	@Test
	@DisplayName("deleteProyecto elimina y responde 1 cuando el registro existe")
	void eliminarExistente() throws Exception {
		when(repository.existsById(197)).thenReturn(true);

		mvc.perform(delete("/deleteProyecto").contentType(MediaType.APPLICATION_JSON).content("{\"id_proyecto\":197}"))
			.andExpect(content().string("1"));

		verify(repository).deleteById(197);
	}

	@Test
	@DisplayName("deleteProyecto responde 0 y no elimina cuando el registro no existe")
	void eliminarInexistente() throws Exception {
		when(repository.existsById(99999)).thenReturn(false);

		mvc.perform(delete("/deleteProyecto").contentType(MediaType.APPLICATION_JSON).content("{\"id_proyecto\":99999}"))
			.andExpect(content().string("0"));

		verify(repository, never()).deleteById(any());
	}

	@Test
	@DisplayName("buscarProyectos devuelve la lista completa")
	void listar() throws Exception {
		when(repository.findAll()).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/buscarProyectos"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1));
	}
}
