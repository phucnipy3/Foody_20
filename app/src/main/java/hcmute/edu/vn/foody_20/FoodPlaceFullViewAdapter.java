package hcmute.edu.vn.foody_20;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

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

public class FoodPlaceFullViewAdapter extends BaseAdapter {
    private SearchResultActivity context;
    private Activity mActivity;
    private int layout;
    private List<FoodPlaceFullViewModel> foodplaceList;

    private FusedLocationProviderClient client;
    private Location myLocation = new Location("");
    private Location foodplaceLocation = new Location("");
    private Geocoder geocoder;
    private List<Address> foodplaceaddresses;

    public FoodPlaceFullViewAdapter(Activity mActivity,SearchResultActivity context, int layout, List<FoodPlaceFullViewModel> foodplaceList) {
        this.context = context;
        this.layout = layout;
        this.foodplaceList = foodplaceList;
        this.mActivity = mActivity;
    }


    @Override
    public int getCount() {
        return foodplaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgFoodPlaceResult;
        TextView txtName,txtRate,txtAddress,txtS,txtKind,txtCmt,txtPhoto,txtDiscount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        geocoder = new Geocoder(context, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(mActivity);
        GetMyLocation();
        final ViewHolder holder;
        if(convertView==null)
        {
            holder = new  ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(layout,null);
            holder.imgFoodPlaceResult = (ImageView) convertView.findViewById(R.id.imgFoodPlaceResult);
            holder.txtName =(TextView) convertView.findViewById(R.id.txtNameResult);
            holder.txtRate =(TextView) convertView.findViewById(R.id.txtRate);
            holder.txtAddress =(TextView) convertView.findViewById(R.id.txtAddress);
            holder.txtS =(TextView) convertView.findViewById(R.id.txtS);
            holder.txtKind =(TextView) convertView.findViewById(R.id.txtKind);
            holder.txtCmt =(TextView) convertView.findViewById(R.id.txtCmt);
            holder.txtPhoto =(TextView) convertView.findViewById(R.id.txtPhoto);
            holder.txtDiscount =(TextView) convertView.findViewById(R.id.txtDiscount);
            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        final FoodPlaceFullViewModel foodPlaceFullViewModel = foodplaceList.get(position);
        String[] lstCode = new String[]{"FOODY20","50% tối đa 25K!","KHANGLD","30K cho đơn 40K!","PHUCNI","phí vận chuyển!","KOIEUAI","10% cho đơn 200K!"};
        Picasso.get().load(foodPlaceFullViewModel.getImage()).into(holder.imgFoodPlaceResult);
        holder.txtName.setText(foodPlaceFullViewModel.getName());
        holder.txtRate.setText(String.valueOf(foodPlaceFullViewModel.getRate()));
        holder.txtAddress.setText(foodPlaceFullViewModel.getAddress());
        holder.txtCmt.setText(String.valueOf(foodPlaceFullViewModel.getReviewCount()));
        holder.txtPhoto.setText(String.valueOf(foodPlaceFullViewModel.getCheckinCount()));
        holder.txtS.setText(Distance(foodPlaceFullViewModel.getAddress()).replace(",","."));

        int min = 1;
        int max = 4;
        Random r = new Random();
        int i = r.nextInt(max - min + 1) + min;
        holder.txtDiscount.setText("  Nhập '"+lstCode[i*2-2]+"' nhận giảm "+lstCode[i*2-1]);
        return convertView;
    }

    public void GetMyLocation(){
        if(ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(mActivity,
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
