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

## Dificultades encontradas

(Ordenadas de horrible a molesto)

1. Gestión y optimización del uso de clases controladoras de publicaciones
2. Rendimiento de composición y scroll
3. Gestión de estado de UI y flujo de datos estable y práctico
4. Actualización de datos (las referencias no se actualizan si el referenciado cambia)
5. Gestión de recursos en memoria
6. Display de video
7. Ajuste de UI a código
