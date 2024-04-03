package core.LanguageProcessors.ParserComponents;

import core.LanguageProcessors.LexerComponents.Token;

public class Binary extends Expression{
    Expression left;
    Token token;
    Expression right;

    public Binary(Expression left,Token token,Expression right){
        this.left = left;
        this.token = token;
        this.right = right;
    }
}
