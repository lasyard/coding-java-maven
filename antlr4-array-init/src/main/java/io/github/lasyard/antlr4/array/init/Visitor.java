package io.github.lasyard.antlr4.array.init;

import org.checkerframework.checker.nullness.qual.NonNull;

class Visitor extends ArrayInitBaseVisitor<Node> {
    @Override
    public ArrayNode visitInit(ArrayInitParser.@NonNull InitContext ctx) {
        ArrayNode array = new ArrayNode(ctx.value().size());
        int i = 0;
        for (ArrayInitParser.ValueContext valueContext : ctx.value()) {
            array.set(i++, visitValue(valueContext));
        }
        return array;
    }

    @Override
    public Node visitValue(ArrayInitParser.@NonNull ValueContext ctx) {
        if (ctx.init() != null) {
            return visitInit(ctx.init());
        } else if (ctx.INT() != null) {
            return new ValueNode(Integer.parseInt(ctx.INT().getText()));
        }
        throw new RuntimeException("Not implemented! Context: " + ctx);
    }
}
