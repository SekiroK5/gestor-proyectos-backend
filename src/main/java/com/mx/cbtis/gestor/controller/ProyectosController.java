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

import com.mx.cbtis.gestor.dto.ProyectosDto;
import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.repository.ProtectosRepository;

@RestController
public class ProyectosController {
	
	private final ProtectosRepository proyectostable;
	
	public ProyectosController(ProtectosRepository proyectostable) {
		this.proyectostable = proyectostable;
	}
	
	/*@PostMapping("/buscarProyecto")
	public Proyectos getProyecto(@RequestBody ProyectosDto proyecto) {
		
		Proyectos buscar = new Proyectos();
		Optional<Proyectos> proyectos = proyectostable.findById(proyecto.id_proyecto());
		if(proyectos.isPresent()) {
			buscar = proyectos.get();
		}
		
		return buscar;
	}*/
	
	@GetMapping("/buscarProyecto")
	public ResponseEntity<Proyectos> getProyecto(@RequestParam int id_proyecto) {
		
		Optional<Proyectos> resultado = proyectostable.findById(id_proyecto);
		if(resultado.isPresent()) {
			return ResponseEntity.ok(resultado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/addProyecto")
	public int addProyecto(@RequestBody ProyectosDto proyecto) {
		
		proyectostable.save(proyecto.toEntity());
		return 1;
	}
	
	@PostMapping("/updateProyecto")
	public int updateProyecto(@RequestBody ProyectosDto proyecto) {
		
		proyectostable.save(proyecto.toEntity());
		return 1;
	}
	
	@DeleteMapping("/deleteProyecto")
	public int deleteProyecto(@RequestBody ProyectosDto proyecto) {
		
		int flag = 0;
		if(proyecto.id_proyecto() != null && proyectostable.existsById(proyecto.id_proyecto())) {
			proyectostable.deleteById(proyecto.id_proyecto());
			flag = 1;
		}
		
		return flag;
	}   
	
	@PostMapping("/buscarProyectos")
	public List<Proyectos> getProyectos(){
		
		List<Proyectos> listaProyectos = new ArrayList<>();
		proyectostable.findAll().forEach(listaProyectos :: add);
		return listaProyectos;
	}

}
