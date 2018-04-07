package in.anukool.architecture.mvvm.data.source;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */

@Entity(tableName = "Contact")
public final class Contact {

    @Nullable
    @ColumnInfo(name = "name")
    public final String mName;

    @Nullable
    @ColumnInfo(name = "image")
    public final String mImageUrl;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "phone")
    public String mPhoneNumber;

    public Contact(@Nullable String name, @Nullable String imageUrl, @NonNull String phoneNumber){
            mName  = name;
            mImageUrl = imageUrl;
            mPhoneNumber =phoneNumber;
    }

    @Nullable
    public String getmName() {
        return mName;
    }

    @Nullable
    public String getmImageUrl() {
        return mImageUrl;
    }

    @Nullable
    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    @Override
    public String toString() {
        return "Contact with title " + mName;
    }
}
