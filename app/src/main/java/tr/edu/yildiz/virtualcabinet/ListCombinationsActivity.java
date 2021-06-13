package tr.edu.yildiz.virtualcabinet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListCombinationsActivity extends AppCompatActivity {

    ListView combinationListView;
    FloatingActionButton fab;
    CombinationDao combinationDao;
    List<Combination> combinationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_combinations);
        combinationListView = (ListView) findViewById(R.id.list_combinations);
        fab = (FloatingActionButton) findViewById(R.id.fab_combination);
        combinationDao = CabinetDB.getInstance(this.getApplicationContext()).combinationDao();
        populateList();
        combinationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClicked(position);
            }
        });

    }

    private void itemClicked(int position){
        Integer combinationId = combinationList.get(position).getId();
        Intent i = new Intent(ListCombinationsActivity.this, CombinationActivity.class);
        i.putExtra("id", combinationId);
        startActivity(i);
    }

    private void populateList(){
        ArrayAdapter<String> arr;

        combinationList = combinationDao.getCombinations();

        String[] content = new String[combinationList.size()];
        for(int i = 0; i < content.length; i++){
            content[i] = combinationList.get(i).getName();
        }
        arr = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                content);
        combinationListView.setAdapter(arr);
    }

    @Override
    protected void onResume(){
        super.onResume();
        populateList();
    }

    public void fabClicked(View view){
        final EditText combinationName = new EditText(this);
        combinationName.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Combination's name");
        builder.setMessage("Please give a name to the combination");
        builder.setView(combinationName);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = combinationName.getText().toString();
                if (Utils.isEmpty(combinationName)){
                    combinationName.setError("The name can not be empty!");
                }
                else {
                    Combination combination = new Combination(name);
                    combinationDao.insert(combination);
                    dialog.dismiss();
                    populateList();
                }
            }
        });
    }
}