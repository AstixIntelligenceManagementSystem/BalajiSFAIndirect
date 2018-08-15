package project.astix.com.balajisfaindirect;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astix.Common.CommonInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Pattern;


public class AddNewStore_DynamicSectionWise extends BaseFragmentActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,SearchListCommunicator,OnMapReadyCallback
{
    int IsComposite=0;
    int flgCheckNewOldStore=0;
    String StoreNameFromBack="";
    public static String currentBeatName="All";
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000  * 1; // 1 second
    private static final long MIN_TIME_BW_UPDATESNew = 100  * 1; // 1 second
   public static String OwnerName,StoreContactNo,StoreCatType;
    public static int flgRuleTaxVal, flgTransType;
    public static String distID="0";
    public static String fnAccurateProvider="";
    public static String fnLati="0";
    public static String fnLongi="0";
    public static Double fnAccuracy=0.0;
    public static String prvsStoreId="";
    public static String channelOptId;
    public static String address,pincode,city,state,latitudeToSave,longitudeToSave,accuracyToSave;
    public static String storeNameToShow="";
    public static int StoreTypeTradeChannel=0;
    public static int flgLocationServicesOnOff=0;
    public static int flgGPSOnOff=0;
    public static int flgNetworkOnOff=0;
    public static int flgFusedOnOff=0;
    public static int flgInternetOnOffWhileLocationTracking=0;
    public static int flgRestart=0;
    public static int flgStoreOrder=0;
    public static int ServiceWorkerDataComingFlag=0;
    public static String ServiceWorkerStoreID="";
    public static String ServiceWorkerResultSet="";
    public static int battLevel;
    public static String fetchAddress="";
    public static LinkedHashMap<String, ArrayList<String>> hmapSection_key=new LinkedHashMap<String, ArrayList<String>>();
    public static LinkedHashMap<String, String> hmapDpndtQustGrpId=new LinkedHashMap<String, String>();
    public static LinkedHashMap<String, String> hmapQuesIdandGetAnsCntrlType=new LinkedHashMap<String, String>();
    public static LinkedHashMap<String, String> hmapQuesIdValues=new LinkedHashMap<String, String>();
    public static LinkedHashMap<String, ArrayList<String>> hmapQuesGropKeySection=new LinkedHashMap<String, ArrayList<String>>();
    public static LinkedHashMap<String, String> hmapGroupId_Desc=new LinkedHashMap<String, String>();
    public static LinkedHashMap<String, ArrayList<String>> hmapSctnId_GrpId=new LinkedHashMap<String, ArrayList<String>>();
    public static String date_value="";
    public static String pickerDate="";
    public static String imei;
    public static String rID;
    public static String activityFrom="";
    public static String VisitStartTS="NA";
    public static String selStoreID="0";
    public static String FLAG_NEW_UPDATE="";
    public static String LattitudeFromLauncher="NA";
    public static String LongitudeFromLauncher="NA";
    public static String AccuracyFromLauncher="NA";
    public static String ProviderFromLauncher="NA";
    public static String GpsLatFromLauncher="NA";
    public static String GpsLongFromLauncher="NA";
    public static String GpsAccuracyFromLauncher="NA";
    public static String NetworkLatFromLauncher="NA";
    public static String NetworkLongFromLauncher="NA";
    public static String NetworkAccuracyFromLauncher="NA";
    public static String FusedLatFromLauncher="NA";
    public static String FusedLongFromLauncher="NA";
    public static String FusedAccuracyFromLauncher="NA";
    public static String fnAddressFromLauncher="NA";
    public static String AllProvidersLocationFromLauncher="NA";
    public static String GpsAddressFromLauncher="NA";
    public static String NetwAddressFromLauncher="NA";
    public static String FusedAddressFromLauncher="NA";
    public static String FusedLocationLatitudeWithFirstAttemptFromLauncher="NA";
    public static String FusedLocationLongitudeWithFirstAttemptFromLauncher="NA";
    public static String FusedLocationAccuracyWithFirstAttemptFromLauncher="NA";
    private final long startTime = 15000;
    private final long interval = 200;
    public String flgGSTCapture="1";
    public String flgGSTCompliance="0";
    public String GSTNumber="NA";
    public int flgGSTRecordFromServer=0;
     public String newfullFileName;
    public String QuestIDForOutChannel="0";
    public GetUpdateSchemeForNewStore  taskUpdateScheme=null;
    public int flgHasQuote=0;
    public int flgAllowQuotation=1;
    public int flgSubmitFromQuotation=0;
    public CoundownClass countDownTimer;
    public String OutletID="NA";
    public String StoreName="NA",fnlStoreType="0",fnlOwnerName="NA",fnlMobileNumber="NA",fnlAddressName="NA",fnSalesPersonName="NA",fnSalesPersonContactNo="NA",fnStoreCatType="NA";
    public String StoreName_tag="NA";
    public int checkSecondTaskStatus=0;
    public String FusedLocationLatitudeWithFirstAttempt="0";
    public String FusedLocationLongitudeWithFirstAttempt="0";
    public String FusedLocationAccuracyWithFirstAttempt="0";
    public String AllProvidersLocation="";
    public String FusedLocationLatitude="0";
    public String FusedLocationLongitude="0";
    public String FusedLocationProvider="";
    public String FusedLocationAccuracy="0";
    public String GPSLocationLatitude="0";
    public String GPSLocationLongitude="0";
    public String GPSLocationProvider="";
    public String GPSLocationAccuracy="0";
    public String NetworkLocationLatitude="0";
    public String NetworkLocationLongitude="0";
    public String NetworkLocationProvider="";
    public String NetworkLocationAccuracy="0";
    public AppLocationService appLocationService;
    public	GetAddingStoreInfo task=null;
    public Timer timer;
    public	MyTimerTask myTimerTask;
    public ProgressDialog pDialogGetStores;
    public int chkFlgForErrorToCloseApp=0;
    public LocationManager locationManager;
    public float Current_acc;
    public Location location;
    public ProgressDialog pDialog2STANDBY;
    public LocationListener locationListener;
    public double latitude; // latitude
    public double longitude; // longitude
    public boolean isGPSEnabled = false;
    public   boolean isNetworkEnabled = false;
    public PowerManager pm;
    public	 PowerManager.WakeLock wl;
    public float acc=0F;
    public String lastKnownLocLatitude="";
    public String lastKnownLocLongitude="";
   // public static LinkedHashMap<String, String> hmapOptionId_OptionValue=new LinkedHashMap<String, String>();
    public String accuracy="0";
    public String locationProvider="Default";
    public ProgressDialog pDialogSync;
    public  int sectionToShowHide=1;
    String allValuesOfPaymentStageID="0";
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    String fusedData;
    TextView txtAddress,txtAccuracy,txtLong,txtLat,txt_internetProb;
    RelativeLayout rl_sectionMap,rl_sectionQuest;
    String VisitEndTS;
    String CustomStringForServiceWorker="";
    String CustomStoreID="NA";
    LocationVo locVo;
    MapFragment mapFrag;
    GoogleMap googleMap;
    Button btnSubmit;
    int countSubmitClicked=0;
    String locProvider="None";
    LinearLayout ll_allData,ll_locmsg;
    AddressResultReceiver mResultReceiver;
    NewStoreForm newStore_Fragment;
    FragmentManager manager;
    FragmentTransaction fragTrans;
    ImageView img_next;
    TextView txt_Next;
    LinkedHashMap<String, String> hmapStoreQuestAnsNew = new LinkedHashMap<String, String>();
    LinkedHashMap<String, String> hmapStoreAddress = new LinkedHashMap<String, String>();
    LinearLayout ll_next,ll_back,ll_save_Exit,ll_map,ll_refresh;
    int refreshCount=0;
    RadioGroup rg_yes_no;
    RadioButton rb_yes,rb_no;
    Button btn_refresh;
    TextView txt_rfrshCmnt;
    ImageView img_exit;
    PRJDatabase helperDb=new PRJDatabase(this);
    String AddressFromLauncher="NA";
    String CityFromLauncher="NA";
    String PincodeFromLauncher="NA";
    String StateFromLauncher="NA";
    MyReceiver myReceiver;
    PRJDatabase dbengine=new PRJDatabase(this);
    DatabaseAssistant DA=new DatabaseAssistant(this);
    private boolean mIsServiceStarted = false;
    Context ctx;
    //private MyService mMyService;
    public Context getCtx() {
        return ctx;
    }
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {


            battLevel = intent.getIntExtra("level", 0);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
       /* if (mIsServiceStarted) {
            mIsServiceStarted = false;
            stopService(new Intent(AddNewStore_DynamicSectionWise.this, LocationUpdateService.class));
        }*/
        this.unregisterReceiver(this.mBatInfoReceiver);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        if(keyCode== KeyEvent.KEYCODE_HOME)
        {
            // finish();
            return true;
        }
        if(keyCode== KeyEvent.KEYCODE_MENU)
        {
            return true;
        }
        if(keyCode== KeyEvent.KEYCODE_SEARCH)
        {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstoredynamicsectionwise);
        ctx=this;
        refreshCount=0;
        channelOptId="0";
        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);
       // helperDb=new PRJDatabase(AddNewStore_DynamicSectionWise.this);
        locVo=new LocationVo();
        prvsStoreId="";

        address="";
        pincode="";
        city="";
        state="";
        latitudeToSave="";
        longitudeToSave="";
        accuracyToSave="";
        FLAG_NEW_UPDATE="";

         LattitudeFromLauncher="NA";
           LongitudeFromLauncher="NA";
         AccuracyFromLauncher="NA";

         ProviderFromLauncher="NA";
         GpsLatFromLauncher="NA";
         GpsLongFromLauncher="NA";
         GpsAccuracyFromLauncher="NA";
         NetworkLatFromLauncher="NA";
         NetworkLongFromLauncher="NA";
         NetworkAccuracyFromLauncher="NA";
         FusedLatFromLauncher="NA";
         FusedLongFromLauncher="NA";
         FusedAccuracyFromLauncher="NA";

        fnAddressFromLauncher="NA";
         AllProvidersLocationFromLauncher="NA";
         GpsAddressFromLauncher="NA";
         NetwAddressFromLauncher="NA";
         FusedAddressFromLauncher="NA";
         FusedLocationLatitudeWithFirstAttemptFromLauncher="NA";
        FusedLocationLongitudeWithFirstAttemptFromLauncher="NA";
        FusedLocationAccuracyWithFirstAttemptFromLauncher="NA";


