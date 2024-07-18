package com.example.recipeapppaparaproject.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun login(email: String, password: String):FirebaseUser?{
        val result = auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user
        }.addOnFailureListener {
        }.await()
        return null
    }

    suspend fun signup(email: String, password: String):FirebaseUser?{
        val result = auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user
        }.addOnFailureListener {
        }.await()
        return null
    }

    suspend fun logout(){
        auth.signOut()
    }


    suspend fun currentUser():FirebaseUser?{
        return auth.currentUser
    }





}