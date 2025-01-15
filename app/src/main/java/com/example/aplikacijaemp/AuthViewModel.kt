package com.example.aplikacijaemp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to observe authentication state changes
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // Store the user's email locally (as a variable)
    private var userEmail: String? = null

    init {
        // Check authentication status when the ViewModel is created
        checkAuthStatus()
    }

    // Function to check the current authentication status
    private fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Store the user's email after successful login or signup
            userEmail = currentUser.email
            _authState.value = AuthState.Authenticated(userEmail ?: "Unknown")
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    // Get the stored user email (if logged in)
    fun getUserEmail(): String? {
        return userEmail
    }

    // Sign up function
    fun signup(email: String, password: String, password2: String) {
        if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            _authState.value = AuthState.Error("All fields must be filled")
            return
        }

        if (password != password2) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        _authState.value = AuthState.Loading // Start loading before making the request

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Store the user's email locally after successful signup
                    userEmail = user?.email
                    _authState.value = AuthState.Authenticated(userEmail ?: "Unknown")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                }
            }
    }

    // Login function
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading // Start loading before making the request

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Store the user's email locally after successful login
                    userEmail = user?.email
                    _authState.value = AuthState.Authenticated(userEmail ?: "Unknown")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    // Sign out function
    fun signout() {
        auth.signOut()
        userEmail = null // Clear the stored email when signing out
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    data class Authenticated(val email: String) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
