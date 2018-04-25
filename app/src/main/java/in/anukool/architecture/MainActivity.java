package in.anukool.architecture;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import in.anukool.architecture.databinding.ActivityMainBinding;
import in.anukool.architecture.mvvm.ContactViewModel;
import in.anukool.architecture.mvvm.ViewModelFactory;
import in.anukool.architecture.mvvm.data.source.Contact;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = obtainViewModel(this);

        binding.setViewmodel(mViewModel);


        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ContactsAdapter contactsAdapter = new ContactsAdapter(mViewModel);
        recyclerView.setAdapter(contactsAdapter);


        mViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                mViewModel.updateDataBindingObservables(contacts);
            }
        });

        mViewModel.start();
    }

    public static ContactViewModel obtainViewModel(AppCompatActivity activity) {
        Log.e("Called :", "obtainViewModel");
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        ContactViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(ContactViewModel.class);

        return viewModel;
    }
}
