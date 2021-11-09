package io.github.lasyard.antlr4.array.init;

import javax.annotation.Nonnull;

class Visitor extends ArrayInitBaseVisitor<Node> {
    @Override
    public ArrayNode visitInit(@Nonnull ArrayInitParser.InitContext ctx) {
        ArrayNode array = new ArrayNode(ctx.value().size());
        int i = 0;
        for (ArrayInitParser.ValueContext valueContext : ctx.value()) {
            array.set(i++, visitValue(valueContext));
        }
        return array;
    }

    @Override
    public Node visitValue(@Nonnull ArrayInitParser.ValueContext ctx) {
        if (ctx.init() != null) {
            return visitInit(ctx.init());
        } else if (ctx.INT() != null) {
            return new ValueNode(Integer.parseInt(ctx.INT().getText()));
        }
        throw new RuntimeException("Not implemented! Context: " + ctx);
    }
}
