package edu.purdue.cs505;

import java.net.*;

public class UDPSender implements Runnable{
  public String myName;
  public String targetName;
  public int targetPort;
  public InetAddress targetIP;
  public SenderBuffer senderBuffer;
  public DatagramSocket socket;
  public boolean workFlag;
  public UDPSender(String myName, String targetName, int targetPort, SenderBuffer senderBuffer){
   //just for now;
    this.myName=myName;
    this.senderBuffer=senderBuffer;
    this.targetName=targetName;
    this.targetPort=targetPort;

try{
    this.targetIP=InetAddress.getByName(targetName);
    socket=new DatagramSocket();
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
      DataWrapper data=senderBuffer.getSendJob();
      if (data!=null){
        this.send(data.getData());
      }
      try{
        Thread.sleep(10);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    System.out.println(myName+" sending stops!");
  }
  public void halt(){
    this.workFlag=false;
  }
}
