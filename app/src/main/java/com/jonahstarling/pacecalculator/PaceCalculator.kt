package com.jonahstarling.pacecalculator

class PaceCalculator {

    var timeSeconds: Float? = null
    var distanceInMiles: Float? = null
    var distanceUnit: Distance = Distances.oneMile
    var paceSecondsPerMile: Float? = null
    var paceUnit: Distance = Distances.oneMile

    enum class ValueCalculated {
        TIME,
        DISTANCE,
        PACE,
        ERROR
    }

    private fun timePresent(): Boolean {
        return timeSeconds != null
    }

    fun distancePresent(): Boolean {
        return distanceInMiles != null
    }

    fun pacePresent(): Boolean {
        return paceSecondsPerMile != null
    }

    private fun calculateTime(): Boolean {
        paceSecondsPerMile?.let { pace ->
            distanceInMiles?.let { distance ->
                timeSeconds = pace * distance
                return true
            }
        }
        return false
    }

    private fun calculateDistance(): Boolean {
        timeSeconds?.let { time ->
            paceSecondsPerMile?.let { pace ->
                distanceInMiles = time / pace
                return true
            }
        }
        return false
    }

    fun calculatePace(): Boolean {
        timeSeconds?.let { time ->
            distanceInMiles?.let { distance ->
                paceSecondsPerMile = time / distance
                return true
            }
        }
        return false
    }

    fun calculateMissing(): ValueCalculated {
        if (distancePresent() && pacePresent() && !timePresent()) {
            if (calculateTime()) {
                return ValueCalculated.TIME
            }
        } else if (timePresent() && pacePresent() && !distancePresent()) {
            if (calculateDistance()) {
                return ValueCalculated.DISTANCE
            }
        } else if (timePresent() && distancePresent() && !pacePresent()) {
            if (calculatePace()) {
                return ValueCalculated.PACE
            }
        }
        return ValueCalculated.ERROR
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
        paceSecondsPerMile = null
    }
}