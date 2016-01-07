package lan.wifianalyzer.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lan.wifianalyzer.R;
import lan.wifianalyzer.R.layout;
import lan.wifianalyzer.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
        //Uzupełniamy widok w informacje o access pointach
        adapter.setWifiNetworks(results);
        listview.setAdapter(adapter);

        Timer myTimer = new Timer();


        //Uaktualniamy informację o access poitach w watku co sekundę
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ScanningActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.setWifiNetworks(MainActivity.wifi.getScanResults());
                        adapter.notifyDataSetChanged();
                        TextView t = (TextView) findViewById(R.id.number_of_networks);
                        t.setText(String.valueOf(adapter.getCount()));
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
                /**
                 * Wczytujemy widok row_layout z xmla do programu
                 */
                LayoutInflater inflater = (LayoutInflater) ScanningActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.row_layout, arg2, false);
            }

            TextView networkSSID = (TextView) arg1.findViewById(R.id.ssid);
            TextView networkLevel = (TextView) arg1.findViewById(R.id.level);
            TextView networkChannel = (TextView) arg1.findViewById(R.id.channel_number);

            ScanResult result = wifiNetworks.get(arg0);
            int c = computeChannel(result.frequency);
            /**
             * Uzupełniamy widok o informacje o access poincie
             */
            networkSSID.setText(result.SSID + " (" + result.BSSID + ")");
            networkLevel.setText("Power level: " + result.level + " dBm");
            networkChannel.setText(String.valueOf(c) + "    "+result.frequency + " MHz");
            return arg1;
        }

        public int computeChannel(int frequency){
            int channel;
            switch(frequency){
                case 2412 :
                    channel = 1;
                    break;
                case 2417 :
                    channel = 2;
                    break;
                case 2422 :
                    channel = 3;
                    break;
                case 2427 :
                    channel = 4;
                    break;
                case 2432 :
                    channel = 5;
                    break;
                case 2437 :
                    channel = 6;
                    break;
                case 2442 :
                    channel = 7;
                    break;
                case 2447 :
                    channel = 8;
                    break;
                case 2452 :
                    channel = 9;
                    break;
                case 2457 :
                    channel = 10;
                    break;
                case 2462 :
                    channel = 11;
                    break;
                case 2467 :
                    channel = 12;
                    break;
                case 2472 :
                    channel = 13;
                    break;
                case 2477 :
                    channel = 14;
                    break;
                case 2482 :
                    channel = 15;
                    break;
                default :
                    channel = 0;
                    break;
            }
            return channel;
        }

    }


}
