package edu.purdue.cs505;

import java.util.*;


public class ReliableBroadcastReceiver implements ReliableChannelReceiver{
  public String myID;
  public BroadcastReceiver br;
  public ChannelSender sender;
  public HashMap<String, HashMap<Integer, Message>> indexMap;
  public ReliableBroadcastReceiver(String myID, ChannelSender sender, BroadcastReceiver br, HashMap<String, ReliableBuffer> map){
    indexMap=new HashMap<String, HashMap<Integer, Message>>();
    Set<String> procIDs=map.keySet();
    Iterator iter = procIDs.iterator();
    String procID;
    while(iter.hasNext()){
      procID=(String)iter.next();
      HashMap<Integer, Message> messageBag=new HashMap<Integer, Message>();
      indexMap.put(procID, messageBag);
    }
    this.sender=sender;
    this.myID=myID;
    this.br=br;
  }
  public void rreceive(ChannelMessage mw){
    Message m;
    m=new Message(mw.getMessageContents());
    if (m.getProcessID().equals(myID)){
//My message for another guy, ignore it!
    }
    else{
      String procID=m.getProcessID();
      int index=m.getMessageNumber();
      HashMap<Integer, Message> messageBag;
      messageBag=indexMap.get(procID);
      if (messageBag.containsKey(m.getMessageNumber())){
//Ignore if already have it. 
      }
      else{
        messageBag.put(m.getMessageNumber(),m);
//Send to every other guy
        Set<String> destIDs=indexMap.keySet();
        String destID;
        Iterator iter=destIDs.iterator();
        while(iter.hasNext()){
          destID=(String)iter.next();
          if (!(destID.equals(procID))){
System.out.println(myID+">>"+destID+":"+m.getContents()+"@"+m.getMessageNumber());
            sender.sendMessage(destID, m);
          }
        }
//And then deliver
        this.rdeliver(m);
      }
    }
  }
  public void rdeliver(Message m){
if (this.br!=null){
  br.rdeliver(m);
}
System.out.println(myID+" delivers: "+m.getContents()+"<<from"+m.getProcessID()+"@"+m.getMessageNumber());
  }
}
