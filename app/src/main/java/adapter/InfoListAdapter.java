package adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.InfoAttributeBean;

/**
 * Created by admin on 2016/5/16.
 */
public class InfoListAdapter extends BaseAdapter {

    private Context context;
    private List<InfoAttributeBean> attributeBeans;
    private int index = -1;
    private List<String> bussinessType;
    private String objectName;
    private String brokerID;

    public InfoListAdapter(Context context, List<InfoAttributeBean> attributeBeans,List<String> bussinessType,String objectName,String brokerId) {
        this.context = context;
        this.attributeBeans = attributeBeans;
        this.bussinessType = bussinessType;
        this.objectName = objectName;
        this.brokerID = brokerId;

    }

    public InfoListAdapter(Context context, List<InfoAttributeBean> attributeBeans,int index) {
        this.context = context;
        this.attributeBeans = attributeBeans;
        objectName = "";
        brokerID = "";
        this.index = index;
    }

    @Override
    public int getCount() {
        return attributeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return attributeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.brokerinfo_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.text_infoitem_name);
            viewHolder.value = (EditText) convertView.findViewById(R.id.edit_infoitem_value);
            viewHolder.value.setTag(position);
            viewHolder.value.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = (int) v.getTag();
                    }
                    return false;
                }
            });
            viewHolder.value.addTextChangedListener(new MyTextWatch(viewHolder));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.value.setTag(position);
        }

        if (position == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.listoddback));
        } else {
            if (position % 2 == 0) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.listoddback));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
        viewHolder.name.setText(attributeBeans.get(position).getName());
        viewHolder.value.setText(attributeBeans.get(position).getValue());
        String name = attributeBeans.get(position).getName();
        if (name != null && (name.contains("电话") || name.contains("手机"))) {
            viewHolder.value.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {
            viewHolder.value.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if (index == 1) {

            viewHolder.value.setEnabled(false);

        } else {
            if (bussinessType != null && bussinessType.contains(context.getResources().getString(R.string.primebroker))) {
                if (("经理".equals(name) || "经理手机".equals(name) || "经理固定电话".equals(name) || "联系电话".equals(name)) && attributeBeans.get(position).isEdit()) {
                    viewHolder.name.setText(attributeBeans.get(position).getName() + "(可编辑)");
                    viewHolder.name.setTextColor(context.getResources().getColor(R.color.red));
                    viewHolder.value.setEnabled(true);
                } else {
                    viewHolder.value.setEnabled(false);
                    viewHolder.name.setTextColor(context.getResources().getColor(R.color.black));
                }
            } else {
                if (("其他".equals(objectName) || "-1".equals(brokerID)) && attributeBeans.get(position).isEdit()) {
                    viewHolder.value.setEnabled(true);
                    if (position == 1 || position == 0) {
                        viewHolder.value.setHint("(必填)");
                    }
                } else {
                    if (("产权方".equals(name) || "项目负责人".equals(name) || "项目负责人电话".equals(name) || "联系人".equals(name) || "联系电话".equals(name)) && attributeBeans.get(position).isEdit()) {
                        viewHolder.name.setText(attributeBeans.get(position).getName() + "(可编辑)");
                        viewHolder.name.setTextColor(context.getResources().getColor(R.color.red));
                        viewHolder.value.setEnabled(true);
                    } else {
                        viewHolder.value.setEnabled(false);
                        viewHolder.name.setTextColor(context.getResources().getColor(R.color.black));
                    }
                }

            }
        }

        return convertView;
    }

    class MyTextWatch implements TextWatcher{

        private ViewHolder viewHolder;

        public MyTextWatch( ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            int position = (int) viewHolder.value.getTag();
            String value = viewHolder.value.getText().toString();
//            Log.i("TAG","value"+value);
            attributeBeans.get(position).setValue(value);

        }
    }


    class ViewHolder{
        TextView name;
        //        TextView value;
        EditText value;
    }
}
