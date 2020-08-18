package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    final DatabaseReference bulb_ref = firebaseDatabase.getReference("BULB");
    final DatabaseReference fan_ref  = firebaseDatabase.getReference("FAN");
    final DatabaseReference BULB_ACK = firebaseDatabase.getReference("BULB_ACK");
    final DatabaseReference FAN_ACK  = firebaseDatabase.getReference("FAN_ACK");




    Button button1,button2,signout,delete;

    ImageView img1,img2,google;

    TextView googleView;
    private TextToSpeech tts;

    Boolean listen=FALSE;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        img1 = findViewById(R.id.imageView1red);
        img2 = findViewById(R.id.imageView2red);
        google = findViewById(R.id.google);

        googleView = findViewById(R.id.googleView);



        signout = findViewById(R.id.signout);
        delete = findViewById(R.id.Delete);


        final boolean[] colour = {TRUE};



        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if(i == TextToSpeech.SUCCESS)
                {
                    int result = tts.setLanguage(Locale.ENGLISH);

                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        System.out.println("Language not Supported");
                    }

                }

                else{
                    System.out.println("Initialization failed!!");
                }

            }
        });




        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();

                Intent i = new Intent(getApplicationContext(),FirebaseUIActivity.class);
                startActivity(i);



            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                delete();


                Intent i = new Intent(getApplicationContext(),FirebaseUIActivity.class);
                startActivity(i);

            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null)
        {
            Intent intent = new Intent(getApplicationContext(), FirebaseUIActivity.class);
            startActivity(intent);
        }











        BULB_ACK.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);



                if(value.equals("ON"))
                {

                    img1.setBackground(getDrawable(R.drawable.circle_green));
                    button1.setBackgroundColor(Color.GREEN);
                    colour[0] = FALSE;


                }

                else if(value.equals("OFF"))

                {

                    button1.setBackgroundColor(Color.RED);
                    colour[0] = TRUE;
                    img1.setBackground(getDrawable(R.drawable.circle));
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });




        FAN_ACK.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                System.out.println(value);
                if(value.equals("ON"))
                {
                    button2.setBackgroundColor(Color.GREEN);
                    colour[0] = FALSE;
                    img2.setBackground(getDrawable(R.drawable.circle_green));
                }

                else if(value.equals("OFF"))

                {
                    button2.setBackgroundColor(Color.RED);
                    colour[0] = TRUE;
                    img2.setBackground(getDrawable(R.drawable.circle));
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });













        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(colour[0] == TRUE)
                {
                    bulb_ref.setValue("ON");
                    button1.setBackgroundColor(Color.GREEN);
                    colour[0] = FALSE;


                }

                else if(colour[0] == FALSE)
                {

                    bulb_ref.setValue("OFF");
                    button1.setBackgroundColor(Color.RED);

                    colour[0] = TRUE;
                }



            }
        });




        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(colour[0] == TRUE)
                {

                    fan_ref.setValue("ON");
                    button2.setBackgroundColor(Color.GREEN);
                    colour[0] = FALSE;

                }

                else if(colour[0] == FALSE)
                {

                    fan_ref.setValue("OFF");
                    button2.setBackgroundColor(Color.RED);

                    colour[0] = TRUE;
                }



            }
        });


    }











    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }



    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }




    public void speak(){

        String text = googleView.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_ADD,null,"DEFAULT");

    }

    @Override
    protected void onDestroy() {
        if(tts != null)
        {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }





    //Speech Recognition Part:

    public void getSpeechInput(View view) {

        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if(intent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intent, 1);


        }

        else {

            Toast.makeText(this, "Your Device Does not support Google Speech Input",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK && data!=null)
                {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String command = result.get(0).toLowerCase();


                    googleView.setText(command);


                    if (command.contains("bulb and fan") || command.contains("fan and bulb")){

                        if (command.contains("on")){

                            bulb_ref.setValue("ON");
                            fan_ref.setValue("ON");
                            googleView.setText("Turning both ON");


                        }

                        else if (command.contains("off")){
                            bulb_ref.setValue("OFF");
                            fan_ref.setValue("OFF");

                            googleView.setText("Turning both OFF");


                        }
                        else {
                            googleView.setText("TRY AGAIN");
                        }
                    }



                    else if (command.contains("bulb")){

                        if (command.contains("on")){
                            bulb_ref.setValue("ON");
                            googleView.setText("Turning Bulb ON");

                        }

                        else if (command.contains("off")){
                            bulb_ref.setValue("OFF");
                            googleView.setText("Turning Bulb OFF");

                        }
                        else {
                            googleView.setText("TRY AGAIN");
                        }
                    }

                   else if (command.contains("fan")){

                        if (command.contains("on")){
                            fan_ref.setValue("ON");
                            googleView.setText("Turning Fan ON");

                        }

                        else if (command.contains("off")){
                            fan_ref.setValue("OFF");
                            googleView.setText("Turning Fan OFF");

                        }
                        else {
                            googleView.setText("TRY AGAIN");
                        }
                    }

                   else {
                       googleView.setText("TRY AGAIN");
                    }

                   speak();



                }
        }
    }
}
