package com.karkinos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StationMatch {
    public static void main(String[] args) throws IOException {
        CodeToName aou = new CodeToName();
        List<String> staseis = new ArrayList<>();
        for (int i = 0; i < 1000000000; i++) {
            if (!"null".equals(aou.getStopName(Integer.toString(i)))){
                staseis.add(Integer.toString(i));
            } else {
                staseis.add(null);
            }
        }

        staseis.forEach(item -> System.out.println(item));
        /* 
        for (String item : staseis) {
            System.out.println(item);
        }

        /* 
        File fakelos = new File("StaseisOASA.txt"); 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fakelos))) {
            for (String i : staseis) {
                writer.write(i);  // Write each string to the file
                writer.newLine();     // Add a newline after each string
            }
            System.out.println("List has been written to " + fakelos.getName());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
            */
    }
}       

