package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DialogComment extends Activity
{
	//View variables
	Activity	this_link;
	EditText	edit_comment;
	RadioGroup	radioGroup_status;
	Button		button_cancel;
	Button		button_ok;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_comment);
		
		//Initialize views .........................................................................
		this_link			= this;
		edit_comment		= (EditText)	findViewById(R.id.edit_dialog_comment_text);
		radioGroup_status	= (RadioGroup)	findViewById(R.id.radioGroup_dialog_comment_status);
		button_cancel		= (Button)		findViewById(R.id.button_dialog_comment_cancel);
		button_ok			= (Button)		findViewById(R.id.button_dialog_comment_ok);
		
		button_cancel.setOnClickListener(CancelClick);
		button_ok.setOnClickListener(OkClick);
		
		//Data variables ...........................................................................
		Intent intent = getIntent();
		int idDataStatus = intent.getIntExtra("idDataStatus", 0);
		String textualComment = intent.getStringExtra("textualComment");
		
		setDataOnScreen(idDataStatus, textualComment);
	}

	//----------------------------------------------------------------------------------------------
	private void setDataOnScreen(int idDataStatus, String comment)
	{
		//Set status
		RadioButton radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status0);
		switch(idDataStatus)
		{
			case  0: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status0); break;
			case  1: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status1); break;
			case  2: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status2); break;
			case  3: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status3); break;
			case  4: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status4); break;
			case  5: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status5); break;
			case  6: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status6); break;
		}
		radioGroup_status.check(radioBtn.getId());
		
		//Focus next month radio button
		RadioButton radioBtnNext = radioBtn;
		switch(idDataStatus)
		{
			case  0: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status1); break;
			case  1: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status2); break;
			case  2: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status3); break;
			case  3: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status6); break;
			case  4: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status5); break;
			case  5: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status5); break;
			case  6: radioBtn = (RadioButton)findViewById(R.id.radio_dialog_comment_status4); break;
		}
		radioBtnNext.setFocusableInTouchMode(true);
		radioBtnNext.requestFocus();
		radioBtnNext.setFocusableInTouchMode(false);
		
		//Set comment
		edit_comment.setText(comment);
		//edit_comment.requestFocus();
		//edit_comment.setSelection(edit_comment.length());
	}

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
		//Set status value
		int idDataStatus = 0;
		switch(radioGroup_status.getCheckedRadioButtonId())
		{
			case R.id.radio_dialog_comment_status0: idDataStatus =  0; break;
			case R.id.radio_dialog_comment_status1: idDataStatus =  1; break;
			case R.id.radio_dialog_comment_status2: idDataStatus =  2; break;
			case R.id.radio_dialog_comment_status3: idDataStatus =  3; break;
			case R.id.radio_dialog_comment_status4: idDataStatus =  4; break;
			case R.id.radio_dialog_comment_status5: idDataStatus =  5; break;
			case R.id.radio_dialog_comment_status6: idDataStatus =  6; break;
		}
		
        //Set activity result value
        Intent intent = new Intent();
        intent.putExtra("textualComment", edit_comment.getText().toString());
        intent.putExtra("idDataStatus", idDataStatus);
        setResult(RESULT_OK, intent);
        
        //Close the activity
		finish();
	}
	//----------------------------------------------------------------------------------------------
}
