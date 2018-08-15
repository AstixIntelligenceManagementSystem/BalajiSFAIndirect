package project.astix.com.balajisfaindirect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astix.Common.CommonFunction;
import com.astix.Common.CommonInfo;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ActualVisitStock extends Activity implements CategoryCommunicator {


    String imageName;
    File imageF;


    Uri uriSavedImage;
    ImageView flashImage;
    float mDist=0;
    private boolean isLighOn = false;
    ArrayList<Object> arrImageData=new ArrayList<Object>();
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private Button capture,cancelCam, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;

    LinkedHashMap<String ,String> hmapPhotoDetailsForSaving=new LinkedHashMap<>();

LinearLayout lLayout_main;
PRJDatabase dbengine = new PRJDatabase(this);
Button btnNext;
    public EditText   ed_search;
    public ImageView  btn_go;

    public String storeID;
    public String imei;
    public String date;
    public String pickerDate;
    public String selStoreName;
    List<String> categoryNames;
    int progressBarStatus=0;
    public  Dialog dialog=null;
    LinkedHashMap<String, String> hmapctgry_details=new LinkedHashMap<String, String>();
    ImageView img_ctgry;
    public int StoreCurrentStoreType=0;
    String previousSlctdCtgry="",clickedTagPhoto;
    Button btnClickPic,btnViewPic;
    LinearLayout ll_StockPicData;
    LinkedHashMap<String,String> hmapPrdctData=new LinkedHashMap<>();
    LinkedHashMap<String, String> hmapFilterProductList=new LinkedHashMap<String, String>();
    LinkedHashMap<String, ArrayList<String>> hmapStockPhotoSection = new LinkedHashMap<String,ArrayList<String>>();
LinkedHashMap<String,String> hmapFetchPDASavedData=new LinkedHashMap<>();
LinkedHashMap<String,String> hmapSaveDataInPDA=new LinkedHashMap<>();
LinkedHashMap<String,String> hmapProductStockFromPurchaseTable=new LinkedHashMap<>();




    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            return true;

        }
        if(keyCode==KeyEvent.KEYCODE_HOME)
        {

        }
        if(keyCode==KeyEvent.KEYCODE_MENU)
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
        setContentView(R.layout.activity_actual_visit_stock);
        dbengine=new PRJDatabase(ActualVisitStock.this);

        initializeallViews();
        getDataFromIntent();
        fetchDataFromDatabase();



    }

    public void initializeallViews(){
        lLayout_main= (LinearLayout) findViewById(R.id.lLayout_main);
        ImageView img_back_Btn= (ImageView) findViewById(R.id.img_back_Btn);
        btnNext= (Button) findViewById(R.id.btnNext);


       img_ctgry=(ImageView) findViewById(R.id.img_ctgry);
        ed_search=(EditText) findViewById(R.id.ed_search);
        btn_go=(ImageView) findViewById(R.id.btn_go);
        btnClickPic= (Button) findViewById(R.id.btnClickPic);
        btnViewPic= (Button) findViewById(R.id.btnViewPic);


        btnViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemarksAlert(storeID,false);
            }
        });
        btnClickPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemarksAlert(storeID,true);
            }
        });

       img_ctgry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                img_ctgry.setEnabled(false);
                customAlertStoreList(categoryNames,"Select Category");
            }
        });


        btn_go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                if(!TextUtils.isEmpty(ed_search.getText().toString().trim()))
                {

                    if(!ed_search.getText().toString().trim().equals(""))
                    {
                        searchProduct(ed_search.getText().toString().trim(),"");

                    }


                }


                else
                {
                    searchProduct("All","");
                }

            }


        });

        img_back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fireBackDetPg=new Intent(ActualVisitStock.this,LastVisitDetails.class);
                fireBackDetPg.putExtra("storeID", storeID);
                fireBackDetPg.putExtra("SN", selStoreName);
                fireBackDetPg.putExtra("bck", 1);
                fireBackDetPg.putExtra("imei", imei);
                fireBackDetPg.putExtra("userdate", date);
                fireBackDetPg.putExtra("pickerDate", pickerDate);
                fireBackDetPg.putExtra("flgOrderType", 1);
                //fireBackDetPg.putExtra("rID", routeID);
                startActivity(fireBackDetPg);
                finish();
                //aa

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbengine.deleteActualVisitData(storeID);
                if(hmapFetchPDASavedData!=null && hmapFetchPDASavedData.size()>0)
                {
                    for (Map.Entry<String,String> entry:hmapFetchPDASavedData.entrySet()){

                        dbengine.saveTblActualVisitStock(storeID,entry.getKey(),entry.getValue(),1);


                    }
                }

                if(hmapPhotoDetailsForSaving!=null && hmapPhotoDetailsForSaving.size()>0)
                {
                    dbengine.insertStoreCheckInPic(storeID,hmapPhotoDetailsForSaving);
                }


                passIntentToProductOrderFilter();
      //---------------********Video page open code
    /*            dbengine.open();
               String VideoData=      dbengine.getVideoNameByStoreID(storeID,"2");
                //dbengine.close();
                int flagPlayVideoForStore=0;
                String Video_Name="0";
                String VIDEO_PATH="0";
                String VideoViewed="0";
                String Contentype="0";
                if(!VideoData.equals("0") && VideoData.contains("^")){
                     Video_Name=   VideoData.toString().split(Pattern.quote("^"))[0];
                     flagPlayVideoForStore=   Integer.parseInt( VideoData.toString().split(Pattern.quote("^"))[1]);
                    VideoViewed=    VideoData.toString().split(Pattern.quote("^"))[2];
                    Contentype=    VideoData.toString().split(Pattern.quote("^"))[3];
                }

                *//*  VIDEO_PATH= "/sdcard/WhatsApp/Media/WhatsApp Video/VID-20180303-WA0030.mp4";
                VIDEO_PATH= "/sdcard/VideoLTFOODS/SampleVideo5mb.mp4";*//*
                VIDEO_PATH=   Environment.getExternalStorageDirectory() + "/" + CommonInfo.VideoFolder + "/"+Video_Name;
                Uri intentUri;
                //if videoShown check
                if(flagPlayVideoForStore==1 && !(VIDEO_PATH.equals("0")) && VideoViewed.equals("0")&& Contentype.equals("2")){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        File file = new File(VIDEO_PATH);
                         intentUri = FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", file);
                    }
                    else{
                         intentUri = Uri.parse(VIDEO_PATH);
                    }


                    if(intentUri!=null) {
                        Intent intent = new Intent(ActualVisitStock.this,VideoPlayerActivityForStore.class);
                        intent.putExtra("FROM","ActualVisitStock");
                        intent.putExtra("STRINGPATH",VIDEO_PATH);
                        intent.putExtra("storeID", storeID);
                        intent.putExtra("SN", selStoreName);
                        intent.putExtra("imei", imei);
                        intent.putExtra("userdate", date);
                        intent.putExtra("pickerDate", pickerDate);
                        intent.putExtra("flgOrderType", 1);
                        startActivity(intent);
                        finish();
                       // openVideoPlayerDialog(VIDEO_PATH);

                    }
                    else{
                        Toast.makeText(ActualVisitStock.this, "No video Found", Toast.LENGTH_LONG).show();
                        passIntentToProductOrderFilter();
                    }

                }
                else{

                    passIntentToProductOrderFilter();
                }*/
                //---------------********Video page open code  end
            }
        });

    }

