package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.example.admin.housemanage.R;

import java.util.ArrayList;

import bean.Child;
import bean.Group;

public class EListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
    private Context context;
    private ArrayList<Group> groups;
    private MapView mapView;
    private ArcGISFeatureLayer.Options options;
//    private String url;
    private ArcGISFeatureLayer nulllayer;
//    private int j;
//    private int a;
//    private int b;

    public EListAdapter(Context context, ArrayList<Group> groups, MapView mapView,ArcGISFeatureLayer nulllayer) {
        this.context = context;
        this.groups = groups;
        this.mapView=mapView;
        this.nulllayer = nulllayer;

    }

    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildItem(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildrenCount();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /** 設定 Group 資料 */
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);


        int childrenCount = groups.get(groupPosition).getChildrenCount();


        int b=groups.get(groupPosition).getIndex();
        if (childrenCount==0){
            if (mapView.getLayer(groups.get(groupPosition).getIndex()).isVisible()){
                group.setChecked(true);
            }else{
                group.setChecked(false);
            }
        }else {
            if (groupPosition==0){
                if (mapView.getLayer(8).isVisible()&&mapView.getLayer(9).isVisible()){
                    group.setChecked(true);
                }else{
                    group.setChecked(false);
                }
            }
            else {
                if (mapView.getLayer(7).isVisible()){
                    group.setChecked(true);
                }else{
                    group.setChecked(false);
                }
            }
        }


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_layout, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group.getTitle());

        // 重新產生 CheckBox 時，將存起來的 isChecked 狀態重新設定
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chbGroup);


        // 點擊 CheckBox 時，將狀態存起來
        checkBox.setOnClickListener(new Group_CheckBox_Click(groupPosition));
        checkBox.setChecked(group.getChecked());
        return convertView;
    }

    /** 勾選 Group CheckBox 時，存 Group CheckBox 的狀態，以及改變 Child CheckBox 的狀態 */
    class Group_CheckBox_Click implements OnClickListener {
        private int groupPosition;


        Group_CheckBox_Click(int groupPosition) {
            this.groupPosition = groupPosition;

        }

        public void onClick(View v) {
            groups.get(groupPosition).toggle();

            // 將 Children 的 isChecked 全面設成跟 Group 一樣
            int childrenCount = groups.get(groupPosition).getChildrenCount();
            boolean groupIsChecked = groups.get(groupPosition).getChecked();


            // 注意，一定要通知 ExpandableListView 資料已經改變，ExpandableListView 會重新產生畫面

            if (childrenCount == 0) {

                mapView.getLayer(groups.get(groupPosition).getIndex()).setVisible(groupIsChecked);
            }else{
                for (int i = 0; i < childrenCount; i++) {
                    groups.get(groupPosition).getChildItem(i).setChecked(groupIsChecked);
                    mapView.getLayer(groups.get(groupPosition).getChildItem(i).getlayer()).setVisible(groupIsChecked);
                }
            }
            notifyDataSetChanged();
        }
    }

    /** 設定 Children 資料 */
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Child child = groups.get(groupPosition).getChildItem(childPosition);
        if (mapView.getLayer(groups.get(groupPosition).getChildItem(childPosition).getlayer())==nulllayer){
            groups.get(groupPosition).getChildItem(childPosition).setChecked(false);
        }


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
        tv.setText(child.getFullname());

        // 重新產生 CheckBox 時，將存起來的 isChecked 狀態重新設定
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chbChild);


        // 點擊 CheckBox 時，將狀態存起來
        checkBox.setOnClickListener(new Child_CheckBox_Click(groupPosition, childPosition));
        checkBox.setChecked(child.getChecked());
        return convertView;
    }

    /** 勾選 Child CheckBox 時，存 Child CheckBox 的狀態 */
    class Child_CheckBox_Click implements OnClickListener {
        private int groupPosition;
        private int childPosition;

        Child_CheckBox_Click(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        public void onClick(View v) {
            handleClick(childPosition, groupPosition);
        }
    }

    public void handleClick(int childPosition, int groupPosition) {
        groups.get(groupPosition).getChildItem(childPosition).toggle();

        // 檢查 Child CheckBox 是否有全部勾選，以控制 Group CheckBox
        int childrenCount = groups.get(groupPosition).getChildrenCount();
        boolean childrenAllIsChecked = true;
        for (int i = 0; i < childrenCount; i++) {
            if (!groups.get(groupPosition).getChildItem(i).getChecked())
                childrenAllIsChecked = false;
        }

        groups.get(groupPosition).setChecked(childrenAllIsChecked);
        mapView.getLayer(groups.get(groupPosition).getChildItem(childPosition).getlayer()).setVisible(groups.get(groupPosition).getChildItem(childPosition).getChecked());
        // 注意，一定要通知 ExpandableListView 資料已經改變，ExpandableListView 會重新產生畫面

        notifyDataSetChanged();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        handleClick(childPosition, groupPosition);
        return true;
    }

}


