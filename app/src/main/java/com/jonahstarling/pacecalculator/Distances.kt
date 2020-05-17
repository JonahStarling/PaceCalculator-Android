package com.jonahstarling.pacecalculator

class Distances {
    companion object {
        val oneMile = Distance("MILE", 1.0f)
        val oneKilometer = Distance("KILOMETER", 0.621371f)
    }
}

class Distance(val name: String, val lengthInMiles: Float)