parser grammar SimpleExprParser;

options { tokenVocab=SimpleExprLexer; }

prog : stat+ ;

stat : expr NEWLINE            # Output
     | ID ASSIGN expr NEWLINE  # Assign
     | NEWLINE                 # BlankLine
     ;

expr : expr (MUL|DIV) expr     # MulDiv
     | expr (ADD|SUB) expr     # AddSub
     | INT                     # Int
     | ID                      # Id
     | LPAR expr RPAR          # Parens
     ;
