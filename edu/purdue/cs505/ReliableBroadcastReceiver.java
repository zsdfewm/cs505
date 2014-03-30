package edu.purdue.cs505;

import java.util.*;


public class ReliableBroadcastReceiver implements ReliableChannelReceiver{
  public String myID;
  public BroadcastReceiver br;
  public ChannelSender sender;
  public HashMap<String, HashSet<Integer>> indexMap;
  public ReliableBroadcastReceiver(String myID, ChannelSender sender, BroadcastReceiver br, HashMap<String, ReliableBuffer> map){
    indexMap=new HashMap<String, HashSet<Integer>>();
    Set<String> procIDs=map.keySet();
    Iterator iter = procIDs.iterator();
    String procID;
    while(iter.hasNext()){
      procID=(String)iter.next();
      HashSet<Integer> messageBag=new HashSet<Integer>();
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
      HashSet<Integer> messageBag;
      messageBag=indexMap.get(procID);
      if (messageBag.contains(m.getMessageNumber())){
//Ignore if already have it. 
      }
      else{
        messageBag.add(m.getMessageNumber());
//Send to every other guy
        Set<String> destIDs=indexMap.keySet();
        String destID;
        Iterator iter=destIDs.iterator();
        while(iter.hasNext()){
          destID=(String)iter.next();
          if (!(destID.equals(procID))){
//System.out.println(myID+">>"+destID+":"+m.getContents()+"@"+m.getMessageNumber());
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
