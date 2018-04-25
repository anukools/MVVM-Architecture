package in.anukool.architecture.mvvm.data.source.remote;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.anukool.architecture.MyApplication;
import in.anukool.architecture.mvvm.Constants;
import in.anukool.architecture.mvvm.data.ContactDataSource;
import in.anukool.architecture.mvvm.data.source.Contact;
import in.anukool.architecture.mvvm.data.source.ContactModel;

/**
 * Created by Anukool Srivastav on 02/04/18.
 */
public class ContactRemoteDataSource implements ContactDataSource {

    private static ContactRemoteDataSource INSTANCE;

    private List<Contact> contacts;
    private List<ContactModel> contactModelList;

    private ContactRemoteDataSource(){}

    public  static ContactRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ContactRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        JsonArrayRequest request = new JsonArrayRequest(Constants.URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            callback.onDataNotAvailable();
                        }else {
                            contactModelList = new Gson().fromJson(response.toString(), new TypeToken<List<ContactModel>>(){}.getType());

                            // Getting Iterator

                            contacts = new ArrayList<>();
                            // Traversing elements
                            for (ContactModel model : contactModelList) {
                                contacts.add(new Contact(model.getName(),model.getImage(), model.getPhone()));
                            }

                            callback.onTasksLoaded(contacts);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                callback.onDataNotAvailable();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void saveTask(@NonNull Contact contact) {
        contacts.add(contact);
    }

    @Override
    public void deleteAllTasks() {
        contacts.clear();
    }
}
