package agency.alterway.edillion.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marekrigan on 11/06/15.
 */
public class User implements Parcelable
{
    private String email;
    private String fullname;

    public User() {}

    public User(String email, String fullname)
    {
        this.email = email;
        this.fullname = fullname;
    }

    public User(Parcel in)
    {
        this.email = in.readString();
        this.fullname = in.readString();
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFullName()
    {
        return fullname;
    }

    public void setFullName(String fullname)
    {
        this.fullname = fullname;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
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
        dest.writeString(email);
        dest.writeString(fullname);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        @Override
        public User createFromParcel(Parcel source)
        {
            return new User(source);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };
}
