#!/bin/bash

rm -rf bin;

mkdir bin;

javac -sourcepath ./src -d bin src/Server/StartServer.java -Xlint:unchecked;
javac -sourcepath ./src -d bin src/Client/StartClient.java -Xlint:unchecked;

cp src/GUI/*.bmp bin/GUI;


rm StartClient;
rm StartServer;

touch StartClient;
touch StartServer;

echo "#!/bin/bash
java -classpath ./bin Client.StartClient -ORBInitialPort 1050 -ORBInitialHost localhost" >> StartClient;
echo "#!/bin/bash
java -classpath ./bin Server.StartServer -ORBInitialPort 1050 -ORBInitialHost localhost" >> StartServer;

chmod +x ./StartClient;
chmod +x ./StartServer;

clear;
echo "Started orbd port 1050"
echo "Now u can run server using command ./StartServer"
echo "Now u can run client using command ./StartClient"
terminator
orbd -ORBInitialPort 1050 -ORBInitialHost localhost;
