/*
 * Project Name  : 次世代WEB
 * Subsystem Name: 回収管理機能
 *
 * 
 * Created on 2004/07/27
 */
package jp.ags.acm.ag.action;

import java.sql.PreparedStatement;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import jp.ags.acm.action.AgsActionUtility;
import jp.ags.acm.common.AGSUtil;
import jp.ags.common.AGSM;

/**
 * <strong>[tran]回収管理機能／請求入金状況一覧(AG01)</strong>
 * <p>
 *
 * @author XXXX
 */
public class AGSAG01Action extends AgsActionUtility {
//  private static final int SEIKYU_NYUKIN_LIST_MAX_CNT = 1000 ;
    private static final int SEIKYU_NYUKIN_LIST_MAX_CNT = XXXXUtil.getPropertyInt("F_SEIKYU_NYUKIN_LIST_MAX_CNT");


    private static final String W_TYPE_DISP = "1" ;

    private static final String[] PD_EXTRACTED_DSP = {
        "（全て）",
        "未突合請求",
        "未突合入金",
        "未突合請求／未突合入金",
        "未消込(突合済)",
        "消込済",
    } ;
    private static final String[] PD_EXTRACTED_VAL = {
        "15",
        "1",
        "2",
        "3",
        "4",
        "8",
    } ;
//    private static final int EXTRACTED_ALL   = 0x0000000f ;
    private static final int EXTRACTED_SK    = 0x00000001 ;
    private static final int EXTRACTED_NK    = 0x00000002 ;
    private static final int EXTRACTED_SKNK  = 0x00000003 ;
    private static final int EXTRACTED_TG    = 0x00000004 ;
    private static final int EXTRACTED_KS    = 0x00000008 ;

    // 表示件数
/*
    private static final String[] PD_LISTCNT_DSP = {
        "20",
        "30",
        "40",
        "50",
    } ;
    private static final String[] PD_LISTCNT_VAL = {
        "20",
        "30",
        "40",
        "50",
    } ;
*/
    // ソートのカラム列
    private static final String[] SORT_COLUMN = {
        "SK_DT",
        "SK_USERCD",
        "SK_TOTAL",
        "SK_MISYORI",
        "SK_SUM",
        "NK_DT",
        "NK_TOTAL",
        "NK_MISYORI",
        "NK_SUM",
    } ;
    // ソート順
    private static final String[] ASCEND = {
        "ASC",
        "DESC",
    } ;
    // アクション
    private static final String[] ACTION = {
        "SEARCH",
        "SORT",
        "LISTCNT",
        "PAGEBREAK",
        "RETURN",
        "KESHIKOMI",
        "UPDATE",
    } ;
    private static final int ACT_SEARCH            = 0 ; // 検索
    private static final int ACT_SORT              = 1 ; // 並び替え
    private static final int ACT_LISTCNT           = 2 ; // 表示件数変更
    private static final int ACT_PAGEBREAK         = 3 ; // 改ページ
    private static final int ACT_RETURN            = 4 ; // 戻る
    private static final int ACT_KESHIKOMI         = 5 ; // 消込
    private static final int ACT_UPDATE            = 6 ; // 更新戻り

//    private int action_ ;

/**
 * 入力チェックを行なう.
 *
 * @return boolean チェックＯＫのときtrue、ＮＧのときfalse
 * @since バージョン 1.00
 */
    protected boolean checkInput() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#checkInput()") ;
        printData() ;
        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

