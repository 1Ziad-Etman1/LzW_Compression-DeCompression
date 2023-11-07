import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Compressor compressor = new Compressor();
//        String test = "ABAABABBAABAABAAAABABBBBBBBB";
        File to_compress = new File("/run/media/phantom/New Volume/University/Data Compression/Assignments/Assignment 2/LzW_Compression-DeCompression/LzW/src/test.txt");
        ArrayList<String> arr = new ArrayList<String>( to_compress.read_to_compress());
        String test = "";
        for (String s : arr){
            test += s;
        }
        System.out.println("compressed:"+compressor.compress(test));
        ArrayList<String> ByteStrings = new ArrayList<String>(to_compress.convert_to_bytes(compressor.compress(test)));
        to_compress.string_to_binary_file(ByteStrings,"/run/media/phantom/New Volume/University/Data Compression/Assignments/Assignment 2/LzW_Compression-DeCompression/LzW/src/compressed.bin");



        DeCompressor decompressor = new DeCompressor();
        File to_decompress = new File("/run/media/phantom/New Volume/University/Data Compression/Assignments/Assignment 2/LzW_Compression-DeCompression/LzW/src/compressed.bin");
        ArrayList<String> ByteStringss = new ArrayList<>(to_decompress.read_binary_file());
        for(String s : ByteStringss){
            System.out.println(s);
        }
        String decompressedtext = decompressor.decompress(to_decompress.read_to_decompress());
        System.out.println("decompressed:"+decompressedtext);
        System.out.println(decompressedtext.equals(test));

        to_decompress.string_to_text_file(decompressedtext,"/run/media/phantom/New Volume/University/Data Compression/Assignments/Assignment 2/LzW_Compression-DeCompression/LzW/src/decompressed.txt");





    }
}