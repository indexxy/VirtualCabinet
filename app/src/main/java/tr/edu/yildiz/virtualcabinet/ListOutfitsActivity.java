package tr.edu.yildiz.virtualcabinet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListOutfitsActivity extends AppCompatActivity {


    Integer drawerId;
    OutfitRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    Menu menu;
    String part;
    boolean selectionMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_outfits);
        Intent intent = getIntent();
        drawerId = intent.getIntExtra("drawerId", 1);
        selectionMode = intent.getBooleanExtra("selectionMode", false);
        recyclerAdapter = new OutfitRecyclerAdapter(selectionMode);
        OutfitDao outfitDao = CabinetDB.getInstance(getApplicationContext()).outfitDao();
        if(selectionMode){
            part = intent.getStringExtra("part");
            List<String> cats = Utils.PART_CATS.get(part);
            List<Outfit> outfitList = new ArrayList<Outfit>();
            for(String cat : cats){
                outfitList.addAll(outfitDao.getOutfitsByCategory(cat));
            }
            Log.d("z", outfitList.toString());
            recyclerAdapter.setOutfitList(outfitList);
        }
        else {
            recyclerAdapter.setOutfitList(outfitDao.getOutfitsByDrawer(drawerId));
        }

        if(!selectionMode){
            recyclerAdapter.setOnEditClick(new OutfitRecyclerAdapter.OnEditClickListener() {
                @Override
                public void onEditClick(int position) {
                    Intent intent = new Intent(ListOutfitsActivity.this, AddOutfitActivity.class);
                    intent.putExtra("editMode", true);
                    intent.putExtra("outfitId", recyclerAdapter.getOutfitList().get(position).getId());
                    intent.putExtra("drawerId", drawerId);
                    startActivity(intent);
                }
            });

            recyclerAdapter.setOnDeleteClick(new OutfitRecyclerAdapter.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(int position) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOutfitsActivity.this);
                    builder1.setMessage("Are you sure you want to delete this outfit?");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Outfit outfit = recyclerAdapter.getOutfitList().get(position);
                            File mediaFile = new File(outfit.getPhoto());
                            mediaFile.delete();
                            OutfitDao outfitDao = CabinetDB.getInstance(getApplicationContext()).outfitDao();
                            outfitDao.delete(outfit);
                            recyclerAdapter.removeItem(position);
                            recyclerAdapter.notifyDataSetChanged();
                            Toast.makeText(
                                    ListOutfitsActivity.this,
                                    "Outfit was deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
        }

        recyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        recyclerAdapter.setMenu(menu);
        if(selectionMode){
            MenuItem delete = menu.findItem(R.id.action_delete_drawer);
            MenuItem add = menu.findItem(R.id.action_add_outfit);
            delete.setVisible(false);
            add.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onResume(){
        OutfitDao outfitDao = CabinetDB.getInstance(getApplicationContext()).outfitDao();
        if(selectionMode){
            List<String> cats = Utils.PART_CATS.get(part);
            List<Outfit> outfitList = new ArrayList<Outfit>();
            for(String cat : cats){
                outfitList.addAll(outfitDao.getOutfitsByCategory(cat));
            }
            Log.d("z", outfitList.toString());
            recyclerAdapter.setOutfitList(outfitList);
        }
        else {
            recyclerAdapter.setOutfitList(outfitDao.getOutfitsByDrawer(drawerId));
        }
        recyclerAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.action_add_outfit) {
            Intent intent = new Intent(ListOutfitsActivity.this, AddOutfitActivity.class);
            intent.putExtra("drawerId", drawerId);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.action_delete_drawer){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to delete this drawer?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DrawerDao drawerDao = CabinetDB.getInstance(getApplicationContext()).drawerDao();
                    drawerDao.deleteById(drawerId);
                    Toast toast = Toast.makeText(getApplicationContext(), "Drawer was deleted successfully", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
            });
            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;

        }
        else if(item.getItemId() == R.id.action_selection_done){
            Intent intent = new Intent();
            intent.putExtra("newId", recyclerAdapter.getSelectedOutfit().getId());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


}