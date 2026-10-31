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

import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.repository.TareasRepository;

@WebMvcTest(TareasController.class)
class TareasControllerTest {

	@Autowired
	MockMvc mvc;

	@MockitoBean
	TareasRepository repository;

	private Tareas ejemplo() {
		return new Tareas(199, "Base de datos", "Estructura de tablas", "2024-06-13", "terminado", "danger", 197);
	}

	@Test
	@DisplayName("buscarTarea devuelve 200 con el registro cuando el id existe")
	void buscarExistente() throws Exception {
		when(repository.findById(199)).thenReturn(Optional.of(ejemplo()));

		mvc.perform(get("/buscarTarea").param("id_tarea", "199"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id_tarea").value(199));
	}

	@Test
	@DisplayName("buscarTarea devuelve 404 cuando el id no existe")
	void buscarInexistente() throws Exception {
		when(repository.findById(99999)).thenReturn(Optional.empty());

		mvc.perform(get("/buscarTarea").param("id_tarea", "99999"))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("buscarTarea devuelve 400 cuando el id no es numerico")
	void buscarIdInvalido() throws Exception {
		mvc.perform(get("/buscarTarea").param("id_tarea", "abc"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("addTareas guarda el registro y responde 1")
	void agregar() throws Exception {
		mvc.perform(post("/addTareas").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Tarea QA\",\"id_proyecto\":197}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Tareas.class));
	}

	@Test
	@DisplayName("updateTareas actualiza el registro y responde 1")
	void actualizar() throws Exception {
		mvc.perform(post("/updateTareas").contentType(MediaType.APPLICATION_JSON).content("{\"id_tarea\":199}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Tareas.class));
	}

	@Test
	@DisplayName("deleteTareas elimina y responde 1 cuando el registro existe")
	void eliminarExistente() throws Exception {
		when(repository.existsById(199)).thenReturn(true);

		mvc.perform(delete("/deleteTareas").contentType(MediaType.APPLICATION_JSON).content("{\"id_tarea\":199}"))
			.andExpect(content().string("1"));

		verify(repository).deleteById(199);
	}

	@Test
	@DisplayName("deleteTareas responde 0 y no elimina cuando el registro no existe")
	void eliminarInexistente() throws Exception {
		when(repository.existsById(99999)).thenReturn(false);

		mvc.perform(delete("/deleteTareas").contentType(MediaType.APPLICATION_JSON).content("{\"id_tarea\":99999}"))
			.andExpect(content().string("0"));

		verify(repository, never()).deleteById(any());
	}

	@Test
	@DisplayName("buscarTareas devuelve la lista completa")
	void listar() throws Exception {
		when(repository.findAll()).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/buscarTareas"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	@DisplayName("getTarea devuelve las tareas del proyecto indicado")
	void tareasPorProyecto() throws Exception {
		when(repository.findByIdProyecto(197)).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/getTarea").contentType(MediaType.APPLICATION_JSON).content("{\"id_proyecto\":197}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id_proyecto").value(197));
	}
}
