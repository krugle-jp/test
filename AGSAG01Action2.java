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
            if (action_ == ACT_SEARCH || action_ == ACT_UPDATE) {
                setTranId(db1, "AG01") ;
                // 枝番(_PD_AGSUBCD)のチェック
                String[] agsubcdList = getLoginAgsubcdList() ;
                if (getMastercd().equals(MASTERCD_T)) {
                    agsubcdList = XXXXUtil.addHead(agsubcdList, AGSUBCD_ALL_VAL) ;
                }
                if (AGSUtil.getIndex(agsubcdList, pd_agsubcd) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "_PD_AGSUBCDエラー") ;
                    errors.add("ER_AG01_PD_AGSUBCD", new ActionMessage("errors.ER_AG01_PD_AGSUBCD")) ;
                }
                // 抽出条件
                if (AGSUtil.getIndex(PD_EXTRACTED_VAL, pd_extracted) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "抽出条件error=", pd_extracted) ;
                    errors.add("ER_AG01_PD_EXTRACTED", new ActionMessage("errors.ER_AG01_PD_EXTRACTED")) ;
                }
                // 請求種別
                if ((pd_sk_seg.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_SEIKYUSHUBETSU, "SKS_CD"), pd_sk_seg) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "請求種別error=", pd_sk_seg) ;
                    errors.add("ER_AG01_PD_SK_SEG", new ActionMessage("errors.ER_AG01_PD_SK_SEG")) ;
                }
                // 入金種別
                if ((pd_nk_seg.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_NYUKINSHUBETSU, "NKS_CD"), pd_nk_seg) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金種別error=", pd_nk_seg) ;
                    errors.add("ER_AG01_PD_NK_SEG", new ActionMessage("errors.ER_AG01_PD_NK_SEG")) ;
                }
                // 2017/12/07 登録改善フェーズ2.5対応 CHG START
                // 2006-05-16 支払方法・入金方法条件追加 START
                // 支払方法
                if ((pd_sk_houhou.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_SK_PAYSEG_CREDIT, "SK_PAYSEG_CREDIT_CD"), pd_sk_houhou) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "支払方法error=", pd_sk_houhou) ;
                    errors.add("ER_AG01_PD_SK_HOUHOU", new ActionMessage("errors.ER_AG01_PD_SK_HOUHOU")) ;
                }
                // 2017/12/07 登録改善フェーズ2.5対応 CHG END
                // 入金方法
                if ((pd_nk_houhou.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_NYUKINHOUHOU, "NY_CD"), pd_nk_houhou) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金方法error=", pd_nk_houhou) ;
                    errors.add("ER_AG01_PD_NK_HOUHOU", new ActionMessage("errors.ER_AG01_PD_NK_HOUHOU")) ;
                }
                // 2006-05-16 支払方法・入金方法条件追加 END
                // 突合調整区分
                if (AGSUtil.getIndex(XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_CHOUSEIKOUMOKU_ALL, "CS_CD"), ""), pd_tgc) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "突合調整区分error=", pd_tgc) ;
                    errors.add("ER_AG01_PD_TGC", new ActionMessage("errors.ER_AG01_PD_TGC")) ;
                }
                // 突合パターン
                if (AGSUtil.getIndex(XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_CD"), ""), rb_tg_ptn) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "突合パターンerror=", rb_tg_ptn) ;
                    errors.add("ER_AG01_RB_TG_PTN", new ActionMessage("errors.ER_AG01_RB_TG_PTN")) ;
                }
                // 請求締日
                // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
                rc = checkDateSP(skFromYear, skFromMonth, pd_skFromDay, skToYear, skToMonth, pd_skToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "請求締日開始error=", skFromYear + "/" + skFromMonth + "/" + pd_skFromDay) ;
                    errors.add("ER_AG01_SKFROM", new ActionMessage("errors.ER_AG01_SKFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "請求締日終了error=", skToYear + "/" + skToMonth + "/" + pd_skToDay) ;
                    errors.add("ER_AG01_SKTO", new ActionMessage("errors.ER_AG01_SKTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "請求締日FromとToの妥当性エラー") ;
                    errors.add("ER_AG01_SK_DT", new ActionMessage("errors.ER_AG01_SK_DT")) ;
                }
                // 入金日
                rc = checkDate(nkFromYear, nkFromMonth, nkFromDay, nkToYear, nkToMonth, nkToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金日開始error=", nkFromYear + "/" + nkFromMonth + "/" + nkFromDay) ;
                    errors.add("ER_AG01_NKFROM", new ActionMessage("errors.ER_AG01_NKFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金日終了error=", nkToYear + "/" + nkToMonth + "/" + nkToDay) ;
                    errors.add("ER_AG01_NKTO", new ActionMessage("errors.ER_AG01_NKTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金日FromとToの妥当性エラー") ;
                    errors.add("ER_AG01_NK_DT", new ActionMessage("errors.ER_AG01_NK_DT")) ;
                }
                // 2004-12-15 入金登録日追加 EX
                // 入金登録日日
                rc = checkDate(nkinFromYear, nkinFromMonth, nkinFromDay, nkinToYear, nkinToMonth, nkinToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金登録日開始error=", nkinFromYear + "/" + nkinFromMonth + "/" + nkinFromDay) ;
                    errors.add("ER_AG01_NKINFROM", new ActionMessage("errors.ER_AG01_NKINFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金登録日終了error=", nkinToYear + "/" + nkinToMonth + "/" + nkinToDay) ;
                    errors.add("ER_AG01_NKINTO", new ActionMessage("errors.ER_AG01_NKINTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "入金登録日FromとToの妥当性エラー") ;
                    errors.add("ER_AG01_NKIN_DT", new ActionMessage("errors.ER_AG01_NKIN_DT")) ;
                }
                // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
                // 2004-12-15 入金登録日追加 EX
                // お客様コード
                chk = XXXXUtil.CHK_NUMALPH | XXXXUtil.CHK_SIZ0_OK ;// 2017/02/15 登録改善対応 CHG
                rc = XXXXUtil.checkData(usercd, chk, 8, 8) ;
                if (rc != XXXXUtil.RC_OK) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "お客様コードerror=", usercd) ;
                    errors.add("ER_AG01_USERCD", new ActionMessage("errors.ER_AG01_USERCD")) ;
                }
                // 2019/05/10 回収管理機能仕様変更対応 ADD START
                // 支払日
                rc = checkDate(nkpayFromYear, nkpayFromMonth, nkpayFromDay, nkpayToYear, nkpayToMonth, nkpayToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "支払日開始error=", nkpayFromYear + "/" + nkpayFromMonth + "/" + nkpayFromDay) ;
                    errors.add("ER_AG01_NKPAYFROM", new ActionMessage("errors.ER_AG01_NKPAYFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "支払日終了error=", nkpayToYear + "/" + nkpayToMonth + "/" + nkpayToDay) ;
                    errors.add("ER_AG01_NKPAYTO", new ActionMessage("errors.ER_AG01_NKPAYTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "支払日FromとToの妥当性エラー") ;
                    errors.add("ER_AG01_NKPAY_DT", new ActionMessage("errors.ER_AG01_NKPAY_DT")) ;
                }
                // 受注停止有無
                if ((pd_resstp.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_PD_RESSTP, "RS_PD_RESSTP_CD"), pd_resstp) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "受注停止有無error=", pd_resstp) ;
                    errors.add("ER_AG01_PD_RESSTP", new ActionMessage("errors.ER_AG01_PD_RESSTP")) ;
                }
                // 受注停止理由
            	if ((stopreason_312.length() != 0 && (!stopreason_312.equals("312"))) ||
                    (stopreason_501.length() != 0 && (!stopreason_501.equals("501"))) ||
                    (stopreason_000.length() != 0 && (!stopreason_000.equals("000"))) ||
                    (stopreason_963.length() != 0 && (!stopreason_963.equals("963"))) ||
                    (stopreason_725.length() != 0 && (!stopreason_725.equals("725")))) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "受注停止理由error=", pd_resstp) ;
                    errors.add("ER_AG01_STOPREASON", new ActionMessage("errors.ER_AG01_STOPREASON")) ;
                }
                // 2019/05/10 回収管理機能仕様変更対応 ADD END
            }
            if (action_ == ACT_SORT) {
                if (AGSUtil.getIndex(SORT_COLUMN, sort) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "並び替え列error=", sort) ;
                    errors.add("ER_AG01_SORT", new ActionMessage("errors.ER_AG01_SORT")) ;
                }
                if (AGSUtil.getIndex(ASCEND, ascend) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "並び替え順error=", ascend) ;
                    errors.add("ER_AG01_ASCEND", new ActionMessage("errors.ER_AG01_ASCEND")) ;
                }
            }
            if (action_ == ACT_LISTCNT) {
                if (AGSUtil.getIndex(PD_LISTCNT_VAL, pd_listCnt) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "表示件数error=", pd_listCnt) ;
                    errors.add("ER_AG01_PD_LISTCNT", new ActionMessage("errors.ER_AG01_PD_LISTCNT")) ;
                }
            }
            if (action_ == ACT_RETURN) {
                // 検索条件が有効か
                if (!db2.getStringNvl("LIST_VALID_FLG").equals("1")) {
                    action_ = -1 ;
                }
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_RETURN)) {
                // 改ページ範囲チェック
                chk = XXXXUtil.CHK_NUM ;
                rc = XXXXUtil.checkData(pno, chk, 1, 8) ;
                if (rc == XXXXUtil.RC_OK) {
                    int first_page_no = db2.getIntNvl("FIRST_PAGE_NO", 0) ;
                    int last_page_no = db2.getIntNvl("LAST_PAGE_NO", 0) ;
                    int pageNo = Integer.parseInt(pno) ;
                    if ((pageNo < first_page_no) || (last_page_no < pageNo)) {
                        rc = -1 ;
                    }
                }
                if (rc != XXXXUtil.RC_OK) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "改ページNOerror=", pno) ;
                    errors.add("ER_AG01_PNO", new ActionMessage("errors.ER_AG01_PNO")) ;
                }
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_KESHIKOMI) || (action_ == ACT_LISTCNT)) {

                // 突合選択チェックボックス
                chk = XXXXUtil.CHK_NUM | XXXXUtil.CHK_SIZ0_OK ;
                int len = wlh_pk.length ;
                for (int i = 0 ; i < len ; i++) {
                    if (wlh_pk[i] != null) {
                        rc = XXXXUtil.checkData(wlh_pk[i], chk, 0, 20) ;
                        if (rc != XXXXUtil.RC_OK) {
                            XXXXUtil.log(XXXXUtil.DEBUG, "突合チェックボックスpk error=", wlh_pk[i]) ;
                            errors.add("ER_AG01_WLH_PK", new ActionMessage("errors.ER_AG01_WLH_PK")) ;
                            break ;
                        }
                    }
                }
                len = cb_check_flg.length ;
                for (int i = 0 ; i < len ; i++) {
                    if ((cb_check_flg[i] != null) && !cb_check_flg[i].equals("1")) {
                        XXXXUtil.log(XXXXUtil.DEBUG, "突合チェックボックスcb_check_flg error=", cb_check_flg[i]) ;
                        errors.add("ER_AG01_CB_CHECK_FLG", new ActionMessage("errors.ER_AG01_CB_CHECK_FLG")) ;
                        break ;
                    }
                }
            }

            /*------------------ 入力チェック ここまで -------------------------------*/

            /*------------------ 引継ぎデータをDB2に設定する -------------------------*/
            if (action_ == ACT_SEARCH||action_ == ACT_UPDATE) {
                // 検索条件
            	// 2006-05-17 枝番複数選択 START
            	//db2.setString("PD_AGSUBCD", pd_agsubcd) ;
                db2.setStringArray("PD_AGSUBCD", pd_agsubcd) ;
                // 2006-05-17 枝番複数選択 END
                db2.setString("PD_EXTRACTED", pd_extracted) ;
                db2.setString("PD_SK_SEG", pd_sk_seg) ;
                db2.setString("PD_NK_SEG", pd_nk_seg) ;
                db2.setString("PD_TGC", pd_tgc) ;
                db2.setString("RB_TG_PTN", rb_tg_ptn) ;
                db2.setString("SKFROMYEAR", skFromYear) ;
                // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
                db2.setString("SKFROMMONTH", skFromMonth) ;
                db2.setString("PD_SKFROMDAY", pd_skFromDay) ;
                db2.setString("SKTOYEAR", skToYear) ;
                db2.setString("SKTOMONTH", skToMonth) ;
                db2.setString("PD_SKTODAY", pd_skToDay) ;
                db2.setString("NKFROMYEAR", nkFromYear) ;
                db2.setString("NKFROMMONTH", nkFromMonth) ;
                db2.setString("NKFROMDAY", nkFromDay) ;
                db2.setString("NKTOYEAR", nkToYear) ;
                db2.setString("NKTOMONTH", nkToMonth) ;
                db2.setString("NKTODAY", nkToDay) ;
                // 2004-12-15 入金登録日追加 ST
                db2.setString("NKINFROMYEAR", nkinFromYear) ;
                db2.setString("NKINFROMMONTH", nkinFromMonth) ;
                db2.setString("NKINFROMDAY", nkinFromDay) ;
                db2.setString("NKINTOYEAR", nkinToYear) ;
                db2.setString("NKINTOMONTH", nkinToMonth) ;
                db2.setString("NKINTODAY", nkinToDay) ;
                // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
                // 2004-12-15 入金登録日追加 EX
                // 2006-05-16 支払方法・入金方法条件追加 START
                db2.setString("PD_SK_HOUHOU", pd_sk_houhou);
                db2.setString("PD_NK_HOUHOU", pd_nk_houhou);
                // 2006-05-16 支払方法・入金方法条件追加 END
                db2.setString("USERCD", usercd) ;
                // 2019/05/10 回収管理機能仕様変更対応 ADD START
                db2.setString("NKPAYFROMYEAR", nkpayFromYear) ;
                db2.setString("NKPAYFROMMONTH", nkpayFromMonth) ;
                db2.setString("NKPAYFROMDAY", nkpayFromDay) ;
                db2.setString("NKPAYTOYEAR", nkpayToYear) ;
                db2.setString("NKPAYTOMONTH", nkpayToMonth) ;
                db2.setString("NKPAYTODAY", nkpayToDay) ;
                db2.setString("PD_RESSTP", pd_resstp);
                db2.setString("STOPREASON_312", stopreason_312);
                db2.setString("STOPREASON_501", stopreason_501);
                db2.setString("STOPREASON_000", stopreason_000);
                db2.setString("STOPREASON_963", stopreason_963);
                db2.setString("STOPREASON_725", stopreason_725);
                // 2019/05/10 回収管理機能仕様変更対応 ADD END

                // 検索条件有効フラグを無効にする
                db2.setString("LIST_VALID_FLG", "0") ;

            }
            if (action_ == ACT_SORT) {
                db2.setString("SORT", sort) ;
                db2.setString("ASCEND", ascend) ;
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_RETURN)) {
                // 改ページ制御
                db2.setString("REQUEST_PAGE_NO", pno) ;
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_KESHIKOMI) || (action_ == ACT_LISTCNT)) {
                // 突合選択チェックボックス
                db2.setStringArray("WLH_PK", wlh_pk) ;
                db2.setStringArray("CB_CHECK_FLG", cb_check_flg) ;
            }
            if (action_ == ACT_LISTCNT) {
                db2.setString("PD_LISTCNT", pd_listCnt) ;
            }
            /*------------------ 引継ぎデータをDB2に設定する ここまで ----------------*/
        }

        db2.setInt("action_", action_) ;

        setError(db1, errors) ;

        printData() ;
        return true ;
    } // checkInput()

