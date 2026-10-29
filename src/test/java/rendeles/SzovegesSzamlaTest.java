package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SzovegesSzamlaTest
{
    private Locale eredetiLocale;

    @BeforeEach
    public void elokeszit()
    {
        eredetiLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterEach
    public void visszaallit()
    {
        Locale.setDefault(eredetiLocale);
    }

    @Test
    public void tetelekEsOsszeg()
    {
        Rendeles rendeles = new Rendeles("Anna", "a@x.hu", List.of("Pizza", "Kola"), List.of(100.0, 50.0), "NORMAL");

        // az osszeget kivulrol kapja, nem szamolja ujra
        assertEquals("SZAMLA\nPizza 100.0\nKola 50.0\nOsszesen: 135.50", new SzovegesSzamla().formaz(rendeles, 135.5));
    }
}
