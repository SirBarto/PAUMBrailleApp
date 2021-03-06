package polsl.p.paum;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LearningActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer player;

    int[] sampleOfCharsList = {R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g, R.raw.h, R.raw.i,
            R.raw.j, R.raw.k, R.raw.l, R.raw.m, R.raw.n, R.raw.o, R.raw.p, R.raw.q, R.raw.r, R.raw.s,
            R.raw.t, R.raw.u, R.raw.v, R.raw.w, R.raw.x, R.raw.y, R.raw.z};

    String[] tempCode = new String[6];
    String tempCode2 = "000000";

    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    String[][] BrailleAndAlphabet = {
            {"100000", "101000", "110000", "110100", "100100", "111000", "111100", "101100",
                    "011000", "011100", "100010", "101010", "110010", "110110", "100110",
                    "111010", "111110", "101110", "011010", "011110", "100011", "101011",
                    "011101", "110011", "110111", "100111"},
            {"A", "B", "C", "D", "E", "F", "G", "H",
                    "I", "J", "K", "L", "M", "N", "O",
                    "P", "Q", "R", "S", "T", "U", "V",
                    "W", "X", "Y", "Z"}
    };

    Button menubutton;
    Button[] buttons;
    TextView CompareDisplaytextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        buttons = new Button[6];

        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "button" + (i + 1);

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = ((Button) findViewById(resID));
            buttons[i].setOnClickListener(this);
        }

        menubutton = findViewById(R.id.menubutton);
        menubutton.setOnClickListener(this);

        CompareDisplaytextView = findViewById(R.id.textView3);
        CompareDisplaytextView.setOnClickListener(this);

        for (int i = 0; i < tempCode.length; i++) {
            tempCode[i] = "0";
        }
    }

    void displaySymbol(int tempAlphabet) {
        Log.v("int  tempalphabet", String.valueOf(tempAlphabet));
        Log.v("test alphabet", alphabet[tempAlphabet]);
        CompareDisplaytextView.setText(alphabet[tempAlphabet]);
    }

    public void play(int indexSong) {
        if (player == null) {
            player = MediaPlayer.create(this, sampleOfCharsList[indexSong]);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        player.start();
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void stop() {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            //Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getId() == v.getId()) {
                buttons[i].setEnabled(true);
                if (buttons[i].isPressed()) {
                    buttons[i].setBackgroundColor(getResources().getColor(R.color.colorButtonPressed));
                    tempCode[i] = Integer.toString(1);
                    buttons[i].setClickable(false);
                    break;
                } else if (!buttons[i].isPressed()) {
                    buttons[i].setBackgroundColor(getResources().getColor(R.color.colorButtonFocused));
                    tempCode[i] = Integer.toString(0);
                    break;
                }

                for (int a = 0; a <= tempCode.length; a++)
                    if (a == i) {
                        tempCode[a] = tempCode[i];
                    }
            }
        }

        tempCode2 = tempCode[0] + tempCode[1] + tempCode[2] + tempCode[3] + tempCode[4] + tempCode[5];
        Log.v("tempCode2: ", tempCode2);

        if (CompareDisplaytextView.isPressed())
            compare(tempCode2);

        if(v.getId() == R.id.menubutton){
            startActivity(new Intent(LearningActivity.this, MainActivity.class));
        }
    }

    private void defaultButton() {
        for (int i = 0; i < tempCode.length; i++) {
            tempCode[i] = "0";
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPressed(false);
            buttons[i].clearFocus();
            buttons[i].setBackgroundColor(getResources().getColor(R.color.colorButtonFocused));
            buttons[i].setClickable(true);
        }

    }

    void compare(String tempCode) {
        for (int i = 0; i < BrailleAndAlphabet.length; i++) {
            for (int j = 0; j < BrailleAndAlphabet[i].length; j++) {
                Log.v("compare i:", String.valueOf(i));
                Log.v("compare j:", String.valueOf(j));

                if (BrailleAndAlphabet[i][j].equals(tempCode)) {
                    displaySymbol(j);
                    play(j);
                    break;
                }
            }
            defaultButton();
        }
    }

}