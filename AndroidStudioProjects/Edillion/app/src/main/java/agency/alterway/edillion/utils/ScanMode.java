package agency.alterway.edillion.utils;

/**
 * Created by marekrigan on 05/05/15.
 */
public enum ScanMode
{
    /**
     * Type Of Mode for AbsenceActivity
     */
    CAMERA_REQUEST(42), SELECT_PICTURE(41), UNKNOWN(0);

    private int value;

    ScanMode(int value) {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static ScanMode fromInt(int intList) {
        switch(intList)
        {
            case 42:
                return CAMERA_REQUEST;
            case 41:
                return SELECT_PICTURE;
            default:
                return UNKNOWN;
        }
    }
}
