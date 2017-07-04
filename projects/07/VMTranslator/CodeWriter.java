import java.io.*;
import java.util.regex.*;

public class CodeWriter {
    String currentFile = "";
    int runningEQ = 0;
    int runningGT = 0;
    int runningLT = 0;
    int runningRET = 0;
    String currentFunction = "null";
    FileWriter writer = null;
    public CodeWriter(String newFilePath) throws IOException {
        File nf = new File(newFilePath);
        nf.createNewFile();
        writer = new FileWriter(nf);
    }

    public void setFileName(String fileName) {
        System.out.println(fileName);
        currentFile = fileName;
    }

    public void writeArithmetic(String command) throws IOException {
        switch(command) {
            case "add": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nM=M+D\n@SP\nM=M-1\n"); break;
            case "sub": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nM=M-D\n@SP\nM=M-1\n"); break;
            case "neg": writer.write("@SP\nA=M\nA=A-1\nM=-M\n@SP\nM=M\n"); break;
            case "eq": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nD=M-D\nM=0\n@TRUE_EQ_" + ++runningEQ + "\nD;JEQ\n@SP\nM=M-1\n@CONTINUE_EQ_" + runningEQ + "\n0;JMP\n(TRUE_EQ_" + runningEQ + ")\n@SP\nA=M\nA=A-1\nA=A-1\nM=-1\n@SP\nM=M-1\n(CONTINUE_EQ_" + runningEQ + ")\n"); break;
            case "gt": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nD=M-D\nM=0\n@TRUE_GT_" + ++runningGT + "\nD;JGT\n@SP\nM=M-1\n@CONTINUE_GT_" + runningGT + "\n0;JMP\n(TRUE_GT_" + runningGT + ")\n@SP\nA=M\nA=A-1\nA=A-1\nM=-1\n@SP\nM=M-1\n(CONTINUE_GT_" + runningGT + ")\n"); break;
            case "lt": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nD=M-D\nM=0\n@TRUE_LT_" + ++runningLT + "\nD;JLT\n@SP\nM=M-1\n@CONTINUE_LT_" + runningLT + "\n0;JMP\n(TRUE_LT_" + runningLT + ")\n@SP\nA=M\nA=A-1\nA=A-1\nM=-1\n@SP\nM=M-1\n(CONTINUE_LT_" + runningLT + ")\n"); break;
            case "and": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nM=M&D\n@SP\nM=M-1\n"); break;
            case "or": writer.write("@SP\nA=M\nA=A-1\nD=M\nA=A-1\nM=M|D\n@SP\nM=M-1\n"); break;
            case "not": writer.write("@SP\nA=M\nA=A-1\nM=!M\n@SP\nM=M\n"); break;
        }
    }

    public void writePushPop(String command, String segment, int index) throws IOException {
        if(command == "push") {
            switch(segment) {
                case "constant": writer.write("@" + index + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "argument": writer.write("@" + index + "\nD=A\n@ARG\nA=M\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "local": writer.write("@" + index + "\nD=A\n@LCL\nA=M\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "static": writer.write("@" + currentFile + "." + index + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "this": writer.write("@" + index + "\nD=A\n@THIS\nA=M\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "that": writer.write("@" + index + "\nD=A\n@THAT\nA=M\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "pointer": writer.write("@" + index + "\nD=A\n@3\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
                case "temp": writer.write("@" + index + "\nD=A\n@5\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"); break;
            }
        } else if(command == "pop") {
            switch(segment) {
                case "local": writer.write("@LCL\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n"); break;
                case "argument": writer.write("@ARG\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n"); break;
                case "temp": writer.write("@5\nD=A\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n"); break;
                case "pointer": writer.write("@3\nD=A\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n"); break;
                case "this": writer.write("@THIS\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n"); break;
                case "that": writer.write("@THAT\nD=M\n@" + index + "\nD=D+A\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n"); break;
                case "static": writer.write("@SP\nA=M\nA=A-1\nD=M\n" + "@" + currentFile + "." + index + "\nM=D\n@SP\nM=M-1\n"); break;
                default: writer.write("");
            }
        }
    }

    public void writeLabel(String label) throws IOException {
        writer.write("(" + currentFunction + ":" + label + ")\n");
    }

    public void writeGoto(String label) throws IOException {
        writer.write("@" + currentFunction + ":" + label + "\n0;JMP\n");
    }

    public void writeIf(String label) throws IOException {
        writer.write("@SP\nA=M\nA=A-1\nD=M\n@SP\nM=M-1\n@" + currentFunction + ":" + label + "\nD;JNE\n");
    }
    
    public void writeFunction(String functionName, int numLocals) throws IOException {
        currentFunction = functionName;
        writer.write("(" + functionName + ")\n");
        for(int i = 0; i < numLocals; i++) {
            System.out.println("In local initialization loop.");
            writer.write("@SP\nA=M\nM=0\n@SP\nM=M+1\n");
        }
    }

    public void writeCall(String functionName, int numArgs) throws IOException {
        writer.write("@RETURN_ADDRESS_" + runningRET + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
        writer.write("@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
        writer.write("@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
        writer.write("@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
        writer.write("@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
        writer.write("@SP\nD=M\n@" + numArgs + "\nD=D-A\n@5\nD=D-A\n@ARG\nM=D\n");
        writer.write("@SP\nD=M\n@LCL\nM=D\n");
        writer.write("@" + functionName + "\n0;JMP\n");
        writer.write("(RETURN_ADDRESS_" + runningRET++ + ")\n");
    }

    public void writeReturn() throws IOException {
        writer.write("@LCL\nD=M\n@R11\nM=D\n@5\nA=D-A\nD=M\n@R12\nM=D\n");
        writer.write("@ARG\nD=M\n@R13\nM=D\n@SP\nA=M\nA=A-1\nD=M\n@R13\nA=M\nM=D\n@SP\nM=M-1\n");
        writer.write("@ARG\nD=M\n@SP\nM=D+1\n");
        writer.write("@R11\nD=M\n@1\nD=D-A\nA=D\nD=M\n@THAT\nM=D\n");
        writer.write("@R11\nD=M\n@2\nD=D-A\nA=D\nD=M\n@THIS\nM=D\n");
        writer.write("@R11\nD=M\n@3\nD=D-A\nA=D\nD=M\n@ARG\nM=D\n");
        writer.write("@R11\nD=M\n@4\nD=D-A\nA=D\nD=M\n@LCL\nM=D\n");
        writer.write("@R12\nA=M\n0;JMP\n");
    }

    public void writeInit() throws IOException {
        writer.write("@256\nD=A\n@SP\nM=D\n"); 
        writeCall("Sys.init", 0);
    }
    
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}