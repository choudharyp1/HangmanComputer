package hangmancomputer;

import java.io.*;
import java.util.Scanner;

public class HangmanComputer {

    public static void main(String[] args) {
        try {
            HangmanComputer a = new HangmanComputer();
            Scanner in = new Scanner(System.in);
            LinkedList l = new LinkedList();
            BufferedReader read = new BufferedReader(new FileReader("./words.txt"));
            String x = read.readLine();
            while (x != null) {
                l.push(x);
                x = read.readLine();
            }
            System.out.println("Enter word to be guessed");
            String guessWord = in.nextLine();

            System.out.println("Computer will start guessing now");

            String[] word = new String[guessWord.length()];
            for (int i = 0; i < word.length; i++) {
                word[i] = "null";
            }

            System.out.println("Computer will first filter all words of the length given");
            l.filterbyLength(guessWord.length());
            //l.display();

            //Initiating the Array of Alphabets
            Assoc[] assoc = new Assoc[26];
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            for (int i = 0; i < assoc.length; i++) {
                assoc[i] = new Assoc();
                assoc[i].alpha = alphabet[i];
            }

            a.algorithm(assoc, l, word, guessWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int findMax(Assoc[] array) {
        int maxIndex = -1;
        int max = -1;
        for (int i = 0; i < array.length; i++) {
            if (max < array[i].num && array[i].used == false) {
                max = array[i].num;
                maxIndex = i;
            }
        }
        System.out.println("This is it");
        System.out.println(array[maxIndex].alpha);
        for (int i = 0; i < array.length; i++) {
            if (array[i].used == false) {
                System.out.println(array[i].alpha + " : " + array[i].num + " : " + array[i].used);
            }
        }
        array[maxIndex].used = true;
        return maxIndex;
    }

    void algorithm(Assoc[] assoc, LinkedList l, String[] word, String guessWord) {
        int counter = 0;
        int numberOfTurns = 0;
        l.refreshArray(assoc);
        while (counter != guessWord.length() && l.head.next != null) {
            numberOfTurns++;
            char a = assoc[findMax(assoc)].alpha;
            if (guessWord.contains(a + "")) {
                for (int i = 0; i < guessWord.length(); i++) {
                    if (guessWord.charAt(i) == a) {
                        word[i] = a + "";
                        counter++;
                    }
                }
                l.filterbyArray(word);
                l.refreshArray(assoc);
            } else {
                l.filterbyLetter(a);
                l.refreshArray(assoc);
            }
        }
        System.out.println("The word guessed by computer is: " + l.head.data);
        System.out.println(numberOfTurns);
    }
}

class LinkedList {

    Node head;

    void push(String i) {
        Node h = new Node();
        h.next = head;
        head = h;
        h.data = i;
    }

    void display() {
        Node tmp;
        tmp = head;
        while (tmp != null) {
            System.out.println(tmp.data);
            tmp = tmp.next;
        }
    }

    void filterbyLength(int length) {
        while (head != null && head.data.length() != length) {
            head = head.next;
        }
        if (head != null) {
            Node tmp;
            tmp = head;
            while (tmp.next != null) {
                if (tmp.next.data.length() != length) {
                    tmp.next = tmp.next.next;
                } else {
                    tmp = tmp.next;
                }
            }
        }
    }

    void filterbyArray(String[] word) {
        while (head != null && compareWords(head.data, word) == false) {
            head = head.next;
        }
        if (head != null) {
            Node tmp;
            tmp = head;
            while (tmp.next != null) {
                if (compareWords(tmp.next.data, word) == false) {
                    tmp.next = tmp.next.next;
                } else {
                    tmp = tmp.next;
                }
            }
        }
    }

    void filterbyLetter(char a) {
        while (head != null && head.data.contains(a + "")) {
            head = head.next;
        }
        if (head != null) {
            Node tmp;
            tmp = head;
            while (tmp.next != null) {
                if (tmp.next.data.contains(a + "")) {
                    tmp.next = tmp.next.next;
                } else {
                    tmp = tmp.next;
                }
            }
        }
    }

    boolean compareWords(String s, String[] word) {
        for (int i = 0; i < s.length(); i++) {
            if (word[i].equals("null") || word[i].equalsIgnoreCase(s.charAt(i) + "")) {
                if (word[i].equals("null")) {
                    for (int j = 0; j < word.length; j++) {
                        if (word[j].equalsIgnoreCase(s.charAt(i) + "")) {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    void refreshArray(Assoc[] array) {
        display();
        for (int i = 0; i < array.length; i++) {
            array[i].num = 0;
        }
        Node tmp;
        tmp = head;
        while (tmp != null) {
            String s = tmp.data;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                int pos = ch - 'a';
                array[pos].num++;
            }
            tmp = tmp.next;
        }
    }
}

class Node {

    String data;
    Node next;

    Node() {
    }

    Node(String n) {
        data = n;

    }
}

class Assoc {

    char alpha;
    int num;
    boolean used;

    void Assoc() {
        num = 0;
        used = false;
    }
}
