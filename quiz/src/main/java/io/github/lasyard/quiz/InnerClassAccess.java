package io.github.lasyard.quiz;

import lombok.Getter;

@Getter
class InnerClassAccess {
    static final String ORIGINAL_SECRET = "Original secret in outer class.";
    static final String NEW_SECRET = "New secret set in inner class.";
    static final String INNER_SAY = "Inner class is saying.";

    private String secret = ORIGINAL_SECRET;

    String innerSay() {
        return new Inner().say();
    }

    private class Inner {
        private Inner() {
            InnerClassAccess.this.secret = NEW_SECRET;
        }

        private String say() {
            return INNER_SAY;
        }
    }
}
