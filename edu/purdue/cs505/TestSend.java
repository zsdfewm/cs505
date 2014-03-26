package edu.purdue.cs505;

public class TestSend{
  public static void main(String args[]){
    Channel sender=new Channel("node1", 9876);
    sender.init("localhost", 9877);
    sender.rlisten(null);
    int i;
    int n=10000;
    for(i=0;i<n;i++){
      sender.rsend(new MessageWrapper(Integer.toString(i)));
    }
    try{
      Thread.sleep(10000);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("Sender done\n");
    sender.halt();
  }
}
