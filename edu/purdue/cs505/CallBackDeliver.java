package edu.purdue.cs505;
public class CallBackDeliver implements BroadcastReceiver{
  public void rdeliver(Message m){
    System.out.println(m.getContents());
  }
}
