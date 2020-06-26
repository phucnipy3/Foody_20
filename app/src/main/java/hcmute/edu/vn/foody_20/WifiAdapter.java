package hcmute.edu.vn.foody_20;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WifiAdapter extends BaseAdapter {
    private Context context;
    private List<WifiViewModel> lstWifi;

    public WifiAdapter(Context context,  List<WifiViewModel> lstWifi) {
        this.context = context;
        this.lstWifi = lstWifi;
    }

    @Override
    public int getCount() {
        return lstWifi.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private  class ViewHolder{
        TextView wifiName;
        TextView wifiPass;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.wifi_items,null);
            holder.wifiName = (TextView) convertView.findViewById(R.id.tvWifiNameItems);
            holder.wifiPass = (TextView) convertView.findViewById(R.id.tvPassItems);
            convertView.setTag(holder);
        }
        else {
            holder =(ViewHolder) convertView.getTag();
        }
        final WifiViewModel wifiViewModel = lstWifi.get(position);
        holder.wifiName.setText(wifiViewModel.getName());
        holder.wifiPass.setText(wifiViewModel.getPassword());
        return convertView;
    }
}
