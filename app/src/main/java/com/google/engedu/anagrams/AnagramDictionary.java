package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private HashMap<String, ArrayList<String>> letterToWord = new HashMap<>();
    private HashSet<String> wordSet= new HashSet<String>();
    private ArrayList<String> set;
    private ArrayList<String> set2;
    private Integer wordLength=DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        int wordLenght;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordLenght=word.length();
            String newWord=sortLetters(word);
            if(!letterToWord.containsKey(newWord))
            {
                set = new ArrayList<>();
                set.add(word);
                letterToWord.put(newWord,set);
            }
            else letterToWord.get(newWord).add(word);
            if(!sizeToWords.containsKey(wordLenght))
            {
                set2 = new ArrayList<>();
                set2.add(word);
                sizeToWords.put(wordLenght,set2);
            }
            else sizeToWords.get(wordLenght).add(word);
        }

    }

    public boolean isGoodWord(String word, String base) {
        boolean check=false;
        if(wordSet.contains(word)){

            if(word.length()>=base.length())
            {
                for(int i=0;i<=word.length()-base.length();i++)
                {   int length = base.length();
                    String substr=word.substring(i,i+length);
                    if(substr.equals(base))
                    {
                        check=false;
                        break;
                    }
                    else check=true;
                }
                return check;

            }
            else return false;
        }
        else
        return false;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        String sortedWord=sortLetters(targetWord);
        if(letterToWord.containsKey(sortedWord))
        {
            for(String x:letterToWord.get(sortedWord))
            {
                result.add(x);
            }
        }

        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for(int i=97;i<123;i++)
        {
            char c=(char) i;
            String newWord=word.concat(String.valueOf(c));
            ArrayList<String> newList=getAnagrams(newWord);
            for(String x:newList)
            {
                if(isGoodWord(x,word))
                    result.add(x);
            }
        }

        return result;
    }

    public String sortLetters(String word){
        char[] alphabets = word.toCharArray();
        Arrays.sort(alphabets);
        String sortedword = new String(alphabets);
        return sortedword;
    }

    public String pickGoodStarterWord() {
        int i=0;
        ArrayList<String> newList =sizeToWords.get(wordLength);
        while (true) {
            String newWord = newList.get(random.nextInt(newList.size()));
            ArrayList<String> minAnagrams = getAnagramsWithOneMoreLetter(newWord);
            for (String x : minAnagrams) {
                i++;
            }
            if (i >= MIN_NUM_ANAGRAMS) {
                if(wordLength<MAX_WORD_LENGTH)wordLength++;
                return newWord;
            }
            else i=0;
        }

    }
}
