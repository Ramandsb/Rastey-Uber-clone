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


//private static final String TAG = "TestingLog";
//    SensorManager mSensorManager;
//    Sensor mSensoracc, mSensormag, mSensorgrav;
//    //SensorEventListener myListener;
//    static float[] magval, accval, ResVec, accval1 = new float[4];
//    static float[] Orival = new float[4];
//    static float[] Ri, Ii, Ro = new float[16];
//    static double[] Angles = new double[3];
//    long currtime = 0;
//TextView orx,ory,orz;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_check_rotation);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        RegisterListeners();
//        orx= (TextView) findViewById(R.id.orx);
//        ory= (TextView) findViewById(R.id.ory);
//        orz= (TextView) findViewById(R.id.orz);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    public void shareText(String subject, String body) {
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
//        startActivity(Intent.createChooser(sharingIntent, "Share via"));
//    }
//
//    public void RegisterListeners() {
//        //Initialize the Arrays
//        Ri = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
//        Ro = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
//        Ii = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
//
//        magval = new float[]{0, 0, 0};
//        accval = new float[]{0, 0, 0, 0};
//        accval1 = new float[]{0, 0, 0, 0};
//        ResVec = new float[]{0, 0, 0, 0};
//
//
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
//            mSensorgrav = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
//            mSensorManager.registerListener(myListener, mSensorgrav, SensorManager.SENSOR_DELAY_NORMAL);
//            Log.d(TAG, "GRAVPASS");
//        } else {
//            Log.d(TAG, "GRAVFAIL");
//            // Sorry, there are no accelerometers on your device.
//            // You can't play this game.
//            if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
//                mSensoracc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//                mSensorManager.registerListener(myListener, mSensoracc, SensorManager.SENSOR_DELAY_FASTEST);
//                Log.d(TAG, "ACCEREG");
//            } else {
//                Log.d(TAG, "ACCFAIL");
//                // Sorry, there are no accelerometers on your device.
//                // You can't play this game.
//            }
//        }
//
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
//            mSensormag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//            mSensorManager.registerListener(myListener, mSensormag, SensorManager.SENSOR_DELAY_NORMAL);
//            Log.d(TAG, "GRAVPASS");
//        } else {
//            Log.d(TAG, "GRAVFAIL");
//            // Sorry, there are no accelerometers on your device.
//            // You can't play this game.
//        }
//    }
//
//    public SensorEventListener myListener = new SensorEventListener() {
//
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                //Log.d(TAG, "AccelFail");
//                //accx.setText(""+String.format("%.3f\n",event.values[0]));
//                //accy.setText(""+String.format("%.3f\n",event.values[1]));
//                //accz.setText(""+String.format("%.3f\n",event.values[2]));
//                double a = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
//                //accful.setText(String.format("%.3f\n", a));
//                System.arraycopy(event.values, 0, accval, 0, 3);
//
//            } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
//
//                //Log.d(TAG, "MagFail");
//                //magx.setText(""+String.format("%.3f\n",event.values[0]));
//                //magy.setText(""+String.format("%.3f\n",event.values[1]));
//                //magz.setText(""+String.format("%.3f\n",event.values[2]));
//                double b = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
//                //magful.setText(String.format("%.3f\n", b));
//                //Angles[0] = Math.acos(event.values[0]/b);
//                //Angles[1] = Math.acos(event.values[1]/b);
//                //Angles[2] = Math.acos(event.values[2]/b);
//                System.arraycopy(event.values, 0, accval1, 0, 3);
//            }
//            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                //Log.d(TAG, "MagFail");
//                //magx.setText(""+String.format("%.3f\n",event.values[0]));
//                //magy.setText(""+String.format("%.3f\n",event.values[1]));
//                //magz.setText(""+String.format("%.3f\n",event.values[2]));
//                double b = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));
//                //magful.setText(String.format("%.3f\n", b));
//
//                System.arraycopy(event.values, 0, magval, 0, 3);
//            }
//            boolean succ = SensorManager.getRotationMatrix(Ri, null, accval1, magval);
//
//            if (magval != null && accval != null && Ri != null) {
//                //Log.d(TAG, "Rotation"+Ri[9]);
//                //Log.d(TAG, ""+Ri.length);
//                //android.opengl.Matrix.invertM(Ii,0,Ri,0);
//                //android.opengl.Matrix.multiplyMV(ResVec, 0, Ii, 0, accval1, 0);
//                /*
//                if (currtime>100) {
//                    float timeDelta =(System.currentTimeMillis() - currtime)/1000f;
//
//                    //veloc[0] += timeDelta*accval1[0];
//                    //veloc[1] += timeDelta*accval1[1];
//                    //veloc[2] += timeDelta*accval1[2];
//                    //Log.d(TAG, ""+veloc[0]+" "+veloc[1]+" "+veloc[2]);
//                    //Log.d(TAG, ""+ResVec[0]);
//                    //distTrav[0] += (veloc[0]*timeDelta);// + ((Math.pow(timeDelta, 2) / 2) * accval1[0]);
//                    //distTrav[1] += (veloc[1]*timeDelta);// + ((Math.pow(timeDelta, 2) / 2) * accval1[1]);
//                    //distTrav[2] += (veloc[2]*timeDelta);// + ((Math.pow(timeDelta, 2) / 2) * accval1[2]);
//
//
//
//                    //magx.setText("" + String.format("%.8f\n", distTrav[0]));
//                    //magy.setText("" + String.format("%.8f\n", distTrav[1]));
//                    //magz.setText("" + String.format("%.8f\n", distTrav[2]));
//                }
//                */
//                currtime = System.currentTimeMillis();
//                SensorManager.remapCoordinateSystem(Ri, SensorManager.AXIS_X, SensorManager.AXIS_Z, Ro);
//
//                Orival = SensorManager.getOrientation(Ro, Orival);
//                //Incl = SensorManager.getInclination(Ii);
//                //magful.setText(String.format("%.3f\n", Math.toDegrees(Incl)));
//
//                orx.setText("" + String.format("%.3f\n", Math.toDegrees(Orival[0])));
//                ory.setText("" + String.format("%.3f\n", Math.toDegrees(Orival[1])));
//                orz.setText("" + String.format("%.3f\n", Math.toDegrees(Orival[2])));
//            }
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int i) {
//
//        }
//    };
//
//    public float orX(){
//        return Orival[0];
//    }
//
//    public float orY(){
//        return Orival[1];
//    }
//
//    public float orZ(){
//        return Orival[2];
//    }