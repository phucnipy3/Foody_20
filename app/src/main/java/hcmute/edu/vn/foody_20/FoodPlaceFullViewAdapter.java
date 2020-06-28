package hcmute.edu.vn.foody_20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FoodPlaceFullViewAdapter extends RecyclerView.Adapter<FoodPlaceFullViewAdapter.MyViewHolder>  {
    private Activity mActivity;
    private int layout;
    private List<FoodPlaceFullViewModel> foodplaceList;

    private FusedLocationProviderClient client;
    private Location myLocation = new Location("");
    private Location foodplaceLocation = new Location("");
    private Geocoder geocoder;
    private List<Address> foodplaceaddresses;

    private Context mContext;

    public FoodPlaceFullViewAdapter(Activity mActivity,Context mContext, List<FoodPlaceFullViewModel> foodplaceList) {
        this.mContext = mContext;
        this.foodplaceList = foodplaceList;
        this.mActivity = mActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.result_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtNameResult.setText(foodplaceList.get(position).getName().toString());
        holder.txtAddress.setText(foodplaceList.get(position).getAddress().toString());
        holder.txtCmt.setText(String.valueOf(foodplaceList.get(position).getReviewCount()));
        holder.txtKind.setText(String.valueOf(foodplaceList.get(position).getType()));
        holder.txtRate.setText(String.valueOf(foodplaceList.get(position).getRate()));
        holder.txtPhoto.setText(String.valueOf(foodplaceList.get(position).getCheckinCount()));
        String[] lstCode = new String[]{"FOODY20","50% tối đa 25K!","KHANGLD","30K cho đơn 40K!","PHUCNI","phí vận chuyển!","KOIEUAI","10% cho đơn 200K!"};
        int min = 1;

        int max = 4;
        Random r = new Random();
        int i = r.nextInt(max - min + 1) + min;
        holder.txtDiscount.setText("  Nhập '"+lstCode[i*2-2]+"' nhận giảm "+lstCode[i*2-1]);
        geocoder = new Geocoder(mContext, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(mContext);
        GetMyLocation();
        holder.txtS.setText(Distance(foodplaceList.get(position).getAddress()));
        Picasso.get().load(foodplaceList.get(position).getImage()).into(holder.imgFoodPlace);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.putExtra("idFoodPlace",foodplaceList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodplaceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView txtNameResult,txtAddress,txtCmt,txtPhoto,txtKind,txtS,txtRate,txtDiscount;
        ImageView imgFoodPlace;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            txtNameResult = (TextView) itemView.findViewById(R.id.txtNameResult);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtCmt = (TextView) itemView.findViewById(R.id.txtCmt);
            txtPhoto = (TextView) itemView.findViewById(R.id.txtPhoto);
            txtKind = (TextView) itemView.findViewById(R.id.txtKind);
            txtS = (TextView) itemView.findViewById(R.id.txtS);
            txtRate = (TextView) itemView.findViewById(R.id.txtRate);
            txtDiscount = (TextView) itemView.findViewById(R.id.txtDiscount);
            imgFoodPlace = (ImageView) itemView.findViewById(R.id.imgFoodPlaceResult) ;
            cardView =(CardView) itemView.findViewById(R.id.card_view_search_id);

        }
    }

    public void GetMyLocation(){
        if(ActivityCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( mActivity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
            return;
        }
        Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    myLocation.setLatitude(location.getLatitude());
                    myLocation.setLongitude(location.getLongitude());

                }
            }
        });

    }
    public String Distance(String foodplaceAddress){
        double distance = 0.0;
        try {
            foodplaceaddresses = geocoder.getFromLocationName(foodplaceAddress, 1);
            if ( foodplaceaddresses != null && foodplaceaddresses.size() > 0) {

                Address address = (Address) foodplaceaddresses.get(0);
                foodplaceLocation.setLatitude(address.getLatitude());
                foodplaceLocation.setLongitude(address.getLongitude());
                distance = myLocation.distanceTo(foodplaceLocation)/1000;

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DecimalFormat("##.#").format(distance) + " " +"km";
    }
}
