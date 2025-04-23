%{
#include <stdio.h>
#include <string.h>

char *keywords[] = {
    "int", "float", "char", "double", "if", "else", "while", "for", "return", "void"
};
int isKeyword(char *str) {
    for(int i = 0; i < sizeof(keywords)/sizeof(keywords[0]); i++) {
        if(strcmp(str, keywords[i]) == 0)
            return 1;
    }
    return 0;
}
%}

%%
[ \t\n]                  ; // Ignore whitespace
"=="|"!="|"<="|">="|"="|"<"|">"    { printf("Relational Operator: %s\n", yytext); }
"*"|"+"|"-"|"/"          { printf("Arithmetic Operator: %s\n", yytext); }
[0-9]+\.[0-9]+           { printf("Float Literal: %s\n", yytext); }
[0-9]+                   { printf("Integer Literal: %s\n", yytext); }
[a-zA-Z_][a-zA-Z0-9_]*   {
                            if (isKeyword(yytext))
                                printf("Keyword: %s\n", yytext);
                            else
                                printf("Identifier: %s\n", yytext);
                         }
.                        { printf("Special Symbol: %s\n", yytext); }
%%

int main() {
    printf("Enter the input:\n");
    yylex();
    return 0;
}

int yywrap() {
    return 1;
}


lex tokenizer.l
gcc lex.yy.c -lfl
./a.out
