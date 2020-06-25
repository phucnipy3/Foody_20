package hcmute.edu.vn.foody_20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandMenuAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> lstFoodKind;
    HashMap<String,List<String>> lstFoodInKind;

    public ExpandMenuAdapter(Context context, List<String> lstFoodKind, HashMap<String, List<String>> lstFoodInKind) {
        this.context = context;
        this.lstFoodKind = lstFoodKind;
        this.lstFoodInKind = lstFoodInKind;
    }

    @Override
    public int getGroupCount() {
        return lstFoodKind.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.lstFoodInKind.get(lstFoodKind.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lstFoodKind.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lstFoodInKind.get(lstFoodKind.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String kind = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.foodkind_items,null);
        }
        TextView textView = convertView.findViewById(R.id.tvKindMenu);
        textView.setText(kind);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String food = (String) getChild(groupPosition,childPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.foodinkind_items,null);
        }
        TextView textView = convertView.findViewById(R.id.tvFoodMenu);
        textView.setText(food);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
