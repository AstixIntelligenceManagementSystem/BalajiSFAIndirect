package project.astix.com.balajisfaindirect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.astix.Common.CommonInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;

public class ProductEntryForm extends BaseActivity implements View.OnClickListener,CategoryCommunicator,focusLostCalled {

    //Invoice TextView
    public TextView tv_NetInvValue,tvTAmt,tvDis,tv_GrossInvVal,tvFtotal,tvAddDisc,tv_NetInvAfterDiscount,tvAmtPrevDueVAL,etAmtCollVAL
            ,tvAfterTaxValue,tvPreAmtOutstandingVALNew,tvAmtOutstandingVAL,tvCredAmtVAL,tvINafterCredVAL,textView1_CredAmtVAL_new,tvNoOfCouponValue,txttvCouponAmountValue;

    RecyclerView rv_prdct_detal;

    String progressTitle;
    ProgressDialog mProgressDialog;
    //Database
    PRJDatabase dbengine = new PRJDatabase(this);
    //Intent Data
    public String storeID,imei,date,pickerDate,SN;

    //Custom Keyboard
    CustomKeyboard mCustomKeyboardNum, mCustomKeyboardNumWithoutDecimal;

    //Initialize fields
    ImageView img_ctgry,btn_go,img_return,btn_bck;
    EditText ed_search;
    TextView txt_RefreshOdrTot;
    Button btn_InvoiceReview,btn_OrderReview;
    LinearLayout ll_scheme_detail;

    //hmap for Products Info and Saving
    LinkedHashMap<String, String> hmapFilterProductList ;
    LinkedHashMap<String, String> hmapctgry_details;
    HashMap<String, String> hmapProductIdStock;
    ArrayList<HashMap<String, String>> arrLstHmapPrdct;
    HashMap<String, String> hmapProductStandardRateBeforeTax;
    HashMap<String, String> hmapProductMRP;
    HashMap<String, Integer> hmapDistPrdctStockCount;// = new HashMap<String, Integer>();
    HashMap<String, String> hmapProductVatTaxPerventage;
    HashMap<String, String> hmapCtgryPrdctDetail;
    HashMap<String, String> hmapPrdctIdPrdctName;
    HashMap<String, String> hmapProductStandardRate;// = new HashMap<String, String>();
    HashMap<String, String> hmapProductTaxValue;
    HashMap<String, String> hmapProductSelectedUOMId ;
    HashMap<String, String> hmapLineValBfrTxAftrDscnt;
    HashMap<String, String> hmapLineValAftrTxAftrDscnt;
    HashMap<String, String> hmapProductIdLastStock;
    LinkedHashMap<String,String> hampGetLastProductExecution;
    HashMap<String, String> hmapPrdctOdrQty;
    //ModelClass
    public int PriceApplyDiscountLevelType = 0, flgTransferStatus = 0;;

    public ProductFilledDataModel prdctModelArrayList=new ProductFilledDataModel();

