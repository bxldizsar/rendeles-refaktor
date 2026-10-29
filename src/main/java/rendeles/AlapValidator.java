package rendeles;

public class AlapValidator implements RendelesValidator
{
    @Override
    public String ellenoriz(String vevo, String email)
    {
        if(vevo == null || vevo.isEmpty())
        {
            return "HIBA: vevo";
        }
        if(!email.contains("@"))
        {
            return "HIBA: email";
        }
        return null;
    }
}