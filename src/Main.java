import java.io.FileReader;
import compiler.Lexer;

public class Main {
    public static void main(String[] args) {
        try{
            Lexer lexer = new Lexer(new FileReader("src/input.text"));
            Parser myParser = new Parser(lexer);

            CodeGenerator cg = new CodeGenerator("output.txt");
            myParser.setCodeGenerator(cg);

            myParser.parse();
            cg.writeCodeToFile();
            cg.close();
            System.out.println("success!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}