    public int StoreCurrentStoreType = 0,chkflgInvoiceAlreadyGenerated=0;
    public String strFinalAllotedInvoiceIds = "NA",TmpInvoiceCodePDA = "NA",StoreVisitCode = "NA",defaultCatName_Id = "0",previousSlctdCtgry,distID = "",VisitTypeStatus = "1";
    List<String> categoryNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_entry_form);

        mProgressDialog = new ProgressDialog(ProductEntryForm.this);
        initializeFields();
        getDataFromatabase();
        setInvoiceTableView();
        orderBookingTotalCalc();
    }


    private void initializeFields() {

        getDataFromIntent();
        mCustomKeyboardNum = new CustomKeyboard(this, R.id.keyboardviewNum, R.xml.num);
        mCustomKeyboardNumWithoutDecimal = new CustomKeyboard(this, R.id.keyboardviewNumDecimal, R.xml.num_without_decimal);



        img_ctgry = (ImageView) findViewById(R.id.img_ctgry);
        ed_search = (EditText) findViewById(R.id.ed_search);
        btn_go = (ImageView) findViewById(R.id.btn_go);
        txt_RefreshOdrTot = (TextView) findViewById(R.id.txt_RefreshOdrTot);
        btn_InvoiceReview = (Button) findViewById(R.id.btn_InvoiceReview);
        btn_OrderReview = (Button) findViewById(R.id.btn_OrderReview);
        img_return = (ImageView) findViewById(R.id.img_return);
        btn_bck = (ImageView) findViewById(R.id.btn_bck);
        rv_prdct_detal=(RecyclerView)findViewById(R.id.rv_prdct_detal);
        ll_scheme_detail = (LinearLayout) findViewById(R.id.ll_scheme_detail);

        img_ctgry.setOnClickListener(this);
        btn_bck.setOnClickListener(this);
        btn_InvoiceReview.setOnClickListener(this);
        btn_OrderReview.setOnClickListener(this);
        ed_search.setOnClickListener(this);
        btn_go.setOnClickListener(this);


    }
    private void getDataFromIntent() {


        Intent passedvals = getIntent();

        storeID = passedvals.getStringExtra("storeID");
        imei = passedvals.getStringExtra("imei");
        date = passedvals.getStringExtra("userdate");
        pickerDate = passedvals.getStringExtra("pickerDate");
        SN = passedvals.getStringExtra("SN");


    }

    public void searchProduct(String filterSearchText,String ctgryId)
    {

        if(hmapFilterProductList!=null)
        {
            hmapFilterProductList.clear();
        }

        hmapFilterProductList=dbengine.getFileredProductListMap(filterSearchText.trim(),StoreCurrentStoreType,ctgryId);

        if(hmapFilterProductList.size()>0)
        {
            String[] listProduct=new String[hmapFilterProductList.size()];
            int index=0;
            for(Map.Entry<String,String> entry:hmapFilterProductList.entrySet())
            {
                listProduct[index]=entry.getKey()+"^"+entry.getValue();
                index++;
            }

            OrderAdapter orderAdapter=new OrderAdapter(ProductEntryForm.this,listProduct,hmapFilterProductList,hmapProductStandardRateBeforeTax,hmapProductMRP,hmapProductIdStock,hmapProductVatTaxPerventage,hmapProductIdLastStock,hampGetLastProductExecution,prdctModelArrayList);
            rv_prdct_detal.setAdapter(orderAdapter);
            rv_prdct_detal.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        }
        else
        {
            allMessageAlert(ProductEntryForm.this.getResources().getString(R.string.AlertFilter));
        }


    }

    private void allMessageAlert(String message) {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(ProductEntryForm.this);
        alertDialogNoConn.setTitle(ProductEntryForm.this.getResources().getString(R.string.genTermInformation));
        alertDialogNoConn.setMessage(message);
        //alertDialogNoConn.setMessage(getText(R.string.connAlertErrMsg));
        alertDialogNoConn.setNeutralButton(ProductEntryForm.this.getResources().getString(R.string.AlertDialogOkButton),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        ed_search.requestFocus();
	                     /*if(isMyServiceRunning())
	               		{
	                     stopService(new Intent(DynamicActivity.this,GPSTrackerService.class));
	               		}
	                     finish();*/
                        //finish();
                    }
                });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

    public void getDataFromatabase()
    {

        StoreVisitCode = dbengine.fnGetStoreVisitCode(storeID);
        StoreCurrentStoreType=Integer.parseInt(dbengine.fnGetStoreTypeOnStoreIdBasis(storeID));
        chkflgInvoiceAlreadyGenerated=dbengine.fnCheckForNewInvoiceOrPreviousValue(storeID,StoreVisitCode);//0=Need to Generate Invoice Number,1=No Need of Generating Invoice Number
        if(chkflgInvoiceAlreadyGenerated==1)
        {
            TmpInvoiceCodePDA=dbengine.fnGetInvoiceCodePDA(storeID,StoreVisitCode);

        }
        else
        {
            TmpInvoiceCodePDA=genTempInvoiceCodePDA();
        }
        hmapProductIdStock=dbengine.fetchActualVisitData(storeID);
        hmapDistPrdctStockCount=dbengine.fnGetFinalInvoiceQtyProductWise();
        distID=dbengine.getDisId(storeID);
        getCategoryDetail();
        getPrductInfoDetail();


        if(defaultCatName_Id.contains("^"))
        {
            ed_search.setText(defaultCatName_Id.split(Pattern.quote("^"))[0]);

            searchProduct(defaultCatName_Id.split(Pattern.quote("^"))[0],defaultCatName_Id.split(Pattern.quote("^"))[1]);
        }

    }

    public void getPrductInfoDetail()
    {
        hmapProductIdLastStock=dbengine.fnGetLastStockByDMS_Or_SFA(storeID);
        arrLstHmapPrdct = dbengine.fetch_catgry_prdctsData(storeID, StoreCurrentStoreType);
        hampGetLastProductExecution=dbengine.fnGetHampGetLastProductExecution(storeID);
        if(arrLstHmapPrdct!=null && arrLstHmapPrdct.size()>0)
        {
            hmapCtgryPrdctDetail = arrLstHmapPrdct.get(0);

            hmapPrdctIdPrdctName = arrLstHmapPrdct.get(5);
            hmapProductVatTaxPerventage = arrLstHmapPrdct.get(8);
            hmapProductMRP = arrLstHmapPrdct.get(9);
            hmapProductTaxValue = arrLstHmapPrdct.get(12);
            hmapProductStandardRate = arrLstHmapPrdct.get(15);
            hmapProductStandardRateBeforeTax = arrLstHmapPrdct.get(16);
            hmapProductSelectedUOMId = arrLstHmapPrdct.get(25);
            hmapLineValBfrTxAftrDscnt = arrLstHmapPrdct.get(26);
            hmapLineValAftrTxAftrDscnt = arrLstHmapPrdct.get(27);

        }
        hmapPrdctOdrQty=dbengine.fnGetProductPurchaseList(storeID,TmpInvoiceCodePDA);
        if(hmapPrdctOdrQty!=null && hmapPrdctOdrQty.size()>0)
        {
            for(Map.Entry<String,String> entry:hmapPrdctOdrQty.entrySet())
            {
                if(Integer.parseInt(entry.getValue())>0)
                {
                    prdctModelArrayList.setPrdctQty(entry.getKey(),entry.getValue());
                }

            }

        }

    }

    private void getCategoryDetail() {

        hmapctgry_details = dbengine.fetch_Category_List();

        int index = 0;
        if (hmapctgry_details != null) {
            categoryNames = new ArrayList<String>();
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapctgry_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while (iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry) iterator.next();
                categoryNames.add(me2.getKey().toString());
                if (index == 0) {
                    defaultCatName_Id = me2.getKey().toString() + "^" + me2.getValue().toString();
                }
                index = index + 1;
            }
        }


    }

    public String genTempInvoiceCodePDA()
    {
        //store ID generation <x>
        long syncTIMESTAMP = System.currentTimeMillis();
        Date dateobj = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        String VisitStartTS = df.format(dateobj);
        String cxz;
        cxz = UUID.randomUUID().toString();
        StringTokenizer tokens = new StringTokenizer(String.valueOf(cxz), "-");

        String val1 = tokens.nextToken().trim();
        String val2 = tokens.nextToken().trim();
        String val3 = tokens.nextToken().trim();
        String val4 = tokens.nextToken().trim();
        cxz = tokens.nextToken().trim();
        String IMEIid =  CommonInfo.imei.substring(9);
        //cxz = IMEIid +"-"+cxz;
        cxz = "TmpInvoiceCodePDA" + "-" +IMEIid +"-"+cxz+"-"+VisitStartTS.replace(" ", "").replace(":", "").trim();


        return cxz;
        //-_
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_ctgry:
                img_ctgry.setEnabled(false);
                customAlertStoreList(categoryNames, "Select Category");
                break;
            case R.id.btn_bck:
                fnCreditAndStockCal(5);
                break;
            case R.id.btn_InvoiceReview:

                fnCreditAndStockCal(0);
                break;
            case R.id.btn_OrderReview:
                fnCreditAndStockCal(1);
                break;
            case R.id.ed_search:
                mCustomKeyboardNumWithoutDecimal.hideCustomKeyboard();
                // mCustomKeyboardNum.hideCustomKeyboard();
                break;
            case R.id.btn_go:
                if (!TextUtils.isEmpty(ed_search.getText().toString().trim())) {
                    if (!ed_search.getText().toString().trim().equals("")) {
                        searchProduct(ed_search.getText().toString().trim(), "");

                    }


                }
        }


    }

    public void customAlertStoreList(final List<String> listOption, String sectionHeader)
    {

        final Dialog listDialog = new Dialog(ProductEntryForm.this);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listDialog.setContentView(R.layout.search_list);
        listDialog.setCanceledOnTouchOutside(false);
        listDialog.setCancelable(false);
        WindowManager.LayoutParams parms = listDialog.getWindow().getAttributes();
        parms.gravity = Gravity.CENTER;
        //there are a lot of settings, for dialog, check them all out!
        parms.dimAmount = (float) 0.5;




        TextView txt_section=(TextView) listDialog.findViewById(R.id.txt_section);
        txt_section.setText(sectionHeader);
        TextView txtVwCncl=(TextView) listDialog.findViewById(R.id.txtVwCncl);
        //    TextView txtVwSubmit=(TextView) listDialog.findViewById(R.id.txtVwSubmit);

        final EditText ed_search=(AutoCompleteTextView) listDialog.findViewById(R.id.ed_search);
        ed_search.setVisibility(View.GONE);
        final ListView list_store=(ListView) listDialog.findViewById(R.id.list_store);
        final CardArrayAdapterCategory cardArrayAdapter = new CardArrayAdapterCategory(ProductEntryForm.this,listOption,listDialog,previousSlctdCtgry);

        //img_ctgry.setText(previousSlctdCtgry);





        list_store.setAdapter(cardArrayAdapter);
        //	editText.setBackgroundResource(R.drawable.et_boundary);
        img_ctgry.setEnabled(true);





        txtVwCncl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                img_ctgry.setEnabled(true);


            }
        });




        //now that the dialog is set up, it's time to show it
        listDialog.show();

    }

    @Override
    public void selectedOption(String selectedCategory, Dialog dialog) {
        dialog.dismiss();
        previousSlctdCtgry=selectedCategory;
        String lastTxtSearch=ed_search.getText().toString().trim();
        //img_ctgry.setText(selectedCategory);
        ed_search.setText(previousSlctdCtgry);
        if(hmapctgry_details.containsKey(selectedCategory))
        {
            searchProduct(selectedCategory,hmapctgry_details.get(selectedCategory));
        }
        else
        {
            searchProduct(selectedCategory,"");
        }



    }

    public void fnCreditAndStockCal(int butnClkd)
    {
        EditText ed_LastEditextFocusd=prdctModelArrayList.getLastEditText();
        if(ed_LastEditextFocusd!=null)
        {
            String ProductIdOnClickedEdit=ed_LastEditextFocusd.getTag().toString().split(Pattern.quote("_"))[0];
            String tag=ed_LastEditextFocusd.getTag().toString();
            if(tag.contains("etOrderQty"))
            {
                String prdctQty=   prdctModelArrayList.getPrdctOrderQty(ProductIdOnClickedEdit);

                if(!TextUtils.isEmpty(prdctQty))
                {


                    int originalNetQntty=Integer.parseInt(prdctQty);
                    int totalStockLeft=hmapDistPrdctStockCount.get(ProductIdOnClickedEdit);

                    if (originalNetQntty>totalStockLeft)
                    {

                        alertForOrderExceedStock(ProductIdOnClickedEdit,ed_LastEditextFocusd,ed_LastEditextFocusd,-1);
                    }
                    else
                    {

                        nextStepAfterRetailerCreditBal(butnClkd);



                    }

                }
                else
                {


                    nextStepAfterRetailerCreditBal(butnClkd);


                }

            }


            else
            {

                nextStepAfterRetailerCreditBal(butnClkd);


            }
        }
        else
        {
            nextStepAfterRetailerCreditBal(butnClkd);

        }


    }

    public void nextStepAfterRetailerCreditBal(int btnClkd)
    {
        if(btnClkd==0 || btnClkd==1) // Invoice Review
        {
            orderInvoiceReviewClkd(btnClkd);
        }

        if(btnClkd==5)// btn back pressed
        {
            orderInvoiceReviewClkd(btnClkd);

        }
    }

    public void orderInvoiceReviewClkd(int btnClkd)
    {
        long StartClickTime = System.currentTimeMillis();
        Date dateobj1 = new Date(StartClickTime);
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
        String StartClickTimeFinal = df1.format(dateobj1);

        String fileName=imei+"_"+storeID;
        //StringBuffer content=new StringBuffer(imei+"_"+storeID+"_"+"SaveExit Button Click on Product List"+StartClickTimeFinal);
        //File file = new File("/sdcard/MeijiIndirectTextFile/"+fileName);
        File file = new File("/sdcard/"+ CommonInfo.TextFileFolder+"/"+fileName);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        CommonInfo.fileContent= CommonInfo.fileContent+"     "+imei+"_"+storeID+"_"+"SaveExit Button Click on Product List"+StartClickTimeFinal;
        FileWriter fw;
        try
        {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(CommonInfo.fileContent);
            bw.close();
            //dbengine.open();
            dbengine.savetblMessageTextFileContainer(fileName,0);
            //dbengine.close();
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }




        progressTitle=ProductEntryForm.this.getResources().getString(R.string.WhileWeSaveExit);
        //  progressTitle="While we save your data then review Order";
        new SaveData().execute(String.valueOf(btnClkd));
    }

    public void alertForOrderExceedStock(final String productOIDClkd, final EditText edOrderCurrent, final EditText edOrderCurrentLast, final int flagClkdButton)
    {

        final Dialog listDialog = new Dialog(ProductEntryForm.this);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listDialog.setContentView(R.layout.custom_stock_alert);
        listDialog.setCanceledOnTouchOutside(false);
        listDialog.setCancelable(false);
        WindowManager.LayoutParams parms = listDialog.getWindow().getAttributes();
        parms.gravity =Gravity.CENTER;
        parms.width=WindowManager.LayoutParams.MATCH_PARENT;
        //there are a lot of settings, for dialog, check them all out!
        parms.dimAmount = (float) 0.5;


        final int avilabQty=hmapDistPrdctStockCount.get(productOIDClkd);

        TextView order_detail=(TextView) listDialog.findViewById(R.id.order_detail);

        order_detail.setText(hmapFilterProductList.get(productOIDClkd)+"\n\n"+ProductEntryForm.this.getResources().getString(R.string.AvailableQty)+avilabQty +"\n"+ProductEntryForm.this.getResources().getString(R.string.RqrdQty)+prdctModelArrayList.getPrdctOrderQty(productOIDClkd)+"\n"+getText(R.string.order_exceeds_stock));
        Button btn_done= (Button) listDialog.findViewById(R.id.btn_done);
        final EditText editText_prdctQty= (EditText) listDialog.findViewById(R.id.editText_prdctQty);
        editText_prdctQty.setText(""+avilabQty);
        editText_prdctQty.setVisibility(View.GONE);
        EditText ed_extraQty= (EditText) listDialog.findViewById(R.id.ed_extraQty);
        ed_extraQty.setVisibility(View.GONE);

        btn_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                prdctModelArrayList.removePrdctQty(productOIDClkd);

                edOrderCurrentLast.setText("");

                edOrderCurrent.clearFocus();
                edOrderCurrentLast.requestFocus();
                //alertForOrderExceedStock(productOIDClkd,edOrderCurrent,edOrderCurrentLast,-1);

            }
        });




        //now that the dialog is set up, it's time to show it
        listDialog.show();

    }

    @Override
    public void fcsLstCld(boolean hasFocus, EditText editText) {
        mCustomKeyboardNumWithoutDecimal.hideCustomKeyboard();
        if(!hasFocus)
        {
            orderBookingTotalCalc();
        }
        else
        {
            if(editText.getTag().toString().contains("etOrderQty"))
            {

                mCustomKeyboardNumWithoutDecimal.registerEditText(editText);
                mCustomKeyboardNumWithoutDecimal.showCustomKeyboard(editText);
            }
            else
            {


            }

        }

    }


    public class SaveData extends AsyncTask<String, String, Void>
    {
        int btnClkd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Text need to e changed according to btn Click
            orderBookingTotalCalc();

            if(mProgressDialog.isShowing()==false)
            {
                mProgressDialog = new ProgressDialog(ProductEntryForm.this);
                mProgressDialog.setTitle(ProductEntryForm.this.getResources().getString(R.string.Loading));
                mProgressDialog.setMessage(progressTitle);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            String executedData=params[0];



            btnClkd=Integer.parseInt(executedData);



            fnSaveFilledDataToDatabase(btnClkd);
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {

            if(mProgressDialog.isShowing()==true)
            {
                mProgressDialog.dismiss();
            }
            long syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
            String startTS = df.format(dateobj);
            //dbengine.open();
            dbengine.UpdateStoreEndVisit(storeID,startTS);
            //dbengine.close();


            if(btnClkd==0) {
                Intent storeOrderReviewIntent = new Intent(ProductEntryForm.this, ProductInvoiceReview.class);
                storeOrderReviewIntent.putExtra("storeID", storeID);
                storeOrderReviewIntent.putExtra("SN", SN);
                storeOrderReviewIntent.putExtra("bck", 1);
                storeOrderReviewIntent.putExtra("imei", imei);
                storeOrderReviewIntent.putExtra("userdate", date);
                storeOrderReviewIntent.putExtra("pickerDate", pickerDate);
                storeOrderReviewIntent.putExtra("OrderPDAID", TmpInvoiceCodePDA);
                startActivity(storeOrderReviewIntent);
                finish();
            }
            if(btnClkd==1) {
                Intent storeOrderReviewIntent = new Intent(ProductEntryForm.this, ProductOrderReview.class);
                storeOrderReviewIntent.putExtra("storeID", storeID);
                storeOrderReviewIntent.putExtra("SN", SN);
                storeOrderReviewIntent.putExtra("bck", 1);
                storeOrderReviewIntent.putExtra("imei", imei);
                storeOrderReviewIntent.putExtra("userdate", date);
                storeOrderReviewIntent.putExtra("pickerDate", pickerDate);
                storeOrderReviewIntent.putExtra("OrderPDAID", TmpInvoiceCodePDA);
                startActivity(storeOrderReviewIntent);
                finish();
            }
            if(btnClkd==5) {
                Intent fireBackDetPg = new Intent(ProductEntryForm.this,ActualVisitStock.class);

                fireBackDetPg.putExtra("storeID", storeID);
                fireBackDetPg.putExtra("SN", SN);
                fireBackDetPg.putExtra("imei", imei);
                fireBackDetPg.putExtra("userdate", date);
                fireBackDetPg.putExtra("pickerDate", pickerDate);

                startActivity(fireBackDetPg);
                finish();
            }

        }


    }


    public void fnSaveFilledDataToDatabase(int valBtnClickedFrom)
    {

        int Outstat=1;
        TransactionTableDataDeleteAndSaving(Outstat);
        InvoiceTableDataDeleteAndSaving(Outstat,flgTransferStatus);
        //dbengine.open();
        dbengine.UpdateStoreFlag(storeID.trim(), 1);
        dbengine.UpdateStoreOtherMainTablesFlag(storeID.trim(), 1,TmpInvoiceCodePDA,TmpInvoiceCodePDA);
        dbengine.UpdateStoreStoreReturnDetail(storeID.trim(),"1",TmpInvoiceCodePDA,TmpInvoiceCodePDA);
        dbengine.UpdateStoreProductAppliedSchemesBenifitsRecords(storeID.trim(),"1",TmpInvoiceCodePDA,TmpInvoiceCodePDA);
        //dbengine.close();

        if(dbengine.checkCountIntblStoreSalesOrderPaymentDetails(storeID,TmpInvoiceCodePDA,TmpInvoiceCodePDA)==0)
        {
            String strDefaultPaymentStageForStore=dbengine.fnGetDefaultStoreOrderPAymentDetails(storeID);
            if(!strDefaultPaymentStageForStore.equals(""))
            {
                //dbengine.open();
                dbengine. fnsaveStoreSalesOrderPaymentDetails(storeID,TmpInvoiceCodePDA,strDefaultPaymentStageForStore,"1",TmpInvoiceCodePDA);
                //dbengine.close();
            }
        }
        long  syncTIMESTAMP = System.currentTimeMillis();
        Date dateobj = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
        String StampEndsTime = df.format(dateobj);



    }


    public void TransactionTableDataDeleteAndSaving(int Outstat)
    {

        dbengine.deleteStoreRecordFromtblStorePurchaseDetailsFromProductTrsaction(storeID,TmpInvoiceCodePDA,TmpInvoiceCodePDA);

        LinkedHashMap<String,String> hmapPrdctOrderQty=prdctModelArrayList.getHmapPrdctOrderQty();
        if(hmapPrdctOrderQty!=null)
        {
            for (Map.Entry<String, String> entry:hmapPrdctOrderQty.entrySet() )
            {
                int PCateId=Integer.parseInt(hmapCtgryPrdctDetail.get(entry.getKey()));
                String PName =hmapPrdctIdPrdctName.get(entry.getKey());
                String ProductID=entry.getKey();
                String ProductStock ="0";
                if(hmapProductIdStock!=null && hmapProductIdStock.containsKey(ProductID))
                {
                    ProductStock= hmapProductIdStock.get(ProductID);
                }


                int ProductExtraOrder=0;
                double TaxRate=0.00;
                double TaxValue=0.00;
                if(ProductStock==null)
                {
                    ProductStock="0";
                }
                String SampleQTY ="0";
                if(SampleQTY.equals(""))
                {
                    SampleQTY="0";
                }
                String OrderQTY =entry.getValue();

                if(OrderQTY.equals(""))
                {
                    OrderQTY="0";

                }
                String OrderValue="0";
                if(Integer.parseInt(OrderQTY)>0)
                {
                    OrderValue = String.valueOf(prdctModelArrayList.getPrdctOrderVal(ProductID));// ((TextView)(vRow).findViewById(R.id.tv_Orderval)).getText().toString();
                    if(OrderValue.equals(""))
                    {
                        OrderValue="0";
                    }
                }

                String OrderFreeQty ="0";

                String OrderDisVal= "0";

                String PRate="0.00";
                int flgIsQuoteRateApplied=0;
                PRate=hmapProductStandardRate.get(ProductID);
                TaxRate=Double.parseDouble(hmapProductVatTaxPerventage.get(ProductID));
                TaxValue=Double.parseDouble(hmapProductTaxValue.get(ProductID));

                if(Integer.valueOf(OrderFreeQty)>0 || Integer.valueOf(SampleQTY)>0 || Integer.valueOf(OrderQTY)>0 || Integer.valueOf(OrderValue)>0 || Integer.valueOf(OrderDisVal)>0 || ProductExtraOrder>0)// || Integer.valueOf(ProductStock)>0
                {

                    int flgRuleTaxVal=1;
                    dbengine.fnsaveStoreTempOrderEntryDetails(TmpInvoiceCodePDA,storeID,""+PCateId,ProductID,Double.parseDouble(PRate),TaxRate,flgRuleTaxVal,Integer.parseInt(OrderQTY),Integer.parseInt(hmapProductSelectedUOMId.get(ProductID)),Double.parseDouble(hmapLineValBfrTxAftrDscnt.get(ProductID)),Double.parseDouble(hmapLineValAftrTxAftrDscnt.get(ProductID)),Integer.parseInt(OrderFreeQty.split(Pattern.quote("."))[0]),Double.parseDouble(OrderDisVal),Integer.parseInt(SampleQTY),PName,TaxValue,TmpInvoiceCodePDA,flgIsQuoteRateApplied,PriceApplyDiscountLevelType,distID,Outstat,ProductExtraOrder);
                    //dbengine.close();
                }


            }
        }


    }
    public void InvoiceTableDataDeleteAndSaving(int Outstat,int flgTransferStatus)
    {

        dbengine.deleteOldStoreInvoice(storeID,TmpInvoiceCodePDA,TmpInvoiceCodePDA);

        Double TBtaxDis;
        Double TAmt;
        Double Dis;
        Double INval;

        Double AddDis;
        Double InvAfterDis;

        Double INvalCreditAmt;
        Double INvalInvoiceAfterCreditAmt;

        Double INvalInvoiceOrginal=0.00;


        int Ftotal;




        if(!tv_NetInvValue.getText().toString().isEmpty()){
            TBtaxDis = Double.parseDouble(tv_NetInvValue.getText().toString().trim());
        }
        else{
            TBtaxDis = 0.00;
        }
        if(!tvTAmt.getText().toString().isEmpty()){
            TAmt = Double.parseDouble(tvTAmt.getText().toString().trim());
        }
        else{
            TAmt = 0.00;
        }
        if(!tvDis.getText().toString().isEmpty()){
            Dis = Double.parseDouble(tvDis.getText().toString().trim());
        }
        else{
            Dis = 0.00;
        }
        if(!tv_GrossInvVal.getText().toString().isEmpty()){


            INval = Double.parseDouble(tv_GrossInvVal.getText().toString().trim());
        }
        else{
            INval = 0.00;
        }
        if(!tvFtotal.getText().toString().isEmpty()){
            Double FtotalValue=Double.parseDouble(tvFtotal.getText().toString().trim());
            Ftotal =FtotalValue.intValue();
        }
        else{
            Ftotal = 0;
        }

        if(!tv_NetInvAfterDiscount.getText().toString().isEmpty()){
            InvAfterDis = Double.parseDouble(tv_NetInvAfterDiscount.getText().toString().trim());
        }
        else{
            InvAfterDis = 0.00;
        }
        if(!tvAddDisc.getText().toString().isEmpty()){
            AddDis = Double.parseDouble(tvAddDisc.getText().toString().trim());
        }
        else{
            AddDis = 0.00;
        }


        Double AmtPrevDueVA=0.00;
        Double AmtCollVA=0.00;
        Double AmtOutstandingVAL=0.00;
        if(!tvAmtPrevDueVAL.getText().toString().isEmpty()){
            AmtPrevDueVA = Double.parseDouble(tvAmtPrevDueVAL.getText().toString().trim());
        }
        else{
            AmtPrevDueVA = 0.00;
        }
        if(!etAmtCollVAL.getText().toString().isEmpty()){
            AmtCollVA = Double.parseDouble(etAmtCollVAL.getText().toString().trim());
        }
        else{
            AmtCollVA = 0.00;
        }

        if(!tvAmtOutstandingVAL.getText().toString().isEmpty()){
            AmtOutstandingVAL = Double.parseDouble(tvAmtOutstandingVAL.getText().toString().trim());
        }
        else{
            AmtOutstandingVAL = 0.00;
        }

        int NoOfCouponValue=0;
		/*if(!txttvNoOfCouponValue.getText().toString().isEmpty()){
			NoOfCouponValue = Integer.parseInt(txttvNoOfCouponValue.getText().toString().trim());
		}
		else{
			NoOfCouponValue = 0;
		}
		*/
        Double TotalCoupunAmount=0.00;
        if(!txttvCouponAmountValue.getText().toString().isEmpty()){
            TotalCoupunAmount = Double.parseDouble(txttvCouponAmountValue.getText().toString().trim());
        }
        else{
            TotalCoupunAmount = 0.00;
        }

        int flgRuleTaxVal=1;
        int flgTransType=1;
        //dbengine.open();
        //dbengine.saveStoreInvoice(imei,storeID, pickerDate, TBtaxDis, TAmt, Dis, INval, Ftotal, InvAfterDis, AddDis, AmtPrevDueVA, AmtCollVA, AmtOutstandingVAL, NoOfCouponValue, TotalCoupunAmount,Outstat,strGlobalOrderID,TmpInvoiceCodePDA,strFinalAllotedInvoiceIds);//, INvalCreditAmt, INvalInvoiceAfterCreditAmt, valInvoiceOrginal);

        dbengine.saveStoreTempInvoice(StoreVisitCode, TmpInvoiceCodePDA, storeID, pickerDate, TBtaxDis, TAmt, Dis, INval, Ftotal, InvAfterDis, AddDis, NoOfCouponValue, TotalCoupunAmount, pickerDate, flgTransType, PriceApplyDiscountLevelType, flgRuleTaxVal, Outstat,flgTransferStatus);//strFinalAllotedInvoiceIds);//, INvalCreditAmt, INvalInvoiceAfterCreditAmt, valInvoiceOrginal);

        //dbengine.close();



    }

    public void orderBookingTotalCalc() {



        Double StandardRate = 0.00;
        Double StandardRateBeforeTax = 0.00;

        Double ActualRateAfterDiscountBeforeTax = 0.00;
        Double DiscountAmount = 0.00;
        Double ActualTax = 0.00;
        Double ActualRateAfterDiscountAfterTax = 0.00;


        Double TotalFreeQTY = 0.00;
        Double TotalProductLevelDiscount = 0.00;
        Double TotalOrderValBeforeTax = 0.00;

        Double TotTaxAmount = 0.00;
        Double TotOderValueAfterTax = 0.00;

        LinkedHashMap<String,String> hmapPrdctOrderQty=prdctModelArrayList.getHmapPrdctOrderQty();
        if(hmapPrdctOrderQty!=null)
        {
            for (Map.Entry<String,String> entry:hmapPrdctOrderQty.entrySet()) {



                String ProductID=entry.getKey();
                String prdctQty=entry.getValue();
                //((TextView) (vRow).findViewById(R.id.txtVwRate)).setText("" + hmapProductStandardRate.get(ProductID).toString());

                if ((!TextUtils.isEmpty(prdctQty)) &&(Integer.parseInt(prdctQty)>0)) {

                    StandardRate = Double.parseDouble(hmapProductStandardRate.get(ProductID));///((1+(Double.parseDouble(hmapProductRetailerMarginPercentage.get(ProductID))/100)));
                    StandardRateBeforeTax = StandardRate / (1 + (Double.parseDouble(hmapProductVatTaxPerventage.get(ProductID)) / 100));

                    //If No Percentage Discount or Flat Discount is Applicable Code Starts Here
                    ActualRateAfterDiscountBeforeTax = StandardRateBeforeTax;
                    DiscountAmount = 0.00;
                    ActualTax = ActualRateAfterDiscountBeforeTax * (Double.parseDouble(hmapProductVatTaxPerventage.get(ProductID)) / 100);
                    ActualRateAfterDiscountAfterTax = ActualRateAfterDiscountBeforeTax * (1 + (Double.parseDouble(hmapProductVatTaxPerventage.get(ProductID)) / 100));

                    Double DiscAmtOnPreQtyBasic = DiscountAmount * Double.parseDouble(prdctQty);

                    Double DiscAmtOnPreQtyBasicToDisplay = DiscAmtOnPreQtyBasic;
                    DiscAmtOnPreQtyBasicToDisplay = Double.parseDouble(new DecimalFormat("##.##").format(DiscAmtOnPreQtyBasicToDisplay));



                    TotalProductLevelDiscount = TotalProductLevelDiscount + DiscAmtOnPreQtyBasic;
                    TotTaxAmount = TotTaxAmount + (ActualTax * Double.parseDouble(prdctQty));

                    Double TaxValue = ActualTax * Double.parseDouble(prdctQty);
                    TaxValue = Double.parseDouble(new DecimalFormat("##.##").format(TaxValue));
                    hmapProductTaxValue.put(ProductID, "" + TaxValue);

                    Double OrderValPrdQtyBasis = ActualRateAfterDiscountAfterTax * Double.parseDouble(prdctQty);
                    hmapLineValAftrTxAftrDscnt.put(ProductID, "" + OrderValPrdQtyBasis);




                    TotalOrderValBeforeTax = TotalOrderValBeforeTax + (ActualRateAfterDiscountBeforeTax * Double.parseDouble(prdctQty));
                    hmapLineValBfrTxAftrDscnt.put(ProductID, "" + ActualRateAfterDiscountBeforeTax * Double.parseDouble(prdctQty));
                    TotOderValueAfterTax = TotOderValueAfterTax + OrderValPrdQtyBasis;

                    //If No Percentage Discount or Flat Discount is Applicable Code Ends Here


                }

            }
        }

        //Now the its Time to Show the OverAll Summary Code Starts Here

        tvFtotal.setText(("" + TotalFreeQTY).trim());

        TotalProductLevelDiscount = Double.parseDouble(new DecimalFormat("##.##").format(TotalProductLevelDiscount));
        tvDis.setText(("" + TotalProductLevelDiscount).trim());

        TotalOrderValBeforeTax = Double.parseDouble(new DecimalFormat("##.##").format(TotalOrderValBeforeTax));
        tv_NetInvValue.setText(("" + TotalOrderValBeforeTax).trim());

        String percentBenifitMax = dbengine.fnctnGetMaxAssignedBen8DscntApld1(storeID, TmpInvoiceCodePDA, TmpInvoiceCodePDA);
        Double percentMax = 0.00;
        Double percentMaxGross = 0.0;
        Double amountMaxGross = 0.0;

        String amountBenfitMaxGross = dbengine.fnctnGetMaxAssignedBen9DscntApld2(storeID, TmpInvoiceCodePDA, TmpInvoiceCodePDA);
        String percentBenifitMaxGross = dbengine.fnctnGetMaxAssignedBen8DscntApld2(storeID, TmpInvoiceCodePDA, TmpInvoiceCodePDA);

        if (percentBenifitMaxGross.equals("")) {
            percentMaxGross = 0.0;
        } else {
            percentMaxGross = Double.parseDouble(percentBenifitMaxGross.split(Pattern.quote("^"))[0]);
        }
        if (percentBenifitMax.equals("")) {
            percentMax = 0.00;
        } else {
            percentMax = Double.parseDouble(percentBenifitMax.split(Pattern.quote("^"))[0]);
        }

        String amountBenifitMax = dbengine.fnctnGetMaxAssignedBen9DscntApld1(storeID, TmpInvoiceCodePDA, TmpInvoiceCodePDA);
        Double amountMax = 0.00;
        if (percentBenifitMax.equals("")) {
            amountMax = 0.0;
        } else {
            amountMax = Double.parseDouble(amountBenifitMax.split(Pattern.quote("^"))[0]);
        }


        tvAddDisc.setText("" + "0.00");

        tv_NetInvAfterDiscount.setText("" + TotalOrderValBeforeTax);

        TotTaxAmount = Double.parseDouble(new DecimalFormat("##.##").format(TotTaxAmount));
        tvTAmt.setText("" + TotTaxAmount);

        Double totalGrossVALMaxPercentage = TotalOrderValBeforeTax - TotalOrderValBeforeTax * (percentMaxGross / 100);
        Double totalGrossrVALMaxAmount = TotalOrderValBeforeTax - amountMaxGross;
        Double totalGrossVALAfterDiscount = 0.0;
        if (totalGrossVALMaxPercentage != totalGrossrVALMaxAmount) {
            totalGrossVALAfterDiscount = Math.min(totalGrossrVALMaxAmount, totalGrossVALMaxPercentage);
        } else {
            totalGrossVALAfterDiscount = totalGrossrVALMaxAmount;
        }

        if (totalGrossVALAfterDiscount == totalGrossrVALMaxAmount && totalGrossrVALMaxAmount != 0.0) {
            dbengine.updatewhatAppliedFlag(1, storeID, Integer.parseInt(amountBenfitMaxGross.split(Pattern.quote("^"))[1]), TmpInvoiceCodePDA, TmpInvoiceCodePDA);
        } else if (totalGrossVALAfterDiscount == totalGrossVALMaxPercentage && percentMaxGross != 0.0) {
            dbengine.updatewhatAppliedFlag(1, storeID, Integer.parseInt(percentBenifitMaxGross.split(Pattern.quote("^"))[1]), TmpInvoiceCodePDA, TmpInvoiceCodePDA);
        }

        Double GrossInvValue = totalGrossVALAfterDiscount + TotTaxAmount;
        GrossInvValue = Double.parseDouble(new DecimalFormat("##.##").format(GrossInvValue));
        tv_GrossInvVal.setText("" + GrossInvValue);
        tvAfterTaxValue.setText("" + GrossInvValue);

        Double CollectionAmt = dbengine.fnTotCollectionAmtAgainstStore(storeID, TmpInvoiceCodePDA, StoreVisitCode);
        CollectionAmt = Double.parseDouble(new DecimalFormat("##.##").format(CollectionAmt));

        if (GrossInvValue > 0.0) {
            VisitTypeStatus = "2";
        }
        if (CollectionAmt > 0.0) {
            VisitTypeStatus = "3";
        }
        if (CollectionAmt > 0.0 && GrossInvValue > 0.0) {
            VisitTypeStatus = "4";
        }

        dbengine.updateVisitTypeStatusOfStore(storeID, VisitTypeStatus, StoreVisitCode);

    }

    private void setInvoiceTableView() {

        LayoutInflater inflater = getLayoutInflater();
        final View row123 = (View) inflater.inflate(R.layout.activity_detail_productfilter, ll_scheme_detail, false);


        tvCredAmtVAL = (TextView) row123.findViewById(R.id.textView1_CredAmtVAL);
        tvINafterCredVAL = (TextView) row123.findViewById(R.id.textView1_INafterCredVAL);
        textView1_CredAmtVAL_new = (TextView) row123.findViewById(R.id.textView1_CredAmtVAL_new);


        tv_NetInvValue = (TextView) row123.findViewById(R.id.tv_NetInvValue);
        tvTAmt = (TextView) row123.findViewById(R.id.textView1_v2);
        tvDis = (TextView) row123.findViewById(R.id.textView1_v3);
        tv_GrossInvVal = (TextView) row123.findViewById(R.id.tv_GrossInvVal);
        tvFtotal = (TextView) row123.findViewById(R.id.textView1_v5);
        tvAddDisc = (TextView) row123.findViewById(R.id.textView1_AdditionalDiscountVAL);
        tv_NetInvAfterDiscount = (TextView) row123.findViewById(R.id.tv_NetInvAfterDiscount);

        tvAmtPrevDueVAL = (TextView) row123.findViewById(R.id.tvAmtPrevDueVAL);
        tvAmtOutstandingVAL = (TextView) row123.findViewById(R.id.tvAmtOutstandingVAL);
        etAmtCollVAL = (EditText) row123.findViewById(R.id.etAmtCollVAL);

        tvNoOfCouponValue = (EditText) row123.findViewById(R.id.tvNoOfCouponValue);
        txttvCouponAmountValue = (EditText) row123.findViewById(R.id.tvCouponAmountValue);

        tvPreAmtOutstandingVALNew = (TextView) row123.findViewById(R.id.tvPreAmtOutstandingVALNew);
        tvAfterTaxValue = (TextView) row123.findViewById(R.id.tvAfterTaxValue);
        ll_scheme_detail.addView(row123);
        Double outstandingvalue = dbengine.fnGetStoretblLastOutstanding(storeID);
        tvPreAmtOutstandingVALNew.setText("" + outstandingvalue);

    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }

    }
}
