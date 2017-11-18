/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static Card.Card.client1;
import static Card.Card.shuffledeck;

/**
 *
 * @author Anup Kumar
 */
public class Card {

    /**
     * @param args the command line arguments
     */
    static int port = 1234;
    static String currcard = null;
    static int turncount = 0;
    static int gameround = 1;
    static String win = "";
    static int trickround = 0;
    static int firstplayer = 1;
    static int position = 0;
    static boolean gameflag = true;
    static ServerSocket myServerSocket;
    static MyClient client1, client2, client3, client4;
    static boolean commenceGame = false;
    public static ArrayList<String> curplayerdeck = new ArrayList<String>();
    public static ArrayList<String> playerdeck4 = new ArrayList<String>();
    public static ArrayList<String> playerdeck1 = new ArrayList<String>();
    public static ArrayList<String> playerdeck2 = new ArrayList<String>();
    public static ArrayList<String> playerdeck3 = new ArrayList<String>();
    public static ArrayList<Integer> roundscore = new ArrayList<Integer>();

    static String[] deck = {"AS", "2S", "3S", "4S", "5S", "6S", "7S", "8S", "9S", "10S", "KS", "QS", "JS", "AD", "2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D", "10D", "KD", "QD", "JD", "AH", "2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "10H", "KH", "QH", "JH", "AC", "2C", "3C", "4C", "5C", "6C", "7C", "8C", "9C", "10C", "KC", "QC", "JC",};
    static String[] shuffledeck = new String[52];

    public static void main(String[] args) {
        // TODO code application logic here
        boolean playerflag = true;
        shuffledeck = shuffleArray(deck);
        ClientThread.playerCards();
        MyClient.currentMoveClientID = 1;
        try {
            myServerSocket = new ServerSocket(port);

            while (true) {

                // Client 1 connect
                System.err.println("Waiting for first client ... ");
                client1 = new MyClient(1, myServerSocket.accept());
                ClientThread client1Thread = new ClientThread(client1);
                client1Thread.start();

                // Client 2 connect
                System.err.println("Waiting for second client ... ");
                client2 = new MyClient(2, myServerSocket.accept());
                ClientThread client2Thread = new ClientThread(client2);
                client2Thread.start();

                System.err.println("Waiting for Third client ... ");
                client3 = new MyClient(3, myServerSocket.accept());
                ClientThread client3Thread = new ClientThread(client3);
                client3Thread.start();

                System.err.println("Waiting for fourth client ... ");
                client4 = new MyClient(4, myServerSocket.accept());
                ClientThread client4Thread = new ClientThread(client4);
                client4Thread.start();

                System.out.println("Commensing game !! ");
                commenceGame = true;
                playerflag = true;

            }

        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(0);
        }
    }

    public static String[] shuffleArray(String[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;

    }

}

class ClientThread extends Thread {

    MyClient client;
    PrintWriter outputWriter;
    BufferedReader inputReader;

    public ClientThread(MyClient client) {
        this.client = client;
        try {
            outputWriter = new PrintWriter(client.clientSocket.getOutputStream());
            inputReader = new BufferedReader(new InputStreamReader(client.clientSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        boolean flg = true;
        boolean flagg = true;
        int x = 0;
        boolean cardflg = true;
        int temp = 0;
        System.out.println(" Started thread for player - " + client.clientID);
        if (client.clientID == 1) {
            Card.curplayerdeck.clear();
            for (int i = 0; i < Card.playerdeck1.size(); i++) {
                Card.curplayerdeck.add(i, Card.playerdeck1.get(i));
            }
            outputWriter.println("wait-client");
            outputWriter.flush();
        }
        try {
            while (!Card.commenceGame) {
                Thread.sleep((long) 0.13);
            }
            // All clients connected
            outputWriter.println("Players 1 and 3 are Teams ");
            outputWriter.println("Players 2 and 4 are Teams ");

            
            while (Card.gameflag) {

                  if (MyClient.currentMoveClientID == client.clientID) {
                   
                    while (flg) {
                        outputWriter.println("Round 1   \n       enter-bid");
                        outputWriter.flush();
                        String data = inputReader.readLine();
                        if (Integer.parseInt(data) > 13 || Integer.parseInt(data) < 0) {
                            outputWriter.println("set-error");
                            outputWriter.flush();
                        } else {
                            int bii = Integer.parseInt(data);
                            MyClient.teamBid(client.clientID, bii);
                            flg = false;
                        }
                    }
                     outputWriter.println("deck" + Card.curplayerdeck);
                    outputWriter.println("your-turn               " + Card.currcard);
                    outputWriter.flush();

                    while (cardflg) {
                        String card = inputReader.readLine();
                        if (Card.firstplayer == client.clientID) {
                            if (card.contains("S")) {
                                Card.currcard = "S";
                            } else if (card.contains("C")) {
                                Card.currcard = "C";
                            } else if (card.contains("H")) {
                                Card.currcard = "H";
                            } else if (card.contains("D")) {
                                Card.currcard = "D";
                            }
                        }

                        if (client.clientID != Card.firstplayer) {
                            for (int i = 0; i < Card.curplayerdeck.size(); i++) {

                                if ((Card.curplayerdeck.get(i).contains(Card.currcard)) && (!card.contains(Card.currcard))) {
                                    temp = 1;
                                    break;
                                } else if (!Card.curplayerdeck.get(i).contains(Card.currcard)) {
                                    temp = 0;
                                }
                            }
                        }

                        if (temp != 1 && Card.curplayerdeck.contains(card)) {
                            Card.roundscore.add(score(card));
                            Card.turncount++;
                            Card.trickround++;
                            remove(card);
                            cardflg = false;
                        } else {
                            outputWriter.println("card-error");
                            outputWriter.flush();
                            temp = 0;
                        }

                    }
                    cardflg = true;
        
                      
                    
                    if (Card.trickround == 4) {
                     while(flagg){
                            outputWriter.println("Team A total Bid is :  "+MyClient.returnBid("A") +"Team B total Bid is :  "+MyClient.returnBid("B"));
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                        flagg = false;
                     }        
                        countscore();
                        if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                            Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false;
                        }
                        Card.curplayerdeck = player(Card.curplayerdeck, client.clientID);
                        if (Card.position == 1) {
                            Card.curplayerdeck.addAll(Card.playerdeck1);
                        } else if (Card.position == 2) {
                            Card.curplayerdeck.addAll(Card.playerdeck2);
                        } else if (Card.position == 3) {
                            Card.curplayerdeck.addAll(Card.playerdeck3);
                        } else if (Card.position == 4) {
                            Card.curplayerdeck.addAll(Card.playerdeck4);
                        }
                        MyClient.currentMoveClientID = Card.position;
                        Card.firstplayer = Card.position;
                        Card.trickround = 0;
                        Card.currcard = "";
                        if (Card.turncount == 52 || (Card.curplayerdeck.isEmpty())) {
                            Card.gameround++;
                            System.out.println(" Game Round  "+Card.gameround);
                          outputWriter.println("Game Round");
                           outputWriter.println( Card.gameround);
                            outputWriter.flush();
                            
                            totalscore();
                            ClientThread.playerCards();
                            if (client.clientID == 1) {
                                x = 2;
                            }
                            if (client.clientID == 2) {
                                x = 3;
                            }
                            if (client.clientID == 3) {
                                x = 4;
                            }
                            if (client.clientID == 4) {
                                x = 1;
                            }
                            Card.firstplayer = x;
                            client.clientID = x;
                            Card.turncount = 0;
                            MyClient.currentMoveClientID = x;
                            if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                        Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false;
                            }
                        }

                    } else {

                        if (client.clientID == 1) {
                            if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                                Card.win = check();
                                Card.gameflag = false;
                            }
                            Card.playerdeck1.clear();
                            Card.playerdeck1.addAll(Card.curplayerdeck);
                            Card.curplayerdeck.clear();
                            Card.curplayerdeck.addAll(Card.playerdeck2);

                            MyClient.currentMoveClientID = 2;

                            if (Card.turncount == 52 || (Card.curplayerdeck.isEmpty())) {
                                Card.shuffledeck = Card.shuffleArray(Card.deck);
                                Card.gameround++;
                                ClientThread.playerCards();
                                totalscore();
                                Card.firstplayer = 2;
                                client.clientID = 2;
                                Card.turncount = 0;
                                MyClient.currentMoveClientID = 2;
                                if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                                Card.win = check();
                                Card.gameflag = false;
                                }
                            }

                        } else if (client.clientID == 2) {
                            if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                            Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false; }

                            Card.playerdeck2.clear();
                            Card.playerdeck2.addAll(Card.curplayerdeck);
                            Card.curplayerdeck.clear();
                            Card.curplayerdeck.addAll(Card.playerdeck3);
                            MyClient.currentMoveClientID = 3;
                            Card.firstplayer = Card.position;

                            if (Card.turncount == 52 || (Card.curplayerdeck.isEmpty())) {
                                Card.shuffledeck = Card.shuffleArray(Card.deck);
                                ClientThread.playerCards();
                                totalscore();
                                Card.gameround++;
                                Card.firstplayer = 3;
                                client.clientID = 3;
                                MyClient.currentMoveClientID = 3;
                                Card.turncount = 0;
                                if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                        Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false;
                                }
                            }
                        } else if (client.clientID == 3) {
                            if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                        Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false;
                            }
                            Card.playerdeck3.clear();
                            Card.playerdeck3.addAll(Card.curplayerdeck);
                            Card.curplayerdeck.clear();
                            Card.curplayerdeck.addAll(Card.playerdeck4);
                            Card.firstplayer = 4;

                            MyClient.currentMoveClientID = 4;

                            if (Card.turncount == 52 || (Card.curplayerdeck.isEmpty())) {
                                Card.shuffledeck = Card.shuffleArray(Card.deck);
                                ClientThread.playerCards();
                                totalscore();
                                Card.firstplayer = 4;
                                client.clientID = 4;
                                Card.gameround++;
                                MyClient.currentMoveClientID = 4;
                                Card.turncount = 0;
                                if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                        Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false;
                                }
                            }

                        } else if (client.clientID == 4) {
                            totalscore();
                            if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                            Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false; }
                            MyClient.currentMoveClientID = 1;
                            Card.playerdeck4.clear();
                            Card.playerdeck4.addAll(Card.curplayerdeck);
                            Card.curplayerdeck.clear();
                            Card.curplayerdeck.addAll(Card.playerdeck1);

                            if (Card.turncount == 52 || (Card.curplayerdeck.isEmpty())) {
                                totalscore();
                                Card.shuffledeck = Card.shuffleArray(Card.deck);
                                ClientThread.playerCards();
                                client.clientID = 1;
                                MyClient.currentMoveClientID = 1;
                                Card.gameround++;
                                Card.firstplayer = 1;
                                Card.turncount = 0;
                                if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {
                        Card.win = check();
                            outputWriter.println("Game Over");
                           outputWriter.println( Card.win);
                            outputWriter.flush();
                            Card.gameflag = false;
                                }

                            }

                        }
                    }
                } else {
                    outputWriter.println("wait-turn");
                    outputWriter.flush();
                    while (MyClient.currentMoveClientID != client.clientID) {
                        Thread.sleep((long) 0.22);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    

    public static ArrayList player(ArrayList e, int x) {
        if (x == 1) {
            Card.playerdeck1.clear();
            Card.playerdeck1.addAll(e);
            e.clear();
        } else if (x == 2) {
            Card.playerdeck2.clear();
            Card.playerdeck2.addAll(e);
            e.clear();
        } else if (x == 3) {
            Card.playerdeck3.clear();
            Card.playerdeck3.addAll(e);
            e.clear();

        } else if (x == 4) {
            Card.playerdeck4.clear();
            Card.playerdeck4.addAll(e);
            e.clear();
        }
        return e;
    }

    public static int score(String h) {
        int len = h.length();
        String s = null;
        int score;
        if (len == 2) {
            s = h.substring(0, 1);

            if (s.equals("A")) {
                score = 14;
            } else if (s.equals("Q")) {
                score = 12;
            } else if (s.equals("J")) {
                score = 11;
            } else if (s.equals("K")) {
                score = 13;
            } else {
                score = Integer.parseInt(s);
            }
        } else {
            score = 10;
        }
        return score;
    }

    public static void playerCards() {
        int j = 0;
        int x = 1;

        String a[] = new String[Card.shuffledeck.length];
        String[] playerdeck = new String[13];
        for (int i = 0; i < Card.shuffledeck.length; i++) {
            a[i] = Card.shuffledeck[i];
        }

        while (x < 5) {
            for (int i = x - 1; i < a.length; i = i + 4) {
                playerdeck[j] = a[i];
                j++;
            }
            if (x == 1) {
                Collections.addAll(Card.playerdeck1, playerdeck);
            } else if (x == 2) {
                Collections.addAll(Card.playerdeck2, playerdeck);
            } else if (x == 3) {
                Collections.addAll(Card.playerdeck3, playerdeck);
            } else if (x == 4) {
                Collections.addAll(Card.playerdeck4, playerdeck);
            }
            x++;
            j = 0;
        }
   
    }

    public static void totalscore() {

        int teama = 0;
        int teamb = 0;
        int scorea = 0;
        int scoreb = 0;
        int finalscoreA = 0;

        int finalscoreB = 0;
        teama = MyClient.returnBid("A");
        teamb = MyClient.returnBid("B");
        scorea = MyClient.getscore("A");
        scoreb = MyClient.getscore("B");

        if (scorea >= teama) {
            finalscoreA = (10 * teama) + (scorea - teama);
        } else {
            finalscoreA = -(teama * 10);
        }

        if (scoreb >= teamb) {
            finalscoreB = (10 * teamb) + (scoreb - teamb);
        } else {
            finalscoreB = -(teamb * 10);
        }

        MyClient.finalscore(finalscoreA, finalscoreB);

    }

    public static void remove(String card) {

        Iterator<String> ite = Card.curplayerdeck.iterator();

        while (ite.hasNext()) {

            String value = ite.next();

            if (value.equalsIgnoreCase(card)) {
                ite.remove();
            } else {

            }

        }

    }

    public static void countscore() {
        int pos = Card.roundscore.indexOf(Collections.max(Card.roundscore));
        Card.position = pos + 1;
        MyClient.setscore(pos + 1);
        Card.roundscore.clear();
    }

    public static String check() {
        String result = "";
        if (MyClient.getfinalscore("A") > 250 || MyClient.getfinalscore("A") < -250 || MyClient.getfinalscore("B") > 250 || MyClient.getfinalscore("B") < -250) {

            if (MyClient.getfinalscore("A") > MyClient.getfinalscore("B")) {

                result = "A";
            } else if (MyClient.getfinalscore("A") > MyClient.getfinalscore("B")) {

                result = "B";
            } else {

                result = "Tie";
            }
        }

        return result;
    }

}
