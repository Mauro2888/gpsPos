package gps.test.com.testgps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mGetGPS;
    TextView mTextLongitude;
    TextView mTextLatitude;
    LocationManager mLocationManager;
    LocationListener mLocationLisetener;

    String[] mPermissionArray = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
    private static final int PERM_ID = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGetGPS = (Button) findViewById(R.id.star_gps);
        mTextLongitude = (TextView) findViewById(R.id.pos_lon);
        mTextLatitude = (TextView)findViewById(R.id.pos_lat);

        if (CheckPermissionResult(MainActivity.this)){
            StartGPS();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissionArray,PERM_ID);
            }
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationLisetener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mTextLongitude.append("\n" + location.getLongitude());
                mTextLatitude.append("\n" + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERM_ID:
                CheckPermissionResult(MainActivity.this);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean CheckPermissionResult(Context context) {
        int check = ContextCompat.checkSelfPermission(context, mPermissionArray[0]);
        return check == PackageManager.PERMISSION_GRANTED;
    }


    private void StartGPS() {
        Toast.makeText(this, "Getting GPS Position", Toast.LENGTH_SHORT).show();

        mGetGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckPermissionResult(MainActivity.this)) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationLisetener);
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationLisetener);
                }

            }
        });
    }
}
