/*
 * Project Name : 次世代WEB-AGS
 * Subsytem Name : ACM
 * 
 * Copyright 2004 (C) Askul Corp. All rights reserved.
 * 
 * Created on 2006/08/08
 */
package jp.ags.acm.ab.action;

import org.apache.struts.action.ActionErrors;

import jp.ags.acm.ab.bean.ACMKeshikomiCancelBean;
import jp.ags.acm.action.AgsActionUtility;
import jp.ags.common.AGSM;
import jp.co.askul.ASKULDataBean;
import jp.co.askul.ASKULDefaultDataBean;
import jp.co.askul.ASKULUtil;

/**
 * <strong>消込解除機能／入力画面(AB08)</strong>
 * <p>
 * 消込解除機能入力画面を表示します。
 * 
 * @author ITF桑沢景太
 * @version 1.00 2006/08/08 ITF桑沢景太
 * @since バージョン 1.00
 */
public class ACMKeshikomiCancelRequestAction extends AgsActionUtility {

    /**
     * 管理者権限を表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String CONST_ADMIN = "12";

    /**
     * 消込解除機能のセッションオブジェクトを<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_ACM_AB08 = "DB2_ACMAB_KESHIKOMICANCEL";

    /**
     * 管理者権限フラグを設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String KEY_CLCTBISFLG = "CLCTBISFLG";

    /**
     * シーケンスを設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String KEY_SEQ = "SEQ";

    /**
     * 入力欄表示行数を設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String KEY_CANCELROW = "CANCELROW";

    /**
     * 前回入力値のお問い合わせ番号を<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_TMP_USERCODE = "TMP_USERCODE";

    /**
     * 前回入力値の請求締日(年)を<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_TMP_SKYEAR = "TMP_SKYEAR";

    /**
     * 前回入力値の請求締日(月)を<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_TMP_SKMONTH = "TMP_SKMONTH";

    /**
     * 前回入力値の請求締日(日)を<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_TMP_SKDAY = "TMP_SKDAY";

    /**
     * エラー行である事を示す値を<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_TMP_ISERROR = "TMP_ISERROR";

    // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD START
    /**
     * 前回入力値の都度カード決済分を消込解除する／しないのチェック値を<br>
     * 設定／取得する為のマッピングキーを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String
            KEY_TMP_CARDRELEASE = "TMP_CARDRELEASE";
    // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD END

    /**
     * プロパティファイルから入力欄表示行数を<br>
     * 取得する為のキーを表す文字列定数
     * 
     * <pre>
     * プロパティファイル
     *   /WEB-INF/classes/ASKULResource_ja_JP.properties</pre>
     * 
     * @since バージョン 1.00
     */
    private static final String PROPERTY_CANCELROW = "F_CANCEL_ROW";

    /**
     * トランザクションID:EA912を表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String TID_EA912 = "EA912";

    /**
     * トランザクションID:AB08を表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String TID_AB08 = "AB08";

    /**
     * 解除確認ボタンで遷移時のアクションタイプを表す文字列定数
     * 
     * @since バージョン 1.00
     */
    private static final String ACTION_CONFIRM = "CONFIRM";

    /**
     * チェック処理を行います。<br>
     * 
     * <p>
     * 権限／セッションオブジェクトの正当性のチェックを行います。<br>
     * チェックに失敗した場合、不正遷移画面(AGSEA912)に遷移します。
     * 
     * @return チェック結果
     * @since バージョン 1.00
     */
    protected boolean checkInput() {
        ASKULUtil.log(ASKULUtil.DEBUG, "ACMKeshikomiCancelRequestAction#checkInput()");

        printData();

        // 画面情報を保持するオブジェクトを取得
        final ASKULDataBean db1 = getDataBean();

        // 権限フラグを取得
        final String clcTbisFlg = db1.getString(KEY_CLCTBISFLG);

        // 権限チェック
        if (!CONST_ADMIN.equals(clcTbisFlg)) {
            ASKULUtil.log(ASKULUtil.DEBUG, "権限の無い画面遷移です。");

            setTranId(db1, TID_EA912);

            return false;
        }

        // 消込解除機能のセッションオブジェクトを取得
        ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // 消込解除機能のセッションオブジェクトの正当性チェック
        if (!isSeq(db2, true)) {
            db2 = new ASKULDefaultDataBean();

            setDataBean(KEY_ACM_AB08, db2);

            newTemporaryData();
        }

        return true;
    }

