package lan.wifianalyzer.activities;

import java.util.List;

import lan.wifianalyzer.R;
import lan.wifianalyzer.WifiReceiver;
import lan.wifianalyzer.R.id;
import lan.wifianalyzer.R.layout;
import lan.wifianalyzer.R.menu;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnDismissListener;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	
	private Button startScanningBtn;
	
	public static WifiReceiver wifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addButtonListener();
        
        WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        
        wifi = new WifiReceiver(wm);
        this.registerReceiver(wifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        
        wifi.start();
        
    }

    private void addButtonListener() {
		
    	final Context context = this;
    	
    	startScanningBtn = (Button) findViewById(R.id.startScanningBtn);
    	startScanningBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				List<ScanResult> scanResultList = wifi
						.getScanResults();
				if (scanResultList == null) {
					final ProgressDialog modal = new ProgressDialog(MainActivity.this);
					modal.setTitle("First scanning network...");
					modal.setMessage("Please wait");
					modal.setIndeterminate(false);
					modal.setCancelable(true);
					modal.show();
					modal.setOnDismissListener(new OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							Intent intent = new Intent(getBaseContext(), ScanningActivity.class);
							startActivity(intent);
						}
					});
					
					Thread thread = new Thread()
					{
					    @Override
					    public void run() {
					        try {
					        	List<ScanResult> networks = wifi
								.getScanResults();
					            while(networks == null || networks.size()==0) {
					                sleep(1000);
					            }
					        } catch (InterruptedException e) {
					            e.printStackTrace();
					        }
					        modal.dismiss();
					    } 
					};
					thread.start();
					
				}else{
					Intent intent = new Intent(getBaseContext(), ScanningActivity.class);
					startActivity(intent);
				}
			}
		});
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
