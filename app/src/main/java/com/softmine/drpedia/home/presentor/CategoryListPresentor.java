package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.CategoryListView;
import com.softmine.drpedia.home.domain.usecases.CategoryListUseCase;
import com.softmine.drpedia.home.domain.usecases.CreateUserInterestUseCase;
import com.softmine.drpedia.home.domain.usecases.FeedbackUseCase;
import com.softmine.drpedia.home.domain.usecases.UpdateInterestUseCase;
import com.softmine.drpedia.home.model.CategoryMainItemResponse;
import com.softmine.drpedia.home.model.SubCategoryItem;
import com.softmine.drpedia.home.model.UserInterestTypes;
import com.softmine.drpedia.home.model.UserNewAddedInterestType;
import com.softmine.drpedia.home.model.UserRemovedInterestType;
import com.softmine.drpedia.home.model.UserUpdateInterestType;
import com.softmine.drpedia.utils.GsonFactory;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class CategoryListPresentor implements ICategoryListPresenter {

    private CategoryListView categoryListView;
    private CategoryListUseCase categoryListUseCase;
    private CreateUserInterestUseCase createUserInterestUseCase;
    private UpdateInterestUseCase updateInterestUseCase;

    @Inject
    public CategoryListPresentor(CategoryListUseCase categoryListUseCase , CreateUserInterestUseCase createUserInterestUseCase , UpdateInterestUseCase updateInterestUseCase)
    {
        this.categoryListUseCase = categoryListUseCase;
        this.createUserInterestUseCase = createUserInterestUseCase;
        this.updateInterestUseCase = updateInterestUseCase;
    }

    public void setView(@NonNull CategoryListView view) {
        this.categoryListView = view;
    }

    private  void showViewLoading()
    {
        this.categoryListView.showProgressBar();
    }


    private void hideViewLoading() {
        Log.d("loginresponse","hide loading====================");

        this.categoryListView.hideProgressBar();
    }

    private void showCategoryListInView(List<CategoryMainItemResponse> usersCollection) {
      /*  final Collection<CaseItem> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);*/
        this.categoryListView.updateCategoryList(usersCollection);
    }

    @Override
    public void loadCategoryList() {
        this.showViewLoading();

        this.categoryListUseCase.execute(RequestParams.EMPTY, new Subscriber<List<CategoryMainItemResponse>>() {
            @Override
            public void onCompleted() {
                CategoryListPresentor.this.hideViewLoading();
            }

            @Override
            public void onError(Throwable e) {
                CategoryListPresentor.this.hideViewLoading();
            }

            @Override
            public void onNext(List<CategoryMainItemResponse> categoryMainItems) {
                Log.d("categoryListItems" ,""+ categoryMainItems.size());

                for(CategoryMainItemResponse item: categoryMainItems)
                {
                    Log.d("categoryListItems" , item.getCategoryName());
                    Log.d("categoryListItems" , "Subcategory List");
                     for(SubCategoryItem subItem : item.getSubCategory())
                     {
                         Log.d("categoryListItems" , ""+subItem.getSubtype_id());
                         Log.d("categoryListItems" , subItem.getSubtype());
                     }
                }
                CategoryListPresentor.this.showCategoryListInView(categoryMainItems);
            }
        });

    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(categoryListView.getContext(),
                errorBundle.getException());
        categoryListView.showSnackBar(errorMessage);
    }

    public void updateUserInterest(ArrayList<Integer> newInterestType ,ArrayList<Integer> removeInterestType )
    {
        UserUpdateInterestType userUpdateInterestType = new UserUpdateInterestType();
        userUpdateInterestType.setNewIntrest(newInterestType);
        userUpdateInterestType.setRemoveIntrest(removeInterestType);
        String userData = GsonFactory.getGson().toJson(userUpdateInterestType);

        Log.d("categoryListItems","Update Interest");
        Log.d("categoryListItems",userData);

        RequestParams requestParams =  UpdateInterestUseCase.createRequestParams(userData);
        this.showViewLoading();
        this.updateInterestUseCase.execute(requestParams, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("subTypePos","onCompleted called==");
                CategoryListPresentor.this.categoryListView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("subTypePos","onerror called==");
                CategoryListPresentor.this.categoryListView.hideProgressBar();
                // EditProfilePresenter.this.editProfileView.setProfileUpdateFailed();
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("subTypePos","exception code  "+((HttpException)e).code());
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("subTypePos","other issues");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("subTypePos", "other issue");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("subTypePos", "Json Parsing exception");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("subTypePos", "Http exception issue");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("subTypePos", "other issue");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(String string) {
                Log.d("subTypePos","onNext called==");
                Log.d("subTypePos",string);
                //  if(string.equalsIgnoreCase("Intrest added successfully"))
                CategoryListPresentor.this.categoryListView.showToast(string);
                CategoryListPresentor.this.categoryListView.startProfileActivity();
            }
        });
    }


    @Override
    public void createUserInterest(ArrayList<Integer> subTypeList) {

        UserInterestTypes type = new UserInterestTypes();
        type.setSubtype_id(subTypeList);
        String userData = GsonFactory.getGson().toJson(type);
        Log.d("subTypePos" , userData);
        RequestParams requestParams =  CreateUserInterestUseCase.createRequestParams(userData);
        this.showViewLoading();
        this.createUserInterestUseCase.execute(requestParams, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("subTypePos","onCompleted called==");
                CategoryListPresentor.this.categoryListView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("subTypePos","onerror called==");
                CategoryListPresentor.this.categoryListView.hideProgressBar();
                // EditProfilePresenter.this.editProfileView.setProfileUpdateFailed();
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("subTypePos","exception code  "+((HttpException)e).code());
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("subTypePos","other issues");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("subTypePos", "other issue");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("subTypePos", "Json Parsing exception");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("subTypePos", "Http exception issue");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("subTypePos", "other issue");
                        CategoryListPresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                }
            }

            @Override
            public void onNext(String string) {
                Log.d("subTypePos","onNext called==");
                Log.d("subTypePos",string);
              //  if(string.equalsIgnoreCase("Intrest added successfully"))
                CategoryListPresentor.this.categoryListView.showToast(string);
                CategoryListPresentor.this.categoryListView.startActivity();
            }
        });
    }
}
