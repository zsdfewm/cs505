package edu.purdue.cs505;

//import java.io.*;
//import java.net.*;
//import java.lang.*;

public class Channel implements ReliableChannel, Runnable{
  public ChannelReceiver crecver;
  public ChannelSender csender;
  public UDPSender usender;
  public SenderBuffer senderBuffer;
  public String myName;
  public int listenPort;
  public String targetIP;
  public int targetPort;
  public Channel(String myName,int listenPort){
    this.myName=myName;
    this.listenPort=listenPort;
  }
  public Channel(String myName, int listenPort, String targetIP, int targetPort){
    this.myName=myName;
    this.listenPort=listenPort;
    this.targetIP=targetIP;
    this.targetPort=targetPort;
  }
  public void init(String destinationIP, int destinationPort){
    this.targetIP=destinationIP;
    this.targetPort=destinationPort;
    senderBuffer=new SenderBuffer(myName);
    crecver=new ChannelReceiver(this.myName, this.listenPort, senderBuffer);
    csender=new ChannelSender(this.myName, this.targetIP, this.targetPort, senderBuffer);
    csender.begin();
  } 
  public void rsend(Message m){
    try{
      csender.sendString(m.getMessageContents());
    }
    catch(SenderBufferOverflowException e){
      e.printIndexInfo();
    }
  }
  public void rlisten(ReliableChannelReceiver rc){
    crecver.init();
    new Thread(crecver).start();
  }
  public void halt(){
    csender.halt();
    crecver.halt();
  }
  public void run(){
    this.init(this.targetIP,this.targetPort);
    this.rlisten(crecver);
    String s;
    for(int i=0;i<100000;i++){
      s="";
      for(int j=0;j<50;j++){
        s=s+'A';
      }
      this.rsend(new MessageWrapper(s));
    }
try{
      Thread.sleep(10000);
//      this.halt();
}
catch(Exception e){
  e.printStackTrace();
}
  }
  public static void main(String args[]){
    System.out.println("Everything begins here");
    Channel node1=new Channel("node1",9876,"localhost",9877);
    Channel node2=new Channel("node2",9877,"localhost",9876);
    new Thread(node1).start();
    new Thread(node2).start();
  }
}
