import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulate access to the input code. Read an assembly language command, 
 * parse it, and provide convenient access to the command’s components 
 * (fields and symbols). In addition, remove all white space and comments.
 */
class Parser {
    private BufferedReader br;
    private String command;

    private static Pattern address = Pattern.compile("^@([\\w.$:]+)$");
    private static Pattern loop = Pattern.compile("^\\(([\\w.$:]+)\\)$");

    public Parser(String file) throws IOException {
        br = new BufferedReader(new FileReader(file));
        advance();
    }

    public void advance() throws IOException {
        String line;
        while(true) {
            line = br.readLine();
            if(line == null) {
                br.close();
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

    public type commandType() {
        if(command.charAt(0) == '(') {
            return type.L_COMMAND;
        } else if(command.charAt(0) == '@') {
            return type.A_COMMAND;
        } else {
            return type.C_COMMAND;
        }
     }

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

    public String comp() {
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

    public String dest(){
        if(command.contains("=")) {
            return command.split("=")[0];
        } else {
            return null;
        }
    }

    public String jump() {
        if(command.contains(";")) {
            return command.split(";")[1];
        } else {
            return null;
        }
    }

    public String command() {
        return command;
    }
}
