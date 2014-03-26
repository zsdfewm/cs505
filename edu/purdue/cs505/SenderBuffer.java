package edu.purdue.cs505;

import java.util.*;

//latest_index, the index for last message;
//sended_index, the next message to send;
//pending_ack_index, the ack to send;
//confirmed_index, waiting for the confirmed

public class SenderBuffer{
  public static final int SENDING_BUFFER_SIZE=20000;
  public static final int SENDING_WINDOW_SIZE=2000;
  public String myName;
  public SortedMap<Integer, MessageWrapper> buffer;
  public int latest_index;
  public int pending_ack_index;
  public int sended_index;
  public int confirmed_index;
  public SenderBuffer(String myName){
    this.myName=myName;
    buffer=new TreeMap<Integer, MessageWrapper>();
    latest_index=-1;
    sended_index=-1;
    pending_ack_index=-1;
    confirmed_index=-1;
  }
  public synchronized void confirmed(int confirmed_index){
    if (confirmed_index==this.confirmed_index){
      //reconfirmed means lost packet. resend the request packet;
//      System.out.printf("resend [%d] confirmed[%d] this.confirmed[%d] buffered[%d]\n",sended_index, confirmed_index, this.confirmed_index, latest_index);
      sended_index=confirmed_index;
      return;
    }
    for(int i=this.confirmed_index+1;i<=confirmed_index;i++){
      this.confirmed_index++;
      buffer.remove(i);
    }
    System.out.println(myName+" confirmed: "+ confirmed_index+" size="+buffer.size());
    if (sended_index<this.confirmed_index){
      sended_index=this.confirmed_index;
    }
  }
  public synchronized void addACKJob(int pending_ack_index){
    this.pending_ack_index=pending_ack_index;
  }
  public synchronized void addSendJob(MessageWrapper m) throws SenderBufferOverflowException{
    if (this.confirmed_index+SenderBuffer.SENDING_BUFFER_SIZE>this.latest_index) {
      latest_index++;
      m.setIndex(latest_index);
      buffer.put(latest_index,m);
    }
    else{
      //Sender buffer overlow. 
      throw(new SenderBufferOverflowException(sended_index, confirmed_index, latest_index));
    }
  }
  public synchronized DataWrapper getSendJob(){
    DataWrapper data=null;
    MessageWrapper m;
    if ((sended_index>=confirmed_index+SenderBuffer.SENDING_WINDOW_SIZE) || (sended_index==latest_index)){
      sended_index=confirmed_index;
    }
    if (sended_index<latest_index){
      int packedSize=0;
      m=buffer.get(sended_index+1);
      if (m.isPacked()){
//	System.out.printf("Used packed for [%d]..[%d]\n",sended_index+1,sended_index+m.getPackedSize());
        data=m.getDataWrapper();
	sended_index+=m.getPackedSize();
      }
      else{ //try to pack data.
        data=new DataWrapper(m);
        sended_index++;
        packedSize++;
        boolean flag=true;
        while((sended_index<latest_index) && (flag)){
          MessageWrapper mNext=buffer.get(sended_index+1);
          if (data.appendMessage(mNext)){
            sended_index++;
	    packedSize++;
          }
          else{
            flag=false;
          }
        }
	m.setDataWrapper(data);
	m.setPackedSize(packedSize);
      }
    }
    if (pending_ack_index!=-1){
      if (data==null){
        data=new DataWrapper();
      }
      data.setACK(pending_ack_index);
      pending_ack_index=-1;
    }
    return data;
  }
}
