package com.mx.cbtis.gestor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.cbtis.gestor.dto.ComentariosDto;
import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.repository.ComentariosRepository;

@RestController
public class ComentariosController {
	
	private final ComentariosRepository comentariostable;
	
	public ComentariosController(ComentariosRepository comentariostable) {
		this.comentariostable = comentariostable;
	}
	
	@PostMapping("/getComentario")

	public List<Comentarios> getComentario(@RequestBody ComentariosDto idComentario){
		List<Comentarios> listaComentarios = comentariostable.findByTarea(idComentario.id_tarea());
		return listaComentarios;
	}
	
	@GetMapping("/buscarComentario")
	public ResponseEntity<Comentarios> getComentario(@RequestParam int id) {
		
		Optional<Comentarios> resultado = comentariostable.findById(id);
		if(resultado.isPresent()) {
			return ResponseEntity.ok(resultado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/addComentario")
	public int addComentario(@RequestBody ComentariosDto comentario) {
		
		comentariostable.save(comentario.toEntity());
		return 1;
	}
	
	@PostMapping("/updateComentario")
	public int updateComentario(@RequestBody ComentariosDto comentario) {
		
		comentariostable.save(comentario.toEntity());
		return 1;
	}
	
	@DeleteMapping("/deleteComentario")
	public int deleteComentario(@RequestBody ComentariosDto comentario) {
		
		int flag = 0;
		if(comentario.id_comentario() != null && comentariostable.existsById(comentario.id_comentario())) {
			comentariostable.deleteById(comentario.id_comentario());
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/buscarComentarios")
	public List<Comentarios> getComentarios(){
		
		List<Comentarios> listaComentarios = new ArrayList<>();
		comentariostable.findAll().forEach(listaComentarios :: add);
		return listaComentarios;
	}

}
