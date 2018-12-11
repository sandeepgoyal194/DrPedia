package com.softmine.drpedia.home.fragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.softmine.drpedia.R;
import com.softmine.drpedia.home.CaseUploadView;
import com.softmine.drpedia.home.activity.MultiPhotoSelectActivity;
import com.softmine.drpedia.home.adapter.MediaItemListAdapter;
import com.softmine.drpedia.home.customview.CaseMediaItemHorizontalRecyclerView;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.model.CaseMediaItem;
import com.softmine.drpedia.home.notification.UploadNotificationConfig;
import com.softmine.drpedia.home.presentor.UploadCasePresentor;
import com.softmine.drpedia.home.service.UploadService;
import com.softmine.drpedia.home.service.UploadTaskParameters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.di.component.HasComponent;

public class UploadCaseFragment extends Fragment implements CaseUploadView {

    private static final int IMAGE_REQUEST_CODE = 100;
    private static final int VIDEO_REQUEST_CODE = 101;

    private static final String TYPE_VIDEO_TXT = "VIDEO";
    private static final String TYPE_IMAGE_TXT = "IMAGE";
    private static final String TYPE_UPLOAD = "TYPE";

    ByteArrayOutputStream byteBuff;
    @Inject
    UploadCasePresentor uploadCasePresentor;

    protected UploadTaskParameters params = null;

    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;

    @BindView(R.id.postType)
    EditText caseType;
    @BindView(R.id.postDesc)
    EditText caseDesc;

    String imageType;
    View fragmentView;

    @BindView(R.id.uploadContainer)
    ScrollView container;

    final int CASE_UPLOAD_REQUEST_CODE=101;
    final int CASE_UPLOAD_RESPONSE_OK=102;
    final int CASE_UPLOAD_REQUEST_FAILS=103;


    @BindView(R.id.openBottonSheet)
    ImageView openBottomSheet;

    @BindView(R.id.expandSheetArrow)
    ImageView expandSheetArrow;

    @BindView(R.id.photoContainer)
    LinearLayout photoContainer;

    @BindView(R.id.videoContainer)
    LinearLayout videoContainer;


    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    @BindView(R.id.typeContainer)
    FrameLayout typeContainer;

    BottomSheetBehavior sheetBehavior;

    ArrayList<String> uriList;
    ArrayList<String> videoUriList;
    ArrayList<String> imageUriList;

    MediaController mediaController;

    Glide glide;

    @BindView(R.id.horizontal_recycler_view)
    CaseMediaItemHorizontalRecyclerView rc_view;

    MediaItemListAdapter mediaItemListAdapter ;

    List<CaseMediaItem> mediaItemList= new ArrayList<>();

