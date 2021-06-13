package tr.edu.yildiz.virtualcabinet;

import android.content.Context;
import android.net.Uri;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Utils {

    public static List<String> CATS = Arrays.asList(
            new String[]{"Hat", "Glasses", "T-Shirt", "Coat", "Trousers", "Shoes", "Other"}
            );

    public static List<String> PATTERNS = Arrays.asList(
            new String[]{"Plain", "Striped", "Checkered", "Other"}
    );

    public static List<String> HEAD_CATS = Arrays.asList(
            new String[]{"Hat", "Other"}
    );
    public static List<String> FACE_CATS = Arrays.asList(
            new String[]{"Glasses", "Other"}
    );
    public static List<String> UPPER_CATS = Arrays.asList(
            new String[]{"Coat", "T-Shirt", "Other"}
    );
    public static List<String> LOWER_CATS = Arrays.asList(
            new String[]{"Trousers", "Other"}
    );
    public static List<String> FEET_CATS = Arrays.asList(
            new String[]{"Shoes", "Other"}
    );

    public static LinkedHashMap<String, List<String>> PART_CATS = new LinkedHashMap<String, List<String>>() {

        {
            put("Head", HEAD_CATS);
            put("Face", FACE_CATS);
            put("Upper", UPPER_CATS);
            put("Lower", LOWER_CATS);
            put("Feet", FEET_CATS);
        }
        ;
    };

    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    public static Uri copyFile(Context context, Uri src, String folder, String fileName) throws IOException {
        InputStream in = context.getContentResolver().openInputStream(src);
        File subDirectory = new File(context.getFilesDir(), folder);
        if (!subDirectory.exists()){
            if (folder.contains("media/")){
                File media = new File(subDirectory.getParent());
                if (!media.exists()){
                    media.mkdir();
                }
            }
            subDirectory.mkdir();
        }
        File dst = new File(subDirectory, fileName);
        dst.createNewFile();
        try {
            OutputStream out = new FileOutputStream(dst, false);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
        return Uri.fromFile(dst);
    }
}
