package project.astix.com.balajisfaindirect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DayCollectionReport extends AppCompatActivity {
    PRJDatabase dbengine = new PRJDatabase(this);
    ArrayList<LinkedHashMap<String,ArrayList<String>>> arrList=new ArrayList<LinkedHashMap<String,ArrayList<String>>>();
    LinkedHashMap<String,ArrayList<String>> hmapFirstSection=new LinkedHashMap<String,ArrayList<String>>();
    LinkedHashMap<String,ArrayList<String>> hmapSecondSection=new LinkedHashMap<String,ArrayList<String>>();
    TextView txt_invoice,txt_cllctn,txt_chq_cllctn,txt_blncClctn,btn_UpdateCollection;
    LinearLayout ll_chequeDetails;
    Button btn_details,btn_Next;
    ImageView imgVw_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_collection_report);
        arrList=dbengine.fnGetDayEndOverAllCollectionReport();

        TextView  txt_invoice=(TextView) findViewById(R.id.txt_invoice);
        TextView  txt_cllctn=(TextView) findViewById(R.id.txt_cllctn);
        TextView  txt_chq_cllctn=(TextView) findViewById(R.id.txt_chq_cllctn);
        TextView  txt_blncClctn=(TextView) findViewById(R.id.txt_blncClctn);
        LinearLayout ll_chequeDetails=(LinearLayout) findViewById(R.id.ll_chequeDetails);
        btn_Next= (Button) findViewById(R.id.btn_Next);
        btn_details= (Button) findViewById(R.id.btn_details);
        btn_UpdateCollection=(Button) findViewById(R.id.btn_UpdateCollection);
        btn_UpdateCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1 = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String fDate = sdf.format(date1).toString().trim();

                sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String fDateNew = sdf.format(date1).toString();
                String rID = dbengine.GetActiveRouteID();
                Intent storeIntent = new Intent(DayCollectionReport.this, StoreSelection.class);
                storeIntent.putExtra("imei", CommonInfo.imei);
                storeIntent.putExtra("userDate", fDate);
                storeIntent.putExtra("pickerDate", fDateNew);
                storeIntent.putExtra("rID", rID);
                startActivity(storeIntent);
                finish();
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DayCollectionReport.this,StockRequestActivity.class);
                intent.putExtra("IntentFrom",0);
                startActivity(intent);
                finish();
            }
        });
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent refresh = new Intent(DayCollectionReport.this, DayEndStoreCollectionsChequeReport.class);
                startActivity(refresh);
                finish();
            }
        });
        imgVw_back=(ImageView) findViewById(R.id.imgVw_back);
        imgVw_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayCollectionReport.this, AllButtonActivity.class);
                intent.putExtra("imei", CommonInfo.imei);
                DayCollectionReport.this.startActivity(intent);
                finish();
            }
        });

        if(arrList!=null)
        {
            hmapFirstSection=arrList.get(0);
            hmapSecondSection=arrList.get(1);
        }
        if(hmapFirstSection!=null && hmapFirstSection.size()>0)
        {
            ArrayList<String> arrFistrSectionDetails=hmapFirstSection.get("FirstSectionDetails");
            txt_invoice.setText(arrFistrSectionDetails.get(0));
            txt_cllctn.setText(arrFistrSectionDetails.get(1));
            txt_chq_cllctn.setText(arrFistrSectionDetails.get(2));
            txt_blncClctn.setText(arrFistrSectionDetails.get(3));
        }
        if(hmapSecondSection!=null && hmapSecondSection.size()>0)
        {
            LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>(hmapSecondSection);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while (iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry) iterator.next();
                String RefNoChequeNoTrnNo=me2.getKey().toString();
                ArrayList<String> CheequeFulleDetails=(ArrayList<String>)me2.getValue();
                TextView txtChqName=getTextView(RefNoChequeNoTrnNo,false);
                TextView txtChqAmount=getTextView(CheequeFulleDetails.get(0).split(Pattern.quote("^"))[0],true);
                TextView txtChqDate=getTextView(CheequeFulleDetails.get(0).split(Pattern.quote("^"))[1],true);
                TextView txtChqBank=getTextView(CheequeFulleDetails.get(0).split(Pattern.quote("^"))[2],true);
                LinearLayout layoutChq=getLinearLayoutHorizontal(txtChqName,txtChqAmount,txtChqDate,txtChqBank);
                ll_chequeDetails.addView(layoutChq);
            }
        }

    }

    private LinearLayout getLinearLayoutHorizontal(TextView tvChqName,TextView tvChqAmount,TextView tvChqDate,TextView tvChqBank) {
        LinearLayout lay = new LinearLayout(DayCollectionReport.this);

        lay.setOrientation(LinearLayout.HORIZONTAL);
        //  lay.setBackgroundResource(R.drawable.card_background_white);

        lay.addView(tvChqName);
        lay.setPadding(0,1,0,5);
        lay.addView(tvChqAmount);
        lay.setBackgroundResource(R.drawable.card_background_white);
        lay.addView(tvChqDate);
        lay.addView(tvChqBank);
        return lay;

    }
    public TextView getTextView(String uomDes,boolean isMarginToSet)
    {


        TextView txtVw_ques=new TextView(DayCollectionReport.this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, 1f);
       if(isMarginToSet)
       {
           layoutParams1.setMargins(2,0,0,0);
       }
        txtVw_ques.setLayoutParams(layoutParams1);
        //  txtVw_ques.setTag(tagVal);

        txtVw_ques.setTextColor(getResources().getColor(R.color.blue));
        txtVw_ques.setText(uomDes);
        txtVw_ques.setPadding(5,0,0,0);
        txtVw_ques.setBackgroundResource(R.drawable.table_cell_bg_left);



        return txtVw_ques;
    }
}
