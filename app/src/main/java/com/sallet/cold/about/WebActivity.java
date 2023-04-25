package com.sallet.cold.about;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sallet.cold.App;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Introduction to Terms of Service
 */
public class WebActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webView)
    TextView webView;

    String zhContent="<h1 align=\"center\">服务条款 2021年6月1号生效版本</h1>\n" +
            "<br></br>\n" +
            "<p>欢迎您来到Sallet One。请仔细阅读以下服务条款（以下简称为“本条款”），特别是以粗体字标出的内容。本条款描述了Sallet One平台、ATELAS SOLUTIONS GROUP LIMITED(“我们”、“我们”或“我们”)在https://www.salletone.com/及我们提供的相关移动应用程序和产品(“服务”或“赛雷钱包”)上的政策和程序。通过点击“创建帐户”按钮或通过访问或使用本服务，即视为您（即“用户”）同意遵守本条款及所有附加条款。如果您不同意受本条款的约束，请勿访问或使用本服务。我们希望您经常回顾本条款，以确保您了解适用于您访问和使用本服务的条款和条件。本条款及附加条款适用于您访问和使用由我们提供的任何服务，包括但不限于Sallet One钱包（以下简称“钱包”）、Sallet One网站https://www.salletone.com/（以下简称“网站服务”）以及我们提供的移动应用程序。签订其他有关我们所提供的产品的相关协议不会影响本条款的生效。如果您代表任何机构使用本服务，您声明并保证(a)该机构是符合当地法律法规的合法机构，(b)您有权代表该机构接受本条款。如果您违反本条款，该机构同意对您的行为负责。</p>\n" +
            "<h2>一、注册与帐户</h2>\n" +
            "<h3>1. 注册者资格</h3>\n" +
            "<p>您确认，在您完成注册程序或以其他我们允许的方式实际使用本服务时，您应当是具备完全民事权利能力和完全民事行为能力的自然人、法人或其他组织。若您不具备前述主体资格，则您及您的监护人应承担因此而导致的一切后果，且我们有权注销或永久冻结您的帐户，并向您及您的监护人索偿。</P>\n" +
            "<h3>2. 注册和帐户</h3>\n" +
            "<p>在您按照注册页面提示填写信息、阅读并同意本条款且完成全部注册程序后，或在您按照恢复钱包页面提示填写信息、阅读并同意本条款且完成全部激活程序后，或您以其他我们允许的方式实际使用钱包时，您即受本条款约束。您可以使用您的密码和助记词作为登录手段进入Sallet One。如果您忘记助记词，我们对此不承担任何责任，您将承担因此产生的任何直接或间接损失及不利后果。</p>\n" +
            "<h3>3. 帐户安全</h3>\n" +
            "<p>您须自行负责对您的Sallet One密码和助记词的保密，且须对您在该登录账户下发生的所有活动（包括但不限于信息披露、发布信息、网上点击同意或提交各类规则协议、网上续签协议或购买服务、设置帐户信息等）承担责任。\n" +
            "    您同意：\n" +
            "    (a) 如发现任何人未经授权使用您的账号和密码，或发生违反保密规定的任何其他情况，您会立即通知我们；\n" +
            "    (b) 确保您严格遵守钱包的安全、操作机制或者流程；\n" +
            "    (c) 确保您在每个使用时段结束时，以正确步骤离开钱包。我们不能也不会对因您未能遵守本款规定而发生的任何损失负责。您理解我们对您的请求采取行动需要合理时间，我们对在采取行动前已经产生的后果（包括但不限于您的任何损失）不承担任何责任。</p>\n" +
            "<h2>二、服务内容</h2>\n" +
            "<p>1. 我们为客户提供冷钱包服务，包括以下内容：\n" +
            "    (一)  转账、收款：可以搭配使用Sallet One APP进行转账、收款功能进行数字代币的管理，即运用私钥进行电子签名，对相关区块链的账本进行修改。转账是指付款方利用收款方的ENS域名或区块链地址进行转账操作，该“转账”行为涉及在相关区块链系统的分布式账本中对该交易的有效记录。\n" +
            "    (二)  管理数字代币：用户可以从Sallet One操作界面在默认钱包之外，导入、保管或移除其他的数字资产钱包。\n" +
            "    (三)  交易记录：APP可以将从链上获取并显示用户全部或部分的交易记录，但用户应以区块链的最新交易记录为准。\n" +
            "    (四)  暂停服务：基于区块链交易“不可撤销”的属性，Sallet One钱包不能为用户撤回或撤销交易操作。</p>\n" +
            "<p>2. 您在使用本服务过程中，所产生的应纳税赋，以及一切硬件、软件、服务及其它方面的费用，均由您独自承担。</p>\n" +
            "<p>3. 用户应当自行承担保管含有货币钱包的硬件设备、移动设备、备份密码、助记词等责任。如用户遗失助记词、钱包被盗或损坏、遗忘钱包密码造成损失，Sallet One均不负责；如用户进行兑换交易时误操作（例如输错转账地址、输错兑换数额），钱包无法取消交易，且亦不应对此承担任何责任。</p>\n" +
            "\n" +
            "<h2>三、服务使用规范</h2>\n" +
            "<p>1. 在钱包使用过程中，您承诺遵守以下约定：出于合法目的创建钱包及在使用钱包过程中实施的所有行为均遵守国家法律、法规等规范性文件及我们各项规则的规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不违反本条款及相关规则，用户保证存入钱包的数字资产来源合法。\n" +
            "</p>\n" +
            "<p>2. 您了解并同意：对于您涉嫌违反承诺的行为对任意第三方造成损害的，您均应当以自己的名义独立承担所有的法律责任，并应确保我们免于因此产生损失或增加费用。如您涉嫌违反有关法律或者本条款之规定，使我们遭受任何损失，或受到任何第三方的索赔，或受到任何行政管理部门的处罚，您应当赔偿我们因此造成的损失及（或）发生的费用，包括合理的律师费用。</p>\n" +
            "<h2>四、用户义务</h2>\n" +
            "<p>1. 用户不得利用本服务危害国家安全、泄露国家秘密，不得侵犯国家社会集体的和公民的合法权益，不得利用本服务制作、复制和传播下列信息：\n" +
            "    （1）煽动抗拒、破坏宪法和法律、行政法规实施的；\n" +
            "    （2）煽动颠覆国家政权，推翻社会主义制度的；\n" +
            "    （3）煽动分裂国家、破坏国家统一的；\n" +
            "    （4）煽动民族仇恨、民族歧视，破坏民族团结的；\n" +
            "    （5）捏造或者歪曲事实，散布谣言，扰乱社会秩序的；\n" +
            "    （6）宣扬封建迷信、淫秽、色情、赌博、暴力、凶杀、恐怖、教唆犯罪的；\n" +
            "    （7）公然侮辱他人或者捏造事实诽谤他人的，或者进行其他恶意攻击的；\n" +
            "    （8）损害国家机关信誉的；\n" +
            "    （9）其他违反宪法和法律行政法规的；\n" +
            "    （10）进行商业广告行为的。</p>\n" +
            "<p>2. 用户不得通过任何手段恶意注册Sallet One网站帐号，包括但不限于以牟利、炒作、套现、获奖等为目的多个帐户注册。用户亦不得盗用其他用户帐号。如用户违反上述规定，则我们有权直接采取一切必要的措施，包括但不限于删除用户发布的内容、暂停或查封用户帐号，取消因违规所获利益，乃至通过诉讼形式追究用户法律责任等。</p>\n" +
            "<p>3. 禁止用户将Sallet One以任何形式作为从事各种非法活动的场所、平台或媒介。未经我们的授权或许可，用户不得借用本站的名义从事任何商业活动，也不得以任何形式将Sallet One作为从事商业活动的场所、平台或媒介。</p>\n" +
            "<h2>五、隐私政策</h2>\n" +
            "<p>我们尊重用户的隐私。我们承诺不会向任何第三方透露用户的密码、助记词等信息</p>\n" +
            "<h2>六、法律适用、管辖与其他</h2>\n" +
            "<p>1. 本条款之效力、解释、变更、执行与争议解决均适用法律，如无相关法律规定的，则应参照通用国际商业惯例和（或）行业惯例。</p>\n" +
            "<p>2. 因本条款产生之争议，应首先通过友好协商解决，协商不成的，一方应将争议提交至国际仲裁中心解决。</p>\n" +
            "<p>3.本协议自用户创建钱包时生效，对协议双方具有约束力。</p>\n" +
            "<p>4. 本协议的最终解释权归平台所有。</p>";

    String enContent="<h1 align=\"center\"> Terms of Service 2021.6.1 Hao into force version </h1>\n" +
            "\n" +
            "<br></br>\n" +
            "\n" +
            "<p> Welcome to Sallet One . Please read the following terms of service (hereinafter referred to as \"the terms\")\n" +
            "    carefully, especially the content marked in bold. This clause describes the Sallet One platform, ATELAS SOLUTIONS\n" +
            "    GROUP LIMITED ( \"we\", \"us\" or \"us\" ) at https://www.salletone.com/ and the related mobile applications and products\n" +
            "    provided by us ( \"services\" Or \"Selei Wallet\" ) . By clicking the \"Create Account\" button or by accessing or using\n" +
            "    this service, it is deemed that you (the \"User\") agree to abide by these terms and all additional terms. If you do\n" +
            "    not agree to be bound by these terms, please do not access or use this service. We hope that you will review these\n" +
            "    terms frequently to ensure that you understand the terms and conditions that apply to your access and use of this\n" +
            "    service. These terms and additional terms apply to your access and use of any services provided by us, including but\n" +
            "    not limited to Sallet One wallet (hereinafter referred to as \"wallet\"), Sallet One website\n" +
            "    https://www.salletone.com/ (hereinafter referred to as \" Web Services”) and the mobile applications we provide.\n" +
            "    Signing other agreements related to the products we provide will not affect the effectiveness of this clause. If you\n" +
            "    use this service on behalf of any organization, you declare and warrant that (a) the organization is a legal\n" +
            "    organization that complies with local laws and regulations, (b)You have the right to accept these terms on behalf of\n" +
            "    the organization. If you violate these terms, the agency agrees to be responsible for your actions. </p>\n" +
            "\n" +
            "<h1> First, registration and account </h1>\n" +
            "\n" +
            "<h4>1. Registrant qualifications </h4>\n" +
            "\n" +
            "<p> You confirm that you should be a natural person, legal person or other organization with full capacity for civil\n" +
            "    rights and full capacity for civil conduct when you complete the registration process or actually use this service\n" +
            "    in any other way we allow. If you do not have the aforementioned subject qualifications, you and your guardian shall\n" +
            "    bear all the consequences resulting therefrom, and we have the right to cancel or permanently freeze your account\n" +
            "    and claim compensation from you and your guardian. </P>\n" +
            "\n" +
            "<h4>2. Registration and account </h4>\n" +
            "\n" +
            "<p> After you fill in the information as prompted on the registration page, read and agree to these terms and complete\n" +
            "    all registration procedures, or after you fill in the information as prompted on the recovery wallet page, read and\n" +
            "    agree to these terms, and complete all activation procedures, or you When you actually use the wallet in other ways\n" +
            "    we allow, you are bound by these terms. You can use your password and mnemonic phrase as a means to log in to Sallet\n" +
            "    One . If you forget the mnemonic phrase, we are not responsible for it, and you will bear any direct or indirect\n" +
            "    losses and adverse consequences arising therefrom. </p>\n" +
            "\n" +
            "<h4>3. Account Security </h4>\n" +
            "\n" +
            "<p> You are responsible for maintaining the confidentiality of your Sallet One password and mnemonic, and for all\n" +
            "    activities that occur under your login account (including but not limited to information disclosure, information\n" +
            "    release, online click to agree or submit each Such rule agreements, online renewal agreements or purchasing\n" +
            "    services, setting account information, etc.).\n" +
            "\n" +
            "    You agree:\n" +
            "\n" +
            "    (a) If anyone finds unauthorized use of your account and password, or any other violation of confidentiality\n" +
            "    regulations, you will immediately notify us;\n" +
            "\n" +
            "    (b) Ensure that you strictly abide by the security, operating mechanism or process of the wallet;\n" +
            "\n" +
            "    (c) Make sure that you leave the wallet in the correct steps at the end of each usage period. We cannot and will not\n" +
            "    be responsible for any losses incurred due to your failure to comply with the provisions of this paragraph. You\n" +
            "    understand that it takes reasonable time for us to take action on your request, and we are not responsible for the\n" +
            "    consequences (including but not limited to any loss of you) that have occurred before taking the action. </p>\n" +
            "\n" +
            "<h1> Second, service content </h1>\n" +
            "\n" +
            "<p>1. We provide customers with cold wallet services, including the following:\n" +
            "\n" +
            "    ( 1 ) Transfer and collection: It can be used with the Sallet One APP for transfer and collection functions to\n" +
            "    manage digital tokens, that is, use private keys for electronic signatures and modify related blockchain accounts.\n" +
            "    Transfer refers to the transfer operation of the payer using the ENS domain name or blockchain address of the payee\n" +
            "    . This \"transfer\" behavior involves the effective record of the transaction in the distributed ledger of the\n" +
            "    relevant blockchain system.\n" +
            "\n" +
            "    ( 2 ) Manage digital tokens: Users can import, keep or remove other digital asset wallets from the Sallet One\n" +
            "    operation interface in addition to the default wallet.\n" +
            "\n" +
            "    ( 3 ) Transaction records: APP can obtain and display all or part of the user's transaction records from the chain,\n" +
            "    but the user should refer to the latest transaction records of the blockchain.\n" +
            "\n" +
            "    ( 4 ) Suspension of service: Based on the \"irrevocable\" nature of blockchain transactions, Sallet One wallet cannot\n" +
            "    withdraw or revoke transaction operations for users. </p>\n" +
            "\n" +
            "<p>2. During your use of this service, you will be solely responsible for all taxable allowances and all hardware,\n" +
            "    software, service and other expenses incurred. </p>\n" +
            "\n" +
            "<p>3. The user shall be responsible for keeping the hardware devices, mobile devices, backup passwords, mnemonics, etc.\n" +
            "    containing currency wallets. If the user loses the mnemonic phrase, the wallet is stolen or damaged, or the wallet\n" +
            "    password is forgotten, Sallet One will not be responsible; if the user makes a mistake in the exchange transaction\n" +
            "    (such as entering the wrong transfer address, entering the wrong exchange amount), the wallet cannot cancel the\n" +
            "    transaction , And should not bear any responsibility for this. </p>\n" +
            "\n" +
            "\n" +
            "<h1> Three, service usage specifications </h1>\n" +
            "\n" +
            "<p>1. During the use of the wallet, you promise to abide by the following agreement: create a wallet for legal purposes\n" +
            "    and all actions implemented during the use of the wallet shall comply with national laws, regulations and other\n" +
            "    normative documents and our rules And requirements, do not violate social public interests or public morals, do not\n" +
            "    damage the legal rights of others, do not violate these terms and related rules, and users guarantee that the source\n" +
            "    of the digital assets deposited in the wallet is legal.\n" +
            "\n" +
            "</p>\n" +
            "\n" +
            "<p>2. You understand and agree: if your alleged violation of your commitment has caused damage to any third party, you\n" +
            "    should independently bear all legal responsibilities in your own name, and should ensure that we are free from\n" +
            "    losses or increases due to this cost. If you are suspected of violating the relevant laws or the provisions of these\n" +
            "    terms, causing us to suffer any loss, or subject to any third-party claims, or any administrative department's\n" +
            "    punishment, you should compensate us for the losses and/or expenses incurred thereby , Including reasonable attorney\n" +
            "    fees. </p>\n" +
            "\n" +
            "<h1> Four. User Obligations </h1>\n" +
            "\n" +
            "<p>1. Users must not use this service to endanger national security, divulge state secrets, and must not infringe the\n" +
            "    legal rights and interests of national social collectives and citizens, and must not use this service to produce,\n" +
            "    copy and disseminate the following information:\n" +
            "\n" +
            "    ( 1 ) Inciting resistance or undermining the implementation of the Constitution, laws and administrative\n" +
            "    regulations;\n" +
            "\n" +
            "    ( 2 ) Inciting to subvert state power and overthrow the socialist system;\n" +
            "\n" +
            "    ( 3 ) Inciting to split the country and undermining national unity;\n" +
            "\n" +
            "    ( 4 ) Inciting ethnic hatred, ethnic discrimination, and undermining ethnic unity;\n" +
            "\n" +
            "    ( 5 ) Fabricating or distorting facts, spreading rumors, disturbing social order;\n" +
            "\n" +
            "    ( 6 ) Propagating feudal superstition, obscenity, pornography, gambling, violence, murder, terror, and abetting\n" +
            "    crime;\n" +
            "\n" +
            "    ( 7 ) Openly insulting others or fabricating facts to slander others, or carrying out other malicious attacks;\n" +
            "\n" +
            "    ( 8 ) Damage to the credibility of state agencies;\n" +
            "\n" +
            "    ( 9 ) Other violations of the Constitution and laws and administrative regulations;\n" +
            "\n" +
            "    ( 10 ) Conducting commercial advertising activities. </p>\n" +
            "\n" +
            "<p>2. The user shall not maliciously register the Sallet One website account through any means , including but not\n" +
            "    limited to multiple account registration for the purpose of profit-making, hype, cash out, awards, etc. The user\n" +
            "    shall also not embezzle other user accounts. If the user violates the above regulations, we have the right to\n" +
            "    directly take all necessary measures, including but not limited to deleting the content posted by the user,\n" +
            "    suspending or sealing the user's account, canceling the benefits derived from the violation, and even prosecuting\n" +
            "    the user's legal responsibility through litigation. </p>\n" +
            "\n" +
            "<p>3. It is forbidden for users to use Sallet One in any form as a place, platform or medium for engaging in various\n" +
            "    illegal activities. Without our authorization or permission, users are not allowed to use the name of this site to\n" +
            "    engage in any commercial activities, nor to use Sallet One as a place, platform or medium for commercial activities\n" +
            "    in any form . </p>\n" +
            "\n" +
            "<h1> Five. Privacy Policy </h1>\n" +
            "\n" +
            "<p> We respect the privacy of users. We promise not to disclose the user's password, mnemonic words and other\n" +
            "    information to any third party </p>\n" +
            "\n" +
            "<h1> Six. Application of Law, Jurisdiction and Others </h1>\n" +
            "\n" +
            "<p>1. The validity, interpretation, modification, execution and dispute resolution of these terms are all applicable to\n" +
            "    laws. If there are no relevant legal provisions, general international business practices and/or industry practices\n" +
            "    should be referred to. </p>\n" +
            "\n" +
            "<p>2. Disputes arising from this clause shall first be resolved through friendly negotiation. If the negotiation fails,\n" +
            "    one party shall submit the dispute to the International Arbitration Center for resolution. </p>\n" +
            "\n" +
            "<p>3. This agreement takes effect when the user creates the wallet and is binding on both parties to the agreement. </p>\n" +
            "<p>4. The final interpretation of this agreement belongs to the platform. </p>";



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web);
        ButterKnife.bind(this);
        /**
         * Judging language
         */
            if(App.getSpString(App.language).equals("zh")) {
                webView.setText(Html.fromHtml(zhContent));

            }else {
                webView.setText(Html.fromHtml(enContent));

            }
            tvTitle.setText(getStringResources(R.string.about_service));

    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
