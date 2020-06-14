package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    TextView txtProvinces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtProvinces = findViewById(R.id.txtProvinces);
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();

            String provinceName = intent.getExtras().getString("ProvinceName");
            txtProvinces.setText(provinceName);
        }
        txtProvinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChooseProvincesActivity.class);
                intent.putExtra("CurrentProvince",txtProvinces.getText());
                startActivity(intent);

            }
        });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    // Set the connection string
                    Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                    String username = "phucni";
                    String password = "12345678";

                    Connection DBconn = DriverManager.getConnection("jdbc:jtds:sqlserver://Nhom20FoodyDB.mssql.somee.com/Nhom20FoodyDB;user="+username+";password="+password);
                    Log.e("Connection","open");
                    Statement stmt = DBconn.createStatement();
                    ResultSet resultSet = stmt.executeQuery("select * from Province");
                    while(resultSet.next()){
                        String value = resultSet.getString("Name");
                        Log.e("Connection",value);
                    }
                    DBconn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
