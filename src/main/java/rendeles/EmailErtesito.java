package rendeles;

import java.io.PrintStream;

public class EmailErtesito implements Ertesito
{
    private final PrintStream ki;

    public EmailErtesito(PrintStream ki)
    {
        this.ki = ki;
    }

    @Override
    public void ertesit(Rendeles rendeles)
    {
        ki.println("EMAIL -> " + rendeles.email() + ": Koszonjuk a rendelest!");
    }
}
