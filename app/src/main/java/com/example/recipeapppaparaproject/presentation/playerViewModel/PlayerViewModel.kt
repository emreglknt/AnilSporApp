package com.example.recipeapppaparaproject.presentation.playerViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapppaparaproject.data.model.Player
import com.example.recipeapppaparaproject.data.repo.PlayerRepo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class UIState{
    object Loading : UIState()
    data class Success(val data: List<Player>) : UIState()
    data class SuccessPLayer(val data: Player) : UIState() // player detay için
    data class Error(val message: String) : UIState()
}



@HiltViewModel
class PlayerViewModel @Inject constructor(private val playerRepo: PlayerRepo,  private val auth: FirebaseAuth) : ViewModel() {

    private val _playersState = MutableStateFlow<UIState>(UIState.Loading)
    val playersState: StateFlow<UIState> get() = _playersState

    val currentUserUid: String?
        get() = auth.currentUser?.uid

    fun getPlayers() {
        viewModelScope.launch {
            try {
                val players = playerRepo.getPlayers()
                _playersState.value = UIState.Success(players)
            } catch (e: Exception) {
                _playersState.value = UIState.Error(e.message ?: "An error occurred")
            }
        }
    }
        fun searchPlayerByName(name: String) {
            viewModelScope.launch {
                _playersState.value = UIState.Loading
                try {
                    val players = playerRepo.searchPlayerByName(name)
                    _playersState.value = UIState.Success(players)
                } catch (e: Exception) {
                    _playersState.value = UIState.Error(e.message ?: "An error occurred")
                }
            }
        }


    //Player Detail Screen by formNumber
    fun getPlayersByFormNumber(formNumber: String) {
        viewModelScope.launch {
            _playersState.value = UIState.Loading
            try {
                val player = playerRepo.getPlayersByFormNumber(formNumber)
                _playersState.value = player?.let { UIState.SuccessPLayer(it) }!!
            }catch (e: Exception) {
                _playersState.value = UIState.Error(e.message ?: "An error occurred")
            }

        }
    }

    fun ratePlayer(formNumber: String, rating: Int) {
        val userId = currentUserUid ?: throw IllegalStateException("User not authenticated")
        viewModelScope.launch {
            playerRepo.ratePlayer(formNumber, userId, rating)
        }
    }

    suspend fun getRate(formanumber: String): Float {
        return playerRepo.getRate(formanumber)
    }

}