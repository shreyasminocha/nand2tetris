import java.io.*;

public class Parser {
    BufferedReader br;
    String command;

    public Parser(String file) {
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void advance() throws IOException{
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

            line = line.trim().replaceAll("//.*", "").trim();

            if (line.length() != 0) {
                command = line;
                break;
            }
        }
    }

    public String commandType() {
        if(command.indexOf(" ") == -1) {
            return (command.indexOf("return") == 0) ? "C_RETURN" : "C_ARITHMETIC";
        } else {
            String firstPart = command.toLowerCase().substring(0, command.indexOf(' '));
            switch(firstPart) {
                case "push": return "C_PUSH";
                case "pop": return "C_POP";
                case "label": return "C_LABEL";
                case "goto": return "C_GOTO";
                case "if-goto": return "C_IF";
                case "function": return "C_FUNCTION";
                case "call": return "C_CALL";
                default: return "";
            }
        }
    }

    public String arg1() { //Do not call when the commandtype is "C_RETURN".
        if(commandType() == "C_ARITHMETIC" || commandType() == "C_RETURN") {
            return command;
        } else if(commandType() == "C_LABEL" || commandType() == "C_GOTO" || commandType() == "C_IF") {
            return command.substring(command.indexOf(" ") + 1, command.length());
        } else {
            //System.out.print(command);
            return command.substring(command.indexOf(" ") + 1, command.lastIndexOf(" "));
        }
    }

    public int arg2() {//Call only when the commandtype is "C_PUSH", "C_POP", "C_FUNCTION" or "C_CALL".
        return Integer.parseInt(command.toLowerCase().substring(command.lastIndexOf(" ") + 1, command.length()));
    }
}