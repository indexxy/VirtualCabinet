package tr.edu.yildiz.virtualcabinet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CombinationActivity extends AppCompatActivity {

    ListView listView;
    Integer combId;
    Outfit[] outfits;
    String lastClickedPart;
    Integer lastClickedPosition;
    Combination currentComb;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination);
        listView = (ListView) findViewById(R.id.comb_listView);
        combId = getIntent().getIntExtra("id", 1);
        CombinationDao combDao = CabinetDB.getInstance(getApplicationContext()).combinationDao();
        OutfitDao outfitDao = CabinetDB.getInstance(getApplicationContext()).outfitDao();
        currentComb = combDao.getCombination(combId);
        outfits = new Outfit[5];
        if(currentComb != null){
            outfits[0] = outfitDao.getOutfitById(currentComb.getHeadId());
            outfits[1] = outfitDao.getOutfitById(currentComb.getFaceId());
            outfits[2] = outfitDao.getOutfitById(currentComb.getUpperBodyId());
            outfits[3] = outfitDao.getOutfitById(currentComb.getLowerBodyId());
            outfits[4] = outfitDao.getOutfitById(currentComb.getFeetId());
        }
        adapter = new CustomAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CombinationActivity.this, ListOutfitsActivity.class);
                intent.putExtra("selectionMode", true);
                lastClickedPosition = position;
                lastClickedPart =  (String) Utils.PART_CATS.keySet().toArray()[position];
                intent.putExtra("part", lastClickedPart);
                startActivityForResult(intent, 1);
            }
        });

        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Integer newId = data.getIntExtra("newId", -1);
                OutfitDao outfitDao = CabinetDB.getInstance(getApplicationContext()).outfitDao();
                outfits[lastClickedPosition] = outfitDao.getOutfitById(newId);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.combination_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.action_delete_comb){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to delete this combination?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    CombinationDao dao = CabinetDB.getInstance(getApplicationContext()).combinationDao();
                    dao.deleteById(combId);
                    Toast toast = Toast.makeText(getApplicationContext(), "Combination was deleted successfully", Toast.LENGTH_LONG);
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
        else if(item.getItemId() == R.id.action_share_comb){
            try {
                ArrayList<Uri> files = new ArrayList<Uri>();
                for(int i = 0; i < outfits.length; i++){
                    if(outfits[i] != null){
                        Uri imageUri = Uri.parse(outfits[i].getPhoto());
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        Uri newImg = saveImage(bitmap, String.valueOf(i) + ".png");
                        files.add(newImg);
                    }
                };
                shareImages(files);
            } catch(IOException e){
                e.printStackTrace();
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void fabClicked(View view) {
        Integer[] ids = new Integer[outfits.length];
        for(int i = 0; i < ids.length; i++){
            if(outfits[i] != null){
                ids[i] = outfits[i].getId();
            }
            else{
                ids[i] = null;
            }
        }

        Combination comb = new Combination(currentComb.getName(),
                ids[0],
                ids[1],
                ids[2],
                ids[3],
                ids[4]
        );
        comb.setId(currentComb.getId());
        CombinationDao dao = CabinetDB.getInstance(getApplicationContext()).combinationDao();
        dao.insert(comb);
        Toast.makeText(getApplicationContext(), "Combination was saved successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    private class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Utils.PART_CATS.keySet().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.part_item, null);

            TextView partName = v.findViewById(R.id.part_textView);
            partName.setText((String) Utils.PART_CATS.keySet().toArray()[position]);
            if(outfits[position] != null){
                ImageView imageView = v.findViewById(R.id.part_imageView);
                imageView.setImageURI(Uri.parse(outfits[position].getPhoto()));
            }
            return v;
        }
    }
    private Uri saveImage(Bitmap image, String fileName) {
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            if(!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }
            File file = new File(imagesFolder, fileName);

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

        } catch (IOException e) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

    private void shareImages(ArrayList<Uri> files){
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to leave without saving the combination?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
    }
}