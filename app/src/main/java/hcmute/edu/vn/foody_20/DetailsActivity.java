package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // query;
        int id = 2;
        String query = "select FoodPlace.Id Id, Name, Address, Type, OpenTime, CloseTime, Max(Price) MaxPrice, Min(Price) MinPrice from FoodPlace, FoodInMenu where FoodPlace.Id = FoodInMenu.FoodPlaceId and FoodPlace.Id = "+String.valueOf(id) + "group by FoodPlace.Id, Name, Address, Type, OpenTime, CloseTime";
        new GetFoodPlaceDetail().execute(query);
        String queryFood = "select FoodInMenu.Id Id, FoodName, Price, FoodImage, FoodPlaceId, TypeId, FoodType TypeName from FoodInMenu, FoodType where FoodInMenu.TypeId = FoodType.Id and FoodPlaceId = "+String.valueOf(id);
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
                    foodPlaceDetailViewModel = new FoodPlaceDetailViewModel(id,name,address,type,openTime,closeTime,minPrice,maxPrice);
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
                    String typeName = resultSet.getString("TypeName");
                    foodViewModels.add(new FoodViewModel(id,foodName,price,foodImage,foodPlaceId,typeId,typeName));
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
}
