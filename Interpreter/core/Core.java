package core;

import core.LanguageProcessors.Lexer;

public class Core{

    public static void main(String[] arg){
        Lexer lexer = new Lexer("if", 0);
        System.out.println("->"+lexer.Tokenize().get(0).getTokenType());
    }
}