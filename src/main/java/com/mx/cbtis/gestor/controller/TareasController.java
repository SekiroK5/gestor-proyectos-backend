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

import com.mx.cbtis.gestor.dto.TareasDto;
import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.repository.TareasRepository;

@RestController
public class TareasController {
	
	private final TareasRepository tareastable;
	
	public TareasController(TareasRepository tareastable) {
		this.tareastable = tareastable;
	}
	
	@PostMapping("/getTarea")
	public List<Tareas> getTarea(@RequestBody TareasDto id_proyecto) {
		List<Tareas> listaTareas = tareastable.findByIdProyecto(id_proyecto.id_proyecto());
		return listaTareas;
	}
	
	@GetMapping("/buscarTarea")
	public ResponseEntity<Tareas> getTarea(@RequestParam int id_tarea) {
		
		Optional<Tareas> resultado = tareastable.findById(id_tarea);
		if(resultado.isPresent()) {
			return ResponseEntity.ok(resultado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/addTareas")
	public int addTareas(@RequestBody TareasDto tareas) {
		
		tareastable.save(tareas.toEntity());
		return 1;
	}
	
	@PostMapping("/updateTareas")
	public int updateTareas(@RequestBody TareasDto tareas) {
		
		tareastable.save(tareas.toEntity());
		return 1;
	}
	
	@DeleteMapping("/deleteTareas")
	public int deleteTareas(@RequestBody TareasDto tareas) {
		
		int flag = 0;
		if(tareas.id_tarea() != null && tareastable.existsById(tareas.id_tarea())) {
			tareastable.deleteById(tareas.id_tarea());
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/buscarTareas")
	public List<Tareas> getTareas(){
		
		List<Tareas> listaTareas = new ArrayList<>();
		tareastable.findAll().forEach(listaTareas :: add);
		return listaTareas;
	}

}
