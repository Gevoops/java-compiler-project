import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {
    private final FileWriter writer;
    private final Map<String,String> typeTable = new HashMap<>();
    private static int tempVarCounter = 1;

    public CodeGenerator(String name) throws IOException {
        writer = new FileWriter(name);
    }

    public String binaryOp(String op, String op1, String op2) throws IOException {
        String res = genTemp();
        String ope = op;
        switch (op) {
            case "+" -> op = "ADD";
            case "-" -> op = "SUB";
            case "*" -> op = "MLT";
            case "/" -> op = "DIV";
            case "==" -> op = "EQL";
            case "!=" -> op = "NQL";
            case "<", "<=" -> op = "LSS";
            case ">", ">=" -> op = "GRT";
        }

        String res1 = genTemp();
        boolean isFloat = true;
        if(getType(op1).equals(getType(op2))){
            if (getType(op1).equals("int")) {
                isFloat = false;
                emitQuad("I" + op, res , op1 , op2);
                grtEqlHelper(ope,op1,op2,res1,res,"I");
            } else {
                emitQuad("R" + op, res , op1 , op2);
                grtEqlHelper(ope,op1,op2,res1,res,"R");
            }
        } else {
            String temp1 = genTemp();
            declare(temp1,"float");
            if(getType(op1).equals("int")) {
                emitQuad("ITOR", temp1, op1);
                emitQuad("R" + op, res, temp1 , op2);
                grtEqlHelper(ope,temp1,op2,res1,res,"R");
            } else {
                emitQuad("ITOR", temp1, op2);
                emitQuad("R" + op, res, op1, temp1);
                grtEqlHelper(ope,op1,temp1,res1,res,"R");
            }
        }
        if (isFloat) {
            declare(res,"float");
        } else {
            declare(res, "int");
        }
        return res;
    }

    private void grtEqlHelper(String op,String op1,String op2, String res1, String res , String type) throws IOException{
        if(op.equals("<=") || op.equals(">=")) {
            emitQuad(type + "EQL", res1, op1 , op2);
            emitQuad("IADD", res, res1, res);
            emitQuad( "IGRT", res, res , "0");
            emitQuad("IPRT",res);
        }
    }

    public String genTemp(){
        return "t" + tempVarCounter++;
    }

    public void declare(String var, String type) {
        if (typeTable.containsKey(var)){
            throw new RuntimeException("variable already declared" + var);
        } else {
            typeTable.put(var,type);
        }
    }

    public String getType(String operand) {
        if(operand.matches("\\d+")) {
            return "int";
        }
        if(operand.matches("\\d+\\.\\d+")) {
            return "float";
        }
        String type = typeTable.get(operand);
        if (type == null) {
            throw new RuntimeException("Variable (gev) not declared: " + operand);
        }
        return type;
    }

    public void emitQuad(String... args) throws IOException {
        StringBuilder quad = new StringBuilder();
        for (String str : args) {
            quad.append(str).append(" ");
        }
        writer.write(quad.toString().trim() + "\n");
    }

    public void close() throws IOException {
        writer.close();
    }


}
