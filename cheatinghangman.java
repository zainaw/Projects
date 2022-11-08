package com.company;

// files

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// collections
import java.sql.SQLOutput;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class cheatinghangman {
    // any letter except already used
    private static final Character ANY_LETTER = '_';

    private int numLetters; // number of letters in a guessed word
    private int numMaxGuesses; // a maximum number of wrong quesses
    private int numWrongGuesses; // actual number of wrong guesses

    // list of words read from file
    private List<String> allWords;
    // list of words that have the given length and satisfy the current scheme
    private List<String> validWords;

    // set of distinct letters used as guesses
    private Set<Character> guesses;
    // map of word classes
    // the key is a scheme, the value is the set of words that satisfy it
    private Map<String, Set<String>> classes;

    private Scanner in = new Scanner(System.in); // to get user input


    // the current scheme
    // scheme is a string of numLetters characters
    // each of which is either a capital letter or the underscore symbol.
    private String curScheme;

    public static void main(String[] args) throws IOException {
        System.out.println("***Welcome to HangMan: The Game***");
        cheatinghangman game = new cheatinghangman();
        game.runAgain();
    }

    // reads all words from the file and stores them in allWords list
    private int readWords() throws IOException {
        //stores all words in a file and returns them in a list
        //source for readAllLines function: https://www.tabnine.com/code/java/methods/java.nio.file.Files/readAllLines
        allWords = Files.readAllLines(Paths.get("words.txt"));
        return allWords.size();
    }

    // asks the number of letters in a guessword
    // and stores it in numLetters
    private void askNumLetters() {
        // private Scanner in = new Scanner(System.in);
        System.out.print("Please enter the size of the word: ");
        String line = in.nextLine();

        numLetters = Integer.parseInt(line);
    }

    // asks the allowed number of incorrect guesses
    private void askMaxGuesses() {
        System.out.print("Please enter the number of guesses you would like: ");
        String line = in.nextLine();
        numMaxGuesses = Integer.parseInt(line);

        guesses = new HashSet<Character>();
    }

    // asks the next guess, filters empty input, non-characters and previous guesses
    private Character askGuess() {
        while (true) {
            System.out.print("Please enter a letter: ");
            String line = in.nextLine();
            //if user does not answer
            if (line.length() <= 0) {
                continue;
            }
            //if not a character
            Character ch = line.charAt(0);
            if (!Character.isLetter(ch)) {
                continue;
            }
            //if the guess was not made then we add it to our guess set
            ch = Character.toUpperCase(ch);
            if (!guesses.contains(ch)) {
                guesses.add(ch);
                return ch;
            }
            //for repeated inputs
            else {
                System.out.println("Sorry, this letter was already used!");
            }
        }
    }

    // checks whether the given word satisfies the current scheme
    private boolean isWordValid(String word) {
        //if the word length is not equal to the length of guessed word obvi not valid
        if (word.length() != numLetters) {
            return false;
        }
        //
        for (int i = 0; i < numLetters; i++) {
            Character chS = curScheme.charAt(i);
            Character chW = word.charAt(i);
            //if we have a char and the char of the word is not equal to the char of our guess then not valid
            if (chS != ANY_LETTER && chW != chS) {
                return false;
            }
            //if our character is equal to blank and we already guessed a char in the word then not valid
            if (chS == ANY_LETTER && guesses.contains(chW)){
                return false;
            }
        }
        //other cases should be valid
        return true;
    }

    // filters only valid words from the allWords list to validWords list
    private int fillValidWords() {
        validWords = new ArrayList<String>();

        for (String word : allWords) {
            word = word.toUpperCase();
            //if we have a valid word then we add them to our list
            if (isWordValid(word)){
                validWords.add(word);
            }
        }

        allWords = validWords;

        return validWords.size();
    }

    // creates and fills classes of words
    // a class is defined by a "scheme" -- all words that satisfy it
    private void fillWordsClasses() {
        classes = new HashMap<String, Set<String>>();

        for (String word : validWords) {
            // It is the most hard part of the program.
            // I see no simple way to enumerate all possible schemes
            // BUT it is relatively simple to form a scheme given a word.
            String scheme = "";
            for (int i = 0; i < numLetters; i++) {
                Character chS = curScheme.charAt(i);
                Character chW = word.charAt(i);
                //if guessed char is not a letter we already used then we add that to our scheme
                if (chS != ANY_LETTER) {
                    scheme += chS;
                }
                //if guessed char is letter we did not already used and the char in the word is in our list of guesses then we add the char of word
                else if (chS == ANY_LETTER && guesses.contains(chW)) {
                    scheme += chW;
                }
                // if(chS == ANY_LETTER && !guesses.contains(chW))
                else {
                    scheme += ANY_LETTER;
                }
            }

            // find existing scheme or add a new if necessary
            Set<String> class1;
            if (classes.containsKey(scheme)){
                class1 = classes.get(scheme);
            }
            else {
                // debugging
                // System.out.println(word + "->" + scheme);

                class1 = new HashSet<String>();
                classes.put(scheme, class1);
            }

            class1.add(word); // and add the current word to it
        }
    }

    // finds and returns the scheme with most words.
    // Also incements numWrongGuesses if the last guess was wrong
    // WITH RESPECT to the chosen scheme.
    private String findLongestClass(Character ch) {
        int maxClassSize = -1;
        String maxClassScheme = "";

        for (String scheme : classes.keySet()) {
            Set<String> class1 = classes.get(scheme);
            if (maxClassSize < class1.size()) {
                maxClassSize = class1.size();
                maxClassScheme = scheme;
            }
        }

        if (maxClassScheme.indexOf(ch) < 0){
            numWrongGuesses++;
        }

        return maxClassScheme;
    }

    // returns true if the game is not over
    private boolean noWin() {
        //System.out.println(classes.get(curScheme).iterator().next());
        if (numWrongGuesses >= numMaxGuesses) {
            System.out.println("\n You Lost:(");
            //source for iterator: https://www.w3schools.com/java/java_iterator.asp (iterates over collection so good for maps)
            String word = classes.get(curScheme).iterator().next();
            System.out.println("The word was '" + word + "'.");
            return false;
        }
        else if (curScheme.indexOf(ANY_LETTER) < 0) {
            System.out.println("\nYou have won:D");
            return false;
        }
        else {
            return true;
        }

    }

    // Runs a game, maybe many times.
    public void runAgain() throws IOException {
        while (true) {
            runGame();

            System.out.print("\nDo you want to play again? :D (y/n): ");
            String line = in.nextLine();
            if (!line.equalsIgnoreCase("y")) {
                break;
            }
            else {
                System.out.println();
            }
        }
    }

    // runs game once
    private void runGame() throws IOException {
        //our prompts
        readWords();
        askNumLetters();
        askMaxGuesses();

        char[] chars = new char[numLetters];
        Arrays.fill(chars, ANY_LETTER);
        curScheme = new String(chars);

        numWrongGuesses = 0;
        System.out.println("Guess the word: " + curScheme);

        while (noWin()) {
            System.out.println();

            int numValid = fillValidWords();
            System.out.println("Wrong guesses: " + numWrongGuesses + "/" + numMaxGuesses
                    + ", words remain: " + numValid);
            System.out.println("Letters used: " + guesses.toString());

            Character ch = askGuess();

            fillWordsClasses();
            String longestScheme = findLongestClass(ch);
            System.out.println("Guess again: " + longestScheme);

            curScheme = longestScheme;
        }
    }
}

