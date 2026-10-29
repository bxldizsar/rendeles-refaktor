package rendeles;

import java.io.PrintStream;

// adatbazis helyett konzolra ir, a bevetelt peldanyszinten tarolja
public class KonzolosTar implements RendelesTar
{
    private final PrintStream ki;
    private double osszesBevetel = 0;

    public KonzolosTar(PrintStream ki)
    {
        this.ki = ki;
    }

    @Override
    public void ment(Rendeles rendeles, double osszeg)
    {
        ki.println("DB INSERT rendeles " + rendeles.vevo() + " " + osszeg);
        osszesBevetel += osszeg;
    }

    @Override
    public double osszesBevetel()
    {
        return osszesBevetel;
    }
}