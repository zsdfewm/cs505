package edu.purdue.cs505;

import java.io.*;
import java.net.*;
import java.util.*;


public class ChannelSender{
  public String myName;
  public SenderBuffer senderBuffer;
  public String targetIP;
  public int targetPort;
  public UDPSender udpSender;
  public ChannelSender(String myName, String targetIP, int targetPort, SenderBuffer senderBuffer){
    this.myName=myName;
    this.targetIP=targetIP;
    this.targetPort=targetPort;
    this.senderBuffer=senderBuffer;
    this.udpSender=new UDPSender(myName,targetIP, targetPort,senderBuffer);
  }
  public void sendMsg(MessageWrapper m) throws SenderBufferOverflowException{
    senderBuffer.addSendJob(m);
  }
  public void sendString(String s) throws SenderBufferOverflowException {
    MessageWrapper m=new MessageWrapper(s);
    this.sendMsg(m);
  }
  public void begin(){
    new Thread(udpSender).start();
  }
  public void halt(){
    udpSender.halt();
  }
}

