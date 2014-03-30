package edu.purdue.cs505;

import java.net.*;
import java.util.*;

public class UDPSender implements Runnable{
  public String myID;
  public String targetName;
  public int targetPort;
  public InetAddress targetIP;
  public HashMap<String, ReliableBuffer> channelMap;
  public DatagramSocket socket;
  public boolean workFlag;
  public UDPSender(String myID, HashMap<String, ReliableBuffer> channelMap){
   //just for now;
    this.myID=myID;
    this.channelMap=channelMap;

try{
//    this.targetIP=InetAddress.getByName(targetName);
    socket=new DatagramSocket();
}
catch(Exception e){
  e.printStackTrace();
}

  } 

  public void sendRound(){
//round robin all channelMap to getSendJob();
try{
    Set<String> procIDs=this.channelMap.keySet();
    Iterator iter = procIDs.iterator();
    String procID;
    String[] tmp;
    DataWrapper data;
    ReliableBuffer rb;
    while (iter.hasNext()){
       procID=(String)iter.next();
       rb=channelMap.get(procID);
       tmp=procID.split(":",2);
       this.targetName=tmp[0];
       this.targetIP=InetAddress.getByName(targetName);
       this.targetPort=Integer.parseInt(tmp[1]);
       data=rb.getSendJob();
       if (data!=null){
         this.send(data.getData());
       }
    }
}
catch(Exception e){
  e.printStackTrace();
}
  }
  public void send(byte[] data){
    try{
      DatagramPacket sendPacket=new DatagramPacket(data,data.length,targetIP, targetPort);
      socket.send(sendPacket);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  public void run(){
    this.workFlag=true;
    while(workFlag){
//      DataWrapper data=this.getSendJob();
//      if (data!=null){
//        this.send(data.getData());
//      }
      this.sendRound();
      try{
        Thread.sleep(50);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    System.out.println(myID+" sending stops!");
  }
  public void halt(){
    this.workFlag=false;
  }
}
