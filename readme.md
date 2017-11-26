# CORBA LITTLE INSTRUCTION

**Для компиляции файлов с IDL-описанием используются следующие команды:**

* *idlj -fall GameInterface.idl*

* *idlj -fall PrintingInterface.idl*


**Запустить на выполнение службу имен orbd  можно командой**
* *orbd -ORBInitialPort 1050 -ORBInitialHost localhost &*

**Запустить на выполнение сервер нужно командой**
* *java StartServer -ORBInitialPort 1050 -ORBInitialHost localhost &*

**Выполнить клиента нужно командой**
* *java StartClient -ORBInitialPort 1050 -ORBInitialHost localhost*

**Более подробно [ТУТ](http://www.igce.comcor.ru/Dev_bach/java+corba.html)**
