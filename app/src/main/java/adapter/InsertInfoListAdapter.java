package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bean.InfoAttributeBean;
import datepicker.cons.DPMode;
import datepicker.views.DatePicker;

/**
 * Created by admin on 2016/5/16.
 */
public class InsertInfoListAdapter extends BaseAdapter {

    private Context context;
    private List<InfoAttributeBean> attributeBeans;
    private int index = -1;
    private List<String> checkStr;
    private AlertDialog alertDialog;

    public InsertInfoListAdapter(Context context, List<InfoAttributeBean> attributeBeans) {
        this.context = context;
        this.attributeBeans = attributeBeans;
        checkStr = new ArrayList<>();

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
    public View getView( final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.insertinfo_item,null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.text_insertitem_name);
            viewHolder.value = (EditText) convertView.findViewById(R.id.edit_insertitem_value);
            viewHolder.spin_value = (Spinner) convertView.findViewById(R.id.spinner_insertitem_value);
            viewHolder.lin_checkbox = (LinearLayout) convertView.findViewById(R.id.lin_insertitem_checkbox);
            viewHolder.date = (TextView) convertView.findViewById(R.id.text_insertitem_date);
            viewHolder.value.setTag(position);

            viewHolder.value.addTextChangedListener(new MyTextWatch(viewHolder));
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.value.setTag(position);
        }

        final String name = attributeBeans.get(position).getName();
        viewHolder.value.setHint("请输入"+name);
        viewHolder.value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((TextView) v).setHint("请输入"+name);
                } else {
                    ((TextView) v).setHint("");
                }
            }
        });

        viewHolder.value.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = (int) v.getTag();
                }
                return false;
            }
        });



        String newName = "";
        if("机构类别".equals(name)||"经纪机构名称".equals(name)||"注册地址".equals(name)||"营业执照号".equals(name)||"联系电话".equals(name)||"项目名称".equals(name)||"项目地址".equals(name)||"联系人".equals(name)){
            newName = name+"(必填项)";
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.red));
        }else if("总建筑面积".equals(name)){
            newName = name+"(平方米)";
        }else{
            newName = name;
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.black));
        }
        viewHolder.name.setText(newName);

        if(name.contains("面积")){
            viewHolder.value.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }else if(name.contains("电话")||name.contains("手机")){
            viewHolder.value.setInputType(InputType.TYPE_CLASS_PHONE);
        }else {
            viewHolder.value.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if("机构类别".equals(name)){
            final int positionNum = position;
            viewHolder.date.setVisibility(View.GONE);
            viewHolder.spin_value.setVisibility(View.VISIBLE);
            viewHolder.value.setVisibility(View.GONE);
            viewHolder.lin_checkbox.setVisibility(View.GONE);
            final String[] strs = new String[]{"总支", "分支"};
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, strs);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.spin_value.setAdapter(arrayAdapter);
            viewHolder.spin_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attributeBeans.get(positionNum).setValue(strs[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else if("项目类型".equals(name)){
            viewHolder.date.setVisibility(View.GONE);
            viewHolder.spin_value.setVisibility(View.GONE);
            viewHolder.value.setVisibility(View.GONE);
            viewHolder.lin_checkbox.setVisibility(View.VISIBLE);

            String[] protype = context.getResources().getStringArray(R.array.protype);
            viewHolder.lin_checkbox.removeAllViews();
            checkStr.clear();
            for(int i=0;i<protype.length;i++){

                final String type = protype[i];
                CheckBox checkBox  = new CheckBox(context);
                checkBox.setText(type);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            checkStr.add(type);
                        }else{
                            checkStr.remove(type);
                        }
                        attributeBeans.get(position).setValue(getType());
                    }
                });
                viewHolder.lin_checkbox.addView(checkBox);
            }
        }else if("投入使用时间".equals(name)){

            viewHolder.spin_value.setVisibility(View.GONE);
            viewHolder.value.setVisibility(View.GONE);
            viewHolder.lin_checkbox.setVisibility(View.GONE);
            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.date.setOnClickListener(new MyClick(viewHolder.date,position));



        }else{
            viewHolder.date.setVisibility(View.GONE);
            viewHolder.spin_value.setVisibility(View.GONE);
            viewHolder.value.setVisibility(View.VISIBLE);
            viewHolder.lin_checkbox.setVisibility(View.GONE);
        }
        viewHolder.value.setText(attributeBeans.get(position).getValue());


//        viewHolder.value.clearFocus();
//        if(index!=-1&&position==index){
//            viewHolder.value.setFocusable(true);
//            viewHolder.value.setFocusableInTouchMode(true);
//            viewHolder.value.requestFocus();
//        }
//        viewHolder.value.setSelection(viewHolder.value.getText().length());
        return convertView;
    }


    private String getType(){
        StringBuilder sb = new StringBuilder();
        if(checkStr!=null&&checkStr.size()!=0){
            for(int i=0;i<checkStr.size();i++){
                if(i==0){
                    sb.append(checkStr.get(i).toString());
                }else{
                    sb.append(","+checkStr.get(i).toString());
                }
            }
        }
        return sb.toString();
    }

    private void initDialog( final TextView textView, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog= builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setContentView(R.layout.date_layout);
        DatePicker datePicker = (DatePicker) alertDialog.findViewById(R.id.datepicker);


        WindowManager wm = ((Activity)context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        datePicker.setLayoutParams(new RelativeLayout.LayoutParams(width * 2 / 3, height / 2));
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        datePicker.setDate(year, month);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                alertDialog.dismiss();
                textView.setText(date);
                attributeBeans.get(position).setValue(date);
            }
        });
    }


    class MyClick implements View.OnClickListener{

        private TextView text;
        private int position;

        public MyClick(TextView text,int position) {
            this.text = text;
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            initDialog(text,position);

        }
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

        TextView date;

        Spinner spin_value;

        LinearLayout lin_checkbox;
    }
}
