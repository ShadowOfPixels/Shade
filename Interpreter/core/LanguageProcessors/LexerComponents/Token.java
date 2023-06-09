package core.LanguageProcessors.LexerComponents;

public class Token {

    private int row;
    private int col;
    private Object token;
    private TokenType tokentype;
    private TokenType KeyWordType;

    public void setRow(int i) {
        row = i;
    }

    public void setKeyWordType(TokenType type) {
        KeyWordType = type;
    }

    public void setCol(int i) {
        col = i;
    }

    public void setToken(Object tokens) {
        token = tokens;
    }

    public void setTokenType(TokenType i) {
        tokentype = i;
    }

    public TokenType getTokenType() {
        return tokentype;
    }

    public Object getToken() {
        return token;
    }

    public TokenType getKeyWordType() {
        return KeyWordType;
    }

    public int getRow() {
        return row;
    }
}
