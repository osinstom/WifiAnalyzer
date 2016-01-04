package lan.wifianalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {

	private WifiManager wifiManager = null;
	private List<ScanResult> results = null;

	public WifiReceiver(WifiManager wm) {
		this.wifiManager = wm;
		results = new ArrayList<ScanResult>();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		List<ScanResult> tmpResults = wifiManager.getScanResults();
		Collections.sort(tmpResults, new SignalLevelComparator());
		Collections.reverse(tmpResults);
		results = tmpResults;
		start();
	}
	
	private class SignalLevelComparator implements Comparator<ScanResult> {
	    @Override
	    public int compare(ScanResult o1, ScanResult o2) {
	    	Integer v1 = o1.level;
	    	Integer v2 = o2.level;
	        return v1.compareTo(v2);
	    }
	}

	public List<ScanResult> getScanResults() {
		 return results;
	}
	
	public void start() {
		wifiManager.startScan();
	}

}
