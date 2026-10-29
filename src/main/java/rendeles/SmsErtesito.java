package rendeles;

import java.io.PrintStream;

// telefonszam nincs, ezert a vevo nevere megy
public class SmsErtesito implements Ertesito
{
    private final PrintStream ki;

    public SmsErtesito(PrintStream ki)
    {
        this.ki = ki;
    }

    @Override
    public void ertesit(Rendeles rendeles)
    {
        ki.println("SMS -> " + rendeles.vevo() + ": Koszonjuk a rendelest!");
    }
}
