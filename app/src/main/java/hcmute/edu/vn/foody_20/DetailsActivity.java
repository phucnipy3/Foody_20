package hcmute.edu.vn.foody_20;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.net.Uri;

import android.Manifest;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DetailsActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private Location myLocation = new Location("");
    private Location foodplaceLocation = new Location("");
    Geocoder geocoder;
    List<Address> addresses;
    TextView tvAddress, tvDistance, tvType, tvPrice, tvFoodPlaceName, tvTime, tvProvinceName, tvStatus, tvContact;
    List<Address> foodplaceaddresses;
    private String contact = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        geocoder = new Geocoder(this, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(this);
        GetMyLocation();
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvType = (TextView) findViewById(R.id.tvType);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvProvinceName = (TextView) findViewById(R.id.tvProvinceName);
        tvFoodPlaceName = (TextView) findViewById(R.id.tvFoodPlaceName);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvContact = (TextView) findViewById(R.id.tvContact);
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+contact));
                String a = Uri.parse("tel:"+contact).toString();
                if(ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(DetailsActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            101);
                    return;
                }
                
                startActivity(intent);

            }


        });
        // query;
        int id = 0;
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();

            id = intent.getExtras().getInt("idFoodPlace");

        }
        String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, OpenTime, CloseTime, Max(Price) MaxPrice, Min(Price) MinPrice,Province.Name ProvinceName,Contact from FoodPlace, Province, FoodInMenu where FoodPlace.Id = FoodInMenu.FoodPlaceId and FoodPlace.ProvinceId=Province.Id and FoodPlace.Id = "+String.valueOf(id) + " group by FoodPlace.Id, FoodPlace.Name, Address, Type, OpenTime, CloseTime,Province.Name ,Contact";
        new GetFoodPlaceDetail().execute(query);

        String queryFood = "select * from FoodInMenu where FoodPlaceId = "+String.valueOf(id);
        new GetFoodWithImage().execute(queryFood);
    }

    private class GetFoodPlaceDetail extends AsyncTask<String, Void, FoodPlaceDetailViewModel> {

        @Override
        protected FoodPlaceDetailViewModel doInBackground(String... strings) {
            String query = "select * from FoodPlace where id = 1";

            if(strings.length > 0)
            {
                query = strings[0];
            }
            FoodPlaceDetailViewModel foodPlaceDetailViewModel = new FoodPlaceDetailViewModel();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                if(resultSet.next()){
                    int id = resultSet.getInt("Id");
                    String name = resultSet.getString("Name");
                    String address = resultSet.getString("Address");
                    String type = resultSet.getString("Type");
                    Time openTime = resultSet.getTime("OpenTime");
                    Time closeTime = resultSet.getTime("CloseTime");
                    BigDecimal maxPrice = resultSet.getBigDecimal("MaxPrice");
                    BigDecimal minPrice= resultSet.getBigDecimal("MinPrice");
                    String contact = resultSet.getString("Contact");
                    String provinceName = resultSet.getString("ProvinceName");
                    foodPlaceDetailViewModel = new FoodPlaceDetailViewModel(id,name,address,type,openTime,closeTime,minPrice,maxPrice,provinceName,contact);
                }
                DBconn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodPlaceDetailViewModel;
        }

        @Override
        protected void onPostExecute(FoodPlaceDetailViewModel foodPlaceDetailViewModel) {
            super.onPostExecute(foodPlaceDetailViewModel);

            SetFoodPlaceDetail(foodPlaceDetailViewModel);
        }
    }
    public void SetFoodPlaceDetail(FoodPlaceDetailViewModel foodPlaceDetailViewModel)
    {

        try {
            tvAddress.setText(foodPlaceDetailViewModel.getAddress());
            tvType.setText(foodPlaceDetailViewModel.getType());
            tvPrice.setText(foodPlaceDetailViewModel.getMinPrice() + " - " + foodPlaceDetailViewModel.getMaxPrice());
            tvProvinceName.setText(foodPlaceDetailViewModel.getProvinceName());
            tvTime.setText(foodPlaceDetailViewModel.getOpenTime().toString().substring( 0, 5 ) + " - " + foodPlaceDetailViewModel.getCloseTime().toString().substring( 0, 5 ));
            tvFoodPlaceName.setText(foodPlaceDetailViewModel.getName());
            tvDistance.setText(Distance(foodPlaceDetailViewModel.getAddress()));
            int opentimeFomartted = Integer.parseInt(foodPlaceDetailViewModel.getOpenTime().toString().substring(0,5).replaceAll(":",""));
            int closetimeFomartted = Integer.parseInt(foodPlaceDetailViewModel.getCloseTime().toString().substring(0,5).replaceAll(":",""));
            int timesystemFomartted = Integer.parseInt(GetTimeSystem());
            if(timesystemFomartted>=opentimeFomartted && timesystemFomartted<=closetimeFomartted)
                tvStatus.setText("MỞ CỬA");
            else
                tvStatus.setText("CHƯA MỞ CỬA");

            contact = foodPlaceDetailViewModel.getContact();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class GetFoodWithImage extends AsyncTask<String, Void, ArrayList<FoodViewModel>>{

        @Override
        protected ArrayList<FoodViewModel> doInBackground(String... strings) {
            String query = "select * from FoodInMenu";

            if(strings.length > 0)
            {
                query = strings[0];
            }
            ArrayList<FoodViewModel> foodViewModels = new ArrayList<>();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                while(resultSet.next()){
                    int id = resultSet.getInt("Id");
                    String foodName = resultSet.getString("FoodName");
                    BigDecimal price = resultSet.getBigDecimal("Price");
                    String foodImage = resultSet.getString("FoodImage");
                    int foodPlaceId = resultSet.getInt("FoodPlaceId");
                    int typeId = resultSet.getInt("TypeId");
                    foodViewModels.add(new FoodViewModel(id,foodName,price,foodImage,foodPlaceId,typeId));
                }
                DBconn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodViewModels;
        }

        @Override
        protected void onPostExecute(ArrayList<FoodViewModel> foodViewModels) {
            super.onPostExecute(foodViewModels);

        }
    }

    public void SetFoods(ArrayList<FoodViewModel> foodViewModels)
    {

    }
    public void GetMyLocation(){
        if(ActivityCompat.checkSelfPermission(DetailsActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
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
    public String Distance(String foodplaceAddress){
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
        return new DecimalFormat("##.#").format(distance) + " " +"km";
    }
    public String GetTimeSystem(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate.replaceAll(":","");

    }

}
