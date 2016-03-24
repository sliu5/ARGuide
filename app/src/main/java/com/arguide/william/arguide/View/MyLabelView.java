package com.arguide.william.arguide.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.arguide.william.arguide.R;

import org.w3c.dom.Text;
//import android.R;

/**
 * Created by william on 15-4-9.
 */
public class MyLabelView extends LinearLayout {
    private String TAG = "MyLabelView";
    private Button nameBtn;
    private TextView distanceText;
    public double distance=0;
    public double angle = 0;
    public double latitude = 0;
    public double longitude = 0;
    public int factor = 0;
    public String description;

    public MyLabelView(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mylabel_view,this);
        nameBtn = (Button)findViewById(R.id.nameBtn);
        distanceText = (TextView)findViewById(R.id.distanceText);
    }

    public MyLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//Name label
    public void SetName(String name){
        nameBtn.setText(name);
    }
    public String GetName(){
        return nameBtn.getText().toString();
    }
//Distance label
    public void SetDistance(double dis){
        distanceText.setText(""+dis+" m");
        distance = dis;
    }
    public void SetFactor(int z,int factor){
        this.setZ(z);
        if(factor !=0) {

            this.setScaleX(1-factor/10);//
            this.setScaleY(1-factor/10);//(float) Math.sqrt(factor/10)
        }
    }
}
