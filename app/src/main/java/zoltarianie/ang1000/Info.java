package zoltarianie.ang1000;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import zoltarianie.ang1000.R;

public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main2, menu);
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent myIntent = null;
    	
        if(item.getItemId() == android.R.id.home || item.getItemId() == R.id.menu_show_mp){
        	myIntent = new Intent(getApplicationContext(), MainActivity.class);
        }
    	if(item.getItemId() == R.id.menu_show_info){
        	myIntent = new Intent(getApplicationContext(), Info.class);
    	}
    	startActivity(myIntent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		
        return super.onOptionsItemSelected(item);
    }
}
