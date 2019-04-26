package com.example.quickdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class DrawOS extends View {

    Paint paint = new Paint();
    PointF pointF = new PointF();
    ArrayList<PointF> lpointf = new ArrayList<>();
    public ArrayList<ArrayList<PointF>> listOfLine = new ArrayList<ArrayList<PointF>>();

    public DrawOS(Context context, AttributeSet attrs) {
        super(context, attrs);
        //(getContext(),R.layout.activity_draw,this);
    }

    public void setNewLine(ArrayList<ArrayList<PointF>> newLinelist){

        listOfLine = newLinelist;
        this.invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(20);

        for (int l1=0; l1 < listOfLine.size(); l1++){
            for(int l2=0; l2 < listOfLine.get(l1).size()-1; l2++){
                canvas.drawLine(listOfLine.get(l1).get(l2).x,
                        listOfLine.get(l1).get(l2).y,
                        listOfLine.get(l1).get(l2+1).x,
                        listOfLine.get(l1).get(l2+1).y,
                        paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                pointF = new PointF(x,y);
                lpointf = addPointToList(lpointf, pointF);
                listOfLine.add(lpointf);
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                pointF = new PointF(x,y);
                lpointf = addPointToList(lpointf, pointF);
                listOfLine.remove(listOfLine.size()-1);
                listOfLine.add(lpointf);
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                listOfLine.add(lpointf);
                lpointf = new ArrayList<>();
                this.invalidate();
                break;
            }
        }
        return true;
    }

    private ArrayList<PointF> addPointToList(ArrayList<PointF> lpointf,PointF pointF){


        if(lpointf.size() >= 0) {
            lpointf.add(pointF);
        }
        return lpointf;
    }

    public void DrawPicture(Canvas canvas,ArrayList<PointF> IpointF,ArrayList<ArrayList<PointF>> listOfLine,Paint paint){

        for(int i=0;i<IpointF.size()-1;i++){
            //canvas.drawCircle(Ipointf.get(i).x,Ipointf.get(i).y,100,paint);
            if(IpointF.size()>1){
                canvas.drawLine(IpointF.get(i).x,IpointF.get(i).y,IpointF.get(i+1).x,IpointF.get(i+1).y,paint);
            }
        }

        for (int i=0;i<listOfLine.size();i++){
            for(int j=0;j<listOfLine.get(i).size()-1;j++){
                //canvas.drawCircle(Ipointf.get(i).x,Ipointf.get(i).y,100,paint);
                if(listOfLine.get(i).size()>1){
                    canvas.drawLine(listOfLine.get(i).get(j).x,listOfLine.get(i).get(j).y,listOfLine.get(i).get(j+1).x,listOfLine.get(i).get(j+1).y,paint);
                }
            }
        }
    }
}
