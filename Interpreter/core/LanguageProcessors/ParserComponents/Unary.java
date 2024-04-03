package core.LanguageProcessors.ParserComponents;

import core.LanguageProcessors.LexerComponents.Token;

public class Unary extends Expression{
    Token left;
    public Unary(Token left){
        this.left = left;
    }
}
