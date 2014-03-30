package edu.purdue.cs505;

import java.util.*;

//For SRB, indexMap extends to contain my self. 


public class SRBReliableBroadcastReceiver extends ReliableBroadcastReceiver implements Runnable{
  public long delay;
  public String myID;
  public BroadcastReceiver br;
  public ChannelSender sender;
  public HashMap<String, TreeMap<Integer,Message>> indexMap;
  public HashMap<String, HashSet<Integer>> savedMap;

  public SRBReliableBroadcastReceiver(String myID, ChannelSender sender, BroadcastReceiver br, HashMap<String, ReliableBuffer> map, long delay){
    indexMap=new HashMap<String, TreeMap<Integer, Message>>();
    savedMap=new HashMap<String, HashSet<Integer>>();
    Set<String> procIDs=map.keySet();
    Iterator iter = procIDs.iterator();
    String procID;
    TreeMap<Integer, Message> messageBag;
    HashSet<Integer> savedBag;
    while(iter.hasNext()){
      procID=(String)iter.next();
      messageBag=new TreeMap<Integer,Message>();
      savedBag= new HashSet<Integer>();
      indexMap.put(procID, messageBag);
      savedMap.put(procID, savedBag);
    }
    messageBag=new TreeMap<Integer,Message>();
    savedBag=new HashSet<Integer>();
    indexMap.put(myID,messageBag);
    savedMap.put(myID,savedBag);
    this.sender=sender;
    this.myID=myID;
    this.br=br;
    this.delay=delay;
  }
  public void rreceive(ChannelMessage mw){
    Message m;
    m=new Message(mw.getMessageContents());
System.out.println(mw.getMessageContents());
    this.breceive(m);
  }
  public synchronized void addMessage(TreeMap<Integer, Message> map, int index, Message m){
//Add message and then make obsolete!
    Date t=new Date();
    m.setTimeStamp(t.getTime());
    map.put(index,m);
    int[] tmp;
    tmp=m.getObsoletedMessages();
    for(int i=0;i<tmp.length;i++){
      map.remove(tmp[i]);
    }
  }
  public void breceive(Message m){
    String procID=m.getProcessID();
    int index=m.getMessageNumber();
    TreeMap<Integer, Message> messageBag;
    HashSet<Integer> savedBag;
    messageBag=indexMap.get(procID);
    savedBag=savedMap.get(procID);
    if (savedBag.contains(m.getMessageNumber())){
//Ignore if already have it. 
    }
    else{
      this.addMessage(messageBag, m.getMessageNumber(), m);
      savedBag.add(m.getMessageNumber());

//Send to every other guy
      Set<String> destIDs=indexMap.keySet();
      String destID;
      Iterator iter=destIDs.iterator();
      while(iter.hasNext()){
        destID=(String)iter.next();
        if (!(destID.equals(procID)) && (!(destID.equals(myID)))){
//System.out.println(myID+">>"+destID+":"+m.getContents()+"@"+m.getMessageNumber());
          sender.sendMessage(destID, m);
        }
      }
    }
  }
  public void rdeliver(Message m){
if (this.br!=null){
  br.rdeliver(m);
}
System.out.println(myID+" delivers: "+m.getContents()+"<<from"+m.getProcessID()+"@"+m.getMessageNumber());
  }

  public synchronized void fetchRound(long ts){
    Set<String> procIDs=indexMap.keySet();
    Iterator<String> iter=procIDs.iterator();
    String procID;
    TreeMap<Integer, Message> messageBag;
    HashSet<Integer> savedBag;
    Message m;
    while(iter.hasNext()){
      procID=iter.next();
      messageBag=indexMap.get(procID);
      if (messageBag.size()>0) {
        m=messageBag.get(messageBag.firstKey());
//  System.out.println(m.getContents()+":"+m.getTimeStamp()+"@"+ts);
        if (m.getTimeStamp()+delay<ts){
          this.rdeliver(m);
          messageBag.remove(messageBag.firstKey());
        }
      }
    }
  }
  public void run(){
    while(true){
      Date t=new Date();
      long ts=t.getTime();
try{
      fetchRound(ts);
      Thread.sleep(10);
}
catch(Exception e){
  e.printStackTrace();
}
    }
  }
}
