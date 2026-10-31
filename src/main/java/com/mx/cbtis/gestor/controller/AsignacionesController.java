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

import com.mx.cbtis.gestor.dto.AsignacionesDto;
import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;

@RestController
public class AsignacionesController {
	
	private final AsignacionesRepository asignacionestable;
	
	public AsignacionesController(AsignacionesRepository asignacionestable) {
		this.asignacionestable = asignacionestable;
	}
	
	@GetMapping("/buscarAsignacion")
	public ResponseEntity<Asignaciones> getAsignacion(@RequestParam int id) {
		
		Optional<Asignaciones> resultado = asignacionestable.findById(id);
		if(resultado.isPresent()) {
			return ResponseEntity.ok(resultado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/addAsignacion")
	public int addAsignacion(@RequestBody AsignacionesDto asignacion) {
		
		asignacionestable.save(asignacion.toEntity());
		return 1;
	}
	
	@PostMapping("/updateAsignacion")
	public int updateAsignacion(@RequestBody AsignacionesDto asignacion) {
		
		asignacionestable.save(asignacion.toEntity());
		return 1;
	}
	
	@DeleteMapping("/deleteAsignacion")
	public int deleteAsignacion(@RequestBody AsignacionesDto asignacion) {
		
		int flag = 0;
		if(asignacion.id_asignacion() != null && asignacionestable.existsById(asignacion.id_asignacion())) {
			asignacionestable.deleteById(asignacion.id_asignacion());
			flag = 1;
		}
		
		return flag;
	}   
	
	@PostMapping("/buscarAsignaciones")
	public List<Asignaciones> getAsignaciones(){
		
		List<Asignaciones> listaAsignaciones = new ArrayList<>();
		asignacionestable.findAll().forEach(listaAsignaciones :: add);
		return listaAsignaciones;
	}
}
