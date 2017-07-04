import java.io.*;
import java.lang.*;
import java.util.regex.*;
import java.util.HashMap;

public class Main {
    private String file;
    private int pc = 0;
    public Main(String fin) {
        file = fin;
    }

    public void main() throws IOException, Exception {  
        //CREATE INSTANCES OF OTHER MODULES
        Parser fp = new Parser(file);
        Parser sp = new Parser(file);
        Code code = new Code();
        HashMap<String, String> st = new HashMap<String, String>();

        //sYMBOL TABLE INITIALIZATION
        st.put("R0", "0"); st.put("R1", "1"); st.put("R2", "2"); st.put("R3", "3"); st.put("R4", "4"); st.put("R5", "5"); st.put("R6", "6"); st.put("R7", "7");
        st.put("R8", "8"); st.put("R9", "9"); st.put("R10", "10"); st.put("R11", "11"); st.put("R12", "12"); st.put("R13", "13"); st.put("R14", "14"); st.put("R15", "15");
        st.put("SCREEN", "16384"); st.put("KBD", "24576");
        st.put("SP", "0"); st.put("LCL", "1"); st.put("ARG", "2"); st.put("THIS", "3"); st.put("THAT", "4");

        //FIRST PASS
        fp.advance();
        while(fp.command != null) {   
            if(fp.commandType() == "L_COMMAND") {
                st.put(fp.symbol(), Integer.toString(pc));
                pc--;
            }
            fp.advance();
            pc++;
        }

        //SECOND PASS
        FileWriter writer = null;
        int rAllocation = 16; //Keeps a record of the last register allocated to a variable.

        try {
            //CREATE FILE, FILE WRITER
            File nf = new File(file.replaceAll("\\.asm", ".hack"));
            nf.createNewFile();
            writer = new FileWriter(nf);

            //SECOND PASS
            sp.advance();
            while(sp.command != null) {
                if(sp.commandType() == "L_COMMAND") {
                    //Do nothing.
                } else if(sp.commandType() == "A_COMMAND") {
                    if(!(Pattern.compile("[a-zA-Z]").matcher(sp.symbol()).find())) { //If the symbol consists of only digits.
                        writer.write(convertAddr(sp.symbol()) + "\n"); //Translate integer value to binary, write to file.
                    } else if(st.get(sp.symbol()) == null){
                        st.put(sp.symbol(), Integer.toString(rAllocation)); //Assign the variable an unoccupied register. 
                        rAllocation++;
                        writer.write(convertAddr(st.get(sp.symbol())) + "\n");  //Retrieve the just allocated value from SymbolTable, translate to binary, write.
                    } else {
                        writer.write(convertAddr(st.get(sp.symbol())) + "\n");  //Retrieve value of symbol from SymbolTable, translate to binary, write.
                    }
                } else if(sp.commandType() == "C_COMMAND") {
                    String d = code.dest(sp.dest());
                    String c = code.comp(sp.comp());
                    String j = code.jump(sp.jump());

                    writer.write("111" + c + d + j + "\n");
                }
                sp.advance();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            //CLOSE WRITER
            writer.flush();
            writer.close();
        }
    }

    public String convertAddr(String addr) throws Exception{
        String bin;
        String zeroPad = "";
        if(addr != null) {
            bin = Integer.toBinaryString(Integer.parseInt(addr));
            for(int i = 0; i < (16 - bin.length()); i++) {
                zeroPad += "0";
            }
            return zeroPad + bin; 
        } else {
            throw new Exception("Null Parameter.");
        }

    }
}