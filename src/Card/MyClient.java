/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
/**
 *
 * @author Anup Kumar
 */
public class MyClient {
    public int clientID;
    public int bid;
    static   ArrayList<String> playerdeck = new ArrayList<String>();
    public String team;
    public static int flgs =1;
    public Socket clientSocket;
    public static int currentMoveClientID;
    static int teambidA = 0;
    static int teambidB = 0;
    static int teamscoreB = 0;
    static int teamscoreA = 0;
    static int finalA = 0;
    static int finalB = 0;
    
    //public static int playerflag =0;
    
    public MyClient(int clientID,Socket socket){
    this.clientID = clientID;
    this.clientSocket = socket;
    
    }
    
    public static   void setdeck(int clientID,ArrayList e){
    clientID = clientID;
    playerdeck = e;
    
    }
    
    public  ArrayList getdeck(){
    
    return playerdeck;
    }
    
    
    public static void teamBid(int clientID,int bid){
    clientID = clientID;
    bid = bid;
    if(clientID == 1 || clientID == 3){
    teambidA = teambidA + bid;
    } else{
    teambidB = teambidB + bid;
    }
    
    }
    
    public static int returnBid(String team){
    int x1 = 0;
    if(team.equals("A")){
    x1 =  teambidA;
    }else if(team.equals("B")){
    x1 =  teambidB;
    }
    return x1;
    }
    
    
    
    
    public static void finalscore(int A,int B){
    
        finalA = A;
        finalB = B;
        
    }
    
    public static int getfinalscore(String team){
    int x1 = 0;
    if(team.equals("A")){
    x1 =  finalA;
    }else if(team.equals("B")){
    x1 =  finalB;
    }
    return x1;
      
    }
    
    
 
    public static void setscore(int clientID){
    
    if(clientID == 1 || clientID == 3){
    teamscoreA = teamscoreA + 1;
    } else{
    teamscoreB = teamscoreB + 1;
    }
      
    }
    public static int  getscore(String team){
    int x1 = 0;
    if(team.equals("A")){
    x1 =  teamscoreA;
    }else if(team.equals("B")){
    x1 =  teamscoreB;
    }
    return x1;
      
    }
}
