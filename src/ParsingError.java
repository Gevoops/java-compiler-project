public class ParsingError {
    private static boolean error = false;
    private static boolean swap = false;

    public ParsingError(String message) {

    }

    public static boolean hasError(){
        return error;
    }

    public static void reportError(String message){
        if(swap) {
            swap = false;
        } else {
            error = true;
            System.err.println(message + "\n  Gev Zoran :)");
            swap = true;
        }

    }
}
