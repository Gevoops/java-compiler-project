public class SemanticError {
    private static boolean error = false;

    public SemanticError (String message) {

    }

    public static boolean hasError(){
        return error;
    }

    public static void reportError(String message){
        error = true;
        System.err.println(message + "\n  Gev Zoran :)");
    }
}
