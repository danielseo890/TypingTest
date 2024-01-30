package typingtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a random set of words using res/ files
 * @Author Kien Nguyen, Daniel Seo
 */

public class RandomWordsGenerator {
    ArrayList<String> wordList;
    Random generator;

    /**
     * Reads a file to split it into words then adds to a array wordList. 
     * @throws FileNotFoundException
     */
    public RandomWordsGenerator() throws FileNotFoundException {
        this.generator = new Random();
        
        ArrayList<String> wordList = new ArrayList<String>();
        String text = "";
        try {
            File file = new File("res/dictionary.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text += scanner.nextLine() + " ";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            wordList.add(words[i]);
        }

        this.wordList = wordList;
    }

    /**
     * Gets random words from an array then creates a string of those words.
     * @param numWords
     * @return
     */
    public String getRandomWords(int numWords) {
        int dictLimit = 1000;
        String stringResult = "";
        for (int i = 0; i < numWords; i++) {
            int randomNum = generator.nextInt(dictLimit);
            String word = this.wordList.get(randomNum);
            if (i == 0) {
                stringResult = stringResult + word;
            } else {
            stringResult = stringResult + " " + word ;
            }
        }
        return stringResult;
    }   
}
