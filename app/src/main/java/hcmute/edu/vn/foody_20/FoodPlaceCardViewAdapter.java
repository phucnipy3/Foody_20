package hcmute.edu.vn.foody_20;

import android.content.Context;
import android.content.Intent;
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

        public ItemViewHolder(View itemView){
            super(itemView);
            tv_FPName =(TextView) itemView.findViewById(R.id.tvFPName);
            tv_review =(TextView) itemView.findViewById(R.id.tvReview);
            img_foodplace=(ImageView) itemView.findViewById(R.id.imgFoodPlace);
            cardView_foodplace =(CardView) itemView.findViewById(R.id.card_view_id);
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarLoading);
        }
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, final int position) {

        FoodPlaceCardViewModel item = mData.get(position);

        viewHolder.tv_FPName.setText(mData.get(position).getName());
        viewHolder.tv_review.setText(mData.get(position).getReview());
        Picasso.get().load(mData.get(position).getImage()).into(viewHolder.img_foodplace);

        viewHolder.cardView_foodplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.putExtra("idFoodPlace",mData.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }
}

