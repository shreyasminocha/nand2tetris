import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

/**
 * Translate assembly language commands and write them to a hack file.
 */
public class Main {
    private static String inputPath;  // input file path
    private static String outputPath; // output file path

    /**
     * Accept input file path and write assembled code to output file.
     * @param  args        Command line arguments
     * @throws IOException Error in reading the file
     */
    public static void main(String[] args) throws IOException {
        //////////////////
        // handle files //
        //////////////////

        if(args.length < 1) {
            Scanner ui = new Scanner(System.in);
            System.out.print("Please enter the path of the file to be converted: ");
            inputPath = ui.nextLine();
        } else {
            inputPath = args[0];
        }

        int dot = inputPath.lastIndexOf(".");
        String extension = inputPath.substring(dot + 1);
        String name = inputPath.substring(0, dot);

        if(!extension.equals("asm")) {
            throw new IOException("The input file should have a '.asm' extension");
        }

        if(args.length > 1) {
            outputPath = args[1];
        } else {
            outputPath = name + ".hack";
        }

        /////////////////////////////
        // initialize symbol table //
        /////////////////////////////

        HashMap<String, Integer> symbols = new HashMap<String, Integer>();
        for(int num = 0; num <= 15; num++) {
            symbols.put("R" + num, num);
        }
        symbols.put("SP",         0);
        symbols.put("LCL",        1);
        symbols.put("ARG",        2);
        symbols.put("THIS",       3);
        symbols.put("THAT",       4);
        symbols.put("SCREEN", 16384);
        symbols.put("KBD",    24576);

        ////////////////////////
        // run the first pass //
        ////////////////////////

        Parser firstPass = new Parser(inputPath);
        int lineNumber = 0;

        while(firstPass.isNotOver()) {
            if(firstPass.commandType() == type.L_COMMAND) { // (xxx)
                symbols.put(firstPass.symbol(), lineNumber);
            } else {
                lineNumber++;
            }

            firstPass.advance();
        }

        /////////////////////////
        // run the second pass //
        /////////////////////////

        File outputFile = new File(outputPath);
        outputFile.createNewFile();
        PrintWriter writer = new PrintWriter(outputFile);

        Parser secondPass  = new Parser(inputPath);
        Code code = new Code();
        int unallocatedRegister = 16; // last unallocated register

        while(secondPass.isNotOver()) {
            type commandType = secondPass.commandType();

            if(commandType == type.A_COMMAND) { // @xxx
                String symbol = secondPass.symbol();

                if(symbol.matches("\\d+")) { // address

                    int address = Integer.parseInt(symbol);
                    writer.println(inBinary(address));

                } else if(symbols.containsKey(symbol)) { // predefined symbol or label

                    writer.println(inBinary(symbols.get(symbol)));

                } else { // variable

                    symbols.put(symbol, unallocatedRegister++);
                    writer.println(inBinary(symbols.get(symbol)));

                }
            } else if(commandType == type.C_COMMAND) { // dest=comp;jump
                String comp, dest, jump;

                try {
                    comp = code.comp(secondPass.comp());
                    dest = code.dest(secondPass.dest());
                    jump = code.jump(secondPass.jump());
                } catch(IllegalArgumentException exception) {
                    syntaxError(secondPass.lineNumber(),
                                secondPass.command(),
                                "Invalid C_COMMAND: " + exception.getMessage());

                    writer.close();
                    outputFile.delete();
                    return;
                }

                // [111] + [ccccccc] + [ddd] + [jjj]
                writer.println("111" + comp + dest + jump);
            }

            secondPass.advance();
        }

        System.out.println("The file has successfully been converted to: " + outputPath);
        writer.close();
    }

    /**
     * Convert supplied address to a 16-bit binary string.
     * @param  address                  Address to convert to binary
     * @return                          16-bit binary string
     */
    private static String inBinary(int address) {
        String binary = Integer.toBinaryString(address);
        String manyZeros = "000000000000000" + binary;

        return manyZeros.substring(manyZeros.length() - 16);
    }

    /**
     * Inform the user that a syntax error was encountered.
     * @param lineNumber The line number at which the error was encountered
     * @param line       The erraneous line
     * @param message    A description of the error
     */
    private static void syntaxError(int lineNumber, String line, String message) {
        System.out.println("Invalid syntax at line number " + lineNumber + ", '" + line + "'");
        System.out.println(message);
        System.out.println("Conversion aborted.");
    }
}
