package md.statistica.ipc.ipcm_v01;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//Class - for implementation of the data loading into the list view
public class DialogAsrtCountryAdapter extends BaseAdapter 
{
	private List<DBClsCounties.ItemClass> listData;
	private LayoutInflater layoutInflater;
	
	//----------------------------------------------------------------------------------------------
	public DialogAsrtCountryAdapter(Context context, List<DBClsCounties.ItemClass> listData) 
	{
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}
	
	//----------------------------------------------------------------------------------------------
	//Holder contains all objects of the view item
	class ViewHolder 
	{
		TextView itemCode;
		TextView itemName;
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	public int getCount() 
	{
		return listData.size();
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	public Object getItem(int position) 
	{
		return listData.get(position);
	}
	
	//----------------------------------------------------------------------------------------------
	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	//----------------------------------------------------------------------------------------------
	//Implements loading of the information into the view
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		//Create holder
		ViewHolder holder;
		if (convertView == null) 
		{
			convertView = layoutInflater.inflate(R.layout.string_item, null);
			holder = new ViewHolder();
			holder.itemCode = (TextView) convertView.findViewById(R.id.text_string_item_code);
			holder.itemName = (TextView) convertView.findViewById(R.id.text_string_item_name);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//Fill in holder
		holder.itemCode.setText(listData.get(position).code);
		holder.itemName.setText(listData.get(position).name);
		
		return convertView;
	}
	//----------------------------------------------------------------------------------------------
}