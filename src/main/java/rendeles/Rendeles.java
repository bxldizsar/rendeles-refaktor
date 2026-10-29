package rendeles;

import java.util.List;

public record Rendeles(String vevo, String email, List<String> termekek, List<Double> arak, String tipus, String kupon)
{
    // kupon nelkuli rendeles, a regi hivasok igy valtozatlanok maradnak
    public Rendeles(String vevo, String email, List<String> termekek, List<Double> arak, String tipus)
    {
        this(vevo, email, termekek, arak, tipus, null);
    }
}