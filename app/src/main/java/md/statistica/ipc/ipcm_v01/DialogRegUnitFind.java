package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DialogRegUnitFind extends Activity 
{
	//View variables
	Activity	this_link;
	EditText	edit_code;
	EditText	edit_name;
	Button		button_cancel;
	Button		button_ok;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_regunitfind);
		
		//Initialize views .........................................................................
		this_link		= this;
		edit_code		= (EditText)	findViewById(R.id.edit_dialog_regunit_find_code);
		edit_name		= (EditText)	findViewById(R.id.edit_dialog_regunit_find_name);
		button_cancel	= (Button)		findViewById(R.id.button_dialog_regunit_find_cancel);
		button_ok		= (Button)		findViewById(R.id.button_dialog_regunit_find_ok);
		
		button_cancel.setOnClickListener(CancelClick);
		button_ok.setOnClickListener(OkClick);
		
		edit_code.setOnKeyListener(onEnterSubmitView);
		edit_name.setOnKeyListener(onEnterSubmitView);
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
		//Set activity result value
        Intent intent = new Intent();
        intent.putExtra("code", edit_code.getText().toString());
        intent.putExtra("name", edit_name.getText().toString());
        setResult(RESULT_OK, intent);
        
        //Close the activity
		finish();
	}
	//----------------------------------------------------------------------------------------------
}
