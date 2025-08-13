import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeGenerator {
    private final FileWriter writer;
    private final Map<String,String> typeTable = new HashMap<>();
    private static int tempVarCounter = 1;
    private final Stack<JumpData> jumpZStack = new Stack<>();
    private final Stack<JumpData> jumpStack = new Stack<>();
    private final Stack<Integer> breakJumpStack = new Stack<>();
    private final List<String> code = new ArrayList<>();
    private String switchVar;
    public int caseLine = -1;
    public String caseNum;

    public CodeGenerator(String name) throws IOException {
        writer = new FileWriter(name);
        pushBreakStack(-2);
    }

    public String binaryOp(String op, String op1, String op2) {
        String res = genTemp();
        String ope = op;
        switch (op) {
            case "+" -> op = "ADD";
            case "-" -> op = "SUB";
            case "*" -> op = "MLT";
            case "/" -> op = "DIV";
            case "==" -> op = "EQL";
            case "!=" -> op = "NQL";
            case "<", ">=" -> op = "LSS";
            case ">", "<=" -> op = "GRT";
        }

        boolean isFloat = true;
        if(getType(op1).equals(getType(op2))){
            if (getType(op1).equals("int")) {
                isFloat = false;
                emitQuad("I" + op, res , op1 , op2);
            } else {
                emitQuad("R" + op, res , op1 , op2);
            }
        } else {
            String temp1 = genTemp();
            declare(temp1,"float");
            if(getType(op1).equals("int")) {
                emitQuad("ITOR", temp1, op1);
                emitQuad("R" + op, res, temp1 , op2);
            } else {
                emitQuad("ITOR", temp1, op2);
                emitQuad("R" + op, res, op1, temp1);
            }
        }
        if (isFloat) {
            declare(res,"float");
        } else {
            declare(res, "int");
        }
        if(ope.equals("<=") || ope.equals(">=")) {
            emitQuad("ISUB",res , "1" , res);
        }
        return res;
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
        if(operand.matches("-?\\d+")) {
            return "int";
        }
        if(operand.matches("-?\\d+\\.\\d+")) {
            return "float";
        }
        String type = typeTable.get(operand);
        if (type == null) {
            throw new RuntimeException("Variable (gev) not declared: " + operand);
        }
        return type;
    }

    public void emitQuad(String... args) {
        StringBuilder quad = new StringBuilder();
        for (String str : args) {
            quad.append(str).append(" ");
        }
        code.add(quad.toString().trim() + "\n");
    }

    public void editQuad(int line, String... args) {
        if(line < 0 ) {
            return;
        }
        StringBuilder quad = new StringBuilder();
        for (String str : args) {
            quad.append(str).append(" ");
        }
        code.set(line - 1,  quad.toString().trim() + "\n");
    }

    public void writeCodeToFile() throws IOException{
        for (String line : code) {
            writer.write(line);
        }
    }

    public void close() throws IOException {
        writer.close();
    }

    public void dummyJump(int line) {
        JumpData data = new JumpData(line);
        data.setQuad("JUMP");
        emitQuad("JUMP", "dummy");
        jumpStack.push(data);
    }
    public void dummyJumpZ(String var, int line) {
        JumpData data = new JumpData(var, line);
        data.setQuad("JMPZ");
        emitQuad("JMPZ",  "dummy", var);
        jumpZStack.push(data);
    }
    public void patchJump(){
        JumpData data = jumpStack.pop();
        editQuad(data.getOriginalLine(), data.getQuad(),
                "" +  getCurrentLine());
    }

    public void patchJumpZ(int offset){
        JumpData data = jumpZStack.pop();
        editQuad(data.getOriginalLine(), data.getQuad(),
                "" +  (getCurrentLine() + offset), data.getZeroVar());
    }

    public void patchJumpZ(){
        JumpData data = jumpZStack.pop();
        editQuad(data.getOriginalLine(), data.getQuad(),"" +  getCurrentLine(), data.getZeroVar());
    }

    public int getCurrentLine(){
        return code.size() + 1;
    }

    public void pushJumpStack(JumpData data){
        jumpStack.push(data);
    }

    public JumpData popJumpStack(){
        return jumpStack.pop();
    }

    public void pushBreakStack(int line){
        breakJumpStack.push(line);
    }

    public int popBreakStack(){
        return breakJumpStack.pop();
    }

    public int topBreakStack(){
        return breakJumpStack.peek();
    }

    public String getSwitchVar() {
        return switchVar;
    }

    public void setSwitchVar(String switchVar) {
        this.switchVar = switchVar;
    }
}
