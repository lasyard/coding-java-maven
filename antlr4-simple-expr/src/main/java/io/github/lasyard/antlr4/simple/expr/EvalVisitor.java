package io.github.lasyard.antlr4.simple.expr;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

class EvalVisitor extends SimpleExprParserBaseVisitor<Integer> {
    private final Map<String, Integer> memory = new LinkedHashMap<>();

    @Override
    public Integer visitOutput(@Nonnull SimpleExprParser.OutputContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitAssign(@Nonnull SimpleExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        Integer value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    @Override
    public Integer visitMulDiv(@Nonnull SimpleExprParser.MulDivContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ctx.MUL() != null) {
            return left * right;
        }
        return left / right;
    }

    @Override
    public Integer visitAddSub(@Nonnull SimpleExprParser.AddSubContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ctx.ADD() != null) {
            return left + right;
        }
        return left - right;
    }

    @Override
    public Integer visitInt(@Nonnull SimpleExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitId(@Nonnull SimpleExprParser.IdContext ctx) {
        return memory.get(ctx.ID().getText());
    }

    @Override
    public Integer visitParens(@Nonnull SimpleExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
