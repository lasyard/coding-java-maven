package io.github.lasyard.antlr4.simple.expr;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

class EvalVisitor extends SimpleExprParserBaseVisitor<Integer> {
    private final Map<String, Integer> memory = new LinkedHashMap<>();

    @Override
    public Integer visitOutput(SimpleExprParser.@NonNull OutputContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitAssign(SimpleExprParser.@NonNull AssignContext ctx) {
        String id = ctx.ID().getText();
        Integer value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    @Override
    public Integer visitMulDiv(SimpleExprParser.@NonNull MulDivContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ctx.MUL() != null) {
            return left * right;
        }
        return left / right;
    }

    @Override
    public Integer visitAddSub(SimpleExprParser.@NonNull AddSubContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ctx.ADD() != null) {
            return left + right;
        }
        return left - right;
    }

    @Override
    public Integer visitInt(SimpleExprParser.@NonNull IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitId(SimpleExprParser.@NonNull IdContext ctx) {
        return memory.get(ctx.ID().getText());
    }

    @Override
    public Integer visitParens(SimpleExprParser.@NonNull ParensContext ctx) {
        return visit(ctx.expr());
    }
}
