package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChooseProvincesActivity extends AppCompatActivity {
    TextView btnCancel,btnDone;
    Database database;
    ListView lstProvinces;
    EditText edtSearch;
    ArrayList<Province> provinceArrayList;
    ProvinceAdapter provinceAdapter;
    Intent intentProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_provinces);
        intentProvince = new Intent(ChooseProvincesActivity.this,MainActivity.class);


        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);
        edtSearch = findViewById(R.id.edtSearch);

        lstProvinces = (ListView) findViewById(R.id.lstProvices);
        provinceArrayList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(this,R.layout.line_province,provinceArrayList,GetCurrentProvince());
        lstProvinces.setAdapter(provinceAdapter);

        database = new Database(this,"Foody.sqlite",null,1);

        database.QueryData("CREATE TABLE IF NOT EXISTS Province(Id INTEGER PRIMARY KEY AUTOINCREMENT, ProvinceName NVARCHAR(200))");
        if(!ProvincesExisted())
            CreateProvincesinDB();

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
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Cursor dataSearch =database.GetData("SELECT * FROM Province Where ProvinceName like '%"+s+"%' ");
                provinceArrayList.clear();
                while (dataSearch.moveToNext()){
                    String name = dataSearch.getString(1);
                    int id = dataSearch.getInt(0);
                    provinceArrayList.add(new Province(id,name));
                }
                provinceAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void GetDataProvince(){
        Cursor dataProvince =database.GetData("SELECT * FROM Province");
        provinceArrayList.clear();
        while (dataProvince.moveToNext()){
            String name = dataProvince.getString(1);
            int id = dataProvince.getInt(0);
            provinceArrayList.add(new Province(id,name));
        }
        provinceAdapter.notifyDataSetChanged();

    }
    public boolean ProvincesExisted (){
        Cursor dataProvince =database.GetData("SELECT * FROM Province");
        if(dataProvince.getCount()>1)
            return true;
        return false;
    }
    public void CreateProvincesinDB(){
        database.QueryData("INSERT INTO Province VALUES(null,'TP HCM')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hà Nội')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bắc Ninh')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hưng Yên')");
        database.QueryData("INSERT INTO Province VALUES(null,'Thái Bình')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hải Phòng')");
        database.QueryData("INSERT INTO Province VALUES(null,'Nam Định')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hải Dương')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hà Nam')");
        database.QueryData("INSERT INTO Province VALUES(null,'Vĩnh Phúc')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bình Dương')");
        database.QueryData("INSERT INTO Province VALUES(null,'Đà Nẵng')");
        database.QueryData("INSERT INTO Province VALUES(null,'Cần Thơ')");
        database.QueryData("INSERT INTO Province VALUES(null,'Ninh Bình')");
        database.QueryData("INSERT INTO Province VALUES(null,'Tiền Giang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Vĩnh Long')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bà Rịa - Vũng Tàu')");
        database.QueryData("INSERT INTO Province VALUES(null,'An Giang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bến Tre')");
        database.QueryData("INSERT INTO Province VALUES(null,'Đồng Nai')");
        database.QueryData("INSERT INTO Province VALUES(null,'Đồng Tháp')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bắc Giang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hậu Giang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Trà Vinh')");
        database.QueryData("INSERT INTO Province VALUES(null,'Phú Thọ')");
        database.QueryData("INSERT INTO Province VALUES(null,'Long An')");
        database.QueryData("INSERT INTO Province VALUES(null,'Thái Nguyên')");
        database.QueryData("INSERT INTO Province VALUES(null,'Sóc Trăng')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bạc Liêu')");
        database.QueryData("INSERT INTO Province VALUES(null,'Thanh Hóa')");
        database.QueryData("INSERT INTO Province VALUES(null,'Tây Ninh')");
        database.QueryData("INSERT INTO Province VALUES(null,'Kiên Giang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bình Định')");
        database.QueryData("INSERT INTO Province VALUES(null,'Khánh Hòa')");
        database.QueryData("INSERT INTO Province VALUES(null,'Quảng Ngãi')");
        database.QueryData("INSERT INTO Province VALUES(null,'Cà Mau')");
        database.QueryData("INSERT INTO Province VALUES(null,'Thừa Thiên - Huế')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hà Tĩnh')");
        database.QueryData("INSERT INTO Province VALUES(null,'Quảng Ninh')");
        database.QueryData("INSERT INTO Province VALUES(null,'Nghệ An')");
        database.QueryData("INSERT INTO Province VALUES(null,'Phú Yên')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hòa Bình')");
        database.QueryData("INSERT INTO Province VALUES(null,'Ninh Thuận')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bình Thuận')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bình Phước')");
        database.QueryData("INSERT INTO Province VALUES(null,'Đắk Lắk')");
        database.QueryData("INSERT INTO Province VALUES(null,'Quảng Nam')");
        database.QueryData("INSERT INTO Province VALUES(null,'Tuyên Quang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Lâm Đồng')");
        database.QueryData("INSERT INTO Province VALUES(null,'Quảng Trị')");
        database.QueryData("INSERT INTO Province VALUES(null,'Yên Bái')");
        database.QueryData("INSERT INTO Province VALUES(null,'Lào Cai')");
        database.QueryData("INSERT INTO Province VALUES(null,'Quảng Bình')");
        database.QueryData("INSERT INTO Province VALUES(null,'Hà Giang')");
        database.QueryData("INSERT INTO Province VALUES(null,'Gia Lai')");
        database.QueryData("INSERT INTO Province VALUES(null,'Đắk Nông')");
        database.QueryData("INSERT INTO Province VALUES(null,'Lạng Sơn')");
        database.QueryData("INSERT INTO Province VALUES(null,'Sơn La')");
        database.QueryData("INSERT INTO Province VALUES(null,'Cao Bằng')");
        database.QueryData("INSERT INTO Province VALUES(null,'Bắc Kạn')");
        database.QueryData("INSERT INTO Province VALUES(null,'Điện Biên')");
        database.QueryData("INSERT INTO Province VALUES(null,'Kon Tum')");
        database.QueryData("INSERT INTO Province VALUES(null,'Lai Châu')");

    }
    public  void SendProvinceName( String name){
        intentProvince.putExtra("ProvinceName",name);
    }

    public String GetCurrentProvince(){
        Intent intent =getIntent();
        return intent.getExtras().getString("CurrentProvince");
    }


}
