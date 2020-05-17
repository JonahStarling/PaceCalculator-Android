package com.jonahstarling.pacecalculator

class PaceCalculator {

    var timeSeconds: Float? = null
    var distanceInMiles: Float? = null
    var distanceUnit: Distance = Distances.oneMile
    var paceSeconds: Float? = null
    var paceUnit: Distance = Distances.oneMile

    private fun timePresent() {
        // TODO
    }

    private fun distancePresent() {
        // TODO
    }

    private fun pacePresent() {
        // TODO
    }

    private fun calculateTime() {
        // TODO
    }

    private fun calculateDistance() {
        // TODO
    }

    private fun calculatePace() {
        // TODO
    }

    fun calculateMissing() {

    }

    fun switchDistanceUnit() {
        if (distanceUnit.name == Distances.oneMile.name) {
            distanceUnit = Distances.oneKilometer
        } else if (distanceUnit.name == Distances.oneKilometer.name) {
            distanceUnit = Distances.oneMile
        }
    }

    fun switchPaceUnit() {
        if (paceUnit.name == Distances.oneMile.name) {
            paceUnit = Distances.oneKilometer
        } else if (paceUnit.name == Distances.oneKilometer.name) {
            paceUnit = Distances.oneMile
        }
    }

    fun clear() {
        timeSeconds = null
        distanceInMiles = null
        paceSeconds = null
    }
}