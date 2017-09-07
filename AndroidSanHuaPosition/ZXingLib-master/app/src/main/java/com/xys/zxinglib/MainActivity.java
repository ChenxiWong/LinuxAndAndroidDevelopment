package com.xys.zxinglib;

import java.io.IOException;
import java.util.List;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




import com.xys.libzxing.zxing.activity.CaptureActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {


    private TextView longtitude = null;
    private TextView latitude = null;
    private Button scanebutton = null;
    private TextView barcodevalue = null;
    private LocationManager locationManager = null;
    private String provider = null;
    private Button finalsendbutton = null;
    private TextView finaltext = null;
    private OkHttpClient client = new OkHttpClient();
    private  boolean already_location = false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result= (String) msg.obj;
            finaltext.setText(result);
        }
    };


    private Callback callback=new Callback(){
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("MainActivity","onFailure");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            //从response从获取服务器返回的数据，转成字符串处理
            String str = new String(response.body().bytes(),"utf-8");
            Log.i("MainActivity","onResponse:"+str);

            //通过handler更新UI
            Message message=handler.obtainMessage();
            message.obj=str;
            message.sendToTarget();
        }
    };

    private void getUserInfo( String tmp_url){
        //创建一个Request
        Request.Builder builder = new Request.Builder().url(tmp_url);
        execute(builder);
    }

    //执行请求
    private void execute(Request.Builder builder){
        Call call = client.newCall(builder.build());
        call.enqueue(callback);//加入调度队列
    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        longtitude = (TextView) findViewById(R.id.longtitudevalue);
        latitude = (TextView) findViewById(R.id.latitudevalue);
        scanebutton = (Button) findViewById(R.id.scanebutton);
        barcodevalue = (TextView) findViewById(R.id.signcodevalue);
        finalsendbutton = (Button) findViewById(R.id.finalsend);
        finaltext = (TextView) findViewById(R.id.editText1);

        scanebutton.setOnClickListener(new ScaneCodeButtonListener());
        finalsendbutton.setOnClickListener(new FinalSendButtonListener());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {

            provider = LocationManager.GPS_PROVIDER;
            // } else if
            // (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            // provider = LocationManager.NETWORK_PROVIDER;
        } else {

            Toast.makeText(MainActivity.this, "请核对GPS服务是否正常...",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[]
            // permissions,
            // int[] grantResults)
            // to handle the case where the user grants the permission. See the
            // documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {

            already_location = true;
            showLocation(location);
        } else {
            String info = "等待获取位置信息...";
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();

        }

        locationManager.requestLocationUpdates(provider, 3 * 1000, 1,
                locationListener);


        scanebutton = (Button) this.findViewById(R.id.scanebutton);
        scanebutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(already_location) {
                    // 打开扫描界面扫描条形码或二维码
                    Intent openCameraIntent = new Intent(MainActivity.this,
                            CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                }else
                {
                    Toast.makeText(MainActivity.this, "正在获取位置信息...",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }

    }

    class FinalSendButtonListener implements OnClickListener {


        @Override
        public void onClick(View v) {


            if(!barcodevalue.getText().toString().isEmpty()) {
                String url_str = "/location_init?";
                String url = "http://172.35.31.110:80" + url_str + "longtitude="
                        + longtitude.getText().toString() + "&latitude="
                        + latitude.getText().toString() + "&barcodestr="
                        + barcodevalue.getText().toString();
                getUserInfo(url);
            }
            else
            {
                Toast.makeText(MainActivity.this, "请核实需要获取信息。", Toast.LENGTH_LONG).show();
            }
        }

    }

    class ScaneCodeButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String longtitude_str = longtitude.getText().toString();
            String latitude_str = latitude.getText().toString();
            if (!longtitude_str.isEmpty() && !latitude_str.isEmpty()) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CaptureActivity.class);
                MainActivity.this.startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "正在获取地址...",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     *
     * @param location
     */
    private void showLocation(Location location) {
        // TODO Auto-generated method stub
        String longtitude_str = String.valueOf(location.getLongitude());
        longtitude.setText(longtitude_str);
        String latitude_str = String.valueOf(location.getLatitude());
        latitude.setText(latitude_str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            barcodevalue.setText(scanResult);
        }
    }

}
