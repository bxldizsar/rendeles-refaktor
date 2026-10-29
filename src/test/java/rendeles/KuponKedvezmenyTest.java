package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public class KuponKedvezmenyTest
{
    private final KedvezmenySzabaly kupon = new KuponKedvezmeny("OSZ10", 10);

    private Rendeles rendeles(String kod)
    {
        return new Rendeles("Anna", "a@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL", kod);
    }

    @Test
    public void joKodraLevon()
    {
        assertEquals(90.0, kupon.alkalmaz(100.0, rendeles("OSZ10")), 0.0001);
    }

    @Test
    public void rosszVagyHianyzoKodraNemLevon()
    {
        assertEquals(100.0, kupon.alkalmaz(100.0, rendeles("TAVASZ")), 0.0001);
        assertEquals(100.0, kupon.alkalmaz(100.0, rendeles(null)), 0.0001);
    }

    @Test
    public void nullaAlaNemMegy()
    {
        assertEquals(0.0, kupon.alkalmaz(5.0, rendeles("OSZ10")), 0.0001);
    }

    @Test
    public void kezeloKuponnalIsMukodik()
    {
        Locale eredeti = Locale.getDefault();
        Locale.setDefault(Locale.US);
        try
        {
            PrintStream csend = new PrintStream(new ByteArrayOutputStream());
            // a meglevo szabalyok valtozatlanok, csak egy uj elem kerul a listaba
            List<KedvezmenySzabaly> szabalyok = List.of(new VipKedvezmeny(), new DiakKedvezmeny(),
                    new NagyRendelesKedvezmeny(), new KuponKedvezmeny("OSZ10", 10));
            RendelesKezelo kezelo = new RendelesKezelo(new AlapValidator(), szabalyok, new KonzolosTar(csend),
                    new EmailErtesito(csend), new SzovegesSzamla());

            String szamla = kezelo.feldolgoz("Anna", "a@x.hu", List.of("Pizza"), List.of(200.0), "VIP", "OSZ10");

            // 200 * 0.9 = 180, kupon -10 = 170
            assertEquals("SZAMLA\nPizza 200.0\nOsszesen: 170.00", szamla);
        }
        finally
        {
            Locale.setDefault(eredeti);
        }
    }
}