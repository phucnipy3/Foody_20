package hcmute.edu.vn.foody_20;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodPlaceCardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<FoodPlaceCardViewModel> mData;
    public FoodPlaceCardViewAdapter(Context mContext, List<FoodPlaceCardViewModel> mData) {
        this.mContext =mContext;
        this.mData =mData;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_ITEM){
            View view;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.main_item,parent,false);
            return new ItemViewHolder(view);
        }
        else {
            View view;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.loading_item,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }



    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_review;
        TextView tv_FPName;
        ImageView img_foodplace;
        CardView cardView_foodplace;
        TextView tv_reviewed;

        public ItemViewHolder(View itemView){
            super(itemView);
            tv_FPName =(TextView) itemView.findViewById(R.id.tvFPName);
            tv_review =(TextView) itemView.findViewById(R.id.tvReview);
            img_foodplace=(ImageView) itemView.findViewById(R.id.imgFoodPlace);
            cardView_foodplace =(CardView) itemView.findViewById(R.id.card_view_id);
            tv_reviewed = itemView.findViewById(R.id.tvReviewed);
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgLoading);
        }
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, final int position) {

        FoodPlaceCardViewModel item = mData.get(position);

        if(item.getReview().equals(""))
        {
            viewHolder.tv_reviewed.setVisibility(View.INVISIBLE);
        }
        else {
            viewHolder.tv_reviewed.setVisibility(View.VISIBLE);

        }
        viewHolder.tv_FPName.setText(item.getName());
        viewHolder.tv_review.setText(item.getReview());
        Picasso.get().load(item.getImage()).into(viewHolder.img_foodplace);

        viewHolder.cardView_foodplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.putExtra("idFoodPlace",mData.get(position).getId());
                intent.putExtra("nameFoodPlace",mData.get(position).getName());
                mContext.startActivity(intent);
            }
        });
    }
}

