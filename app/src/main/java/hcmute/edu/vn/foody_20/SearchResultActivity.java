package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private TextView tvProvinces,txtBack;
    private EditText edtSearch;
    private ArrayList<FoodPlaceFullViewModel> foodPlaceArrayList;
    private ArrayList<FoodPlaceFullViewModel> tempfoodPlaceArrayList;
    private FoodPlaceFullViewAdapter foodPlaceFullViewAdapter;
    private ListView lstResult;
    private ProgressBar progressBar;
    private TextView btnBestMatch,btnNearby,btnPopular,btnFilter;
    int pageIndex =0;
    private String searchstring="";
    private enum SearchType {BestMatch,Popular,Nearby};
    private SearchType mySearchType = SearchType.BestMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        txtBack = findViewById(R.id.txtBack);
        tvProvinces =(TextView) findViewById(R.id.tvProvinces);
        edtSearch =(EditText) findViewById(R.id.edtSearchResult);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_search);
        btnBestMatch = (TextView) findViewById(R.id.btnBestMatch);
        btnNearby =(TextView) findViewById(R.id.btnNearby);
        btnFilter =(TextView) findViewById(R.id.btnFilter);
        btnPopular = (TextView) findViewById(R.id.btnPopular);
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("currentprovince",MODE_PRIVATE);
        tvProvinces.setText(sharedPreferences.getString("currentprovincename","Hồ Chí Minh"));

        lstResult = (ListView) findViewById(R.id.lstResult);

        foodPlaceArrayList = new ArrayList<>();
        tempfoodPlaceArrayList = new ArrayList<>();
        foodPlaceFullViewAdapter = new FoodPlaceFullViewAdapter(this,this,R.layout.result_item,foodPlaceArrayList);
        lstResult.setAdapter(foodPlaceFullViewAdapter);
        lstResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResultActivity.this,DetailsActivity.class);
                intent.putExtra("idFoodPlace",foodPlaceArrayList.get(position).getId());
                startActivity(intent);
            }
        });


        ExecuteQuery();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchstring =s.toString();
                ExecuteQuery();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySearchType = SearchType.Popular;
                ExecuteQuery();
            }
        });
        btnBestMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mySearchType = SearchType.BestMatch;
                ExecuteQuery();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class GetFoodPlaceFull extends AsyncTask<String, Void, ArrayList<FoodPlaceFullViewModel>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<FoodPlaceFullViewModel> doInBackground(String... strings) {
            String query = "select * from FoodPlace";

            if(strings.length > 0)
            {
                query = strings[0];
            }
            ArrayList<FoodPlaceFullViewModel> foodPlaceFullViewModels = new ArrayList<>();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                while(resultSet.next()){
                    int id = resultSet.getInt("Id");
                    String name = resultSet.getString("Name");
                    String address = resultSet.getString("Address");
                    String type = resultSet.getString("Type");
                    Time openTime = resultSet.getTime("OpenTime");
                    Time closeTime = resultSet.getTime("CloseTime");
                    int reviewCount = resultSet.getInt("ReviewCount");
                    int checkinCount = resultSet.getInt("CheckinCount");
                    float rate = resultSet.getFloat("Rate");
                    String image = resultSet.getString("Image");
                    String reviewContent = resultSet.getString("ReviewContent");
                    foodPlaceFullViewModels.add(new FoodPlaceFullViewModel(id,name,address,type,image,openTime,closeTime,reviewContent,reviewCount,checkinCount,rate));
                }
                DBconn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodPlaceFullViewModels;
        }

        @Override
        protected void onPostExecute(ArrayList<FoodPlaceFullViewModel> foodPlaceFullViewModels) {
            super.onPostExecute(foodPlaceFullViewModels);

            SetFoodPlaceFull(foodPlaceFullViewModels);
            progressBar.setVisibility(View.GONE);
        }
    }
    public void SetFoodPlaceFull(ArrayList<FoodPlaceFullViewModel> foodPlaceFullViewModels){
        foodPlaceArrayList.clear();
        tempfoodPlaceArrayList.clear();
        for (FoodPlaceFullViewModel foodPlaceFullViewModel: foodPlaceFullViewModels
        ) {
            if(foodPlaceFullViewModel.getName().toLowerCase().contains(searchstring.toLowerCase())){
                foodPlaceArrayList.add(foodPlaceFullViewModel);
                tempfoodPlaceArrayList.add(foodPlaceFullViewModel);
            }
        }
        foodPlaceFullViewAdapter.notifyDataSetChanged();
    }
    public int GetProvinceID(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("currentprovince",MODE_PRIVATE);
        return sharedPreferences.getInt("currentprovinceid",1);
    }

    public void ExecuteQuery(){
        int provinceID = GetProvinceID();
        if(mySearchType == SearchType.BestMatch){
            String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, Image, OpenTime, CloseTime, ReviewContent, ReviewCount, CheckinCount, Rate from FoodPlace, Province where FoodPlace.ProvinceId = Province.Id ";

            query = query + " and Province.Id = " + String.valueOf(provinceID) + " ";
            query = query + "order by Id  offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
            new SearchResultActivity.GetFoodPlaceFull().execute(query);
        }
        if(mySearchType==SearchType.Popular){
            String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, Image, OpenTime, CloseTime, ReviewContent, ReviewCount, CheckinCount, Rate from FoodPlace, Province where FoodPlace.ProvinceId = Province.Id ";

            query = query + "and Province.Id = " + String.valueOf(provinceID) + " ";
            query = query + "order by CheckinCount DESC offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
            new SearchResultActivity.GetFoodPlaceFull().execute(query);
        }

    }

}
