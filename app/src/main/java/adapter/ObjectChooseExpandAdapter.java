package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.PropertyProjectBean;

/**
 * Created by admin on 2016/5/31.
 */
public class ObjectChooseExpandAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<PropertyProjectBean> projectBeans;


    public ObjectChooseExpandAdapter(Context context, List<PropertyProjectBean> projectBeans) {
        this.context = context;
        this.projectBeans = projectBeans;
    }

    @Override
    public int getGroupCount() {
        return projectBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return projectBeans.get(groupPosition).getGeneralBeans().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return projectBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return projectBeans.get(groupPosition).getGeneralBeans().get(childPosition);
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
        groupHolder.groupCheckBox.setText(projectBeans.get(groupPosition).getProjectName());
        groupHolder.groupCheckBox.setChecked(projectBeans.get(groupPosition).isCheck());
        groupHolder.groupCheckBox.setOnClickListener(new Group_CheckBox_Click(groupPosition));


        return convertView;
    }


    class Group_CheckBox_Click implements View.OnClickListener {
        private int groupPosition;


        Group_CheckBox_Click(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        public void onClick(View v) {
            projectBeans.get(groupPosition).toggle();
            int childrenCount = projectBeans.get(groupPosition).getGeneralBeans().size();
            boolean groupIsChecked = projectBeans.get(groupPosition).isCheck();
            for (int i = 0; i < childrenCount; i++) {
                projectBeans.get(groupPosition).getGeneralBeans().get(i).setIsCheck(groupIsChecked);
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
        childHolder. childCheckBox.setText(projectBeans.get(groupPosition).getGeneralBeans().get(childPosition).getName());

        childHolder. childCheckBox.setChecked(projectBeans.get(groupPosition).getGeneralBeans().get(childPosition).isCheck());
        childHolder.childCheckBox.setOnClickListener(new Child_CheckBox_Click(groupPosition, childPosition));
        return convertView;
    }


    class Child_CheckBox_Click implements View.OnClickListener {
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
        projectBeans.get(groupPosition).getGeneralBeans().get(childPosition).toggle();

        int childrenCount = projectBeans.get(groupPosition).getGeneralBeans().size();
        boolean childrenAllIsChecked = true;
        for (int i = 0; i < childrenCount; i++) {
            if (!projectBeans.get(groupPosition).getGeneralBeans().get(i).isCheck())
                childrenAllIsChecked = false;
        }

        projectBeans.get(groupPosition).setIsCheck(childrenAllIsChecked);
        notifyDataSetChanged();
    }







    class GroupHolder{
        CheckBox groupCheckBox;
    }
    class ChildHolder{
        CheckBox childCheckBox;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