       int action_ ;
       ActionErrors errors = new ActionErrors() ;

/* 2004/10/26 排他制御 */
        // ★★★DB2の正当性をチェックする★★★
        String pid = getPageId(db1) ;
        if(!(pid.equals("AG01") ||
            pid.equals("AG05") ||
            pid.equals("AG07") ||
            pid.equals("AG08") ||
            pid.equals("AG10") ||
            pid.equals("AG12") ||
            pid.equals("AG14") ||
            pid.equals("AG15") ||
            pid.equals("AG16") ||
            pid.equals("AG17") ||
            pid.equals("AG18") ||
            pid.equals("AG19") ||
			pid.equals("AG20") ||
            pid.equals("AJ01") ||
            pid.equals("AJ03") ||
            pid.equals("AJ04") ||
            pid.equals("AJ05") ||
            pid.equals("EA913"))){
            String clctbisflg = db1.getString("CLCTBISFLG");
            db1.clear();
            db1.setString("CLCTBISFLG", clctbisflg);

            if(!(null == db2)) {
                db2.clear();
            }
            db1.setInt("INIT", 1) ;
            setTranId(db1,"AG01");
        } else if(pid.equals("AJ04") && db1.getString("_ACTION").equals("UPDATE")) {
            XXXXDataBean db2_aj04 = getDataBean("DB2_AJ04");
            db2.setString("SEQ", db2_aj04.getString("SEQ"));
            db1.setString("_PNO", db2_aj04.getString("AG01_PNO"));
            db1.setString("_ACTION", "RETURN");
            removeDataBean("DB2_AJ04") ;
		} else if(pid.equals("AG17") && db1.getString("_ACTION").equals("INSERT")) {
			XXXXDataBean db2_aj04 = getDataBean("DB2_AG17");
			db2.setString("SEQ", db2_aj04.getString("SEQ"));
			db1.setString("_PNO", db2_aj04.getString("AG01_PNO"));
			db1.setString("_ACTION", "RETURN");
			removeDataBean("DB2_AG17") ;
        }
        if (!isSeq(db2, true)) {
            XXXXUtil.log(XXXXUtil.DEBUG, "isSeq()=false", db2) ;
            db2 = new XXXXDefaultDataBean() ;
            setDataBean("DB2_AG01", db2) ;
            db2.setString("SEQ", getNextSeq()) ;
            setError(db2, new ActionErrors()) ;
            db1.setInt("INIT", 1) ;
        }
        else {
            db1.setInt("INIT", 0) ;
            errors = getError(db1);
            if(null == errors) {
            	errors = new ActionErrors();
            }
        }

