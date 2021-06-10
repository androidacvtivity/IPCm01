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
public class MainActivityListAdapter extends BaseAdapter 
{
	private List<DBDataPrices.ItemClass> listData;
	private LayoutInflater layoutInflater;
	
	//----------------------------------------------------------------------------------------------
	public MainActivityListAdapter(Context context, List<DBDataPrices.ItemClass> listData) 
	{
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}
	
	//----------------------------------------------------------------------------------------------
	//Holder contains all objects of the view item
	class ViewHolder 
	{
		TextView	priceCode;
		TextView	priceName;
		TextView	priceMU;
		TextView	priceDate;
		
		TextView	price_c1;
		TextView	price_c2;
		TextView	price_c3;
		TextView	price_p1;
		TextView	price_p2;
		TextView	price_p3;
		
		ImageView	priceStatus;
		TextView	priceComment;
		TextView	priceCommentAdditional;
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
			convertView = layoutInflater.inflate(R.layout.main_activitylist, null);
			holder = new ViewHolder();
			holder.priceCode 				= (TextView)	convertView.findViewById(R.id.text_cod);
			holder.priceName 				= (TextView)	convertView.findViewById(R.id.text_name);
			holder.priceMU	 				= (TextView)	convertView.findViewById(R.id.text_mu);
			holder.priceDate 				= (TextView)	convertView.findViewById(R.id.text_date);
			
			holder.price_c1 				= (TextView)	convertView.findViewById(R.id.text_price_c1);
			holder.price_c2 				= (TextView)	convertView.findViewById(R.id.text_price_c2);
			holder.price_c3 				= (TextView)	convertView.findViewById(R.id.text_price_c3);
			holder.price_p1 				= (TextView)	convertView.findViewById(R.id.text_price_p1);
			holder.price_p2 				= (TextView)	convertView.findViewById(R.id.text_price_p2);
			holder.price_p3 				= (TextView)	convertView.findViewById(R.id.text_price_p3);
			
			holder.priceStatus				= (ImageView)	convertView.findViewById(R.id.image_product_status);
			holder.priceComment 			= (TextView)	convertView.findViewById(R.id.text_comment);
			holder.priceCommentAdditional	= (TextView)	convertView.findViewById(R.id.text_comment_additional);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//Get item
		DBDataPrices.ItemClass item = listData.get(position);
		if (item == null) return convertView;
		
		//Set holder data values
		holder.priceCode.setText((item.codeAsrt.equals("0") ? item.codeProd : item.codeAsrt));
		holder.priceName.setText(item.name);
		holder.priceMU.setText(" ("+ String.valueOf(item.quantity) + " " + item.mesurementUnitShort + ")");
		holder.priceDate.setText(MainLibrary.DateTimeToString(item.modDate));
		
		holder.price_c1.setText(MainLibrary.DoubleToString(item.price_C1));
		holder.price_c2.setText(MainLibrary.DoubleToString(item.price_C2));
		holder.price_c3.setText(MainLibrary.DoubleToString(item.price_C3));
		holder.price_p1.setText(MainLibrary.DoubleToString(item.price_P1));
		holder.price_p2.setText(MainLibrary.DoubleToString(item.price_P2));
		holder.price_p3.setText(MainLibrary.DoubleToString(item.price_P3));
		
		//Set product comment
		holder.priceComment.setText("");
		if (item.idDataStatus == 0) { holder.priceComment.setText(""); }
		if (item.idDataStatus == 1) { holder.priceComment.setText(R.string.main_comment_status1); holder.priceComment.setTextColor(0xFF00820B); }
		if (item.idDataStatus == 2) { holder.priceComment.setText(R.string.main_comment_status2); holder.priceComment.setTextColor(0xFFC70000); }
		if (item.idDataStatus == 3) { holder.priceComment.setText(R.string.main_comment_status3); holder.priceComment.setTextColor(0xFF0000FF); }
		if (item.idDataStatus == 4) { holder.priceComment.setText(R.string.main_comment_status4); holder.priceComment.setTextColor(0xFF00820B); }
		if (item.idDataStatus == 5) { holder.priceComment.setText(R.string.main_comment_status5); holder.priceComment.setTextColor(0xFFC70000); }
		if (item.idDataStatus == 6) { holder.priceComment.setText(R.string.main_comment_status6); holder.priceComment.setTextColor(0xFFB58B00); }
		
		holder.priceCommentAdditional.setText("");
		if (item.idDataStatus == 3 && item.repeatedCount > 0) { holder.priceCommentAdditional.setText(""+item.repeatedCount+""); }
		
