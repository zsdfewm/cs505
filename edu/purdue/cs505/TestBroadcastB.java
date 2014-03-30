package edu.purdue.cs505;

public class TestBroadcastB{
  public static void main(String args[]){
    FIFOBroadcast.srbOn=true;
    FIFOBroadcast.deliveryDelay=100;
    FIFOBroadcast bc=new FIFOBroadcast();
    Process p0=new Process("localhost", 9876);
    Process p1=new Process("localhost", 9877);
    Process p2=new Process("localhost", 9878);
    bc.init(p1);
    bc.addProcess(p0);
    bc.addProcess(p2);
    bc.rblisten(null);
try{
    Thread.sleep(1000);
//    Message m=new Message();
//    m.setContents("1");
//    bc.rbroadcast(m);
}
catch(Exception e){
  e.printStackTrace();
}

  }
}
