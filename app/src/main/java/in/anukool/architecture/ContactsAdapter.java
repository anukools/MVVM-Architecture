package in.anukool.architecture;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.anukool.architecture.databinding.RowContactBinding;
import in.anukool.architecture.mvvm.ContactViewModel;
import in.anukool.architecture.mvvm.data.source.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {
    private List<Contact> contactList;

    private ContactViewModel viewModel;

    class ContactHolder extends RecyclerView.ViewHolder {
        private RowContactBinding binding;

        ContactHolder(View view) {
            super(view);
        }

        ContactHolder(RowContactBinding contactBinding) {
            this(contactBinding.getRoot());
            binding = contactBinding;
        }
    }

    public void updateData(List<Contact> tasks) {
        contactList.addAll(tasks);
        notifyDataSetChanged();
    }

    ContactsAdapter(ContactViewModel contactViewModel) {
        this.contactList =  new ArrayList<>();
        viewModel = contactViewModel;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RowContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.row_contact, viewGroup, false);

        return new ContactHolder(binding);
    }


    @Override
    public void onBindViewHolder(ContactHolder holder, final int position) {
        holder.binding.setContact(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
