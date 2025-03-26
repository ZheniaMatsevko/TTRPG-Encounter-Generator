package com.example.backend.utils;


public class MonstersXPCalculationUtils {
    private MonstersXPCalculationUtils() {
    }

    public static float getMultiplier(int numberOfMonsters) {
        if (numberOfMonsters == 1) {
            return 1.0f;
        } else if (numberOfMonsters == 2) {
            return 1.5f;
        } else if (numberOfMonsters >= 3 && numberOfMonsters <= 6) {
            return 2.0f;
        } else if (numberOfMonsters >= 7 && numberOfMonsters <= 10) {
            return 2.5f;
        } else if (numberOfMonsters >= 11 && numberOfMonsters <= 14) {
            return 3.0f;
        } else if (numberOfMonsters >= 15) {
            return 4.0f;
        } else {
            throw new IllegalArgumentException("Number of monsters must be positive.");
        }
    }
}
