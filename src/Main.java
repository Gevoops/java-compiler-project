import java.io.FileReader;
import lexing.Lexer;
import lexing.LexingError;

public class Main {
    public static void main(String[] args) {
        try{
            Lexer lexer = new Lexer(new FileReader("src/input.text"));
            Parser myParser = new Parser(lexer);

            CodeGenerator cg = new CodeGenerator("output.txt");
            myParser.setCodeGenerator(cg);
            myParser.parse();

            if(!LexingError.hasError() && !ParsingError.hasError() & !SemanticError.hasError()) {
                cg.writeCodeToFile();
                System.out.println("success!");
            }

            cg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}