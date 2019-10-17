package com.gambridzy.balanceboardcontrolapp.data

data class Command
(
    var type: Int,
    var target: Int,
    var exercise: Int,
    var level: Int
)
