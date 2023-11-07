package com.example.lzwgui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DeCompressor {
    public String decompress(ArrayList<Integer> compressedinput){
        Map<Integer,String> dictionary = new HashMap<>();
        int dictindex = 128;
        String previous = "" + (char)(int) compressedinput.remove(0);
        StringBuilder result = new StringBuilder(previous);

        for(int i =0;i<128;i++){
            dictionary.put(i,""+(char) i);
        }

        for (int tag : compressedinput){
            String current;
            if(dictionary.containsKey(tag)){
                current = dictionary.get(tag);
            }
            else if (tag == dictindex){current = previous+previous.charAt(0);}
            else {
                current="";
            }
            result.append(current);
            dictionary.put(dictindex++, previous + current.charAt(0));
            previous = current;
        }
        return result.toString();
    }
}
