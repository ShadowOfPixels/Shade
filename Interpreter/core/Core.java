package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.nio.charset.Charset;

import core.ErrorHandling.ErrorHandler;
import core.LanguageProcessors.Lexer;
import core.LanguageProcessors.LexerComponents.Token;

public class Core {

    static ErrorHandler handler = new ErrorHandler();
    public static boolean hadError = false;

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String command = new String(bytes, Charset.defaultCharset());
        run(command, command.lines().toArray().length);
        if (hadError)
            System.exit(65);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (int i = 0;; i++) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            else {
                run(line, i);
                hadError = false;
            }
        }
    }

    private static void run(String source, int totalLines) {
        List<Token> lexerOut = new ArrayList<>();
        for (int i = 0; i < source.lines().toArray().length; i++) {
            Lexer lexer = new Lexer(source.lines().toArray()[i].toString(), i, handler);
            if (lexer.Tokenize() != null) {
                lexerOut.addAll(lexer.Tokenize());
            } else {
                return;
            }
        }

        if (lexerOut != null) {
            for (Token token : lexerOut) {
                System.out.println("************TOKENS***********");
                System.out.println("->" + token.getToken());
                System.out.println("->" + token.getTokenType());
                System.out.println("->" + token.getKeyWordType());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // TESTING LEXER
        if (args.length < 1) {
            System.out.println("usage: JShade [Script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
}