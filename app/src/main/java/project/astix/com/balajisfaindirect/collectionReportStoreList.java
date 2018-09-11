package project.astix.com.balajisfaindirect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Typeface;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.astix.Common.CommonInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class collectionReportStoreList extends BaseActivity {
    //public static HashMap<String, String> hmapStoreIdSstat=new HashMap<String, String>();
    public String[] xmlForWeb = new String[1];
    int serverResponseCode = 0;
    public int syncFLAG = 0;
    InputStream inputStream;
    public String currSysDate;
    public ImageView btn_bck;

    boolean serviceException=false;


    public String passDate;
    public SimpleDateFormat sdf;
    public String fDate;
    public String userDate;

    public String pickerDate;
    public String imei;
    public String[] storeList;

    Dialog dialog;

    public TableLayout tl2;
    RelativeLayout relativeLayout1;

    public TextView txtview_selectstoretext;

    ServiceWorker newservice = new ServiceWorker();

    DatabaseAssistant DA = new DatabaseAssistant(this);
    public ProgressDialog pDialogSync;

    int closeList = 0;
    int whatTask = 0;
    String whereTo = "11";

    public String fullFileName1;

    public String[] storeCode;
    public String[] storeName;
    ArrayList<String> stIDs;
    ArrayList<String> stNames;

    public String[] storeStatus;


    public ProgressDialog pDialog2STANDBY;
    PRJDatabase dbengine = new PRJDatabase(this);


    public TableRow tr;
    public String selStoreID = "";
    public String selStoreName = "";

    ProgressDialog pDialog2;



    private final Context mContext = this;

    public String rID;
    public PowerManager pm;
    public	 PowerManager.WakeLock wl;

    public long syncTIMESTAMP;

    // *****SYNC******

    public void SyncNow()
    {

        syncTIMESTAMP = System.currentTimeMillis();
        Date dateobj = new Date(syncTIMESTAMP);


        //dbengine.open();
        String presentRoute=dbengine.GetActiveRouteID();
        //dbengine.close();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss", Locale.ENGLISH);

        String newfullFileName=imei+"."+presentRoute+"."+ df.format(dateobj);

        LinkedHashMap<String,String>    hmapStoreListToProcessWithoutAlret=dbengine.fnGetStoreListToProcessWithoutAlret();

        if(hmapStoreListToProcessWithoutAlret!=null)
        {

            Set set2 = hmapStoreListToProcessWithoutAlret.entrySet();
            Iterator iterator = set2.iterator();
            //dbengine.open();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                String StoreIDToProcessWithoutAlret=me2.getKey().toString();
                dbengine.UpdateStoreFlagAtDayEndOrChangeRouteWithOnlyVistOrCollection(StoreIDToProcessWithoutAlret,3);

            }
            //dbengine.close();;

            Set set3 = hmapStoreListToProcessWithoutAlret.entrySet();
            Iterator iterator1 = set3.iterator();

            while(iterator1.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator1.next();
                String StoreIDToProcessWithoutAlret=me2.getKey().toString();
                String  StoreVisitCode=dbengine.fnGetStoreVisitCode(StoreIDToProcessWithoutAlret);
                String TmpInvoiceCodePDA=dbengine.fnGetInvoiceCodePDAWhileSync(StoreIDToProcessWithoutAlret,StoreVisitCode);
                dbengine.UpdateStoreVisitWiseTables(StoreIDToProcessWithoutAlret, 4,StoreVisitCode,TmpInvoiceCodePDA);
                dbengine.updateflgFromWhereSubmitStatusAgainstStore(StoreIDToProcessWithoutAlret, 1,StoreVisitCode);
            }
        }

        try
        {

            File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!OrderXMLFolder.exists())
            {
                OrderXMLFolder.mkdirs();
            }

            String routeID=dbengine.GetActiveRouteIDSunil();

            DA.open();
            DA.export(dbengine.DATABASE_NAME, newfullFileName,routeID);
            DA.close();


            if(hmapStoreListToProcessWithoutAlret!=null)
            {

                Set set2 = hmapStoreListToProcessWithoutAlret.entrySet();
                Iterator iterator = set2.iterator();
                //dbengine.open();
                while(iterator.hasNext())
                {
                    Map.Entry me2 = (Map.Entry)iterator.next();
                    String StoreIDToProcessWithoutAlret=me2.getKey().toString();
                    dbengine.UpdateStoreFlagAtDayEndOrChangeRouteWithOnlyVistOrCollection(StoreIDToProcessWithoutAlret,4);
                   /* String  StoreVisitCode=dbengine.fnGetStoreVisitCode(StoreIDToProcessWithoutAlret);
                    String TmpInvoiceCodePDA=dbengine.fnGetInvoiceCodePDAWhileSync(StoreIDToProcessWithoutAlret,StoreVisitCode);
                    dbengine.UpdateStoreVisitWiseTables(StoreIDToProcessWithoutAlret, 4,StoreVisitCode,TmpInvoiceCodePDA);*/
                }
                //dbengine.close();;

                Set set3 = hmapStoreListToProcessWithoutAlret.entrySet();
                Iterator iterator1 = set3.iterator();

                while(iterator1.hasNext())
                {
                    Map.Entry me2 = (Map.Entry)iterator1.next();
                    String StoreIDToProcessWithoutAlret=me2.getKey().toString();
                    String  StoreVisitCode=dbengine.fnGetStoreVisitCode(StoreIDToProcessWithoutAlret);
                    String TmpInvoiceCodePDA=dbengine.fnGetInvoiceCodePDAWhileSync(StoreIDToProcessWithoutAlret,StoreVisitCode);
                    dbengine.UpdateStoreVisitWiseTables(StoreIDToProcessWithoutAlret, 4,StoreVisitCode,TmpInvoiceCodePDA);
                }

            }


            dbengine.savetbl_XMLfiles(newfullFileName, "3","1");
            //dbengine.open();
            dbengine.UpdateStoreImage("0", 5);

            Intent syncIntent = new Intent(collectionReportStoreList.this, SyncMaster.class);
            syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");
            syncIntent.putExtra("OrigZipFileName", newfullFileName);
            syncIntent.putExtra("whereTo", whereTo);
            startActivity(syncIntent);
            finish();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_report_store_list);
        tl2 = (TableLayout) findViewById(R.id.dynprodtable);
        imei=getIMEI();
        pickerDate=getDateInMonthTextFormat();
        userDate=getDateInMonthTextFormat();



        relativeLayout1=(RelativeLayout) findViewById(R.id.relativeLayout1);

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

            Date date1=new Date();
            sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
            passDate = sdf.format(date1).toString();

            //System.out.println("Selctd Date: "+ passDate);

            fDate = passDate.trim().toString();

            setStoresList();

        btn_bck=(ImageView) findViewById(R.id.btn_bck);
        btn_bck.setTag("0_0");
        btn_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(collectionReportStoreList.this, AllButtonActivity.class);
                startActivity(refresh);
                finish();
            }
        });
    }


    public void setStoresList()
    {
        if(tl2!=null)
        {
            tl2.removeAllViews();
        }

        storeList = dbengine.FetchAllStoreListForCollectionReport();

        storeCode = new String[storeList.length];
        storeName = new String[storeList.length];

        for (int splitval = 0; splitval <= (storeList.length - 1); splitval++)
        {
            StringTokenizer tokens = new StringTokenizer(String.valueOf(storeList[splitval]), "_");

            storeCode[splitval] = tokens.nextToken().trim();
            storeName[splitval] = tokens.nextToken().trim();

        }


        float density = getResources().getDisplayMetrics().density;

        TableRow.LayoutParams paramRB = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(int) (10 * density));



        LayoutInflater inflater = getLayoutInflater();
        for (int current = 0; current < storeList.length; current++)
        {

            final TableRow row = (TableRow) inflater.inflate(R.layout.table_row1, tl2, false);

            final RadioButton rb1 = (RadioButton) row.findViewById(R.id.rg1StoreName);
            final CheckBox check1 = (CheckBox) row.findViewById(R.id.check1);

            final CheckBox check2 = (CheckBox) row.findViewById(R.id.check2);

            rb1.setTag(storeCode[current]);
            rb1.setText("  " + storeName[current]);
            rb1.setTextSize(14.0f);
            rb1.setChecked(false);

            check1.setTag(storeCode[current]);
            check1.setChecked(false);
            check1.setEnabled(false);

            check2.setTag(storeCode[current]);
            check2.setChecked(false);
            check2.setEnabled(false);
            row.setTag(storeCode[current]);



            rb1.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View arg0) {

                    for (int xc = 0; xc < storeList.length; xc++)
                    {
                        TableRow dataRow = (TableRow) tl2.getChildAt(xc);

                        RadioButton child1;
                        CheckBox child2;
                        CheckBox child3;

                        child1 = (RadioButton)dataRow.findViewById(R.id.rg1StoreName);
                        child2 = (CheckBox)dataRow.findViewById(R.id.check1);
                        child3 = (CheckBox)dataRow.findViewById(R.id.check2);

                        child1.setChecked(false);
                        child2.setEnabled(false);
                        child3.setEnabled(false);

                    }

                    check1.setEnabled(true);
                    check2.setEnabled(true);

                    selStoreID = arg0.getTag().toString();

                    //dbengine.open();
                    selStoreName=dbengine.FetchStoreName(selStoreID);
                    //dbengine.close();

                    RadioButton child2get12 = (RadioButton) arg0;
                    child2get12.setChecked(true);
                    check1.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {

                        }
                    });

                    check2.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {

                        }
                    });

                }
            });


            tl2.addView(row);

        }






        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                        if (!selStoreID.isEmpty())
                        {
                               Intent ready4GetLoc = new Intent(collectionReportStoreList.this,LastVisitDetails.class);
                                ready4GetLoc.putExtra("storeID", selStoreID);
                                ready4GetLoc.putExtra("selStoreName", selStoreName);
                                ready4GetLoc.putExtra("imei", imei);
                                ready4GetLoc.putExtra("userDate", userDate);
                                ready4GetLoc.putExtra("pickerDate", pickerDate);
                                ready4GetLoc.putExtra("bck", 0);
                                startActivity(ready4GetLoc);
                                finish();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
                        }
            }
        });
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


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


    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }


}
