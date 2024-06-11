---
description: Descripción y diagramas de diseño funcional
---

# Diseño funcional

## Descripción

Cada pantalla tiene un objeto controlador "viewModel", debido a que hay mucho código que se duplica, se han creado ciertas interfaces y clases abstractas de las cuales heredarían estos

Aunque todos los viewModels tienen la capacidad de acceder a toda la info de la app, se ha creado uno específico para bajar la mayoría de recursos, gestionarlos y procesarlos, este es el DataFetchViewModel, todos los viewModels se generarán a modo de "Singleton", y todos tendrán la instancia del DataFetchViewModel

### CommentsController (Interfaz)

De este hereda el PostsController, tiene lo necesario para albergar una publicación y sus comentarios, así como interactuar con estos / crearlos

### PostsController (Interfaz)

De esta hereda GeneralController, tiene lo necesario para mostrar publicaciones, gestionar su uso, interactuar con estas y reportarlas/borrarlas

### GeneralController (Abstracta)

Esta es una clase abstracta que implementa la mayoría de cosas de las dos interfaces anteriores, gracias a la instancia del DataFetchViewModel, esta misma es ya de por si un ViewModel, hereda de esta clase y de PostsController

Gestiona todas las funciones de animaciones, pausas de video... gracias a la versatilidad asíncrona que da heredar de ViewModel

Deja a libre interpretación la carga de recursos y deja como obligatoria de sobreescribir la función scroll, que representa el codigo a ejecutar cuando se llega al fondo de las publicaciones que hay de momento

### ProfileInteractor (Interfaz)

Esta es bastante simple, es una interfaz que sirve para los viewModels de observación de perfil/mascota ajen@s, basicamente funciones para las animaciones de follow



