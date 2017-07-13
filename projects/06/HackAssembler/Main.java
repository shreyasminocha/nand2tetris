import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represent the three possible command types.
 */
enum type {
    A_COMMAND, // @xxx
    C_COMMAND, // dest=comp;jmp
    L_COMMAND  // (xxx)
}

/**
 * Translate assembly language commands and write them to a hack file.
 */
public class Main {
    private static String input;  // input file path
    private static String output; // output file path

    /**
     * Accept input file path and write assembled code to output file.
     * @param  args        Command line arguments
     * @throws IOException Error in reading the file
     */
    public static void main(String[] args) throws IOException {
        input  = args[0];
        
        if(args.length > 1) {
            output = args[1];
        } else {
            output = input.replaceAll("\\.asm", ".hack");
        }

        Code code = new Code();
        HashMap<String, Integer> symbols = new HashMap<String, Integer>(); /** Symbol table **/

        // initialize symbol table
        for(int i = 0; i <= 15; i++) {
            symbols.put("R" + i, i);
        }
        symbols.put("SCREEN", 16384);
        symbols.put("KBD",    24576);
        symbols.put("SP",         0);
        symbols.put("LCL",        1);
        symbols.put("ARG",        2);
        symbols.put("THIS",       3);
        symbols.put("THAT",       4);

        // run the first pass
        Parser fp = new Parser(input); /** Parser for first pass */
        int pc = 0;
        
        while(!fp.endOfFile()) {
            if(fp.commandType().equals(type.L_COMMAND)) { // (xxx)
                symbols.put(fp.symbol(), pc);
            } else {
                pc++;
            }

            fp.advance();
        }

        // run the second pass
        Parser sp = new Parser(input); /** Parser for second pass */
        FileWriter writer;
        int vacantRegister = 16; /** Last unallocated register **/

        File outputFile = new File(output);
        outputFile.createNewFile();
        writer = new FileWriter(outputFile);

        while(!sp.endOfFile()) {
            type commandType = sp.commandType();
            
            if(commandType.equals(type.A_COMMAND)) { // @xxx
                String symbol = sp.symbol();
                
                if(symbol.matches("\\d+")) { // address
                    // convert to binary, write
                    writer.write(inBinary(Integer.parseInt(symbol)));
                    writer.write("\n");
                } else if(symbols.get(symbol) == null) { // variable
                    symbols.put(symbol, vacantRegister++);
                    writer.write(inBinary(symbols.get(symbol)));
                    writer.write("\n");
                } else { // predefined symbol or label
                    writer.write(inBinary(symbols.get(symbol)));
                    writer.write("\n");
                }
            } else if(commandType.equals(type.C_COMMAND)) { // dest=comp;jump
                String dest = code.dest(sp.dest());
                String comp = code.comp(sp.comp());
                String jump = code.jump(sp.jump());
                
                // [111] + [ccccccc] + [ddd] + [jjj]
                writer.write("111" + comp + dest + jump);
                writer.write("\n");
            }
            
            sp.advance();
        }

        writer.flush();
        writer.close();
    }

    /**
     * Convert supplied address to a 16-bit binary string.
     * @param  address                  Address to convert to binary
     * @return                          16-bit binary string
     * @throws IllegalArgumentException Out of unsigned 16-bit range
     */
    private static String inBinary(int address) throws IllegalArgumentException {
        if(address > 65535 || address < 0) {
            throw new IllegalArgumentException(address + " not in range 0–65535");
        }

        // convert and zeropad the address
        String manyZeros = "000000000000000" + Integer.toBinaryString(address);
        return manyZeros.substring(manyZeros.length() - 16);
    }
}
