package io.github.lasyard.quiz;

final class CalcBits {
    private CalcBits() {
    }

    static int calcBits(int num) {
        int bits = 0;
        while (num > 0) {
            if (num % 2 == 1) {
                bits++;
            }
            num >>= 1;
        }
        return bits;
    }

    static int calcBitsRecursive(int num) {
        if (num < 2) {
            return num;
        }
        return calcBitsRecursive(num / 2) + num % 2;
    }
}
