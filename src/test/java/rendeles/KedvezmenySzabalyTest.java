package rendeles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class KedvezmenySzabalyTest
{
    private Rendeles rendeles(String tipus)
    {
        return new Rendeles("Anna", "a@x.hu", List.of("Pizza"), List.of(100.0), tipus);
    }

    @Test
    public void vipCsakVipnek()
    {
        KedvezmenySzabaly vip = new VipKedvezmeny();
        assertEquals(90.0, vip.alkalmaz(100.0, rendeles("VIP")), 0.0001);
        assertEquals(100.0, vip.alkalmaz(100.0, rendeles("DIAK")), 0.0001);
    }

    @Test
    public void diakCsakDiaknak()
    {
        KedvezmenySzabaly diak = new DiakKedvezmeny();
        assertEquals(95.0, diak.alkalmaz(100.0, rendeles("DIAK")), 0.0001);
        assertEquals(100.0, diak.alkalmaz(100.0, rendeles("NORMAL")), 0.0001);
    }

    @Test
    public void nagyRendelesCsakOtszazFelett()
    {
        KedvezmenySzabaly nagy = new NagyRendelesKedvezmeny();
        assertEquals(481.0, nagy.alkalmaz(501.0, rendeles("NORMAL")), 0.0001);
        assertEquals(500.0, nagy.alkalmaz(500.0, rendeles("NORMAL")), 0.0001);
    }
}