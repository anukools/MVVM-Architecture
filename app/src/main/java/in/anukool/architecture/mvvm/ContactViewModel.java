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
    public final ObservableList<Contact> contacts = new ObservableArrayList<>();

    private final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private final ObservableBoolean empty = new ObservableBoolean(false);

    private final ContactRepository mContactRepository;

    private final SingleLiveEvent<String> singleLiveEvent = new SingleLiveEvent<>();



    public ContactViewModel(@NonNull Application application, ContactRepository contactRepository) {
        super(application);
        mContext = application.getApplicationContext();
        mContactRepository = contactRepository;

        Log.e("Called :", "ContactViewModel Constructor");
    }

    SingleLiveEvent<String> getContacts() {
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
                mIsDataLoadingError.set(false);
                updateDataBindingObservables(tasks);

                Log.e("Called :", "loadedContactData");
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(false);
            }
        });

    }

    private void updateDataBindingObservables(List<Contact> tasks) {

        Log.e("Called :", "updateDataBindingObservables - " + tasks.size());

        dataLoading.set(false);
        contacts.clear();
        contacts.addAll(tasks);
        empty.set(contacts.isEmpty());
    }

}
