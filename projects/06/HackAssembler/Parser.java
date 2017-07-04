import java.io.*;
import java.util.regex.*;
import java.util.HashMap;

public class Parser {
    private BufferedReader br;
    String command;

    public Parser(String file) {
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void advance() throws IOException {      
        String line;       
        while(true) {
            line = br.readLine();
            if (line == null) {
                try {
                    if (br != null) {
                        br.close();
                        command = null;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {   
                    break;
                }
            }

            line = line.replaceAll("\\s","").replaceAll("//.*", "");

            if (line.length() != 0) {
                command = line;
                break;
            }
        }
    }

    public String commandType() {
        if(command.charAt(0) == '(') {
            return "L_COMMAND";
        } else if(command.charAt(0) == '@') {
            return "A_COMMAND";
        } else {
            return "C_COMMAND";
        }
    }

    public String symbol() {
        if(command.indexOf("@") == -1) {
            return command.replaceAll("\\(", "").replaceAll("\\)", "");
        } else {
            return command.replaceAll("@", "");
        }
    }

    public String comp() {
        String s = command.replaceAll(".*=", "");
        return s.replaceAll(";.*", "");
    }

    public String dest(){
        if(command.indexOf("=") == -1) {
            return "";
        } else {
            return command.replaceAll("=.*", "");
        }
    }

    public String jump() {
        if(command.indexOf(";") == -1) {
            return "";
        } else {
            return command.replaceAll(".*;", "");
        }
    }
}