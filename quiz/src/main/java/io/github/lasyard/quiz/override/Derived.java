package io.github.lasyard.quiz.override;

public class Derived extends Base {
    static final String SAY = "Derived is saying.";
    static final String SAY_ELSE = "Derived is saying something else.";

    @Override
    public String say() {
        return SAY;
    }

    /**
     * Add @Override here would cause an error in compilation.
     */
    // @Override
    public String saySomethingElse() {
        return SAY_ELSE;
    }
}