    /**
     * 業務処理を行います。<br>
     * 
     * <p>
     * 消込解除機能(入力画面)の初期値を設定します。
     * 
     * @since バージョン 1.00
     */
    protected void execute() {
        ASKULUtil.log(ASKULUtil.DEBUG, "ACMKeshikomiCancelRequestAction#execute()");

        printData();

        // 画面情報を保持するオブジェクトを取得
        final ASKULDataBean db1 = getDataBean();

        // 消込解除機能のセッションオブジェクトを取得
        final ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // アクションタイプを取得
        final String actionType = db1.getStringNvl(ACMKeshikomiCancelBean.KEY_ACTION);
        
        // 入力欄表示行数をプロパティファイルより取得
        final int cancelRow = ASKULUtil.getPropertyInt(PROPERTY_CANCELROW);

        /* -------------------- 引継ぎデータを設定(db2) -------------------- */

        ActionErrors errors;

        if (ACTION_CONFIRM.equals(actionType)) {
        	errors = getError(db2);
        } else {
        	errors = new ActionErrors();
        }

        setError(db2, errors);

        
        /* -------------------- 引継ぎデータを設定(db1) -------------------- */

        String[] userCode = db2.getStringArray(KEY_TMP_USERCODE);
        String[] skYear = db2.getStringArray(KEY_TMP_SKYEAR);
        String[] skMonth = db2.getStringArray(KEY_TMP_SKMONTH);
        String[] skDay = db2.getStringArray(KEY_TMP_SKDAY);
        String[] isError = db2.getStringArray(KEY_TMP_ISERROR);
        // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD START
        String[] cardReleaseChecked;
        if (db2.getStringArray(KEY_TMP_CARDRELEASE) == null) {
        	cardReleaseChecked = ASKULUtil.createStringArray("", cancelRow);
        } else {
        	cardReleaseChecked = db2.getStringArray(KEY_TMP_CARDRELEASE);
        };
        // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD END

        setNextData(
            ASKULUtil.nvl(userCode),
            ASKULUtil.nvl(skYear),
            ASKULUtil.nvl(skMonth),
            ASKULUtil.nvl(skDay),
            ASKULUtil.nvl(cardReleaseChecked),  // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD
            ASKULUtil.nvl(isError),
            getCurrSeq(db2),
            cancelRow,
            getError(db2));

        setTranId(db1, TID_AB08);
    }

    /**
     * 引継ぎデータを一時保存します。<br>
     * 
     * <p>
     * このデータは DB2_ACMAB_KESHIKOMIRESETREQUEST を
     * キーとしたオブジェクトに保存されます。<br>
     * 意図的に削除しない限り、複数画面で使用可能です。
     *
     * @param cancelRow - 入力欄表示行数
     * @param errors - エラー
     * @since バージョン 1.00
     */
    /*private void setTemporaryData(
        final int cancelRow,
        final ActionErrors errors) {

    	ASKULUtil.log(
    	    ASKULUtil.DEBUG,
    	    "ACMKeshikomiCancelRequestAction#setTemporaryData(");

    	// 消込解除機能のセッションオブジェクトを取得
        final ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // 入力欄表示行数を設定
        db2.setInt(KEY_CANCELROW, cancelRow);

        setError(db2, errors);
    }*/

