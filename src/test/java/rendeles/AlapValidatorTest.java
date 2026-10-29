package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class AlapValidatorTest
{
    private final RendelesValidator validator = new AlapValidator();

    @Test
    public void joAdatokraNullt()
    {
        assertNull(validator.ellenoriz("Anna", "anna@x.hu"));
    }

    @Test
    public void eloszorAVevotNezi()
    {
        // ha mindketto rossz, a vevo hibat kapjuk
        assertEquals("HIBA: vevo", validator.ellenoriz("", "rossz"));
    }
}