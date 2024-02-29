---
description: Idea original y tecnologías usadas
---

# Introducción y objetivos

## Idea original

Aplicación orientada a contenido sobre mascotas o animales en general, con apartado para dar en adopción o adoptar

Contenido generado por el usuario, basandose en sus mascotas, con perfiles individuales para estas

## Objetivos implementadas

* Autenticación por correo electrónico
* Almacenamiento y display de publicaciones
* Ordenado de Publicaciones por categoría
* Perfil editable con su propia pestaña
* Creación de publicaciones con archivos del usuario

## Objetivos no implementados (a futuro)

* Interacción con publicaciones en tiempo real (guardar y like)
* Colección de publicaciones guardadas
* Pestaña de comentarios por publicación&#x20;
* Sistema de notificaciones por comentario o like
* Pestaña para mostrar perfiles ajenos
* Interacción entre perfiles
* Carga de datos cuando sea necesaria
* Sección de adopción
* Chat entre usuarios por adopción
* Perfiles "hijos" al usuario, orientado a ser uno por mascota

## Tecnologías usadas

* UI : Figma design
* UI a codigo : Relay for figma
* Lógica de aplicación : Android \[Jetpack compose]
  * Imagenes asíncronas : Android Coil
  * Video : Google Exoplayer
  * Navegación : Android Navigation
  * Interacción con servidor : Firebase for Android
* Servidor : Google Firebase
  * Autenticación : Firebase Authentication
  * Persistencia y acceso a datos : Firebase Firestore
  * Persistencia y acceso de archivos : Firebase Storage
