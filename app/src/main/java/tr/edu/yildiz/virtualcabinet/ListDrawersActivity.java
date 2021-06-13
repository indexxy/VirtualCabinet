package tr.edu.yildiz.virtualcabinet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListDrawersActivity extends AppCompatActivity {

    ListView drawerListView;
    FloatingActionButton fab;
    DrawerDao drawerDao;
    List<Drawer> drawerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drawers);
        drawerListView = (ListView) findViewById(R.id.list_drawers);
        fab = (FloatingActionButton) findViewById(R.id.fab_drawer);
        drawerDao = CabinetDB.getInstance(this.getApplicationContext()).drawerDao();
        populateList();
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClicked(position);
            }
        });

    }

    private void itemClicked(int position){
        Integer drawerId = drawerList.get(position).getId();
        Intent i = new Intent(ListDrawersActivity.this, ListOutfitsActivity.class);
        i.putExtra("drawerId", drawerId);
        startActivity(i);
    }

    private void populateList(){
        ArrayAdapter<String> arr;

        drawerList = drawerDao.getDrawers();

        String[] content = new String[drawerList.size()];
        for(int i = 0; i < content.length; i++){
            content[i] = drawerList.get(i).getName();
        }
        arr = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                content);
        drawerListView.setAdapter(arr);
    }

    @Override
    protected void onResume(){
        super.onResume();
        populateList();
    }

    public void fabClicked(View view){
        final EditText drawerName = new EditText(this);
        drawerName.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Drawer's name");
        builder.setMessage("Please give a name to the drawer");
        builder.setView(drawerName);
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
                String name = drawerName.getText().toString();
                if (Utils.isEmpty(drawerName)){
                    drawerName.setError("The name can not be empty!");
                }
                else {
                    Drawer drawer = new Drawer(name);
                    drawerDao.insert(drawer);
                    dialog.dismiss();
                    populateList();
                }
            }
        });
    }
}