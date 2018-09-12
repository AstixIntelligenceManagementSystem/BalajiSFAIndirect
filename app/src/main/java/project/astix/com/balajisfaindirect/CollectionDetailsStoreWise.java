package project.astix.com.balajisfaindirect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by User on 10-Sep-18.
 */

public class CollectionDetailsStoreWise extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Button btn_add_cheque,submit_btn;
    LinearLayout parentOfAllChequeRow;
    TextView chequeDate;
    Calendar calendar;
    ImageView backIcon;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    boolean calFirst = false;
    boolean calSecond = false;
    boolean calThird = false;
    AlertDialog.Builder alertDialog;
    AlertDialog ad;
    View convertView;
    ListView listviewBankSecond;
    ArrayAdapter<String> adapterBankSecond;
    String[]  bankSecondList;

    LinkedHashMap<String, String> hashmapBank;
    EditText inputSearch;
    PRJDatabase dbengine = new PRJDatabase(this);
    public String storeID;
    public String imei;
    public String date;
    public String pickerDate;
    public Double currLon;
    public Double currLat;
    public String selStoreName;
    public String startTS;
    public int bck = 0;
    TextView tv_total_collection;
    EditText et_modified;

    ArrayList<LinkedHashMap<String,LinkedHashMap<String,String>>> chequeAllDataArray;
    LinkedHashMap<String,LinkedHashMap<String,String>> chequeRowData=new LinkedHashMap<String,LinkedHashMap<String,String>>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_details_storewise);

        Intent passedvals = getIntent();
        storeID = passedvals.getStringExtra("storeID");
        selStoreName = passedvals.getStringExtra("selStoreName");
        imei = passedvals.getStringExtra("imei");
        date = passedvals.getStringExtra("userDate");
        pickerDate= passedvals.getStringExtra("pickerDate");
        pickerDate= passedvals.getStringExtra("bck");

        hashmapBank = dbengine.fnGettblBankMaster();
        initializeAllView();
        getDataFromDataBase();
    }
    public void initializeAllView(){


        backIcon=(ImageView) findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(CollectionDetailsStoreWise.this, AllButtonActivity.class);
                startActivity(refresh);
                finish();
            }
        });
        TextView storeNameText=(TextView) findViewById(R.id.storeNameText);
        tv_total_collection=(TextView) findViewById(R.id.tv_total_collection);
        et_modified=(EditText) findViewById(R.id.et_modified);

        storeNameText.setText(selStoreName);

        submit_btn=(Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_modified.getText().toString().trim().equals("")){
                    if(validateChequeRowFillOrNot() && validateAllRows().equals("TRUE")){
                        saveDataToDataBase();

                    }
                }
                else{
                    showAlertSingleButtonError("Please Enter the Modified Amount.");
                }

            }
        });
        btn_add_cheque=(Button) findViewById(R.id.btn_add_cheque);
        parentOfAllChequeRow=(LinearLayout) findViewById(R.id.parentOfAllChequeRow);
        btn_add_cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateChequeRowFillOrNot()){
                    addViewIntoTable("0","0",1,"NEW");
                }

            }
        });
    }
    public void addViewIntoTable( String oldChequeRecord ,String newChequeRecord ,int flagFromAddButtonOrLoading,String hashMapKey){

        final View view=getLayoutInflater().inflate(R.layout.cheque_row_collection_details,null);
        view.setTag(hashMapKey);
        final ImageView delete= (ImageView) view.findViewById(R.id.delete);
        delete.setTag(oldChequeRecord);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                if(view.getTag().toString().equals("NEW")){
                    view.setTag("NEW"+"DELETE");
                }
                else{
                    view.setTag("DELETE");
                }
            }
        });
        final EditText amountEdittextSecond= (EditText) view.findViewById(R.id.amountEdittextSecond);
        final EditText checqueNoEdittextSecond= (EditText) view.findViewById(R.id.checqueNoEdittextSecond);
        final TextView dateTextViewSecond= (TextView) view.findViewById(R.id.dateTextViewSecond);
        dateTextViewSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amountEdittextSecond.getText().toString().trim().equals("") && !checqueNoEdittextSecond.getText().toString().trim().equals("")){
                    chequeDate=(TextView) v;
                    calendar = Calendar.getInstance();

                    Year = calendar.get(Calendar.YEAR);
                    Month = calendar.get(Calendar.MONTH);
                    Day = calendar.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = DatePickerDialog.newInstance(
                            CollectionDetailsStoreWise.this, Year, Month, Day);

                    datePickerDialog.setThemeDark(false);

                    datePickerDialog.showYearPickerFirst(false);

                    Calendar calendarForSetDate = Calendar.getInstance();
                    calendarForSetDate.setTimeInMillis(System.currentTimeMillis());

                    // calendar.setTimeInMillis(System.currentTimeMillis()+24*60*60*1000);
                    // YOU can set min or max date using this code
                    // datePickerDialog.setMaxDate(Calendar.getInstance());
                    // datePickerDialog.setMinDate(calendar);

                    datePickerDialog.setAccentColor(Color.parseColor("#5c5696"));

                    datePickerDialog.setTitle("SELECT DATE");
                    datePickerDialog.setMinDate(calendarForSetDate);
                    datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

                }
                else{
                    if (amountEdittextSecond.getText().toString().trim().equals("")) {
                        showAlertSingleButtonError("Please Enter the Amount.");
                    } else if (checqueNoEdittextSecond.getText().toString().trim().equals("")) {
                        showAlertSingleButtonError("Please Enter ChequeNo/RefNo.");
                    } else {
                        showAlertSingleButtonError("Please Enter the Amount or ChequeNo/RefNo.");
                    }
                }

            }
        });
        final TextView BankSpinnerSecond= (TextView) view.findViewById(R.id.BankSpinnerSecond);
        BankSpinnerSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amountEdittextSecond.getText().toString().trim().equals("") && !checqueNoEdittextSecond.getText().toString().trim().equals("") && !dateTextViewSecond.getText().toString().trim().equals(""))
                {
                    alertDialog = new AlertDialog.Builder(CollectionDetailsStoreWise.this);
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = (View) inflater.inflate(R.layout.activity_list,
                            null);

                    listviewBankSecond = (ListView) convertView.findViewById(R.id.list_view);

                    int index = 0;
                    if (hashmapBank != null) {
                        bankSecondList = new String[hashmapBank.size() + 1];
                        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(
                                hashmapBank);
                        Set set2 = map.entrySet();
                        Iterator iterator = set2.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry me2 = (Map.Entry) iterator.next();
                            if (index == 0) {
                                bankSecondList[index] = "Select";

                                index = index + 1;

                                bankSecondList[index] = me2.getKey().toString().trim();

                                index = index + 1;
                            } else {
                                bankSecondList[index] = me2.getKey().toString().trim();

                                index = index + 1;
                            }

                        }

                    }
                    adapterBankSecond = new ArrayAdapter<String>(CollectionDetailsStoreWise.this,
                            R.layout.list_item, R.id.product_name, bankSecondList);

                    listviewBankSecond.setAdapter(adapterBankSecond);
                    inputSearch = (EditText) convertView
                            .findViewById(R.id.inputSearch);
                    inputSearch.setVisibility(View.VISIBLE);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle("Bank");
                    inputSearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                            // TODO Auto-generated method stub
                            adapterBankSecond.getFilter().filter(arg0.toString().trim());

                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1,
                                                      int arg2, int arg3) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                    listviewBankSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            String abc = listviewBankSecond.getItemAtPosition(arg2)
                                    .toString().trim();
                            BankSpinnerSecond.setText(abc);
                            inputSearch.setText("");

                            ad.dismiss();

                            if (abc.equals("Select")) {


                            } else {
                            }
                        }
                    });
                    ad = alertDialog.show();

                }
                else{
                    if (amountEdittextSecond.getText().toString().trim().equals("")) {
                        // allMessageAlert("Please Enter the Amount.");
                        showAlertSingleButtonError("Please Enter the Amount.");
                    } else if (checqueNoEdittextSecond.getText().toString().trim().equals("")) {
                        showAlertSingleButtonError("Please Enter ChequeNo/RefNo.");
                    } else if (dateTextViewSecond.getText().toString().trim().equals("")) {
                        showAlertSingleButtonError("Please Select the Date.");
                    } else {
                        showAlertSingleButtonError("Please Enter the Amount or ChequeNo/RefNo. or Select Date");
                    }
                }

            }
        });
        if((flagFromAddButtonOrLoading==0) && (!hashMapKey.equals("NEW"))){
            //means  function called from dynamic loading

            String PaymentMode_Old = oldChequeRecord.split(Pattern.quote("^"))[0];
            String PaymentModeID_Old = oldChequeRecord.split(Pattern.quote("^"))[1];
            String Amount_Old = oldChequeRecord.split(Pattern.quote("^"))[2];
            String RefNoChequeNoTrnNo_Old = oldChequeRecord.split(Pattern.quote("^"))[3];
            String Date_Old = oldChequeRecord.split(Pattern.quote("^"))[4];
            String Bank_Old = oldChequeRecord.split(Pattern.quote("^"))[5];
            amountEdittextSecond.setText(Amount_Old);
            checqueNoEdittextSecond.setText(RefNoChequeNoTrnNo_Old);
            dateTextViewSecond.setText(Date_Old);
            BankSpinnerSecond.setText(Bank_Old);
        }
        else if((flagFromAddButtonOrLoading==1) && (!hashMapKey.equals("NEW"))){

            //means  function called from AddCheque Button
            //so set no text
            String PaymentMode_Old = newChequeRecord.split(Pattern.quote("^"))[0];
            String PaymentModeID_Old = newChequeRecord.split(Pattern.quote("^"))[1];
            String Amount_Old = newChequeRecord.split(Pattern.quote("^"))[2];
            String RefNoChequeNoTrnNo_Old = newChequeRecord.split(Pattern.quote("^"))[3];
            String Date_Old = newChequeRecord.split(Pattern.quote("^"))[4];
            String Bank_Old = newChequeRecord.split(Pattern.quote("^"))[5];
            amountEdittextSecond.setText(Amount_Old);
            checqueNoEdittextSecond.setText(RefNoChequeNoTrnNo_Old);
            dateTextViewSecond.setText(Date_Old);
            BankSpinnerSecond.setText(Bank_Old);
        }
        parentOfAllChequeRow.addView(view);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String mon=MONTHS[monthOfYear];
        try{
            year=Integer.parseInt(String.valueOf(year).substring(2));
        }
        catch (Exception e){

        }
        chequeDate.setText(dayOfMonth+"-"+mon+"-"+year);
    }
    public void showAlertSingleButtonError(String msg)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.AlertDialogHeaderErrorMsg))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(R.drawable.error_ico)
                .setPositiveButton(getResources().getString(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
    public void getDataFromDataBase(){
        Double totalCollection = dbengine.fnGetAllCashCollectedAmountDetailsAgainstStore(storeID);
        Double modifiedValue = dbengine.fnfetchModifiedCollectionReportCashChange(storeID);
        tv_total_collection.setText(""+totalCollection);
        et_modified.setText("" + modifiedValue);

        if (modifiedValue != 0.0) {
            // et_modified.setText("" + modifiedValue);
        }
        int chequeChangeAgainstStore = dbengine.fnCheckflgCollectionReportChequeChangeAgainstStore(storeID);
        if (chequeChangeAgainstStore == 0) {
            //means no data in cheque table
            chequeAllDataArray = dbengine.fnRetrievetblCollectionChequeAgainstStore(storeID);
            setDataToLayout(chequeAllDataArray,0);
        } else {
            //means have data in cheque table
            chequeAllDataArray = dbengine.fnRetrievetblCollectionReportChequeChange(storeID);
            setDataToLayout(chequeAllDataArray,1);
        }

    }
    public void setDataToLayout(ArrayList<LinkedHashMap<String,LinkedHashMap<String,String>>> chequeAllDataArrayPassed,int flagOnlyOldDataOrBoth){
        if(chequeAllDataArrayPassed.size()>0){
            for(int i=0;i<chequeAllDataArrayPassed.size();i++){
                chequeRowData= chequeAllDataArrayPassed.get(i);
                //i+1+"^"+2 this key is using to save data thats why using this key
                for(Map.Entry<String,LinkedHashMap<String,String>>  entry:chequeRowData.entrySet()){

                    LinkedHashMap<String,String> OldAndNewChequeData=  entry.getValue();
                    String oldChequeRecord= OldAndNewChequeData.get("Old");
                    String newChequeRecord= OldAndNewChequeData.get("New");
                    if(flagOnlyOldDataOrBoth==0){
                        //means only old data ,so load layout by old data
                        addViewIntoTable(oldChequeRecord,newChequeRecord,0,entry.getKey());
                    }
                    else{
                        //means old and new both data ,so load layout by new  data
                        addViewIntoTable(oldChequeRecord,newChequeRecord,1,entry.getKey());
                    }

                }

            }
        }

    }
    public boolean validateChequeRowFillOrNot(){

        if((parentOfAllChequeRow!=null) && (parentOfAllChequeRow.getChildCount()>0)){
            int childCOut=    parentOfAllChequeRow.getChildCount();
            View view=   parentOfAllChequeRow.getChildAt(childCOut-1);
            EditText amountEdittextSecond= (EditText) view.findViewById(R.id.amountEdittextSecond);
            EditText checqueNoEdittextSecond= (EditText) view.findViewById(R.id.checqueNoEdittextSecond);
            TextView dateTextViewSecond= (TextView) view.findViewById(R.id.dateTextViewSecond);
            TextView BankSpinnerSecond= (TextView) view.findViewById(R.id.BankSpinnerSecond);
            if(amountEdittextSecond.getText().toString().trim().equals("")){
                showAlertSingleButtonError("Please Enter the Amount.");
                return false;
            }
            else  if(checqueNoEdittextSecond.getText().toString().trim().equals("")){
                showAlertSingleButtonError("Please Enter ChequeNo/RefNo.");
                return false;
            }
            else   if(dateTextViewSecond.getText().toString().trim().equals("")){
                showAlertSingleButtonError("Please select Data");
                return false;
            }
            else  if(BankSpinnerSecond.getText().toString().trim().equals("") || BankSpinnerSecond.getText().toString().trim().equals("Select")){
                showAlertSingleButtonError("Please select Bank");
                return false;
            }
            else{
                return true;
            }

        }
        else{
            return true;
        }
    }

    public void saveDataToDataBase(){
        if(parentOfAllChequeRow.getChildCount()>0){
            if((parentOfAllChequeRow!=null) && (parentOfAllChequeRow.getChildCount()>0)){
                dbengine.fnDeletetblCollectionReportChequeChange(storeID);
                for(int i=0;i<parentOfAllChequeRow.getChildCount();i++) {
                    View view = parentOfAllChequeRow.getChildAt(i);
                    EditText amountEdittextSecond = (EditText) view.findViewById(R.id.amountEdittextSecond);
                    EditText checqueNoEdittextSecond = (EditText) view.findViewById(R.id.checqueNoEdittextSecond);
                    TextView dateTextViewSecond = (TextView) view.findViewById(R.id.dateTextViewSecond);
                    TextView BankSpinnerSecond = (TextView) view.findViewById(R.id.BankSpinnerSecond);
                    ImageView delete = (ImageView) view.findViewById(R.id.delete);
                    String oldChequeRecord=   delete.getTag().toString();
                    if(view.getTag().equals("NEW")){
                        dbengine.fnsavetblCollectionReportChequeChange(storeID,"Cheque/DD","2",amountEdittextSecond.getText().toString().trim(),checqueNoEdittextSecond.getText().toString().trim(),dateTextViewSecond.getText().toString().trim(),BankSpinnerSecond.getText().toString().trim(),"Cheque/DD","2",amountEdittextSecond.getText().toString().trim(),checqueNoEdittextSecond.getText().toString().trim(),dateTextViewSecond.getText().toString().trim(),BankSpinnerSecond.getText().toString().trim(),3);
                    }
                    else if(view.getTag().equals("DELETE")){
                        dbengine.fnsavetblCollectionReportChequeChange(storeID,"Cheque/DD","2",amountEdittextSecond.getText().toString().trim(),checqueNoEdittextSecond.getText().toString().trim(),dateTextViewSecond.getText().toString().trim(),BankSpinnerSecond.getText().toString().trim(),"Cheque/DD","2",amountEdittextSecond.getText().toString().trim(),checqueNoEdittextSecond.getText().toString().trim(),dateTextViewSecond.getText().toString().trim(),BankSpinnerSecond.getText().toString().trim(),1);
                    }
                    else if(view.getTag().equals("NEWDELETE")){

                    }
                    else{
                        String PaymentMode_Old = oldChequeRecord.split(Pattern.quote("^"))[0];
                        String PaymentModeID_Old = oldChequeRecord.split(Pattern.quote("^"))[1];
                        String Amount_Old = oldChequeRecord.split(Pattern.quote("^"))[2];
                        String RefNoChequeNoTrnNo_Old = oldChequeRecord.split(Pattern.quote("^"))[3];
                        String Date_Old = oldChequeRecord.split(Pattern.quote("^"))[4];
                        String Bank_Old = oldChequeRecord.split(Pattern.quote("^"))[5];
                        int flag=2;
                        if(view.getTag().toString().contains("^")){
                            try{
                                flag=    Integer.parseInt(view.getTag().toString().split(Pattern.quote("^"))[1]);
                            }
                            catch (Exception e){
                                flag=2;
                            }
                            finally {

                            }

                        }
                        dbengine.fnsavetblCollectionReportChequeChange(storeID,PaymentMode_Old,PaymentModeID_Old,Amount_Old,RefNoChequeNoTrnNo_Old,Date_Old,Bank_Old,"Cheque/DD","2",amountEdittextSecond.getText().toString().trim(),checqueNoEdittextSecond.getText().toString().trim(),dateTextViewSecond.getText().toString().trim(),BankSpinnerSecond.getText().toString().trim(),flag);
                    }

                }
            }

        }

        dbengine.fnDeleteInsertCollectionReportCashChange(storeID,tv_total_collection.getText().toString().trim(),et_modified.getText().toString().trim(),"3");
    }
    public String validateAllRows(){
        String flag="TRUE";

        if((parentOfAllChequeRow!=null) && (parentOfAllChequeRow.getChildCount()>0)){
            for(int i=0;i<parentOfAllChequeRow.getChildCount();i++){
                View view=    parentOfAllChequeRow.getChildAt(i);


                EditText amountEdittextSecond= (EditText) view.findViewById(R.id.amountEdittextSecond);
                EditText checqueNoEdittextSecond= (EditText) view.findViewById(R.id.checqueNoEdittextSecond);
                TextView dateTextViewSecond= (TextView) view.findViewById(R.id.dateTextViewSecond);
                TextView BankSpinnerSecond= (TextView) view.findViewById(R.id.BankSpinnerSecond);
                if(amountEdittextSecond.getText().toString().trim().equals("")){
                    showAlertSingleButtonError("Please Enter the Amount.");
                    flag="FALSE";
                    break;

                }
                else  if(checqueNoEdittextSecond.getText().toString().trim().equals("")){
                    showAlertSingleButtonError("Please Enter ChequeNo/RefNo.");
                    flag="FALSE";
                    break;
                }
                else   if(dateTextViewSecond.getText().toString().trim().equals("")){
                    showAlertSingleButtonError("Please select Data");
                    flag="FALSE";
                    break;
                }
                else  if(BankSpinnerSecond.getText().toString().trim().equals("") || BankSpinnerSecond.getText().toString().trim().equals("Select")){
                    showAlertSingleButtonError("Please select Bank");
                    flag="FALSE";

                    break;

                }


            }


        }
        else{
            flag="TRUE";
        }
        return flag;
    }
}
