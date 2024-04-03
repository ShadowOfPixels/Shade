package core.LanguageProcessors.ParserComponents;


public class Grouping extends Expression{
    Expression expression;

    public Grouping(Expression expression){
        this.expression = expression;
    }
}