    public UploadCaseFragment() {
        //setRetainInstance(true);
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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getComponent(CaseStudyComponent.class).inject(this);
        byteBuff = new ByteArrayOutputStream();
        uriList = new ArrayList<>();
        imageUriList = new ArrayList<>();
        videoUriList = new ArrayList<>();
        Bundle bundle = getArguments();
        this.params = bundle.getParcelable(UploadService.PARAM_TASK_PARAMETERS);

        if(this.params!=null) {
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
        }
      //  setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        fragmentView =  inflater.inflate(R.layout.upload_case_item, container, false);
        ButterKnife.bind(this,fragmentView);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        openBottomSheet.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        openBottomSheet.setVisibility(View.GONE);
                        expandSheetArrow.setVisibility(View.GONE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        expandSheetArrow.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

     /*   mediaController= new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
*/

        if(this.params!=null) {
            Log.d("UploadFragmentLog", "params size  " + params.attachmentList.size());
            Log.d("UploadFragmentLog", "case title " + params.caseTitle);
            Log.d("UploadFragmentLog", "case title " + params.caseTitle);
            Log.d("UploadFragmentLog", "case desc " + params.caseDesc);
            Log.d("UploadFragmentLog", "case category " + params.caseCategory);

            this.caseDesc.setText(params.caseDesc);
            this.caseType.setText(params.caseTitle);

            for(String file : params.attachmentList)
            {
                String fileType = checkFileExtension(file);
                uriList.add(file);
                if(fileType.substring(0,fileType.indexOf("/")).equals("image"))
                {
                    Log.d("UploadFragmentLog","file type image");
                    CaseMediaItem item = new CaseMediaItem();
                    item.setImage(file);
                    item.setSrc("storage");
                    mediaItemList.add(item);
                }
                else if(fileType.substring(0,fileType.indexOf("/")).equals("video"))
                {
                    Log.d("UploadFragmentLog","file type video");
                    CaseMediaItem item = new CaseMediaItem();
                    item.setVideo(file);
                    item.setSrc("storage");
                    mediaItemList.add(item);
                }
            }
        }

        mediaItemListAdapter =  new MediaItemListAdapter(getActivity());
        if(this.params!=null)
        {
            mediaItemListAdapter.setMediaItemList(mediaItemList);
            typeContainer.setVisibility(View.VISIBLE);
        }

        rc_view.setAdapter(mediaItemListAdapter);
        return fragmentView;
    }

    @OnClick(R.id.expandSheetArrow)
    public void expandBottomSheet()
    {
        if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
        {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            expandSheetArrow.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.openBottonSheet)
    public void openBottomSheet()
    {
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
        {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            openBottomSheet.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upload_case,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.uploadCasePresentor.setView(this);
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

   /* @OnClick(R.id.click_to_upload)
    public void selectCaseImage()
    {
        Log.d("uploadimage","clicked selectCaseImage");
        uploadCasePresentor.selectCaseImage();
    }*/

    @OnClick({R.id.photoContainer , R.id.imageHolder , R.id.photo})
    public void selectCaseImage()
    {
        Log.d("uploadimage","clicked selectCaseImage");
        uploadCasePresentor.selectCaseImage();
    }

    @OnClick({R.id.videoContainer , R.id.videoHolder , R.id.video})
    public void selectCaseVideo()
    {
        Log.d("uploadimage","clicked selectCaseImage");
        uploadCasePresentor.selectCaseVideo();
    }

  //  @OnClick(R.id.uploadCaseBtn)
    public void uploadCaseDetails()
    {
        uploadCasePresentor.uploadCaseDetails();
    }

    @Override
    public void selectImageFromStorage() {
        Intent intent = new Intent(getActivity(), MultiPhotoSelectActivity.class);
        intent.putExtra(TYPE_UPLOAD, "IMAGE");
        try {
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void selectVideoFromStorage()
    {
        Intent intent = new Intent(getActivity(), MultiPhotoSelectActivity.class);
        intent.putExtra(TYPE_UPLOAD, "VIDEO");
        try {
            startActivityForResult(intent, VIDEO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
   /* @Override
    public void selectImageFromStorage() {
        Log.d("uploadimage","clicked selectImageFromStorage");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        try {
           startActivityForResult(intent, INTENT_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }*/

   /* @Override
    public void selectVideoFromStorage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        try {
            startActivityForResult(intent, VIDEO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
*/
    @Override
    public void setUploadResult(String result) {
        Log.d("uploadlogs","upload result==="+result);

        uriList.clear();
        imageUriList.clear();
        videoUriList.clear();
        mediaItemList.clear();
        Intent userIntent = new Intent();
        userIntent.putExtra("uploadMsg", result);
        getActivity().setResult(CASE_UPLOAD_RESPONSE_OK,userIntent);
        getActivity().finish();
    }


    @Override
    public String getCaseDesc() {
        return this.caseDesc.getText().toString();
    }

    @Override
    public String getCaseTitle() {
        return this.caseType.getText().toString();
    }

   @Override
    public UploadNotificationConfig getNotificationConfig(@StringRes int title) {
       Log.d("MyService","notificationConfig called");

        UploadNotificationConfig config = new UploadNotificationConfig();
       Log.d("MyService","notificationConfig message  "+config.getProgress().message);
        config.setTitleForAllStatuses(getString(title))
                .setRingToneEnabled(true);
       config.getProgress().message = getString(R.string.uploading);
       config.getProgress().iconResourceID = R.drawable.ic_upload;
       config.getProgress().iconColorResourceID = Color.BLUE;
      /* config.getProgress().actions.add(new UploadNotificationAction(
               R.drawable.ic_cancelled,
               getString(R.string.cancel_upload),
               NotificationActions.getCancelUploadAction(this, 1, uploadId)));*/

       config.getCompleted().message = getString(R.string.upload_success);
       config.getCompleted().iconResourceID = R.drawable.ic_upload_success;
       config.getCompleted().iconColorResourceID = Color.GREEN;

       config.getError().message = getString(R.string.upload_error);
       config.getError().iconResourceID = R.drawable.ic_upload_error;
       config.getError().iconColorResourceID = Color.RED;

       config.getCancelled().message = getString(R.string.upload_cancelled);
       config.getCancelled().iconResourceID = R.drawable.ic_cancelled;
       config.getCancelled().iconColorResourceID = Color.YELLOW;
       return config;
    }

    @Override
    public List<String> getDataUri() {
        return uriList;
    }

    @Override
    public String getInterestType()
    {
        return "1";
    }

    @Override
    public void onCaseTypeEmpty() {
        this.caseType.requestFocus();
        this.caseType.setError(String.format(getString(R.string.casetype_error), getString(R.string.postType)));
       // mTextInputLayoutUserName.setError(String.format(getString(R.string.username_error), getString(R.string.username)));
    }

    @Override
    public void onCaseDescEmpty() {
        this.caseDesc.requestFocus();
        this.caseDesc.setError(String.format(getString(R.string.casedesc_error), getString(R.string.postDesc)));
    }

    @Override
    public void onUriListEmpty() {
        showSnackBar("Upload atleast 1 image or video");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IMAGE_REQUEST_CODE) {

            if (resultCode == getActivity().RESULT_OK) {

                /*try {
                    Log.d("uploadlogs","data uri==="+getActivity().getContentResolver().getType(data.getData()));
                    Uri uri = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        selected_image.setImageBitmap(bitmap);
                        typeContainer.setVisibility(View.VISIBLE);
                        selected_image.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageType = getActivity().getContentResolver().getType(data.getData());

                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());

                    byteBuff = getBytes(is);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/


                try
                {
                    ArrayList<String> strings = data.getStringArrayListExtra("mydata");
                    Log.d("attachmedia", "selected size: " + strings.size());
                    Log.d("attachmedia", "Selected Items: " + strings.toString());
                    imageUriList.clear();
                    Log.d("attachmedia", "image uri list size: " + imageUriList.size());
                    imageUriList.addAll(strings);
                    Log.d("attachmedia", "image uri list size: " + imageUriList.size());
                    uriList.addAll(imageUriList);
                    Log.d("attachmedia", "total list size: " + uriList.size());
                    //mediaItemList.clear();
                    for(String url :imageUriList)
                    {
                        CaseMediaItem item = new CaseMediaItem();
                        item.setImage(url);
                        item.setSrc("storage");
                        mediaItemList.add(item);
                        Log.d("attachmedia","url==="+url);
                    }
                    mediaItemListAdapter.setMediaItemList(mediaItemList);
                    typeContainer.setVisibility(View.VISIBLE);
                }
                catch(Exception e)
                {

                }
            }
        }
        else if(requestCode==VIDEO_REQUEST_CODE)
        {
            if (resultCode == getActivity().RESULT_OK) {
                /*try {
                    Log.d("videopath", "video data uri===" + getActivity().getContentResolver().getType(data.getData()));
                    Uri uri = data.getData();

                    String filemanagerstring = uri.getPath();
                    Log.d("videopath", "data path===" + filemanagerstring);
                    String selectedImagePath = getPath(uri);
                    Log.d("videopath", "data path===" + "file://"+selectedImagePath);
                    setImage(selectedImagePath);
                   *//*
                   if (selectedImagePath != null)
                   {
                        Log.d("videopath", "path not null");
                        typeContainer.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setMediaController(mediaController);
                        videoView.setVideoURI(uri);
                        videoView.requestFocus();
                     //   videoView.start();

                    }*//*
                }
                catch (Exception e) {
                    e.printStackTrace();
                }*/
                try
                {
                    ArrayList<String> strings = data.getStringArrayListExtra("mydata");
                    Log.d("attachmedia", "size: " + strings.size());
                    Log.d("attachmedia", "Selected Items: " + strings.toString());
                    videoUriList.clear();
                    Log.d("attachmedia", "video uri list size: " + videoUriList.size());
                    videoUriList.addAll(strings);
                    Log.d("attachmedia", "video uri list size: " + videoUriList.size());
                    uriList.addAll(videoUriList);
                    Log.d("attachmedia", "Total list size: " + uriList.size());
                //    mediaItemList.clear();
                    for(String url :videoUriList)
                    {
                        CaseMediaItem item = new CaseMediaItem();
                        item.setVideo(url);
                        item.setSrc("storage");
                        mediaItemList.add(item);
                        Log.d("attachmedia","url==="+url);
                    }
                    mediaItemListAdapter.setMediaItemList(mediaItemList);
                    typeContainer.setVisibility(View.VISIBLE);

//                    Bitmap bmThumbnail = createThumbnailFromPath(videoUriList.get(0) , MediaStore.Video.Thumbnails.MINI_KIND);
 //                   saveBitmap(bmThumbnail);



                }
                catch(Exception e)
                {

                }

            }
        }
    }

    public static File saveBitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "testimage.jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        Log.d("savebitmap",f.getAbsolutePath());
        Log.d("savebitmap",f.getName());
        Log.d("savebitmap",f.getPath());
        return f;
    }

    public void  setImage(String path) {
     /*   glide.with(this)
                .load(path)
                .centerCrop()
                .placeholder(Color.BLUE)
                .crossFade()
                .into(thumbView);

        thumbView.setVisibility(View.VISIBLE);*/
      //  playIcon.setVisibility(View.VISIBLE);
        typeContainer.setVisibility(View.VISIBLE);
      /* Bitmap bmThumbnail = createThumbnailFromPath("/storage/emulated/0/DCIM/Camera/20180716_164028.mp4", MediaStore.Video.Thumbnails.MINI_KIND);
       thumbnail1.setImageBitmap(bmThumbnail);*/
    }

    public Bitmap createThumbnailFromPath(String filePath, int type){
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
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

    @Override
    public void showProgressBar() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void hideProgressBar() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void setResult(int result) {

    }

    @Override
    public void setResult(int result, Intent data) {

    }

    @Override
    public void finish() {

    }

    @Override
    public void addEmptyLayout() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.menu_itm_signup:
                uploadCasePresentor.uploadCaseDetails();
                Log.d("killactivity","activity killed");
                getActivity().finish();
                Log.d("killactivity","activity killed1");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
