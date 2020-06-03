package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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


    }
}
