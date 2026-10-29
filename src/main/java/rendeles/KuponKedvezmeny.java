package rendeles;

public class KuponKedvezmeny implements KedvezmenySzabaly
{
    private final String kod;
    private final double levonas;

    public KuponKedvezmeny(String kod, double levonas)
    {
        this.kod = kod;
        this.levonas = levonas;
    }

    @Override
    public double alkalmaz(double osszeg, Rendeles rendeles)
    {
        if(kod.equals(rendeles.kupon()))
        {
            // nulla ala nem megyunk
            return Math.max(0, osszeg - levonas);
        }
        return osszeg;
    }
}