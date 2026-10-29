package rendeles;

public class VipKedvezmeny implements KedvezmenySzabaly
{
    @Override
    public double alkalmaz(double osszeg, Rendeles rendeles)
    {
        if(rendeles.tipus().equals("VIP"))
        {
            return osszeg * 0.9;
        }
        return osszeg;
    }
}