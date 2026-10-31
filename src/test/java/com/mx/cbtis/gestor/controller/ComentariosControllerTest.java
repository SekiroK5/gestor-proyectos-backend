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

import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.repository.ComentariosRepository;

@WebMvcTest(ComentariosController.class)
class ComentariosControllerTest {

	@Autowired
	MockMvc mvc;

	@MockitoBean
	ComentariosRepository repository;

	private Comentarios ejemplo() {
		return new Comentarios(201, "Perla", 199, "Subirlo YA!!!", "2024-06-26 13:48:00");
	}

	@Test
	@DisplayName("buscarComentario devuelve 200 con el registro cuando el id existe")
	void buscarExistente() throws Exception {
		when(repository.findById(201)).thenReturn(Optional.of(ejemplo()));

		mvc.perform(get("/buscarComentario").param("id", "201"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id_comentario").value(201));
	}

	@Test
	@DisplayName("buscarComentario devuelve 404 cuando el id no existe")
	void buscarInexistente() throws Exception {
		when(repository.findById(99999)).thenReturn(Optional.empty());

		mvc.perform(get("/buscarComentario").param("id", "99999"))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("buscarComentario devuelve 400 cuando el id no es numerico")
	void buscarIdInvalido() throws Exception {
		mvc.perform(get("/buscarComentario").param("id", "abc"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("addComentario guarda el registro y responde 1")
	void agregar() throws Exception {
		mvc.perform(post("/addComentario").contentType(MediaType.APPLICATION_JSON).content("{\"id_usuario\":\"Perla\",\"id_tarea\":199,\"texto_comentario\":\"Listo\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Comentarios.class));
	}

	@Test
	@DisplayName("updateComentario actualiza el registro y responde 1")
	void actualizar() throws Exception {
		mvc.perform(post("/updateComentario").contentType(MediaType.APPLICATION_JSON).content("{\"id_comentario\":201}"))
			.andExpect(status().isOk())
			.andExpect(content().string("1"));

		verify(repository).save(any(Comentarios.class));
	}

	@Test
	@DisplayName("deleteComentario elimina y responde 1 cuando el registro existe")
	void eliminarExistente() throws Exception {
		when(repository.existsById(201)).thenReturn(true);

		mvc.perform(delete("/deleteComentario").contentType(MediaType.APPLICATION_JSON).content("{\"id_comentario\":201}"))
			.andExpect(content().string("1"));

		verify(repository).deleteById(201);
	}

	@Test
	@DisplayName("deleteComentario responde 0 y no elimina cuando el registro no existe")
	void eliminarInexistente() throws Exception {
		when(repository.existsById(99999)).thenReturn(false);

		mvc.perform(delete("/deleteComentario").contentType(MediaType.APPLICATION_JSON).content("{\"id_comentario\":99999}"))
			.andExpect(content().string("0"));

		verify(repository, never()).deleteById(any());
	}

	@Test
	@DisplayName("buscarComentarios devuelve la lista completa")
	void listar() throws Exception {
		when(repository.findAll()).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/buscarComentarios"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	@DisplayName("getComentario devuelve los comentarios de la tarea indicada")
	void comentariosPorTarea() throws Exception {
		when(repository.findByTarea(199)).thenReturn(List.of(ejemplo()));

		mvc.perform(post("/getComentario").contentType(MediaType.APPLICATION_JSON).content("{\"id_tarea\":199}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].texto_comentario").value("Subirlo YA!!!"));
	}
}
