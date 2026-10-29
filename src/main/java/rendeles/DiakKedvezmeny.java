package rendeles;

public class DiakKedvezmeny implements KedvezmenySzabaly
{
    @Override
    public double alkalmaz(double osszeg, Rendeles rendeles)
    {
        if(rendeles.tipus().equals("DIAK"))
        {
            return osszeg * 0.95;
        }
        return osszeg;
    }
}