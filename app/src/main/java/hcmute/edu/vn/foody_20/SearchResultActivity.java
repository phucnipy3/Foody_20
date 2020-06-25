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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private TextView tvProvinces;
    private EditText edtSearch;
    private ArrayList<FoodPlaceFullViewModel> foodPlaceArrayList;
    private ArrayList<FoodPlaceFullViewModel> tempfoodPlaceArrayList;
    private FoodPlaceFullViewAdapter foodPlaceFullViewAdapter;
    private ListView lstResult;
    private ProgressBar progressBar;
    private TextView btnBestMatch,btnNearby,btnPopular,btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        tvProvinces =(TextView) findViewById(R.id.tvProvinces);
        edtSearch =(EditText) findViewById(R.id.edtSearchResult);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_search);
        btnBestMatch = (TextView) findViewById(R.id.btnBestMatch);
        btnNearby =(TextView) findViewById(R.id.btnNearby);
        btnFilter =(TextView) findViewById(R.id.btnFilter);
        btnPopular = (TextView) findViewById(R.id.btnPopular);
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.share_key),MODE_PRIVATE);
        tvProvinces.setText(sharedPreferences.getString(getString(R.string.key_province_name),getString(R.string.default_province_name)));

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

        int pageIndex =0;
        int provinceID = GetProvinceID();
        String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, Image, OpenTime, CloseTime, ReviewContent, ReviewCount, CheckinCount, Rate from FoodPlace, Province where FoodPlace.ProvinceId = Province.Id ";
        query = query + "and Province.Id = " + String.valueOf(provinceID) + " ";
        query = query + "order by Id offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
        new GetFoodPlaceFullAsync().execute(query);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int countFoodPlaceFound =0;
                foodPlaceArrayList.clear();
                for (int i = 0; i < tempfoodPlaceArrayList.size(); i++) {
                    if (tempfoodPlaceArrayList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        foodPlaceArrayList.add(tempfoodPlaceArrayList.get(i));
                        foodPlaceFullViewAdapter.notifyDataSetChanged();
                        countFoodPlaceFound ++;
                    }
                }
                if(countFoodPlaceFound<1){
                    foodPlaceArrayList.clear();
                    foodPlaceFullViewAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }
    private class GetFoodPlaceFullAsync extends AsyncTask<String, Void, ArrayList<FoodPlaceFullViewModel>> {

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
        for (FoodPlaceFullViewModel foodPlaceFullViewModel: foodPlaceFullViewModels
        ) {
            foodPlaceArrayList.add(foodPlaceFullViewModel);
            tempfoodPlaceArrayList.add(foodPlaceFullViewModel);
        }
        foodPlaceFullViewAdapter.notifyDataSetChanged();
    }
    public int GetProvinceID(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.share_key),MODE_PRIVATE);
        return sharedPreferences.getInt(getString(R.string.key_province_id), getResources().getInteger(R.integer.default_province_id));
    }

}
