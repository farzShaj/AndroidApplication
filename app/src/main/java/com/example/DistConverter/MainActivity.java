package com.example.DistConverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences myPref;
    private SharedPreferences.Editor prefsEditor;
    private TextView hist;//=findViewById(R.id.history);
    private EditText input;// = findViewById(R.id.indegree);
    private TextView out;
    private TextView intext;
    private TextView outtext;//= findViewById(R.id.outdegree);
    public static boolean kmToMiles=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPref=getSharedPreferences("PREF", Context.MODE_PRIVATE);
        prefsEditor=myPref.edit();

        Button sub =findViewById(R.id.convert);
        out = findViewById(R.id.outdegree);
        hist = findViewById(R.id.history);
        input= findViewById(R.id.indegree);
        intext=findViewById(R.id.input);
        outtext=findViewById(R.id.output);
        String data=myPref.getString("Data","");
        hist.setText(data);
        RadioGroup selection = findViewById(R.id.radioGroup);
        RadioButton rb1 = findViewById(R.id.kToM);
        RadioButton rb2 = findViewById(R.id.mToK);
        kmToMiles=myPref.getBoolean("Conversion",false);
        if(kmToMiles) {
            rb1.setChecked(true);
            intext.setText("Kilometers value:");
            outtext.setText("Miles Value:");
        }
        hist.setMovementMethod(new ScrollingMovementMethod());
    }
    public void onSelect(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        TextView in=findViewById(R.id.input);
        TextView out=findViewById(R.id.output);
        EditText intext=findViewById(R.id.indegree);
        TextView outtext=findViewById(R.id.outdegree);
        String temp;
        switch(view.getId()) {
            case R.id.kToM:
                if (checked){
                    in.setText("Kilometers value:");
                    out.setText("Miles Value:");
                  kmToMiles=true;
                    temp=intext.getText().toString();
                    intext.setText(outtext.getText());
                    outtext.setText(temp);
                    Toast.makeText(this,"You have selected kilometers to miles conversion", Toast.LENGTH_SHORT).show();
                }
                    break;
            case R.id.mToK:
                if (checked){
                    in.setText("Miles Value: ");
                    out.setText("Kilometers value: ");
                  kmToMiles=false;
                    temp=intext.getText().toString();
                    intext.setText(outtext.getText());
                    outtext.setText(temp);
                    Toast.makeText(this,"You have selected miles to kilometers conversion", Toast.LENGTH_SHORT).show();
               }
                    break;
        }
        outtext.setText("");
        input.setText("");
        input.setCursorVisible(true);
        prefsEditor.putBoolean("Conversion",kmToMiles);
        prefsEditor.apply();
    }
    public void clear(View view) {
        TextView history=findViewById(R.id.history);
        history.setText("");
        out.setText("");
        input.setText("");
        input.setCursorVisible(true);
        Toast.makeText(this,"Conversion history deleted", Toast.LENGTH_SHORT).show();
    }
    public void convert(View view){
        try {
            out = findViewById(R.id.outdegree);
            hist = findViewById(R.id.history);
            input= findViewById(R.id.indegree);
            String inputdegree;//
            float indegree;//
            float outdegree;
            String outs, ins;
            if (!kmToMiles) {
                inputdegree = input.getText().toString();
                inputdegree=String.format("%.2f",Float.valueOf(inputdegree));
                indegree = Float.valueOf(inputdegree);
                outdegree = (float) (indegree * 1.60934);
                outs = String.format("%.2f",Float.valueOf(outdegree));
                out.setText(outs);
                hist.setText(" "+inputdegree + " mi => " + outs + " km\n" + hist.getText());
            } else {
                inputdegree = input.getText().toString();
                inputdegree=String.format("%.2f",Float.valueOf(inputdegree));
                indegree = Float.valueOf(inputdegree);
                outdegree = (float) (indegree * 0.621371);
                outs = String.format("%.2f",Float.valueOf(outdegree));
                out.setText(outs);
                hist.setText(" "+inputdegree + " km => " + outs + " mi\n" + hist.getText());
            }
            out.setText(outs);
            input.setText(inputdegree);
            input.setCursorVisible(false);
            prefsEditor.putString("Data",hist.getText().toString());
            prefsEditor.putBoolean("Conversion",kmToMiles);
            prefsEditor.apply();
        }
        catch(NumberFormatException e) {
            if(kmToMiles)
                Toast.makeText(this,"Please enter distance in kilometer", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Please enter distance in miles", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener indegree = new View.OnClickListener() {
      public void onClick(View v) {
        input.setCursorVisible(true);
      }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("History",hist.getText().toString());
        outState.putString("Input",input.getText().toString());
        outState.putString("Output",out.getText().toString());
        outState.putString("Intext",intext.getText().toString());
        outState.putString("Outtext",outtext.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hist.setText(savedInstanceState.getString("History"));
        input.setText(savedInstanceState.getString("Input"));
        out.setText(savedInstanceState.getString("Output"));
        intext.setText(savedInstanceState.getString("Intext"));
        outtext.setText(savedInstanceState.getString("Outtext"));
    }
}
