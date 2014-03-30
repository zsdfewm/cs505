package edu.purdue.cs505;

public class TestBroadcastA{
  public static void main(String args[]){
    FIFOBroadcast.srbOn=true;
    FIFOBroadcast.deliveryDelay=100;
    FIFOReliableBroadcast bc=new FIFOBroadcast();
    Process p0=new Process("192.168.1.28", 9876);
    Process p1=new Process("192.168.1.40", 9877);
    Process p2=new Process("192.168.1.41", 9878);
    bc.init(p0);
    bc.addProcess(p1);
    bc.addProcess(p2);
    bc.rblisten(null);
try{
    Thread.sleep(1000);
    int n=10;
    int nn=10;
    Message m;
    for(int i=0;i<n;i++){
      for(int j=0;j<nn-1;j++){
        m=new Message();
        m.setContents("A"+Integer.toString((i*nn+j)));
        bc.rbroadcast(m);
      }
      m=new Message();
      m.setContents("A"+Integer.toString((i*nn+nn-1)));
      for(int j=0;j<nn-1;j++){
        m.makesObsolete(i*nn+j);
      }
      bc.rbroadcast(m);
    }
}
catch(Exception e){
  e.printStackTrace();
}

  }
}
