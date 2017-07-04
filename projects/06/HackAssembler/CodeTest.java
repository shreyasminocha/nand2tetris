import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CodeTest {
    /**
     * Default constructor for test class CodeTest
     */
    public CodeTest() {
        
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        
    }

    @Test
    public void destTest() {
        Code code = new Code();
        String[] input = {"", "M", "D", "MD", "A", "AM", "AD", "AMD"};
        String[] expectedOutput = {"000", "001", "010", "011", "100", "101", "110", "111"};
        
        for(int i = 0; i < input.length; i++) {
            assertEquals(expectedOutput[i], code.dest(input[i]));
        }
    }    
    
    @Test
    public void jumpTest() {
        Code code = new Code();
        String[] input = {"", "JGT", "JEQ", "JGE", "JLT", "JNE", "JLE", "JMP"};
        String[] expectedOutput = {"000", "001", "010", "011", "100", "101", "110", "111"};
        
        for(int i = 0; i < input.length; i++) {
            assertEquals(expectedOutput[i], code.jump(input[i]));
        }
    }
    
    @Test
    public void compTest() {
        Code code = new Code();
        String[] inputAD = {"0", "1", "-1", "D", "A", "!D", "!A", "-D", "-A", "D+1", "A+1", "D-1", "A-1", "D+A", "D-A", "A-D", "D&A", "D|A"};
        String[] inputMD = {"M", "!M", "-M", "M+1", "M-1", "D+M", "D-M", "M-D", "D&M", "D|M"};
        String[] expectedAD = {"0101010", "0111111", "0111010", "0001100", "0110000", "0001101", "0110001", "0001111", "0110011", "0011111", "0110111", "0001110", "0110010", "0000010", "0010011", "0000111", "0000000", "0010101"};
        String[] expectedMD = {"1110000", "1110001", "1110011", "1110111", "1110010", "1000010", "1010011", "1000111", "1000000", "1010101"};
        
        for(int i = 0; i < inputAD.length; i++) {
            assertEquals(expectedAD[i], code.comp(inputAD[i]));
        }
        
        for(int i = 0; i < inputMD.length; i++) {
            assertEquals(expectedMD[i], code.comp(inputMD[i]));
        }
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        
    }
}
