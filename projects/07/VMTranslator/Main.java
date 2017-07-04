import java.io.*;
import java.util.regex.*;

public class Main {
    String path = null;
    CodeWriter cw = null;
    Boolean isFolder = false;
    String writingTo = null;
    public Main(String pathParam) throws IOException{
        path = pathParam;
        if(Pattern.compile("\\.vm").matcher(pathParam).find()) {
            writingTo = path.replaceAll(".vm", ".asm");
        } else {
            writingTo = path + path.substring(path.lastIndexOf('/'), path.length()) + ".asm";
            isFolder = true;
        }
        cw = new CodeWriter(writingTo);
    }

    public void main() throws IOException, Exception {
        if(isFolder) {
            File file = new File(path);
            cw.writeInit();
            for(final File child : file.listFiles()) {
                if(Pattern.compile("\\.vm").matcher(child.getPath()).find()) {
                    Parser p = new Parser(child.getPath());
                    cw.setFileName(child.getPath().substring(child.getPath().lastIndexOf("\\") + 1, child.getPath().lastIndexOf(".vm")));
                    System.out.println(cw.currentFile);
                    p.advance();
                    while(p.command != null) {
                        switch(p.commandType()) {
                            case "C_ARITHMETIC": cw.writeArithmetic(p.arg1()); break;
                            case "C_PUSH": cw.writePushPop("push", p.arg1(), p.arg2()); break;
                            case "C_POP": cw.writePushPop("pop", p.arg1(), p.arg2()); break;
                            case "C_LABEL": cw.writeLabel(p.arg1()); break;
                            case "C_GOTO": cw.writeGoto(p.arg1()); break;
                            case "C_IF": cw.writeIf(p.arg1()); break;
                            case "C_FUNCTION": cw.writeFunction(p.arg1(), p.arg2()); break;
                            case "C_CALL": cw.writeCall(p.arg1(), p.arg2()); break;
                            case "C_RETURN": cw.writeReturn(); break;
                        }
                        p.advance();
                    }
                }
            }
            cw.close();
        } else {
            Parser p = new Parser(path);
            cw.setFileName(path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf(".vm")));
            cw.writeInit();
            p.advance();
            while(p.command != null) {
                switch(p.commandType()) {
                    case "C_ARITHMETIC": cw.writeArithmetic(p.arg1()); break;
                    case "C_PUSH": cw.writePushPop("push", p.arg1(), p.arg2()); break;
                    case "C_POP": cw.writePushPop("pop", p.arg1(), p.arg2()); break;
                    case "C_LABEL": cw.writeLabel(p.arg1()); break;
                    case "C_GOTO": cw.writeGoto(p.arg1()); break;
                    case "C_IF": cw.writeIf(p.arg1()); break;
                    case "C_FUNCTION": cw.writeFunction(p.arg1(), p.arg2()); break;
                    case "C_CALL": cw.writeCall(p.arg1(), p.arg2()); break;
                    case "C_RETURN": cw.writeReturn(); break;
                }
                p.advance();
            }
            cw.close();
        }
    }
}