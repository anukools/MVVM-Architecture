package in.anukool.architecture.mvvm.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import in.anukool.architecture.mvvm.data.source.Contact;
import in.anukool.architecture.mvvm.data.source.local.ContactLocalDBSource;
import in.anukool.architecture.mvvm.data.source.remote.ContactRemoteDataSource;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
public class ContactRepository implements ContactDataSource {

    private volatile  static ContactRepository INSTANCE = null;

    private final ContactLocalDBSource localDBSource;

    private final ContactRemoteDataSource remoteDataSource;

    List<Contact> mCachedContacts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    public static ContactRepository getInstance(ContactLocalDBSource contactLocalDBSource,
                                                ContactRemoteDataSource contactRemoteDataSource) {
        if (INSTANCE == null) {
            synchronized (ContactRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ContactRepository(contactLocalDBSource, contactRemoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    private ContactRepository(@NonNull ContactLocalDBSource contactLocalDBSource,
                              @NonNull ContactRemoteDataSource contactRemoteDataSource){
            localDBSource =  contactLocalDBSource;
            remoteDataSource = contactRemoteDataSource;
    }



    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {

        // Respond immediately with cache if available and not dirty
        if (mCachedContacts != null && !mCacheIsDirty) {
            callback.onTasksLoaded(mCachedContacts);
            return;
        }

        // If the cache is dirty we need to fetch new data from the network.
        getTasksFromRemoteDataSource(callback);

//        if (mCacheIsDirty) {
//
//        } else {
//            // Query the local storage if available. If not, query the network.
//            localDBSource.getTasks(new LoadTasksCallback() {
//                @Override
//                public void onTasksLoaded(List<Contact> contacts) {
//                    refreshCache(contacts);
//
//                    callback.onTasksLoaded(contacts);
//                }
//
//                @Override
//                public void onDataNotAvailable() {
//                    getTasksFromRemoteDataSource(callback);
//                }
//            });
//        }
    }

    @Override
    public void saveTask(@NonNull Contact contact) {
        remoteDataSource.saveTask(contact);
        localDBSource.saveTask(contact);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedContacts == null) {
            mCachedContacts = new ArrayList<>();
        }
        mCachedContacts.add(contact);
    }

    @Override
    public void deleteAllTasks() {
        remoteDataSource.deleteAllTasks();
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        remoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Contact> contactList) {
                callback.onTasksLoaded(contactList);
            }

            @Override
            public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Contact> tasks) {
        if (mCachedContacts == null) {
            mCachedContacts = new ArrayList<>();
        }
        mCachedContacts.clear();
        mCachedContacts.addAll(tasks);
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Contact> tasks) {
        localDBSource.deleteAllTasks();
        for (Contact task : tasks) {
            localDBSource.saveTask(task);
        }
    }
}
