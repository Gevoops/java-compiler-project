package lexing;


public class LexingError {
    private static boolean error = false;

    public LexingError (String message) {

    }

    public static boolean getError(){
        return error;
    }

    public static void reportError(String message){
        error = true;
        System.err.println(message);
    }

}
