package in.anukool.architecture.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import in.anukool.architecture.mvvm.data.ContactDataSource;
import in.anukool.architecture.mvvm.data.ContactRepository;
import in.anukool.architecture.mvvm.data.source.Contact;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
public class ContactViewModel extends AndroidViewModel {

    private final Context mContext;

    // These observable fields will update Views automatically
    public final ObservableList<Contact> contactList = new ObservableArrayList<>();

    private final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    public final ObservableBoolean empty = new ObservableBoolean(false);

    private final ContactRepository mContactRepository;

    private final SingleLiveEvent<List<Contact>> singleLiveEvent;



    public ContactViewModel(@NonNull Application application, ContactRepository contactRepository) {
        super(application);
        mContext = application.getApplicationContext();
        mContactRepository = contactRepository;
        singleLiveEvent = new SingleLiveEvent<>();

        Log.e("Called :", "ContactViewModel Constructor");
    }

    public SingleLiveEvent<List<Contact>> getContacts() {
        return singleLiveEvent;
    }

    public void start(){
        loadContactData();
    }

    private void loadContactData(){

        dataLoading.set(true);

        mContactRepository.getTasks(new ContactDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Contact> tasks) {
                singleLiveEvent.setValue(tasks);

                Log.e("Called :", "loadedContactData");
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(false);
            }
        });

    }

    public void updateDataBindingObservables(List<Contact> tasks) {

        Log.e("Called :", "updateDataBindingObservables - " + tasks.size());
        mIsDataLoadingError.set(false);
        dataLoading.set(false);
        contactList.clear();
        contactList.addAll(tasks);
        empty.set(contactList.isEmpty());
    }

}
