package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.PersonBean;

/**
 * Created by admin on 2016/5/18.
 */
public class PersonAdapter extends BaseAdapter {

    private Context context;
    private List<PersonBean> personBeans;
    private boolean isEdit;

    public PersonAdapter(Context context, List<PersonBean> personBeans,boolean isEdit) {
        this.context = context;
        this.personBeans = personBeans;
        this.isEdit = isEdit;
    }

    @Override
    public int getCount() {
        return personBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return personBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.person_item,null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.text_person_name);
            viewHolder.number = (TextView) convertView.findViewById(R.id.text_person_number);
            viewHolder.img_delete = (ImageButton) convertView.findViewById(R.id.imgbt_personitem_delete);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(personBeans.get(position).getName());
        if(personBeans.get(position).getCertNo()!=null&&!personBeans.get(position).getCertNo().equals("")){
            viewHolder.number.setText("执法证号 : "+personBeans.get(position).getCertNo());
        }else{
            viewHolder.number.setText("");
        }

        final int finalPosition = position;
        if(isEdit){
            viewHolder.img_delete.setVisibility(View.VISIBLE);
        }else{
            viewHolder.img_delete.setVisibility(View.GONE);
        }
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personBeans.remove(finalPosition);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView name;
        TextView number;
        ImageButton img_delete;
    }

}
