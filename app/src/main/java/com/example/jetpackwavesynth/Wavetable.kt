package com.example.jetpackwavesynth

enum class Wavetable {
    SINE{
        override fun toResourceString(): Int {
            return R.string.sine
        }
    },

    TRIANGLE{
        override fun toResourceString(): Int {
            return R.string.triangle
        }
    },

    SQUARE{
        override fun toResourceString(): Int {
            return R.string.square
        }
    },

    SAW{
        override fun toResourceString(): Int {
            return R.string.sawtooth
        }
    };

    abstract fun toResourceString(): Int
}