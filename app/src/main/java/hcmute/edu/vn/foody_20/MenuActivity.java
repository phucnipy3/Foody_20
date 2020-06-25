package hcmute.edu.vn.foody_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    LinearLayout btnChangeMenu;
    Button btnBackMenu;
    RecyclerView rcvMenu;
    ExpandableListView eplMenu;
    TextView tvNameMenu,tvShowPicture,tvShowMenu;

    ExpandMenuAdapter adapter;
    List<String> lstFoodKind;
    HashMap<String,List<String>> lstFoodInKind;


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

        lstFoodKind = new ArrayList<>();
        lstFoodInKind= new HashMap<>();

        adapter = new ExpandMenuAdapter(this,lstFoodKind,lstFoodInKind);
        eplMenu.setAdapter(adapter);

        initData();

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

    private void initData() {
        lstFoodKind.add("Trà sữa");
        lstFoodKind.add("Cháo");
        lstFoodKind.add("Bò nướng");

        for (int i=0;i<lstFoodKind.size();i++  ) {
            List<String> lst = new ArrayList<>();
            for (int j=1;j<5;j++) {
                lst.add(lstFoodKind.get(i).toString() + " 0" + String.valueOf(j));
            }
            lstFoodInKind.put(lstFoodKind.get(i).toString(),lst);
        }
        adapter.notifyDataSetChanged();
    }
}