package edu.purdue.cs505;

//import java.io.*;
//import java.net.*;
//import java.lang.*;

public class Channel implements ReliableChannel{
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
    crecver.init();
    new Thread(crecver).start();
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
//    crecver.init();
//    new Thread(crecver).start();
    crecver.callBackReceiver=rc;
  }
  public void halt(){
    csender.halt();
    crecver.halt();
  }
}