    /**
     * 一時保存データの初期化を行います。
     * 
     * @since バージョン 1.00
     */
    private void newTemporaryData() {
	    ASKULUtil.log(
	        ASKULUtil.DEBUG,
	        "ACMKeshikomiCancelRequestAction#newTemporaryData(");

	    // 消込解除機能のセッションオブジェクトを取得
        final ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // 入力欄表示行数をプロパティファイルより取得
        final int cancelRow = ASKULUtil.getPropertyInt(PROPERTY_CANCELROW);

        // 入力欄表示行数を設定
        db2.setInt(KEY_CANCELROW, cancelRow);

        // ユーザコードを設定
        db2.setStringArray(
            KEY_TMP_USERCODE,
            ASKULUtil.nvl(new String[cancelRow]));

        // 請求締日(年)を設定
        db2.setStringArray(
            KEY_TMP_SKYEAR,
            ASKULUtil.nvl(new String[cancelRow]));

        // 請求締日(月)を設定
        db2.setStringArray(
            KEY_TMP_SKMONTH,
            ASKULUtil.nvl(new String[cancelRow]));

        // 請求締日(日)を設定
        db2.setStringArray(
            KEY_TMP_SKDAY,
            ASKULUtil.nvl(new String[cancelRow]));

        // エラー行数を設定
        db2.setStringArray(
            KEY_TMP_ISERROR,
            ASKULUtil.nvl(new String[cancelRow]));

        // シーケンスを設定
        db2.setString(KEY_SEQ, getNextSeq());

        // エラーを設定
        setError(db2, new ActionErrors());
    }

    /**
     * 次画面で使用する引継ぎデータを設定します。<br>
     * 
     * <p>
     * このデータは次画面でのJSPで使用します。<br>
     * 意図的に引き継がない限り、複数画面での使用は出来ません。
     * 
     * @param userCode - お問い合わせ番号
     * @param skYear - 請求締日(年)
     * @param skMonth - 請求締日(月)
     * @param skDay - 請求締日(日)
     * @param cardReleaseChecked - 都度カード決済分を消込解除する／しないのチェック値  // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD
     * @param isError - エラー行
     * @param seq - シーケンス
     * @param cancelRow - 入力欄表行数
     * @param errors - エラー
     * @since バージョン 1.00
     */
    private void setNextData(
        final String[] userCode,
        final String[] skYear,
        final String[] skMonth,
        final String[] skDay,
        final String[] cardReleaseChecked,   // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD
        final String[] isError,
        final String seq,
    	final int cancelRow,
    	final ActionErrors errors) {

    	ASKULUtil.log(ASKULUtil.DEBUG, "ACMKeshikomiCancelRequest#setNextData()");

    	// 画面情報を保持するオブジェクトを取得
        final ASKULDataBean db1 = getDataBean();

        // お問い合わせ番号
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_USERCODE,
            userCode);

        // 請求締日(年)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_SKYEAR,
            skYear);

        // 請求締日(月)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_SKMONTH,
            skMonth);

        // 請求締日(日)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_SKDAY,
            skDay);

        // 請求締日(日)のプルダウン
        db1.setPullDown(
            ACMKeshikomiCancelBean.KEY_PUL_SKDAY,
            AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL"),
            AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_DSP"),
            skDay);

        // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD START
        // 請求締日(日)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_CARDRELEASE,
            cardReleaseChecked);
        // 2017/12/11 登録改善フェーズ2.5対応 NSSOL菅野 ADD END

        // エラー行
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_ISERROR,
            isError);

        // System.out.println("**********************************");
        // System.out.println("userCodeCount=" + userCode.length);
        // System.out.println("skYearCount=" + skYear.length);
        // System.out.println("skMonthCount=" + skMonth.length);
        // System.out.println("skDayCount=" + skDay.length);
        // System.out.println("isError=" + isError.length);
        // System.out.println("**********************************");

        for (int i=0;i<isError.length;i++) {
        	
        	// System.out.println("isError=" + isError[i]);
        }
        // System.out.println("**********************************");

        // シーケンス
        db1.setString(KEY_SEQ, seq);

        // 入力欄表示行数
        db1.setInt(ACMKeshikomiCancelBean.KEY_CANCELROW, cancelRow);

        // エラー
        setError(db1, errors);
    }
}
