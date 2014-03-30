package edu.purdue.cs505;

public class TestBroadcastB{
  public static void main(String args[]){
    FIFOBroadcast.srbOn=true;
    FIFOBroadcast.deliveryDelay=100;
    FIFOBroadcast bc=new FIFOBroadcast();
    Process p0=new Process("192.168.1.28", 9876);
    Process p1=new Process("192.168.1.40", 9877);
    Process p2=new Process("192.168.1.41", 9878);
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
