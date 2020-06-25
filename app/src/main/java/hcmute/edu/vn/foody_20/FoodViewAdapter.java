package hcmute.edu.vn.foody_20;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodViewAdapter extends RecyclerView.Adapter<FoodViewAdapter.MyViewHolder> {

    private Context mContext;

    private List<FoodViewModel> mData;
    public FoodViewAdapter(Context mContext, List<FoodViewModel> mData) {
        this.mContext =mContext;
        this.mData =mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.menu_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int price = mData.get(position).getPrice().intValue()/1000;
        holder.tv_description.setText(mData.get(position).getFoodName() + " " + price +"K");
        Picasso.get().load(mData.get(position).getFoodImage()).into(holder.img_food);


    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tv_description;
        ImageView img_food;
        CardView cardView_food;

        public MyViewHolder(View itemView){
            super(itemView);
            tv_description =(TextView) itemView.findViewById(R.id.tvDescription);
            img_food=(ImageView) itemView.findViewById(R.id.imgFood);
            cardView_food =(CardView) itemView.findViewById(R.id.card_view_food_id);
        }
    }
}
