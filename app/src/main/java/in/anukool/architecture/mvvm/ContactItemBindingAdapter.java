package in.anukool.architecture.mvvm;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.anukool.architecture.ContactsAdapter;
import in.anukool.architecture.mvvm.data.source.Contact;

/**
 * Created by Anukool Srivastav on 05/04/18.
 */
public class ContactItemBindingAdapter {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Contact> items) {
        ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            Log.e("Called :", " ContactItemBindingAdapter setItems    " + items.size());
            adapter.updateData(items);
        }
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
