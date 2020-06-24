package hcmute.edu.vn.foody_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar progressBar;
    private RecyclerView rcvFoodPlace;
    private boolean isLoading = false;
    int pageIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtProvinces = findViewById(R.id.txtProvinces);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
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
        rcvFoodPlace = (RecyclerView) findViewById(R.id.recyclerviewFoodPlace_id);
        myFoodPlaceAdapter = new FoodPlaceCardViewAdapter(this,lstFoodPlace);
        rcvFoodPlace.setLayoutManager(new GridLayoutManager(this,2));
        rcvFoodPlace.setAdapter(myFoodPlaceAdapter);
        initScrollListener();
        // Lấy trang đầu tiên là 10 thằng;
        // load more thì tăng pageIndex lên;

        new GetFoodPlaceCardAsync().execute("select Id, Name, Image, ReviewContent from FoodPlace order by Id offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only");
        myFoodPlaceAdapter.notifyDataSetChanged();


    }
    private class GetFoodPlaceCardAsync extends AsyncTask<String, Void, ArrayList<FoodPlaceCardViewModel>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pageIndex!=0) {
                lstFoodPlace.add(null);
                myFoodPlaceAdapter.notifyItemInserted(lstFoodPlace.size() - 1);
            }
            isLoading=true;
        }

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
            progressBar.setVisibility(View.GONE);
            if(pageIndex!=0){
                lstFoodPlace.remove(lstFoodPlace.size() - 1);
                myFoodPlaceAdapter.notifyItemRemoved(lstFoodPlace.size());
            }
            SetFoodPlaceCards(foodPlaceCardViewModels);
            isLoading =false;
        }
    }
    public void SetFoodPlaceCards(ArrayList<FoodPlaceCardViewModel> foodPlaceCardViewModels){
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

    private void initScrollListener() {
        rcvFoodPlace.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == lstFoodPlace.size() - 1) {
                        loadMore();
                    }
                }

            }
        });
    }

    private void loadMore() {
        pageIndex++;
        new GetFoodPlaceCardAsync().execute("select Id, Name, Image, ReviewContent from FoodPlace order by Id offset "+ String.valueOf(pageIndex * 10)+" rows fetch next 10 row only");
    }
}

