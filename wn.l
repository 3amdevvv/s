%{
#include <stdio.h>
int yywrap(void);
%}

%%

[0-9]+(\.[0-9]+)?    { printf("'%s' is a NUMBER\n", yytext); }
[a-zA-Z]+            { printf("'%s' is a WORD\n", yytext); }
.|\n                 { /* Ignore other characters */ }

%%

int yywrap(void) {
    return 1;
}

int main() {
    printf("Enter input: ");
    yylex();
    return 0;
}

lex tokenizer.l
gcc lex.yy.c -lfl
./a.out


 to run
  ./tokenizer
  hello 1234 test 45.6 abc

