package com.mx.cbtis.gestor.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las entidades JPA: cada constructor debe asignar todos los campos
 * y cada setter debe dejar el valor que despues devuelve su getter.
 */
class ModelosTest {

	@Test
	@DisplayName("Usuario: el constructor completo asigna todos los campos")
	void usuarioConstructor() {
		Usuario u = new Usuario(131, "Ana Paola", "ana@cbtis75.edu.mx", "1234", "Estudiante", "#A1B2C3");
		assertEquals(131, u.getId_usuario());
		assertEquals("Ana Paola", u.getNombre());
		assertEquals("ana@cbtis75.edu.mx", u.getCorreo_electronico());
		assertEquals("1234", u.getContrasena());
		assertEquals("Estudiante", u.getRol());
		assertEquals("#A1B2C3", u.getImagen_usuario());
	}

	@Test
	@DisplayName("Usuario: los setters cambian el valor que regresan los getters")
	void usuarioSetters() {
		Usuario u = new Usuario();
		assertNull(u.getId_usuario());
		u.setId_usuario(7);
		u.setNombre("Liliana");
		u.setCorreo_electronico("lili@cbtis75.edu.mx");
		u.setContrasena("abcd");
		u.setRol("Docente");
		u.setImagen_usuario("#000000");
		assertEquals(7, u.getId_usuario());
		assertEquals("Liliana", u.getNombre());
		assertEquals("lili@cbtis75.edu.mx", u.getCorreo_electronico());
		assertEquals("abcd", u.getContrasena());
		assertEquals("Docente", u.getRol());
		assertEquals("#000000", u.getImagen_usuario());
	}

	@Test
	@DisplayName("Proyectos: el constructor completo asigna todos los campos")
	void proyectosConstructor() {
		Proyectos p = new Proyectos(197, "My API Rest", "Api del gestor", "2024-05-31", "2024-06-29", "En proceso", "/Images/animacion1.gif");
		assertEquals(197, p.getId_proyecto());
		assertEquals("My API Rest", p.getNombre());
		assertEquals("Api del gestor", p.getDescripcion());
		assertEquals("2024-05-31", p.getFecha_inicio());
		assertEquals("2024-06-29", p.getFecha_finalizacion());
		assertEquals("En proceso", p.getEstado_proyecto());
		assertEquals("/Images/animacion1.gif", p.getFondo());
	}

	@Test
	@DisplayName("Proyectos: los setters cambian el valor que regresan los getters")
	void proyectosSetters() {
		Proyectos p = new Proyectos();
		assertNull(p.getId_proyecto());
		p.setId_proyecto(5);
		p.setNombre("Front");
		p.setDescripcion("Vistas");
		p.setFecha_inicio("2026-10-01");
		p.setFecha_finalizacion("2026-11-30");
		p.setEstado_proyecto("Finalizado");
		p.setFondo("/Images/blanco.png");
		assertEquals(5, p.getId_proyecto());
		assertEquals("Front", p.getNombre());
		assertEquals("Vistas", p.getDescripcion());
		assertEquals("2026-10-01", p.getFecha_inicio());
		assertEquals("2026-11-30", p.getFecha_finalizacion());
		assertEquals("Finalizado", p.getEstado_proyecto());
		assertEquals("/Images/blanco.png", p.getFondo());
	}

	@Test
	@DisplayName("Tareas: el constructor completo asigna todos los campos")
	void tareasConstructor() {
		Tareas t = new Tareas(199, "Base de datos", "Estructura de tablas", "2024-06-13", "terminado", "danger", 197);
		assertEquals(199, t.getId_tarea());
		assertEquals("Base de datos", t.getNombre());
		assertEquals("Estructura de tablas", t.getDescripcion());
		assertEquals("2024-06-13", t.getFecha_vencimiento());
		assertEquals("terminado", t.getLista());
		assertEquals("danger", t.getEstado());
		assertEquals(197, t.getId_proyecto());
	}

	@Test
	@DisplayName("Tareas: los setters cambian el valor que regresan los getters")
	void tareasSetters() {
		Tareas t = new Tareas();
		assertNull(t.getId_tarea());
		t.setId_tarea(3);
		t.setNombre("Pruebas");
		t.setDescripcion("JUnit");
		t.setFecha_vencimiento("2026-11-01");
		t.setLista("Pendientes");
		t.setEstado("warning");
		t.setId_proyecto(5);
		assertEquals(3, t.getId_tarea());
		assertEquals("Pruebas", t.getNombre());
		assertEquals("JUnit", t.getDescripcion());
		assertEquals("2026-11-01", t.getFecha_vencimiento());
		assertEquals("Pendientes", t.getLista());
		assertEquals("warning", t.getEstado());
		assertEquals(5, t.getId_proyecto());
	}

	@Test
	@DisplayName("Comentarios: el constructor completo asigna todos los campos")
	void comentariosConstructor() {
		Comentarios c = new Comentarios(201, "Perla", 199, "Subirlo YA!!!", "2024-06-26 13:48:00");
		assertEquals(201, c.getId_comentario());
		assertEquals("Perla", c.getId_usuario());
		assertEquals(199, c.getId_tarea());
		assertEquals("Subirlo YA!!!", c.getTexto_comentario());
		assertEquals("2024-06-26 13:48:00", c.getFecha_hora());
	}

	@Test
	@DisplayName("Comentarios: los setters cambian el valor que regresan los getters")
	void comentariosSetters() {
		Comentarios c = new Comentarios();
		assertNull(c.getId_comentario());
		c.setId_comentario(9);
		c.setId_usuario("Liliana");
		c.setId_tarea(3);
		c.setTexto_comentario("Listo");
		c.setFecha_hora("2026-10-30 10:00:00");
		assertEquals(9, c.getId_comentario());
		assertEquals("Liliana", c.getId_usuario());
		assertEquals(3, c.getId_tarea());
		assertEquals("Listo", c.getTexto_comentario());
		assertEquals("2026-10-30 10:00:00", c.getFecha_hora());
	}

	@Test
	@DisplayName("Asignaciones: constructor y setters guardan los valores")
	void asignaciones() {
		Asignaciones a = new Asignaciones(21, 131, 199, "2026-10-17");
		assertEquals(21, a.getId_asignacion());
		assertEquals(131, a.getId_usuario());
		assertEquals(199, a.getId_tarea());
		assertEquals("2026-10-17", a.getFecha_asignacion());

		Asignaciones b = new Asignaciones();
		assertNull(b.getId_asignacion());
		b.setId_asignacion(22);
		b.setId_usuario(172);
		b.setId_tarea(3);
		b.setFecha_asignacion("2026-10-30");
		assertEquals(22, b.getId_asignacion());
		assertEquals(172, b.getId_usuario());
		assertEquals(3, b.getId_tarea());
		assertEquals("2026-10-30", b.getFecha_asignacion());
	}
}
