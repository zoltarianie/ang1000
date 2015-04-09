package zoltarianie.ang1000;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zoltarianie.ang1000.R;

import java.io.IOException;
import java.util.Locale;

public class DialogActivity extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("ingo", "DialogActivity onCreate");
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_signin);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
        Bundle extras = getIntent().getExtras();
		if (extras == null) {
			Log.e("error", "nie ma danych w popupie");
			return;
		}
		
		boolean poprawnie = false;
		String a1 = extras.getString("en").toLowerCase(Locale.getDefault());
		String a2 = extras.getString("translated").toLowerCase(Locale.getDefault());

		if(MainActivity.compareWords(a1, a2)){
			poprawnie = true;
		}
		
		TextView t1 = (TextView) findViewById(R.id.t1);
		//textView1.setTextColor(context.getResources().getColor(R.color.mycolor))

		t1.setText(a2);
		if(poprawnie)	t1.setTextColor(getResources().getColor(R.color.green));
		else			t1.setTextColor(getResources().getColor(R.color.red));
		
		TextView t2 = (TextView) findViewById(R.id.t2);
		t2.setText(a1);
		
		ImageView img = (ImageView) findViewById(R.id.result);
		if(poprawnie)	img.setImageResource(R.drawable.ok);
		else			img.setImageResource(R.drawable.nook);
		
		if("".equals(a2)){
			TextView t0 = (TextView) findViewById(R.id.t0);
			((LinearLayout) t0.getParent()).removeView(t0);
			((LinearLayout) t1.getParent()).removeView(t1);
		}
		
		zaladujSlowkoMP3(a1);
	}
	
	public void soundClicked(final View view){
		if(czyMPGotowe) mediaPlayer.start();
	}
    
	public void dialogClicked(final View view){
		finish();
	}

	@Override
	public void finish() {
	    Intent data = new Intent();
	    setResult(Activity.RESULT_OK, data);
	    super.finish();
	}
	
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

	MediaPlayer mediaPlayer = null;
	boolean czyMPGotowe = false;
	 
	public void moznaDzwiek() {
		czyMPGotowe = true;
		
		ImageView imgSound = (ImageView) findViewById(R.id.sound);
		imgSound.setVisibility(View.VISIBLE);
	}
	
    private void zaladujSlowkoMP3(String sUrl) {
        sUrl = sUrl.replace("`", "");
    	czyMPGotowe = false;
    	if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
        	String url = "https://translate.google.pl/translate_tts?ie=UTF-8&q="+sUrl+"&tl=en&total=1&idx=0&textlen="+sUrl.length()+"&client=t&prev=input";
        	mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        	mediaPlayer.setOnPreparedListener(new OnPreparedListener(){
    		    @Override
    		    public void onPrepared(MediaPlayer mp){
    		       if (mp.equals(mediaPlayer)) moznaDzwiek();
    		    }
    		});
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(url));
            mediaPlayer.prepareAsync();
        }
        catch (IllegalArgumentException e) { /*e.printStackTrace();*/ }
        catch (IllegalStateException e) { /*e.printStackTrace();*/ }
        catch (IOException e) { /*e.printStackTrace();*/ }
    }	
}

