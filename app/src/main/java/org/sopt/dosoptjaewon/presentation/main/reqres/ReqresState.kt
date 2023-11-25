package org.sopt.dosoptjaewon.presentation.main.reqres

sealed class ReqresState {
    object Success : ReqresState()
    data class Failure(val message: String) : ReqresState()
    object Loading : ReqresState()
    object Idle : ReqresState()
}