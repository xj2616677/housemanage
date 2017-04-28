package widget;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.housemanage.R;

/**   
*    
* 项目名称：cehuimap   
* 类名称：LoadingLoadingDialog   
* 类描述：  加载进度条对话框  
* 创建人：cys   
* 创建时间：2015-2-5 下午4:45:40   
* 修改人：cys   
* 修改时间：2015-2-5 下午4:45:40   
* 修改备注：   
* @version    
*    
*/
public class LoadingLoadingDialog extends Dialog {
	private TextView tv;

	public LoadingLoadingDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar_upload);
		tv = (TextView)findViewById(R.id.tv);
		tv.setText("正在加载.....");
		  LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);  
	      linearLayout.getBackground().setAlpha(210);  
	}

}

