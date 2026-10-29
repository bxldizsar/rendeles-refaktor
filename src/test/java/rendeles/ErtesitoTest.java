package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ErtesitoTest
{
    private final Ertesito ertesito = mock(Ertesito.class);

    private RendelesKezelo kezelo()
    {
        RendelesTar tar = new KonzolosTar(new PrintStream(new ByteArrayOutputStream()));
        return new RendelesKezelo(new AlapValidator(), List.of(), tar, ertesito, new SzovegesSzamla());
    }

    @Test
    public void sikeresRendelesnelEgyErtesites()
    {
        kezelo().feldolgoz("Anna", "anna@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL");

        Rendeles vart = new Rendeles("Anna", "anna@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL");
        verify(ertesito, times(1)).ertesit(vart);
    }

    @Test
    public void hibasRendelesnelNincsErtesites()
    {
        kezelo().feldolgoz("Anna", "anna.hu", List.of("Pizza"), List.of(100.0), "NORMAL");

        verify(ertesito, never()).ertesit(any());
    }

    @Test
    public void smsAVevoNevereMegy()
    {
        ByteArrayOutputStream kimenet = new ByteArrayOutputStream();
        Ertesito sms = new SmsErtesito(new PrintStream(kimenet, true));

        sms.ertesit(new Rendeles("Anna", "anna@x.hu", List.of("Pizza"), List.of(100.0), "NORMAL"));

        assertEquals("SMS -> Anna: Koszonjuk a rendelest!" + System.lineSeparator(), kimenet.toString());
    }
}
