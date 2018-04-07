package in.anukool.architecture.mvvm.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import in.anukool.architecture.mvvm.data.source.Contact;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {
    private static ContactDatabase INSTANSE ;

    private static final Object sLock = new Object();

    public abstract ContactDao contactDao();

    public static ContactDatabase getInstance(Context context){
        synchronized (sLock){
            if(INSTANSE == null){
                INSTANSE = Room.databaseBuilder(context.getApplicationContext(),
                        ContactDatabase.class, "Contacts.db" )
                        .build();
            }
            return INSTANSE;
        }
    }

}
