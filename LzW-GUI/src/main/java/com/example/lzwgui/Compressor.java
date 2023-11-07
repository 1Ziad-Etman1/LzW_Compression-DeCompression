package com.example.lzwgui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Compressor {
    public ArrayList<Integer> compress (String input){
        Map<String,Integer> dictionary = new HashMap<>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        int dictionary_index=128;
        String current = "";

        for(int i =0; i <128; i++){
            dictionary.put(""+(char) i,i);
        }

        for (char c : input.toCharArray()){
            String combined = current+c;
            if (dictionary.containsKey(combined)){
                current = combined;
            }
            else {
                result.add(dictionary.get(current));
                dictionary.put(combined,dictionary_index++);
                current = ""+c;
            }
        }

        if (!current.equals("")){
            result.add(dictionary.get(current));
        }

        return result;
    }

}