public void passIntentToProductOrderFilter(){
    Intent nxtP4 = new Intent(ActualVisitStock.this,ProductOrderEntry.class);
    //Intent nxtP4 = new Intent(LastVisitDetails.this,ProductOrderFilterSearch_RecycleView.class);
    nxtP4.putExtra("storeID", storeID);
    nxtP4.putExtra("SN", selStoreName);
    nxtP4.putExtra("imei", imei);
    nxtP4.putExtra("userdate", date);
    nxtP4.putExtra("pickerDate", pickerDate);
    nxtP4.putExtra("flgOrderType", 1);
    startActivity(nxtP4);
    finish();
}
    public void inflatePrdctStockData(){


        if(hmapFilterProductList!=null && hmapFilterProductList.size()>0){
            for (Map.Entry<String, String> entry : hmapFilterProductList.entrySet()) {

                String prdId = entry.getKey();
                String value = entry.getValue();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewProduct = inflater.inflate(R.layout.inflate_row_actual_visit, null);
                LinearLayout ll_inflate= (LinearLayout) viewProduct.findViewById(R.id.ll_inflate);

                TextView prdName= (TextView) viewProduct.findViewById(R.id.prdName);
                final EditText et_stckVal= (EditText) viewProduct.findViewById(R.id.et_stckVal);
                prdName.setText(value);
                prdName.setTag(prdId);

                et_stckVal.setTag(prdId+"_Stock");

                if(hmapFetchPDASavedData!=null && hmapFetchPDASavedData.containsKey(prdId))
                {
                    et_stckVal.setText(hmapFetchPDASavedData.get(prdId));
                }
                et_stckVal.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                       if(!TextUtils.isEmpty(et_stckVal.getText().toString().trim()))
                       {
                           String tagProductId=et_stckVal.getTag().toString().split(Pattern.quote("_"))[0];
                           hmapFetchPDASavedData.put(tagProductId,et_stckVal.getText().toString().trim());
                       }
                       else
                       {
                           String tagProductId=et_stckVal.getTag().toString().split(Pattern.quote("_"))[0];
                           if(hmapFetchPDASavedData.containsKey(tagProductId))
                           {
                               hmapFetchPDASavedData.remove(tagProductId);
                           }
                       }
                    }
                });
                lLayout_main.addView(viewProduct);

             // btnNextClick(storeID,prdId,et_stckVal);




            }
        }
    }


    public void fetchDataFromDatabase(){
        //dbengine.open();
        hmapPrdctData=dbengine.fetchProductDataForActualVisit();
        hmapFetchPDASavedData=dbengine.fetchActualVisitData(storeID);
        hmapProductStockFromPurchaseTable=dbengine.fetchProductStockFromPurchaseTable(storeID);
        StoreCurrentStoreType=Integer.parseInt(dbengine.fnGetStoreTypeOnStoreIdBasis(storeID));

            ArrayList<String> list_ImgName=dbengine.getImageNameForStoreCheckIn(storeID);
            if(list_ImgName != null && list_ImgName.size()>0)
            {
                hmapStockPhotoSection.put(storeID,list_ImgName);


            }
        if(hmapStockPhotoSection!=null && hmapStockPhotoSection.size()>0)
        {
            btnViewPic.setVisibility(View.VISIBLE);
            btnClickPic.setText("Edit/Add Stock Pic");
        }
        else
        {
            btnViewPic.setVisibility(View.GONE);
            btnClickPic.setText("Add Stock Pic");
        }

        //dbengine.close();

        getCategoryDetail();

        Iterator it11new = hmapProductStockFromPurchaseTable.entrySet().iterator();
        String crntPID="0";
        int cntPsize=0;
        while (it11new.hasNext()) {
            Map.Entry pair = (Map.Entry) it11new.next();

            hmapFetchPDASavedData.put(pair.getKey().toString(),pair.getValue().toString());
        }

      //  img_ctgry.setText("All");
        //searchProduct("All","");
        searchLoadDefaultProduct("All","");//********WE load defualt product on Oncreate
       /* if(hmapFetchPDASavedData!=null && hmapFetchPDASavedData.size()>0) {


            for (Map.Entry<String, String> entry : hmapFetchPDASavedData.entrySet()) {

                String prdId=entry.getKey();
                String stckVal=entry.getValue();


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewProduct = inflater.inflate(R.layout.inflate_row_actual_visit, null);
                LinearLayout ll_inflate= (LinearLayout) viewProduct.findViewById(R.id.ll_inflate);

                TextView prdName= (TextView) viewProduct.findViewById(R.id.prdName);
                EditText et_stckVal= (EditText) findViewById(R.id.et_stckVal);

                lLayout_main.addView(viewProduct);

            }
        }*/
    }


    private void getDataFromIntent() {


        Intent passedvals = getIntent();

        if(passedvals!=null){

            storeID = passedvals.getStringExtra("storeID");
            imei = passedvals.getStringExtra("imei");
            date = passedvals.getStringExtra("userdate");
            pickerDate = passedvals.getStringExtra("pickerDate");
            selStoreName = passedvals.getStringExtra("SN");

        }

    }

    public void customAlertStoreList(final List<String> listOption, String sectionHeader)
    {

        final Dialog listDialog = new Dialog(ActualVisitStock.this);
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


        final ListView list_store=(ListView) listDialog.findViewById(R.id.list_store);
        final CardArrayAdapterCategory cardArrayAdapter = new CardArrayAdapterCategory(ActualVisitStock.this,listOption,listDialog,previousSlctdCtgry);

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

      //  img_ctgry.setText(selectedCategory);

        if(hmapctgry_details.containsKey(selectedCategory))
        {
            searchProduct(selectedCategory,hmapctgry_details.get(selectedCategory));
        }
        else
        {
            searchProduct(selectedCategory,"");
        }



    }



    public void searchProduct(String filterSearchText,String ctgryId)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBarStatus = 0;


        hmapFilterProductList.clear();


        hmapFilterProductList=dbengine.getFileredProductListMap(filterSearchText.trim(),StoreCurrentStoreType,ctgryId);
        //System.out.println("hmapFilterProductListCount :-"+ hmapFilterProductList.size());
        lLayout_main.removeAllViews();

		/*if(hmapFilterProductList.size()<250)
		{*/
        if(hmapFilterProductList.size()>0)
        {
            inflatePrdctStockData();
        }
        else
        {
            allMessageAlert(ActualVisitStock.this.getResources().getString(R.string.AlertFilter));
        }

		/*}

		else
		{
			allMessageAlert("Please put some extra filter on Search-Box to fetch related product");
		}*/


        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void allMessageAlert(String message) {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(ActualVisitStock.this);
        alertDialogNoConn.setTitle(ActualVisitStock.this.getResources().getString(R.string.genTermInformation));
        alertDialogNoConn.setMessage(message);
        //alertDialogNoConn.setMessage(getText(R.string.connAlertErrMsg));
        alertDialogNoConn.setNeutralButton(ActualVisitStock.this.getResources().getString(R.string.AlertDialogOkButton),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();


                    }
                });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }
    private void getCategoryDetail()
    {

        hmapctgry_details=dbengine.fetch_Category_List();

        int index=0;
        if(hmapctgry_details!=null)
        {
            categoryNames=new ArrayList<String>();
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapctgry_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry)iterator.next();
                categoryNames.add(me2.getKey().toString());
                index=index+1;
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }

        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
            if(dialog!=null){
                if(dialog.isShowing()){
                    dialog.dismiss();

                }
            }
        }
    }


