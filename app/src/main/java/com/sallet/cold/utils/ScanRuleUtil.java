package com.sallet.cold.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.common.dto.BtcTransDTO;
import com.hk.common.dto.EthTransDTO;
import com.hk.common.dto.FilMsgDTO;
import com.hk.common.dto.FilTransDTO;
import com.hk.common.dto.SolTransDTO;
import com.hk.common.dto.TronTransDTO;
import com.hk.common.dto.XrpTransDTO;
import com.sallet.cold.bean.ScanResultTradeBean;

/**
 * Scan and parse data from the software
 */
public class ScanRuleUtil {




    static String agree= ConfigContent.agree;

    /**
     * External call
     * Generate the address as a string with protocol format for software parsing
     * @param type   currency
     * @param address    address
     * @return
     */

    public static String createAddressTCode(int type,String address){

        String tCode=agree+ConfigContent.deviceCode+"/"+ConfigContent.deviceVersion+"/address/"+type+"/"+address;


        return  tCode;
    }

    /**
     * Check the data
     * @param result
     * @return
     */


    public static int checkData(String result){
        try {
            //unpack string
            String ss=DeflaterUtils.unzipString(result.substring(agree.length()));
        String []ssArray=ss.split("/");
        if(ssArray[2].equals("tx")){
            return 1;// is a transaction QR code
        }else {
            return -1;
        }
        }catch ( Exception e){
            return -1;
        }


    }




    /**
     * Get the scan code result and parse the data of each currency through the defined data structure
     * @param result  Scanning result
     * @return
     */


    public static ScanResultTradeBean analysisTradeTCode(String result){
        //Transaction data entity class
        ScanResultTradeBean content=new ScanResultTradeBean();
        // Unzip the content
        String ss=DeflaterUtils.unzipString(result.substring(agree.length()));

        String []ssArray=ss.split("/");
        content.setDeviceCode(ssArray[0]);
        content.setDeviceVersion(ssArray[1]);
        content.setType(ssArray[3]);
        String cut=ssArray[0]+"/"+ssArray[1]+"/"+ssArray[2]+"/"+ssArray[3]+"/";
        String trade=ss.substring(cut.length());
        Gson gson = new Gson();
        String type=ssArray[3];
        switch (type) {
            case "0":
            case "4":
            case "5":
            case "6":
                 { //btc doge ltc bch
                BtcTransDTO dto = gson.fromJson(trade, new TypeToken<BtcTransDTO>() {
                }.getType()); //

                content.setBtcTrade(dto);
                break;
            }
            default: {//eth
                EthTransDTO dto = gson.fromJson(trade, new TypeToken<EthTransDTO>() {
                }.getType()); //

                content.setEthTrade(dto);
                break;
            }
        }






        return content;
    }




}
