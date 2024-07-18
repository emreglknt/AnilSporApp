package com.example.recipeapppaparaproject.data.repo

import android.util.Log
import com.example.recipeapppaparaproject.data.model.Player
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepo @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun getPlayers(): List<Player> {
        return try {
            val result = db.collection("oyuncular").get().await()
            val players = result.map { document ->
                document.toObject(Player::class.java)
            }
            Log.d("PlayerRepo", "Fetched players: $players")
            players
        } catch (e: Exception) {
            Log.e("PlayerRepo", "Error fetching players", e)
            emptyList()
        }
    }

    suspend fun searchPlayerByName(name: String): List<Player> {
        return try {
            val result = db.collection("oyuncular")
                .whereEqualTo("name", name)
                .get()
                .await()
            val players = result.map { document ->
                document.toObject(Player::class.java)
            }
            players
        } catch (e: Exception) {
            emptyList()
        }
    }


    //Player Detail Screen by formNumber
    suspend fun getPlayersByFormNumber(formNumber: String): Player? {
        return try {
            val result = db.collection("oyuncular")
                .whereEqualTo("formanumber", formNumber)
                .get()
                .await()

            if (result.documents.isNotEmpty()) {
                result.documents.first().toObject(Player::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    //Rate Screen

    suspend fun ratePlayer(formNumber: String, userId: String, rating: Int) {
        val documentId = "${userId}_${formNumber}" // Combine userId and formNumber
        val userRatingDocRef = db.collection("rating").document(documentId)

        val rateData = hashMapOf(
            "formNumber" to formNumber,
            "ratevalue" to rating
        )

        try {
            userRatingDocRef.set(rateData).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getRate(formanumber: String): Float {
        return try {
            val querySnapshot = db.collection("rating")
                .whereEqualTo("formNumber", formanumber).get().await()
            var totalRating = 0
            var count = 0

            for (document in querySnapshot.documents) {
                val rate = document.getLong("ratevalue")?.toInt()
                if (rate != null) {
                    totalRating += rate
                    count++
                }
            }

            if (count > 0) {
                totalRating.toFloat() / count.toFloat()
            } else {
                0f // veya başka bir varsayılan değer
            }
        } catch (e: Exception) {
            throw e
        }
    }






}













