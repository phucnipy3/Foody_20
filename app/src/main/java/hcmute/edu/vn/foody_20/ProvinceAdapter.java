package hcmute.edu.vn.foody_20;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ProvinceAdapter extends BaseAdapter {
    private ChooseProvincesActivity context;
    private int layout;
    private List<Province> provinceList;
    private String provinceName;
    private  int mSelectedItem = -1;

    public ProvinceAdapter(ChooseProvincesActivity context, int layout, List<Province> provinceList, String provinceName) {
        this.context = context;
        this.layout = layout;
        this.provinceList = provinceList;
        this.provinceName =provinceName;
    }


    @Override
    public int getCount() {
        return provinceList.size();
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
        TextView txtProvinceName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(layout,null);
            holder.txtProvinceName = (TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);
            holder.txtProvinceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    mSelectedItem = pos;
                    notifyDataSetChanged();

                }
            });

        }
        else {
            holder =(ViewHolder) convertView.getTag();
        }

        final Province province = provinceList.get(position);
        holder.txtProvinceName.setText(province.getProvinceName());
        if(provinceName.equals(province.getProvinceName())){
            Drawable iconCheck = context.getDrawable(R.drawable.icn_check);
            iconCheck.setBounds(0, 0, 60, 60);
            holder.txtProvinceName.setCompoundDrawables(null, null, iconCheck, null);
        }

        if (mSelectedItem != -1) {
            if (position == mSelectedItem) {
                Drawable iconCheck = context.getDrawable(R.drawable.icn_check);
                iconCheck.setBounds(0, 0, 60, 60);
                holder.txtProvinceName.setCompoundDrawables(null, null, iconCheck, null);
                context.SendProvinceName(province.getProvinceName());
            } else {
                holder.txtProvinceName.setCompoundDrawables(null, null, null, null);
            }
        }

        holder.txtProvinceName.setTag(position);
        return convertView;

    }
}
