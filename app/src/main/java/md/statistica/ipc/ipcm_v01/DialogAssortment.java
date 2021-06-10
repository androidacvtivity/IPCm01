package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogAssortment extends Activity
{
	//View variables
	Activity	this_link;
	TextView	text_title;
	TextView	text_regunit;
	TextView	text_product;
	EditText	edit_regunitCode;
	EditText	edit_productCode;
	EditText	edit_assortCode;
	EditText	edit_name;
	EditText	edit_quantity;
	EditText	edit_quantity_um;
	EditText	edit_countryCode;
	EditText	edit_country;
	EditText	edit_company;
	EditText	edit_brand;
	Button		button_product;
	Button		button_country;
	Button		button_cancel;
	Button		button_ok;
	
	//Data variables
	boolean			isUpdateMode;
	
	DBRegUnits		regUnits;
	DBClsProducts	clsProducts;
	DBClsCounties	clsCounties;
	
	DBRegUnits.ItemClass	regUnit;
	DBClsProducts.ItemClass	clsProduct;
	DBClsCounties.ItemClass	clsCounty;
	
	//Dialog activities identifiers
	public static final int ACTIVITY_RESULT_ASRT_PRODUCT = 1;
	public static final int ACTIVITY_RESULT_ASRT_COUNTRY = 2;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_assortment);
		
		//Initialize views .........................................................................
		this_link		= this;
		text_title		= (TextView)	findViewById(R.id.text_dialog_assortment_title);
		text_regunit	= (TextView)	findViewById(R.id.text_dialog_assortment_regunit);
		text_product	= (TextView)	findViewById(R.id.text_dialog_assortment_product);
		edit_regunitCode= (EditText)	findViewById(R.id.edit_dialog_assortment_regunit_code);
		edit_productCode= (EditText)	findViewById(R.id.edit_dialog_assortment_product_code);
		edit_assortCode	= (EditText)	findViewById(R.id.edit_dialog_assortment_assort_code);
		edit_name		= (EditText)	findViewById(R.id.edit_dialog_assortment_name);
		edit_quantity	= (EditText)	findViewById(R.id.edit_dialog_assortment_quantity);
		edit_quantity_um= (EditText)	findViewById(R.id.edit_dialog_assortment_quantity_um);
		edit_countryCode= (EditText)	findViewById(R.id.edit_dialog_assortment_country_code);
		edit_country	= (EditText)	findViewById(R.id.edit_dialog_assortment_country);
		edit_company	= (EditText)	findViewById(R.id.edit_dialog_assortment_company);
		edit_brand		= (EditText)	findViewById(R.id.edit_dialog_assortment_brand);
		button_product	= (Button)		findViewById(R.id.button_dialog_assortment_product_choose);
		button_country	= (Button)		findViewById(R.id.button_dialog_assortment_country_choose);
		button_cancel	= (Button)		findViewById(R.id.button_dialog_assortment_cancel);
		button_ok		= (Button)		findViewById(R.id.button_dialog_assortment_ok);

		text_title.setText(getResources().getString(R.string.dialog_assortment_title_add));
		edit_assortCode.requestFocus();
		
		edit_productCode.addTextChangedListener(ProductCodeChanged);
		edit_countryCode.addTextChangedListener(CountryCodeChanged);
		
		edit_country.setKeyListener(null);
		edit_regunitCode.setKeyListener(null);
		edit_quantity_um.setKeyListener(null);
		button_product.setOnClickListener(ProductClick);
		button_country.setOnClickListener(CountryClick);
		button_cancel.setOnClickListener(CloseClick);
		button_ok.setOnClickListener(OkClick); 
		
		//Data variables ...........................................................................
		isUpdateMode = false;
		
		regUnits = new DBRegUnits();
		clsProducts = new DBClsProducts();
		clsCounties = new DBClsCounties();
		
		clsProducts.loadData(DBClsProducts.SORT_BY_CODE);
		clsCounties.loadData(DBClsCounties.SORT_BY_CODE);
		
		regUnit = null;
		clsProduct = null;
		clsCounty = null;
		
		Intent intent = getIntent();
		setRegUnit(intent.getStringExtra("codeUnit"));
		setProduct(intent.getStringExtra("codeProd"));
		setAssortmen(intent.getStringExtra("codeAsrt"));
	}

	//----------------------------------------------------------------------------------------------
	//Set statistical unit values
	private void setRegUnit(String codeUnitSet)
	{
		if (regUnits == null) return;
		
		regUnits.loadData(DBRegUnits.FILTER_ALL, DBRegUnits.SORT_BY_CODE, codeUnitSet);
		regUnit = regUnits.getItem(codeUnitSet);
		
		if (regUnit == null) 
		{
			edit_regunitCode.setText("");
			text_regunit.setText(R.string.dialog_assortment_label_regunit_default);
		}
		else 
		{
			edit_regunitCode.setText(regUnit.code);
			text_regunit.setText(regUnit.name);
		}
	}

	//----------------------------------------------------------------------------------------------
	//Set product values
	private void setProduct(String codeProdSet)
	{
		//The rest of operations is done by listener
		edit_productCode.setText(codeProdSet);
	}

	//----------------------------------------------------------------------------------------------
	//Product code change Listener
	TextWatcher ProductCodeChanged = new TextWatcher()
	{
		public void afterTextChanged(Editable s) 
		{
			if (clsProducts == null) return;
			clsProduct = clsProducts.getItem(edit_productCode.getText().toString());
			
			if (clsProduct == null) 
			{
				text_product.setText(R.string.dialog_assortment_label_product_default);
				edit_quantity.setText("");
				edit_quantity_um.setText("");
			}
			else 
			{
				text_product.setText(clsProduct.getNameFull());
				edit_quantity.setText(String.valueOf(clsProduct.quantity));
				edit_quantity_um.setText(clsProduct.mesurementUnit);
			}
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		public void onTextChanged(CharSequence s, int start, int before, int count){}
	};

	//----------------------------------------------------------------------------------------------
	//Country code change Listener
	TextWatcher CountryCodeChanged = new TextWatcher()
	{
		public void afterTextChanged(Editable s) 
		{
			if (clsCounties == null) return;
			clsCounty = clsCounties.getItem(edit_countryCode.getText().toString());
			
			if (clsCounty == null) edit_country.setText("");
			else edit_country.setText(clsCounty.name);
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		public void onTextChanged(CharSequence s, int start, int before, int count){}
	};

	//----------------------------------------------------------------------------------------------
	//Set assortment values
	private void setAssortmen(String codeAsrtSet)
	{
		String codeUnit = edit_regunitCode.getText().toString();
		String codeProd = edit_productCode.getText().toString();
		String codeAsrt = codeAsrtSet;
		
		if (!codeAsrt.equals("0"))
		{
			isUpdateMode = true;
			edit_assortCode.setKeyListener(null);
			edit_productCode.setKeyListener(null);
			button_product.setVisibility(View.GONE);
			text_title.setText(getResources().getString(R.string.dialog_assortment_title_mod));
			
			DBDataPrices dataPrices = new DBDataPrices();
			dataPrices.loadData(DBDataPrices.FILTER_ALL, DBDataPrices.SORT_BY_CODE, DBUserInfo.setDate, codeUnit, codeProd, codeAsrt);
			DBDataPrices.ItemClass item = dataPrices.getItem(codeUnit, codeProd, codeAsrt);
			
			if (item == null)
			{
				edit_assortCode.setText("");
				edit_name.setText("");
				edit_quantity.setText("");
				edit_countryCode.setText("");
				edit_company.setText("");
				edit_brand.setText("");
			}
			else
			{
				edit_assortCode.setText(item.codeAsrt);
				edit_name.setText(item.name);
				edit_quantity.setText(String.valueOf(item.quantity));
				edit_countryCode.setText(item.asrtCountry);
				edit_company.setText(item.asrtCompany);
				edit_brand.setText(item.asrtBrand);
				
				edit_name.requestFocus();
				edit_name.setSelection(edit_name.length());
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//On return from the activity for result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ACTIVITY_RESULT_ASRT_PRODUCT && resultCode == RESULT_OK) prodtActivityResult(data);
		if (requestCode == ACTIVITY_RESULT_ASRT_COUNTRY && resultCode == RESULT_OK) countryActivityResult(data);
	}
	
	//----------------------------------------------------------------------------------------------
	//Product OnClick Listener
	OnClickListener ProductClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			Intent intent = new Intent(this_link, DialogAsrtProduct.class);
			intent.putExtra("code",  edit_productCode.getText().toString());
			startActivityForResult(intent, ACTIVITY_RESULT_ASRT_PRODUCT);
		}
	};
	
	//----------------------------------------------------------------------------------------------
	//Country OnClick Listener
	OnClickListener CountryClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			Intent intent = new Intent(this_link, DialogAsrtCountry.class);
			intent.putExtra("code",  edit_countryCode.getText().toString());
			startActivityForResult(intent, ACTIVITY_RESULT_ASRT_COUNTRY);
		}
	};
	
	//----------------------------------------------------------------------------------------------
	//Comment price activity result
	private void prodtActivityResult(Intent data)
	{
		if (data == null) return;
		edit_productCode.setText(data.getStringExtra("code"));
	}
	
	//----------------------------------------------------------------------------------------------
	//Comment price activity result
	private void countryActivityResult(Intent data)
	{
		if (data == null) return;
		edit_countryCode.setText(data.getStringExtra("code"));
	}
	
	//----------------------------------------------------------------------------------------------
	//Close OnClick Listener
	OnClickListener CloseClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			finish();
		}
	};

	//----------------------------------------------------------------------------------------------
	//OK OnClick Listener
	OnClickListener OkClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			okClickProcedure();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Save procedure
	private void okClickProcedure()
	{
		int errorMessage = 0;

		//Validate data
		if (edit_regunitCode.getText().toString().equals(""))		{ errorMessage = R.string.dialog_assortment_error_regunit_empty; edit_regunitCode.requestFocus(); }
		else if (regUnit == null) 									{ errorMessage = R.string.dialog_assortment_error_regunit_bad; edit_regunitCode.requestFocus(); }
		else if (edit_productCode.getText().toString().equals(""))	{ errorMessage = R.string.dialog_assortment_error_product_empty; edit_productCode.requestFocus(); }
		else if (clsProduct == null)								{ errorMessage = R.string.dialog_assortment_error_product_bad; edit_productCode.requestFocus(); }
		else if (edit_assortCode.getText().toString().equals(""))	{ errorMessage = R.string.dialog_assortment_error_assort_empty; edit_assortCode.requestFocus(); }
		else if (edit_name.getText().toString().equals(""))			{ errorMessage = R.string.dialog_assortment_error_name_empty; edit_name.requestFocus(); }
		else if (assortExists())									{ errorMessage = R.string.dialog_assortment_error_assort_exists; edit_assortCode.requestFocus(); }
		else if (edit_quantity.getText().toString().equals(""))		{ errorMessage = R.string.dialog_assortment_error_quantity_empty; edit_quantity.requestFocus(); }
		else if (edit_country.getText().toString().equals("") &&
				!edit_countryCode.getText().toString().equals(""))	{ errorMessage = R.string.dialog_assortment_error_country_bad; edit_countryCode.requestFocus(); }
		
		//Show error message (if there is any)
		if (errorMessage != 0)
		{
			new AlertDialog.Builder(this_link).setTitle(R.string.dialog_error).setMessage(errorMessage)
	        .setPositiveButton(R.string.dialog_ok, null).create().show();
			return;
		}
		
		//Save data
		boolean resultOk = true;
		if (isUpdateMode) resultOk = updateData(); else resultOk = insertData();
		
		//Show result
		if (!resultOk)
		{
			new AlertDialog.Builder(this_link).setTitle(R.string.dialog_error).setMessage(R.string.dialog_assortment_error_save_bad)
	        .setPositiveButton(R.string.dialog_ok, null).create().show();
		}
		else
		{
			Intent intent = new Intent();
	        setResult(RESULT_OK, intent);
			
			if(isUpdateMode) finish();
			else
			{
				new AlertDialog.Builder(this_link).setTitle(R.string.dialog_info).setMessage(R.string.dialog_assortment_error_save_ok)
		        .setPositiveButton(R.string.dialog_ok, null).create().show();
				
				edit_assortCode.setText("");
				edit_name.setText("");
				edit_countryCode.setText("");
				edit_company.setText("");
				edit_brand.setText("");
				edit_assortCode.requestFocus();
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//Save procedure
	private boolean assortExists()
	{
		if (isUpdateMode) return false;
		if (regUnit == null) return false;
		if (clsProduct == null) return false;
		
		String codeUnit = edit_regunitCode.getText().toString();
		String codeProd = edit_productCode.getText().toString();
		String codeAsrt = edit_assortCode.getText().toString();
		
		DBDataPrices dataPrices = new DBDataPrices();
		dataPrices.loadData(DBDataPrices.FILTER_ALL, DBDataPrices.SORT_BY_CODE, DBUserInfo.setDate, codeUnit, codeProd, codeAsrt);
		DBDataPrices.ItemClass item = dataPrices.getItem(codeUnit, codeProd, codeAsrt);
		
		if (item != null) return true;
		else return false;
	}

	//----------------------------------------------------------------------------------------------
	//Insert procedure
	private boolean insertData()
	{
		if (isUpdateMode) return false;
		if (regUnit == null) return false;
		if (clsProduct == null) return false;
		
		//Parent product item
		boolean parentExists = true;
		DBDataPrices dataPrices = new DBDataPrices();
		dataPrices.loadData(DBDataPrices.FILTER_ALL, DBDataPrices.SORT_BY_CODE, DBUserInfo.setDate, edit_regunitCode.getText().toString(), edit_productCode.getText().toString(), "0");
		DBDataPrices.ItemClass itemProduct = dataPrices.getItem(edit_regunitCode.getText().toString(), edit_productCode.getText().toString(), "0");
		if (itemProduct == null)
		{
			parentExists = false;
			itemProduct = new DBDataPrices.ItemClass();
			itemProduct.setDate = DBUserInfo.setDate;
			itemProduct.cuatm = DBUserInfo.cuatm;
			itemProduct.codeUnit = edit_regunitCode.getText().toString();
			itemProduct.codeProd = edit_productCode.getText().toString();
			itemProduct.codeAsrt = "0";
			itemProduct.name = clsProduct.name;
			itemProduct.quantity = clsProduct.quantity;
			itemProduct.modDate = MainLibrary.CurrentDate();
			parentExists = dataPrices.insertItem(itemProduct);
		}
		
		//Create new object for insert
		if (parentExists)
		{
			DBDataPrices.ItemClass item = new DBDataPrices.ItemClass();
			item.setDate = DBUserInfo.setDate;
			item.cuatm = DBUserInfo.cuatm;
			item.codeUnit = edit_regunitCode.getText().toString();
			item.codeProd = edit_productCode.getText().toString();
			item.codeAsrt = edit_assortCode.getText().toString();
			item.name = edit_name.getText().toString();
			item.quantity = Double.valueOf(edit_quantity.getText().toString());
			item.asrtCountry = edit_countryCode.getText().toString();
			item.asrtCompany = edit_company.getText().toString();
			item.asrtBrand = edit_brand.getText().toString();
			itemProduct.modDate = MainLibrary.CurrentDate();
			return dataPrices.insertItem(item);
		}
		
		return false;
	}

	//----------------------------------------------------------------------------------------------
	//Update procedure
	private boolean updateData()
	{
		if (!isUpdateMode) return false;
		if (regUnit == null) return false;
		if (clsProduct == null) return false;
		
		//Create new object for update
		DBDataPrices dataPrices = new DBDataPrices();
		DBDataPrices.ItemClass item = new DBDataPrices.ItemClass();
		item.setDate = DBUserInfo.setDate;
		item.cuatm = DBUserInfo.cuatm;
		item.codeUnit = edit_regunitCode.getText().toString();
		item.codeProd = edit_productCode.getText().toString();
		item.codeAsrt = edit_assortCode.getText().toString();
		item.name = edit_name.getText().toString();
		item.quantity = Double.valueOf(edit_quantity.getText().toString());
		item.asrtCountry = edit_countryCode.getText().toString();
		item.asrtCompany = edit_company.getText().toString();
		item.asrtBrand = edit_brand.getText().toString();
		return dataPrices.updateItem(item);
	}
	//----------------------------------------------------------------------------------------------
}
