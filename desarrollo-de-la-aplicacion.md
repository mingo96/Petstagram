---
description: Pasos seguidos y dificultades encontradas
---

# Desarrollo de la aplicación

## Historial del desarrollo

1. Creación de UI en Figma
2. Adaptación de diseño Figma a Android Compose gracias a Relay
3. Ajustes de UI al código generado por Relay
4. Implementación de navegación
5. Creación de servidor y estructura de clases
6. Implementación de autenticación
7. Implementación de uso de datos para mostrar categorías
8. Implementación de la entrada de datos para crear publicaciones (solo con imagenes)
9. Implementación de flujo de datos estable para recibir información
10. Implementación de uso de datos para mostrar publicaciones
11. Implementación de uso de archivos de tipo video
    1. Para publicaciones
    2. Para creación de publicaciones
12. Implementación de uso de datos para mostrar perfil propio
    1. Implementación de cambio de nombre de usuario
    2. Implementación de cambio de foto de perfil
13. Optimización de flujos de datos
14. Gestión de recursos en memoria
15. Refactorizado y documentación del funcionamiento del código

Aquí finaliza el proyecto base

1. Completado de funcionalidad de Publicaciones
   1. Mejorado de los controladores, unión de los viewmodel de publicaciones mediante una interfaz
   2. Mejorado de las clases de uso, las originales son usadas solo para datos y las nuevas \*UIClasses\* se usan para facilitar el uso de objetos en la interfaz
   3. Ligero rediseño para mostrado del título de la publicación
   4. Implementación del sistema de Likes
   5. Implementación del sistema de guardado (solo guardado, aun falta implementar una vista de la lista de guardados del usuario)
   6. Implementación del sistema de comentarios con sus Likes
2. Rediseño de la interfaz de creación de publicación
3. Implementación de inyección de dependencias con dagger hilt (aun en progreso)
4. Rediseño de sistema de clases controladoras para unificación y simplificación del código
5. Re-Implementación de gestión de comentarios de forma que sobrecargue menos el dispositivo y esté más organizado
6. Implementación de interfaz de publicaciones guardadas
7. Rediseño a base estable del display, gestión de carga y scroll de la interfaz&#x20;
8. Implementación de página de publicaciones guardadas
9. Implementación de botones de interacción con publicación
   1. Botón de reporte
   2. Botón de descarga
10. Rediseño de la interfaz de publicar
11. Optimización de acceso a recursos con archivos temporales
12. Optimización final de uso de videos en interfaz
13. Implementación de mascotas y su creación
    1. Creación de clases y edición de otras para manejar esta
    2. Diseño e implementación de interfaz de creación
14. Rediseño interfaz de Perfil para mostrar mascotas
15. Ligero rediseño de publicaciones para mostrar mascotas (nombre de mascota y su foto)
16. Arreglo de publicaciones corruptas que rompen la app por no terminar de subirse
17. Diseñado e implementación de perfil ajeno
    1. Adicion de lista de seguidores para perfil
18. Diseñado e implementación de perfil de mascota (ajena o no)
    1. Adicion de lista de seguidores para mascota
19. Implementación de animación para like y cambio en modo de video/reproducción
20. Implementación de foto del dueño en pefil de mascota a modo de enlace
21. Cambiar boton de reporte a borrado en caso de publicación propia
22. Rediseño de UI de Login
    1. Implementacion de nueva UI
23. Implementación de Google Auth para registro/inicio de sesión
24. Cambios en animaciones para hacerlas más dinámicas

Entrega inicial del proyecto

25. Implementación de scroll real en perfil de usuario entre posts y mascotas
26. Creación de aplicación ajena de administración
27. Implementación de notificaciones observando TODOS los datos del usuario
28. Cambio de sistema de notificaciones a observación de colección notificaciones
29. Implementación de servicio para notificaciones, ahora es ajeno a la interfaz y funciona con la app cerrada/segundo plano también
30. Cambiar forma de descarga permanente de recursos, ahora es reflejada en la galería del movil, y sin hacer descarga, ya que el archivo debemos tenerlo de antes
31. Cambios varios en interfaz
32. Uso de memoria caché para imágenes (al no tener conexión se intentan recuperar de la caché de peticiones)
33. Implementación del buscador de perfiles
34. Implementación de dialogo de seguidores del usuario

## Dificultades encontradas

(Ordenadas de horrible a molesto)

1. Display de video, gestión de este y de su uso de recursos
2. Gestión y optimización del uso de clases controladoras de publicaciones
3. Rendimiento de composición y scroll
4. Implementación de notificaciones y servicio para estas
5. Gestión de estado de UI y flujo de datos estable y práctico
6. Actualización de datos en BBDD (las referencias no se actualizan si el referenciado cambia)
7. Gestión de recursos en memoria
8. Ajuste de UI a código
