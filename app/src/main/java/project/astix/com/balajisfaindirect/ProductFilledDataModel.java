package project.astix.com.balajisfaindirect;

import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;

public class ProductFilledDataModel {

   private LinkedHashMap<String,String> hmapPrdctOrderQty=new LinkedHashMap<String,String>();
   private LinkedHashMap<String,String> hmapPrdctOrderVal=new LinkedHashMap<String,String>();
   private EditText lastEditText;

   //Product Quantity

    public void setPrdctQty(String prdctId,String prdctQty)
    {
        hmapPrdctOrderQty.put(prdctId,prdctQty);
    }
    public String getPrdctOrderQty(String prdctId)
    {
        String prdctQty="";
        if((hmapPrdctOrderQty!=null) && (hmapPrdctOrderQty.containsKey(prdctId)))
        {
            prdctQty=hmapPrdctOrderQty.get(prdctId);
        }
        return prdctQty;
    }
    public void removePrdctQty(String prdctId)
    {
        if((hmapPrdctOrderQty!=null) && (hmapPrdctOrderQty.containsKey(prdctId)))
        {
            hmapPrdctOrderQty.remove(prdctId);
        }
    }
//ProductValue
    public void setPrdctVal(String prdctId,String prdctVal)
    {
        hmapPrdctOrderVal.put(prdctId,prdctVal);
    }
    public Double getPrdctOrderVal(String prdctId)
    {
        Double prdctVal=0.0;
        if((hmapPrdctOrderVal!=null) && (hmapPrdctOrderVal.containsKey(prdctId)))
        {
            prdctVal=Double.parseDouble(hmapPrdctOrderVal.get(prdctId));
            prdctVal=Double.parseDouble(new DecimalFormat("##.##").format(prdctVal));
        }
        return prdctVal;
    }
    public void removePrdctVal(String prdctId)
    {
        if((hmapPrdctOrderVal!=null) && (hmapPrdctOrderVal.containsKey(prdctId)))
        {
            hmapPrdctOrderVal.remove(prdctId);
        }
    }

    public void setLastEditText(EditText lastEditText)
    {
        this.lastEditText=lastEditText;
    }
    public EditText getLastEditText()
    {
        return lastEditText;
    }
    public int gethmapPrdctOrderQtySize()
    {
        int totalSize=0;
        if(hmapPrdctOrderQty!=null)
        {
            totalSize=hmapPrdctOrderQty.size();
        }
        return totalSize;
    }

    public LinkedHashMap<String, String> getHmapPrdctOrderQty() {
        return hmapPrdctOrderQty;
    }
}