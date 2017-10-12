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
import ash.wasset.adapters.NewsAdapter;
import ash.wasset.models.NewsModel;
import ash.wasset.models.StandardWebServiceResponse;

/**
 * Created by ahmed on 1/5/17.
 */

public class NewsFragment extends Fragment {

    View view;
    ListView newsListView;
    String getJson;
    ArrayList<NewsModel> newsModels;
    NewsAdapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragment, container, false);
        initilization();
        decodeJson(getJson);
        callAdapter();
        return view;
    }
    private void initilization(){
        newsListView = (ListView) view.findViewById(R.id.newsListView);
        getJson = getArguments().getString("json");
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        newsModels = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        newsModels = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<NewsModel>>(){}.getType());
    }

    private void callAdapter(){
        newsAdapter = new NewsAdapter(getActivity(), newsModels);
        newsListView.setAdapter(newsAdapter);
    }
}
