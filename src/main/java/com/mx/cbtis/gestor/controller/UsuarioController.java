package com.mx.cbtis.gestor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;

	public UsuarioController(UsuarioRepository usuariotable, PasswordEncoder passwordEncoder) {
		this.usuariotable = usuariotable;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/getUsuarioLogin")
	public Usuario getUsuarioLogin(@RequestBody UsuarioDto userParam) {

		Optional<Usuario> optionaluser = usuariotable.findByCorreo(userParam.correo_electronico());

		if(optionaluser.isPresent() && userParam.contrasena() != null
				&& passwordEncoder.matches(userParam.contrasena(), optionaluser.get().getContrasena())) {
			return sinContrasena(optionaluser.get());
		}

		Usuario user = new Usuario();
		user.setNombre("000");
		return user;
	}

	@GetMapping("/buscarUsuario")
	public ResponseEntity<Usuario> getUsuario(@RequestParam int id) {

		Optional<Usuario> resultado = usuariotable.findById(id);
		if(resultado.isPresent()) {
			return ResponseEntity.ok(sinContrasena(resultado.get()));
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping("/addUsuario")
	public int addUsuario(@RequestBody UsuarioDto usuario) {
		if(vacia(usuario.contrasena())) {
			return 0;
		}
		Usuario nuevo = usuario.toEntity();
		nuevo.setContrasena(passwordEncoder.encode(usuario.contrasena()));
		usuariotable.save(nuevo);
		return 1;
	}

	@PostMapping("/updateUsuario")
	public int updateUsuario(@RequestBody UsuarioDto usuario) {
		Usuario actualizado = usuario.toEntity();
		if(vacia(usuario.contrasena())) {
			// Sin contrasena nueva se conserva el hash que ya estaba guardado.
			Optional<Usuario> actual = usuario.id_usuario() == null ? Optional.empty() : usuariotable.findById(usuario.id_usuario());
			if(actual.isEmpty()) {
				return 0;
			}
			actualizado.setContrasena(actual.get().getContrasena());
		}else {
			actualizado.setContrasena(passwordEncoder.encode(usuario.contrasena()));
		}
		usuariotable.save(actualizado);
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
		usuariotable.findAll().forEach(u -> listaUsuarios.add(sinContrasena(u)));
		return listaUsuarios;
	}

	// El hash nunca sale de la API.
	private static Usuario sinContrasena(Usuario u) {
		return new Usuario(u.getId_usuario(), u.getNombre(), u.getCorreo_electronico(), null, u.getRol(), u.getImagen_usuario());
	}

	private static boolean vacia(String texto) {
		return texto == null || texto.isBlank();
	}
}
