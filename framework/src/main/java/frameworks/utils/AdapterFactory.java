package frameworks.utils;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import javax.inject.Inject;

import frameworks.customadapter.CustomAdapterModel;
import frameworks.customadapter.CustomArrayAdapter;
import frameworks.di.qualifier.ApplicationContext;
import in.healthhunt.framework.R;
/**
 * Created by sandeep on 7/5/18.
 */
public class AdapterFactory {

    Context context;

    @Inject
    public AdapterFactory(@ApplicationContext Context context) {
        this.context = context;
    }


    public ArrayAdapter<CharSequence> getGenderAdapter() {
        return ArrayAdapter.createFromResource(context,
                R.array.gender, R.layout.dropdown_item);

    }

    public ArrayAdapter<CharSequence> getRelationShipAdapter() {
        return ArrayAdapter.createFromResource(context,
                R.array.relation_ship_type, R.layout.dropdown_item);

    }

    public ArrayAdapter getCustomAdapter(List<CustomAdapterModel> customAdapterModels) {
        return new CustomArrayAdapter(context,
                customAdapterModels);

    }
}
