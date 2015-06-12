package agency.alterway.edillion.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marekrigan on 11/06/15.
 */
public class Customer implements Parcelable
{
    private long groupId;
    private String name;
    private boolean isBroker;

    public Customer() {}

    public Customer(long groupId, String name, boolean isBroker)
    {
        this.groupId = groupId;
        this.name = name;
        this.isBroker = isBroker;
    }

    public Customer(Parcel in)
    {
        this.groupId = in.readLong();
        this.name = in.readString();
        this.isBroker = in.readByte() == 1;
    }

    public long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(long groupId)
    {
        this.groupId = groupId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isBroker()
    {
        return isBroker;
    }

    public void setIsBroker(boolean isBroker)
    {
        this.isBroker = isBroker;
    }

    @Override
    public String toString()
    {
        return "Customer{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                ", isBroker=" + isBroker +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(groupId);
        dest.writeString(name);
        dest.writeByte((byte)(isBroker ? 1:0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Customer createFromParcel(Parcel in)
        {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size)
        {
            return new Customer[0];
        }
    };

}
