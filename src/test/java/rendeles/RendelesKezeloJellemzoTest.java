package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// jellemzo tesztek: a jelenlegi viselkedest rogzitik, nem azt, hogy mi lenne helyes
public class RendelesKezeloJellemzoTest
{
    private static final String NL = System.lineSeparator();

    private final ByteArrayOutputStream kimenet = new ByteArrayOutputStream();
    private PrintStream eredetiOut;
    private Locale eredetiLocale;
    private RendelesTar tar;

    @BeforeEach
    public void elokeszit()
    {
        eredetiOut = System.out;
        System.setOut(new PrintStream(kimenet, true));
        // a %.2f a locale-tol fugg, magyar gepen vesszot irna
        eredetiLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterEach
    public void visszaallit()
    {
        System.setOut(eredetiOut);
        Locale.setDefault(eredetiLocale);
    }

    // a refaktor soran csak ez a ket metodus valtozhat
    private RendelesKezelo kezelo()
    {
        List<KedvezmenySzabaly> szabalyok = List.of(new VipKedvezmeny(), new DiakKedvezmeny(), new NagyRendelesKedvezmeny());
        tar = new KonzolosTar(System.out);
        return new RendelesKezelo(new AlapValidator(), szabalyok, tar);
    }

    private double bevetel(RendelesKezelo k)
    {
        return tar.osszesBevetel();
    }
}