package com.example.recipeapppaparaproject.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton



sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}


@Singleton
class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(authResult.user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun signup(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(authResult.user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun logout(){
        auth.signOut()
    }


    suspend fun currentUser():FirebaseUser?{
        return auth.currentUser
    }





}