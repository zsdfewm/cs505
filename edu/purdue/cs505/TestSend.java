package edu.purdue.cs505;

import java.util.*;

public class TestSend{
  public static void main(String args[]){
    String myID="localhost:9876";
    int myPort=9876;
    String destID="localhost:9877";
    HashMap<String, ReliableBuffer> map=new HashMap<String, ReliableBuffer>();
    UDPSender udpSender=new UDPSender(myID, map);
    ChannelReceiver recver=new ChannelReceiver(myID,myPort,map);
    ChannelSender  sender=new ChannelSender(myID, map);
    ReliableBuffer rb=new ReliableBuffer(myID, destID);
    map.put(destID,rb);

    new Thread(recver).start();
    new Thread(udpSender).start();
    int i;
    int n=1000;
try{
    for(i=0;i<n;i++){
      sender.sendString(destID, Integer.toString(i));
    }
    Thread.sleep(100000);
}
catch(Exception e){
  e.printStackTrace();
}
    System.out.println("Sender done\n");
    udpSender.halt();
  }
}
