package com.arguide.william.arguide;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.hardware.SensorManager;
import com.arguide.william.arguide.DataManager.DataManager;
import com.arguide.william.arguide.Model.Calculator;
import android.widget.Button;
import android.widget.TextView;
import android.location.*;

import com.arguide.william.arguide.Model.OrientationManager;
import com.arguide.william.arguide.View.MyLabelView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.view.Display;
import android.graphics.Point;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import android.hardware.Camera;
import com.arguide.william.arguide.View.CameraPreview;
import com.arguide.william.arguide.View.MyAdapter;
import android.widget.FrameLayout;
/*
 * This is the camera activity that use the camera
 *
 *
*/

public class CameraActivity extends Activity implements OrientationManager.Listener,DataManager.DataSuccessListener{

    String TAG = "CameraActivity";
    Context context = this;
    DataManager dataManager = DataManager.GetInstance();
    private OrientationManager orientationManager;
    private LocationManager locationManager;
    private Calculator calculator;
    private boolean dataLock = false;
    private double angle = 0.0;
    private Vector<MyLabelView> labelVector;
    private int count = 0;
    private double latitude,longitude;
    private RelativeLayout rootLayout;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout subLayout;
    private RelativeLayout arrowFrame;
    private LinearLayout compasslabel;
    private ListView listView;
    private ImageView arrowImg;
    private int screenHeight,screenWidth;
    private int guideIndex=0;
    private boolean isGuiding = false;
    private int cameraWideAngle = 60;
    private ImageButton compassBtn;
    private Camera mCamera;
    private CameraPreview mPreview;
    private List<String> data;
    class MyCompare implements Comparator{
        @Override
        public int compare(Object lhs, Object rhs) {
            MyLabelView intPL=(MyLabelView)lhs;
            MyLabelView intPR=(MyLabelView)rhs;

            if(intPL.distance > intPR.distance)//这样比较是降序,如果把-1改成1就是升序.
            {
                return 1;
            }
            else if(intPL.distance < intPR.distance)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        rootLayout = (RelativeLayout)findViewById(R.id.rootlayout);
        mCamera = CameraPreview.getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this,mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.container);
        preview.addView(mPreview);
        Init();
        dataManager.SetListener(this);
        dataManager.StartHttp("http://people.cs.clemson.edu/~sliu5/php/getData.php");// request the data from the php
    }
    /*
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0 :
                    break;
            }
            mThread.destroy();
        }
    };

    Runnable runnable = new Runnable() {
        // override the run() function & runs on a new thread
        @Override
        public void run() {
                for (int i = 0; i < count; i++) {
                    double ang = calculator.GetAngle(latitude, longitude,
                            dataManager.pointVector.get(i).latitude, dataManager.pointVector.get(i).longitude, angle);
                    dataManager.pointVector.get(i).angle = ang;
                }
                handler.obtainMessage(0).sendToTarget();
            }
    };
*/
/*
    *Initial variable
     */
    private void Init(){
        //Get the width of the landscape-screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
        Log.e(TAG,"height:"+screenHeight+",width:"+screenWidth);
        //Initial variables
        data = new ArrayList<String>();
        //hiddenVector = new Vector<Integer>();
        //Initial views
        arrowFrame = (RelativeLayout)findViewById(R.id.arrowframe);
        subLayout = (RelativeLayout)findViewById(R.id.relalayout);
        compassBtn = (ImageButton)findViewById(R.id.compass_btn);
        listView = (ListView)findViewById(R.id.listView);
        arrowImg = (ImageView)findViewById(R.id.arrow);
        compasslabel = (LinearLayout)findViewById(R.id.compassboard);
        compasslabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compasslabel.setVisibility(View.GONE);
                arrowImg.setVisibility(View.GONE);
                isGuiding = false;
            }
        });
        compassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowListView();
            }
        });
        //Initial models
        calculator = new Calculator();
        //Initial managers
        LocationManagerInit();
        SensorInit();
    }

    private void LocationManagerInit(){
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //Calculate distance
                if(dataLock) {
                    for (int i = 0; i < count; i++) {
                        double distance = calculator.GetDistance(latitude, longitude,
                                labelVector.get(i).latitude, labelVector.get(i).longitude);
                       // labelVector.get(i).distance = distance;
                        labelVector.get(i).SetDistance(distance);
                    }
                    if(isGuiding){
                        ((TextView)compasslabel.findViewById(R.id.compass_distanceText)).setText(labelVector.get(guideIndex).distance+" m");
                    }
                    Comparator comparator = new MyCompare();
                    Collections.sort(labelVector,comparator);
                    for(int i=0;i<count;i++) {
                        labelVector.get(i).factor = count-i;
                        labelVector.get(i).setZ(count-i);
                        //Log.e(TAG,"scale :"+(float)(count-i)/count);
                        labelVector.get(i).setScaleX((float)(count-i)/count);
                        labelVector.get(i).setScaleY((float) (count - i) / count);
                    }

                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 0, locationListener);
    }

    private void SensorInit(){
        // Get an instance of the RotationManager
        orientationManager = new OrientationManager((SensorManager)getSystemService(Activity.SENSOR_SERVICE),
                getWindow().getWindowManager());

    }



    @Override
    protected void onResume() {
        super.onResume();
        orientationManager.startListening(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationManager.stopListening();
    }

    @Override
    public void DataSuccess(String result) {
        count = dataManager.count;
        if(result.equals("Success")){
            labelVector = new Vector<MyLabelView>(count);
            dataLock = true;
            for(int i = 0 ;i < count ;i++){
                MyLabelView myLabelView = new MyLabelView(context);
                myLabelView.SetName(dataManager.pointVector.get(i).name);
                myLabelView.latitude = dataManager.pointVector.get(i).latitude;
                myLabelView.longitude = dataManager.pointVector.get(i).longitude;
                labelVector.add(myLabelView);
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params.addRule(RelativeLayout.CENTER_VERTICAL);
                subLayout.addView(myLabelView,params);
            }
        }

    }

    @Override
    public void onOrientationChanged(float azimuth,float pitch, float roll) {
        angle = azimuth;
        //Log.e(TAG,"pitch :"+roll);
        if(dataLock) {
            for (int i = 0; i < count; i++) {
                double ang = calculator.GetAngle(latitude, longitude,
                        labelVector.get(i).latitude, labelVector.get(i).longitude, angle);
                labelVector.get(i).angle = ang;
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)(screenWidth / 2 + (ang / (cameraWideAngle/2)) * screenWidth / 2),
                        (int)(screenHeight/2+(pitch*(2+labelVector.get(i).factor/count)*(screenHeight/2)/90)),0, 0);
                //Log.e(TAG,"pitch :"+(screenWidth/2+(pitch*3*(screenWidth/2)/90)));
                labelVector.get(i).setLayoutParams(params);
            }
            if(isGuiding){
                if(pitch<=0) {
                    arrowFrame.setScaleY((float)((0.6*pitch-36)/(-90)));
                }
                arrowImg.setRotation((float)labelVector.get(guideIndex).angle);
            }
        }
    }
    private void ShowListView(){
        InitialListView();
        listView.setVisibility(View.VISIBLE);
        rootLayout.bringChildToFront(listView);
    }
    private void HideListView(){
        listView.setVisibility(View.GONE);
    }
    private void InitialListView(){
        data.clear();
        for(int i=0;i<count;i++){
            Log.e(TAG,"name :"+labelVector.get(i).GetName());
        data.add(labelVector.get(i).GetName());
        }
        final MyAdapter mSelfAdapter = new MyAdapter(this,data);
        /**
         * 向ListView设置Adapter。
         */
        listView.setAdapter(mSelfAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Button)compasslabel.findViewById(R.id.compass_nameBtn)).setText(labelVector.get(position).GetName());
                ((TextView)compasslabel.findViewById(R.id.compass_distanceText)).setText(labelVector.get(position).distance + " m");
                compasslabel.setVisibility(View.VISIBLE);
                arrowImg.setVisibility(View.VISIBLE);
                HideListView();
                StartGuide(position);
            }
        });
    }
    private void StartGuide(int index){
        guideIndex = index;
        isGuiding = true;
    }
}
