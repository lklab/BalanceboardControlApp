package com.gambridzy.balanceboardcontrolapp.network

data class RequestResult<T>(var resultState: ResultState, var data: T)
