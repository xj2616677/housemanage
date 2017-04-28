package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.PrimeBrokerBean;
import constants.Contant;

/**
 * Created by admin on 2016/4/29.
 */
public class PrimeObjectAdapter extends BaseAdapter {

    private Context context;
    private List<PrimeBrokerBean> primeObjectBeans;

    public PrimeObjectAdapter(Context context, List<PrimeBrokerBean> primeObjectBeans) {
        this.context = context;
        this.primeObjectBeans = primeObjectBeans;
    }

    @Override
    public int getCount() {
        return primeObjectBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return primeObjectBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.object_list_item,null);
            holder.nameText = (TextView) convertView.findViewById(R.id.text_objectitem_name);
            holder.deleteText = (TextView) convertView.findViewById(R.id.text_objectitem_delete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameText.setText(primeObjectBeans.get(position).getName());
        holder.deleteText.setOnClickListener(new deleteClickListener(position));

        return convertView;
    }

    class deleteClickListener implements View.OnClickListener{

        private int position;

        public deleteClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Contant.primeObjectList.remove(primeObjectBeans.get(position));
            notifyDataSetChanged();
        }
    }
    class ViewHolder{
        TextView nameText;
        TextView deleteText;
    }
}
