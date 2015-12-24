package lan.wifianalyzer;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiReceiver  extends BroadcastReceiver{
	
	private WifiManager wifiManager = null;
	private List<ScanResult> results = null;
	
	public WifiReceiver(WifiManager wm) {
		this.wifiManager = wm;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		results = wifiManager.getScanResults();
		for(ScanResult res : results) {
			System.out.println(res.level);
		}
		wifiManager.startScan();
	}

	public void start() {
		wifiManager.startScan();
	}
	
	

}
