package edu.purdue.cs505;

public class TestBroadcastC{
  public static void main(String args[]){
   // FIFOBroadcast.srbOn=true;
   // FIFOBroadcast.deliveryDelay=100;
    FIFOBroadcast bc=new FIFOBroadcast();
    Process p0=new Process("localhost", 9876);
    Process p1=new Process("localhost", 9877);
    Process p2=new Process("localhost", 9878);
    CallBackDeliver cb=new CallBackDeliver();
    bc.init(p2);
    bc.addProcess(p0);
    bc.addProcess(p1);
    bc.rblisten(cb);
try{
    Thread.sleep(1000);
    int n=100;
    for(int i=0;i<n;i++){
      Message m=new Message();
      m.setContents("12831 lofasd fdsa8 63423"+i);
      if (i%2==1){
        m.makesObsolete(i-1);
      }
      bc.rbroadcast(m);
    }
}
catch(Exception e){
  e.printStackTrace();
}

  }
}
