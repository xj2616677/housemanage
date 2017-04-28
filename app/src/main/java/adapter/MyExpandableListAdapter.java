package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.CheckTermGroupBean;

/**
 * 检查项选择adapter
 * Created by admin on 2016/4/13.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private List<CheckTermGroupBean> checkTermGroupBeans;
    private Context context;
    private String wujiao = "☆";

    public MyExpandableListAdapter(List<CheckTermGroupBean> checkTermGroupBeans,Context context) {
        this.checkTermGroupBeans = checkTermGroupBeans;
        this.context = context;
    }

    public List<CheckTermGroupBean> getCheckTermGroupBeans() {
        return checkTermGroupBeans;
    }


    @Override
    public int getGroupCount() {
        return checkTermGroupBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return checkTermGroupBeans.get(groupPosition).getChildCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return checkTermGroupBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return checkTermGroupBeans.get(groupPosition).getChild(childPosition);
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
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if(convertView==null) {
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expanditem_group, null);
            groupHolder.groupCheckBox = (CheckBox) convertView.findViewById(R.id.cb_groupitem);
            convertView.setTag(groupHolder);
        } else{
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.groupCheckBox.setText(checkTermGroupBeans.get(groupPosition).getGroupName());
        groupHolder.groupCheckBox.setChecked(checkTermGroupBeans.get(groupPosition).getChecked());
        groupHolder.groupCheckBox.setOnClickListener(new Group_CheckBox_Click(groupPosition,groupHolder));


        return convertView;
    }


    class Group_CheckBox_Click implements View.OnClickListener {
        private int groupPosition;
        private GroupHolder groupHolder;


        Group_CheckBox_Click(int groupPosition,GroupHolder groupHolder) {
            this.groupPosition = groupPosition;
            this.groupHolder = groupHolder;
        }

        public void onClick(View v) {
            groupHolder.groupCheckBox.setChecked(!checkTermGroupBeans.get(groupPosition).getChecked());
            checkTermGroupBeans.get(groupPosition).toggle();
            int childrenCount = checkTermGroupBeans.get(groupPosition).getChildCount();
            boolean groupIsChecked = checkTermGroupBeans.get(groupPosition).getChecked();
            for (int i = 0; i < childrenCount; i++) {
                checkTermGroupBeans.get(groupPosition).getChild(i).setIsCheck(groupIsChecked);
            }

            notifyDataSetChanged();
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView==null) {
            childHolder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expanditem_child, null);
            childHolder.childCheckBox = (CheckBox) convertView.findViewById(R.id.cb_childitem);
            convertView.setTag(childHolder);
        }else{
            childHolder = (ChildHolder) convertView.getTag();
        }
        if("0".equals(checkTermGroupBeans.get(groupPosition).getChild(childPosition).getIsInLaw())){
            childHolder.childCheckBox.setText(checkTermGroupBeans.get(groupPosition).getChild(childPosition).getTitle());
            childHolder.childCheckBox.setTextColor(context.getResources().getColor(R.color.black));
        }else{
            childHolder.childCheckBox.setText(wujiao+checkTermGroupBeans.get(groupPosition).getChild(childPosition).getTitle());
            childHolder.childCheckBox.setTextColor(context.getResources().getColor(R.color.red));
        }

        childHolder. childCheckBox.setChecked(checkTermGroupBeans.get(groupPosition).getChild(childPosition).isCheck());
        childHolder.childCheckBox.setOnClickListener(new Child_CheckBox_Click(groupPosition, childPosition,childHolder));
        return convertView;
    }


    class Child_CheckBox_Click implements View.OnClickListener {
        private int groupPosition;
        private int childPosition;
        private ChildHolder childHolder;

        Child_CheckBox_Click(int groupPosition, int childPosition,ChildHolder childHolder) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            this.childHolder = childHolder;
        }

        public void onClick(View v) {
            childHolder.childCheckBox.setChecked(!checkTermGroupBeans.get(groupPosition).getChild(childPosition).isCheck());
            changeChecked(childPosition, groupPosition);
        }
    }

    public void changeChecked(int childPosition, int groupPosition) {
        checkTermGroupBeans.get(groupPosition).getChild(childPosition).toggle();

        int childrenCount = checkTermGroupBeans.get(groupPosition).getChildCount();
        boolean childrenAllIsChecked = true;
        for (int i = 0; i < childrenCount; i++) {
            if (!checkTermGroupBeans.get(groupPosition).getChild(i).isCheck())
                childrenAllIsChecked = false;
        }

        checkTermGroupBeans.get(groupPosition).setChecked(childrenAllIsChecked);
        notifyDataSetChanged();
    }


    class GroupHolder{
        CheckBox groupCheckBox;
    }
    class ChildHolder{
        CheckBox childCheckBox;
    }
}
