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

## Servicio de notificaciones

Para las notificaciones no se ha podido usar el servicio de firebase, ya que es sería necesario el uso de Functions, y requiere de plan de pago, pero nos las hemos apañado, se ha creado un servicio que se ejecuta en el momento que te autentificas, y que deja una tarea de fondo que, gracias a la versatibilidad de FireStore, deja preparado código para ejecutarse en el momento en que hayan cambios en el documento NotificationsChannels que corresponda al usuario, lo que hace es básicamente ejecutar una función cada vez que hay cambios en el documento, la función detecta que tipo de notificación ha sido añadida y, al obtener los recursos necesarios, muestra en el dispositivo una notificación

Habiendo probado el rendimiento se ha comprobado que no supone una carga relevante al uso de batería puesto que no está comprobando nada, es un proceso vacío que ejecuta algo cuando el servidor avisa de cambios en el documento, y no uno que va comprobando todo todo el rato (así se había planteado al principio)

Para esto se ha creado la clase notificación, que es un poco "omnidiestra", ya que para evitar implementar mutaciones y demás se han adaptado los campos a todas las notificaciones diseñadas, solo con el atributo TypeOfNotification sabemos cómo procesar los datos de esta

Para dar un ejemplo, en una notificación de like queremos mostrar la imagen/video del post, osea que necesitamos su id, pero en una notificación de follow no, pues sabiendo el tipo, sabemos que si es like, podemos partir el "notificationText" en id de la publicacion y contenido real

Las notificaciones son generadas por el usuario que las causa, alterando el NotificationsChannel de este con una nueva Notification, para que si alguien con esa cuenta está observando, reciba la notificación

El proceso se ejecuta de fondo, cómo nos afecta eso? Tenemos una notificación permanente, totalmente inutil, hay UI's de movil que permiten borrarla y UI's que no

Ha sido ya comprobado que el servicio no deja residuos, ya que al cerrarse vacía todos los observadores y recursos a modo de medida de seguridad

Tener este servicio también facilita la carga de datos, ya que los archivos temporales generados no se borran si este sigue abierto, y la proxima vez que carguemos la app, ya estarán descargados

A modo de apoyo añadido, se ha generado una aplicación de administración exclusiva mía, la cual sirve para añadir/actualizar/borrar categorías, ya que en el diseño no cuadraba que el usuario pudiera hacer esto, además de arreglar problemas de canales de notificación, que por ahora siguen bajo lupa

