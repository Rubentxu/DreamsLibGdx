package com.rubentxu.juegos.core.utils.physics;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class PhysicsCollisionCategories {

    private static int allCategories = 0;
    private static int numCategories = 0;

    private static int[] categoryIndexes = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};

    private static HashMap<String,Object> categoryNames = new HashMap<String, Object>();

    public static Boolean Has(int categories,int theCategory){
        BitSet bs= BitSet.valueOf(new byte[]{new Integer(theCategory).byteValue()});
        return bs.intersects(BitSet.valueOf(new byte[]{new Integer(categories).byteValue()}));


    }

    public void Add(String categoryName) throws Exception {

        if(numCategories==15) throw new Exception("Solo puede tener 15 categorias");

        if(categoryNames.containsKey(categoryName)) return;

        categoryNames.put(categoryName,categoryIndexes[numCategories]);


    }

}
