package tr.edu.yildiz.virtualcabinet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (prefs.getBoolean("firstRun", true)){
            FirstRun.run(this);
            prefs.edit().putBoolean("firstRun", false).apply();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void drawersClicked(View view){
        Intent intent = new Intent(MainActivity.this, ListDrawersActivity.class);
        startActivity(intent);
    }

    public void combsClicked(View view){
        Intent intent = new Intent(MainActivity.this, ListCombinationsActivity.class);
        startActivity(intent);
    }

}