        boolean isGPSok = false;
        boolean isNWok=false;
        VisitStartTS="";
        isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        long  syncTIMESTAMP1 = System.currentTimeMillis();
        Date dateobj1 = new Date(syncTIMESTAMP1);
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
         VisitStartTS = df1.format(dateobj1);
    //int flgCheckNewOldStore=0;
        if(!isGPSok)
        {
            isGPSok = false;
        }
        if(!isNWok)
        {
            isNWok = false;
        }
        if(!isGPSok && !isNWok)
        {
            try
            {
              //  showSettingsAlert();
            }
            catch(Exception e)
            {

            }

            isGPSok = false;
            isNWok=false;
        }
        storeNameToShow="";
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Intent extras = getIntent();
        FLAG_NEW_UPDATE=  extras.getStringExtra("FLAG_NEW_UPDATE");
        activityFrom=extras.getStringExtra("activityFrom");
        if(FLAG_NEW_UPDATE.equals("UPDATE"))
        {
            selStoreID=  extras.getStringExtra("StoreID");
            StoreName=    extras.getStringExtra("StoreName");
            storeNameToShow=StoreName;
            // currentBeatName="All";
            currentBeatName= extras.getStringExtra("CurrntRouteName");
        }
        else
        {
            currentBeatName= extras.getStringExtra("CurrntRouteName");

            flgCheckNewOldStore=1;
            selStoreID=genTempID();
            StoreNameFromBack= extras.getStringExtra("StoreName");
            if(StoreNameFromBack.equals("NA") || StoreNameFromBack.equals(""))
            {
                StoreNameFromBack="";
            }

        }
       /* Intent extras = getIntent();
        FLAG_NEW_UPDATE=  extras.getStringExtra("FLAG_NEW_UPDATE");
        if(FLAG_NEW_UPDATE.equals("UPDATE"))
        {
            selStoreID=  extras.getStringExtra("StoreID");
            StoreName=    extras.getStringExtra("StoreName");
            storeNameToShow=StoreName;
        }
        else
        {
            flgCheckNewOldStore=1;
            selStoreID=genTempID();

        }


        if(extras !=null)
        {


            date_value="28-Jun-2017";
            pickerDate= "28-Jun-2017";
            //imei="123";
            rID="1";
        }*/


       /* Intent extras = getIntent();
        if(extras !=null)
        {
            StoreName="NA";
            selStoreID=extras.getStringExtra("storeID");
            if(selStoreID.equals("0"))
            {
                flgCheckNewOldStore=1;
                selStoreID=genTempID();
            }
            activityFrom=extras.getStringExtra("activityFrom");
            date_value=extras.getStringExtra("userdate");
            pickerDate= extras.getStringExtra("pickerDate");
            imei=extras.getStringExtra("imei");
            rID=extras.getStringExtra("rID");
        }
*/

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();


        if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
        {
            imei = tManager.getDeviceId();
            CommonInfo.imei=imei;
        }
        else
        {
            imei= CommonInfo.imei.trim();
        }
        checkHighAccuracyLocationMode(AddNewStore_DynamicSectionWise.this);
        ////helperDb.open();
      String allLoctionDetails=  helperDb.getLocationDetails();
        //helperDb.close();

        prvsStoreId=helperDb.getPreviousStoreId();

        if(helperDb.fnCheckIfStoreIDExistsIn_tblStoreDeatils(selStoreID)==0)
        {
             LattitudeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[0];
               LongitudeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[1];
             AccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[2];
               AddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[3];
             CityFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[4];
               PincodeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[5];
             StateFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[6];

            ProviderFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[7];
            GpsLatFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[8];
            GpsLongFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[9];
            GpsAccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[10];
            NetworkLatFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[11];
            NetworkLongFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[12];
            NetworkAccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[13];
            FusedLatFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[14];
            FusedLongFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[15];
            FusedAccuracyFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[16];

            AllProvidersLocationFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[17];
            GpsAddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[18];
            NetwAddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[19];
            FusedAddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[20];
            FusedLocationLatitudeWithFirstAttemptFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[21];
            FusedLocationLongitudeWithFirstAttemptFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[22];
            FusedLocationAccuracyWithFirstAttemptFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[23];



          /*  myReceiver = new MyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LocationUpdateService.MY_ACTION);
            registerReceiver(myReceiver, intentFilter);
            startService(new Intent(this, LocationUpdateService.class));
            mIsServiceStarted = true;
*/
        }
        else
        {


            ArrayList<String> arrBasisDetailsAgainstStore=    helperDb.fnGetDetails_tblStoreDeatils(selStoreID);


            allValuesOfPaymentStageID =  arrBasisDetailsAgainstStore.get(1);//bydefalt "0"
            AddressFromLauncher = arrBasisDetailsAgainstStore.get(2);////bydefalt "NA"
            CityFromLauncher =  arrBasisDetailsAgainstStore.get(3);
            PincodeFromLauncher =  arrBasisDetailsAgainstStore.get(4);
            StateFromLauncher =  arrBasisDetailsAgainstStore.get(5);
            LattitudeFromLauncher = arrBasisDetailsAgainstStore.get(6);
            LongitudeFromLauncher = arrBasisDetailsAgainstStore.get(7);
            AccuracyFromLauncher = arrBasisDetailsAgainstStore.get(8);







        }

        address=AddressFromLauncher;
        pincode=PincodeFromLauncher;
        city=CityFromLauncher;
        state=StateFromLauncher;
        latitudeToSave=LattitudeFromLauncher;
        longitudeToSave=LongitudeFromLauncher;
        accuracyToSave=AccuracyFromLauncher;


		/*  PackageManager m = getPackageManager();
		    if (m.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {

		    	m.setApplicationEnabledSetting(PackageManager.FEATURE_LOCATION,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
		    }*/
        mResultReceiver = new AddressResultReceiver(new Handler());
     //   ll_allData=(LinearLayout) findViewById(R.id.ll_allData);

        ll_back=(LinearLayout) findViewById(R.id.ll_back);
        ll_next=(LinearLayout) findViewById(R.id.ll_next);
        ll_save_Exit=(LinearLayout) findViewById(R.id.ll_save_Exit);
        img_next=(ImageView) findViewById(R.id.img_next);
        img_exit=(ImageView) findViewById(R.id.img_exit);
        txt_Next=(TextView) findViewById(R.id.txt_Next);
        ll_map=(LinearLayout) findViewById(R.id.ll_map);
        rg_yes_no= (RadioGroup) findViewById(R.id.rg_yes_no);
        rb_yes= (RadioButton) findViewById(R.id.rb_yes);
        rb_no=(RadioButton)findViewById(R.id.rb_no);
         btn_refresh= (Button) findViewById(R.id.btn_refresh);
        txt_rfrshCmnt= (TextView) findViewById(R.id.txt_rfrshCmnt);
        ll_refresh= (LinearLayout) findViewById(R.id.ll_refresh);
      //  rl_sectionQuest=(RelativeLayout) findViewById(R.id.rl_sectionQuest);


