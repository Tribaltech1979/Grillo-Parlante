package schiappa.android.grilloparlante;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.lang.annotation.Annotation;
import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends Activity  {
	
	TextToSpeech ttobj;
	boolean voiceEnabled = true;
	String loc = Locale.getDefault().toString();
	float speechRate = 1;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
      //  webView.addJavascriptInterface(new WebAppInterface(this), "Grillo");
        webView.addJavascriptInterface(new myJavaScriptInterface(this), "Grillo");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/index.html");
        
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		loc = settings.getString("pref_voice_list", Locale.getDefault().toString());
		voiceEnabled = settings.getBoolean("voice_checkbox", true);
		try{
		speechRate = Float.parseFloat(settings.getString("pref_voice_speech", "1.0f"));
		}catch (NumberFormatException e){
			
		}
		Log.w("Voice", "create main :"+loc+" "+voiceEnabled+" "+speechRate);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
        {
        	case R.id.Preferences:
        		//Toast.makeText(this, "Preferences is Selected", Toast.LENGTH_SHORT).show();
        		this.showOptions();
            return true;
    		default:
    		return true;
        }
    	
    	
    //	return true;
    	
    }
    
    
    private void showOptions() {
    	  Intent intent = new Intent(this, SettingsActivity.class);
    	  startActivity(intent);
	}


	public class myJavaScriptInterface implements OnInitListener, OnSharedPreferenceChangeListener{
    	
    	Context mContext;
    	boolean TtsInitOk = false;

    	myJavaScriptInterface(Context c){
    		mContext = c;
    		Log.w("Voice", "constr :"+loc+" "+voiceEnabled+" "+speechRate);
    	}
    	
    	@Override
    	public void onInit(int status) {
    	//	Log.w("Voice", "onInit1 :"+this.loc+" "+this.voiceEnabled+" "+this.speechRate);
    		if(status != TextToSpeech.ERROR){
    			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    			loc = settings.getString("pref_voice_list", Locale.getDefault().toString());
    			voiceEnabled = settings.getBoolean("voice_checkbox", true);
    			try{
    			speechRate = Float.parseFloat(settings.getString("pref_voice_speech", "1.0f"));
    			}catch (NumberFormatException e){
    				
    			}
    			Log.w("Voice", "onInit :"+loc+" "+voiceEnabled+" "+speechRate);
                ttobj.setLanguage(new Locale(loc));
                ttobj.setSpeechRate(speechRate);
               }
    		if (status == TextToSpeech.SUCCESS){
    			this.TtsInitOk = true;
    		}
    	}

    	
    	@JavascriptInterface
    	public void parla(String frase){
    		int count = 0;	
    		Log.w("Voice", "parla "+ loc+" "+ voiceEnabled+" "+speechRate);
    		if (voiceEnabled){
    		
	    		if (ttobj == null){
	    			ttobj = new TextToSpeech(mContext , this);
	    		}
	    
	    		/*
	    		 * ASPETTO 5 Secondi che il motore si avvii
	    		 * 
	    		 */
	    		while(!this.TtsInitOk || count == 5){
	    			
	    			try {
						wait(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			count = count++;
	    			
	    		}
	    		
	    		if(this.TtsInitOk){
		    		Log.w("Voice", "parla 2"+ loc+" "+ voiceEnabled+" "+speechRate+" "+ttobj.getLanguage().toString());
		    		if( ttobj.getLanguage().toString() == loc){
		    		
			    		if (this.TtsInitOk){
			    			ttobj.speak(frase, TextToSpeech.QUEUE_FLUSH, null);
			    		}
			    		else {
			    			Toast.makeText(mContext, "TTS Error", Toast.LENGTH_SHORT).show();
			    		}
		    		}
		    		else{ 
		    			ttobj.setLanguage(new Locale(loc));
			    		if (this.TtsInitOk){
			    			ttobj.speak(frase, TextToSpeech.QUEUE_FLUSH, null);
			    		}
			    		else {
			    			Toast.makeText(mContext, "TTS Error", Toast.LENGTH_SHORT).show();
			    		}
		    			
		    		}
	    		}
	    		else{
	    			Toast.makeText(mContext, "TTS Error", Toast.LENGTH_SHORT).show();
	    		}
	    			
    		}
    		
    	}
    	

    	  @JavascriptInterface
    	    public void showToast(String toast) {
    	        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    	    }

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			Log.w("Voice", "onChange "+loc+" "+ voiceEnabled+" "+speechRate);
			loc = sharedPreferences.getString("pref_voice_list", Locale.getDefault().toString());
			voiceEnabled = sharedPreferences.getBoolean("voice_checkbox", true);
			try{
    			speechRate = Float.parseFloat(sharedPreferences.getString("pref_voice_speech", "1.0f"));
    			}catch (NumberFormatException e){
    				
    			}
			
		}
    	
    }




    
}
