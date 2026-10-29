package rendeles;

public class NagyRendelesKedvezmeny implements KedvezmenySzabaly
{
    @Override
    public double alkalmaz(double osszeg, Rendeles rendeles)
    {
        if(osszeg > 500)
        {
            return osszeg - 20;
        }
        return osszeg;
    }
}