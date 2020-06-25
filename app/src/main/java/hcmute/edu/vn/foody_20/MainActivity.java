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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
    int pageSize = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtProvinces = findViewById(R.id.txtProvinces);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        txtProvinces.setText(GetSelectedProvinceName());
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
                startActivity(intent);

            }
        });
        lstFoodPlace = new ArrayList<>();
        rcvFoodPlace = (RecyclerView) findViewById(R.id.recyclerviewFoodPlace_id);
        if(lstFoodPlace.size()>0  && lstFoodPlace.get(lstFoodPlace.size()-1)==null)
        {
            myFoodPlaceAdapter = new FoodPlaceCardViewAdapter(this,lstFoodPlace);
            rcvFoodPlace.setLayoutManager(new GridLayoutManager(this,1));
            rcvFoodPlace.setAdapter(myFoodPlaceAdapter);
        }
        else {
            myFoodPlaceAdapter = new FoodPlaceCardViewAdapter(this,lstFoodPlace);
            rcvFoodPlace.setLayoutManager(new GridLayoutManager(this,2));
            rcvFoodPlace.setAdapter(myFoodPlaceAdapter);
        }
        initScrollListener();

        new GetFoodPlaceCardAsync(pageIndex,pageSize).execute();

    }
    private class GetFoodPlaceCardAsync extends AsyncTask<Void, Void, ArrayList<FoodPlaceCardViewModel>>{
        int pageIndex;
        int pageSize;

        public GetFoodPlaceCardAsync(int pageIndex, int pageSize) {
            this.pageIndex = pageIndex;
            this.pageSize = pageSize;
        }

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
        protected ArrayList<FoodPlaceCardViewModel> doInBackground(Void... voids) {
            String query = "select Id, Name, Image, ReviewContent from FoodPlace where FoodPlace.ProvinceId = " + GetSelectedProvinceId() + " order by Id offset "+ String.valueOf(pageIndex * pageSize)+" rows fetch next " + pageSize + " row only";
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

    public String GetSelectedProvinceName(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.share_key),MODE_PRIVATE);
        return sharedPreferences.getString(getString(R.string.key_province_name),getString(R.string.default_province_name));
    }
    public int GetSelectedProvinceId(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(getString(R.string.share_key),MODE_PRIVATE);
        return sharedPreferences.getInt(getString(R.string.key_province_id),getResources().getInteger(R.integer.default_province_id));
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
        new GetFoodPlaceCardAsync(pageIndex,pageSize).execute();
    }
}

