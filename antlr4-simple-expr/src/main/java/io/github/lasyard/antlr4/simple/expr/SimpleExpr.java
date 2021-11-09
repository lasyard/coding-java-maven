package io.github.lasyard.antlr4.simple.expr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

final class SimpleExpr {
    private SimpleExpr() {
    }

    static int eval(String input) {
        CharStream stream = CharStreams.fromString(input);
        SimpleExprLexer lexer = new SimpleExprLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleExprParser parser = new SimpleExprParser(tokens);
        ParseTree tree = parser.prog();
        EvalVisitor visitor = new EvalVisitor();
        return visitor.visit(tree);
    }
}
