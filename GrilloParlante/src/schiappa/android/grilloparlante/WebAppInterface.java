package schiappa.android.grilloparlante;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
	Context mContext;
	TextToSpeech ttobj;
	
	WebAppInterface(Context c){
		mContext = c;
	}
	
	@JavascriptInterface
	public void parla(String frase){
		ttobj = new TextToSpeech(mContext ,new TextToSpeech.OnInitListener() {
			
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
		             ttobj.setLanguage(Locale.ITALY);
		            }		
				
			}
		}
		);
		
		ttobj.speak(frase, TextToSpeech.QUEUE_FLUSH, null);
		
	}
	

	  @JavascriptInterface
	    public void showToast(String toast) {
	        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	    }
	
}
