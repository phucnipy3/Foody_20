package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView txtProvinces;
    private List<FoodPlaceCardViewModel> lstFoodPlace;
    private FoodPlaceCardViewAdapter myFoodPlaceAdapter;
    private EditText edtSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtProvinces = findViewById(R.id.txtProvinces);


        txtProvinces.setText(GetProvinceName());
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchResultActivity.class));
            }
        });

        txtProvinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChooseProvincesActivity.class);
                intent.putExtra("CurrentProvince",txtProvinces.getText());
                startActivity(intent);

            }
        });
        lstFoodPlace = new ArrayList<>();
        RecyclerView rcvFoodPlace = (RecyclerView) findViewById(R.id.recyclerviewFoodPlace_id);
        myFoodPlaceAdapter = new FoodPlaceCardViewAdapter(this,lstFoodPlace);
        rcvFoodPlace.setLayoutManager(new GridLayoutManager(this,2));
        rcvFoodPlace.setAdapter(myFoodPlaceAdapter);

        // Lấy trang đầu tiên là 10 thằng;
        // load more thì tăng pageIndex lên;
        int pageIndex = 0;
        new GetFoodPlaceCardAsync().execute("select Id, Name, Image, ReviewContent from FoodPlace order by Id offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only");
        myFoodPlaceAdapter.notifyDataSetChanged();
        //Query cho quán ăn full thông tin
        String searchString = "";
        int provinceID = 1;
        String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, Image, OpenTime, CloseTime, ReviewContent, ReviewCount, CheckinCount, Rate from FoodPlace, Province where FoodPlace.ProvinceId = Province.Id ";
        if(!searchString.equals("") && !searchString.equals(null)){
            query = query + "and FoodPlace.Name like '%"+searchString+"%' ";
        }
        query = query + "and Province.Id = " + String.valueOf(provinceID) + " ";
        query = query + "order by Id offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
        new GetFoodPlaceFull().execute(query);

    }
    private class GetFoodPlaceCardAsync extends AsyncTask<String, Void, ArrayList<FoodPlaceCardViewModel>>{

        @Override
        protected ArrayList<FoodPlaceCardViewModel> doInBackground(String... strings) {
            String query = "select Id, Name, Image, ReviewContent from FoodPlace";

            if(strings.length > 0)
            {
                query = strings[0];
            }
            ArrayList<FoodPlaceCardViewModel> foodPlaceCardViewModels = new ArrayList<>();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                while(resultSet.next()){
                    int id = resultSet.getInt("Id");
                    String name = resultSet.getString("Name");
                    String image = resultSet.getString("Image");
                    String reviewContent = resultSet.getString("ReviewContent");
                    foodPlaceCardViewModels.add(new FoodPlaceCardViewModel(id,image,name,reviewContent));
                }
                DBconn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return foodPlaceCardViewModels;
        }

        @Override
        protected void onPostExecute(ArrayList<FoodPlaceCardViewModel> foodPlaceCardViewModels) {
            super.onPostExecute(foodPlaceCardViewModels);

            SetFoodPlaceCards(foodPlaceCardViewModels);
        }
    }
    private class GetFoodPlaceFull extends AsyncTask<String, Void, ArrayList<FoodPlaceFullViewModel>>{

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
    public void SetFoodPlaceCards(ArrayList<FoodPlaceCardViewModel> foodPlaceCardViewModels){
        // Tạo cái array list rồi add vô như bên Choose province
        // t làm load 1 lần 10 cái
        // có view more thì thay đổi tham số câu query rồi add vô, đừng clear cái list
        for (FoodPlaceCardViewModel foodPlaceCardViewModel: foodPlaceCardViewModels
        ) {
            lstFoodPlace.add(foodPlaceCardViewModel);
        }
        myFoodPlaceAdapter.notifyDataSetChanged();
    }

    public void SetFoodPlaceFull(ArrayList<FoodPlaceFullViewModel> foodPlaceFullViewModels){
        // Tương tự
    }
    public String GetProvinceName(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("currentprovince",MODE_PRIVATE);
        return sharedPreferences.getString("currentprovincename","Hồ Chí Minh");
    }
}
