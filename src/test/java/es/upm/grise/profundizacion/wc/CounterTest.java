package es.upm.grise.profundizacion.wc;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CounterTest {

    //DL - Comprueba que los métodos cuentan caracteres, líneas y palabras correctamente
    @Test
    public void testCountCharactersWordsAndLines() throws IOException {
        //DL - sustituido un \n por un \t para cubrir toda la clase de Counter
        String content = "Esta frase\nes un ejemplo para\tel test de recuento.\n";
        BufferedReader reader = new BufferedReader(new StringReader(content));
        
        Counter counter = new Counter(reader);
        
        assertEquals(51, counter.getNumberCharacters());
        assertEquals(2, counter.getNumberLines());
        assertEquals(10, counter.getNumberWords());
    }
   

}

