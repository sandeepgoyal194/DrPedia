package frameworks.customadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.healthhunt.framework.R;

/**
 * Created by sandeepgoyal on 10/05/18.
 */

public class CustomArrayAdapter extends ArrayAdapter<CustomAdapterModel>{
    List<CustomAdapterModel> adapterDataList = new ArrayList<>();

    public CustomArrayAdapter(@NonNull Context context) {
        super(context, R.layout.dropdown_item);
    }
    public CustomArrayAdapter(@NonNull Context context,List<CustomAdapterModel> adapterDataList) {
        super(context, R.layout.dropdown_item);
        this.adapterDataList = adapterDataList;
    }
    public void setAdapterDataList(List<CustomAdapterModel> adapterDataList) {
        this.adapterDataList = adapterDataList;
    }

    @Override
    public int getCount() {
        return adapterDataList.size();
    }

    @Nullable
    @Override
    public CustomAdapterModel getItem(int position) {
        return adapterDataList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dropdown_item, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.textView);
        strName.setText(getItem(position).getText());
        return view;
    }
}
