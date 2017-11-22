import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulate access to the input code. Read an assembly language command,
 * parse it, and provide convenient access to the command’s components
 * (fields and symbols). In addition, remove all white space and comments.
 */
class Parser {
    private BufferedReader reader;
    private String command;
    private int lineNumber;

    private static Pattern addressPattern = Pattern.compile("^@([\\w.$:]+)$");
    private static Pattern loopPattern    = Pattern.compile("^\\(([\\w.$:]+)\\)$");

    /**
     * Open the input file/stream and prepare to parse it
     * @param  file        Path of the file to parse
     * @throws IOException Error encountered in attempting to read file
     */
    public Parser(String file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        advance();
    }

    /**
     * Read the next command from the input and makes it the current command. If
     * no next command exists, set the current command to `null`.
     * @throws IOException Error accessing the file
     */
    public void advance() throws IOException {
        String line;

        // keep reading lines till one that is not entirely whitespace/comments is found
        do {
            line = reader.readLine();

            if(line == null) {
                reader.close();
                break;
            }

            lineNumber++;
            line = line.replaceAll("\\s", "").replaceAll("//.*", "");
        } while(line.length() == 0);

        command = line;
    }

    /**
     * Return the most recent command
     * @return The command
     */
    public String command() {
        if(!isNotOver()) {
            throw new IllegalStateException("All commands have already been read.");
        }

        return command;
    }

    public int lineNumber() {
        return lineNumber;
    }

    /**
     * Return the type of the current command:
     *  <ul>
     *      <li> `A_COMMAND` for `@xxx` where `xxx` is either a symbol or a decimal number</li>
     *      <li> `C_COMMAND` for `dest=comp;jump`</li>
     *      <li> `L_COMMAND` for `(xxx)` where `xxx` is a symbol.</li>
     *  </ul>
     * @return The command type
     */
    public type commandType() {
        Matcher addrM = addressPattern.matcher(command);
        Matcher loopM = loopPattern.matcher(command);

        if(loopM.matches()) {
            return type.L_COMMAND;
        } else if(addrM.matches()) {
            return type.A_COMMAND;
        } else { // perhaps add a check for this as well
            return type.C_COMMAND;
        }
     }

     /**
      * Return the symbol or decimal `xxx` of the current command
      * `@xxx` or `(xxx)`. Should be called only when the commond is a valid 'A command' or 'L command'.
      * @return The symbol portion
      * @throws IllegalStateException The command is not a valid A or C command
      */
    public String symbol() {
        Matcher addrM = addressPattern.matcher(command);
        Matcher loopM = loopPattern.matcher(command);

        if(addrM.matches()) {
            return addrM.group(1);
        } else if(loopM.matches()) {
            return loopM.group(1);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Return the computation mnemonic of the current command. Should be called
     * only when the command is a valid C command.
     * @return The computation mnemonic
     * @throws IllegalStateException the command is not a valid C command
     */
    public String comp() {
        if(commandType() != type.C_COMMAND) {
            throw new IllegalStateException();
        }

        if(command.contains("=") && command.contains(";")) {
            return command.split("=")[1].split(";")[0];
        } else if(command.contains("=")) {
            return command.split("=")[1];
        } else if(command.contains(";")) {
            return command.split(";")[0];
        } else {
            return command;
        }
    }

    /**
     * Returns the destination mnemonic of the current command. Should be called
     * only when the command is a valid C command.
     * @return The destination mnemonic
     * @throws IllegalStateException The command is not a valid C command
     */
    public String dest() {
        if(commandType() != type.C_COMMAND) {
            throw new IllegalStateException();
        }

        if(command.contains("=")) {
            return command.split("=")[0];
        } else {
            return "";
        }
    }

    /**
     * Return the jump mnemonic of the current command. Should be called
     * only when the command is a valid C command.
     * @return The jump mnemonic
     * @throws IllegalStateException The command is not a valid C command
     */
    public String jump() {
        if(commandType() != type.C_COMMAND) {
            throw new IllegalStateException();
        }

        if(command.contains(";")) {
            return command.split(";")[1];
        } else {
            return "";
        }
    }

    /**
     * Whether the file has no more commands
     * @return `true` if no commands remain and vice-versa
     */
    public boolean isNotOver() {
        return command != null;
    }
}
