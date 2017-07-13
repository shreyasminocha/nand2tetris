import java.util.HashMap;

/** 
 * Translate Hack assembly language mnemonics into binary codes.
 */
public class Code {
    private HashMap<String, String> cMap = new HashMap<String, String>();

    public Code() {
        // populate the computation symbol table
        cMap.put("0", "0101010"); cMap.put("1", "0111111"); cMap.put("-1", "0111010");
        cMap.put("D", "0001100"); cMap.put("A", "0110000"); cMap.put("M", "1110000");
        cMap.put("!D", "0001101"); cMap.put("!A", "0110001"); cMap.put("!M", "1110001");
        cMap.put("-D", "0001111"); cMap.put("-A", "0110011"); cMap.put("-M", "1110011");
        cMap.put("D+1", "0011111"); cMap.put("A+1", "0110111"); cMap.put("M+1", "1110111");
        cMap.put("D-1", "0001110"); cMap.put("A-1", "0110010"); cMap.put("M-1", "1110010");
        cMap.put("D+A", "0000010"); cMap.put("D+M", "1000010");
        cMap.put("D-A", "0010011"); cMap.put("D-M", "1010011");
        cMap.put("A-D", "0000111"); cMap.put("M-D", "1000111");
        cMap.put("D&A", "0000000"); cMap.put("D&M", "1000000");
        cMap.put("D|A", "0010101"); cMap.put("D|M", "1010101");
    }

    /**
     * Convert the dest portion to its binary equivalent.
     * @param  destPortion The destination portion of the command
     * @return             Binary equivalent of the destination
     */
    public String dest(String destPortion) {
        if(destPortion == null) {
            return "000";
        }
        
        switch(destPortion) {
            case "A": 
                return "100";
            case "M": 
                return "001";
            case "D": 
                return "010";
            case "AM": 
                return "101";
            case "AD": 
                return "110";
            case "MD": 
                return "011";
            case "AMD": 
                return "111";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Convert the comp portion to its binary equivalent.
     * @param  compPortion The computation portion of the command
     * @return             Binary equivalent of the computation
     */
    public String comp(String compPortion) {
        if(cMap.get(compPortion) == null) {
            throw new IllegalArgumentException();
        }
        
        return cMap.get(compPortion);
    }

    /**
     * Convert the jump portion to its binary equivalent.
     * @param  jumpPortion The jump portion of the command
     * @return             Binary equivalent of the jump condition
     */
    public String jump(String jumpPortion) {
        if(jumpPortion == null) {
            return "000";
        }
        
        switch(jumpPortion) {
            case "JLT": 
                return "100";
            case "JLE": 
                return "110";
            case "JEQ": 
                return "010";
            case "JGE": 
                return "011";
            case "JGT": 
                return "001";
            case "JNE": 
                return "101";
            case "JMP": 
                return "111";
            default:
                throw new IllegalArgumentException();
        }
    }
}
