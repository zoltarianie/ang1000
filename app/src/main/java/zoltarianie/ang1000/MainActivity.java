package zoltarianie.ang1000;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import zoltarianie.ang1000.R;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends Activity {

	private DBHelper myDB;
	private HashMap<String, String> aWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

		myDB = new DBHelper(this);

		setNewWorld();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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

	private boolean dialogOnScene = false;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dialogOnScene = false;

		super.onActivityResult(requestCode, resultCode, data);
		setNewWorld();
	}

	public void dialogClicked(final View view){
		if(dialogOnScene) return;
		dialogOnScene = true;

		EditText mEdit = (EditText) findViewById(R.id.translated);

	    String sTranslated = mEdit.getText().toString().trim();

		Intent myIntent = new Intent(this, DialogActivity.class);
		myIntent.putExtra("id", aWorld.get("id"));
		myIntent.putExtra("en", aWorld.get("en"));
		myIntent.putExtra("pl", aWorld.get("pl"));
		myIntent.putExtra("re", aWorld.get("re"));
		myIntent.putExtra("translated", sTranslated);

		startActivityForResult(myIntent, 1);

		String s1 = aWorld.get("en").toLowerCase(Locale.getDefault());
		String s2 = sTranslated.toLowerCase(Locale.getDefault());

        myDB.serResult(compareWords(s1, s2), aWorld);
	}

    private void setNewWorld() {
		EditText mEdit = (EditText) findViewById(R.id.translated);
    	mEdit.setText("");
    	mEdit.setHint(R.string.translated);
		aWorld = myDB.getWord();
    	TextView text = (TextView) findViewById(R.id.to_translate);
		text.setText(aWorld.get("pl"));

        HashMap<String, String> aWyn = myDB.getResult();
        TextView tvWynOk = (TextView) findViewById(R.id.tvWynOk);
        TextView tvWynYet = (TextView) findViewById(R.id.tvWynYet);
        TextView tvWynErr = (TextView) findViewById(R.id.tvWynErr);
        tvWynOk.setText(aWyn.get("ok"));
        tvWynYet.setText(aWyn.get("yet"));
        tvWynErr.setText(aWyn.get("err"));
    }

    public static Boolean compareWords(String s1 , String s2) {
        String a1_com = s1.replace("'", "");
        String a2_com = s2.replace("'", "");
        a1_com = a1_com.replace("`", "");
        a2_com = a2_com.replace("`", "");
        return(a1_com.equals(a2_com));
    }
}