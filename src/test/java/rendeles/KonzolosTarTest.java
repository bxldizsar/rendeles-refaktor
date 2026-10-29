package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.Test;

public class KonzolosTarTest
{
    private final Rendeles rendeles = new Rendeles("Anna", "a@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL");

    @Test
    public void menteskorKiirjaEsOsszeadja()
    {
        ByteArrayOutputStream kimenet = new ByteArrayOutputStream();
        RendelesTar tar = new KonzolosTar(new PrintStream(kimenet, true));

        tar.ment(rendeles, 100.0);
        tar.ment(rendeles, 50.0);

        assertEquals(150.0, tar.osszesBevetel(), 0.0001);
        assertEquals("DB INSERT rendeles Anna 100.0" + System.lineSeparator()
                + "DB INSERT rendeles Anna 50.0" + System.lineSeparator(), kimenet.toString());
    }

    @Test
    public void ketTarNemKeveredik()
    {
        // static allapotnal ez elbukott volna
        RendelesTar elso = new KonzolosTar(new PrintStream(new ByteArrayOutputStream()));
        RendelesTar masodik = new KonzolosTar(new PrintStream(new ByteArrayOutputStream()));

        elso.ment(rendeles, 100.0);

        assertEquals(100.0, elso.osszesBevetel(), 0.0001);
        assertEquals(0.0, masodik.osszesBevetel(), 0.0001);
    }
}