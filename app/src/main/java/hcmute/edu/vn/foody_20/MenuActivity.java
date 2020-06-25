package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    LinearLayout btnChangeMenu;
    Button btnBackMenu;
    RecyclerView rcvImageMenu;
    ExpandableListView eplMenu;
    TextView tvNameMenu,tvShowPicture,tvShowMenu;

    private List<FoodViewModel> lstFood;

    FoodViewAdapter ImgFoodAdapter;
    ExpandMenuAdapter adapter;
    List<String> lstFoodKind;
    HashMap<String,List<FoodViewModel>> lstFoodInKind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnChangeMenu = findViewById(R.id.btnChangeMenu);
        btnBackMenu = findViewById(R.id.btnBackMenu);
        rcvImageMenu = findViewById(R.id.rcvImageMenu);
        eplMenu = findViewById(R.id.eplMenu);
        tvNameMenu = findViewById(R.id.tvNameMenu);
        tvShowMenu = findViewById(R.id.tvShowMenu);
        tvShowPicture = findViewById(R.id.tvShowPicture);

        lstFoodKind = new ArrayList<>();
        lstFoodInKind= new HashMap<>();
        lstFood = new ArrayList<>();

        adapter = new ExpandMenuAdapter(this,lstFoodKind,lstFoodInKind);
        eplMenu.setAdapter(adapter);

        rcvImageMenu = (RecyclerView) findViewById(R.id.rcvImageMenu);
        ImgFoodAdapter = new FoodViewAdapter(this,lstFood);
        rcvImageMenu.setLayoutManager(new GridLayoutManager(this,2));
        rcvImageMenu.setAdapter(ImgFoodAdapter);

        int id = 0;
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            id = intent.getExtras().getInt("idFoodPlace");
        }
        new GetFoodAsync(id).execute();

        btnBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnChangeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView temp = new TextView(getApplicationContext());
                temp.setBackground(tvShowMenu.getBackground());
                tvShowMenu.setBackground(tvShowPicture.getBackground());
                tvShowPicture.setBackground(temp.getBackground());

                if (tvShowMenu.getBackground() != null){
                    rcvImageMenu.setVisibility(View.INVISIBLE);
                    eplMenu.setVisibility(View.VISIBLE);
                }else{
                    rcvImageMenu.setVisibility(View.VISIBLE);
                    eplMenu.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private class GetFoodAsync extends AsyncTask<Integer, Void, ArrayList<FoodViewModel>> {
        int id;

        public GetFoodAsync(int foodPlaceId) {
            this.id = foodPlaceId;
        }

        @Override
        protected ArrayList<FoodViewModel> doInBackground(Integer... ints) {
            String query = "select FoodInMenu.Id Id, FoodName, Price, FoodImage, FoodPlaceId, TypeId, FoodType TypeName from FoodInMenu, FoodType where FoodInMenu.TypeId = FoodType.Id and FoodPlaceId = "+String.valueOf(id);

            ArrayList<FoodViewModel> foodViewModels = new ArrayList<>();
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                while(resultSet.next()){
                    int idFood = resultSet.getInt("Id");
                    String foodName = resultSet.getString("FoodName");
                    BigDecimal price = resultSet.getBigDecimal("Price");
                    String foodImage = resultSet.getString("FoodImage");
                    int foodPlaceId = resultSet.getInt("FoodPlaceId");
                    int typeId = resultSet.getInt("TypeId");
                    String typeName = resultSet.getString("TypeName");
                    foodViewModels.add(new FoodViewModel(idFood,foodName,price,foodImage,foodPlaceId,typeId,typeName));
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
            SetFood(foodViewModels);
        }
    }
    public void SetFood(ArrayList<FoodViewModel> foodViewModels)
    {
        for (FoodViewModel foodViewModel: foodViewModels) {
            lstFood.add(foodViewModel);
        }
        // get food kind
        for (int i=0;i<lstFood.size();i++)
            if (!lstFoodKind.contains(lstFood.get(i).getTypeName().toString()))
                lstFoodKind.add(lstFood.get(i).getTypeName().toString());
        // get food in kind
        for (int j=0;j<lstFoodKind.size();j++){
            List<FoodViewModel> tempList = new ArrayList<>();
            for (int i=0;i<lstFood.size();i++)
                if (lstFood.get(i).getTypeName().toString().equals(lstFoodKind.get(j).toString()))
                    tempList.add(lstFood.get(i));
            lstFoodInKind.put(lstFoodKind.get(j).toString(),tempList);
        }
        adapter.notifyDataSetChanged();
        ImgFoodAdapter.notifyDataSetChanged();
    }
}
