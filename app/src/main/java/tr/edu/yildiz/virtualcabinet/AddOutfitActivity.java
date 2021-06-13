package tr.edu.yildiz.virtualcabinet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class AddOutfitActivity extends AppCompatActivity {

    EditText colorEditText, priceEditText;
    ImageView imageView;
    DatePicker datePicker;
    Integer currentDrawerId;
    Uri imageUri;
    boolean editMode;
    Outfit currentOutfit;
    Spinner patternSpinner, categorySpinner;
    int GALLERY_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outfit);
        currentDrawerId = getIntent().getIntExtra("drawerId", 1);
        datePicker = (DatePicker) findViewById(R.id.add_outfit_datepicker);
        colorEditText = (EditText) findViewById(R.id.edit_outfit_color);
        priceEditText = (EditText) findViewById(R.id.edit_outfit_price);
        imageView = (ImageView) findViewById(R.id.preview_image_outfit);
        patternSpinner = (Spinner) findViewById(R.id.pattern_spinner);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        editMode = getIntent().getBooleanExtra("editMode", false);

        if(editMode){
            Integer currentId = getIntent().getIntExtra("outfitId", 0);
            OutfitDao dao = CabinetDB.getInstance(getApplicationContext()).outfitDao();
            currentOutfit = dao.getOutfitById(currentId);

            populateFields();
        }
        else
            setDefaultDate();
    }


    public void onSaveClick(View view){
        boolean valid = true;
        if(Utils.isEmpty(colorEditText)){
            colorEditText.setError("This field can't be empty!");
            valid = false;
        }
        if(Utils.isEmpty(priceEditText)){
            priceEditText.setError("This field can't be empty!");
            valid = false;
        }
        if(patternSpinner.getSelectedItemPosition() == 0){
            Toast toast = Toast.makeText(this, "Please choose a pattern", Toast.LENGTH_LONG);
            toast.show();
            valid = false;
        }
        else if(categorySpinner.getSelectedItemPosition() == 0){
            Toast toast = Toast.makeText(this, "Please choose a category", Toast.LENGTH_LONG);
            toast.show();
            valid = false;
        }
        else if(imageUri == null){
            Toast toast = Toast.makeText(this, "Please choose an image first", Toast.LENGTH_LONG);
            toast.show();
            valid = false;
        }
        if(valid){
            String color = colorEditText.getText().toString();
            String cat = categorySpinner.getSelectedItem().toString();
            String pat = patternSpinner.getSelectedItem().toString();
            float price = Float.parseFloat(priceEditText.getText().toString());
            price = (float) Math.round(price * 100) / 100 ;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, datePicker.getYear());
            c.set(Calendar.MONTH, datePicker.getMonth());
            c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            String fileName = UUID.randomUUID().toString().replace("-", "");
            try {
                String mediaUri = Utils.copyFile(getApplicationContext(), imageUri, "outfit-pictures", fileName).toString();
                Outfit outfit = new Outfit(currentDrawerId, cat, color, pat, c, price,mediaUri);
                if(editMode){
                    outfit.setId(currentOutfit.getId());
                }
                CabinetDB.getInstance(getApplicationContext()).outfitDao().insert(outfit);
            } catch (IOException e){
                Log.e("E", e.toString());
            }
            Toast toast = Toast.makeText(getApplicationContext(), "Outfit was saved successfully", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }


    }

    public void onChooseClick(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Pick a picture"), GALLERY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultData, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultData, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultData == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(imageUri);
        }
        else{
            imageView.setImageURI(null);
            imageView.setVisibility(View.GONE);
        }
    }

    private void setDefaultDate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(mYear, mMonth, mDay);
    }

    private void populateFields(){
        colorEditText.setText(currentOutfit.getColor());
        priceEditText.setText(String.valueOf(currentOutfit.getPrice()));
        patternSpinner.setSelection(Utils.PATTERNS.indexOf(currentOutfit.getPattern()) + 1);
        categorySpinner.setSelection(Utils.CATS.indexOf(currentOutfit.getCategory()) + 1);
        imageView.setVisibility(View.VISIBLE);
        imageUri = Uri.parse(currentOutfit.getPhoto());
        imageView.setImageURI(imageUri);
    }
}