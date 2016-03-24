package com.arguide.william.arguide.DataManager;

import android.util.Log;
import com.arguide.william.arguide.HttpRequest.HttpConnection;
import org.json.*;
import com.arguide.william.arguide.Model.InterestingPoint;
import java.util.Vector;

/*
*
*Manage data,singleton
*/
public class DataManager implements HttpConnection.CallbackListener{
    public Vector<InterestingPoint> pointVector;
    public int count=0;

    private String TAG = "DataManager";
    private DataSuccessListener dataListener;
    private static DataManager dataManager = null;

    public void SetListener(DataSuccessListener listener){
        this.dataListener = listener;
    }
    //private CallbackListener callbackListener = new HttpConnection.CallbackListener() {
    @Override
    public void callBack(String v) {
        try{
            JSONObject jsonObject = new JSONObject(v);
            count = jsonObject.getInt("count");
            pointVector = new Vector<InterestingPoint>(count);
            for(int i=0;i<count;i++){
                pointVector.add(new InterestingPoint(
                        jsonObject.getJSONObject(""+i).getString("name"),
                        jsonObject.getJSONObject(""+i).getString("description"),
                        jsonObject.getJSONObject(""+i).getDouble("latitude"),
                        jsonObject.getJSONObject(""+i).getDouble("longitude")));
            }
            //Log.e(TAG,"name :"+pointVector);
            dataListener.DataSuccess("Success");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    // };
    public interface DataSuccessListener {
        public void DataSuccess(String result);
    }
    public static DataManager GetInstance(){
        if(dataManager == null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public void StartHttp(String URL){
        new HttpConnection().get(URL, this);

    }
}
