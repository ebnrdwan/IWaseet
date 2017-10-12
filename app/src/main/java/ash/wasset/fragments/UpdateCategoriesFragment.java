package ash.wasset.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ash.wasset.R;
import ash.wasset.adapters.CategoriesListAdapter;
import ash.wasset.models.Categories;
import ash.wasset.models.StandardWebServiceResponse;

/**
 * Created by ahmed on 1/5/17.
 */

public class UpdateCategoriesFragment extends Fragment {

    View view;
    ListView categoriesListView;
    CategoriesListAdapter categoriesListAdapter;
    ArrayList<Categories> categories, categoriesSelected;
    String getJsonCategories, getJsonCategoriesSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_categories_fragment, container, false);
        initialization();
        decodeJsonCategories(getJsonCategories);
        decodeJsonCategoriesSelected(getJsonCategoriesSelected);
        setSelected();
        callAdapter();
        return view;
    }

    private void initialization(){
        categoriesListView = (ListView) view.findViewById(R.id.categoriesListView);
        getJsonCategories = getArguments().getString("jsonCategories");
        getJsonCategoriesSelected = getArguments().getString("jsonCategoriesSelected");
    }

    private void decodeJsonCategories(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        categories = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        categories = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<Categories>>(){}.getType());
    }

    private void decodeJsonCategoriesSelected(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        categoriesSelected = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        categoriesSelected = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<Categories>>(){}.getType());
    }

    private void setSelected(){
        for (int i = 0; i < categoriesSelected.size(); i ++){
            for (int j = 0; j < categories.size(); j ++){
                if (categoriesSelected.get(i).getId() == categories.get(j).getId()){
                    categories.get(j).setChecked(true);
                }
            }
        }
    }

    private void callAdapter(){
        categoriesListAdapter = new CategoriesListAdapter(getActivity(), categories);
        categoriesListView.setAdapter(categoriesListAdapter);
    }
}
