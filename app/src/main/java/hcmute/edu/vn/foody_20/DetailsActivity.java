package hcmute.edu.vn.foody_20;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView tvAddress, tvDistance, tvType, tvPrice, tvFoodPlaceName, tvTime, tvProvinceName, tvStatus, tvContact;
    private List<Address> foodplaceaddresses;
    private String contact = "";
    private ProgressBar progressBar;

    private List<FoodViewModel> lstFood;
    private FoodViewAdapter myFoodAdapter;

    Button btnBackDetails;
    ConstraintLayout lineMenu,lineWifi,maps;
    WifiViewModel wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        geocoder = new Geocoder(this, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(this);
        GetMyLocation();

        lineMenu = findViewById(R.id.lineMenu);
        lineWifi = findViewById(R.id.lineWifi);
        btnBackDetails = findViewById(R.id.btnBackDetails);
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
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_details);
        maps = findViewById(R.id.maps);

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailsActivity.this, "Openning GG maps...", Toast.LENGTH_SHORT).show();
                String uri  = "google.navigation:q=" + foodplaceLocation.getLatitude() +"," +foodplaceLocation.getLongitude();
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        lineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                int fpID = intent.getExtras().getInt("idFoodPlace");
                String fpName = intent.getExtras().getString("nameFoodPlace");
                Intent intent1 = new Intent(DetailsActivity.this,MenuActivity.class);
                intent1.putExtra("idFoodPlace",fpID);
                intent1.putExtra("nameFoodPlace",fpName);
                startActivity(intent1);
            }
        });

        lineWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddWifi();
            }
        });

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

        btnBackDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // query;
        int id = 0;
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();

            id = intent.getExtras().getInt("idFoodPlace");

        }

        lstFood = new ArrayList<>();
        RecyclerView rcvFoodPlace = (RecyclerView) findViewById(R.id.recyclerviewFood_id);
        myFoodAdapter = new FoodViewAdapter(this,lstFood);
        rcvFoodPlace.setLayoutManager(new GridLayoutManager(this,2));
        rcvFoodPlace.setAdapter(myFoodAdapter);

        new GetFoodPlaceDetailAsync(id).execute();

         new GetFoodWithImageAsync(id).execute();

         new AddOrUpdateWifiAsync(wifi).execute();
    }
    ///// DialogAddWifi
    private void DialogAddWifi(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_wifi);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvWifiName = (TextView) dialog.findViewById(R.id.tvWifiName);
        TextView tvWifiPass = (TextView) dialog.findViewById(R.id.tvWifiPass);
        TextView tvCancelDialog = (TextView) dialog.findViewById(R.id.tvCancelDialog);
        Button btnUpdateWifi = (Button) dialog.findViewById(R.id.btnUpdateWifi);
        ListView wifi_list_view = (ListView) dialog.findViewById(R.id.wifi_list_view);

        btnUpdateWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        tvCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private class GetFoodPlaceDetailAsync extends AsyncTask<Void, Void, FoodPlaceDetailViewModel> {

        int id;
        public GetFoodPlaceDetailAsync(int foodPlaceId) {
            this.id = foodPlaceId;
        }

        @Override
        protected FoodPlaceDetailViewModel doInBackground(Void... voids) {
            String query = "select FoodPlace.Id Id, FoodPlace.Name Name, Address, Type, OpenTime, CloseTime, Max(Price) MaxPrice, Min(Price) MinPrice,Province.Name ProvinceName,Contact from FoodPlace, Province, FoodInMenu where FoodPlace.Id = FoodInMenu.FoodPlaceId and FoodPlace.ProvinceId=Province.Id and FoodPlace.Id = "+String.valueOf(id) + " group by FoodPlace.Id, FoodPlace.Name, Address, Type, OpenTime, CloseTime,Province.Name ,Contact";
            FoodPlaceDetailViewModel foodPlaceDetailViewModel = new FoodPlaceDetailViewModel();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                if(resultSet.next()){
                    int foodPlaceId = resultSet.getInt("Id");
                    String name = resultSet.getString("Name");
                    String address = resultSet.getString("Address");
                    String type = resultSet.getString("Type");
                    Time openTime = resultSet.getTime("OpenTime");
                    Time closeTime = resultSet.getTime("CloseTime");
                    BigDecimal maxPrice = resultSet.getBigDecimal("MaxPrice");
                    BigDecimal minPrice= resultSet.getBigDecimal("MinPrice");
                    String contact = resultSet.getString("Contact");
                    String provinceName = resultSet.getString("ProvinceName");
                    foodPlaceDetailViewModel = new FoodPlaceDetailViewModel(foodPlaceId,name,address,type,openTime,closeTime,minPrice,maxPrice,provinceName,contact);
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
            progressBar.setVisibility(View.GONE);
            SetFoodPlaceDetail(foodPlaceDetailViewModel);
        }
    }
    public void SetFoodPlaceDetail(FoodPlaceDetailViewModel foodPlaceDetailViewModel)
    {

        try {
            tvAddress.setText(foodPlaceDetailViewModel.getAddress());
            tvType.setText(foodPlaceDetailViewModel.getType());
            tvPrice.setText(new DecimalFormat("#,###").format(foodPlaceDetailViewModel.getMinPrice())+"đ" + " - " + new DecimalFormat("#,###").format(foodPlaceDetailViewModel.getMaxPrice()) +"đ");
            tvProvinceName.setText(foodPlaceDetailViewModel.getProvinceName());
            tvTime.setText(foodPlaceDetailViewModel.getOpenTime().toString().substring( 0, 5 ) + " - " + foodPlaceDetailViewModel.getCloseTime().toString().substring( 0, 5 ));
            tvFoodPlaceName.setText(foodPlaceDetailViewModel.getName());
            tvDistance.setText(Distance(foodPlaceDetailViewModel.getAddress()).replace(",","."));
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


    private class GetFoodWithImageAsync extends AsyncTask<Void, Void, ArrayList<FoodViewModel>>{
        int id;

        public GetFoodWithImageAsync(int foodPlaceId) {
            this.id = foodPlaceId;
        }

        @Override
        protected ArrayList<FoodViewModel> doInBackground(Void... voids) {
            String query = "select FoodInMenu.Id Id, FoodName, Price, FoodImage, FoodPlaceId, TypeId, FoodType TypeName from FoodInMenu, FoodType where FoodInMenu.TypeId = FoodType.Id and FoodPlaceId = "+String.valueOf(id);
            ArrayList<FoodViewModel> foodViewModels = new ArrayList<>();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                while(resultSet.next()){
                    int foodId = resultSet.getInt("Id");
                    String foodName = resultSet.getString("FoodName");
                    BigDecimal price = resultSet.getBigDecimal("Price");
                    String foodImage = resultSet.getString("FoodImage");
                    int foodPlaceId = resultSet.getInt("FoodPlaceId");
                    int typeId = resultSet.getInt("TypeId");
                    String typeName = resultSet.getString("TypeName");
                    foodViewModels.add(new FoodViewModel(foodId,foodName,price,foodImage,foodPlaceId,typeId,typeName));
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
            SetFoodWithImage(foodViewModels);

        }
    }

    public void SetFoodWithImage(ArrayList<FoodViewModel> foodViewModels)
    {

        for (FoodViewModel foodViewModel: foodViewModels
        ) {
            lstFood.add(foodViewModel);
        }
        myFoodAdapter.notifyDataSetChanged();
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
    private class AddOrUpdateWifiAsync extends AsyncTask<Void, Void, Boolean> {
        WifiViewModel wifi;

        public AddOrUpdateWifiAsync(WifiViewModel wifi) {
            this.wifi = wifi;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery("select count(*) Count from Wifi where Wifi.WifiName = '"+wifi.getName()+"' and Wifi.FoodPlaceId = '"+ wifi.getFoodPlaceId() +"'");
                resultSet.next();
                int count = resultSet.getInt("Count");
                if(count == 0)
                    stmt.execute("insert into Wifi(WifiName, WifiPassword, FoodPlaceId) values('"+wifi.getName()+"', '"+wifi.getPassword()+"', "+wifi.getFoodPlaceId()+")");
                else
                    stmt.execute("update Wifi set Wifi.WifiPassword = "+ wifi.getPassword() +" where Wifi.WifiName = '"+wifi.getName()+"' and Wifi.FoodPlaceId = '"+ wifi.getFoodPlaceId()+"'");
                DBconn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

    }
}
