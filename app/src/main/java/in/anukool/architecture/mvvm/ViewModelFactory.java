package in.anukool.architecture.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import in.anukool.architecture.mvvm.data.ContactRepository;
import in.anukool.architecture.mvvm.data.source.local.ContactDatabase;
import in.anukool.architecture.mvvm.data.source.local.ContactLocalDBSource;
import in.anukool.architecture.mvvm.data.source.remote.ContactRemoteDataSource;
import in.anukool.architecture.mvvm.threads.AppExecutors;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    private final ContactRepository mContactRepository;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    Log.e("Called :", "getInstance");
                    INSTANCE = new ViewModelFactory(application,
                            ContactRepository.getInstance(ContactLocalDBSource.getInstance(new AppExecutors(),
                                    ContactDatabase.getInstance(application.getApplicationContext()).contactDao()),
                                    ContactRemoteDataSource.getInstance()));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application, ContactRepository repository) {
        mApplication = application;
        mContactRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactViewModel.class)) {
            return (T) new ContactViewModel(mApplication, mContactRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
