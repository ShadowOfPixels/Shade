package core.ErrorHandling;

import core.Core;
import core.LanguageProcessors.LexerComponents.Token;

public class ErrorHandler {

    public static void error(int line, Token token, String command, String msg, boolean arrow) {
        report(line, token, command, msg, arrow);
    }

    private static void report(int line, Token token, String command, String msg, boolean arg) {
        Core.hadError = true;

        System.err.println("[Error] " + msg);
        System.err.println("    " + line + "|" + command);
        int tokenPoint = ("    " + line + "|").length() + token.getRow();
        if (arg) {
            String arrow = "";
            String here = "";
            for (int i = 0; i < tokenPoint; i++) {
                arrow += " ";
            }
            System.err.println(arrow + "^--Here.");
        }
    }
}
