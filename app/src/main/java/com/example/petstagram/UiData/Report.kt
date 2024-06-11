package com.example.petstagram.UiData

import java.time.Instant
import java.util.Date

class Report {
    var user: String = ""
    var score: Double = 0.0
    var reportDate: Date = Date.from(Instant.now())
}