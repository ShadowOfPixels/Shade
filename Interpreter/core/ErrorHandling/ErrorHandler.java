package core.ErrorHandling;

import core.LanguageProcessors.LexerComponents.Token;

public class ErrorHandler {
    
    public static void error(int line,int row,Token token,String command,String msg){
        report(line, token, command, msg);
    }

    private static  void report(int line,Token token,String command,String msg){
        System.out.println("[Error] "+msg);
        System.out.println("    "+line+"|"+command);
        int tokenPoint = ("    "+line+"|").length()+token.getRow();
        String arrow="";
        String here = "";
        for(int i=0;i<tokenPoint;i++){
            arrow+=" ";
        }
        System.out.println(arrow+"^--Here.");
    }
}
