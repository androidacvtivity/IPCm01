package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogAuthorize extends Activity 
{
	Button		button_close;
	Button		button_login;
	EditText	edit_login;
	EditText	edit_password;
	TextView	text_message;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
	    getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		setContentView(R.layout.dialog_authorize);
		
		//Initialize views .........................................................................
		button_close	= (Button)		findViewById(R.id.button_dialog_authorize_close);
		button_login	= (Button)		findViewById(R.id.button_dialog_authorize_login);
		edit_login		= (EditText)	findViewById(R.id.edit_dialog_authorize_login);
		edit_password	= (EditText)	findViewById(R.id.edit_dialog_authorize_password);
		text_message	= (TextView)	findViewById(R.id.text_dialog_authorize_message);
		
		edit_login.setText(DBUserInfo.login);
		edit_password.requestFocus();

		//Initialize listeners .....................................................................
		button_close.setOnClickListener(CloseClick);
		button_login.setOnClickListener(LoginClick);
		edit_login.addTextChangedListener(TextChange);
		edit_password.addTextChangedListener(TextChange);
		
		edit_login.setOnKeyListener(onEnterSubmitView);
		edit_password.setOnKeyListener(onEnterSubmitView);
	}

	//----------------------------------------------------------------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (MotionEvent.ACTION_OUTSIDE == event.getAction())
		{
			return true;
	    }
		return super.onTouchEvent(event);
	}

	//----------------------------------------------------------------------------------------------
	//Virtual keyboard key pressed
	protected View.OnKeyListener onEnterSubmitView = new View.OnKeyListener() 
	{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) 
		{
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK))
			{
				closeProcedure();
				return true;
			}
			
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
			{
				if (edit_login.isFocused()) edit_password.requestFocus();
                else if (edit_password.isFocused()) loginProcedure();
                return true;
			}
			return false;
		}
	};
	
	//----------------------------------------------------------------------------------------------
	//Text change Listener
	TextWatcher TextChange = new TextWatcher()
	{
        public void afterTextChanged(Editable s)
        {
        	text_message.setText("");
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        public void onTextChanged(CharSequence s, int start, int before, int count){}
    };

	//----------------------------------------------------------------------------------------------
	//Close OnClick Listener
	OnClickListener CloseClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			closeProcedure();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Close procedure
	private void closeProcedure()
	{
		finish();
	}

	//----------------------------------------------------------------------------------------------
	//Login OnClick Listener
	OnClickListener LoginClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			loginProcedure();
		}
	};

	//----------------------------------------------------------------------------------------------
	//Login procedure
	private void loginProcedure()
	{
		if (edit_login.getText().toString().equals(DBUserInfo.login) && edit_password.getText().toString().equals(DBUserInfo.pasHash)) 
		{
			DBUserInfo.isAuthorized = true;
			finish();
		}
		else
		{
			DBUserInfo.isAuthorized = false;
			text_message.setText(getResources().getString(R.string.dialog_authorize_message_wrong_login));
		}
	}
	//----------------------------------------------------------------------------------------------

}