        fillHmapData();
        addFragment();
        rg_yes_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i!=-1)
                {
                    RadioButton radioButtonVal = (RadioButton) radioGroup.findViewById(i);
                    if(radioButtonVal.getId()==R.id.rb_yes)
                    {
                        ll_refresh.setVisibility(View.GONE);

                    }
                    else if(radioButtonVal.getId()==R.id.rb_no)
                    {
                        ll_refresh.setVisibility(View.VISIBLE);


                    }
                }

            }
        });
        btn_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (mIsServiceStarted)
                {
                    mIsServiceStarted = false;
                    stopService(new Intent(AddNewStore_DynamicSectionWise.this, LocationUpdateService.class));
                }*/
                boolean isGPSok = false;
                boolean isNWok=false;
                isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(!isGPSok && !isNWok)
                {
                    try
                    {
                        showSettingsAlert();
                    }
                    catch(Exception e)
                    {

                    }
                    isGPSok = false;
                    isNWok=false;
                }
                else
                {

                    manager= getFragmentManager();
                    mapFrag = (MapFragment)manager.findFragmentById(
                            R.id.map);
                   // mapFrag.getMapAsync(AddNewStore_DynamicSectionWise.this);
                    mapFrag.getView().setVisibility(View.GONE);
                   /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.hide(mapFrag);*/
                    locationRetrievingAndDistanceCalculating();
                }


                refreshCount++;
                if(refreshCount==1)
                {
                    txt_rfrshCmnt.setText(getString(R.string.second_msg_for_map));
                }
                else if(refreshCount==2)
                {
                    txt_rfrshCmnt.setText(getString(R.string.third_msg_for_map));
                    btn_refresh.setVisibility(View.GONE);
                }
                rg_yes_no.clearCheck();
                ll_refresh.setVisibility(View.GONE);

            }
        });

        img_exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //helperDb.fnDeletesaveNewOutletFromOutletMstr(selStoreID);
               /* if (mIsServiceStarted) {
                    mIsServiceStarted = false;
                    stopService(new Intent(AddNewStore_DynamicSectionWise.this, LocationUpdateService.class));
                }*/
                getSectionNextOrBack(4, sectionToShowHide);
                NewStoreForm recFragment = (NewStoreForm) getFragmentManager().findFragmentByTag("NewStoreFragment");
                StoreName = recFragment.currentStoreName;

            }
        });

        ll_save_Exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //
                getSectionNextOrBack(2, sectionToShowHide);
                NewStoreForm recFragment = (NewStoreForm) getFragmentManager().findFragmentByTag("NewStoreFragment");
                StoreName = recFragment.currentStoreName;



              /*  if (mIsServiceStarted) {
                    mIsServiceStarted = false;
                    stopService(new Intent(AddNewStore_DynamicSectionWise.this, LocationUpdateService.class));
                }
*/


            }
        });
      /*  btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showSubmitConfirm();

            }
        });*/

        ll_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                hideSoftKeyboard(v);

                if(sectionToShowHide<hmapSctnId_GrpId.size()-1)
                {
                    boolean isNextMoved=getSectionNextOrBack(0,sectionToShowHide );
                    if(isNextMoved)
                    {
                        if(ll_map.getVisibility()== View.VISIBLE)
                        {
                            ll_map.setVisibility(View.GONE);
                        }
                        sectionToShowHide++;
                        NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                        StoreName=recFragment.currentStoreName;
                        if(sectionToShowHide!=1 )
                        {
                            if(ll_back.getVisibility()== View.INVISIBLE)
                            {
                                ll_back.setVisibility(View.VISIBLE);
                            }


                        }
                    }

                }
                else if(sectionToShowHide>hmapSctnId_GrpId.size()-1)
                {


                    //NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                    NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                   if(recFragment!=null)
                   {

                      /* if(channelOptId.equals("0-2-80") || channelOptId.equals("0-3-80"))
                       {
                           if(recFragment.validate() && recFragment.validatePaymentStageID())
                           {
                               showSubmitConfirm();
                           }
                       }
                      else
                       {*/
                           if(recFragment.validate() && recFragment.validateNameFilled())
                           {
                               StoreName=recFragment.currentStoreName;
                               showSubmitConfirm();
                                    //28.4866451,77.1022041,10.0
                              // 28.4866037     77.1021193   24.066999435424805
                             //  if(!checkLastFinalLoctionIsRepeated("28.4866037","77.1021193","24.066999435424805"))
                             /*if(!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher))
                               {
                                   fnCreateLastKnownFinalLocation(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher);
                                   showSubmitConfirm();
                               }
                               else
                               {
                                   if(countSubmitClicked==0)
                                   {
                                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewStore_DynamicSectionWise.this);
                                       // Setting Dialog Title
                                       alertDialog.setTitle("Information");
                                       alertDialog.setIcon(R.drawable.error_info_ico);
                                       alertDialog.setCancelable(false);
                                       // Setting Dialog Message
                                       alertDialog.setMessage("Your current location is same as previous, so please turn off your location services then turn on, it back again.");

                                       // On pressing Settings button
                                       alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                               startActivity(intent);
                                           }
                                       });

                                       // Showing Alert Message
                                       alertDialog.show();
                                   }
                                   else
                                   {
                                       locationRetrievingAndDistanceCalculating();
                                   }
                                   countSubmitClicked++;
                               }*/


                           }
                      // }

                   }

                 /*   if(recFragment.validate() && recFragment.validatePaymentStageID())
                    {

                       // getSectionNextOrBack(5,sectionToShowHide );


                    }*/




                }

                else
                {
                    NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                    StoreName=recFragment.currentStoreName;
                    boolean isNextMoved=getSectionNextOrBack(0,sectionToShowHide );
                    if(isNextMoved)
                    {
                        if(ll_map.getVisibility()== View.VISIBLE)
                        {
                            ll_map.setVisibility(View.GONE);
                        }
                        if(sectionToShowHide==hmapSctnId_GrpId.size()-1)
                        {
                            sectionToShowHide++;
                            img_next.setImageResource(R.drawable.done);
                            txt_Next.setText(getText(R.string.txtDone));

                        }

                        if(ll_back.getVisibility()== View.INVISIBLE)
                        {
                            ll_back.setVisibility(View.VISIBLE);
                        }


                    }
                }


            }
        });

        ll_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sectionToShowHide--;
                getSectionNextOrBack(1,sectionToShowHide );
                NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                StoreName=recFragment.currentStoreName;




                if(sectionToShowHide==1)
                {
                    if(ll_back.getVisibility()== View.VISIBLE)
                    {
                        ll_back.setVisibility(View.INVISIBLE);
                        if(ll_map.getVisibility()== View.GONE)
                        {
                            ll_map.setVisibility(View.VISIBLE);
                        }
                    }

                }
                if(sectionToShowHide<hmapSctnId_GrpId.size())
                {
                    img_next.setImageResource(R.drawable.next);
                    txt_Next.setText(getText(R.string.lastvisitdetails_next));
                }

            }
        });
        ll_map.setVisibility(View.VISIBLE);
        manager= getFragmentManager();
        mapFrag = (MapFragment)manager.findFragmentById(
                R.id.map);
        mapFrag.getMapAsync(AddNewStore_DynamicSectionWise.this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(mapFrag);



        if(sectionToShowHide==hmapSctnId_GrpId.size())
        {

            img_next.setImageResource(R.drawable.done);
            txt_Next.setText(getText(R.string.txtDone));
            sectionToShowHide++;

        }
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean getSectionNextOrBack(int isNextPressed,int sectionToShowOrHide)
    {
        boolean isNextMoved=false;
        NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
        if(null != recFragment)
        {
            isNextMoved=recFragment.nextOrBackSection(isNextPressed,sectionToShowOrHide);
        }
        return isNextMoved;
    }
    private void addFragment() {

        newStore_Fragment=new NewStoreForm();
        manager=getFragmentManager();
        fragTrans=manager.beginTransaction();
        fragTrans.replace(R.id.fragmentForm, newStore_Fragment,"NewStoreFragment");

        fragTrans.commit();

    }
    private void fillHmapData() {
        //QuestID,QuestCode,QuestDesc,QuestType,AnsControlType,AnsControlInputTypeID,AnsControlInputTypeMaxLength,AnsMustRequiredFlg,QuestBundleFlg,ApplicationTypeID,Sequence,AnsControlInputTypeMinLength,AnsHint,QuestBundleGroupId
        //hmapQuesIdValues.put("1^2", "1^")

        SharedPreferences sPref=this.getSharedPreferences("LanguagePref", MODE_PRIVATE);


        hmapQuesIdValues=helperDb.fnGetQuestionMstr(sPref);
        hmapQuesGropKeySection=helperDb.fnGetQuestionMstrKey();
        hmapGroupId_Desc=helperDb.getGroupDescription();
        hmapSctnId_GrpId=helperDb.fnGetGroupIdMpdWdSectionId();
        hmapDpndtQustGrpId=helperDb.fnGetDependentQuestionMstr();
        hmapSection_key=helperDb.fnGetSection_Key();
        //helperDb.open();
        channelOptId=helperDb.getChannelGroupIdOptId();
        //helperDb.close();
     //   hmapOptionId_OptionValue=helperDb.fnGetOptionId_OptionValue();
        QuestIDForOutChannel=helperDb.fnGetQuestIDForOutChannelFromQuestionMstr();

    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(getText(R.string.genTermInformation));
        alertDialog.setIcon(R.drawable.error_info_ico);
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(getText(R.string.genTermGPSDisablePleaseEnable));

        // On pressing Settings button
        alertDialog.setPositiveButton(getText(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    public void showSubmitConfirm()
    {

        AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(AddNewStore_DynamicSectionWise.this);
        alertDialogSubmitConfirm.setTitle(getText(R.string.genTermInformation));
        alertDialogSubmitConfirm.setMessage(getText(R.string.SubmitStrInfo));
        alertDialogSubmitConfirm.setCancelable(false);

        alertDialogSubmitConfirm.setNeutralButton(getText(R.string.AlertDialogYesButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                sectionToShowHide--;

                dialog.dismiss();







               NewStoreForm recFragment = (NewStoreForm) getFragmentManager().findFragmentByTag("NewStoreFragment");
                if (null != recFragment)
                {
                    recFragment.saveDynamicQuesAns(true);
                    hmapStoreQuestAnsNew = recFragment.hmapAnsValues;
                    hmapStoreAddress=recFragment.hmapAddress;
                    fnlStoreType=recFragment.currentStoreType;
                    fnlOwnerName=recFragment.currentOwnerName;
                    fnlMobileNumber=recFragment.currentMobileNumber;
                    fnlAddressName=recFragment.currentAddressName;
                    fnSalesPersonName=recFragment.currentSalesPersonName;
                    fnSalesPersonContactNo=recFragment.currentSalesPersonContactNo;
                    fnStoreCatType=recFragment.currentStoreCatType;
                    if(!TextUtils.isEmpty(recFragment.distBId))
                    {
                        distID=recFragment.distBId;
                    }

                }



                if(ProviderFromLauncher.equals("Fused"))
                {
                    fnAddressFromLauncher=FusedAddressFromLauncher;
                }
                else if(ProviderFromLauncher.equals("Gps"))
                {
                    fnAddressFromLauncher=GpsAddressFromLauncher;
                }
                else if(ProviderFromLauncher.equals("Network"))
                {
                    fnAddressFromLauncher=NetwAddressFromLauncher;
                }


                helperDb.saveLatLngToTxtFile(selStoreID,LattitudeFromLauncher, LongitudeFromLauncher,AccuracyFromLauncher,ProviderFromLauncher,GpsLatFromLauncher,GpsLongFromLauncher,GpsAccuracyFromLauncher,NetworkLatFromLauncher,NetworkLongFromLauncher,NetworkAccuracyFromLauncher,FusedLatFromLauncher,FusedLongFromLauncher,FusedAccuracyFromLauncher,3,"0",fnAddressFromLauncher,AllProvidersLocationFromLauncher,GpsAddressFromLauncher,NetwAddressFromLauncher,FusedAddressFromLauncher,FusedLocationLatitudeWithFirstAttemptFromLauncher
                        ,FusedLocationLongitudeWithFirstAttemptFromLauncher,FusedLocationAccuracyWithFirstAttemptFromLauncher);
                String lastprvsStoreId=helperDb.PrvsStoreMsgShownAndRestrtDone();
                if(!TextUtils.isEmpty(lastprvsStoreId.trim()))
                {
                   helperDb.updateCurrentStoreId(selStoreID,lastprvsStoreId);
                }
                long syncTIMESTAMP = System.currentTimeMillis();
                Date datefromat = new Date(syncTIMESTAMP);
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                String VisitEndFinalTS = df.format(datefromat);

                    helperDb.insertRestartStoreInfo(selStoreID,selStoreID,"2","0","0",3,VisitEndFinalTS);





                try
                {
                    if(isOnline())
                    {

                        addStoreOffline();
                        /*if (timer!=null)
                        {
                            timer.cancel();
                            timer = null;
                        }

                        timer = new Timer();
                        myTimerTask = new MyTimerTask();
                        timer.schedule(myTimerTask,45000);

                        try
                        {

                            task= new GetAddingStoreInfo(AddNewStore_DynamicSectionWise.this);
                            task.execute();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/
                    }
                    else
                    {

                        addStoreOffline();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    //System.out.println("onGetStoresForDayCLICK: Exec(). EX: "+e);
                }



            }
        });

        alertDialogSubmitConfirm.setNegativeButton(getText(R.string.AlertDialogNoButton), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);

        AlertDialog alert = alertDialogSubmitConfirm.create();

        alert.show();

    }


    public void addStoreOffline()
    {
        helperDb.fnsaveOutletQuestAnsMstrSectionWise(hmapStoreQuestAnsNew, 0, selStoreID);

        long syncTIMESTAMP = System.currentTimeMillis();
        Date datefromat = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);


        String VisitEndFinalTS = df.format(datefromat);
        String VisitDate = dfDate.format(datefromat);
        // int DatabaseVersion=CommonInfo.DATABASE_VERSIONID;
        int ApplicationID = CommonInfo.Application_TypeID;
        //getSectionNextOrBack(3,sectionToShowHide );

        String VisitStartTS=df.format(datefromat);
        fnGettingGSTOFflineVal();
        checkHighAccuracyLocationMode(AddNewStore_DynamicSectionWise.this);


        String selectedBeatName="";
        String slctdBeatId="0";
        String slctdBeatNodeType="0";
        LinkedHashMap<String, String> hmapStoreQuestAnsNew = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> hmapStoreAddress = new LinkedHashMap<String, String>();
        NewStoreForm recFragment = (NewStoreForm) getFragmentManager().findFragmentByTag("NewStoreFragment");
        if (null != recFragment)
        {
            recFragment.saveDynamicQuesAns(true);
            hmapStoreQuestAnsNew = recFragment.hmapAnsValues;
            hmapStoreAddress=recFragment.hmapAddress;
            selectedBeatName= recFragment.getSelectedBeatName();
            if(!selectedBeatName.equals("0") && (selectedBeatName.contains("-")))
            {
                slctdBeatId= selectedBeatName.split(Pattern.quote("-"))[1];
                slctdBeatNodeType=selectedBeatName.split(Pattern.quote("-"))[2];
            }
        }
        if (FLAG_NEW_UPDATE.equals("UPDATE")) {
            //helperDb.open();
            helperDb.UpdateStoreReturnphotoFlagSM(selStoreID, StoreName,0);
            //helperDb.close();
        } else
        {
            //helperDb.open();
            helperDb.saveTblPreAddedStores(selStoreID, StoreName, LattitudeFromLauncher, LongitudeFromLauncher, VisitDate, 1,0, 3,Integer.parseInt(slctdBeatId),Integer.parseInt(slctdBeatNodeType));
            //helperDb.close();
        }

        //helperDb.open();
        helperDb.deletetblstoreMstrOnStoreIDBasis(selStoreID);
        if((!fnlStoreType.equals("0")) && fnlStoreType.contains("-"))
        {
            fnlStoreType=fnlStoreType.split(Pattern.quote("-"))[1];
        }
                                                //RetailerName
        helperDb.savetblStoreMain("NA",selStoreID,StoreName,"NA","NA","NA","NA","NA","NA","NA","0",StoreTypeTradeChannel,
                Integer.parseInt(fnlStoreType),0,0, 0, "NA",VisitStartTS,imei,""+battLevel,3,1,String.valueOf(fnLati),String.valueOf(fnLongi),"" + fnAccuracy,"" + fnAccurateProvider,0,fnlAddressName,allValuesOfPaymentStageID,flgHasQuote,flgAllowQuotation,flgSubmitFromQuotation,flgGSTCapture,flgGSTCompliance,GSTNumber,flgGSTRecordFromServer,flgLocationServicesOnOff,flgGPSOnOff,flgNetworkOnOff,flgFusedOnOff,flgInternetOnOffWhileLocationTracking,flgRestart,flgStoreOrder, hmapStoreAddress.get("2"), hmapStoreAddress.get("1"), hmapStoreAddress.get("3"),distID,fnlOwnerName,fnlMobileNumber,fnStoreCatType,flgRuleTaxVal,flgTransType,fnlMobileNumber,fnSalesPersonName,fnSalesPersonContactNo,IsComposite);

        helperDb.saveSOAPdataStoreListDetailsInNewTable(selStoreID, hmapStoreAddress.get("2"), hmapStoreAddress.get("1"), hmapStoreAddress.get("3"),1);
        //helperDb.close();
        // new code

        try {
            File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
            if (!OrderXMLFolder.exists())
            {
                OrderXMLFolder.mkdirs();
            }
            //dbengine.open();
            String presentRoute=dbengine.GetActiveRouteID();
            //dbengine.close();


            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss",Locale.ENGLISH);

            newfullFileName=imei+"."+presentRoute+"."+df1.format(dateobj);


            DA.open();
            DA.export(CommonInfo.DATABASE_NAME, newfullFileName,presentRoute);
            DA.close();

            dbengine.savetbl_XMLfiles(newfullFileName, "3","1");
            dbengine.UpdateStorWhileAdding(selStoreID,5);

        } catch (Exception e) {

            e.printStackTrace();
            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }
        }
        // new code


        StoreSelection.flgChangeRouteOrDayEnd=0;
        DayStartActivity.flgDaySartWorking=0;
        String presentRoute = dbengine.GetActiveRouteID();
        Intent mMyServiceIntent = new Intent(getCtx(), MyService.class);
        mMyServiceIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");
        mMyServiceIntent.putExtra("storeID", selStoreID);
        mMyServiceIntent.putExtra("OrigZipFileName", newfullFileName);
        mMyServiceIntent.putExtra("whereTo", "Regular");//



        if (!isMyServiceRunning(MyService.class)) {
            startService(mMyServiceIntent);
        }
        if(activityFrom.equals("StoreSelection"))
        {
           /* Intent intent =new Intent(AddNewStore_DynamicSectionWise.this,StorelistActivity.class);
            intent.putExtra("activityFrom", "StoreSelection");
            startActivity(intent);
            finish();*/
            Intent ide=new Intent(AddNewStore_DynamicSectionWise.this,StoreSelection.class);
            ide.putExtra("userDate", date_value);
            ide.putExtra("pickerDate", pickerDate);
            ide.putExtra("imei", imei);
            ide.putExtra("rID", rID);
            AddNewStore_DynamicSectionWise.this.startActivity(ide);
            finish();

        }

        else if(activityFrom.equals("AllButtonActivity")){

            Intent intent =new Intent(AddNewStore_DynamicSectionWise.this,StorelistActivity.class);
            intent.putExtra("activityFrom", "AllButtonActivity");
            startActivity(intent);
            finish();
           /* Intent ide=new Intent(AddNewStore_DynamicSectionWise.this,AllButtonActivity.class);
            ide.putExtra("userDate", date_value);
            ide.putExtra("pickerDate", pickerDate);
            ide.putExtra("imei", imei);
            ide.putExtra("rID", rID);
            AddNewStore_DynamicSectionWise.this.startActivity(ide);
            finish();*/
        }
        else
        {
           /* Intent intent =new Intent(AddNewStore_DynamicSectionWise.this,StorelistActivity.class);
            intent.putExtra("activityFrom", "AllButtonActivity");
            startActivity(intent);
            finish();*/
            Intent ide=new Intent(AddNewStore_DynamicSectionWise.this,StoreSelection.class);
            ide.putExtra("userDate", date_value);
            ide.putExtra("pickerDate", pickerDate);
            ide.putExtra("imei", imei);
            ide.putExtra("rID", rID);
            AddNewStore_DynamicSectionWise.this.startActivity(ide);
            finish();
        }


    }

    public void addStoreOnLine()
    {


        StringTokenizer tokens = new StringTokenizer(String.valueOf(ServiceWorkerResultSet), "^");

        String StoreName= tokens.nextToken().toString().trim();
        String StoreType= tokens.nextToken().toString().trim();
        String StoreLatitude= tokens.nextToken().toString().trim();
        String StoreLongitude= tokens.nextToken().toString().trim();
        String LastVisitDate= tokens.nextToken().toString().trim();
        String LastTransactionDate= tokens.nextToken().toString().trim();
        String dateVAL= tokens.nextToken().toString().trim();
        String AutoIdStore= tokens.nextToken().toString().trim();
        String Sstat= tokens.nextToken().toString().trim();
        String IsClose= tokens.nextToken().toString().trim();
        String IsNextDat= tokens.nextToken().toString().trim();
        String RouteID= tokens.nextToken().toString().trim();
        int flgHasQuoteNew= Integer.parseInt(tokens.nextToken().toString().trim());
        int flgAllowQuotationNew= Integer.parseInt(tokens.nextToken().toString().trim());
        int flgSubmitFromQuotationNew= Integer.parseInt(tokens.nextToken().toString().trim());

        String flgGSTCapture= tokens.nextToken().toString().trim();
        String flgGSTCompliance= tokens.nextToken().toString().trim();
        String GSTNumber= tokens.nextToken().toString().trim();
        String flgGSTRecordFromServer= "1";
        String distID= tokens.nextToken().toString().trim();


        helperDb.fnDeletesaveNewOutletFromOutletMstr(selStoreID);


        String allValuesOfPaymentStageID=helperDb.fngettblNewStoreSalesQuotePaymentDetails(selStoreID);
        String VisitTypeStatus="0";
        //helperDb.open();

        helperDb.fndeleteNewStoreSalesQuotePaymentDetails(selStoreID);
       /* helperDb.saveSOAPdataStoreList(ServiceWorkerStoreID,StoreName,StoreType,Double.parseDouble(StoreLatitude),Double.parseDouble(StoreLongitude),LastVisitDate,LastTransactionDate,
                dateVAL.toString().trim(),Integer.parseInt(AutoIdStore), Integer.parseInt(Sstat),Integer.parseInt(IsClose),Integer.parseInt(IsNextDat),Integer.parseInt(RouteID),StoreTypeTradeChannel,fetchAddress,allValuesOfPaymentStageID,flgHasQuoteNew,flgAllowQuotationNew,flgSubmitFromQuotationNew,flgGSTCapture,flgGSTCompliance,GSTNumber,Integer.parseInt(flgGSTRecordFromServer),distID,"0",VisitTypeStatus);*/
        //helperDb.close();




    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }





    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        try {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        catch (SecurityException e)
        {

        }
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
    }


    @Override
    public void onLocationChanged(Location args0) {
        // TODO Auto-generated method stub
        mCurrentLocation = args0;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();

    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }
    private void updateUI() {
        Location loc =mCurrentLocation;
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            if(lat.contains("E") || lat.contains("e")){
                lat=convertExponential(mCurrentLocation.getLatitude());
            }
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            if(lng.contains("E") || lng.contains("e")){
                lng=convertExponential(mCurrentLocation.getLongitude());
            }
            FusedLocationLatitude=lat;
            FusedLocationLongitude=lng;
            FusedLocationProvider=mCurrentLocation.getProvider();
            FusedLocationAccuracy=""+mCurrentLocation.getAccuracy();
            fusedData="At Time: " + mLastUpdateTime  +
                    "Latitude: " + lat  +
                    "Longitude: " + lng  +
                    "Accuracy: " + mCurrentLocation.getAccuracy() +
                    "Provider: " + mCurrentLocation.getProvider();

        } else {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(!LattitudeFromLauncher.equals("NA") && !LattitudeFromLauncher.equals("0.0"))
        {
            googleMap.clear();
            googleMap.setMyLocationEnabled(false);
            MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(LattitudeFromLauncher), Double.parseDouble(LongitudeFromLauncher)));
            Marker locationMarker=googleMap.addMarker(marker);
            locationMarker.showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(LattitudeFromLauncher), Double.parseDouble(LongitudeFromLauncher)), 15));

        }

       else
        {
                if(refreshCount==2)
                {
                    txt_rfrshCmnt.setText(getString(R.string.loc_not_found));
                    btn_refresh.setVisibility(View.GONE);
                }
                try
                {
                    googleMap.setMyLocationEnabled(true);
                }
                catch (SecurityException e)
                {

                }

            googleMap.moveCamera(CameraUpdateFactory.zoomIn());
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    marker.setTitle(StoreName);
                }
            });

        }

    }





    public void showAlertForEveryOne(String msg)
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AddNewStore_DynamicSectionWise.this);
        alertDialogNoConn.setTitle(getText(R.string.genTermInformation));
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setNeutralButton(getText(R.string.AlertDialogOkButton),new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                long syncTIMESTAMP = System.currentTimeMillis();
                Date datefromat = new Date(syncTIMESTAMP);
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);


                String VisitEndFinalTS = df.format(datefromat);
                if(!TextUtils.isEmpty(prvsStoreId.trim()))
                {
                    helperDb.updateMsgToRestartPopUpShown(prvsStoreId,VisitEndFinalTS);
                }
               else
                {

                   helperDb.updateMsgToRestartPopUpShown(selStoreID,VisitEndFinalTS);
                }
                 finish();
            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
    }

   /* private void initializaeMap(GoogleMap googleMap) {


        googleMap.setMyLocationEnabled(true);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(StoreName);
        Marker locationMarker=googleMap.addMarker(marker);
        locationMarker.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        //}
			    *//*else
			    {
			     ft.hide(mapFrag);
			       txt_internetProb.setVisibility(View.VISIBLE);
			    }*//*
        //mapFrag.getView().setVisibility(View.GONE);

        ft.commit();
    }*/
    public String genTempID()
    {
        //store ID generation <x>

        String cxz;
        cxz = UUID.randomUUID().toString();
						/*cxz.split("^([^-]*,[^-]*,[^-]*,[^-]*),(.*)$");*/
        //System.out.println("cxz (BEFORE split): "+cxz);
        StringTokenizer tokens = new StringTokenizer(String.valueOf(cxz), "-");

        String val1 = tokens.nextToken().trim();
        String val2 = tokens.nextToken().trim();
        String val3 = tokens.nextToken().trim();
        String val4 = tokens.nextToken().trim();
        cxz = tokens.nextToken().trim();

        //System.out.println("cxz (AFTER split): "+cxz);

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tManager.getDeviceId();
        String IMEIid =  imei.substring(9);

        cxz = IMEIid +"-"+cxz+"-"+VisitStartTS.replace(" ", "").replace(":", "").trim();


        return cxz;
        //-_
    }

    public void fetchAddress()
    {


        /*Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        intent.putExtra("Reciever", mResultReceiver);
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        intent.putExtra("Location", location);
        startService(intent);*/
    }

    @Override
    public void selectedOption(String optId, String optionVal,
                               EditText txtVw, ListView listViewOption, String tagVal,
                               Dialog dialog, TextView textView,ArrayList<String> listStoreIDOrigin) {

        NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
        if(null != recFragment)
        {
            recFragment.selectedOption( optId,  optionVal,txtVw,  listViewOption,  tagVal,dialog,  textView,listStoreIDOrigin);
        }

    }

    @Override
    public void selectedStoreMultiple(String optId, String optionVal,
                                      EditText txtVw, ListView listViewOption, String tagVal,
                                      Dialog dialog, TextView textView, LinearLayout ll_SlctdOpt,
                                      ArrayList<String> listSelectedOpt,
                                      ArrayList<String> listSelectedStoreID, ArrayList<String> listSelectedStoreOrigin) {

        NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
        if(null != recFragment)
        {
            recFragment.selectedStoreMultiple( optId,  optionVal,
                    txtVw,  listViewOption,  tagVal,
                    dialog,  textView,  ll_SlctdOpt,
                    listSelectedOpt,
                    listSelectedStoreID,listSelectedStoreOrigin);
        }

    }

    public void alertSubmit()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                AddNewStore_DynamicSectionWise.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(getText(R.string.DataSent));

        // Setting Dialog Message
        alertDialog.setMessage(getText(R.string.DataSucc));

        // Setting Icon to Dialog
      //  alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton(getText(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(AddNewStore_DynamicSectionWise.this, LauncherActivity.class);
                intent.putExtra("FROM", "AddNewStore_DynamicSectionWise");
               startActivity(intent);
               finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void locationRetrievingAndDistanceCalculating()
    {

        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();


        pDialog2STANDBY = ProgressDialog.show(AddNewStore_DynamicSectionWise.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.rtrvng_loc), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(AddNewStore_DynamicSectionWise.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(AddNewStore_DynamicSectionWise.this)
                    .addOnConnectionFailedListener(AddNewStore_DynamicSectionWise.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));
        startService(new Intent(AddNewStore_DynamicSectionWise.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
        countDownTimer = new CoundownClass(startTime, interval);
        countDownTimer.start();

    }

    public String getAddressForDynamic(String latti, String longi){


        String areaToMerge="NA";
        Address addressTemp=null;
        String addr="NA";
        String zipcode="NA";
        String city="NA";
        String state="NA";
        String fullAddress="";
        StringBuilder FULLADDRESS3 =new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

           /* AddressFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[3];
            CityFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[4];
            PincodeFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[5];
            StateFromLauncher = allLoctionDetails.split(Pattern.quote("^"))[6];*/
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);
            if (addresses != null && addresses.size() > 0){
                if(addresses.get(0).getAddressLine(1)!=null){
                    addr=addresses.get(0).getAddressLine(1);

                }




                if(addresses.get(0).getLocality()!=null){
                    city=addresses.get(0).getLocality();
                    this.city=city;
                }

                if(addresses.get(0).getAdminArea()!=null){
                    state=addresses.get(0).getAdminArea();
                    this.state=state;
                }


                for(int i=0 ;i<addresses.size();i++){
                    addressTemp = addresses.get(i);
                    if(addressTemp.getPostalCode()!=null){
                        zipcode=addressTemp.getPostalCode();
                        this.pincode=zipcode;
                        break;
                    }





                }
                if(addresses.get(0).getAddressLine(0)!=null && addr.equals("NA")){
                    String countryname="NA";
                    if(addresses.get(0).getCountryName()!=null){
                        countryname=addresses.get(0).getCountryName();
                    }

                    address=  getAddressNewWay(addresses.get(0).getAddressLine(0),city,state,zipcode,countryname);
                    addr=address;
                }


                NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                if(null != recFragment)
                {
                    recFragment.setFreshAddress();
                }
            }
            else{FULLADDRESS3.append("NA");}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            return fullAddress=addr+"^"+zipcode+"^"+city+"^"+state;
        }
    }

    public void fnCreateLastKnownGPSLoction(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy)
    {

        try {

            JSONArray jArray=new JSONArray();
            JSONObject jsonObjMain=new JSONObject();


            JSONObject jOnew = new JSONObject();
            jOnew.put( "chekLastGPSLat",chekLastGPSLat);
            jOnew.put( "chekLastGPSLong",chekLastGPSLong);
            jOnew.put( "chekLastGpsAccuracy", chekLastGpsAccuracy);


            jArray.put(jOnew);
            jsonObjMain.put("GPSLastLocationDetils", jArray);

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.AppLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="GPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+ CommonInfo.AppLatLngJsonFile+"/"+txtFileNamenew;


            // If file does not exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


            FileWriter fw;
            try {
                fw = new FileWriter(file.getAbsoluteFile());

                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(jsonObjMain.toString());

                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
				 /*  file=contextcopy.getFilesDir();
				//fileOutputStream=contextcopy.openFileOutput("GPSLastLocation.txt", Context.MODE_PRIVATE);
				fileOutputStream.write(jsonObjMain.toString().getBytes());
				fileOutputStream.close();*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{

        }
    }

    public void fnCreateLastKnownFinalLocation(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy)
    {

        try {

            JSONArray jArray=new JSONArray();
            JSONObject jsonObjMain=new JSONObject();


            JSONObject jOnew = new JSONObject();
            jOnew.put( "chekLastGPSLat",chekLastGPSLat);
            jOnew.put( "chekLastGPSLong",chekLastGPSLong);
            jOnew.put( "chekLastGpsAccuracy", chekLastGpsAccuracy);


            jArray.put(jOnew);
            jsonObjMain.put("GPSLastLocationDetils", jArray);

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.FinalLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="FinalGPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+ CommonInfo.FinalLatLngJsonFile+"/"+txtFileNamenew;


            // If file does not exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


            FileWriter fw;
            try {
                fw = new FileWriter(file.getAbsoluteFile());

                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(jsonObjMain.toString());

                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
				 /*  file=contextcopy.getFilesDir();
				//fileOutputStream=contextcopy.openFileOutput("FinalGPSLastLocation.txt", Context.MODE_PRIVATE);
				fileOutputStream.write(jsonObjMain.toString().getBytes());
				fileOutputStream.close();*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{

        }
    }

    public boolean checkLastFinalLoctionIsRepeated(String currentLat, String currentLong, String currentAccuracy){
        boolean repeatedLoction=false;

        try {

            String chekLastGPSLat="0";
            String chekLastGPSLong="0";
            String chekLastGpsAccuracy="0";
            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.FinalLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="FinalGPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+ CommonInfo.FinalLatLngJsonFile+"/"+txtFileNamenew;

            // If file does not exists, then create it
            if (file.exists()) {
                StringBuffer buffer=new StringBuffer();
                String myjson_stampiGPSLastLocation="";
                StringBuffer sb = new StringBuffer();
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new FileReader(file));

                    String temp;
                    while ((temp = br.readLine()) != null)
                        sb.append(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close(); // stop reading
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                myjson_stampiGPSLastLocation=sb.toString();

                JSONObject jsonObjGPSLast = new JSONObject(myjson_stampiGPSLastLocation);
                JSONArray jsonObjGPSLastInneralues = jsonObjGPSLast.getJSONArray("GPSLastLocationDetils");

                String StringjsonGPSLastnew = jsonObjGPSLastInneralues.getString(0);
                JSONObject jsonObjGPSLastnewwewe = new JSONObject(StringjsonGPSLastnew);

                chekLastGPSLat=jsonObjGPSLastnewwewe.getString("chekLastGPSLat");
                chekLastGPSLong=jsonObjGPSLastnewwewe.getString("chekLastGPSLong");
                chekLastGpsAccuracy=jsonObjGPSLastnewwewe.getString("chekLastGpsAccuracy");

                if(currentLat!=null )
                {
                    if(currentLat.equals(chekLastGPSLat) && currentLong.equals(chekLastGPSLong) && currentAccuracy.equals(chekLastGpsAccuracy))
                    {
                        repeatedLoction=true;
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repeatedLoction;

    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isGPSokCheckInResume = false;
        boolean isNWokCheckInResume=false;
        isGPSokCheckInResume = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNWokCheckInResume = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSokCheckInResume && !isNWokCheckInResume)
        {
            try
            {
                showSettingsAlert();
            }
            catch(Exception e)
            {

            }
            isGPSokCheckInResume = false;
            isNWokCheckInResume=false;
        }
        else
        {

            if(countSubmitClicked==1)
            {

                locationRetrievingAndDistanceCalculating();
                countSubmitClicked++;


            }
               /* if(countSubmitClicked==2)
                {

                        locationRetrievingAndDistanceCalculating();
                        countSubmitClicked++;


                }else if(countSubmitClicked==0)
                {

                locationRetrievingAndDistanceCalculating();
                countSubmitClicked++;

                }*/


        }
        if(flgCheckNewOldStore==1)
        {
            flgCheckNewOldStore=0;
            // setStoreName(StoreNameFromBack);
            NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
            if(null != recFragment)
            {
                recFragment.setStoreName(StoreNameFromBack);
            }
        }
    }

    public String getAddressOfProviders(String latti, String longi){

        StringBuilder FULLADDRESS2 =new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.ENGLISH);



        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);

            if (addresses == null || addresses.size()  == 0)
            {
                FULLADDRESS2=  FULLADDRESS2.append("NA");
            }
            else
            {
                for(Address address : addresses) {
                    //  String outputAddress = "";
                    for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        if(i==1)
                        {
                            FULLADDRESS2.append(address.getAddressLine(i));
                        }
                        else if(i==2)
                        {
                            FULLADDRESS2.append(",").append(address.getAddressLine(i));
                        }
                    }
                }
		      /* //String address = addresses.get(0).getAddressLine(0);
		       String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		       String city = addresses.get(0).getLocality();
		       String state = addresses.get(0).getAdminArea();
		       String country = addresses.get(0).getCountryName();
		       String postalCode = addresses.get(0).getPostalCode();
		       String knownName = addresses.get(0).getFeatureName();
		       FULLADDRESS=address+","+city+","+state+","+country+","+postalCode;
		      Toast.makeText(contextcopy, "ADDRESS"+address+"city:"+city+"state:"+state+"country:"+country+"postalCode:"+postalCode, Toast.LENGTH_LONG).show();*/

            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return FULLADDRESS2.toString();

    }

    public void checkHighAccuracyLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        flgLocationServicesOnOff=0;
        flgGPSOnOff=0;
        flgNetworkOnOff=0;
        flgFusedOnOff=0;
        flgInternetOnOffWhileLocationTracking=0;

        if(isGooglePlayServicesAvailable())
        {
            flgFusedOnOff=1;
        }
        if(isOnline())
        {
            flgInternetOnOffWhileLocationTracking=1;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            //Equal or higher than API 19/KitKat
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                if (locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY){
                    flgLocationServicesOnOff=1;
                    flgGPSOnOff=1;
                    flgNetworkOnOff=1;
                    //flgFusedOnOff=1;
                }
                if (locationMode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING){
                    flgLocationServicesOnOff=1;
                    flgGPSOnOff=0;
                    flgNetworkOnOff=1;
                   // flgFusedOnOff=1;
                }
                if (locationMode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY){
                    flgLocationServicesOnOff=1;
                    flgGPSOnOff=1;
                    flgNetworkOnOff=0;
                    //flgFusedOnOff=0;
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            //Lower than API 19
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = Settings.Secure.LOCATION_MODE_OFF;

                flgLocationServicesOnOff = 0;
                flgGPSOnOff = 0;
                flgNetworkOnOff = 0;
               // flgFusedOnOff = 0;
            }
            if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                flgLocationServicesOnOff = 1;
                flgGPSOnOff = 1;
                flgNetworkOnOff = 1;
                //flgFusedOnOff = 0;
            } else {
                if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 1;
                    flgNetworkOnOff = 0;
                   // flgFusedOnOff = 0;
                }
                if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                    flgLocationServicesOnOff = 1;
                    flgGPSOnOff = 0;
                    flgNetworkOnOff = 1;
                    //flgFusedOnOff = 0;
                }
            }
        }

    }

    public  void fnGettingGSTOFflineVal()
    {
        // Start for getting GST Offline

        String OutletID="0",QuestID = "0",AnswerType,AnswerValue = "";
        int sectionID = 0;
        int QuestionGroupID=0;


        for(Entry<String, String> entry:hmapStoreQuestAnsNew.entrySet())
        {
            String questId=entry.getKey().split(Pattern.quote("^"))[0].toString();
            AnswerType=entry.getKey().split(Pattern.quote("^"))[1].toString();
            QuestionGroupID=Integer.valueOf(entry.getKey().split(Pattern.quote("^"))[2].toString());
            AnswerValue=entry.getValue();

            String optionValue="0";

            if(questId.equals("49"))
            {
                try
                {
                    //flgGSTCompliance=helperDb.fnGetGstOptionIDComplianceWhileAddingNewStore(""+AnswerValue);
                    String OptionDescr=helperDb.fnGetOptionDescrFromtblOptionMstr(questId,""+AnswerValue);
                    if(OptionDescr.equals("Yes"))
                    {
                        flgGSTCompliance="1";
                    }
                    else if(OptionDescr.equals("Not Required"))
                    {
                        flgGSTCompliance="0";
                    }
                    else if(OptionDescr.equals("Pending"))
                    {
                        flgGSTCompliance="2";
                    }

                }
                catch(Exception e)
                {

                }
            }
            if(questId.equals("50"))
            {
                if(!AnswerValue.equals(""))
                {
                    GSTNumber=AnswerValue;
                }
            }



        }

        // End for gst getting
    }

    public String getAddressNewWay(String ZeroIndexAddress,String city,String State,String pincode,String country){
        String editedAddress=ZeroIndexAddress;
        if(editedAddress.contains(city)){
            editedAddress= editedAddress.replace(city,"");

        }
        if(editedAddress.contains(State)){
            editedAddress=editedAddress.replace(State,"");

        }
        if(editedAddress.contains(pincode)){
            editedAddress= editedAddress.replace(pincode,"");

        }
        if(editedAddress.contains(country)){
            editedAddress=editedAddress.replace(country,"");

        }
        if(editedAddress.contains(",")){
            editedAddress=editedAddress.replace(","," ");

        }

        return editedAddress;
    }

    class MyTimerTask extends TimerTask
    {

        @Override
        public void run()
        {

            runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {

                    if(task!=null)
                    {
                        if(task.getStatus()==AsyncTask.Status.RUNNING)
                        {
                            task.cancel(true);


                            if(pDialogGetStores.isShowing())
                            {
                                pDialogGetStores.dismiss();
                            }

                            if(ServiceWorkerDataComingFlag==0)
                            {
                                addStoreOffline();
                            }

                            else
                            {

                                addStoreOnLine();

                            }


                        }
                    }
                    if(taskUpdateScheme!=null)
                    {

                        if(checkSecondTaskStatus==1)
                        {

                            if(taskUpdateScheme.getStatus()==AsyncTask.Status.RUNNING)
                            {
                                taskUpdateScheme.cancel(true);
                                if(pDialogGetStores.isShowing())
                                {
                                    pDialogGetStores.dismiss();
                                }


                                if(ServiceWorkerDataComingFlag==0)
                                {
                                    addStoreOffline();
                                }
                                else
                                {
                                    addStoreOnLine();
                                }


                            }

                        }
                    }
                }});
        }

    }

    private class GetAddingStoreInfo extends AsyncTask<Void, Void, Void>
    {
        ServiceWorker getRouteservice = new ServiceWorker();


        public GetAddingStoreInfo(AddNewStore_DynamicSectionWise activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            long  syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
            VisitEndTS = df.format(dateobj);

            System.out.println("Im  Testing H2");


            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage("");
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try
            {
                for(int mm = 1; mm<2; mm++)
                {
                    if(mm==1)
                    {


                        long syncTIMESTAMP = System.currentTimeMillis();
                        Date datefromat = new Date(syncTIMESTAMP);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS",Locale.ENGLISH);


                        String VisitStartTS=df.format(datefromat);

                        int ApplicationID= CommonInfo.Application_TypeID;
										   /*helperDb.inserttblOutletMstr(OutletID,VisitStartTS,String.valueOf(lastKnownLocLatitude),
												String.valueOf(lastKnownLocLongitude),"" + accuracy,locationProvider,battLevel + "%",StoreName,
												imei,0,3,ApplicationID);*/

                        String NewStoreOutletPaymentDetails=helperDb.fngettblNewStoreSalesQuotePaymentDetails(selStoreID);
                        JSONArray ArrOutletPaymentDetails = new JSONArray();
                        JSONObject OutletPaymentDetails = new JSONObject();
                        OutletPaymentDetails.put("PymtStageId", NewStoreOutletPaymentDetails.trim().split(Pattern.quote("~"))[0]);
                        OutletPaymentDetails.put("Percentage", NewStoreOutletPaymentDetails.trim().split(Pattern.quote("~"))[1]);
                        OutletPaymentDetails.put("CreditDays", NewStoreOutletPaymentDetails.trim().split(Pattern.quote("~"))[2]);
                        OutletPaymentDetails.put("CreditLimit", NewStoreOutletPaymentDetails.trim().split(Pattern.quote("~"))[3]);
                        OutletPaymentDetails.put("PymtMode", NewStoreOutletPaymentDetails.trim().split(Pattern.quote("~"))[4]);
                        ArrOutletPaymentDetails.put(OutletPaymentDetails);







                        JSONArray ArrOutletGeneralInfoTable = new JSONArray();
                        JSONObject OutletInfo = new JSONObject();

                        OutletInfo.put("OutletID", selStoreID.trim());
                        OutletInfo.put("VisitStartTS", VisitStartTS.trim());
                        OutletInfo.put("VisitEndTS", VisitStartTS.trim());
                        OutletInfo.put("AppVersion", ApplicationID);
                        OutletInfo.put("ActualLatitude", String.valueOf(fnLati));
                        OutletInfo.put("ActualLongitude", String.valueOf(fnLongi));


                        OutletInfo.put("LocProvider", fnAccurateProvider);
                        OutletInfo.put("Accuracy", fnAccuracy);
                        OutletInfo.put("BateryLeftStatus", battLevel);
                        OutletInfo.put("StoreName", StoreName.trim());
                        OutletInfo.put("imei", imei.trim());
                        OutletInfo.put("ISNewStore", 1);
                        OutletInfo.put("IsNewStoreDataCompleteSaved", 0);

                        OutletInfo.put("Sstat", 0);
                        OutletInfo.put("StoreMapAddress", fetchAddress);


                        ArrOutletGeneralInfoTable.put(OutletInfo);

                        JSONArray ArrAnsAndQuestionTable = new JSONArray();

                        LinkedHashMap<String, String> hmapStoreQuestAnsNew=new LinkedHashMap<String, String>();


                        NewStoreForm recFragment = (NewStoreForm)getFragmentManager().findFragmentByTag("NewStoreFragment");
                        if(null != recFragment)
                        {
                            hmapStoreQuestAnsNew=recFragment.hmapAnsValues;

                        }

                        try
                        {
                            if(hmapStoreQuestAnsNew.size()>0)
                            {
                                for(Entry<String, String> entry:hmapStoreQuestAnsNew.entrySet())
                                {
                                    JSONObject post_dict = new JSONObject();
                                    String keyhmap=entry.getKey();
                                    String valuehmap=entry.getValue();
                                    post_dict.put("OutletID" , selStoreID);

											        	 /*if(Integer.parseInt(outletsDetailValue.get(1))==15)
											        	 {
											        		 StoreTypeTradeChannel= helperDb.fnGetAnsValFromOptionID(Integer.parseInt(outletsDetailValue.get(3)));//Integer.parseInt(outletsDetailValue.get(3));
											        	 }*/
                                    post_dict.put("QuestionId" , keyhmap.split(Pattern.quote("^"))[0]);
                                    post_dict.put("AnsCtronlType", keyhmap.split(Pattern.quote("^"))[1]);
                                    post_dict.put("QuestGroupId", keyhmap.split(Pattern.quote("^"))[2]);
                                    post_dict.put("Value", valuehmap);

                                    /*String optionValue="0";
                                    if(valuehmap.trim().indexOf("^")!=-1)
                                    {
                                        String[] diffrentOptionIdSelected=valuehmap.trim().split(Pattern.quote("^"));
                                        for(int j=0;j<diffrentOptionIdSelected.length;j++)
                                        {
                                            String curntOptionID=diffrentOptionIdSelected[j];
                                            String filterOptID="0";
                                            if(curntOptionID.trim().indexOf("~")!=-1)
                                            {
                                                filterOptID=curntOptionID.trim().split(Pattern.quote("~"))[0];
                                            }
                                            else
                                            {
                                                filterOptID=curntOptionID;
                                            }

                                            if(hmapOptionId_OptionValue.containsKey(keyhmap.split(Pattern.quote("^"))[0]+"_"+filterOptID))
                                            {
                                                if(j==0)
                                                {
                                                    optionValue=hmapOptionId_OptionValue.get(keyhmap.split(Pattern.quote("^"))[0]+"_"+filterOptID);
                                                }
                                                else
                                                {
                                                    optionValue=optionValue+"^"+hmapOptionId_OptionValue.get(keyhmap.split(Pattern.quote("^"))[0]+"_"+filterOptID);
                                                }
                                            }

                                        }
                                    }
                                    else
                                    {
                                        if(hmapOptionId_OptionValue.containsKey(keyhmap.split(Pattern.quote("^"))[0]+"_"+valuehmap.trim()))
                                        {
                                            optionValue=hmapOptionId_OptionValue.get(keyhmap.split(Pattern.quote("^"))[0]+"_"+valuehmap.trim());
                                        }
                                    }

                                    post_dict.put("optionValue", optionValue);*/

                                    ArrAnsAndQuestionTable.put(post_dict);
                                }
												  /*for(int i=0;i<hmapStoreQuestAnsNew.size();i++)
											         {
											        	 ArrayList<String> outletsDetailValue=hmapStoreQuestAnsNew.get(i);
											        	 JSONObject post_dict = new JSONObject();

													        	 if(i==0)
													        	 {
											        		     post_dict.put("OutletID" , outletsDetailValue.get(0));
											        	         }
													        	 //StoreType
													        	 if(Integer.parseInt(outletsDetailValue.get(1))==15)
													        	 {
													        		 StoreTypeTradeChannel= helperDb.fnGetAnsValFromOptionID(Integer.parseInt(outletsDetailValue.get(3)));//Integer.parseInt(outletsDetailValue.get(3));
													        	 }
											        		     post_dict.put("QuestionId" , outletsDetailValue.get(1));
													             post_dict.put("AnsCtronlType", outletsDetailValue.get(2));
													             post_dict.put("Value", outletsDetailValue.get(3));
													             ArrAnsAndQuestionTable.put(post_dict);


											         }*/
                            }

                            // ArrAnsAndQuestionTable.put(ArrOutletGeneralInfoTable);
                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                       // getRouteservice = getRouteservice.getNewStoreInfoDynamic(getApplicationContext(),ArrAnsAndQuestionTable,ArrOutletGeneralInfoTable,imei,ArrOutletPaymentDetails);

                        CustomStringForServiceWorker="";
                        if(!getRouteservice.director.toString().trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                System.out.println("Im  Testing H4");
                                chkFlgForErrorToCloseApp=1;

                            }

                        }
                    }


                }


            }
            catch (Exception e)
            {

            }
            finally
            {

            }

            return null;
        }

        @Override
        protected void onCancelled()
        {

        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);


            System.out.println("Im  Testing H5 : "+chkFlgForErrorToCloseApp);

            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            if(chkFlgForErrorToCloseApp==1)
            {
                chkFlgForErrorToCloseApp=0;


                // helperDb.saveOutletQuestAnsMstr(saveOutletQuestAnsMstrVal);
                if (timer!=null)
                {
                    timer.cancel();
                    timer = null;
                }
                addStoreOffline();

            }
            else
            {
						/* if(selStoreID=="0")
							 {*/
                if(isOnline())

                {
                    try
                    {
                        checkSecondTaskStatus=1;
                        taskUpdateScheme = new GetUpdateSchemeForNewStore(AddNewStore_DynamicSectionWise.this);

                        taskUpdateScheme.execute();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {

                    addStoreOffline();

                }




            }




        }
    }

    private class GetUpdateSchemeForNewStore extends AsyncTask<Void, Void, Void>
    {
        ServiceWorker newservice = new ServiceWorker();


        public GetUpdateSchemeForNewStore(AddNewStore_DynamicSectionWise activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage("");
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                for(int mm = 1; mm<2; mm++)
                {
                    if(mm==1)
                    {

                        Date currDateNew = new Date();
                        SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                        String currSysDateNew = currDateFormatNew.format(currDateNew).toString();

                      //  newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID);
                        if(!newservice.director.toString().trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;

                            }

                        }
                    }
									/*if(mm==2)
									{

										Date currDateNew = new Date();
										SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

										String currSysDateNew = currDateFormatNew.format(currDateNew).toString();

										newservice = newservice.fnGetStoreListWithPaymentAddressMR(getApplicationContext(), currSysDateNew, imei, rID);
										if(!newservice.director.toString().trim().equals("1"))
										{
											if(chkFlgForErrorToCloseApp==0)
											{
												chkFlgForErrorToCloseApp=1;

											}

										}
									}*/
                }


            }
            catch (Exception e)
            {

            }
            finally
            {

            }

            return null;
        }

        @Override
        protected void onCancelled()
        {

        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if (timer!=null)
            {
                timer.cancel();
                timer = null;
            }

            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            if(chkFlgForErrorToCloseApp==1)
            {
                chkFlgForErrorToCloseApp=0;

                if(ServiceWorkerDataComingFlag==0)
                {
                    addStoreOffline();
                }
                else
                {
                    addStoreOnLine();
                }
            }
            else
            { if(ServiceWorkerDataComingFlag==0)
            {
                addStoreOffline();
            }
            else
            {
                addStoreOnLine();
            }



            }
        }

    }

    class AddressResultReceiver extends ResultReceiver
    {

        public AddressResultReceiver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }
        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            // TODO Auto-generated method stub
            if (resultCode == 1)
            {
                final Address address = resultData.getParcelable("RESULT_ADDRESS");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String address1 = resultData.getString("RESULT_DATA_KEY");
                        fetchAddress=address1;
                        txtAddress.setText(address1);
                    }
                });
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

           /* intent.putExtra("Latitude", mCurrentLocation.getLatitude());
            intent.putExtra("Longitude", mCurrentLocation.getLatitude());
            intent.putExtra("Accuracy", mCurrentLocation.getAccuracy());*/
            double lat = arg1.getDoubleExtra("Latitude", 0.0);
            double lng=arg1.getDoubleExtra("Longitude", 0.0);
            double acc=arg1.getDoubleExtra("Accuracy",0.0);
            if(lat!=0.0)
            {

                    LattitudeFromLauncher= String.valueOf(lat);
                    LongitudeFromLauncher= String.valueOf(lng);
                    AccuracyFromLauncher= String.valueOf(acc);
                    latitudeToSave=LattitudeFromLauncher;
                    longitudeToSave=LongitudeFromLauncher;
                    accuracyToSave=AccuracyFromLauncher;





            }



        }

    }

    public class CoundownClass extends CountDownTimer {

        public CoundownClass(long startTime, long interval) {
            super(startTime, interval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish()
        {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            String GpsLat="0";
            String GpsLong="0";
            String GpsAccuracy="0";
            String GpsAddress="0";
            if(isGPSEnabled)
            {

                Location nwLocation=appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER,location);

                if(nwLocation!=null){
                    double lattitude=nwLocation.getLatitude();
                    double longitude=nwLocation.getLongitude();
                    double accuracy= nwLocation.getAccuracy();
                    GpsLat=""+lattitude;
                    GpsLong=""+longitude;
                    GpsAccuracy=""+accuracy;
                    if(isOnline())
                    {
                        GpsAddress=getAddressOfProviders(GpsLat, GpsLong);
                    }
                    else
                    {
                        GpsAddress="NA";
                    }

                    GPSLocationLatitude=""+lattitude;
                    if(GPSLocationLatitude.contains("E") || GPSLocationLatitude.contains("e")){
                        GPSLocationLatitude=	convertExponential(lattitude);
                    }
                    GPSLocationLongitude=""+longitude;
                    if(GPSLocationLongitude.contains("E") || GPSLocationLongitude.contains("e")){
                        GPSLocationLongitude=	convertExponential(longitude);
                    }
                    GPSLocationProvider="GPS";
                    GPSLocationAccuracy=""+accuracy;
                    AllProvidersLocation="GPS=Lat:"+lattitude+"Long:"+longitude+"Acc:"+accuracy;

                }
            }

            Location gpsLocation=appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER,location);
            String NetwLat="0";
            String NetwLong="0";
            String NetwAccuracy="0";
            String NetwAddress="0";
            if(gpsLocation!=null){
                double lattitude1=gpsLocation.getLatitude();
                double longitude1=gpsLocation.getLongitude();
                double accuracy1= gpsLocation.getAccuracy();

                NetwLat=""+lattitude1;
                NetwLong=""+longitude1;
                NetwAccuracy=""+accuracy1;
                if(isOnline())
                {
                    NetwAddress=getAddressOfProviders(NetwLat, NetwLong);
                }
                else
                {
                    NetwAddress="NA";
                }

                NetworkLocationLatitude=""+lattitude1;
                if(NetworkLocationLatitude.contains("E") || NetworkLocationLatitude.contains("e")){
                    NetworkLocationLatitude=	convertExponential(lattitude1);
                }
                NetworkLocationLongitude=""+longitude1;
                if(NetworkLocationLongitude.contains("E") || NetworkLocationLongitude.contains("e")){
                    NetworkLocationLongitude=	convertExponential(longitude1);
                }
                NetworkLocationProvider="Network";
                NetworkLocationAccuracy=""+accuracy1;
                if(!AllProvidersLocation.equals(""))
                {
                    AllProvidersLocation=AllProvidersLocation+"$Network=Lat:"+lattitude1+"Long:"+longitude1+"Acc:"+accuracy1;
                }
                else
                {
                    AllProvidersLocation="Network=Lat:"+lattitude1+"Long:"+longitude1+"Acc:"+accuracy1;
                }


            }
									 /* TextView accurcy=(TextView) findViewById(R.id.Acuracy);
									  accurcy.setText("GPS:"+GPSLocationAccuracy+"\n"+"NETWORK"+NetworkLocationAccuracy+"\n"+"FUSED"+fusedData);*/

            System.out.println("LOCATION Fused"+fusedData);

            String FusedLat="0";
            String FusedLong="0";
            String FusedAccuracy="0";
            String FusedAddress="0";

            if(!FusedLocationProvider.equals(""))
            {
                fnAccurateProvider="Fused";
                fnLati=FusedLocationLatitude;
                fnLongi=FusedLocationLongitude;
                fnAccuracy= Double.parseDouble(FusedLocationAccuracy);

                FusedLat=FusedLocationLatitude;
                FusedLong=FusedLocationLongitude;
                FusedAccuracy=FusedLocationAccuracy;
                FusedLocationLatitudeWithFirstAttempt=FusedLocationLatitude;
                FusedLocationLongitudeWithFirstAttempt=FusedLocationLongitude;
                FusedLocationAccuracyWithFirstAttempt=FusedLocationAccuracy;


                if(isOnline())
                {
                    FusedAddress=getAddressOfProviders(FusedLat, FusedLong);
                }
                else
                {
                    FusedAddress="NA";
                }

                if(!AllProvidersLocation.equals(""))
                {
                    AllProvidersLocation=AllProvidersLocation+"$Fused=Lat:"+FusedLocationLatitude+"Long:"+FusedLocationLongitude+"Acc:"+fnAccuracy;
                }
                else
                {
                    AllProvidersLocation="Fused=Lat:"+FusedLocationLatitude+"Long:"+FusedLocationLongitude+"Acc:"+fnAccuracy;
                }
            }


            appLocationService.KillServiceLoc(appLocationService, locationManager);
            try {
                if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
                {
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                }
            }
            catch (Exception e){

            }




            fnAccurateProvider="";
            fnLati="0";
            fnLongi="0";
            fnAccuracy=0.0;

            if(!FusedLocationProvider.equals(""))
            {
                fnAccurateProvider="Fused";
                fnLati=FusedLocationLatitude;
                fnLongi=FusedLocationLongitude;
                fnAccuracy= Double.parseDouble(FusedLocationAccuracy);
            }

            if(!fnAccurateProvider.equals(""))
            {
                if(!GPSLocationProvider.equals(""))
                {
                    if(Double.parseDouble(GPSLocationAccuracy)<fnAccuracy)
                    {
                        fnAccurateProvider="Gps";
                        fnLati=GPSLocationLatitude;
                        fnLongi=GPSLocationLongitude;
                        fnAccuracy= Double.parseDouble(GPSLocationAccuracy);
                    }
                }
            }
            else
            {
                if(!GPSLocationProvider.equals(""))
                {
                    fnAccurateProvider="Gps";
                    fnLati=GPSLocationLatitude;
                    fnLongi=GPSLocationLongitude;
                    fnAccuracy= Double.parseDouble(GPSLocationAccuracy);
                }
            }

            if(!fnAccurateProvider.equals(""))
            {
                if(!NetworkLocationProvider.equals(""))
                {
                    if(Double.parseDouble(NetworkLocationAccuracy)<fnAccuracy)
                    {
                        fnAccurateProvider="Network";
                        fnLati=NetworkLocationLatitude;
                        fnLongi=NetworkLocationLongitude;
                        fnAccuracy= Double.parseDouble(NetworkLocationAccuracy);
                    }
                }
            }
            else
            {
                if(!NetworkLocationProvider.equals(""))
                {
                    fnAccurateProvider="Network";
                    fnLati=NetworkLocationLatitude;
                    fnLongi=NetworkLocationLongitude;
                    fnAccuracy= Double.parseDouble(NetworkLocationAccuracy);
                }
            }
            checkHighAccuracyLocationMode(AddNewStore_DynamicSectionWise.this);
            // fnAccurateProvider="";
            if(fnAccurateProvider.equals(""))
            {

                if(pDialog2STANDBY.isShowing())
                {
                    pDialog2STANDBY.dismiss();
                }

                manager= getFragmentManager();
                mapFrag = (MapFragment)manager.findFragmentById(
                        R.id.map);
                mapFrag.getView().setVisibility(View.VISIBLE);
                mapFrag.getMapAsync(AddNewStore_DynamicSectionWise.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(mapFrag);
            }
            else
            {
                if(isOnline())
                {
                    getAddressForDynamic(fnLati,fnLongi);
                }
                if(pDialog2STANDBY.isShowing())
                {
                    pDialog2STANDBY.dismiss();
                }
                if(!GpsLat.equals("0") )
                {
                    fnCreateLastKnownGPSLoction(GpsLat,GpsLong,GpsAccuracy);
                }
                LattitudeFromLauncher=fnLati;
                LongitudeFromLauncher=fnLongi;
                AccuracyFromLauncher= String.valueOf(fnAccuracy);
                latitudeToSave=fnLati;
                longitudeToSave=fnLongi;

                ProviderFromLauncher = fnAccurateProvider;
                GpsLatFromLauncher = GpsLat;
                GpsLongFromLauncher = GpsLong;
                GpsAccuracyFromLauncher = GpsAccuracy;
                NetworkLatFromLauncher = NetwLat;
                NetworkLongFromLauncher = NetwLong;
                NetworkAccuracyFromLauncher = NetwAccuracy;
                FusedLatFromLauncher = FusedLat;
                FusedLongFromLauncher = FusedLong;
                FusedAccuracyFromLauncher =FusedAccuracy;

                AllProvidersLocationFromLauncher = AllProvidersLocation;
                GpsAddressFromLauncher = GpsAddress;
                NetwAddressFromLauncher = NetwAddress;
                FusedAddressFromLauncher = FusedAddress;
                FusedLocationLatitudeWithFirstAttemptFromLauncher = FusedLocationLatitudeWithFirstAttempt;
                FusedLocationLongitudeWithFirstAttemptFromLauncher = FusedLocationLongitudeWithFirstAttempt;
                FusedLocationAccuracyWithFirstAttemptFromLauncher = FusedLocationAccuracyWithFirstAttempt;
                //LLLLL



                manager= getFragmentManager();
                mapFrag = (MapFragment)manager.findFragmentById(
                        R.id.map);
                mapFrag.getView().setVisibility(View.VISIBLE);
                mapFrag.getMapAsync(AddNewStore_DynamicSectionWise.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.show(mapFrag);

                if(!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher))
                {
                    fnCreateLastKnownFinalLocation(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher);
                }
                else
                {
                    if(!TextUtils.isEmpty(prvsStoreId))
                    {
                        if(helperDb.isPrvsStoreMsgShownAndRestrtDone(prvsStoreId))
                        {


                            showAlertForEveryOne(getResources().getString(R.string.SameLocRestart));
                        }
                        else
                        {

                        }
                    }

                    else
                    {
                        //String prvsStoreID,String CrntStoreID,String isSavedOrSubmittedStore,String MsgToRestartPopUpShown,String isRestartDoneByDSR,String Sstat)
                        helperDb.insertRestartStoreInfo(selStoreID,selStoreID,"0","1","0",0,VisitStartTS);
                        showAlertForEveryOne(getResources().getString(R.string.SameLocRestart));
                    }



                }

               //if(!checkLastFinalLoctionIsRepeated("28.4866314","77.101591","100.0"))
                  /* if(!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher))
                    {
                        fnCreateLastKnownFinalLocation(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher);
                    }
                    else
                    {


                        if(countSubmitClicked==1)
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewStore_DynamicSectionWise.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(getText(R.string.genTermInformation));
                            alertDialog.setIcon(R.drawable.error_info_ico);
                            alertDialog.setCancelable(false);
                            // Setting Dialog Message
                            alertDialog.setMessage(getText(R.string.AlertSameLoc));

                            // On pressing Settings button
                            alertDialog.setPositiveButton(getText(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    countSubmitClicked++;
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                        else {
                            if(!TextUtils.isEmpty(prvsStoreId))
                            {
                                if(helperDb.isPrvsStoreMsgShownAndRestrtDone(prvsStoreId))
                                {
                                   showAlertForEveryOne(getResources().getString(R.string.SameLocRestart));
                                }
                                else
                                {

                                }
                            }

                            else
                            {
                                //String prvsStoreID,String CrntStoreID,String isSavedOrSubmittedStore,String MsgToRestartPopUpShown,String isRestartDoneByDSR,String Sstat)
                                helperDb.insertRestartStoreInfo(selStoreID,selStoreID,"0","1","0",0,VisitStartTS);
                                showAlertForEveryOne(getResources().getString(R.string.SameLocRestart));
                            }
                        }





                    }*/

               /* if(countSubmitClicked>0)
                {
                    if(!checkLastFinalLoctionIsRepeated(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher))
                    {
                        fnCreateLastKnownFinalLocation(LattitudeFromLauncher,LongitudeFromLauncher,AccuracyFromLauncher);
                    }

                    showSubmitConfirm();
                }*/
                GpsLat="0";
                GpsLong="0";
                GpsAccuracy="0";
                GpsAddress="0";
                NetwLat="0";
                NetwLong="0";
                NetwAccuracy="0";
                NetwAddress="0";
                FusedLat="0";
                FusedLong="0";
                FusedAccuracy="0";
                FusedAddress="0";

                //code here
            }




        }

        @Override
        public void onTick(long arg0) {
            // TODO Auto-generated method stub

        }}

    public String convertExponential(double firstNumber){
        String secondNumberAsString = String.format("%.10f",firstNumber);
        return secondNumberAsString;
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
}
