all:	
	javac ./edu/purdue/cs505/*.java

run:
	java edu.purdue.cs505.Channel

mtest:
	java edu.purdue.cs505.MessageWrapper

send:
	java edu.purdue.cs505.TestSend

recv:
	java edu.purdue.cs505.TestRecv
