package io.github.lasyard.antlr4.array.init;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

final class ArrayInit {
    private ArrayInit() {
    }

    static Node assign(String input) {
        CharStream stream = CharStreams.fromString(input);
        ArrayInitLexer lexer = new ArrayInitLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArrayInitParser parser = new ArrayInitParser(tokens);
        ParseTree tree = parser.init();
        Visitor visitor = new Visitor();
        return visitor.visit(tree);
    }
}
