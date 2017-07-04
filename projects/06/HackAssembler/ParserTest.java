import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class ParserTest {
    /**
     * Default constructor for test class ParserTest
     */
    public ParserTest() {

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
    public void add() throws IOException{
        Parser p = new Parser("Z:/nand2tetris/projects/06/add/Add.asm");
        p.advance();
        assertEquals("A_COMMAND", p.commandType());
        assertEquals("2", p.symbol());
        p.advance();
        assertEquals("C_COMMAND", p.commandType());
        assertEquals("D", p.dest());
        assertEquals("A", p.comp());
        assertEquals("", p.jump());
        p.advance();
        p.advance();
        assertEquals("D+A", p.comp());
        
        while(p.command != null) {
            p.advance();
        }
    }

    @Test
    public void maxL() throws IOException {
        Parser p = new Parser("Z:/nand2tetris/projects/06/max/MaxL.asm");
        p.advance();
        assertEquals("A_COMMAND", p.commandType());
        assertEquals("0", p.symbol());
        p.advance();
        assertEquals("C_COMMAND", p.commandType());
        assertEquals("D", p.dest());
        assertEquals("M", p.comp());
        assertEquals("", p.jump());
        p.advance();
        p.advance();
        p.advance();
        p.advance();
        assertEquals("JGT", p.jump());
    }

    @Test
    public void max() throws IOException {
        Parser p = new Parser("Z:/nand2tetris/projects/06/max/Max.asm");
        p.advance();
        assertEquals("A_COMMAND", p.commandType());
        assertEquals("R0", p.symbol());
        
        for(int i = 0; i < 4; i++) {
            p.advance();
        }
        
        assertEquals("A_COMMAND", p.commandType());
        assertEquals("OUTPUT_FIRST", p.symbol());
        
        for(int i = 0; i < 6; i++) {
            p.advance();
        }
        
        assertEquals("L_COMMAND", p.commandType());
        assertEquals("OUTPUT_FIRST", p.symbol());
        
        for(int i = 0; i < 3; i++) {
            p.advance();
        }
        
        assertEquals("L_COMMAND", p.commandType());
        assertEquals("OUTPUT_D", p.symbol());
        
        for(int i = 0; i < 3; i++) {
            p.advance();
        }
        
        assertEquals("L_COMMAND", p.commandType());
        assertEquals("INFINITE_LOOP", p.symbol());
        
        p.advance();
        
        assertEquals("A_COMMAND", p.commandType());
        assertEquals("INFINITE_LOOP", p.symbol());
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
