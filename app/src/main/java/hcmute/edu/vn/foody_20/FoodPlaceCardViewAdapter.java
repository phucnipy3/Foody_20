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

public class FoodPlaceCardViewAdapter extends RecyclerView.Adapter<FoodPlaceCardViewAdapter.MyViewHolder> {

    private Context mContext;

    private List<FoodPlaceCardViewModel> mData;
    public FoodPlaceCardViewAdapter(Context mContext, List<FoodPlaceCardViewModel> mData) {
        this.mContext =mContext;
        this.mData =mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.foodplace_cardview_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_review.setText(mData.get(position).getReview());
        Picasso.get().load(mData.get(position).getImage()).into(holder.img_foodplace);
        holder.cardView_foodplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,Book_Activity.class);
//                intent.putExtra("Title",mData.get(position).getTitle());
//                intent.putExtra("Description",mData.get(position).getDescription());
//                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tv_review;
        ImageView img_foodplace;
        CardView cardView_foodplace;

        public MyViewHolder(View itemView){
            super(itemView);
            tv_review =(TextView) itemView.findViewById(R.id.tvReview);
            img_foodplace=(ImageView) itemView.findViewById(R.id.imgFoodPlace);
            cardView_foodplace =(CardView) itemView.findViewById(R.id.card_view_id);
        }
    }
}

