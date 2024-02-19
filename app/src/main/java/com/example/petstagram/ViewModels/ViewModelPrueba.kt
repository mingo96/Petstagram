package com.example.petstagram.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petstagram.UiData.Categoria
import com.example.petstagram.UiData.Perfil

class ViewModelPrueba : ViewModel() {

    val usuarioEnUso = MutableLiveData<Perfil>(Perfil())

    val categorias = MutableLiveData<MutableList<Categoria>>(mutableListOf())




}