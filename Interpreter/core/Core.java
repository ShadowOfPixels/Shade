package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.Charset;

import core.ErrorHandling.ErrorHandler;
import core.LanguageProcessors.Lexer;
import core.LanguageProcessors.LexerComponents.Token;

public class Core {

    static ErrorHandler handler = new ErrorHandler();

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String command = new String(bytes, Charset.defaultCharset());
        run(command,command.lines().toArray().length);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (int i=0;;i++) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            else{
                run(line,i);
            }
        }
    }

    private static void run(String source,int LineNo) {
        Lexer lexer = new Lexer(source, LineNo,handler);
        List<Token> lexerOut = lexer.Tokenize();

        if(lexerOut!= null){
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
        System.out.println(args.length);
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