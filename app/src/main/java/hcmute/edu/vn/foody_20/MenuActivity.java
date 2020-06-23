package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    LinearLayout btnChangeMenu;
    Button btnBackMenu;
    RecyclerView rcvMenu;
    ExpandableListView eplMenu;
    TextView tvNameMenu,tvShowPicture,tvShowMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnChangeMenu = findViewById(R.id.btnChangeMenu);
        btnBackMenu = findViewById(R.id.btnBackMenu);
        rcvMenu = findViewById(R.id.rcvMenu);
        eplMenu = findViewById(R.id.eplMenu);
        tvNameMenu = findViewById(R.id.tvNameMenu);
        tvShowMenu = findViewById(R.id.tvShowMenu);
        tvShowPicture = findViewById(R.id.tvShowPicture);

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
                    rcvMenu.setVisibility(View.VISIBLE);
                    eplMenu.setVisibility(View.INVISIBLE);
                }else{
                    rcvMenu.setVisibility(View.INVISIBLE);
                    eplMenu.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}