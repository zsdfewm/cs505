package edu.purdue.cs505;

import java.util.*;

public class TestRecv{
  public static void main(String args[]){
    String myID="localhost:9877";
    int myPort=9877;
    String destID="localhost:9876";
    HashMap<String, ReliableBuffer> map=new HashMap<String, ReliableBuffer>();
    UDPSender udpSender=new UDPSender(myID, map);
    ChannelReceiver recver=new ChannelReceiver(myID,myPort,map);
    ChannelSender  sender=new ChannelSender(myID, map);
    CallBackReceiver cb=new CallBackReceiver();
    recver.rlisten(cb);
    ReliableBuffer rb=new ReliableBuffer(myID, destID);
    map.put(destID,rb);

    new Thread(recver).start();
    new Thread(udpSender).start();
    try{
      Thread.sleep(1000000);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("Recver done\n");
    recver.halt();
  }
}
