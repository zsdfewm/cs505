all:	
	javac ./edu/purdue/cs505/*.java
send:
	java edu.purdue.cs505.TestSend
recv:
	java edu.purdue.cs505.TestRecv

a:	
	java edu.purdue.cs505.TestBroadcastA

b:
	java edu.purdue.cs505.TestBroadcastB

c:
	java edu.purdue.cs505.TestBroadcastC
