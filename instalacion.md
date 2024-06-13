---
description: Formas de instalar el proyecto
---

# Instalación

### Proceso de clonado

Abrimos una consola de git bash en la ubicación que queremos que esté el archivo y ejecutamos el siguiente comando

git clone [https://github.com/mingo96/Petstagram.git](https://github.com/mingo96/Petstagram.git)

### Ejecución del programa

Si buscamos ejecutar desde el propio proyecto, necesitaremos insertar el archivo google.services, este se obtiene desde la consola de firebase, el apk ya está firmada y está probada para funcionar con todo (sobre todo con el login de google, ya que hay firmas registradas específicas y probablemente no funcione si ejecutamos el proyecto normal, el conocido como modo debug)

Si queremos asegurarnos de que el login de google funciona en modo debug necesitaremos especificar que la firma que usemos está registrada en el servidor de firebase

Por motivos de seguridad esta clave no es proporcionada en el proyecto, tampoco la del propio apk

El apk está en el archivo /app/release/app-release.apk, es la versión release, por lo que rendirá muchisimo mejor que si ejecutamos el proyecto, sobre todo las animaciones
