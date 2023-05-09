package core.LanguageProcessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import core.LanguageProcessors.LexerComponents.Token;
import core.LanguageProcessors.LexerComponents.TokenType;

public class Lexer{

    public static HashMap<String,TokenType> keywords;

    static{
        keywords = new HashMap<>();
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("return", TokenType.RETURN);
        keywords.put("this", TokenType.THIS);
        keywords.put("String", TokenType.STRING);
        keywords.put("int", TokenType.INT);
        keywords.put("char", TokenType.CHAR);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("double", TokenType.DOUBLE);
        keywords.put("long", TokenType.LONG);
        keywords.put("byte", TokenType.BYTE);
        keywords.put("catch", TokenType.CATCH);
        keywords.put("try", TokenType.TRY);
        keywords.put("print", TokenType.PRINT);
        keywords.put("input", TokenType.INPUT);
        keywords.put("while", TokenType.WHILE);
        keywords.put("do", TokenType.DO);
        keywords.put("for", TokenType.FOR);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("public", TokenType.PUBLIC);
        keywords.put("private", TokenType.PRIVATE);
        keywords.put("super", TokenType.SUPER);
    }

    public static String Command;
    public static int LineNo;
    List<String> KeyWords = Arrays.asList("","");

    public Lexer(String Command,int LineNo){
        this.Command=Command;
        this.LineNo=LineNo;
    }

    public static Token setTokenValues(int row,String Token,TokenType tokenType)
    {
        Token token = new Token();
        token.setCol(LineNo);
        token.setRow(row);
        token.setToken(Token);
        token.setTokenType(tokenType);

        return token;
    }

    public static Token setTokenValues(int row,String Token,TokenType tokenType,TokenType KeyWordType)
    {
        Token token = new Token();
        token.setCol(LineNo);
        token.setRow(row);
        token.setToken(Token);
        token.setTokenType(tokenType);
        token.setKeyWordType(KeyWordType);
        return token;
    }

    public static boolean isDigit(String i){
        return Character.isDigit(i.toCharArray()[0])&&(!Character.isWhitespace(i.toCharArray()[0]));
    }

    public static boolean isAlpha(String i){
        return Character.isLetter(i.toCharArray()[0])&&(!Character.isWhitespace(i.toCharArray()[0]));
    }

    public static List<Token> Tokenize()
    {
        List<Token> returnVal = new ArrayList<>();
        String[] Tokens = Command.split("");
        for(int i = 0;i<Tokens.length;i++)
        {
            TokenType type = TokenType.NULL;
            switch(Tokens[i]){
                case "*","/","+","-" -> type = TokenType.ARITHEMATIC_OPERATORS;
                case "{" -> type = TokenType.OPEN_CURLY_BRACK;
                case "}" -> type = TokenType.CLOSE_CURLY_BRACK;
                case "]" -> type = TokenType.OPEN_BOX_BRACK;
                case "[" -> type = TokenType.CLOSE_BOX_BRACK;
                case "(" -> type = TokenType.OPEN_PARENS;
                case ")" -> type = TokenType.CLOSED_PARENS;
                case "=" -> type = TokenType.EQUALS;
                case ";" -> type = TokenType.END;
                case "\'" -> type = TokenType.SINGLE_QUOTES;
                case "\"" -> type = TokenType.DOUBLE_QUOTES;
                case "," -> type = TokenType.COMMA;
                case "!" -> type = TokenType.NOT;
            }

            if(!type.equals(TokenType.NULL)){
                returnVal.add(setTokenValues(i, Tokens[i], type));
            }
            else{
                if(isDigit(Tokens[i])){
                    String number = Tokens[i];
                    if(i!=Tokens.length){
                        for(int j=i+1;j<Tokens.length;j++){
                            if(isDigit(Tokens[j])||Tokens[j].equals(".")){
                                number=number+(Tokens[j]);
                                i++;
                            }else break;
                        }
                    }
                    returnVal.add(setTokenValues(i, number, TokenType.NUMBER_LITERAL));
                }
                else if(isAlpha(Tokens[i])){
                    String string = Tokens[i];

                    if(i!=Tokens.length){
                        for(int j=i+1;j<Tokens.length;j++){
                            if(isAlpha(Tokens[j])){
                                string=string+(Tokens[j]);
                                i++;
                            }else break;
                        }
                    } 
                    
                    if(keywords.get(string)!=null){
                        returnVal.add(setTokenValues(i, string, TokenType.KEYWORD,keywords.get(string)));
                    }
                    else{
                        returnVal.add(setTokenValues(i, string, TokenType.STRING_LITERAL));
                    }
                }
            }
        }
        return returnVal;
    }
}