package dressler.nick.udp_remote;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Steuerung dieSteuerung;

    private Button l,r;
    private SeekBar gas;
    private TextView dieTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.dieSteuerung = new Steuerung(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.l = findViewById(R.id.links);
        this.r = findViewById(R.id.rechts);
        this.gas = findViewById(R.id.gaspedal);
        this.dieTextView = findViewById(R.id.textView);

        this.dieSteuerung.starteÜbertragung();
    }

    public void anzeigen(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dieTextView.setText(s);
            }
        });
    }

    public byte gibRichtung(){
        byte dir = 0;
        if(l.isPressed() && !r.isPressed()){
            dir = 1;
        } if(!l.isPressed() && r.isPressed()){
            dir = 2;
        }
        return dir;
    }
    public int gibGas(){
        return this.gas.getProgress();
    }
}
