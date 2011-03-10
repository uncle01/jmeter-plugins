package kg.apc.jmeter.modifiers;

import org.apache.jmeter.threads.JMeterContextService;
import kg.apc.emulators.TestJMeterUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undera
 */
public class RawRequestSourcePreProcessorTest {
    private String basedir;

    public RawRequestSourcePreProcessorTest()  {
        String file=RawRequestSourcePreProcessorTest.class.getResource("rawdata_broken.txt").getPath();
        basedir=file.substring(0, file.lastIndexOf("/"));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProcess_crlf_metaline() {
        System.out.println("crlf_metaline");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(basedir+"/rawdata_crlf_metaline.txt");
        instance.setRewindOnEOF(true);
        for (int n=1; n<10; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n+"["+result+"]");
            assertTrue(result.length()>0);
            assertTrue(!(result.startsWith("\n")));
            assertTrue(!(result.startsWith("\r")));
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process zeroterm looped");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(basedir+"/rawdata_zeroterm_looped.txt");
        instance.setRewindOnEOF(true);
        for (int n=1; n<10; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n+" "+result);
            assertTrue(result.length()>0);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_zeroterminated() {
        System.out.println("zeroterm");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(basedir+"/rawdata_zeroterm_looped.txt");
        for (int n=1; n<20; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.out.println(n);
            assertTrue(result.length()>0 || n>=6);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_nonzeroterminated() {
        System.out.println("nonzeroterm");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(basedir+"/rawdata_nonzeroterm.txt");
        for (int n=1; n<20; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            //System.err.println("NZ "+result.length()+" "+n);
            assertTrue(result.length()>0 || n>=6);
        }
    }

    /**
     * Test of process method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testProcess_broken() {
        System.out.println("broken");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(basedir+"/rawdata_broken.txt");
        boolean ok=false;
        for (int n=1; n<20; n++)
        {
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            if(result.length()<1)
            {
               assertEquals(3, n);
               ok=true;
               break;
            }
        }
        assertTrue(ok);
    }

   @Test
    public void testProcess_file_not_found() {
        System.out.println("file_not_found");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName("/nofilefortest");
            instance.process();
            String result = JMeterContextService.getContext().getVariables().get(instance.getVarName());
            assertTrue(result.length()==0);
    }

    /**
     * Test of getVarName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testGetVarName() {
        System.out.println("getVarName");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        String expResult = "";
        String result = instance.getVarName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setVarName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetVarName() {
        System.out.println("setVarName");
        String name = "";
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setVarName(name);
    }

    /**
     * Test of getFileName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        String expResult = "";
        String result = instance.getFileName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFileName method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetFileName() {
        System.out.println("setFileName");
        String filename = "";
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setFileName(filename);
    }

    /**
     * Test of setRewindOnEOF method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testSetRewindOnEOF() {
        System.out.println("setRewindOnEOF");
        boolean isRew = false;
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        instance.setRewindOnEOF(isRew);
    }

    /**
     * Test of getRewindOnEOF method, of class RawRequestSourcePreProcessor.
     */
    @Test
    public void testGetRewindOnEOF() {
        System.out.println("getRewindOnEOF");
        RawRequestSourcePreProcessor instance = new RawRequestSourcePreProcessor();
        boolean expResult = false;
        boolean result = instance.getRewindOnEOF();
        assertEquals(expResult, result);
    }

 }