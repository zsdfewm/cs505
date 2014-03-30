package edu.purdue.cs505;

public class TestBroadcastA{
  public static void main(String args[]){
    ReliableBroadcast bc=new Broadcast();
    Process p0=new Process("localhost", 9876);
    Process p1=new Process("localhost", 9877);
    Process p2=new Process("localhost", 9878);
    bc.init(p0);
    bc.addProcess(p1);
    bc.addProcess(p2);
    bc.rblisten(null);
try{
    Thread.sleep(1000);
    int n=10;
    for(int i=0;i<n;i++){
      Message m=new Message();
      m.setContents("A");
      bc.rbroadcast(m);
    }
}
catch(Exception e){
  e.printStackTrace();
}

  }
}
