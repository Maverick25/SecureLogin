package agency.alterway.edillion.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by marekrigan on 22/05/15.
 */
public class DocumentFile implements Parcelable
{
    private long id;
    private String description;
    private boolean isApproved;
    private int thumbnail;

    public DocumentFile() {}

    public DocumentFile(long id, String description, boolean isApproved)
    {
        this.id = id;
        this.description = description;
        this.isApproved = isApproved;
    }

    public DocumentFile(Parcel in)
    {
        this.id = in.readLong();
        this.isApproved = in.readByte() == 1;
        this.description = in.readString();
    }

    public long getId() {
        return id;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeLong(id);
        out.writeByte((byte) (isApproved ? 1 : 0));
        out.writeString(description);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public DocumentFile createFromParcel(Parcel in) {
            return new DocumentFile(in);
        }

        public DocumentFile[] newArray(int size) {
            return new DocumentFile[size];
        }
    };
}
