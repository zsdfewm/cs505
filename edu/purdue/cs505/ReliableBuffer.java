package edu.purdue.cs505;

import java.util.*;

//latest_index, the index for last message;
//sended_index, the next message to send;
//pending_ack_index, the ack to send;
//confirmed_index, waiting for the confirmed

public class ReliableBuffer{
  public static final int SENDING_BUFFER_SIZE=20000;
  public static final int SENDING_WINDOW_SIZE=10;
  public String myID;
  public String destID;
  public SortedMap<Integer, DataWrapper> buffer;
  public int pending_ACK_index;
  public int next_index;
  public int latest_index;
  public int sended_index;
  public int confirmed_index;
  public ReliableBuffer(String myID, String destID){
    this.myID=myID;
    this.destID=destID;
    buffer=new TreeMap<Integer, DataWrapper>();
    next_index=0;
    pending_ACK_index=-1;
    latest_index=-1;
    sended_index=-1;
    confirmed_index=-1;
  }
  public synchronized boolean acceptMessage(int recv_index){
    if (this.next_index==recv_index){
      this.pending_ACK_index=this.next_index;
      this.next_index++;
      return true;
    }
    else if(recv_index>this.next_index){
      this.pending_ACK_index=this.next_index-1;
    }
    return false;
  }
  public synchronized void confirmed(int confirmed_index){
    if (confirmed_index==this.confirmed_index){
      //reconfirmed means lost packet. resend the request packet;
      System.out.printf("resend [%d] confirmed[%d] this.confirmed[%d] buffered[%d]\n",sended_index, confirmed_index, this.confirmed_index, latest_index);
      sended_index=confirmed_index;
      return;
    }
    if (confirmed_index>this.confirmed_index){
      System.out.println(myID+" confirmed: "+ confirmed_index+" size="+buffer.size());
      for(int i=this.confirmed_index+1;i<=confirmed_index;i++){
        buffer.remove(i);
        this.confirmed_index++;
      }
    }
    if (sended_index<this.confirmed_index){
      sended_index=this.confirmed_index;
    }
  }
  public synchronized void addSendJob(MessageWrapper m) throws SenderBufferOverflowException{
    if (this.confirmed_index+ReliableBuffer.SENDING_BUFFER_SIZE>this.latest_index) {
      latest_index++;
      m.setIndex(latest_index);
      DataWrapper data=new DataWrapper(myID,m);
      buffer.put(latest_index,data);
    }
    else{
      //Sender buffer overlow. 
      throw(new SenderBufferOverflowException(sended_index, confirmed_index, latest_index));
    }
  }
  public synchronized DataWrapper getSendJob(){
    DataWrapper data=null;
    if ((sended_index>=confirmed_index+ReliableBuffer.SENDING_WINDOW_SIZE) || (sended_index==latest_index)){
System.out.println("Resend Lost Data");
      sended_index=confirmed_index;
    }
    if (sended_index<latest_index){
      data=buffer.get(sended_index+1);
      sended_index=sended_index+1;
    }
    if (pending_ACK_index!=-1){
      if (data==null){
        data=new DataWrapper(myID);
      }
      data.setACK(pending_ACK_index);
      pending_ACK_index=-1;
    }
    return data;
  }
  public synchronized String getDestID(){
    return destID;
  }
}
