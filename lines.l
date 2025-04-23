%{
#include <stdio.h>

int line_count = 0;
int word_count = 0;
int char_count = 0;

int yywrap(void);
%}

%%

\n              { line_count++; char_count++; }
[ \t]+          { char_count += yyleng; }                     // Spaces and tabs (word separators)
[a-zA-Z0-9]+    { word_count++; char_count += yyleng; }       // Words (letters/numbers)
.               { char_count += yyleng; }                     // All other characters

%%

int yywrap(void) {
    return 1;
}

int main() {
    printf("Enter text (Ctrl+D to end input):\n");
    yylex();
    printf("\nLines: %d\nWords: %d\nCharacters: %d\n", line_count, word_count, char_count);
    return 0;
}


lex count.l
gcc lex.yy.c -lfl
./a.out
