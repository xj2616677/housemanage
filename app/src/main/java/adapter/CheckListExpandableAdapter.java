package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.ArrayList;
import java.util.List;

import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import constants.Contant;

/**
 * Created by admin on 2016/4/13.
 */
public class CheckListExpandableAdapter extends BaseExpandableListAdapter {

    private List<CheckTermGroupBean> checkTermGroupBeans;
    private Context context;
    private int flag;
    private IllegalAdapter illegalAdapter;
    private List<CheckTermBean> termBeans;

    public CheckListExpandableAdapter(List<CheckTermGroupBean> checkTermGroupBeans, Context context,int flag) {
        this.checkTermGroupBeans = checkTermGroupBeans;
        this.context = context;
        this.flag = flag;
    }
    public CheckListExpandableAdapter(List<CheckTermGroupBean> checkTermGroupBeans, Context context,int flag,IllegalAdapter illegalAdapter,List<CheckTermBean> termBeans) {
        this.checkTermGroupBeans = checkTermGroupBeans;
        this.context = context;
        this.flag = flag;
        this.illegalAdapter = illegalAdapter;
        this.termBeans = termBeans;
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
    public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if(convertView == null){
            groupHolder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listexpanditem_group,null);
            groupHolder.title = (TextView) convertView.findViewById(R.id.text_listexpand_title);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.title.setText(checkTermGroupBeans.get(groupPosition).getGroupName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;
        final int groupIndex = groupPosition;
        final int childIndex = childPosition;
        if(convertView ==null){
            childHolder = new ChildHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listexpanditem_child,null);
            childHolder.name = (TextView) convertView.findViewById(R.id.text_listexpandchild_name);
            childHolder.radioGroup = (LinearLayout) convertView.findViewById(R.id.rg_listexpandchild);
            childHolder.wujiao = (TextView) convertView.findViewById(R.id.text_listexpandchild_wujiao);
            convertView.setTag(childHolder);
        }else{
            childHolder = (ChildHolder) convertView.getTag();
        }

        if("0".equals(checkTermGroupBeans.get(groupPosition).getChild(childPosition).getIsInLaw())){
            childHolder.wujiao.setVisibility(View.GONE);
        }else{
            childHolder.wujiao.setVisibility(View.VISIBLE);

        }
        childHolder.radioGroup.removeAllViews();
        childHolder.name.setText(checkTermGroupBeans.get(groupPosition).getChild(childPosition).getTitle());
        CheckTermBean bean = checkTermGroupBeans.get(groupPosition).getChild(childPosition);
        String allOption = bean.getOptions();
        String[] options = allOption.split(";");
//        int[] values = new int[options.length];
        if(options!=null&&options.length!=0){
            for(int i=0;i<options.length;i++){
//                RadioButton radioButton = new RadioButton(context);
//                radioButton.setText(options[i]);
//                radioButton.setTextSize(20);
//                childHolder.radioGroup.addView(radioButton);
                final int index = i;
                 CheckBox checkBox = new CheckBox(context);
                checkBox.setText(options[i]);
                if(options[i].equals(checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex).getValue())){
                    checkBox.setChecked(true);
                }
                checkBox.setTextSize(20);
                childHolder.radioGroup.addView(checkBox);
                if(flag ==0){
                    checkBox.setClickable(false);
                    checkBox.setEnabled(false);
                }
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = childHolder.radioGroup.getChildCount();
                        for (int j = 0; j < count; j++) {
                            if (j == index) {
//                                checkBox.isChecked();
                                if (((CheckBox) v).isChecked()) {
                                    ((CheckBox) childHolder.radioGroup.getChildAt(j)).setChecked(true);
                                    checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex).setValue(((CheckBox) v).getText().toString());

                                    String options = checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex).getIllegalOptions();
                                    String value = checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex).getValue();
                                    List<String> optionlist = new ArrayList<String>();
                                    if(options!=null){
                                        String[] ops = options.split(";");
                                        if(ops!=null&&ops.length!=0){
                                            for(String str:ops){
                                                optionlist.add(str);
                                            }
                                        }
                                    }
                                    if(optionlist.contains(value)){
                                        checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex).setIsShow(Contant.isXCZG);
                                        termBeans.add(checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex));
                                    }else{

                                        if(termBeans.contains(checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex))){
                                            termBeans.remove(checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex));
                                        }
                                    }

                                } else {
                                    ((CheckBox) childHolder.radioGroup.getChildAt(j)).setChecked(false);
                                    checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex).setValue("");
                                    termBeans.remove(checkTermGroupBeans.get(groupIndex).getCheckTermBeans().get(childIndex));
                                }
                            } else {
                                ((CheckBox) childHolder.radioGroup.getChildAt(j)).setChecked(false);
                            }
                        }
                        List<CheckTermBean> checkTermBeans = termBeans;
                        illegalAdapter.notifyDataSetChanged();
                    }
                });

            }
        }



        return convertView;
    }




    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupHolder{
        TextView title;
    }
    class ChildHolder{
        TextView name;
        LinearLayout radioGroup;
        TextView wujiao;
    }
}
