package edu.purdue.cs505;

import java.lang.*;

public class CallBackReceiver implements ReliableChannelReceiver{
  public String name;
  public CallBackReceiver(){
    name="null";
  }
  public CallBackReceiver(String name){
    this.name=name;
  }
  public void rreceive(Message m){
    System.out.println(name+": "+m.getMessageContents());
  }
}
