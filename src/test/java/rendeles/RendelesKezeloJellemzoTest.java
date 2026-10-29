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
        return new RendelesKezelo(new AlapValidator(), szabalyok, tar, new EmailErtesito(System.out));
    }

    private double bevetel(RendelesKezelo k)
    {
        return tar.osszesBevetel();
    }

    @Test
    public void uresVevoHiba()
    {
        assertEquals("HIBA: vevo", kezelo().feldolgoz("", "a@b.hu", List.of("Pizza"), List.of(100.0), "NORMAL"));
        assertEquals("", kimenet.toString());
    }

    @Test
    public void nullVevoHiba()
    {
        assertEquals("HIBA: vevo", kezelo().feldolgoz(null, "a@b.hu", List.of("Pizza"), List.of(100.0), "NORMAL"));
    }

    @Test
    public void kukacNelkuliEmailHiba()
    {
        assertEquals("HIBA: email", kezelo().feldolgoz("Anna", "anna.hu", List.of("Pizza"), List.of(100.0), "NORMAL"));
        assertEquals("", kimenet.toString());
    }

    @Test
    public void normalRendelesSzamlaEsKimenet()
    {
        String szamla = kezelo().feldolgoz("Anna", "anna@x.hu", List.of("Pizza", "Kola"), List.of(100.0, 50.0), "NORMAL");

        assertEquals("SZAMLA\nPizza 100.0\nKola 50.0\nOsszesen: 150.00", szamla);
        assertEquals("DB INSERT rendeles Anna 150.0" + NL
                + "EMAIL -> anna@x.hu: Koszonjuk a rendelest!" + NL, kimenet.toString());
    }

    @Test
    public void vipTizSzazalek()
    {
        String szamla = kezelo().feldolgoz("Bela", "b@x.hu", List.of("Pizza"), List.of(200.0), "VIP");
        assertEquals("SZAMLA\nPizza 200.0\nOsszesen: 180.00", szamla);
    }

    @Test
    public void diakOtSzazalek()
    {
        String szamla = kezelo().feldolgoz("Cili", "c@x.hu", List.of("Pizza"), List.of(200.0), "DIAK");
        assertEquals("SZAMLA\nPizza 200.0\nOsszesen: 190.00", szamla);
    }

    @Test
    public void otszazFelettHuszLevonas()
    {
        String szamla = kezelo().feldolgoz("Dani", "d@x.hu", List.of("TV"), List.of(600.0), "NORMAL");
        assertEquals("SZAMLA\nTV 600.0\nOsszesen: 580.00", szamla);
    }

    @Test
    public void pontosanOtszazNincsLevonas()
    {
        String szamla = kezelo().feldolgoz("Dani", "d@x.hu", List.of("TV"), List.of(500.0), "NORMAL");
        assertEquals("SZAMLA\nTV 500.0\nOsszesen: 500.00", szamla);
    }

    @Test
    public void vipEsOtszazFelettMindketto()
    {
        // eloszor a szazalek, utana a 20-as levonas: 600 * 0.9 = 540 - 20 = 520
        String szamla = kezelo().feldolgoz("Eva", "e@x.hu", List.of("TV"), List.of(600.0), "VIP");
        assertEquals("SZAMLA\nTV 600.0\nOsszesen: 520.00", szamla);
        assertEquals("DB INSERT rendeles Eva 520.0" + NL
                + "EMAIL -> e@x.hu: Koszonjuk a rendelest!" + NL, kimenet.toString());
    }

    @Test
    public void bevetelOsszeadodik()
    {
        RendelesKezelo k = kezelo();
        k.feldolgoz("Anna", "a@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL");
        k.feldolgoz("Bela", "b@x.hu", List.of("Pizza"), List.of(200.0), "VIP");
        assertEquals(280.0, bevetel(k), 0.0001);
    }

    @Test
    public void hibasRendelesNemNoveliABevetelt()
    {
        RendelesKezelo k = kezelo();
        k.feldolgoz("", "a@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL");
        assertEquals(0.0, bevetel(k), 0.0001);
    }

    @Test
    public void nullEmailNullPointerException()
    {
        // ismert hiba, de most csak rogzitjuk
        assertThrows(NullPointerException.class,
                () -> kezelo().feldolgoz("Anna", null, List.of("Pizza"), List.of(100.0), "NORMAL"));
    }
}