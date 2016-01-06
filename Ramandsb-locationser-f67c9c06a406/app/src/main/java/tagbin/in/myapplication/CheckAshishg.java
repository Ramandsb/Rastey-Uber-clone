//package tagbin.in.myapplication;
//
//import java.util.Enumeration;
//
//package USBConnection;
//
//        import java.io.BufferedReader;
//        import java.io.IOException;
//        import java.io.InputStream;
//        import java.io.InputStreamReader;
//        import java.io.OutputStream;
//        import java.text.DateFormat;
//        import java.text.SimpleDateFormat;
//        import java.util.Date;
//        import java.util.Enumeration;
//        import java.util.TooManyListenersException;
//
//        import purejavacomm.CommPortIdentifier;
//        import purejavacomm.PortInUseException;
//        import purejavacomm.SerialPort;
//        import purejavacomm.SerialPortEvent;
//        import purejavacomm.SerialPortEventListener;
//        import purejavacomm.UnsupportedCommOperationException;
//
//public class SimpleRead  implements Runnable, SerialPortEventListener{
//
//    static CommPortIdentifier portId;
//    static Enumeration portList;
//    private String messageString = ">RTU>GET-INFO";
//    boolean writeToPort = true;
//    BufferedReader reader;
//
//    private InputStream inStream;
//    SerialPort serialPort;
//    Thread readThread;
//
//    public static void main(String[] args) {
//        portList = CommPortIdentifier.getPortIdentifiers();
//        //System.out.println(portList);
//        while (portList.hasMoreElements()) {
//            portId = (CommPortIdentifier) portList.nextElement();
//            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//                if (portId.getName().equals("COM3")) {
//                    SimpleRead reader = new SimpleRead();
//                }
//
//            }
//        }
//    }
//
//    public SimpleRead() {
//        try {
//            serialPort = (SerialPort)portId.open("SimpleReadApp", 4000);
//            System.out.println(serialPort);
//        } catch (PortInUseException e) {System.out.println("the port is already in use");}
//        try {
//            if(serialPort!= null)
//            {inStream = serialPort.getInputStream();
//                System.out.println(inStream);
//                //reader = new BufferedReader(new InputStreamReader(inStream));
//            }
//        } catch (IOException e) {System.out.println("the error is :" + e);}
//        try {
//            serialPort.addEventListener(this);
//            System.out.println("event added");
//        } catch (TooManyListenersException e) {System.out.println(e);}
//
//        serialPort.notifyOnDataAvailable(true);
//        try {
//            serialPort.setSerialPortParams(9600,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
//
//        readThread = new Thread(this);
//        readThread.start();
//    }
//
//
//    public void run() {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("what hapnd?");
//
//    }
//
//    public void serialEvent(SerialPortEvent event) {
//        switch(event.getEventType()) {
//            case SerialPortEvent.BI:
//            case SerialPortEvent.OE:
//            case SerialPortEvent.FE:
//            case SerialPortEvent.PE:
//            case SerialPortEvent.CD:
//            case SerialPortEvent.CTS:
//            case SerialPortEvent.DSR:
//            case SerialPortEvent.RI:
//            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//                break;
//            case SerialPortEvent.DATA_AVAILABLE:
//
//                try {
//                    int c;
//                    StringBuffer readBuffer = new StringBuffer();
//
//                    while(true){
//                        while ((c=inStream.read()) != 10){
//                            if(c!=13)  readBuffer.append((char) c);
//                        }
//                        String scannedInput = readBuffer.toString();
//                        System.out.println("the string is :" + scannedInput);
//
//
//                    }
//
//                } catch (IOException e) {System.out.println(e);}
//
//                break;//
//        }
//    }
//}