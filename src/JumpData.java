public class JumpData {
    private String quad;
    private String zeroVar = "";
    private int originalLine;

    public JumpData(int originalLine) {
        this.originalLine = originalLine;
    }

    public JumpData( String zeroVar, int originalLine) {
        this.zeroVar = zeroVar;
        this.originalLine = originalLine;
    }

    public String getQuad() {
        return quad;
    }

    public void setQuad(String quad) {
        this.quad = quad;
    }

    public String getZeroVar() {
        return zeroVar;
    }

    public void setZeroVar(String zeroVar) {
        this.zeroVar = zeroVar;
    }

    public int getOriginalLine() {
        return originalLine;
    }

    public void setOriginalLine(int originalLine) {
        this.originalLine = originalLine;
    }
}
