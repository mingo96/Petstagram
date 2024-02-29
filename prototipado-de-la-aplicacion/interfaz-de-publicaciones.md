---
description: En esta vemos las publicaciones e interactuamos con estas
---

# Interfaz de publicaciones

<div>

<figure><img src="../.gitbook/assets/image (4).png" alt=""><figcaption><p>Mientras carga los datos</p></figcaption></figure>

 

<figure><img src="../.gitbook/assets/image (5).png" alt=""><figcaption><p>Todo cargado</p></figcaption></figure>

 

<figure><img src="../.gitbook/assets/image (6).png" alt=""><figcaption><p>Tiene los datos pero no los recursos</p></figcaption></figure>

</div>

En este componente vemos un deslizable con las publiaciones de la categoría seleccionada previamente, dato a tener en cuenta es que si no hay publicaciones seguirá "cargando" infinitamente

Los datos se guardan en memoria aunque salgas, solo se tienen que cargar una vez, cuando no quede más por cargar parará, si sales y vuelves a entrar a la categoría lo que tendrá que cargar de nuevo son los recursos, además de que volverá a comprobar si le quedan cosas que guardar

La foto del usuario que la publicó también se carga de la misma forma