public void searchLoadDefaultProduct(String filterSearchText,String ctgryId)
{
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    progressBarStatus = 0;


    hmapFilterProductList.clear();




    hmapFilterProductList=dbengine.fetchProductListLastvisitAndOrderBasis(storeID);
    if(hmapFilterProductList!=null && hmapFilterProductList.isEmpty()){
        hmapFilterProductList=dbengine.getFileredProductListMap(filterSearchText.trim(),StoreCurrentStoreType,ctgryId);

    }
    //System.out.println("hmapFilterProductListCount :-"+ hmapFilterProductList.size());
    lLayout_main.removeAllViews();

		/*if(hmapFilterProductList.size()<250)
		{*/
    if(hmapFilterProductList.size()>0)
    {
        inflatePrdctStockData();
    }
    else
    {
        allMessageAlert(ActualVisitStock.this.getResources().getString(R.string.AlertFilter));
    }

		/*}

		else
		{
			allMessageAlert("Please put some extra filter on Search-Box to fetch related product");
		}*/


    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

}


    void openRemarksAlert(final String tagVal,boolean isToEditPic)
    {
        final Dialog listDialogMulti = new Dialog(ActualVisitStock.this);
        listDialogMulti.requestWindowFeature(Window.FEATURE_NO_TITLE);
      /*  // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
*/

        // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDialogMulti.setContentView(R.layout.stock_check_pic);
       // View layout = inflater.inflate(R.layout.stock_check_pic, null);
        WindowManager.LayoutParams parms = listDialogMulti.getWindow().getAttributes();
        parms.gravity =Gravity.CENTER;
        parms.width = WindowManager.LayoutParams.FILL_PARENT;
        parms.height = WindowManager.LayoutParams.WRAP_CONTENT;
       /* layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));*/
        Button btn_clkCamera= (Button) listDialogMulti.findViewById(R.id.btn_clkCamera);
        btn_clkCamera.setTag(tagVal);
        if(isToEditPic)
        {
            btn_clkCamera.setVisibility(View.VISIBLE);
        }
        else
        {
            btn_clkCamera.setVisibility(View.GONE);
        }
        final LinearLayout ll_RemarkImage= (LinearLayout) listDialogMulti.findViewById(R.id.ll_RemarkImage);
        Button btn_done= (Button) listDialogMulti.findViewById(R.id.btn_done);



        if(hmapStockPhotoSection!=null && hmapStockPhotoSection.containsKey(tagVal))
        {
            String selectedImageName="";
            ArrayList<String> listImage=hmapStockPhotoSection.get(tagVal);
            for(String imageName:listImage)
            {
                String file_dj_path = Environment.getExternalStorageDirectory() + "/" + CommonInfo.ImagesFolder + "/" +imageName;

                File fImageShow = new File(file_dj_path);
                if (fImageShow.exists())
                {
                  //  Bitmap bmp = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fImageShow.getAbsolutePath()), 120, 120);
                    //    adapterImage.add(i,bmp,imgName);
                    //setSavedImageWareHouseRemark(Bitmap bitmap, String imageName, String valueOfKey, final String clickedTagPhoto, final LinearLayout ll_imgToSet,boolean isClkdPic)
                    setSavedImageWareHouseRemark(fImageShow.getAbsolutePath(),imageName,"",tagVal,ll_RemarkImage,false,isToEditPic);
                    selectedImageName=imageName;
                }
            }

        }
        btn_clkCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_StockPicData=ll_RemarkImage;
                clickedTagPhoto=v.getTag().toString();
                openCustomCamara();

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listDialogMulti.dismiss();
            }
        });

        listDialogMulti.setCanceledOnTouchOutside(true);

        listDialogMulti.show();
    }


    public void openCustomCamara()
    {
        if(dialog!=null)
        {
            if(!dialog.isShowing())
            {
                openCamera();
            }
        }
        else
        {
            openCamera();
        }
    }

    private void handleZoom(MotionEvent event, Camera.Parameters params)
    {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            // zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            // zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }

    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null
                && supportedFocusModes
                .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
        }
    }


    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //make a new picture file
                File pictureFile = getOutputMediaFile();

                Camera.Parameters params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(params);
                isLighOn = false;

                if (pictureFile == null) {
                    return;
                }
                try
                {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();

                    arrImageData.add(0,pictureFile);
                    arrImageData.add(1,pictureFile.getName());
                    dialog.dismiss();
                    if(pictureFile!=null)
                    {
                        File file=pictureFile;
                        System.out.println("File +++"+pictureFile);
                        imageName=pictureFile.getName();
                        CommonFunction.normalizeImageForUri(ActualVisitStock.this,Uri.fromFile(pictureFile));
                       // Bitmap bmp = decodeSampledBitmapFromFile(file.getAbsolutePath(), 80, 80);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        uriSavedImage = Uri.fromFile(pictureFile);
                       // bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                       // byte[] byteArray = stream.toByteArray();

                        // Convert ByteArray to Bitmap::\
                        //
                        long syncTIMESTAMP = System.currentTimeMillis();
                        Date dateobj = new Date(syncTIMESTAMP);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
                        String clkdTime = df.format(dateobj);
                        //	String valueOfKey=imagButtonTag+"~"+tempId+"~"+file.getAbsolutePath()+"~"+clkdTime+"~"+"2";
                        String valueOfKey=uriSavedImage.toString()+"~"+clkdTime+"~"+"1";
                        //   helperDb.insertImageInfo(tempId,imagButtonTag, imageName, file.getAbsolutePath(), 2);
                      //  Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);



                        setSavedImageWareHouseRemark(pictureFile.getAbsolutePath(),imageName,valueOfKey,clickedTagPhoto,ll_StockPicData,true,true);


                    }
//Show dialog here
//...
//Hide dialog here

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }

                //refresh camera to continue preview--------------------------------------------------------------
                //	mPreview.refreshCamera(mCamera);
                //if want to release camera
                if(mCamera!=null){
                    mCamera.release();
                    mCamera=null;
                }
            }
        };
        return picture;
    }

    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setEnabled(false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            cancelCam.setEnabled(false);
            flashImage.setEnabled(false);
            if(cameraPreview!=null)
            {
                cameraPreview.setEnabled(false);
            }

            if(mCamera!=null)
            {
                mCamera.takePicture(null, null, mPicture);
            }
            else
            {
                dialog.dismiss();
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };

    private boolean hasCamera(Context context) {
        //check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    private static File getOutputMediaFile()
    {
        //make a new file directory inside the "sdcard" folder
        File mediaStorageDir = new File("/sdcard/", CommonInfo.ImagesFolder);

        //if this "JCGCamera folder does not exist
        if (!mediaStorageDir.exists()) {
            //if you cannot make this folder return
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMMdd_HHmmss.SSS",Locale.ENGLISH).format(new Date());
        File mediaFile;
        //and make a media file:
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + CommonInfo.imei+ "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public void openCamera()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        arrImageData.clear();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dialog = new Dialog(ActualVisitStock.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        //dialog.setTitle("Calculation");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_main);
        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();

        parms.height=parms.MATCH_PARENT;
        parms.width=parms.MATCH_PARENT;
        cameraPreview = (LinearLayout)dialog. findViewById(R.id.camera_preview);

        mPreview = new CameraPreview(ActualVisitStock.this, mCamera);
        cameraPreview.addView(mPreview);
        //onResume code
        if (!hasCamera(ActualVisitStock.this)) {
            Toast toast = Toast.makeText(ActualVisitStock.this, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
        }

        if (mCamera == null) {
            //if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(ActualVisitStock.this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                switchCamera.setVisibility(View.GONE);
            }

            //mCamera = Camera.open(findBackFacingCamera());

			/*if(mCamera!=null){
				mCamera.release();
				mCamera=null;
			}*/
            mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			/*if(mCamera==null){
				mCamera=Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			}*/

            boolean isParameterSet=false;
            try {
                Camera.Parameters params= mCamera.getParameters();


                List<Camera.Size> sizes = params.getSupportedPictureSizes();
                Camera.Size size = sizes.get(0);
                //Camera.Size size1 = sizes.get(0);
                for(int i=0;i<sizes.size();i++)
                {

                    if(sizes.get(i).width > size.width)
                        size = sizes.get(i);
                }

                //System.out.println(size.width + "mm" + size.height);

                params.setPictureSize(size.width, size.height);
                params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                //	params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
                params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);

                //	params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

                isLighOn = false;
                int minExpCom=params.getMinExposureCompensation();
                int maxExpCom=params.getMaxExposureCompensation();

                if( maxExpCom > 4 && minExpCom < 4)
                {
                    params.setExposureCompensation(4);
                }
                else
                {
                    params.setExposureCompensation(0);
                }

                params.setAutoExposureLock(false);
                params.setAutoWhiteBalanceLock(false);
                //String supportedIsoValues = params.get("iso-values");
                // String newVAlue = params.get("iso");
                //  params.set("iso","1600");
                params.setColorEffect("none");
                params.set("scene-mode","auto");
                params.setPictureFormat(ImageFormat.JPEG);
                params.setJpegQuality(70);
                params.setRotation(90);

                mCamera.setParameters(params);
                isParameterSet=true;
            }
            catch (Exception e)
            {

            }
            if(!isParameterSet)
            {
                Camera.Parameters params2 = mCamera.getParameters();
                params2.setPictureFormat(ImageFormat.JPEG);
                params2.setJpegQuality(70);
                params2.setRotation(90);

                mCamera.setParameters(params2);
            }

            setCameraDisplayOrientation(ActualVisitStock.this, Camera.CameraInfo.CAMERA_FACING_BACK,mCamera);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
        }

        capture = (Button)dialog.  findViewById(R.id.button_capture);

        flashImage= (ImageView)dialog.  findViewById(R.id.flashImage);
        flashImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLighOn)
                {
                    // turn off flash
                    Camera.Parameters params = mCamera.getParameters();

                    if (mCamera == null || params == null) {
                        return;
                    }

                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(params);
                    flashImage.setImageResource(R.drawable.flash_off);
                    isLighOn=false;
                }
                else
                {
                    // turn on flash
                    Camera.Parameters params = mCamera.getParameters();

                    if (mCamera == null || params == null) {
                        return;
                    }

                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                    flashImage.setImageResource(R.drawable.flash_on);
                    mCamera.setParameters(params);

                    isLighOn=true;
                }
            }
        });

        final Button cancleCamera= (Button)dialog.  findViewById(R.id.cancleCamera);
        cancelCam=cancleCamera;
        cancleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                v.setEnabled(false);
                capture.setEnabled(false);
                cameraPreview.setEnabled(false);
                flashImage.setEnabled(false);

                Camera.Parameters params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(params);
                isLighOn = false;
                dialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        capture.setOnClickListener(captrureListener);

        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Get the pointer ID
                Camera.Parameters params = mCamera.getParameters();
                int action = event.getAction();

                if (event.getPointerCount() > 1) {
                    // handle multi-touch events
                    if (action == MotionEvent.ACTION_POINTER_DOWN) {
                        mDist = getFingerSpacing(event);
                    } else if (action == MotionEvent.ACTION_MOVE
                            && params.isZoomSupported()) {
                        mCamera.cancelAutoFocus();
                        handleZoom(event, params);
                    }
                } else {
                    // handle single touch events
                    if (action == MotionEvent.ACTION_UP) {
                        handleFocus(event, params);
                    }
                }
                return true;
            }
        });

        dialog.show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    void setSavedImageWareHouseRemark(String imagePath, String imageName, String valueOfKey, final String clickedTagPhoto, final LinearLayout ll_imgToSet,boolean isClkdPic,boolean isToPicEdit)
    {
        LayoutInflater inflate= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View convertView = inflate.inflate(R.layout.images_return_grid, null);

        //tagVal= catId+"_"+prodID+"_RemarkImage"


        ImageView img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);

        Uri uri = Uri.fromFile(new File(imagePath));
        Picasso.get().load(uri).resize(120,120).error(R.drawable.image_not_found).centerCrop().into(img_thumbnail);


        ImageView imgCncl = (ImageView) convertView.findViewById(R.id.imgCncl);
        imgCncl.setTag(imageName);

        if(!isToPicEdit)
        {
            imgCncl.setVisibility(View.GONE);
        }
        else
        {
            imgCncl.setVisibility(View.VISIBLE);
        }
        if(isClkdPic)
        {
            ArrayList<String> listClkdPic=new ArrayList<String>();
            if(hmapStockPhotoSection!=null && hmapStockPhotoSection.containsKey(clickedTagPhoto))
            {
                listClkdPic=hmapStockPhotoSection.get(clickedTagPhoto);
            }

            listClkdPic.add(imageName);
            hmapStockPhotoSection.put(clickedTagPhoto,listClkdPic);
            System.out.println("Hmap Photo category..."+clickedTagPhoto+"^"+imageName);

            String photoPath=valueOfKey.split(Pattern.quote("~"))[0];
            String clickedDataTime=valueOfKey.split(Pattern.quote("~"))[1];




            //key- imagName
            //value- businessId^CatID^TypeID^templateID^PhotoPath^ClikcedDatetime^PhotoTypeFlag^StackNo
        /*    savetbWareHousePhotoDetails(String StoreID,String PhotoName,
                    String PhotoPath,String ClickedDateTime,
                    String ClickTagPhoto,String Sstat)*/
            //  tagVal+"_edReason";

            hmapPhotoDetailsForSaving.put(imageName,photoPath+"^"+clickedDataTime+"^"+clickedTagPhoto);


        }

        ll_imgToSet.addView(convertView);
        if(hmapStockPhotoSection!=null && hmapStockPhotoSection.size()>0)
        {
            btnViewPic.setVisibility(View.VISIBLE);
            btnClickPic.setText("Edit/Add Stock Pic");
        }
        else
        {
            btnViewPic.setVisibility(View.GONE);
            btnClickPic.setText("Add Stock Pic");
        }
        imgCncl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String imageNameToDelVal=v.getTag().toString();

                ll_imgToSet.removeView(convertView);
                ArrayList listClkdPic=new ArrayList();
                if(hmapStockPhotoSection!=null && hmapStockPhotoSection.containsKey(clickedTagPhoto))
                {
                    listClkdPic=hmapStockPhotoSection.get(clickedTagPhoto);
                }

                if(listClkdPic.contains(imageNameToDelVal))
                {
                    listClkdPic.remove(imageNameToDelVal);

                    hmapStockPhotoSection.put(clickedTagPhoto,listClkdPic);

                    dbengine.validateStoreCheckIn(storeID,imageNameToDelVal);
                    if(hmapPhotoDetailsForSaving.containsKey(imageNameToDelVal))
                    {
                        hmapPhotoDetailsForSaving.remove(imageNameToDelVal);
                    }

                    if(listClkdPic.size()<1)
                    {
                        hmapStockPhotoSection.remove(clickedTagPhoto);
                    }
                }

                //  String file_dj_path = Environment.getExternalStorageDirectory() + "/RSPLSFAImages/"+imageNameToDel;
                String file_dj_path = Environment.getExternalStorageDirectory() + "/" + CommonInfo.ImagesFolder + "/" +imageNameToDelVal;

                File fdelete = new File(file_dj_path);
                if (fdelete.exists())
                {
                    if (fdelete.delete())
                    {
                        // Log.e("-->", "file Deleted :" + file_dj_path);
                        callBroadCast();
                    }
                    else
                    {
                        // Log.e("-->", "file not Deleted :" + file_dj_path);
                    }
                }
                if(hmapStockPhotoSection!=null && hmapStockPhotoSection.size()>0)
                {
                    btnViewPic.setVisibility(View.VISIBLE);
                    btnClickPic.setText("Edit/Add Stock Pic");
                }
                else
                {
                    btnViewPic.setVisibility(View.GONE);
                    btnClickPic.setText("Add Stock Pic");
                }
            }
        });
    }


    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

}
