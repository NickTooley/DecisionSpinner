package com.example.nickj.decisionspinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nickj.decisionspinner.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static com.example.nickj.decisionspinner.R.id.linear;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Creates an array of values to make each slice equal slice
    public float values[]={10,10,10,10,10,10,10,10,10,10};

    //Creates a map which gives each answer string a value
    Map<String, Float> m1;

    //Title string variable
    public TextView tv1;
    public int answer;
    public RelativeLayout linearVar;

    //Constants which define how many turns to do
    final static int MIN_TURNS = 3;
    final static int MORE_TURNS = 3;

    public String winner;
    public int winnerIntervals;
    Button btn;
    Button remove;
    public float intervals;
    public Map<String, Integer> finalMap;
    public String question;
    public int finalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Array which contains the questions and all the answers
        String[] newQuestionString = {"What do you want for breakfast?", "Bacon and Eggs", "Toast", "Cereal", "Muesli", "Up and Go", "English Breakfast", "Smoothie", "Yoghurt"};
        remove = (Button) findViewById(R.id.resetButton);
        remove.setVisibility(View.INVISIBLE);
        tv1 = findViewById(R.id.textView2);
        finalCount= 0;
        tv1.setText(newQuestionString[0]);
        question = newQuestionString[0];
        finalMap = new HashMap<String, Integer>();
        m1 = new HashMap<String, Float>();

        //Loops through question and answers array, skipping the first element (which is the question) and adds it to the map
        for(int i=1; i < newQuestionString.length; i++){
            if(!newQuestionString[i].equals("")) {
                m1.put(newQuestionString[i], (float) 1.0);
            }
        }
        //winner = "None";
        btn = findViewById(R.id.button2);
        btn.setOnClickListener(this);
        linearVar=(RelativeLayout) findViewById(R.id.linear);
        m1=calculateData(m1);
        linearVar.addView(new spinner(this,values,m1));




    }

    //Function to create a map with the correct amount of degrees required for each slice
    private Map<String, Float> calculateData(Map<String, Float> valueMap) {
        // TODO Auto-generated method stub
        float total=0;
        //for(int i=0;i<data.length;i++)
        for(Map.Entry<String, Float> entry: valueMap.entrySet())
        {
            total+=entry.getValue();
        }
        for(Map.Entry<String, Float> entry: valueMap.entrySet())
        {
            intervals = 360 * (entry.getValue()/total);
            entry.setValue(360 * (entry.getValue()/total));

        }
        return valueMap;

    }

    @Override
    public void onClick(View v) {

        //This picks a random amount of degrees to turn the spinner, picking the eventual winner
        //will be more useful when animation has been added
        double toDegreesdou = 360 * MIN_TURNS + Math.random() * 360f * MORE_TURNS;
        float toDegrees = (float)toDegreesdou;

        answer =(int) toDegrees % 360;
        winnerIntervals = answer / (int)intervals;
        for(Map.Entry<String, Integer> entry: finalMap.entrySet())
        {
            if (entry.getValue() == winnerIntervals){
                winner = entry.getKey();
            }
        }

       //Countdown timer, will be more useful when animation has been added
        new CountDownTimer(0,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                tv1.setText(winner);

            }
        }.start();

    }

    //Class which creates a spinner with the correct amount of 'slices', extends the View class so that it can be added to layout object
    public class spinner extends View
    {
        //Default paint which contains anti aliasing
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //float array which will contain how many degrees each slice will be
        private float[] value_degree;
        private Map<String,Float> value_Degree_Map;

        //array of colors to add into the slices
        private int[] COLORS={Color.parseColor("#AD2A1A"), Color.parseColor("#DA621E"),Color.parseColor("#107896"), Color.parseColor("#093145")};
        //Color 1 is Red, Color 2 is Green, Color 3 is Orange, Color 4 is Blue

        float temp=0;

        //Constructor
        public spinner(Context context, float[] values, Map<String, Float> valueMap) {

            super(context);
            //Clears the finalMap map with all the questions
            finalMap.clear();

            value_Degree_Map=new HashMap<String, Float>(valueMap);
            value_degree=new float[values.length];
            for(int i=0;i<values.length;i++)
            {
                value_degree[i]=values[i];
            }
            finalCount = 0;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub

            super.onDraw(canvas);
            //Moves the draw point on the canvas to the middle of the canvas
            canvas.translate(canvas.getWidth() / 2, canvas.getHeight() /2);
            //Rotates around that point to 270 degrees which makes the first option be at the top
            canvas.rotate(270f);
            //Moves the canvas back to the top left corner
            canvas.translate(-(canvas.getWidth() / 2), -(canvas.getHeight() /2));
            int x2 = canvas.getWidth();
            int y2 = canvas.getHeight();
            //Creates a new rectangle object which is the size of the canvas
            RectF rectf = new RectF(0, 0, x2, y2);
            int count = 0;
            float prev = 0;
            int paintCount = 0;
            int countNum = 0;

            //For loop which loops through all values map and draws that slice
            for(Map.Entry<String, Float> entry: value_Degree_Map.entrySet()){

                if (count == 0) {
                    if(countNum == 0) {


                        if (count % 4 == 0) {
                            paint.setColor(COLORS[0]);
                        }
                        canvas.drawArc(rectf, 0, entry.getValue(), true, paint);
                        count++;
                        count = value_Degree_Map.size() - 1;
                        countNum++;
                    }

                }
                else
                {

                    temp += prev;
                    if(count % 4 == 0) {
                        paint.setColor(COLORS[0]);
                    }
                    if(count% 4 == 1){
                        paint.setColor(COLORS[1]);
                    }
                    if(count%4 ==2){
                        paint.setColor(COLORS[2]);
                    }
                    if(count%4 == 3) {
                        paint.setColor(COLORS[3]);
                    }
                    if(count == 4 && value_Degree_Map.size() == 5){
                        paint.setColor(Color.parseColor("#606264"));
                    }
                    canvas.drawArc(rectf, temp, entry.getValue(), true, paint);
                    count--;
                }
                prev = entry.getValue();
                //finalMap.put(entry.getKey(), temp);
                finalMap.put(entry.getKey(), count);
            }
            finalCount = value_Degree_Map.size();
            int count2 = 0;

        }

    }
}
