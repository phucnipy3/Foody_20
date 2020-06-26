package hcmute.edu.vn.foody_20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SearchResultActivity extends AppCompatActivity {

    private TextView tvProvinces, txtBack;
    private EditText edtSearch;
    private ArrayList<FoodPlaceFullViewModel> foodPlaceArrayList;
    private FoodPlaceFullViewAdapter foodPlaceFullViewAdapter;
    private ListView lstResult;
    private ProgressBar progressBar;
    private TextView btnBestMatch, btnNearby, btnPopular, btnFilter;
    int pageIndex = 0;
    private String searchstring = "";

    private enum SearchType {BestMatch, Popular, Nearby}

    ;
    private SearchType mySearchType = SearchType.BestMatch;
    private Location myLocation = new Location("");
    private Location foodplaceLocation = new Location("");
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private List<Address> foodplaceaddresses;

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

        geocoder = new Geocoder(this, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(this);
        GetMyLocation();

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.share_key),MODE_PRIVATE);
        tvProvinces.setText(sharedPreferences.getString(getString(R.string.key_province_name), getString(R.string.default_province_name)));

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
        btnNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySearchType = SearchType.Nearby;
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
            if(foodPlaceArrayList.size()>0)
                progressBar.setVisibility(View.GONE);

        }
    }
    public void SetFoodPlaceFull(ArrayList<FoodPlaceFullViewModel> foodPlaceFullViewModels){
        foodPlaceArrayList.clear();

        for (FoodPlaceFullViewModel foodPlaceFullViewModel: foodPlaceFullViewModels
        ) {
            if(foodPlaceFullViewModel.getName().toLowerCase().contains(searchstring.toLowerCase())){
                foodPlaceArrayList.add(foodPlaceFullViewModel);
            }
        }
        if(mySearchType==SearchType.Nearby){
            SortByDistance(foodPlaceArrayList);

        }
        foodPlaceFullViewAdapter.notifyDataSetChanged();

    }
    public int GetProvinceID(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.share_key),MODE_PRIVATE);
        return sharedPreferences.getInt(getString(R.string.key_province_id),getResources().getInteger(R.integer.default_province_id));
    }

    public void ExecuteQuery(){
        int provinceID = GetProvinceID();
        String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, Image, OpenTime, CloseTime, ReviewContent, ReviewCount, CheckinCount, Rate from FoodPlace, Province where FoodPlace.ProvinceId = Province.Id ";
        if(searchstring != null && searchstring !=""){
            query = query + " and LOWER(Province.Name) like '%"+ searchstring.toLowerCase() +"%' ";
        }
        query = query + " and Province.Id = " + String.valueOf(provinceID) + " ";

        if(mySearchType == SearchType.BestMatch || mySearchType == SearchType.Nearby){

            query = query + "order by Id  offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
            new SearchResultActivity.GetFoodPlaceFull().execute(query);
        }
        if(mySearchType==SearchType.Popular){

            query = query + "order by CheckinCount DESC offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only";
            new SearchResultActivity.GetFoodPlaceFull().execute(query);
        }

    }
    public void SortByDistance(ArrayList<FoodPlaceFullViewModel> tempfoodPlaceArrayList){
        Collections.sort(tempfoodPlaceArrayList, new Comparator<FoodPlaceFullViewModel>() {
            @Override
            public int compare(FoodPlaceFullViewModel o1, FoodPlaceFullViewModel o2) {
                if(Distance(o1.getAddress())>Distance(o2.getAddress())){
                    return 1;
                }
                else {
                    if(Distance(o1.getAddress())==Distance(o2.getAddress())){
                        return 0;
                    }
                    else
                        return -1;
                }

            }

            @Override
            public boolean equals(@Nullable Object obj) {
                return false;
            }
        });
    }
    public void GetMyLocation(){
        if(ActivityCompat.checkSelfPermission(SearchResultActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
            return;
        }
        Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    myLocation.setLatitude(location.getLatitude());
                    myLocation.setLongitude(location.getLongitude());

                }
            }
        });

    }
    public Double Distance(String foodplaceAddress){
        double distance = 0.0;
        try {
            foodplaceaddresses = geocoder.getFromLocationName(foodplaceAddress, 1);
            if ( foodplaceaddresses != null && foodplaceaddresses.size() > 0) {

                Address address = (Address) foodplaceaddresses.get(0);
                foodplaceLocation.setLatitude(address.getLatitude());
                foodplaceLocation.setLongitude(address.getLongitude());
                distance = myLocation.distanceTo(foodplaceLocation)/1000;

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        return distance;
    }
}




