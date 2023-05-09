package core;

import core.LanguageProcessors.Lexer;

public class Core{

    public static void main(String[] arg){
        //TESTING LEXER
        Lexer lexer = new Lexer("print(1+2+\"hello\")", 0);
        for(int i =0;i<lexer.Tokenize().size();i++){
            System.out.println("->"+i);
            System.out.println("->"+lexer.Tokenize().get(i).getToken());
            System.out.println("->"+lexer.Tokenize().get(i).getTokenType());
            System.out.println("->"+lexer.Tokenize().get(i).getKeyWordType());
        }
    }
}