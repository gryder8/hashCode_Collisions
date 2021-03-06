package pkg;


import java.io.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import java.nio.charset.StandardCharsets;

public class Main {

    private static File text = new File ("/Users/gavin/Desktop/words.txt"); //For a different file, change this path or add a new one and specify it instead of text
    private static Map<Integer, Set> collisions(Collection values) {
        Map<Integer, Set> result = new HashMap<>();
        for (Object value : values) {
            Integer hc = Integer.valueOf(value.hashCode());

            Set bucket = result.get(hc);
            if (bucket == null)
                result.put(hc, bucket = new TreeSet<>());

            bucket.add(value);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        System.err.println("Loading lines from given file...");
        Set lines = new HashSet<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(text),StandardCharsets.UTF_8))) {
            for (String line = r.readLine(); line != null; line = r.readLine())
                lines.add(line);
        }

        // Warm up, if you please
        System.err.print("Warming up");
        for (int i = 0; i < 10; i++) {
            System.err.print(".");
            collisions(lines);
        }
        System.err.println();

        System.err.println("Computing collisions...");
        long start = System.nanoTime();
        Map<Integer, Set> collisions = collisions(lines);
        long finish = System.nanoTime();
        long elapsed = finish - start;

        int maxhc = 0, maxsize = 0;
        for (Map.Entry<Integer, Set> e : collisions.entrySet()) {
            Integer hc = e.getKey();
            Set bucket = e.getValue();
            if (bucket.size() > maxsize) {
                maxhc = hc.intValue();
                maxsize = bucket.size();
            }
        }

        System.out.println("Elapsed time: " + elapsed + "ns");
        System.out.println("Total unique lines: " + lines.size());
        System.out.println("Time per hashcode: " + String.format("%.4f", 1.0 * elapsed / lines.size()) + "ns");
        System.out.println("Total unique hashcodes: " + collisions.size());
        System.out.println("Total collisions: " + (lines.size() - collisions.size()));
        System.out.println("Collision rate: " + String.format("%.8f", 1.0 * (lines.size() - collisions.size()) / lines.size()));
        if (maxsize != 0)
            System.out.println("Max collisions: " + maxsize + " " + collisions.get(maxhc));
    }
}