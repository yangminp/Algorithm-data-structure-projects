package swen221.lab9.parallelStream3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class KeywordFinder {
    public static long count1(String keyword, Path file) {
        // count the number of occurrences of a keyword in a file
    	// by processing the file line-by-line
    	try (Stream<String> stream = Files.lines(file).parallel()) {
            return stream.reduce(0L, (count, line) -> {
                List<String> words = Arrays.asList(line.split(" "));
               for(String s : words){
            	   if (s.equals(keyword)) 
					count++;
               }
               return count;
            }, (a,b) -> (a+b));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public static long count2(String keyword, Path file) {
    	// count the number of occurrences of a keyword in a file
    	// by processing the file word-by-word
        try (Stream<String> stream = Files.lines(file).parallel()) {
            Stream<String> s = stream.flatMap(l -> Stream.of(l.split(" ")));
            return s.reduce(0L, (count, line) -> {
                List<String> words = Arrays.asList(line.split(" "));
               for(String string : words){
            	   if (string.equals(keyword)) 
					count++;
               }
               return count;
            }, (a,b) -> (a+b));
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
