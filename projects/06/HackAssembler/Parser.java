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
public class Parser {
    private BufferedReader reader;
    private String command;

    private static Pattern address = Pattern.compile("^@([\\w.$:]+)$");
    private static Pattern loop = Pattern.compile("^\\(([\\w.$:]+)\\)$");
    
    /**
     * Open the input file/stream and prepare to parse it
     * @param  file        Path of the file to parse
     * @throws IOException Error encountered in attempting to read file
     */
    public Parser(String file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        advance(); // load the first command so the user needs to
    }

    /**
     * Read the next command from the input and makes it the current command. If
     * no next command exists, set the current command to <code>null</code>.
     * @throws IOException Error accessing the file
     */
    public void advance() throws IOException {
        String line;
        
        while(true) {
            line = reader.readLine();
            if(line == null) {
                reader.close();
                command = null;
                break;
            }

            line = line.replaceAll("\\s", "").replaceAll("//.*", "");

            if(line.length() > 0) {
                command = line;
                break;
            }
        }
    }

    /**
     * Returns the type of the current command:
     *  <ul>
     *      <li> <code>A_COMMAND</code> for <code>@Xxx</code> where <code>Xxx</code> 
     *      is either a symbol or a decimal number</li>
     *      <li> <code>C_COMMAND</code> for <code>dest=comp;jump</code> </li>
     *      <li> <code>L_COMMAND</code> (actually, pseudo-command) for <code>(Xxx)</code> 
     *      where <code>Xxx</code> is a symbol.</li>
     *  </ul>
     * @return The command type
     */
    public type commandType() {
        if(command.charAt(0) == '(') {
            return type.L_COMMAND;
        } else if(command.charAt(0) == '@') {
            return type.A_COMMAND;
        } else {
            return type.C_COMMAND;
        }
     }

     /**
      * Return the symbol or decimal <code>Xxx</code> of the current command 
      * <code>@Xxx</code> or <code>(Xxx)</code>. Should be called only when the 
      * commond is a valid A command or L command.
      * @return The symbol portion
      * @throws IllegalStateException The command is not a valid A or C command
      */
    public String symbol() throws IllegalStateException {
        Matcher addrMatcher = address.matcher(command);
        Matcher loopMatcher = loop.matcher(command);
        
        if(addrMatcher.matches()) {
            return addrMatcher.group(1);
        } else if(loopMatcher.matches()) {
            return loopMatcher.group(1);
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
    public String comp() throws IllegalStateException {
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
    public String dest() throws IllegalStateException {
        if(commandType() != type.C_COMMAND) {
            throw new IllegalStateException();
        }
        
        if(command.contains("=")) {
            return command.split("=")[0];
        } else {
            return null;
        }
    }

    /**
     * Return the jump mnemonic of the current command. Should be called
     * only when the command is a valid C command.
     * @return The jump mnemonic
     * @throws IllegalStateException The command is not a valid C command
     */
    public String jump() throws IllegalStateException {
        if(commandType() != type.C_COMMAND) {
            throw new IllegalStateException();
        }
        
        if(command.contains(";")) {
            return command.split(";")[1];
        } else {
            return null;
        }
    }

    /**
     * Whether the file has no more commands
     * @return <code>true</code> if no commands remain and vice-versa
     */
    public boolean endOfFile() {
        return command == null;
    }
}
