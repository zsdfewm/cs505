package edu.purdue.cs505;

public interface ReliableChannel{
	void init(String destinationIP,int destinatinPort);
	void rsend(Message m);
	void rlisten(ReliableChannelReceiver rc);
	void halt();
}

