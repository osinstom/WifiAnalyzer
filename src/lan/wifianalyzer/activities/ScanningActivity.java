package lan.wifianalyzer.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lan.wifianalyzer.R;
import lan.wifianalyzer.R.layout;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScanningActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanning_activity);

		final ListView listview = (ListView) findViewById(R.id.listview);
		ArrayList<ScanResult> results = (ArrayList<ScanResult>) MainActivity.wifi
				.getScanResults();

		final WifiNetworksAdapter adapter = new WifiNetworksAdapter();
		adapter.setWifiNetworks(results);
		listview.setAdapter(adapter);

		Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				ScanningActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						adapter.setWifiNetworks(MainActivity.wifi.getScanResults());
						adapter.notifyDataSetChanged();
					}
				});
			}
		}, 1000, 1000); // initial delay 1 second, interval 1 second

	}

	public class WifiNetworksAdapter extends BaseAdapter {

		List<ScanResult> wifiNetworks;

		public void setWifiNetworks(List<ScanResult> results) {
			wifiNetworks = results;
		}

		@Override
		public int getCount() {
			return wifiNetworks.size();
		}

		@Override
		public ScanResult getItem(int index) {
			return wifiNetworks.get(index);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			if (arg1 == null) {
				LayoutInflater inflater = (LayoutInflater) ScanningActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.row_layout, arg2, false);
			}

			TextView networkSSID = (TextView) arg1.findViewById(R.id.ssid);
			TextView networkLevel = (TextView) arg1.findViewById(R.id.level);

			ScanResult result = wifiNetworks.get(arg0);

			networkSSID.setText(result.SSID);
			networkLevel.setText("Power level: " + result.level + " dBm");

			return arg1;
		}

	}

}
