package io.github.lyndemberg.clientrss.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.lyndemberg.clientrss.valueobject.Notice;
import io.github.lyndemberg.clientrss.R;

public class NoticesAdapter extends ArrayAdapter<Notice> {

    public NoticesAdapter(Context context, ArrayList<Notice> notices){
        super(context,0, notices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notice item = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notice,parent,false);

        TextView titleView = convertView.findViewById(R.id.titleNotice);
        TextView authorView = convertView.findViewById(R.id.authorNotice);

        titleView.setText(item.getTitle());
        authorView.setText(item.getAuthor());

        return convertView;
    }

}
