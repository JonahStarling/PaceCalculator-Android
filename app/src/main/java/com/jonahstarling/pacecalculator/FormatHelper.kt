package com.jonahstarling.pacecalculator

class FormatHelper {
    companion object {
        fun distance(value: Float): String {
            return "%.2f".format(value)
        }

        fun hours(value: Float): String {
            return "%.0f".format(value)
        }

        fun minutes(value: Float): String {
            return if (value < 10) {
                "0"+"%.0f".format(value)
            } else {
                "%.0f".format(value)
            }
        }

        fun paceMinutes(value: Float): String {
            return "%.0f".format(value)
        }

        fun seconds(value: Float): String {
            var seconds = if (value < 10) {
                "0"+"%.2f".format(value)
            } else {
                "%.2f".format(value)
            }
            if (seconds.endsWith(".00")) {
                seconds = seconds.substring(0, 2)
            }
            return seconds
        }
    }
}