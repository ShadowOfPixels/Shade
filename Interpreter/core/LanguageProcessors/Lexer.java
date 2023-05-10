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
    public static int LineNo;
    public static ErrorHandler handler;
    List<String> KeyWords = new ArrayList<>();

    public Lexer(String Command, int LineNo, ErrorHandler handler) {
        this.handler = handler;
        this.Command = Command;
        this.LineNo = LineNo;
    }

    public static Token setTokenValues(int row, String Token, TokenType tokenType) {
        Token token = new Token();
        token.setCol(LineNo);
        token.setRow(row);
        token.setToken(Token);
        token.setTokenType(tokenType);

        return token;
    }

    public static void skip() {
    }

    public static Token setTokenValues(int row, Object Token, TokenType tokenType, TokenType KeyWordType) {
        Token token = new Token();
        token.setCol(LineNo);
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

    public static List<Token> Tokenize() {
        List<Token> returnVal = new ArrayList<>();
        String[] Tokens = Command.split("");
        for (int i = 0; i < Tokens.length; i++) {
            TokenType type = TokenType.NULL;
            switch (Tokens[i]) {
                case "*", "/", "+", "-" -> type = TokenType.ARITHEMATIC_OPERATORS;
                case " ", "\t", "\r" -> skip();
                case "{" -> type = TokenType.OPEN_CURLY_BRACK;
                case "}" -> type = TokenType.CLOSE_CURLY_BRACK;
                case "]" -> type = TokenType.OPEN_BOX_BRACK;
                case "[" -> type = TokenType.CLOSE_BOX_BRACK;
                case "(" -> type = TokenType.OPEN_PARENS;
                case ")" -> type = TokenType.CLOSED_PARENS;
                case "\"" -> {
                    ++i;
                    for (int k = i; k < Tokens.length; k++) {
                        if (Tokens[k].equals("\"")) {
                            String string = Command.substring(i, k);
                            returnVal.add(setTokenValues(i, string, TokenType.STRING_LITERAL, keywords.get(string)));
                            i = k + 1;
                            type = TokenType.END;// pseudo value
                            break;
                        } else if (k == Tokens.length - 1) {
                            Token unnecessaryToken = new Token();
                            unnecessaryToken.setToken(Tokens[i]);
                            unnecessaryToken.setRow(k);
                            handler.error(LineNo, unnecessaryToken, Command, "Unterminating String Literal ", false);
                            return null;
                        }
                    }
                }
                case "\'" -> {
                    if (Tokens[i + 2].equals("\'")) {
                        System.out.println(i);
                        returnVal.add(setTokenValues(i, Tokens[i + 1], TokenType.KEYWORD, TokenType.CHARACTER_LITERAL));
                        i = i + 3;
                        type = TokenType.END;// pseudo value
                    } else {
                        Token unnecessaryToken = new Token();
                        unnecessaryToken.setRow(i + 2);
                        handler.error(LineNo, unnecessaryToken, Command, "Unterminating Character Literal ", false);
                        return null;
                    }
                }
                case "=" -> type = TokenType.EQUALS;
                case ";" -> type = TokenType.END;
                case "," -> type = TokenType.COMMA;
                case "!" -> type = TokenType.NOT;
            }

            if (!type.equals(TokenType.NULL)) {
                returnVal.add(setTokenValues(i, Tokens[i], type));
            } else {
                if (isDigit(Tokens[i])) {
                    String number = Tokens[i];
                    if (i != Tokens.length) {
                        for (int j = i + 1; j < Tokens.length; j++) {
                            if (isDigit(Tokens[j]) || Tokens[j].equals(".")) {
                                number = number + (Tokens[j]);
                                i++;
                            } else
                                break;
                        }
                    }
                    returnVal.add(setTokenValues(i, number, TokenType.NUMBER_LITERAL));
                } else if (isAlpha(Tokens[i])) {
                    String string = Tokens[i];
                    if (i != Tokens.length) {
                        for (int j = i + 1; j < Tokens.length; j++) {
                            if (isAlpha(Tokens[j])) {
                                ++i;
                                string += (Tokens[i]);
                            } else
                                break;
                        }
                    }
                    if (keywords.get(string) != null) {
                        returnVal.add(setTokenValues(i, string, TokenType.KEYWORD, keywords.get(string)));
                    } else {
                        Token unnecessaryToken = new Token();
                        unnecessaryToken.setToken(Tokens[i]);
                        unnecessaryToken.setRow(Command.length() - string.length() / 2);
                        handler.error(LineNo, unnecessaryToken, Command, "Unecessary Hanging String " + string, true);
                        return null;
                    }
                } else {
                    Token unnecessaryToken = new Token();
                    unnecessaryToken.setToken(Tokens[i]);
                    unnecessaryToken.setRow(i);
                    handler.error(LineNo, unnecessaryToken, Command, "Unnecessary character present at ", true);
                    return null;
                }
            }
        }
        return returnVal;
    }
}