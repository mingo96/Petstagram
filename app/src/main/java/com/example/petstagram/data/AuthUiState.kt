package com.example.petstagram.data

import com.example.petstagram.UiData.Profile

sealed interface AuthUiState {
    object IsLoading : AuthUiState
    object Error : AuthUiState

    data class Success(val user:Profile) : AuthUiState

}