-- Datos de ejemplo (ficticios) para desarrollo y para el pipeline de base de datos.

INSERT INTO usuario (id_usuario, nombre, correo_electronico, contrasena, rol, imagen_usuario) VALUES
(1, 'Usuario Demo', 'demo@gestor.local', 'demo123', 'Estudiante', '#70FAB7'),
(2, 'Docente Demo', 'docente@gestor.local', 'docente123', 'Docente', '#54EA01');

INSERT INTO proyectos (id_proyecto, nombre, descripcion, fecha_inicio, fecha_finalizacion, estado_proyecto, fondo) VALUES
(1, 'My API Rest', 'API REST con al menos 5 tablas', '2026-09-01', '2026-11-30', 'En proceso', '/Images/animacion1.gif');

INSERT INTO tareas (id_tarea, nombre, descripcion, fecha_vencimiento, estado, lista, id_proyecto) VALUES
(1, 'Base de datos', 'Realizar la estructura de las tablas', '2026-09-15', 'danger', 'terminado', 1),
(2, 'Pipelines', 'Configurar Jenkins con Java 21', '2026-11-15', 'warning', 'Pendientes', 1);

INSERT INTO comentarios (id_comentario, texto_comentario, fecha_hora, id_usuario, id_tarea) VALUES
(1, 'Tablas revisadas', '2026-09-15 10:00:00', 'Docente Demo', 1);

INSERT INTO asignaciones (id_asignacion, id_usuario, id_tarea, fecha_asignacion) VALUES
(1, 1, 2, '2026-10-30');

-- Las columnas identity deben continuar despues de los ids cargados.
SELECT setval(pg_get_serial_sequence('usuario', 'id_usuario'), (SELECT max(id_usuario) FROM usuario));
SELECT setval(pg_get_serial_sequence('proyectos', 'id_proyecto'), (SELECT max(id_proyecto) FROM proyectos));
SELECT setval(pg_get_serial_sequence('tareas', 'id_tarea'), (SELECT max(id_tarea) FROM tareas));
SELECT setval(pg_get_serial_sequence('comentarios', 'id_comentario'), (SELECT max(id_comentario) FROM comentarios));
SELECT setval(pg_get_serial_sequence('asignaciones', 'id_asignacion'), (SELECT max(id_asignacion) FROM asignaciones));
