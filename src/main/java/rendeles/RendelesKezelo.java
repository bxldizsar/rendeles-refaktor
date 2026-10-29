package rendeles;

import java.util.List;

public class RendelesKezelo
{
    public static double osszesBevetel = 0;

    public String feldolgoz(String vevo, String email, List<String> termekek, List<Double> arak, String tipus)
    {
        if(vevo == null || vevo.isEmpty())
        {
            return "HIBA: vevo";
        }
        if(!email.contains("@"))
        {
            return "HIBA: email";
        }

        double osszeg = 0;
        for(int i = 0; i < termekek.size(); i++)
        {
            osszeg += arak.get(i);
        }

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

        System.out.println("DB INSERT rendeles " + vevo + " " + osszeg);
        System.out.println("EMAIL -> " + email + ": Koszonjuk a rendelest!");
        osszesBevetel += osszeg;

        StringBuilder sb = new StringBuilder("SZAMLA\n");
        for(int i = 0; i < termekek.size(); i++)
        {
            sb.append(termekek.get(i)).append(" ").append(arak.get(i)).append("\n");
        }
        sb.append("Osszesen: ").append(String.format("%.2f", osszeg));
        return sb.toString();
    }
}