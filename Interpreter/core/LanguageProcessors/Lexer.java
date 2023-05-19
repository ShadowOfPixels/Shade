package core.LanguageProcessors;

import java.util.*;

import core.ErrorHandling.ErrorHandler;
import core.LanguageProcessors.LexerComponents.Token;
import core.LanguageProcessors.LexerComponents.TokenType;

public class Lexer {

    public static HashMap<String, TokenType> keywords;

    static {
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
    public static ErrorHandler handler;
    List<String> KeyWords = new ArrayList<>();

    public Lexer(String Command, ErrorHandler handler) {
        this.handler = handler;
        this.Command = Command;
    }

    public static List<Token> Tokenize() {
        List<Token> returnVal = new ArrayList<>();
        String[] Tokens = Command.split("");
        //Character info"s
        int Line = 1;

        for(int row = 0;row<Tokens.length;row++){
            boolean foundToken = false;
            switch(Tokens[row]){
                case " ","\r","\t" : foundToken = true;break;
                case "\n" : {
                    Line++;
                    break;
                }
                case "{" : returnVal.add(setTokenValues(row, Line, Tokens[row], TokenType.OPEN_CURLY_BRACK)); foundToken = true; break;
                case "}" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.CLOSE_CURLY_BRACK)); foundToken = true; break;
                case "]" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.OPEN_BOX_BRACK)); foundToken = true; break;
                case "[" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.CLOSE_BOX_BRACK)); foundToken = true; break;
                case "(" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.OPEN_PARENS)); foundToken = true; break;
                case ")" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.CLOSED_PARENS ));foundToken = true; break;
                case "*","+","-","/" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.ARITHEMATIC_OPERATORS )); foundToken = true; break;
                case "=" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.EQUALS )); foundToken = true; break;
                case ";" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.END )); foundToken = true; break;
                case "," : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.COMMA )); foundToken = true; break;
                case "!" : returnVal.add(setTokenValues(row, Line,Tokens[row], TokenType.NOT )); foundToken = true; break;
                case "\"" : {
                    row++;
                    for (int k = row; k < Tokens.length; k++) {
                        if (Tokens[k].equals("\"")) {
                            foundToken = true; 
                            String string = Command.substring(row, k);
                            returnVal.add(setTokenValues(row,Line,string,TokenType.STRING_LITERAL));
                            row = k;
                            break;
                        } else if (k == Tokens.length - 1) {
                            Token unnecessaryToken = new Token();
                            unnecessaryToken.setToken(Tokens[row]);
                            unnecessaryToken.setRow(k);
                            handler.error(Line, unnecessaryToken, Command, "Unterminating String Literal ", false);
                            return null;
                        }
                    }
                    break;
                }
                case "\'" : {
                    if (Tokens[row + 2].equals("\'")) {
                        foundToken = true; 
                        System.out.println(row);
                        returnVal.add(setTokenValues(row,Line, Tokens[row + 1],TokenType.CHARACTER_LITERAL));
                        row = row + 3;
                    } else {
                        Token unnecessaryToken = new Token();
                        unnecessaryToken.setRow(row + 2);
                        handler.error(Line, unnecessaryToken, Command, "Unterminating Character Literal ", false);
                        return null;
                    }
                    break;
                }
                case "<" : {
                    if(isLessThanEqualTo(Tokens, row)){
                        foundToken = true; 
                        returnVal.add(setTokenValues(row,Line, Tokens[row]+Tokens[row+1],TokenType.LESS_THAN_EQUAL));
                        row++;
                    }
                    else{
                        foundToken = true; 
                        returnVal.add(setTokenValues(row,Line, Tokens[row],TokenType.LESS_THAN));
                    }
                    break;
                }
                case ">" : {
                    foundToken = true; 
                    if(isGreaterThanEqualTo(Tokens, row)){
                        returnVal.add(setTokenValues(row,Line, Tokens[row]+Tokens[row+1],TokenType.GREATER_THAN_EQUAL));
                        row++;
                    }
                    else{
                        foundToken = true; 
                        returnVal.add(setTokenValues(row,Line, Tokens[row]+Tokens[row],TokenType.GREATER_THAN));
                    }
                    break;
                }
            }
            if(!foundToken){
                if (isDigit(Tokens[row])) {
                    String number = Tokens[row];
                    if (row != Tokens.length) {
                        for (int j = row + 1; j < Tokens.length; j++) {
                            if (isDigit(Tokens[j]) || Tokens[j].equals(".")) {
                                number = number + (Tokens[j]);
                                row++;
                            } else
                                break;
                        }
                    }
                    returnVal.add(setTokenValues(row,Line, number, TokenType.NUMBER_LITERAL));
                } else if (isAlpha(Tokens[row])) {
                    String string = Tokens[row];
                    if (row != Tokens.length) {
                        for (int j = row + 1; j < Tokens.length; j++) {
                            if (isAlpha(Tokens[j])) {
                                ++row;
                                string += (Tokens[row]);
                            } else
                                break;
                        }
                    }
                    if (keywords.get(string) != null) {
                        returnVal.add(setTokenValues(row,Line, string, TokenType.KEYWORD, keywords.get(string)));
                    }
                }
            }
        }

        return returnVal;
    }

    public static Token setTokenValues(int row, int line,String Token, TokenType tokenType) {
        Token token = new Token();
        token.setCol(line);
        token.setRow(row);
        token.setToken(Token);
        token.setTokenType(tokenType);

        return token;
    }

    public static void skip() {}

    public static Token setTokenValues(int row, int line,Object Token, TokenType tokenType, TokenType KeyWordType) {
        Token token = new Token();
        token.setCol(line);
        token.setRow(row);
        token.setToken(Token);
        token.setTokenType(tokenType);
        token.setKeyWordType(KeyWordType);
        return token;
    }

    public static boolean isDigit(String i) {
        return Character.isDigit(i.toCharArray()[0]) && (!Character.isWhitespace(i.toCharArray()[0]));
    }

    public static boolean isAlpha(String i) {
        return Character.isLetter(i.toCharArray()[0]) && (!Character.isWhitespace(i.toCharArray()[0]));
    }

    public static boolean isGreaterThanEqualTo(String[] Tokens,int index){
        String equal = Tokens[index]+Tokens[index+1];
        return equal.equals(">=");
    }

    public static boolean isLessThanEqualTo(String[] Tokens,int index){
        String equal = Tokens[index]+Tokens[index+1];
        return equal.equals("<=");
    }
}