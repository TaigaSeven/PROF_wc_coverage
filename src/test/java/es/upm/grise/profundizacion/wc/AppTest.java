package es.upm.grise.profundizacion.wc;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {
    private static String fileContent = "kjdbvws wonvwofjw\n sdnfwijf ooj    kjndfohwouer 21374 vehf\n jgfosj\n\nskfjwoief ewjf\n\n\ndkfgwoihgpw vs wepfjwfin";
    private static Path testFile = Paths.get("ejemplo.txt");

    private static int nCharacters;
    private static int nLines;
    private static int nWords;

    private ByteArrayOutputStream output;
    private PrintStream originalOutput;

    @BeforeAll
    public static void setup() throws IOException {
        Files.writeString(testFile, fileContent);
    
        //DL - Contar caracteres, líneas y palabras para facilitar asserts posteriores
        BufferedReader reader = new BufferedReader(new StringReader(fileContent));
        Counter counter = new Counter(reader);
        nCharacters = counter.getNumberCharacters();
        nLines = counter.getNumberLines();
        nWords = counter.getNumberWords();
    }

    @AfterAll
    public static void teardown() {
        try {
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            System.err.println("Error deleting test file: " + e.getMessage());
            try {
                Thread.sleep(100);
                Files.deleteIfExists(testFile);
            } catch (IOException | InterruptedException ex) {
                System.err.println("Failed to delete test file on retry: " + ex.getMessage());
            }
        }
    }

    //DL - Redirigir System.out para capturar la salida del programa
    @BeforeEach
    public void setUpStreams(){
        originalOutput = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    //DL - Restaurar System.out después de cada test
    @AfterEach
    public void restoreStreams(){
        System.setOut(originalOutput);
    }

    //DL - Comprueba que salta mensaje cuando no hay argumentos
    @Test
    public void testUsageMessageWhenNoArgs() {
        App.main(new String[] {});
        assertEquals("Usage: wc [-clw file]\n".trim(), output.toString().trim());
    }

    //DL - Comprueba que salta mensaje los argumentos son != 2
    @Test
    public void testErrorMessageWhenWrongArgs() {
        App.main(new String[] {"-c"});
        assertEquals("Wrong arguments!", output.toString().trim());
    }

    //DL - Comprueba que salta mensaje cuando no existe el fichero proporcionado
    @Test
    public void testErrorMessageWhenNoFile() {
        App.main(new String[] {"-c","wrong.txt"});
        assertEquals("Cannot find file: wrong.txt", output.toString().trim());
    }

    //DL - Comprueba que cuenta número de caracteres con comando c
    @Test
    public void testCountsCharactersCCommand() {
        App.main(new String[] {"-c","ejemplo.txt"});
        assertEquals(nCharacters + "\tejemplo.txt", output.toString().trim());
    }

    //DL - Comprueba que cuenta número de líneas con comando l
    @Test
    public void testCountsLinesLCommand() {
        App.main(new String[] {"-l","ejemplo.txt"});
        assertEquals(nLines + "\tejemplo.txt", output.toString().trim());
    }

    //DL - Comprueba que cuenta número de palabras con comando w
    @Test
    public void testCountsWordsWCommand() {
        App.main(new String[] {"-w","ejemplo.txt"});
        assertEquals(nWords + "\tejemplo.txt", output.toString().trim());
    }

    //DL - Comprueba que cuenta todo con comando clw
    @Test
    public void testCountsAllCLWCommand() {
        App.main(new String[] {"-clw","ejemplo.txt"});
        assertEquals(nCharacters + "\t" + nLines + "\t" + nWords + "\tejemplo.txt", output.toString().trim());
    }

    //DL - Comprueba que salta mensaje cuando no existe el comando introducido
    @Test
    public void testErrorMessageWrongCommand() {
        App.main(new String[] {"-q","ejemplo.txt"});
        assertEquals("Unrecognized command: q", output.toString().trim());
    }

    //DL - Comprueba que salta mensaje cuando no lleva "-" en el comando
    @Test
    public void testErrorMessageNoDashCommand() {
        App.main(new String[] {"c","ejemplo.txt"});
        assertEquals("The commands do not start with -", output.toString().trim());
    }
}
