package com.softmine.drpedia.home.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;


import com.softmine.drpedia.R;
import com.softmine.drpedia.home.di.ActivityModule;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.fragment.UploadCaseFragment;
import com.softmine.drpedia.home.service.UploadService;
import com.softmine.drpedia.home.service.UploadTaskParameters;

import java.io.File;

import frameworks.AppBaseApplication;
import frameworks.di.component.HasComponent;

public class UploadCaseActivity extends AppCompatActivity implements HasComponent<CaseStudyComponent> {

    private CaseStudyComponent caseStudyComponent;
    final int MY_PERMISSIONS_REQUEST_READ_MEDIA=1;

    protected UploadTaskParameters params = null;
    UploadCaseFragment uploadCaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
          //  actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        toolbar.setTitle(R.string.app_name);
        this.params = getIntent().getParcelableExtra(UploadService.PARAM_TASK_PARAMETERS);
     /*   if(this.params!=null) {
            Log.d("UploadFragmentLog", "params size  " + params.attachmentList.size());
            Log.d("UploadFragmentLog", "case title " + params.caseTitle);
            Log.d("UploadFragmentLog", "case title " + params.caseTitle);
            Log.d("UploadFragmentLog", "case desc " + params.caseDesc);
            Log.d("UploadFragmentLog", "case category " + params.caseCategory);


            for(String file : params.attachmentList)
            {
                String fileType = checkFileExtension(file);
                if(fileType.substring(0,fileType.indexOf("/")).equals("image"))
                {
                    Log.d("UploadFragmentLog","file type image");
                }
                else if(fileType.substring(0,fileType.indexOf("/")).equals("video"))
                {
                    Log.d("UploadFragmentLog","file type video");
                }
            }
        }*/
        this.addFragment(savedInstanceState , this.params);
        this.initializeInjector();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }
    }

    public String checkFileExtension(String filePath)
    {
        File file = new File(filePath);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        Log.d("UploadFragmentLog","file extension "+fileExtension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        Log.d("UploadFragmentLog","file mimeType "+mimeType);
        return mimeType;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                }
                break;

            default:
                break;
        }
    }

    private void addFragment(Bundle savedInstanceState, UploadTaskParameters params)
    {

        if (savedInstanceState == null) {
            uploadCaseFragment = new UploadCaseFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(UploadService.PARAM_TASK_PARAMETERS , params);
            uploadCaseFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.upload_case_fragment_container,uploadCaseFragment);

            fragmentTransaction.commit();
        }
    }

    private void initializeInjector() {
        this.caseStudyComponent = DaggerCaseStudyComponent.builder()
                .baseAppComponent(((AppBaseApplication)getApplication())
                        .getBaseAppComponent())
                .getCaseStudyListModule(new GetCaseStudyListModule(this))
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public CaseStudyComponent getComponent() {
        return caseStudyComponent;
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        uploadCaseFragment.setTouchEvent(event);

        return super.onTouchEvent(event);
    }
}


