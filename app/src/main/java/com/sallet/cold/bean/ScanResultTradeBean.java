package com.sallet.cold.bean;

import com.hk.common.dto.BtcTransDTO;
import com.hk.common.dto.EthTransDTO;
import com.hk.common.dto.FilMsgDTO;
import com.hk.common.dto.FilTransDTO;
import com.hk.common.dto.SolTransDTO;
import com.hk.common.dto.TronTransDTO;
import com.hk.common.dto.XrpTransDTO;

/**
 * 交易相关实体类
 * 用于接收软件传递过来的交易展示数据
 */
public class ScanResultTradeBean {
    private String deviceCode; //设备码 device code
    private String deviceVersion;//设备版本 Device version
    private String type;//0 btc 1eth 2usdt 3usdc 4doge 5ltc 6bch 7fil 8matic
    private BtcTransDTO btcTrade;//btc 相关模型 btc related models
    private EthTransDTO ethTrade;//eth 相关模型 eth related models
    private FilMsgDTO filTrade;//fil 相关模型 fil correlation model
    private XrpTransDTO xrpTransDTO;//xrp 相关模型 xrp correlation model
    private SolTransDTO solTransDTO;//sol 相关模型 sol 相关模型
    private TronTransDTO tronTransDTO;//tron 相关模型 tron 相关模型



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public BtcTransDTO getBtcTrade() {
        return btcTrade;
    }

    public void setBtcTrade(BtcTransDTO btcTrade) {
        this.btcTrade = btcTrade;
    }

    public EthTransDTO getEthTrade() {
        return ethTrade;
    }

    public void setEthTrade(EthTransDTO ethTrade) {
        this.ethTrade = ethTrade;
    }


    public FilMsgDTO getFilTrade() {
        return filTrade;
    }

    public void setFilTrade(FilMsgDTO filTrade) {
        this.filTrade = filTrade;
    }

    public XrpTransDTO getXrpTransDTO() {
        return xrpTransDTO;
    }

    public void setXrpTransDTO(XrpTransDTO xrpTransDTO) {
        this.xrpTransDTO = xrpTransDTO;
    }

    public SolTransDTO getSolTransDTO() {
        return solTransDTO;
    }

    public void setSolTransDTO(SolTransDTO solTransDTO) {
        this.solTransDTO = solTransDTO;
    }

    public TronTransDTO getTronTransDTO() {
        return tronTransDTO;
    }

    public void setTronTransDTO(TronTransDTO tronTransDTO) {
        this.tronTransDTO = tronTransDTO;
    }
}
