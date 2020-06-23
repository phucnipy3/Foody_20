package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private FoodPlaceFullViewAdapter foodPlaceFullViewAdapter;
    private ListView lstResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        tvProvinces =(TextView) findViewById(R.id.tvProvinces);
        edtSearch =(EditText) findViewById(R.id.edtSearchResult);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("currentprovincename",MODE_PRIVATE);
        tvProvinces.setText(sharedPreferences.getString("currentprovincename","Hồ Chí Minh"));

        lstResult = (ListView) findViewById(R.id.lstResult);

        foodPlaceArrayList = new ArrayList<>();
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

        int pageIndex = 0;
        //Query cho quán ăn full thông tin
        String searchString = "";
        int provinceID = 1;
        String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, Image, OpenTime, CloseTime, ReviewContent, ReviewCount, CheckinCount, Rate from FoodPlace, Province where FoodPlace.ProvinceId = Province.Id ";
        if(!searchString.equals("") && !searchString.equals(null)){
            query = query + "and FoodPlace.Name like '%"+searchString+"%' ";
        }
        query = query + "and Province.Id = " + String.valueOf(provinceID) + " ";
        query = query + "order by Id offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
        new SearchResultActivity.GetFoodPlaceFull().execute(query);
    }
    private class GetFoodPlaceFull extends AsyncTask<String, Void, ArrayList<FoodPlaceFullViewModel>> {

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
        }
    }
    public void SetFoodPlaceFull(ArrayList<FoodPlaceFullViewModel> foodPlaceFullViewModels){
        for (FoodPlaceFullViewModel foodPlaceFullViewModel: foodPlaceFullViewModels
        ) {
            foodPlaceArrayList.add(foodPlaceFullViewModel);
        }
        foodPlaceFullViewAdapter.notifyDataSetChanged();
    }
}
