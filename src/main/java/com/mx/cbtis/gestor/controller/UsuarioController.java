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

import com.mx.cbtis.gestor.dto.UsuarioDto;
import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@RestController
public class UsuarioController {
	
	private final UsuarioRepository usuariotable;
	
	public UsuarioController(UsuarioRepository usuariotable) {
		this.usuariotable = usuariotable;
	}
	
	@PostMapping("/getUsuarioLogin")
	public Usuario getUsuarioLogin(@RequestBody UsuarioDto userParam) {
		
		Optional<Usuario> optionaluser = usuariotable.findByCorreoPass(
				userParam.correo_electronico(),
				userParam.contrasena());
		
		Usuario user = new Usuario();
		
		if(optionaluser.isPresent()) {
			user = optionaluser.get();
		}else {
			user.setNombre("000");
		}
		
		return user;
	}
	
	@GetMapping("/buscarUsuario")
	public ResponseEntity<Usuario> getUsuario(@RequestParam int id) {
		
		Optional<Usuario> resultado = usuariotable.findById(id);
		if(resultado.isPresent()) {
			return ResponseEntity.ok(resultado.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/addUsuario")
	public int addUsuario(@RequestBody UsuarioDto usuario) {
		usuariotable.save(usuario.toEntity());
		return 1;
	}
	
	@PostMapping("/updateUsuario")
	public int updateUsuario(@RequestBody UsuarioDto usuario) {
		usuariotable.save(usuario.toEntity());
		return 1;
	}
	
	@DeleteMapping("/deleteUsuario")
	public int deleteUsuario(@RequestBody UsuarioDto usuario) {
		int flag = 0;
		if(usuario.id_usuario() != null && usuariotable.existsById(usuario.id_usuario())) {
			usuariotable.deleteById(usuario.id_usuario());
			flag = 1;
		}
		
		return flag;
	}
	
	@PostMapping("/buscarUsuarios")
	public List<Usuario> getUsuarios(){
		List<Usuario> listaUsuarios = new ArrayList<>(); 
		usuariotable.findAll().forEach(listaUsuarios::add);
		return listaUsuarios;
	}		
}
