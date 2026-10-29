package rendeles;

public class SzovegesSzamla implements SzamlaFormazo
{
    @Override
    public String formaz(Rendeles rendeles, double osszeg)
    {
        StringBuilder sb = new StringBuilder("SZAMLA\n");
        for(int i = 0; i < rendeles.termekek().size(); i++)
        {
            sb.append(rendeles.termekek().get(i)).append(" ").append(rendeles.arak().get(i)).append("\n");
        }
        // a %.2f a locale-tol fugg, ezt most nem javitjuk
        sb.append("Osszesen: ").append(String.format("%.2f", osszeg));
        return sb.toString();
    }
}
