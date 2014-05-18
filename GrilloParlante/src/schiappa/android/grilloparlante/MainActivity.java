package schiappa.android.grilloparlante;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.lang.annotation.Annotation;
import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class MainActivity extends Activity  {
	
	TextToSpeech ttobj;

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public class myJavaScriptInterface implements OnInitListener{
    	
    	Context mContext;
    	boolean TtsInitOk = false;
    	
    	myJavaScriptInterface(Context c){
    		mContext = c;
    	}
    	
    	@Override
    	public void onInit(int status) {
    		if(status != TextToSpeech.ERROR){
                ttobj.setLanguage(Locale.ITALY);
               }
    		if (status == TextToSpeech.SUCCESS){
    			this.TtsInitOk = true;
    		}
    	}

    	
    	@JavascriptInterface
    	public void parla(String frase){
    		
    		
    		if (ttobj == null){
    			ttobj = new TextToSpeech(mContext , this);
    		}
    		
    		
    		if (this.TtsInitOk){
    			ttobj.speak(frase, TextToSpeech.QUEUE_FLUSH, null);
    		}
    		else {
    			Toast.makeText(mContext, "TTS Error", Toast.LENGTH_SHORT).show();
    		}
    		
    	}
    	

    	  @JavascriptInterface
    	    public void showToast(String toast) {
    	        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    	    }
    	
    }




    
}
