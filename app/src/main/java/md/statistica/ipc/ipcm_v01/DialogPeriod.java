package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DialogPeriod extends Activity 
{
	//View variables
	Activity	this_link;
	EditText	edit_year;
	RadioButton	radio_month_01;
	RadioButton	radio_month_02;
	RadioButton	radio_month_03;
	RadioButton	radio_month_04;
	RadioButton	radio_month_05;
	RadioButton	radio_month_06;
	RadioButton	radio_month_07;
	RadioButton	radio_month_08;
	RadioButton	radio_month_09;
	RadioButton	radio_month_10;
	RadioButton	radio_month_11;
	RadioButton	radio_month_12;
	RadioGroup	radioGroup_decade;
	Button		button_cancel;
	Button		button_ok;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_period);
		
		//Initialize views .........................................................................
		this_link			= this;
		edit_year			= (EditText)	findViewById(R.id.edit_dialog_period_year);
		radio_month_01		= (RadioButton)	findViewById(R.id.radio_dialog_period_month01);
		radio_month_02		= (RadioButton)	findViewById(R.id.radio_dialog_period_month02);
		radio_month_03		= (RadioButton)	findViewById(R.id.radio_dialog_period_month03);
		radio_month_04		= (RadioButton)	findViewById(R.id.radio_dialog_period_month04);
		radio_month_05		= (RadioButton)	findViewById(R.id.radio_dialog_period_month05);
		radio_month_06		= (RadioButton)	findViewById(R.id.radio_dialog_period_month06);
		radio_month_07		= (RadioButton)	findViewById(R.id.radio_dialog_period_month07);
		radio_month_08		= (RadioButton)	findViewById(R.id.radio_dialog_period_month08);
		radio_month_09		= (RadioButton)	findViewById(R.id.radio_dialog_period_month09);
		radio_month_10		= (RadioButton)	findViewById(R.id.radio_dialog_period_month10);
		radio_month_11		= (RadioButton)	findViewById(R.id.radio_dialog_period_month11);
		radio_month_12		= (RadioButton)	findViewById(R.id.radio_dialog_period_month12);
		radioGroup_decade	= (RadioGroup)	findViewById(R.id.radioGroup_dialog_period_decade);
		button_cancel		= (Button)		findViewById(R.id.button_dialog_period_cancel);
		button_ok			= (Button)		findViewById(R.id.button_dialog_period_ok);

		radio_month_01.setOnClickListener(MonthClick);
		radio_month_02.setOnClickListener(MonthClick);
		radio_month_03.setOnClickListener(MonthClick);
		radio_month_04.setOnClickListener(MonthClick);
		radio_month_05.setOnClickListener(MonthClick);
		radio_month_06.setOnClickListener(MonthClick);
		radio_month_07.setOnClickListener(MonthClick);
		radio_month_08.setOnClickListener(MonthClick);
		radio_month_09.setOnClickListener(MonthClick);
		radio_month_10.setOnClickListener(MonthClick);
		radio_month_11.setOnClickListener(MonthClick);
		radio_month_12.setOnClickListener(MonthClick);
		button_cancel.setOnClickListener(CancelClick);
		button_ok.setOnClickListener(OkClick);
		
		edit_year.setOnKeyListener(onEnterSubmitView);
		
		setDataOnScreen(DBUserInfo.getDateSetYear(), DBUserInfo.getDateSetMonth(), DBUserInfo.getDateSetDecade());
	}

	//----------------------------------------------------------------------------------------------
	private void setDataOnScreen(int year, int month, int decade)
	{
		//Set decade
		RadioButton radioBtn = (RadioButton)findViewById(R.id.radio_dialog_period_decade1);
		switch(decade)
		{
			case  DBUserInfo.DECADE1: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_period_decade1); break;
			case  DBUserInfo.DECADE2: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_period_decade2); break;
			case  DBUserInfo.DECADE3: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_period_decade3); break;
		}
		radioGroup_decade.check(radioBtn.getId());
		
		//Set month
		radio_month_01.setChecked(month ==  1);
		radio_month_02.setChecked(month ==  2);
		radio_month_03.setChecked(month ==  3);
		radio_month_04.setChecked(month ==  4);
		radio_month_05.setChecked(month ==  5);
		radio_month_06.setChecked(month ==  6);
		radio_month_07.setChecked(month ==  7);
		radio_month_08.setChecked(month ==  8);
		radio_month_09.setChecked(month ==  9);
		radio_month_10.setChecked(month == 10);
		radio_month_11.setChecked(month == 11);
		radio_month_12.setChecked(month == 12);
		
		//Focus next month radio button
		RadioButton radioBtnNext = radio_month_01;
		switch(month)
		{
			case  1: radioBtnNext = radio_month_02; break;
			case  2: radioBtnNext = radio_month_03; break;
			case  3: radioBtnNext = radio_month_04; break;
			case  4: radioBtnNext = radio_month_05; break;
			case  5: radioBtnNext = radio_month_06; break;
			case  6: radioBtnNext = radio_month_07; break;
			case  7: radioBtnNext = radio_month_08; break;
			case  8: radioBtnNext = radio_month_09; break;
			case  9: radioBtnNext = radio_month_10; break;
			case 10: radioBtnNext = radio_month_11; break;
			case 11: radioBtnNext = radio_month_12; break;
			case 12: radioBtnNext = radio_month_12; break;
		}
		radioBtnNext.setFocusableInTouchMode(true);
		radioBtnNext.requestFocus();
		radioBtnNext.setFocusableInTouchMode(false);
		
		//Set year
		edit_year.setText(String.valueOf(year));
		edit_year.requestFocus();
		edit_year.setSelection(edit_year.length());
	}

	//----------------------------------------------------------------------------------------------
	//Virtual keyboard key pressed
	protected View.OnKeyListener onEnterSubmitView = new View.OnKeyListener() 
	{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
			{
				okClickProcedure();
                return true;
			}
			return false;
		}
	};

	//----------------------------------------------------------------------------------------------
	//Month OnClick Listener
	OnClickListener MonthClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			//Check one month only
			if (v != radio_month_01) radio_month_01.setChecked(false);
			if (v != radio_month_02) radio_month_02.setChecked(false);
			if (v != radio_month_03) radio_month_03.setChecked(false);
			if (v != radio_month_04) radio_month_04.setChecked(false);
			if (v != radio_month_05) radio_month_05.setChecked(false);
			if (v != radio_month_06) radio_month_06.setChecked(false);
			if (v != radio_month_07) radio_month_07.setChecked(false);
			if (v != radio_month_08) radio_month_08.setChecked(false);
			if (v != radio_month_09) radio_month_09.setChecked(false);
			if (v != radio_month_10) radio_month_10.setChecked(false);
			if (v != radio_month_11) radio_month_11.setChecked(false);
			if (v != radio_month_12) radio_month_12.setChecked(false);
		}
	};

	//----------------------------------------------------------------------------------------------
	//Cancel OnClick Listener
	OnClickListener CancelClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
            //Close the activity
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
	private void okClickProcedure()
	{
		//Initialize date variables
		int year = DBUserInfo.getDateSetYear();
		int month = DBUserInfo.getDateSetMonth();
		int decade = DBUserInfo.getDateSetDecade();
		
		//Set year
		if (!edit_year.getText().toString().equals("")) year = Integer.valueOf(edit_year.getText().toString());
		
		//Set month
		if      (radio_month_01.isChecked()) month =  1;
		else if (radio_month_02.isChecked()) month =  2;
		else if (radio_month_03.isChecked()) month =  3;
		else if (radio_month_04.isChecked()) month =  4;
		else if (radio_month_05.isChecked()) month =  5;
		else if (radio_month_06.isChecked()) month =  6;
		else if (radio_month_07.isChecked()) month =  7;
		else if (radio_month_08.isChecked()) month =  8;
		else if (radio_month_09.isChecked()) month =  9;
		else if (radio_month_10.isChecked()) month = 10;
		else if (radio_month_11.isChecked()) month = 11;
		else if (radio_month_12.isChecked()) month = 12;
		
		//Set decade
		switch(radioGroup_decade.getCheckedRadioButtonId())
		{
			case R.id.radio_dialog_period_decade1: decade = DBUserInfo.DECADE1; break;
			case R.id.radio_dialog_period_decade2: decade = DBUserInfo.DECADE2; break;
			case R.id.radio_dialog_period_decade3: decade = DBUserInfo.DECADE3; break;
		}
		
        //Set activity result value
        Intent intent = new Intent();
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("decade", decade);
        setResult(RESULT_OK, intent);
        
        //Close the activity
		finish();
	}
	//----------------------------------------------------------------------------------------------
}
