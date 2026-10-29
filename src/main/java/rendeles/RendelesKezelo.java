package rendeles;

import java.util.List;

public class RendelesKezelo
{
    public static double osszesBevetel = 0;

    private final RendelesValidator validator;

    public RendelesKezelo(RendelesValidator validator)
    {
        this.validator = validator;
    }

    public String feldolgoz(String vevo, String email, List<String> termekek, List<Double> arak, String tipus)
    {
        String hiba = validator.ellenoriz(vevo, email);
        if(hiba != null)
        {
            return hiba;
        }

        double osszeg = osszegez(termekek, arak);
        osszeg = kedvezmenytAd(osszeg, tipus);

        ment(vevo, osszeg);
        ertesit(email);
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

    private double kedvezmenytAd(double osszeg, String tipus)
    {
        if(tipus.equals("VIP"))
        {
            osszeg = osszeg * 0.9;
        }
        else if(tipus.equals("DIAK"))
        {
            osszeg = osszeg * 0.95;
        }
        if(osszeg > 500)
        {
            osszeg -= 20;
        }
        return osszeg;
    }

    private void ment(String vevo, double osszeg)
    {
        System.out.println("DB INSERT rendeles " + vevo + " " + osszeg);
        osszesBevetel += osszeg;
    }

    private void ertesit(String email)
    {
        System.out.println("EMAIL -> " + email + ": Koszonjuk a rendelest!");
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