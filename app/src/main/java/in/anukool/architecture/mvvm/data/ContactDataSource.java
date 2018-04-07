package in.anukool.architecture.mvvm.data;

import android.support.annotation.NonNull;

import java.util.List;

import in.anukool.architecture.mvvm.data.source.Contact;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
public interface ContactDataSource  {

    interface LoadTasksCallback {

        void onTasksLoaded(List<Contact> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Contact task);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);

    void saveTask(@NonNull Contact task);

    void deleteAllTasks();

}
