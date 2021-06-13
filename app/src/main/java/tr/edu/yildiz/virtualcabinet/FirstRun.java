package tr.edu.yildiz.virtualcabinet;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class FirstRun {
    public static void run(Context context){

        String baseUri = "android.resource://" + context.getPackageName() + "/drawable/";
        DrawerDao drawerDao = CabinetDB.getInstance(context).drawerDao();


        Drawer dA = new Drawer("Drawer A");
        Drawer dB = new Drawer("Drawer B");
        Drawer dC = new Drawer("Drawer C");

        drawerDao.insert(dA);
        drawerDao.insert(dB);
        drawerDao.insert(dC);
        List<Drawer> dl = drawerDao.getDrawers();
        dA = dl.get(0);
        dB = dl.get(1);
        dC = dl.get(2);

        Log.d("SZZZ: ", String.valueOf(dA.getId()));
        List<Outfit> outfits = new ArrayList<Outfit>();

        outfits.add(
                new Outfit(
                        dA.getId(), "Glasses",
                        "Black", "Other",
                        randomDate(), 170, baseUri + "blackglasses" )
        );
        outfits.add(
                new Outfit(
                        dB.getId(), "Glasses",
                        "Beige", "Other",
                        randomDate(), 150, baseUri + "beigeglasses" )
        );
        outfits.add(
                new Outfit(
                        dC.getId(), "Glasses",
                        "Red", "Plain",
                        randomDate(), 180, baseUri + "redglasses" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Hat",
                        "Black", "Plain",
                        randomDate(), 110, baseUri + "blackhat" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Hat",
                        "Gray", "Checkered",
                        randomDate(), 120, baseUri + "grayhat" )
        );
        outfits.add(
                new Outfit(
                        dB.getId(), "Hat",
                        "Red", "Plain",
                        randomDate(), 100, baseUri + "redhat" )
        );
        outfits.add(
                new Outfit(
                        dC.getId(), "Hat",
                        "Pink", "Plain",
                        randomDate(), 100, baseUri + "pinkhat" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Coat",
                        "Red", "Plain",
                        randomDate(), 477, baseUri + "redcoat" )
        );
        outfits.add(
                new Outfit(
                        dB.getId(), "Coat",
                        "Pink", "Plain",
                        randomDate(), 450, baseUri + "pinkcoat" )
        );
        outfits.add(
                new Outfit(
                        dC.getId(), "Coat",
                        "Beige", "Plain",
                        randomDate(), 500, baseUri + "beigecoat" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Shoes",
                        "Red", "Plain",
                        randomDate(), 350, baseUri + "redshoes" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Shoes",
                        "White", "Other",
                        randomDate(), 320, baseUri + "whiteshoes" )
        );
        outfits.add(
                new Outfit(
                        dB.getId(), "Shoes",
                        "Pink", "Plain",
                        randomDate(), 300, baseUri + "pinkshoes" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Trousers",
                        "Black", "Plain",
                        randomDate(), 200, baseUri + "blacktr" )
        );
        outfits.add(
                new Outfit(
                        dA.getId(), "Trousers",
                        "Red", "Plain",
                        randomDate(), 170, baseUri + "redt" )
        );
        outfits.add(
                new Outfit(
                        dB.getId(), "Trousers",
                        "Pink", "Plain",
                        randomDate(), 250, baseUri + "pinktr" )
        );

        OutfitDao outfitDao = CabinetDB.getInstance(context).outfitDao();
        for(Outfit o: outfits){
            outfitDao.insert(o);
        }
        outfits = outfitDao.getOutfits();
        Combination cA = new Combination(
                "Combination A", outfits.get(6).getId(),
                outfits.get(2).getId(), outfits.get(8).getId(),
                outfits.get(15).getId(), outfits.get(12).getId()
        );

        CombinationDao combDao = CabinetDB.getInstance(context).combinationDao();
        combDao.insert(cA);
    }

    private static Calendar randomDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, new Random().nextInt(30));
        return c;
    }
}
