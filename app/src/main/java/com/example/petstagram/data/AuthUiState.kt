package com.example.petstagram.data

import com.example.petstagram.UiData.Profile

sealed interface AuthUiState {
    object isLoading : AuthUiState
    data class Error(val exception: Throwable) : AuthUiState

    data class Success(val user:Profile) : AuthUiState

    object Base : AuthUiState
}