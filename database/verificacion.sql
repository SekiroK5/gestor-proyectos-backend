-- Verificaciones de calidad de la base de datos. Cada bloque detiene el pipeline (ON_ERROR_STOP)
-- si encuentra un problema.

-- 1. Deben existir las cinco tablas que usa el backend.
DO $$
DECLARE faltantes TEXT;
BEGIN
    SELECT string_agg(t, ', ') INTO faltantes
    FROM unnest(ARRAY['usuario', 'proyectos', 'tareas', 'comentarios', 'asignaciones']) AS t
    WHERE to_regclass('public.' || t) IS NULL;
    IF faltantes IS NOT NULL THEN
        RAISE EXCEPTION 'Faltan tablas: %', faltantes;
    END IF;
END $$;

-- 2. Cada tabla debe tener llave primaria.
DO $$
DECLARE sin_pk TEXT;
BEGIN
    SELECT string_agg(c.relname, ', ') INTO sin_pk
    FROM pg_class AS c
    INNER JOIN pg_namespace AS n ON c.relnamespace = n.oid
    WHERE n.nspname = 'public' AND c.relkind = 'r'
      AND NOT EXISTS (SELECT 1 FROM pg_constraint AS k WHERE k.conrelid = c.oid AND k.contype = 'p');
    IF sin_pk IS NOT NULL THEN
        RAISE EXCEPTION 'Tablas sin llave primaria: %', sin_pk;
    END IF;
END $$;

-- 3. Integridad de referencias (no hay llaves foraneas declaradas, se revisa aqui).
DO $$
DECLARE huerfanos INTEGER;
BEGIN
    SELECT count(*) INTO huerfanos FROM tareas AS t
    WHERE t.id_proyecto IS NOT NULL AND NOT EXISTS (SELECT 1 FROM proyectos AS p WHERE p.id_proyecto = t.id_proyecto);
    IF huerfanos > 0 THEN RAISE EXCEPTION 'Hay % tareas con un proyecto inexistente', huerfanos; END IF;

    SELECT count(*) INTO huerfanos FROM comentarios AS c
    WHERE c.id_tarea IS NOT NULL AND NOT EXISTS (SELECT 1 FROM tareas AS t WHERE t.id_tarea = c.id_tarea);
    IF huerfanos > 0 THEN RAISE EXCEPTION 'Hay % comentarios de una tarea inexistente', huerfanos; END IF;

    SELECT count(*) INTO huerfanos FROM asignaciones AS a
    WHERE NOT EXISTS (SELECT 1 FROM usuario AS u WHERE u.id_usuario = a.id_usuario)
       OR NOT EXISTS (SELECT 1 FROM tareas AS t WHERE t.id_tarea = a.id_tarea);
    IF huerfanos > 0 THEN RAISE EXCEPTION 'Hay % asignaciones con usuario o tarea inexistente', huerfanos; END IF;
END $$;

-- 4. Las secuencias identity deben ir por delante del mayor id (si no, el siguiente INSERT choca).
DO $$
DECLARE tabla TEXT; columna TEXT; maximo BIGINT; siguiente BIGINT;
BEGIN
    FOR tabla, columna IN SELECT * FROM (VALUES
        ('usuario', 'id_usuario'), ('proyectos', 'id_proyecto'), ('tareas', 'id_tarea'),
        ('comentarios', 'id_comentario'), ('asignaciones', 'id_asignacion')) AS v (t, c)
    LOOP
        EXECUTE format('SELECT coalesce(max(%I), 0) FROM %I', columna, tabla) INTO maximo;
        EXECUTE format('SELECT last_value FROM %s', pg_get_serial_sequence(tabla, columna)) INTO siguiente;
        IF siguiente < maximo THEN
            RAISE EXCEPTION 'La secuencia de %.% (%) esta detras del id maximo (%)', tabla, columna, siguiente, maximo;
        END IF;
    END LOOP;
END $$;

-- Resumen para el log del pipeline.
SELECT
    'usuario' AS tabla,
    count(*) AS registros
FROM usuario
UNION ALL
SELECT
    'proyectos' AS tabla,
    count(*) AS registros
FROM proyectos
UNION ALL
SELECT
    'tareas' AS tabla,
    count(*) AS registros
FROM tareas
UNION ALL
SELECT
    'comentarios' AS tabla,
    count(*) AS registros
FROM comentarios
UNION ALL
SELECT
    'asignaciones' AS tabla,
    count(*) AS registros
FROM asignaciones;
