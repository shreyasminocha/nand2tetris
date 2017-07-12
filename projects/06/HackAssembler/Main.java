import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represent one of the three possible command types
 */
enum type {
    A_COMMAND,
    C_COMMAND,
    L_COMMAND
}

/**
 * Write the translated assembly language commands to a hack file.
 */
class Main {
    private static String inputFile;
    private static String outputFile;
    private static int pc = 0;

    /**
     * Accept input file path and write assembled code to output file
     * @param  args        Command line arguments
     * @throws IOException Error in reading the file
     */
    public static void main(String[] args) throws IOException {
        inputFile  = args[0];
        
        if(args.length > 1) {
            outputFile = args[1];
        } else {
            outputFile = inputFile.replaceAll("\\.asm", ".hack");
        }

        Parser fp = new Parser(inputFile); /** Parser for first pass */
        Parser sp = new Parser(inputFile); /** Parser for second pass */
        Code code = new Code();
        HashMap<String, Integer> symbols = new HashMap<String, Integer>(); /** Symbol table **/

        // initialize symbol table
        for(int i = 0; i <= 15; i++) { symbols.put("R" + i, i); }
        symbols.put("SCREEN", 16384);
        symbols.put("KBD", 24576);
        symbols.put("SP", 0);
        symbols.put("LCL", 1);
        symbols.put("ARG", 2);
        symbols.put("THIS", 3);
        symbols.put("THAT", 4);

        // run the first pass
        while(fp.command() != null) {
            if(fp.commandType().equals(type.L_COMMAND)) {
                symbols.put(fp.symbol(), pc);
            } else {
                pc++;
            }

            fp.advance();
        }

        // run the second pass
        FileWriter writer;
        int vacantRegister = 16; // last register allocated to a variable

        File nf = new File(outputFile);
        nf.createNewFile();
        writer = new FileWriter(nf);

        // run the second pass
        while(sp.command() != null) {
            type commandType = sp.commandType();
            if(commandType.equals(type.A_COMMAND)) {
                String symbol = sp.symbol();
                // only digits
                if(sp.symbol().matches("\\d+")) {
                    writer.write(inBinary(Integer.parseInt(symbol))); // convert to binary, write
                    writer.write("\n");
                } else if(symbols.get(symbol) == null) {
                    symbols.put(symbol, vacantRegister++);
                    
                    writer.write(inBinary(symbols.get(symbol)));
                    writer.write("\n");
                } else {
                    writer.write(inBinary(symbols.get(symbol)));
                    writer.write("\n");
                }
            } else if(commandType.equals(type.C_COMMAND)) {
                String c = code.comp(sp.comp());
                String d = code.dest(sp.dest());
                String j = code.jump(sp.jump());

                writer.write("111" + c + d + j);
                writer.write("\n");
            }
            sp.advance();
        }

        writer.flush();
        writer.close();
    }

    /**
     * Convert supplied address to a 16-bit binary string
     * @param  addr                     Address to convert to binary
     * @return                          16-bit binary string
     * @throws IllegalArgumentException Out of unsigned 16-bit range
     */
    private static String inBinary(int addr) throws IllegalArgumentException {
        if(addr > 65535 || addr < 0) {
            throw new IllegalArgumentException(addr + " not in range 0--65535");
        }

        String manyZeros = "000000000000000" + Integer.toBinaryString(addr);
        return manyZeros.substring(manyZeros.length() - 16);
    }
}
