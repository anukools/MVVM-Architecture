package in.anukool.architecture.mvvm.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import in.anukool.architecture.mvvm.data.source.Contact;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */

@Dao
public interface ContactDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Contact")
    List<Contact> getTasks();


    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param contact the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Contact contact);

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM Contact")
    void deleteTasks();
}
