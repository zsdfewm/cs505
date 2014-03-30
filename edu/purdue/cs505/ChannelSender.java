package edu.purdue.cs505;

import java.io.*;
import java.net.*;
import java.util.*;


public class ChannelSender{
  public String myID;
  public HashMap<String, ReliableBuffer> channelMap;
  public ChannelSender(String myID, HashMap<String, ReliableBuffer> channelMap){
    this.myID=myID;
    this.channelMap=channelMap;
  }
  public void sendMessageWrapper(String destID, MessageWrapper m) throws SenderBufferOverflowException{
    ReliableBuffer rb=channelMap.get(destID);
    rb.addSendJob(m);
  }
  public void sendString(String destID,String s) throws SenderBufferOverflowException {
    MessageWrapper m=new MessageWrapper(s);
    this.sendMessageWrapper(destID,m);
  }
  public void sendMessage(String destID, Message m){
try{
    MessageWrapper mw=new MessageWrapper(m.toString());
    this.sendMessageWrapper(destID,mw);
}
catch(Exception e){
  e.printStackTrace();
}
  }
}

