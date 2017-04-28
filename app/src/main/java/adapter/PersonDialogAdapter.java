package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.BranchBean;

/**
 * Created by admin on 2016/5/18.
 */
public class PersonDialogAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<BranchBean> branchBeans;
    private int index;

    public PersonDialogAdapter(Context context, List<BranchBean> branchBeans,int index) {
        this.context = context;
        this.branchBeans = branchBeans;
        this.index = index;
    }

    @Override
    public int getGroupCount() {
        return branchBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return branchBeans.get(groupPosition).getPersonList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return branchBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return branchBeans.get(groupPosition).getPersonList().get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHolder groupHolder;
        if(convertView==null){
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.person_item_group,null);
            groupHolder.branch = (TextView) convertView.findViewById(R.id.text_groupitem_branch);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.branch.setText(branchBeans.get(groupPosition).getBranch());


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder viewHolder;
        if(convertView==null){
            viewHolder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.person_dialog_item,null);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_persondialog_name);
            viewHolder.number = (TextView) convertView.findViewById(R.id.text_persondialog_number);
            viewHolder.branch = (TextView) convertView.findViewById(R.id.text_persondialog_Branch);
            viewHolder.name = (TextView) convertView.findViewById(R.id.text_persondialog_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ChildHolder) convertView.getTag();
        }


        if(branchBeans.get(groupPosition).getPersonList().get(childPosition).getCertNo()!=null&&!branchBeans.get(groupPosition).getPersonList().get(childPosition).getCertNo().equals("")){
            viewHolder.number.setText("执法证号 : "+branchBeans.get(groupPosition).getPersonList().get(childPosition).getCertNo());
        }else{
            viewHolder.number.setText("");
        }

        viewHolder.branch.setText("所属部门 : " + branchBeans.get(groupPosition).getPersonList().get(childPosition).getBranch());
        if(index==1){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.name.setVisibility(View.GONE);
            viewHolder.checkBox.setText(branchBeans.get(groupPosition).getPersonList().get(childPosition).getName());
            viewHolder.checkBox.setOnCheckedChangeListener(new OnCheck(groupPosition,childPosition));
            viewHolder.checkBox.setChecked(branchBeans.get(groupPosition).getPersonList().get(childPosition).isChecked());
        }else if(index==2){
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.checkBox.setClickable(false);
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.name.setText(branchBeans.get(groupPosition).getPersonList().get(childPosition).getName());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class OnCheck implements CompoundButton.OnCheckedChangeListener{

        private int groupPosition;
        private int childPosition;

        public OnCheck(int groupPosition,int childPosition) {
            this.childPosition = childPosition;
            this.groupPosition = groupPosition;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            branchBeans.get(groupPosition).getPersonList().get(childPosition).setIsChecked(isChecked);
        }
    }

    class ChildHolder{
        CheckBox checkBox;
        TextView number,branch,name;

    }
    class GroupHolder{
        TextView branch;
    }

}
