package md.statistica.ipc.ipcm_v01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DialogMenu extends Activity 
{
	//View variables
	Activity	this_link;
	Button		button_clear_data;
	Button		button_assort_add;
	Button		button_assort_mod;
	Button		button_assort_del;
	Button		button_period;
	Button		button_import;
	Button		button_export;
	Button		button_exit;
	
	//----------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//Base initialization ......................................................................
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_menu);
		
		//Initialize views .........................................................................
		this_link			= this;
		button_clear_data	= (Button) findViewById(R.id.button_dialog_menu_clear_data);
		button_assort_add	= (Button) findViewById(R.id.button_dialog_menu_assort_add);
		button_assort_mod	= (Button) findViewById(R.id.button_dialog_menu_assort_mod);
		button_assort_del	= (Button) findViewById(R.id.button_dialog_menu_assort_del);
		button_period		= (Button) findViewById(R.id.button_dialog_menu_period);
		button_import		= (Button) findViewById(R.id.button_dialog_menu_import);
		button_export		= (Button) findViewById(R.id.button_dialog_menu_export);
		button_exit			= (Button) findViewById(R.id.button_dialog_menu_exit);

		//Initialize listeners .....................................................................
		button_clear_data.setOnClickListener(MenuClick);
		button_assort_add.setOnClickListener(MenuClick);
		button_assort_mod.setOnClickListener(MenuClick);
		button_assort_del.setOnClickListener(MenuClick);
		button_period.setOnClickListener(MenuClick);
		button_import.setOnClickListener(MenuClick);
		button_export.setOnClickListener(MenuClick);
		button_exit.setOnClickListener(MenuClick);
	}

	//----------------------------------------------------------------------------------------------
	//Menu OnClick Listener
	OnClickListener MenuClick = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			//Button itemPressed = (Button) v;
			
            //Set activity result value
            Intent intent = new Intent();
            intent.putExtra("itemPressedId", v.getId());
            setResult(RESULT_OK, intent);
            
            //Close the activity
			finish();
		}
	};
}
