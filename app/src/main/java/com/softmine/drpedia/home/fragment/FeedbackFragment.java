package com.softmine.drpedia.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import com.softmine.drpedia.R;
import com.softmine.drpedia.home.IFeedbackView;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.model.FeedbackItem;
import com.softmine.drpedia.home.presentor.FeedbackPresentor;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import frameworks.di.component.HasComponent;

public class FeedbackFragment extends Fragment implements IFeedbackView, MaterialSpinner.OnItemSelectedListener {

    View fragmentView;
    @BindView(R.id.spinner)
    MaterialSpinner type_spinner;

    @BindView(R.id.postTitle)
    EditText feedback_title;

    @BindView(R.id.postDesc)
    EditText feedback_desc;

    String select_type;

    @Inject
    FeedbackPresentor feedbackPresentor;

    String[] some_array;

    public FeedbackFragment()
    {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(CaseStudyComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        fragmentView =  inflater.inflate(R.layout.activity_feedback, container, false);
        ButterKnife.bind(this, fragmentView);
        some_array = getResources().getStringArray(R.array.feedback_activity_titles);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.feedback_activity_titles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        //type_spinner.setAdapter(adapter);
        type_spinner.setItems(some_array);
        type_spinner.setOnItemSelectedListener(this);
        return fragmentView;
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        feedbackPresentor.setView(this);

    }

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

        select_type = parent.getItemAtPosition(i).toString();

        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + select_type,
                Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        select_type =  some_array[view.getSelectedIndex()];

        Toast.makeText(view.getContext(),
                "OnItemSelectedListener : " + select_type,
                Toast.LENGTH_SHORT).show();

        type_spinner.setSelectedIndex(view.getSelectedIndex());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item_report_feedback,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public FeedbackItem setFeedbackModel()
    {
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setTitle(feedback_title.getText().toString());
        feedbackItem.setDescription(feedback_desc.getText().toString());
        feedbackItem.setType(select_type);
        return feedbackItem;
    }

    FeedbackItem feedbackItem;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.menu_itm_report:
                Log.d("feedbacklog","menu pressed");
                feedbackItem =  setFeedbackModel();
                feedbackPresentor.ReportFeedback(feedbackItem);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String setResult(String result) {
        return null;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setProfileUpdateFailed() {

    }

    @Override
    public void showSnackBar(String message) {

    }

    @Override
    public void showToast(String message) {

        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void setEmptyView() {
        feedback_title.setText("");
        feedback_desc.setText("");
    }


}
