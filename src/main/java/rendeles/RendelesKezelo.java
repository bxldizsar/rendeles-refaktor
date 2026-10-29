package rendeles;

import java.util.List;

public class RendelesKezelo
{
    private final RendelesValidator validator;
    private final List<KedvezmenySzabaly> szabalyok;
    private final RendelesTar tar;
    private final Ertesito ertesito;

    public RendelesKezelo(RendelesValidator validator, List<KedvezmenySzabaly> szabalyok, RendelesTar tar, Ertesito ertesito)
    {
        this.validator = validator;
        this.szabalyok = List.copyOf(szabalyok);
        this.tar = tar;
        this.ertesito = ertesito;
    }

    public String feldolgoz(String vevo, String email, List<String> termekek, List<Double> arak, String tipus)
    {
        String hiba = validator.ellenoriz(vevo, email);
        if(hiba != null)
        {
            return hiba;
        }

        Rendeles rendeles = new Rendeles(vevo, email, termekek, arak, tipus);
        double osszeg = osszegez(termekek, arak);
        osszeg = kedvezmenytAd(osszeg, rendeles);

        tar.ment(rendeles, osszeg);
        ertesito.ertesit(rendeles);
        return szamlatKeszit(termekek, arak, osszeg);
    }

    private double osszegez(List<String> termekek, List<Double> arak)
    {
        double osszeg = 0;
        for(int i = 0; i < termekek.size(); i++)
        {
            osszeg += arak.get(i);
        }
        return osszeg;
    }

    // a szabalyok a megadott sorrendben futnak
    private double kedvezmenytAd(double osszeg, Rendeles rendeles)
    {
        for(KedvezmenySzabaly szabaly : szabalyok)
        {
            osszeg = szabaly.alkalmaz(osszeg, rendeles);
        }
        return osszeg;
    }

    private String szamlatKeszit(List<String> termekek, List<Double> arak, double osszeg)
    {
        StringBuilder sb = new StringBuilder("SZAMLA\n");
        for(int i = 0; i < termekek.size(); i++)
        {
            sb.append(termekek.get(i)).append(" ").append(arak.get(i)).append("\n");
        }
        sb.append("Osszesen: ").append(String.format("%.2f", osszeg));
        return sb.toString();
    }
}