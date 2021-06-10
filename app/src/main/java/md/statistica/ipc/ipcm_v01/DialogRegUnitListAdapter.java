package md.statistica.ipc.ipcm_v01;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Class - for implementation of the data loading into the list view
public class DialogRegUnitListAdapter extends BaseAdapter 
{
	private List<DBRegUnits.ItemClass> listData;
	private LayoutInflater layoutInflater;
	
	//----------------------------------------------------------------------------------------------
	public DialogRegUnitListAdapter(Context context, List<DBRegUnits.ItemClass> listData) 
	{
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}
	
	//----------------------------------------------------------------------------------------------
	//Holder contains all objects of the view item
	class ViewHolder 
	{
		TextView	unitCode;
		TextView	unitName;
		TextView	unitInfo;
		TextView	unitStatus_text;
		ImageView	unitStatus_img;
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
			convertView = layoutInflater.inflate(R.layout.dialog_regunitlist, null);
			holder = new ViewHolder();
			holder.unitCode			= (TextView)	convertView.findViewById(R.id.text_dialog_regunit_code);
			holder.unitName			= (TextView)	convertView.findViewById(R.id.text_dialog_regunit_name);
			holder.unitInfo			= (TextView)	convertView.findViewById(R.id.text_dialog_regunit_info);
			holder.unitStatus_text	= (TextView)	convertView.findViewById(R.id.text_dialog_regunit_status);
			holder.unitStatus_img	= (ImageView)	convertView.findViewById(R.id.image_dialog_regunit_status);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//Get item
		DBRegUnits.ItemClass item = listData.get(position);
		if (item == null) return convertView;
		
		//Set holder values
		holder.unitCode.setText(item.code);
		holder.unitName.setText(item.name +" ("+ item.prodCountNonZero + "/" +item.prodCount +")");
		holder.unitInfo.setText(item.getContactInfo());

		//Set unit status
		holder.unitStatus_text.setText("");
		holder.unitStatus_img.setVisibility(View.VISIBLE);
		if (item.prodCountErrored > 0) { holder.unitStatus_img.setImageResource(R.drawable.ic_error); holder.unitStatus_text.setText(""+item.prodCountErrored+""); }
		else if (item.prodCountRemained > 0) { holder.unitStatus_img.setImageResource(R.drawable.ic_info); holder.unitStatus_text.setText(""+item.prodCountRemained+""); }
		else holder.unitStatus_img.setVisibility(View.GONE);

		//Set background
		if (holder.unitCode.getText().equals(DBUserInfo.foundCodeUnit) && !holder.unitCode.getText().equals(DBUserInfo.currentCodeUnit)) convertView.setBackgroundResource(R.drawable.bg_regunit_list_found);
		else if (holder.unitCode.getText().equals(DBUserInfo.foundCodeUnit) && holder.unitCode.getText().equals(DBUserInfo.currentCodeUnit)) convertView.setBackgroundResource(R.drawable.bg_regunit_list_foundselected);
		else if (holder.unitCode.getText().equals(DBUserInfo.currentCodeUnit)) convertView.setBackgroundResource(R.drawable.bg_regunit_list_selected);
		else convertView.setBackgroundResource(android.R.drawable.list_selector_background);
		
		return convertView;
	}
	//----------------------------------------------------------------------------------------------
}