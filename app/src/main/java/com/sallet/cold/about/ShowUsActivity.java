package com.sallet.cold.about;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 关于我们的产品介绍
 * About our product introduction
 */
public class ShowUsActivity extends BaseActivity {


    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_content)
    TextView tvContent;

    String tvAbountUs="关于我们\n" +
            "\n" +
            "Sallet One，一个任何人用的起的开源冷钱包，一个真正可以传世的冷钱包， 一个永不触网的冷钱包。经得起审视，守得住资产，给您最好的保护。\n" +
            "\n" +
            "【永不联网，无懈可击】\n" +
            "只要联网，您的资产就有可能被黑客转移。Sallet One永不联网、永不连接电子设备，仅凭QR码传输数据，你的秘钥没有接触网络的机会，这让最顶尖的黑客也无能为力。\n" +
            "\n" +
            "【完全开源，让您审视我们对于安全的定义】\n" +
            "有别于传统硬件钱包，Sallet One已在GitHub上开放了所有源码。只有完全开源，您才能清楚地了解您所购买的硬件钱包上「程序如何运行」，「私钥如何生成」，「交易签名是如何做」，「冷热钱包间如何通讯」……这样您才能用起来真正的放心、安心。\n" +
            "\n" +
            "【让冷钱包得以传世】\n" +
            "无论是设备丢失或损坏，又或者是Sallet One公司历经百年后，您都可以用Sallet One源码轻松制作并恢复冷钱包，永保数字资产安全。请牢记助记词，这将是您传给下一代的财富密码。\n" +
            "\n" +
            "【您的加密资产交给Sallet One守护，再无后顾之忧】\n" +
            "1.安全，从“芯”开始\n" +
            "采用经过国际认证的安全芯片，提供绝对安全保护\n" +
            "2.物理隔绝风险\n" +
            "绝不联网，物理隔绝一切风险，离线分散存储\n" +
            "3.防篡改，不怕被盗\n" +
            "一旦监测到漏洞或物理攻击，自动删除所有秘钥\n" +
            "4.气密式，坚固耐用\n" +
            "完全密封的气密式硬件钱包，防尘防水；金属制成，坚固耐用\n" +
            "5.支持更多主流币种\n" +
            "自由存储各种加密货币，比特币、以太坊等，且币种不断增加\n" +
            "6.提供二次开发性\n" +
            "开放源码拥有可开发性，您可在我们源码基础上自由添加币种\n" +
            "\n" +
            "【轻松管理资产，只需一个APP】\n" +
            "Sallet One Live可以与你的冷钱包无缝协作，轻松管理、交易数字资产，但仅用于传输数据，不会存储您的钱包凭证或私钥，资产依然保持离线，即使App被恶意入侵也不会对您的资产造成任何威胁。\n" +
            "\n" +
            "Sallet One重新定义加密资产安全，做所有人用得起的、可以传世的开源冷钱包。\n" +
            "\n";
    String tvenAbountUs="about us\n" +

            "\n" +
            "Sallet One , an open source cold wallet that anyone can use, a cold wallet that can be passed down to the world, and a cold wallet that never touches the Internet. Can withstand scrutiny, hold assets, and give you the best protection.\n" +
            "\n" +
            "[Never network, impeccable]\n" +
            "As long as you are connected to the Internet, your assets may be transferred by hackers. Sallet One never connects to the Internet and never connects to electronic devices. Only the QR code transmits data, and your secret key has no chance of contacting the Internet. This leaves the top hackers powerless.\n" +
            "\n" +
            "[Fully open source, let you review our definition of security]\n" +
            "Unlike traditional hardware wallets, Sallet One has opened all source code on GitHub . Only fully open source, you can clearly understand the \"how to run the program\", \"how to generate the private key\", \"how to do the transaction signature\", \"how to communicate between hot and cold wallets\" on the hardware wallet you purchased ... so that you can It is really at ease and peace of mind to use.\n" +
            "\n" +
            "[Let the cold wallet be handed down to the world]\n" +
            "Whether the device is lost or damaged, or Sallet One company after a hundred years, you can easily use the Sallet One source code to easily create and restore the cold wallet, and always keep your digital assets safe. Please keep in mind the mnemonic phrase, this will be your wealth password to the next generation.\n" +
            "\n" +
            "[Your encrypted assets are protected by Sallet One , no more worries]\n" +
            "1. Safety, from the \" core \" start\n" +
            "Use internationally certified security chips to provide absolute security protection\n" +
            "2. Physically isolate the risk\n" +
            "Never connect to the Internet, physically isolate all risks, and store offline for decentralized storage\n" +
            "3. Anti-tampering, not afraid of being stolen\n" +
            "Once a vulnerability or physical attack is detected, all secret keys are automatically deleted\n" +
            "4. Airtight, sturdy and durable\n" +
            "Completely sealed airtight hardware wallet, dustproof and waterproof; made of metal, sturdy and durable\n" +
            "5. Support more mainstream currencies\n" +
            "Free storage of various cryptocurrencies, such as Bitcoin, Ethereum, etc., and the number of currencies continues to increase\n" +
            "6. Provide secondary development\n" +
            "Open source code has developability, you can freely add currencies based on our source code\n" +
            "\n" +
            "[Easily manage assets, only one APP is needed ]\n" +
            "\n" +
            "Sallet One Live can work seamlessly with your cold wallet to easily manage and trade digital assets, but it is only used to transmit data and will not store your wallet credentials or private keys. The assets remain offline, even if the App is maliciously hacked. Will pose any threat to your assets.\n" +
            "\n" +
            "Sallet One redefines the security of encrypted assets and is an open source cold wallet that everyone can afford and can be handed down.";




    String content;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_us);
        ButterKnife.inject(this);
        content=getIntent().getStringExtra("content");



            if(App.getSpString(App.language).equals("zh")) {
                tvContent.setText(tvAbountUs);

            }else {
                tvContent.setText(tvenAbountUs);

            }
            tvTitle.setText(getStringResources(R.string.about_us));

    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
