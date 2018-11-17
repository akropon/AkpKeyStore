package com.akropon.akpkeystore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BundlesListAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    List<Bundle> listOfBundles;
    Context context;

    public BundlesListAdapter(Context context, @NonNull List<Bundle> listOfBundles) {
        super();
        this.listOfBundles = listOfBundles;
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listOfBundles.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfBundles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.bundle_for_list, parent, false);
        }

        Bundle bundle = (Bundle) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textview_title);
        textView.setText(bundle.getName());
        //((TextView) view.findViewById(R.id.textview_title)).setText(bundle.getName());
        ((TextView) view.findViewById(R.id.textview_description)).setText(bundle.getDescription());

        return view;
    }
}
