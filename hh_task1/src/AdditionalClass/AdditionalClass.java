package AdditionalClass;

import java.util.List;

/**
 * Created by user on 20.10.15.
 */
public class AdditionalClass {

    public static boolean isNumber(String str)
    {
        if(isDouble(str) || isInt(str))
            return true;
        else
            return false;
    }

    public static boolean isInt(String str)
    {
        try
        {
            Integer.valueOf(str);
        }catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str)
    {
        try {
            Double.valueOf(str);
        }catch (Exception e)
        {
            return false;
        }
        return true;
    }

}
