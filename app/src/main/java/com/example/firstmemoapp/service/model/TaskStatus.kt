package com.example.firstmemoapp.service.model

enum class TaskStatus(val id: Int, val jp: String) {
    NO_START(0, "未達成(期限前)"),
    PERIOD_OVER(1, "未達成（期限切れ）"),
    DONE(2, "達成済み"),
    STOP(4, "保留中）")
}