/**
 * 業務処理を行なう.
 *
 * @since バージョン 1.00
 */
    protected void execute() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#execute()") ;
        printData() ;
        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int action_ ;
        action_ = db2.getInt("action_") ;

//        String pid = getPageId(db1) ;

        ActionErrors errors = getError(db1) ;

        if (errors.isEmpty()) {
            int rc = accessDB() ;
            if (rc < 0) {
                logError("AGSAG01Action#システムエラー(accessDB rc:"+rc+")") ;
                setTranId(db1, "EA999") ;
                return ;
            }
            else if (rc > 0) {
                errors.add("ER_AG01_MAX_OVER", new ActionMessage("errors.ER_AG01_MAX_OVER", Integer.toString(SEIKYU_NYUKIN_LIST_MAX_CNT))) ;
            }
            else {
                // NOP
            }
        }

        if (action_ == ACT_KESHIKOMI) {
            XXXXUtil.log(XXXXUtil.DEBUG, "消込へ遷移") ;
            XXXXDataBean db2_keshikomi = new XXXXDefaultDataBean() ;
            setDataBean("DB2_KESHIKOMI", db2_keshikomi) ;
            setTranId(db1, "AG19") ;
            return ;
        }

        /*------------------ 画面部品の設定 --------------------------------------*/
        // エラー
        setError(db1, errors) ;
        setError(db2, errors) ;

        // 枝番
        String[] agsubcdList = getLoginAgsubcdList() ;
        String[] pd_agsubcd_val = agsubcdList ;
        String[] pd_agsubcd_dsp = agsubcdList ;
        if (getMastercd().equals(MASTERCD_T)) {
            pd_agsubcd_val = XXXXUtil.addHead(pd_agsubcd_val, AGSUBCD_ALL_VAL) ;
            pd_agsubcd_dsp = XXXXUtil.addHead(pd_agsubcd_dsp, "(全て)") ;
        }
        db1.setString("MASTERCD", getMastercd());

		db1.setString("OPE_AGSUBCD", getLoginAgsubcd());

        db1.setStringArray("CHK_AGSUBCD",pd_agsubcd_val);

    	// 2006-05-18 枝番複数選択 START
        //db1.setPullDown(
        //    "PD_AGSUBCD",
        //    pd_agsubcd_val,
        //    XXXXUtil.nvl2(pd_agsubcd_dsp, pd_agsubcd_dsp.length, NOAGSUBCD_DSP),
        //    db2.getStringNvl2("PD_AGSUBCD", AGSUBCD_ALL_VAL)
        //) ;
        if (getMastercd().equals(MASTERCD_T)){
        db1.setMultiplePullDown(
              "PD_AGSUBCD",
              pd_agsubcd_val,
              XXXXUtil.nvl2(pd_agsubcd_dsp, pd_agsubcd_dsp.length, NOAGSUBCD_DSP),
              db2.getStringArrayNvl("PD_AGSUBCD", AGSUBCD_ALL_VAL)
          ) ;
        }else{
            db1.setMultiplePullDown(
                    "PD_AGSUBCD",
                    pd_agsubcd_val,
                    XXXXUtil.nvl2(pd_agsubcd_dsp, pd_agsubcd_dsp.length, NOAGSUBCD_DSP),
                    db2.getStringArrayNvl("PD_AGSUBCD", agsubcdList)
                ) ;
        }
    	// 2006-05-18 枝番複数選択 END


        // 抽出条件
        db1.setPullDown(
            "PD_EXTRACTED",
            PD_EXTRACTED_VAL,
            PD_EXTRACTED_DSP,
            db2.getStringNvl("PD_EXTRACTED", PD_EXTRACTED_VAL[0])
        ) ;
        // 請求種別
        db1.setPullDown(
            "PD_SK_SEG",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_SEIKYUSHUBETSU, "SKS_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_SEIKYUSHUBETSU, "SKS_NAME"), "(全て)"),
            db2.getStringNvl("PD_SK_SEG")
        ) ;
        // 入金種別
        db1.setPullDown(
            "PD_NK_SEG",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINSHUBETSU, "NKS_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINSHUBETSU, "NKS_NAME"), "(全て)"),
            db2.getStringNvl("PD_NK_SEG")
        ) ;
        // 2017/12/07 登録改善フェーズ2.5対応 CHG START
        // 2006-05-16 支払方法・入金方法条件追加 START
        // 支払方法
        db1.setPullDown(
            "PD_SK_HOUHOU",
            AGSM.getStringArray(AGSM.RS_SK_PAYSEG_CREDIT, "SK_PAYSEG_CREDIT_CD"),
            AGSM.getStringArray(AGSM.RS_SK_PAYSEG_CREDIT, "SK_PAYSEG_CREDIT_NM"),
            db2.getStringNvl("PD_SK_HOUHOU")
        );
        // 2017/12/07 登録改善フェーズ2.5対応 CHG END
        // 入金方法
        db1.setPullDown(
            "PD_NK_HOUHOU",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINHOUHOU, "NY_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINHOUHOU, "NY_NAME"), "(全て)"),
            db2.getStringNvl("PD_NK_HOUHOU")
        );
        // 2006-05-16 支払方法・入金方法条件追加 END
        // 調整区分
        db1.setPullDown(
            "PD_TGC",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_CHOUSEIKOUMOKU_ALL, "CS_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_CHOUSEIKOUMOKU_ALL, "CS_NAME"), "(全て)"),
            db2.getStringNvl("PD_TGC")
        ) ;
        // 突合パターン
        db1.setRadioButton(
            "RB_TG_PTN",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_CD"), ""),
//            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_NAME"), "(全て)"), //略称表示 2004-11-13
//            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_PD_NAME"), "(全て)"), //略称＋名称表示 2004-11-13
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_SHORTNAME_MOD"), "(全て)"), //略称表示 2004-12-10
            db2.getStringNvl("RB_TG_PTN")
        ) ;
        // 請求締日開始年月日
        // 2005-10-14 請求入金状況一覧の日付入力方式変更 START
        db1.setString("SKFROMYEAR", db2.getStringNvl("SKFROMYEAR")) ;
        db1.setString("SKFROMMONTH", db2.getStringNvl("SKFROMMONTH")) ;
        db1.setPullDown(
                "PD_SKFROMDAY",
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL"),
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_DSP"),
                db2.getStringNvl("PD_SKFROMDAY", AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL")[0])
        ) ;
        // 請求締日終了年月日
        db1.setString("SKTOYEAR", db2.getStringNvl("SKTOYEAR")) ;
        db1.setString("SKTOMONTH", db2.getStringNvl("SKTOMONTH")) ;
        db1.setPullDown(
                "PD_SKTODAY",
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL"),
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_DSP"),
                db2.getStringNvl("PD_SKTODAY", AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL")[0])
        ) ;
        // 入金日開始年月日
        db1.setString("NKFROMYEAR", db2.getStringNvl("NKFROMYEAR")) ;
        db1.setString("NKFROMMONTH", db2.getStringNvl("NKFROMMONTH")) ;
        db1.setString("NKFROMDAY", db2.getStringNvl("NKFROMDAY")) ;
        // 入金日終了年月日
        db1.setString("NKTOYEAR", db2.getStringNvl("NKTOYEAR")) ;
        db1.setString("NKTOMONTH", db2.getStringNvl("NKTOMONTH")) ;
        db1.setString("NKTODAY", db2.getStringNvl("NKTODAY")) ;
        // 2004-12-15 入金登録日追加 ST
        // 入金登録日開始年月日
        db1.setString("NKINFROMYEAR", db2.getStringNvl("NKINFROMYEAR")) ;
        db1.setString("NKINFROMMONTH", db2.getStringNvl("NKINFROMMONTH")) ;
        db1.setString("NKINFROMDAY", db2.getStringNvl("NKINFROMDAY")) ;
        // 入金登録日終了年月日
        db1.setString("NKINTOYEAR", db2.getStringNvl("NKINTOYEAR")) ;
        db1.setString("NKINTOMONTH", db2.getStringNvl("NKINTOMONTH")) ;
        db1.setString("NKINTODAY", db2.getStringNvl("NKINTODAY")) ;
        // 2005-10-14 請求入金状況一覧の日付入力方式変更 END
        // 2004-12-15 入金登録日追加 EX
        // お客様番号
        db1.setString("USERCD", db2.getStringNvl("USERCD")) ;
        // 2019/05/10 回収管理機能仕様変更対応 ADD START
        // 支払日開始年月日
        db1.setString("NKPAYFROMYEAR", db2.getStringNvl("NKPAYFROMYEAR")) ;
        db1.setString("NKPAYFROMMONTH", db2.getStringNvl("NKPAYFROMMONTH")) ;
        db1.setString("NKPAYFROMDAY", db2.getStringNvl("NKPAYFROMDAY")) ;
        // 支払日終了年月日
        db1.setString("NKPAYTOYEAR", db2.getStringNvl("NKPAYTOYEAR")) ;
        db1.setString("NKPAYTOMONTH", db2.getStringNvl("NKPAYTOMONTH")) ;
        db1.setString("NKPAYTODAY", db2.getStringNvl("NKPAYTODAY")) ;
        // 受注停止有無
        db1.setPullDown(
            "PD_RESSTP",
            AGSM.getStringArray(AGSM.RS_PD_RESSTP, "RS_PD_RESSTP_CD"),
            AGSM.getStringArray(AGSM.RS_PD_RESSTP, "RS_PD_RESSTP_NM"),
            db2.getStringNvl("PD_RESSTP")
        );
    	// 受注停止理由
    	db1.setString("STOPREASON_312", db2.getStringNvl("STOPREASON_312")) ;
    	db1.setString("STOPREASON_501", db2.getStringNvl("STOPREASON_501")) ;
    	db1.setString("STOPREASON_000", db2.getStringNvl("STOPREASON_000")) ;
    	db1.setString("STOPREASON_963", db2.getStringNvl("STOPREASON_963")) ;
    	db1.setString("STOPREASON_725", db2.getStringNvl("STOPREASON_725")) ;
        // 2019/05/10 回収管理機能仕様変更対応 ADD END
        // 表示件数
        db1.setPullDown(
            "PD_LISTCNT",
            PD_LISTCNT_VAL,
            PD_LISTCNT_DSP,
            db2.getStringNvl("CURRENT_LISTCNT", PD_LISTCNT_VAL[0])
        ) ;
        // 並び替え
        db1.setString("SORT", db2.getStringNvl("SORT", SORT_COLUMN[0])) ;
        db1.setString("ASCEND", db2.getStringNvl("ASCEND", ASCEND[1])) ;

        // 突合選択チェックボックス
        XXXXRowSet rs = db1.getRS("RS_WORK_HEAD") ;
        int len = 0 ;
        String[] check_flg = null ;
        if ((rs != null) && ((len = rs.getRowCount()) > 0)) {
            check_flg = rs.getStringArray("WLH_CHECK_FLG") ;
        }
        else {
            check_flg = new String[0] ;
        }
        db1.setCheckBox(
            "CB_CHECK_FLG",
            XXXXUtil.createStringArray("1", len),
            XXXXUtil.createStringArray(len),
            check_flg
        ) ;

        /*------------------ 画面部品の設定 ここまで -----------------------------*/

		// セッションのチェック状態をクリアする
		db1.setStringArray("_WLH_PK", null) ;
		db1.setStringArray("_CB_CHECK_FLG", null) ;

        // 2019/05/10 回収管理機能仕様変更対応 ADD START
    	db1.setString("_STOPREASON_312", "") ;
    	db1.setString("_STOPREASON_501", "") ;
    	db1.setString("_STOPREASON_000", "") ;
    	db1.setString("_STOPREASON_963", "") ;
    	db1.setString("_STOPREASON_725", "") ;
        // 2019/05/10 回収管理機能仕様変更対応 ADD END

        // トランシーケンスを設定する
        db1.setString("SEQ", getCurrSeq(db2)) ;
        setTranId(db1, "AG01");

        printData() ;
        return ;
    } // execute()

    private int accessDB() {
        int rc = 0 ;

//        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int action_ ;
        action_ = db2.getInt("action_") ;

        // 検索を行い作業テーブルへ抽出
        if ((action_ == ACT_SEARCH) || (action_ == ACT_RETURN) || (action_ == ACT_UPDATE)) {
            rc = accessDB1() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // 並び替え
        if ((action_ == ACT_SEARCH) ||
            (action_ == ACT_SORT) ||
            (action_ == ACT_RETURN) ||
            (action_ == ACT_UPDATE)) {
            rc = accessDB2() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // ページ割制御
        if ((action_ == ACT_SEARCH) ||
            (action_ == ACT_SORT) ||
            (action_ == ACT_LISTCNT) ||
            (action_ == ACT_RETURN) ||
            (action_ == ACT_UPDATE)) {
            rc = accessDB3() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // 選択フラグの更新
        if ((action_ == ACT_PAGEBREAK) ||
            (action_ == ACT_KESHIKOMI) ||
            (action_ == ACT_LISTCNT)) {
            rc = accessDB4() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // 1ページ分の取得
        if ((action_ == ACT_SEARCH) ||
            (action_ == ACT_SORT) ||
            (action_ == ACT_LISTCNT) ||
            (action_ == ACT_PAGEBREAK) ||
            (action_ == ACT_RETURN) ||
            (action_ == ACT_UPDATE)) {
            rc = accessDB5() ;
            if (rc != 0) {
                return rc ;
            }
        }

        return rc ;
    } // accessDB()
