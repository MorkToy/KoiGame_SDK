package koigame.sdk.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import koigame.sdk.util.RUtils;


public class OptionsAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Activity activity = null;
	private Handler handler;
	
	/**
	 * 自定义构造方法.
	 */
	public OptionsAdapter(Activity activity,Handler handler,List<Map<String, Object>> listItems){
		this.activity = activity;
		this.handler = handler;
		this.list = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView ==null){
			holder = new ViewHolder();
			//下拉项布局
			convertView = LayoutInflater.from(activity).inflate(RUtils.getLayoutId("option_item"), null);
            holder.textView = (TextView)convertView.findViewById(RUtils.getLayoutId("item_text"));
            holder.delImage = (ImageView) convertView.findViewById(RUtils.getLayoutId("delImage"));
            convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.textView.setText((String) list.get(position)   
                .get("number"));
		
		//为下拉框选项文字部分设置事件，最终效果是点击将其文字填充到文本框
		holder.textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				//设置选中索引
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 5;
				//发出消息
				handler.sendMessage(msg);
			}
		});
		//为下拉框选项删除图标部分设置事件，最终效果还是点击将该项删除
		holder.delImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				//设置删除索引
				data.putInt("delIndex", position);
				msg.setData(data);
				msg.what = 6;
				//发出消息
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

}

class ViewHolder {
	TextView textView;     
	ImageView delImage;
	ImageView headImage;
}