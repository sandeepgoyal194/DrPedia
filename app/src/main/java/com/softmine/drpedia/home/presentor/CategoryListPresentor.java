package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.home.CategoryListView;
import com.softmine.drpedia.home.domain.usecases.CategoryListUseCase;
import com.softmine.drpedia.home.model.CaseItem;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.model.SubCategoryItem;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

public class CategoryListPresentor implements ICategoryListPresenter {

    private CategoryListView categoryListView;
    private CategoryListUseCase categoryListUseCase;

    @Inject
    public CategoryListPresentor(CategoryListUseCase categoryListUseCase)
    {
        this.categoryListUseCase = categoryListUseCase;
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

    private void showCategoryListInView(List<CategoryMainItem> usersCollection) {
      /*  final Collection<CaseItem> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);*/
        this.categoryListView.updateCategoryList(usersCollection);
    }

    @Override
    public void loadCategoryList() {
        this.showViewLoading();

        this.categoryListUseCase.execute(RequestParams.EMPTY, new Subscriber<List<CategoryMainItem>>() {
            @Override
            public void onCompleted() {
                CategoryListPresentor.this.hideViewLoading();
            }

            @Override
            public void onError(Throwable e) {
                CategoryListPresentor.this.hideViewLoading();
            }

            @Override
            public void onNext(List<CategoryMainItem> categoryMainItems) {
                Log.d("categoryListItems" ,""+ categoryMainItems.size());

                for(CategoryMainItem item: categoryMainItems)
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
}
