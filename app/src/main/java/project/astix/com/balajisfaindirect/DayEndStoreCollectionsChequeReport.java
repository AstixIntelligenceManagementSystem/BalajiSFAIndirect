package project.astix.com.balajisfaindirect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astix.Common.CommonInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DayEndStoreCollectionsChequeReport extends AppCompatActivity {
    PRJDatabase dbengine = new PRJDatabase(this);
    LinkedHashMap<String,String> hmapAllStoreWiseCollectionReport=new  LinkedHashMap<String,String>();
    LinearLayout ll_storeInfo;
    ImageView imgVw_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_end_store_collections_cheque_report);
        hmapAllStoreWiseCollectionReport=dbengine.fnGetAllStoreWiseCollectionReport();
        LinearLayout ll_storeInfo=(LinearLayout)findViewById(R.id.ll_storeInfo);
        imgVw_back=(ImageView) findViewById(R.id.imgVw_back);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imgVw_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayEndStoreCollectionsChequeReport.this, AllButtonActivity.class);
                intent.putExtra("imei", CommonInfo.imei);
                DayEndStoreCollectionsChequeReport.this.startActivity(intent);
                finish();
            }
        });
        if(hmapAllStoreWiseCollectionReport!=null && hmapAllStoreWiseCollectionReport.size()>0)
        {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapAllStoreWiseCollectionReport);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while (iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry) iterator.next();
                String StoreName=me2.getKey().toString();
                String CheequeFulleDetails=(String)me2.getValue();

                View layoutStoreWise = inflater.inflate(R.layout.store_wise_collection_report, null);
                TextView txtStoreName= (TextView) layoutStoreWise.findViewById(R.id.txtStoreName);

                TextView txtInvoiceAmount= (TextView) layoutStoreWise.findViewById(R.id.txtInvoiceAmount);
                TextView txtCollection= (TextView) layoutStoreWise.findViewById(R.id.txtCollection);
                TextView txtChqAmt= (TextView) layoutStoreWise.findViewById(R.id.txtChqAmt);
                TextView txtBalance= (TextView) layoutStoreWise.findViewById(R.id.txtBalance);
                ImageView btnModify= (ImageView) layoutStoreWise.findViewById(R.id.btnModify);
                txtStoreName.setText(StoreName);
                txtInvoiceAmount.setText(CheequeFulleDetails.split(Pattern.quote("^"))[0]);
                txtCollection.setText(CheequeFulleDetails.split(Pattern.quote("^"))[1]);
                txtChqAmt.setText(CheequeFulleDetails.split(Pattern.quote("^"))[2]);
                txtBalance.setText(CheequeFulleDetails.split(Pattern.quote("^"))[3]);
               // btnModify.setText("Modify");
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date1 = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String fDate = sdf.format(date1).toString().trim();

                        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String fDateNew = sdf.format(date1).toString();
                        String rID = dbengine.GetActiveRouteID();
                        Intent storeIntent = new Intent(DayEndStoreCollectionsChequeReport.this, StoreSelection.class);
                        storeIntent.putExtra("imei", CommonInfo.imei);
                        storeIntent.putExtra("userDate", fDate);
                        storeIntent.putExtra("pickerDate", fDateNew);
                        storeIntent.putExtra("rID", rID);
                        startActivity(storeIntent);
                        finish();
                    }
                });


                ll_storeInfo.addView(layoutStoreWise);
            }
        }
    }

}
