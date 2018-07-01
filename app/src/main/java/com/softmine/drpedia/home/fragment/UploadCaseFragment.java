package com.softmine.drpedia.home.fragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.sachin.doctorsguide.R;
import com.softmine.drpedia.home.CaseUploadView;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.presentor.UploadCasePresentor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.di.component.HasComponent;

public class UploadCaseFragment extends Fragment implements CaseUploadView {

    private static final int INTENT_REQUEST_CODE = 100;
    ByteArrayOutputStream byteBuff;
    @Inject
    UploadCasePresentor uploadCasePresentor;


    @BindView(R.id.postType)
    AutoCompleteTextView caseType;
    @BindView(R.id.postDesc)
    AutoCompleteTextView caseDesc;

    String imageType;

    public UploadCaseFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getComponent(CaseStudyComponent.class).inject(this);
        byteBuff = new ByteArrayOutputStream();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.upload_case_item, container, false);
        ButterKnife.bind(this,fragmentView);
        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.uploadCasePresentor.setView(this);
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @OnClick(R.id.selectImageBtn)
    public void selectCaseImage()
    {
        uploadCasePresentor.selectCaseImage();
    }

    @OnClick(R.id.uploadCaseBtn)
    public void uploadCaseDetails()
    {
        uploadCasePresentor.uploadCaseDetails();
    }


    @Override
    public void selectImageFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        try {
           startActivityForResult(intent, INTENT_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUploadResult(String result) {
        Log.d("uploadlogs","upload result==="+result);
    }


    @Override
    public String getCaseDesc() {
        return this.caseDesc.getText().toString();
    }

    @Override
    public String getCaseType() {
        return this.caseType.getText().toString();
    }

    @Override
    public byte[] getImageBytes() {
        return  this.byteBuff.toByteArray();
    }

    @Override
    public String getImageType()
    {
        return "1";
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == getActivity().RESULT_OK) {

                try {
                    Log.d("uploadlogs","data uri==="+getActivity().getContentResolver().getType(data.getData()));
                    imageType = getActivity().getContentResolver().getType(data.getData());

                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());

                    byteBuff = getBytes(is);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ByteArrayOutputStream getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff;
    }
}
