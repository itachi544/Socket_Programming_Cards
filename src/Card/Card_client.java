/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anup Kumar
 */
public class Card_client {

    /**
     * @param args the command line arguments
     */
    static String ip = "";
    static int port = 1234;
    static Socket socket;
    static PrintWriter outputWriter;
    static BufferedReader inputReader;
    
        public static void main(String[] args){
            System.out.println("Enter The IP Address :");
            Scanner s =  new Scanner(System.in);
            ip = s.nextLine();
         try {
            try{
            socket  = new Socket(ip, port);
            }
  
catch (Exception e) {
      System.out.println("Unknown  Server ");
      System.exit(0);
}
            System.out.println(" Client App Started !");
       
            outputWriter = new PrintWriter(socket.getOutputStream());
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner inputKeyboard = new Scanner(System.in);
            
            System.out.println(" Connected to server ! ");
            
            // Start reading from server
            ClientReadThread readThread = new ClientReadThread(inputReader,outputWriter);
            readThread.start();
            
         }catch(Exception e){
             System.out.println(e);
         }
    }
    
}

class ClientReadThread extends Thread{
    
    BufferedReader inputReader;
    PrintWriter outputWriter;
    
    public ClientReadThread(BufferedReader inputReader,PrintWriter outputWriter){
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
    }
    
    @Override
    public void run(){
        String data = "null";
        while(true){
            try{
            data = inputReader.readLine();
            }
            catch(Exception e){
                System.out.println(e);        
            }
            
            if(data.equals("wait-client")){
                System.out.println("waiting for another player !");
            }
                else if(data.equals("wait-turn")){
                System.out.println("waiting for other players turn !");
            }  else  if(data.contains("your-turn")){
                System.out.println("Current Sequence is : "+data);
                System.out.println(" Enter your card  : ");
                     Scanner scan = new Scanner(System.in);
                     String s = scan.nextLine();
                    outputWriter.println(s);
                outputWriter.flush();
               
            }  else if(data.contains("enter-bid")){
                
                System.out.println(" Enter your  Bid  ");
                
                     Scanner scan = new Scanner(System.in);
                    int s = scan.nextInt();
                    if(s >13 || s<0){
                   System.out.println("Invalid input. Enter Bid less than 13 and Greater than Zero: ");
                      s = scan.nextInt();
                    }
                   String s1 = s + "";
                    outputWriter.println(s1);
                   outputWriter.flush();
            
            }  else  if(data.contains("Game Round")){
                try{
            data = inputReader.readLine();
            }
            catch(Exception e){
                System.out.println(e);        
            }
                System.out.println("  Game "+data);
            }
               else if(data.equals("set-error")){
                // send current players move127.0.0.1
                System.out.println("Invalid input. Enter Bid less than 13 and Greater than Zeroo: ");
                Scanner scan = new Scanner(System.in);
                   int s = scan.nextInt();
                    outputWriter.println(s);
                outputWriter.flush();
             }else if(data.equals("card-error")){
              
                System.out.println("Invalid input. Enter card from the deck: ");
                Scanner scan = new Scanner(System.in);
                String s = scan.nextLine();
                outputWriter.println(s);
                outputWriter.flush();
            }else if(data.equals("Game Over")){
              
                System.out.println("The Winning Team is : ");
                try{
            data = inputReader.readLine();
            }
            catch(Exception e){
                System.out.println(e);        
            }
                  System.out.println("   Congrats Team :   "+data);
            
            }else{
                 System.out.println(data);
            }
            
        }
    }
    
    
}
