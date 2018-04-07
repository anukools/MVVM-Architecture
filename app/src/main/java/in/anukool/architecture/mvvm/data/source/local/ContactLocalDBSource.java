package in.anukool.architecture.mvvm.data.source.local;

import android.support.annotation.NonNull;

import java.util.List;

import in.anukool.architecture.mvvm.data.ContactDataSource;
import in.anukool.architecture.mvvm.data.source.Contact;
import in.anukool.architecture.mvvm.threads.AppExecutors;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
public class ContactLocalDBSource implements ContactDataSource {


    private static volatile ContactLocalDBSource INSTANCE;

    private ContactDao mContactDao;

    private AppExecutors appExecutors;

    private ContactLocalDBSource(@NonNull AppExecutors executors, @NonNull ContactDao contactDao){
        appExecutors = executors;
        mContactDao = contactDao;
    }

    public static ContactLocalDBSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull ContactDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (ContactLocalDBSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ContactLocalDBSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Contact> tasks = mContactDao.getTasks();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTasksLoaded(tasks);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveTask(@NonNull final Contact task) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mContactDao.insertTask(task);
            }
        };
        appExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteAllTasks() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mContactDao.deleteTasks();
            }
        };

        appExecutors.diskIO().execute(deleteRunnable);

    }
}
