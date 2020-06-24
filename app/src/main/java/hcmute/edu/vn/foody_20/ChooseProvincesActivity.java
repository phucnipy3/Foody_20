package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ChooseProvincesActivity extends AppCompatActivity {
    TextView btnCancel,btnDone;
    ListView lstProvinces;
    EditText edtSearch;
    ArrayList<Province> provinceArrayList;
    ArrayList<Province> provinceTempArrayList;
    ProvinceViewAdapter provinceAdapter;
    String tempProvinceName;
    Integer tempId;
    Intent intentProvince;
    ProgressBar progressBar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_provinces);
        intentProvince = new Intent(ChooseProvincesActivity.this,MainActivity.class);


        btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnDone = (TextView) findViewById(R.id.btnDone);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_province);

        lstProvinces = (ListView) findViewById(R.id.lstProvices);
        provinceArrayList = new ArrayList<>();
        provinceTempArrayList = new ArrayList<>();
        provinceAdapter = new ProvinceViewAdapter(this,R.layout.province_item,provinceArrayList,GetCurrentProvince());
        lstProvinces.setAdapter(provinceAdapter);

        GetDataProvince();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentProvince.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProvince);
                SaveProvinceName();
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int countProvinceFound =0;
                provinceArrayList.clear();
                for (int i = 0; i < provinceTempArrayList.size(); i++) {
                    if (provinceTempArrayList.get(i).getProvinceName().toLowerCase().contains(s.toString().toLowerCase())) {
                        provinceArrayList.add(provinceTempArrayList.get(i));
                        provinceAdapter.notifyDataSetChanged();
                        countProvinceFound ++;
                    }
                }
                if(countProvinceFound<1){
                    provinceArrayList.clear();
                    provinceAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void GetDataProvince(){

        new GetProvinceAsync().execute();

        provinceAdapter.notifyDataSetChanged();

    }

    public void SaveProvinceName(){
        SharedPreferences sharedPreferencesProvince ;
        sharedPreferencesProvince = getSharedPreferences("currentprovince",MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferencesProvince.edit();
        editor.putString("currentprovincename",tempProvinceName);
        editor.putInt("currentprovinceid",tempId);
        editor.commit();

    }

    public String GetCurrentProvince(){
        Intent intent =getIntent();
        return intent.getExtras().getString("CurrentProvince");
    }

    private class GetProvinceAsync extends AsyncTask<String, Void, ArrayList<Province>>{

        @Override
        protected ArrayList<Province> doInBackground(String... queries) {
            String query = "select * from Province";
            if(queries.length>0)
            {
                query = queries[0];
            }
            ArrayList<Province> provinces = new ArrayList<>();
            Log.e("Connection", "started");
            try  {
                // Set the connection string
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DBconn = DriverManager.getConnection(getString(R.string.connection));
                Statement stmt = DBconn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query);
                while(resultSet.next()){
                    int id = resultSet.getInt("Id");
                    String name = resultSet.getString("Name");
                    provinces.add(new Province(id,name));
                }
                DBconn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return provinces;
        }

        @Override
        protected void onPostExecute(ArrayList<Province> provinces) {
            super.onPostExecute(provinces);
            progressBar.setVisibility(View.GONE);
            SetProvinces(provinces);
        }
    }

    public void SetProvinces(ArrayList<Province> provinces){
        provinceArrayList.clear();
        for (Province province: provinces
             ) {
            provinceArrayList.add(province);
            provinceTempArrayList.add(province);
        }
        provinceAdapter.notifyDataSetChanged();
    }
    public void SendTempProvinceName(String name, Integer id){
        tempProvinceName =name;
        tempId =id;
    }


}
