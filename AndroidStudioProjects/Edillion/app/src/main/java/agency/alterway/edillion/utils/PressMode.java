package agency.alterway.edillion.utils;

/**
 * Created by marekrigan on 27/05/15.
 */
public enum PressMode
{
    /**
     * Type Of Mode for AbsenceActivity
     */
    NONE_SELECTED(55), SELECTED(66), LONG_SELECTED(77), ALL_SELECTED(88), UNKNOWN(99);

    private int value;

    PressMode(int value) {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static PressMode fromInt(int intList) {
        switch(intList)
        {
            case 55:
                return NONE_SELECTED;
            case 66:
                return SELECTED;
            case 77:
                return LONG_SELECTED;
            case 88:
                return ALL_SELECTED;
            default:
                return UNKNOWN;
        }
    }
}
