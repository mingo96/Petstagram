package com.example.petstagram.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Pet
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetCreationViewModel : ViewModel() {

    var pet by mutableStateOf(Pet())

    private var name by mutableStateOf("")

    lateinit var base: DataFetchViewModel

    private val db = Firebase.firestore

    private val _loading = MutableLiveData(true)

    val loading: LiveData<Boolean> = _loading

    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    val categories: StateFlow<List<Category>> = _categories

    private val _resource = MutableLiveData(Uri.EMPTY)

    val resource: LiveData<Uri> = _resource

    private val storageRef = Firebase.storage.reference

    private var finished by mutableStateOf(false)

    fun startLoading() {

        viewModelScope.launch {
            finished = false
            _loading.value = true

            base.categories()

            do {
                delay(100)
            } while (base.alreadyLoading)

            val newRound = base.categories()

            _categories.value += newRound - _categories.value.toSet()

            _loading.value = false
        }

    }

    fun getPetName(): String {
        return name
    }

    fun setPetName(newName: String) {
        name = newName
        pet.name = newName
    }

    fun setCategory(newCategory: Category) {
        pet.category = newCategory
    }

    fun send(context: Context,onSuccess: () -> Unit = {}) {

        if (validate()) {
            try {
                pet.owner = base.selfId

                db.collection("Pets").add(pet).addOnSuccessListener {
                    pet.id = it.id
                    storageRef.child("ProfilePictures/${pet.id}").putFile(_resource.value!!)
                        .addOnSuccessListener {
                            storageRef.child("/ProfilePictures/${pet.id}").downloadUrl.addOnSuccessListener { uri ->
                                pet.profilePic = uri
                                db.collection("Pets")
                                    .document(pet.id)
                                    .update(
                                        mapOf(
                                            Pair("id", pet.id),
                                            Pair("profilePic", pet.profilePic)
                                        )
                                    )
                                finished = true
                                onSuccess()

                            }
                        }
                }

            } catch (e: Exception) {
                Log.e("ERRRRRRRORRRR", e.message.orEmpty())
            }

        }else{
            Toast.makeText(context,"Revisa la informacion dada", Toast.LENGTH_SHORT).show()
        }

    }

    private fun validate(): Boolean {
        return resource.value != null && resource.value != Uri.EMPTY && name.isNotBlank() && pet.category != null
    }

    fun setResource(uri: Uri, thisContext: Context) {

        val isImage = getMimeType(thisContext, uri)?.startsWith("image/") == true

        if (!isImage) {
            Toast.makeText(thisContext, "Archivo no válido", Toast.LENGTH_SHORT).show()
            return
        }
        _resource.value = uri
    }

}