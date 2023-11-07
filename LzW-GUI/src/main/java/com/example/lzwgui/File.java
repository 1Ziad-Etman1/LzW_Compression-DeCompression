package com.example.lzwgui;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
public class File {
    String path;

    public File(String _path) {
        Scanner in = new Scanner(System.in);

        // Validate the path
        while (true) {
            if (is_valid_file_path(_path)) {
                this.path = _path;
                break;
            } else {
                System.out.print("Invalid path\nRe enter it: ");
                _path = in.next();
            }
        }
    }
    public boolean is_valid_file_path(String path) {
        try {
            Path normalizedPath = Paths.get(path).normalize();
            return Files.isRegularFile(normalizedPath);
        } catch (InvalidPathException | SecurityException e) {
            return false;
        }
    }


    //FOR COMPRESSION
    //todo Get the File Content to send it to the Compressor
    public ArrayList<String> read_to_compress(){
        ArrayList<String> text = new ArrayList<String>();
        String s = "";
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(path)
            );
            // store the content in string
            while ((s = br.readLine()) != null){
                text.addLast(s);
            }
            br.close();
        }
        catch (Exception ex){

        }
        return text;
    }
    //todo Get the Array of Integers From the Compressor and convert it to Bytes then send it to string_to_binary_file
    public ArrayList<String> convert_to_bytes(ArrayList<Integer> tags){

        ArrayList<String> tags_binarystrings = new ArrayList<String>();
        int i = 0;
        for(int tag : tags){
            tags_binarystrings.addLast(Integer.toBinaryString(tag));
            if (tags_binarystrings.get(i).length()<8){
                tags_binarystrings.set(i,calculate_zeros(tags_binarystrings.get(i).length()) + tags_binarystrings.get(i));
            }
            i++;
        }

        return tags_binarystrings;
    }
    //todo Convert the BitStrings to bytes and write them into the file
    public void string_to_binary_file(ArrayList<String> bitStrings, String filePath) {

        String bitString = "";
        for (String s : bitStrings){
            bitString+=s;
        }
        try (FileOutputStream fos = new FileOutputStream(filePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            int length = bitString.length();
            if (length % 8 != 0) {
                throw new IllegalArgumentException("Input bit string length must be a multiple of 8.");
            }

            for (int i = 0; i < length; i += 8) {
                String byteString = bitString.substring(i, i + 8);
                byte b = (byte) Integer.parseInt(byteString, 2);
                bos.write(b);
            }

            System.out.println("Binary file has been created.");

        } catch (IOException e) {
            e.printStackTrace(); // Consider providing user-friendly error handling.
        }
    }

    /////////////////

    //FOR DECOMPRESSION
    //todo Read the File and return array of byte strings Send it to read_to_decompress
    //todo IT IS CALLED IN READ_TO_DECOMPRESS METHOD
    public ArrayList<String> read_binary_file() {
        ArrayList<String> binaryStrings = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            int data;
            StringBuilder currentString = new StringBuilder();
            int bitCount = 0;

            while ((data = fileInputStream.read()) != -1) {
                // Append the binary representation of the byte to the current string
                currentString.append(String.format("%8s", Integer.toBinaryString(data & 0xFF)).replace(' ', '0'));
                bitCount += 8;

                // If the current string contains 16 bits, add it to the list
                if (bitCount >= 8) {
                    binaryStrings.add(currentString.substring(0, 8));
                    currentString.delete(0, 8);
                    bitCount -= 8;
                }
            }

            // If there are remaining bits in the current string, add them to the list
            if (bitCount > 0) {
                binaryStrings.add(currentString.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return binaryStrings;
    }
    //todo Get array of byte strings from read_binary_file and send it to the Decompressor
    public ArrayList<Integer> read_to_decompress(){

        // the array for tags to be returned
        ArrayList<Integer> tags = new ArrayList<Integer>();

        // the array for tags as bytes to be converted
        ArrayList<String> arr = new ArrayList<String>();
        arr.addAll(read_binary_file());
        //System.out.println("Hello in Line 141 File.java");
        for (int j = 0; j < arr.size(); j++) {
            tags.addLast(convert_bytes_to_tag(arr.get(j)));
            //System.out.println(arr.get(j));

        }
        for(int tag : tags){
            System.out.print("["+tag+"],");
        }
        System.out.println("");
        return tags;
    }
    //todo Get the String from the Decompressor and write it into new file
    public void string_to_text_file(String content ,String _path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(_path))) {
            bw.write(content);
            bw.close();
            System.out.println("Decompressed File Created!");
        } catch (IOException ex) {
            ex.printStackTrace(); // Print the exception stack trace for debugging
        }
    }
    ///////////////////

    public int convert_bytes_to_tag (String bytes){
        //System.out.println("Hello in Line 165 File.java");
        // get the bits of tag
        String tag_bytes = bytes.substring(0,8);
        //System.out.println("Hello in Line 168 File.java");
        // convert them into numbers
        int tag = byte_to_num(tag_bytes);
        //System.out.println("Hello in Line 171 File.java");
        return tag;
    }
    private String calculate_zeros(int size){
        String temp = "";
        for (int i=8;i>size;i--){
            temp+="0";

        }
        return temp;
    }

    public int byte_to_num (String num_bytes){
        int num = Integer.parseInt(num_bytes, 2); // Convert binary to int
        return num;
    }

}