        action_ = -1 ;

/* 2004/10/26 排他制御 */
        if (pid.equals("AG01") ||
            pid.equals("AG05") ||
            pid.equals("AG07") ||
            pid.equals("AG08") ||
            pid.equals("AG10") ||
            pid.equals("AG12") ||
            pid.equals("AG14") ||
            pid.equals("AG15") ||
            pid.equals("AG16") ||
            pid.equals("AG17") ||
            pid.equals("AG18") ||
            pid.equals("AG19") ||
			pid.equals("AG20") ||
            pid.equals("AJ01") ||
            pid.equals("AJ03") ||
            pid.equals("AJ04") ||
            pid.equals("AJ05") ||
            pid.equals("EA913")) {

            /*------------------ リクエストの取得 --------------------------------*/
            // アクション
            action_ = AGSUtil.getIndex(ACTION, db1.getStringNvl("_ACTION")) ;

            // 検索条件
            // 2006-05-17 枝番複数選択 START
            //String pd_agsubcd = db1.getStringNvl("_PD_AGSUBCD") ;
            String[] pd_agsubcd = db1.getStringArrayNvl("_PD_AGSUBCD",new String[0]) ;
            // 2006-05-17 枝番複数選択 END
            String pd_extracted = db1.getStringNvl("_PD_EXTRACTED") ;
            String pd_sk_seg = db1.getStringNvl("_PD_SK_SEG") ;
            String pd_nk_seg = db1.getStringNvl("_PD_NK_SEG") ;
            String pd_tgc = db1.getStringNvl("_PD_TGC") ;
            String rb_tg_ptn = db1.getStringNvl("_RB_TG_PTN") ;
            String skFromYear = db1.getStringNvl("_SKFROMYEAR") ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
            String skFromMonth = db1.getStringPad("_SKFROMMONTH","0",2) ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
            String pd_skFromDay = db1.getStringNvl("_PD_SKFROMDAY") ;
            String skToYear = db1.getStringNvl("_SKTOYEAR") ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
            String skToMonth = db1.getStringPad("_SKTOMONTH","0",2) ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
            String pd_skToDay = db1.getStringNvl("_PD_SKTODAY") ;
            String nkFromYear = db1.getStringNvl("_NKFROMYEAR") ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
            String nkFromMonth = db1.getStringPad("_NKFROMMONTH","0",2) ;
            String nkFromDay = db1.getStringPad("_NKFROMDAY","0",2) ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
            String nkToYear = db1.getStringNvl("_NKTOYEAR") ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
            String nkToMonth = db1.getStringPad("_NKTOMONTH","0",2) ;
            String nkToDay = db1.getStringPad("_NKTODAY","0",2) ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
            // 2004-12-15 入金登録日追加 ST
            String nkinFromYear = db1.getStringNvl("_NKINFROMYEAR") ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
            String nkinFromMonth = db1.getStringPad("_NKINFROMMONTH","0",2) ;
            String nkinFromDay = db1.getStringPad("_NKINFROMDAY","0",2) ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
            String nkinToYear = db1.getStringNvl("_NKINTOYEAR") ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
            String nkinToMonth = db1.getStringPad("_NKINTOMONTH","0",2) ;
            String nkinToDay = db1.getStringPad("_NKINTODAY","0",2) ;
            // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
            // 2004-12-15 入金登録日追加 EX
            // 2006-05-16 支払方法・入金方法条件追加 START
            String pd_nk_houhou = db1.getStringNvl("_PD_NK_HOUHOU") ;
            String pd_sk_houhou = db1.getStringNvl("_PD_SK_HOUHOU");
            // 2006-05-16 支払方法・入金方法条件追加 END
            String usercd = db1.getStringNvl("_USERCD") ;
            // 2019/05/10 回収管理機能仕様変更対応 ADD START
            String nkpayFromYear = db1.getStringNvl("_NKPAYFROMYEAR") ;
            String nkpayFromMonth = db1.getStringPad("_NKPAYFROMMONTH","0",2) ;
            String nkpayFromDay = db1.getStringPad("_NKPAYFROMDAY","0",2) ;
            String nkpayToYear = db1.getStringNvl("_NKPAYTOYEAR") ;
            String nkpayToMonth = db1.getStringPad("_NKPAYTOMONTH","0",2) ;
            String nkpayToDay = db1.getStringPad("_NKPAYTODAY","0",2) ;
            String pd_resstp = db1.getStringNvl("_PD_RESSTP");
            String stopreason_312 = db1.getStringNvl("_STOPREASON_312");
            String stopreason_501 = db1.getStringNvl("_STOPREASON_501");
            String stopreason_000 = db1.getStringNvl("_STOPREASON_000");
            String stopreason_963 = db1.getStringNvl("_STOPREASON_963");
            String stopreason_725 = db1.getStringNvl("_STOPREASON_725");
            // 2019/05/10 回収管理機能仕様変更対応 ADD END

            // 並び替え
            String sort = db1.getStringNvl("_SORT") ;
            String ascend = db1.getStringNvl("_ASCEND") ;

            // 表示件数
            String pd_listCnt = db1.getStringNvl("_PD_LISTCNT") ;

            // 突合選択チェックボックス
            String[] wlh_pk = db1.getStringArrayNvl("_WLH_PK", new String[0]) ;
            String[] cb_check_flg = db1.getStringArrayNvl("_CB_CHECK_FLG", new String[0]) ;

           // 改ページNo
            String pno = db1.getStringNvl("_PNO") ;

            /*------------------ リクエストの取得 ここまで ---------------------------*/

            /*------------------ 入力チェック ----------------------------------------*/
            if(action_ == ACT_SEARCH) {
                errors = new ActionErrors();
            }
            int chk = 0 ;
            int rc = 0 ;
}

