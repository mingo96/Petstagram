# Introducción y objetivos

### enlace al tango de uso : https://app.tango.us/app/workflow/29ee75e0-2282-4781-b86b-0d192eff22fa enlace al gitbook : https://app.gitbook.com/invite/k1f4oe40ecDSxbAIIK5M/g9RyGcoKcOEKb3MS9YmL

### description: Idea original y tecnologías usadas

## Introducción y objetivos

### Idea original

Aplicación orientada a contenido sobre mascotas o animales en general, con apartado para dar en adopción o adoptar

Contenido generado por el usuario, basandose en sus mascotas, con perfiles individuales para estas

### Objetivos implementadas

* Autenticación por correo electrónico
* Autenticación por Google Auth
* Almacenamiento y display de publicaciones
* Ordenado de Publicaciones por categoría
* Perfil editable con su propia pestaña, mostrando publicaciones y mascotas
* Carga de datos cuando sea necesaria
* Creación de publicaciones con archivos del usuario
* Interacción con publicaciones en tiempo real (like, guardar, descargar, reportar, borrar)
* Pestaña de comentarios por publicación
* Colección de publicaciones guardadas
* Interacción entre perfiles y mascotas
* Perfiles "hijos" al usuario, orientado a ser uno por mascota
* Pestaña para mostrar perfiles ajenos, mostrando publicaciones y mascotas
* Pestaña para perfiles de mascota, mostrando publicaciones

### Objetivos no implementados (a futuro)

* Sistema de notificaciones por comentario o like
* Sección de adopción
* Chat entre usuarios por adopción

### Tecnologías usadas

* UI : Figma design
* UI a codigo : Relay for figma
* Lógica de aplicación : Android \[Jetpack compose]
  * Imagenes asíncronas : Android Coil
  * Video : Google Exoplayer
  * Navegación : Android Navigation
  * Interacción con servidor : Firebase for Android
  * Utilidades de diseño : Material 3
* Servidor : Google Firebase
  * Autenticación y Google auth: Firebase Authentication
  * Persistencia y acceso a datos : Firebase Firestore
  * Persistencia y acceso de archivos : Firebase Storage
  * Notificaciones : Firebase Cloud Message