		//Set product status
		holder.priceStatus.setVisibility(View.VISIBLE);
		if (item.isErrored()) holder.priceStatus.setImageResource(R.drawable.ic_error);
		else if (item.isRemained()) holder.priceStatus.setImageResource(R.drawable.ic_info);
		else if (item.isHighVariaty() && item.idDataStatus != 6) holder.priceStatus.setImageResource(R.drawable.ic_warning);
		else holder.priceStatus.setVisibility(View.GONE);
		
		//Set product background
		int bg_decade = 0;
		if (item.codeUnit.equals(DBUserInfo.currentCodeUnit)
				&& item.codeProd.equals(DBUserInfo.currentCodeProd)
				&& item.codeAsrt.equals(DBUserInfo.currentCodeAstr)) 
		{
			convertView.setBackgroundResource(R.drawable.bg_product_list_selected);
			bg_decade = R.drawable.bg_product_list_decade_selected;
		}
		else if (item.codeAsrt.equals("0")) 
		{
			convertView.setBackgroundResource(R.drawable.bg_product_list_item);
			bg_decade = R.drawable.bg_product_list_decade_item;
		}
		else 
		{
			convertView.setBackgroundResource(R.drawable.bg_product_list_subitem);
			bg_decade = R.drawable.bg_product_list_decade_subitem;
		}
		
		//Set decade background
		if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE1 && bg_decade != 0) 
		{
			holder.price_c1.setBackgroundResource(bg_decade);
			holder.price_p1.setBackgroundResource(bg_decade);
		}
		else if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE2 && bg_decade != 0) 
		{
			holder.price_c2.setBackgroundResource(bg_decade);
			holder.price_p2.setBackgroundResource(bg_decade);
		}
		else if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE3 && bg_decade != 0) 
		{
			holder.price_c3.setBackgroundResource(bg_decade);
			holder.price_p3.setBackgroundResource(bg_decade);
		}
		
		//Set decade visibility (modified: 2013.11.06)
		if (item.collectPerMonth <= 0) { if (item.parent.maxCollectPerMonth < 1) holder.price_c2.setVisibility(View.GONE); else holder.price_c2.setVisibility(View.INVISIBLE); } else holder.price_c2.setVisibility(View.VISIBLE);
		if (item.collectPerMonth <= 1) { if (item.parent.maxCollectPerMonth < 2) holder.price_c1.setVisibility(View.GONE); else holder.price_c1.setVisibility(View.INVISIBLE); } else holder.price_c1.setVisibility(View.VISIBLE);
		if (item.collectPerMonth <= 2) { if (item.parent.maxCollectPerMonth < 3) holder.price_c3.setVisibility(View.GONE); else holder.price_c3.setVisibility(View.INVISIBLE); } else holder.price_c3.setVisibility(View.VISIBLE);
		
		if (item.collectPerMonth <= 0) { if (item.parent.maxCollectPerMonth < 1) holder.price_p2.setVisibility(View.GONE); else holder.price_p2.setVisibility(View.INVISIBLE); } else holder.price_p2.setVisibility(View.VISIBLE);
		if (item.collectPerMonth <= 1) { if (item.parent.maxCollectPerMonth < 2) holder.price_p1.setVisibility(View.GONE); else holder.price_p1.setVisibility(View.INVISIBLE); } else holder.price_p1.setVisibility(View.VISIBLE);
		if (item.collectPerMonth <= 2) { if (item.parent.maxCollectPerMonth < 3) holder.price_p3.setVisibility(View.GONE); else holder.price_p3.setVisibility(View.INVISIBLE); } else holder.price_p3.setVisibility(View.VISIBLE);
		
		//Set current Id
		holder.price_c1.setId(R.id.text_price_c1);
		holder.price_c2.setId(R.id.text_price_c2);
		holder.price_c3.setId(R.id.text_price_c3);
		if (item.codeUnit.equals(DBUserInfo.currentCodeUnit)
				&& item.codeProd.equals(DBUserInfo.currentCodeProd)
				&& item.codeAsrt.equals(DBUserInfo.currentCodeAstr)
				&& !item.codeAsrt.equals("0"))
		{
			if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE1) holder.price_c1.setId(MainActivity.id_text_current_prod_price);
			if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE2) holder.price_c2.setId(MainActivity.id_text_current_prod_price);
			if (DBUserInfo.getDateSetDecade() == DBUserInfo.DECADE3) holder.price_c3.setId(MainActivity.id_text_current_prod_price);
		}
		
		return convertView;
	}
	//----------------------------------------------------------------------------------------------
}