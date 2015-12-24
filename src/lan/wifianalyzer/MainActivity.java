package lan.wifianalyzer;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	
	private WifiReceiver wifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        
        wifi = new WifiReceiver(wm);
        this.registerReceiver(wifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        
        wifi.start();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(wifi);
	}
}
