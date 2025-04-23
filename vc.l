%{
#include <stdio.h>

int yywrap(void){
return 1;
}
%}

%%

[aeiouAEIOU]     { printf("'%s' is a VOWEL\n", yytext); }
[a-zA-Z]         { printf("'%s' is a CONSONANT\n", yytext); }
.|\n             { /* Ignore any other characters */ }

%%

int main() {
    printf("Enter a character: ");
    yylex();
    return 0;
}



Save the code in a file named vowel_consonant.l

lex vowel_consonant.l
gcc lex.yy.c -o vowel_consonant
./vowel_consonant
