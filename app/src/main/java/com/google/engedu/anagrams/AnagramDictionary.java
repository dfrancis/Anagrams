/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            }
            else {
                ArrayList<String> newWordList = new ArrayList<>();
                newWordList.add(word);
                lettersToWord.put(sortedWord, newWordList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        boolean isValidWord = wordSet.contains(word);
        boolean isSubstring = word.contains(base);
        return isValidWord && !isSubstring;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String targetWordSorted = sortLetters(targetWord);

        Iterator<String> it = wordList.iterator();
        String dictWord;
        while (it.hasNext()) {
            dictWord = it.next();
            if (dictWord.length() == targetWord.length()) {
                if (targetWordSorted.equals(sortLetters(dictWord))) {
                    result.add(dictWord);
                }
            }
        }
        return result;
    }

    private String sortLetters(String word) {
        char chars[] = word.toCharArray();
        Arrays.sort(chars);
        String sortedWord = new String(chars);
        return sortedWord;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            String targetWord = word + ch;
            String targetWordSorted = sortLetters(targetWord);
            if (lettersToWord.containsKey(targetWordSorted)) {
                result.addAll(lettersToWord.get(targetWordSorted));
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        // return "stop";
        Random rand = new Random();
        int starterWordIdx = rand.nextInt(wordList.size());
        String starterWord = new String();
        boolean foundGoodStarterWord = false;
        while (!foundGoodStarterWord) {
            starterWord = wordList.get(starterWordIdx);
            if (starterWord.length() <= wordLength) {
                List<String> anagrams = getAnagramsWithOneMoreLetter(starterWord);
                if (anagrams.size() >= MIN_NUM_ANAGRAMS) {
                    foundGoodStarterWord = true;
                }
            }

            starterWordIdx = (starterWordIdx + 1) % wordList.size();
        }

        ++wordLength;

        return starterWord;
    }
}
