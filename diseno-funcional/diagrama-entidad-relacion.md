# Diagrama entidad-relación

<figure><img src="../.gitbook/assets/Petstagram entidad relacion.drawio.png" alt=""><figcaption><p>Diagrama entidad-relación</p></figcaption></figure>

### Matices :&#x20;

Fuera de este diagrama, que representa lo que la base de datos alberga, está la clase UIPost, que extiende la clase Post para facilitar su uso en la interfaz, una función es representar si ya le hemos dado like/guardado a la publicación, otra es contener la mascota como objeto para facilitar acceso a su info, y otra es contener una referencia al recurso de lapublicación, cuando se obtienen los datos de la base de datos se descarga el recurso como archivo local (se borra al cerrar el proceso de la app) y se guarda la URI (ruta local) para que la interfaz tenga más facil acceder a este archivo

Esta misma tiene una función que devuelve un Post normal con la info actual, usado para la persistencia de cambios

```
class UIPost : Post() {
    var liked = Pressed.False
    var saved = SavePressed.No
    var uiPet: Pet? = null
    var UIURL: Uri by mutableStateOf(Uri.EMPTY)
    var mediaItem: MediaItem by mutableStateOf(MediaItem.EMPTY)

    fun basePost(): Post {
        return Post().apply {
            this.id = this@UIPost.id
            this.title = this@UIPost.title
            this.creatorUser = this@UIPost.creatorUser
            this.category = this@UIPost.category
            this.typeOfMedia = this@UIPost.typeOfMedia
            this.postedDate = this@UIPost.postedDate
            this.source = this@UIPost.source
            this.likes = this@UIPost.likes
            this.comments = this@UIPost.comments
            this.pet = this@UIPost.pet
        }
    }

}
```
