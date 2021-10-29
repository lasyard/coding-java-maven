package io.github.lasyard.quiz;

final class CalcBottles {
    private CalcBottles() {
    }

    static int calcBottles(int num) {
        int totalBottles = 0;
        int emptyBottles = 0;
        int emptyCovers = 0;
        int newBottles = num / 2;
        while (newBottles > 0) {
            totalBottles += newBottles;
            emptyBottles += newBottles;
            emptyCovers += newBottles;
            newBottles = emptyBottles / 2 + emptyCovers / 4;
            emptyBottles %= 2;
            emptyCovers %= 4;
        }
        return totalBottles;
    }
}
