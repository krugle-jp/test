/*
 * Project Name : 次世代WEB-AGS
 * Subsystem Name :
 *
 * Copyright 2004 (C) Askul Corp. All rights reserved.
 * 
 * Created on 2004/07/27
 */
package jp.ags.acm.ad.action;

import jp.ags.acm.common.AGSPDFAccess;
import jp.ags.acm.common.AGSQ;
import jp.ags.acm.action.AgsActionUtility;
import jp.ags.common.AGSM;
import jp.ags.acm.common.AGSUtil;
import jp.co.askul.ASKULDataBean;
import jp.co.askul.ASKULDefaultDataBean;
import jp.co.askul.ASKULDefaultRowSet;
import jp.co.askul.ASKULRowSet;
import jp.co.askul.ASKULUtil;
import jp.co.askul.db.ASKULDBConnector;
import jp.co.askul.db.ASKULDBParam;
/**
 * <strong>[tran]リスト出力／未突合請求入金データ対比表PDF出力画面(AG15)</strong>
 * <p>
 *
 * @author ITF金子和正
 * @version 00.00 2004/07/27 T.Nunozawa Oiginal<br>
 *          01.00 2004/09/17 K.Kaneko<br>
 * @since バージョン 1.00
 */
public class AGSAD15Action extends AgsActionUtility {
    private static final int PRM_LEN = 3;
    private static final String RS_HEADER = "sknk_hashtable";
    private static final String RS_DETAIL_S = "sk_record";
    private static final String RS_DETAIL_N = "nk_record";
    private static final String DB_HEADER = "H_TYP";
    private static final String DB_DETAIL = "R_TYP";
    private static final String TITLE = "未突合請求入金データ対比表";
    private static final String SK_BILLSEG_NAME = ASKULUtil.getProperty("F_SK_BILLSEG_NAME");
    private static final String NK_PAYSEG_NAME  = ASKULUtil.getProperty("F_NK_PAYSEG_NAME");
    private static final String NK_DATAKBN_NAME = ASKULUtil.getProperty("F_NK_DATAKBN_NAME");
    private static String[] CNM_TAIHI_HEADER = {
        "TITLE",                                // 帳票名称(ht)
        "SK_CHECKDATE_FROM",                    // 検索条件：請求締日ＦＲＯＭ(ht)
        "SK_CHECKDATE_TO",                      // 検索条件：請求締日ＴＯ(ht)
        "SK_PAY_DT_FROM",                       // 検索条件：支払予定日ＦＲＯＭ(ht)
        "SK_PAY_DT_TO",                         // 検索条件：支払予定日ＴＯ(ht)
        "NK_TRANSFER_DT_FROM",                  // 検索条件：入金日ＦＲＯＭ(ht)
        "NK_TRANSFER_DT_TO",                    // 検索条件：入金日ＴＯ(ht)
        "CR_DT",                                // 作成日(ht)
        "NK_PAYSEG_NAME",                       // 入金種別略称補足(ht)
        "NK_DATAKBN_NAME",                      // 入金経路略称補足(ht)
        "SK_BILLSEG_NAME",                      // 請求種別略称補足(ht)
    };

/**
 * 入力チェックを行なう.
 *
 * @return boolean チェックＯＫのときtrue、ＮＧのときfalse
 * @since バージョン 1.00
 */
    protected boolean checkInput() {
        ASKULUtil.log(ASKULUtil.DEBUG, "AGSAD15Tran#checkInput()");
        printData();
        ASKULDataBean db1 = getDataBean();
        ASKULDataBean db2 = getDataBean("DB2_AD14");

        // ★★★DB2の正当性をチェックする★★★
        if (!isSeq(db2, true)) {
            ASKULUtil.log(ASKULUtil.DEBUG, "isSeq()=false", db2);
            removeDataBean("DB2_AD14");
            setTranId(db1, "AD14");
            return false;
        }

        printData();
        return true;
    } // checkInput()

/**
 * 業務処理を行なう.
 *
 * @since バージョン 1.00
 */
    protected void execute() {
        ASKULUtil.log(ASKULUtil.DEBUG, "AGSAD15Tran#execute()");
        printData();
        ASKULDataBean db1 = getDataBean();
        ASKULDataBean db2 = getDataBean("DB2_AD14");

        int rc = accessDB();
        if (rc < 0) {
            logError("AGSAD15Tran#システムエラー");
            setTranId(db1, "EA999");
            return ;
        }


        //------------------ PDF生成クラス呼び出し --------------------------------//
        try {
            // インターフェス用DataBean
            ASKULDataBean db2_pdf = new ASKULDefaultDataBean();
            ASKULDataBean dbh     = new ASKULDefaultDataBean();
            ASKULDataBean dbr     = new ASKULDefaultDataBean();
            ASKULDataBean dbhid   = new ASKULDefaultDataBean();
            ASKULDataBean dbrid   = new ASKULDefaultDataBean();

            // ------ ヘッダ用RowSetの作成
            // 帳票名称
            String title = TITLE;
            // 請求締日期間
            String tx_sk_checkdate_dt_y_f = db2.getString("TX_SK_CHECKDATE_DT_Y_F");
            String pd_sk_checkdate_dt_m_f = db2.getString("PD_SK_CHECKDATE_DT_M_F");
            String pd_sk_checkdate_dt_d_f = db2.getString("PD_SK_CHECKDATE_DT_D_F");
            String tx_sk_checkdate_dt_y_t = db2.getString("TX_SK_CHECKDATE_DT_Y_T");
            String pd_sk_checkdate_dt_m_t = db2.getString("PD_SK_CHECKDATE_DT_M_T");
            String pd_sk_checkdate_dt_d_t = db2.getString("PD_SK_CHECKDATE_DT_D_T");
            String sk_dt_from = AGSUtil.formatDateSP(tx_sk_checkdate_dt_y_f, pd_sk_checkdate_dt_m_f, pd_sk_checkdate_dt_d_f, ASKULUtil.YYYYMMDD);
            String sk_dt_to = AGSUtil.formatDateSP(tx_sk_checkdate_dt_y_t, pd_sk_checkdate_dt_m_t, pd_sk_checkdate_dt_d_t, ASKULUtil.YYYYMMDD);
            // 入金日期間
            String nk_dt_from = db2.getString("NK_TRANSFER_DT_FROM");
            String nk_dt_to = db2.getString("NK_TRANSFER_DT_TO");
            nk_dt_from = ASKULUtil.formatDate(nk_dt_from, ASKULUtil.YYYY_MM_DD);
            nk_dt_to = ASKULUtil.formatDate(nk_dt_to, ASKULUtil.YYYY_MM_DD);
            // 作成日
            String cr_dt = ASKULUtil.formatDate(ASKULUtil.YYYY_MM_DD);

            // ヘッダ用RowSet
            String[][] s2a = new String[][] {
                {
                    title,
                    sk_dt_from,
                    sk_dt_to,
                    "",         // ★ 未実装 画面項目無し
                    "",         // ★ 未実装 画面項目無し
                    nk_dt_from,
                    nk_dt_to,
                    cr_dt,
                    NK_PAYSEG_NAME,
                    NK_DATAKBN_NAME,
                    SK_BILLSEG_NAME,
                }
            };
            ASKULRowSet rs = new ASKULDefaultRowSet(CNM_TAIHI_HEADER, s2a);
            dbh.setRS(RS_HEADER, rs);
            dbhid.setInt(RS_HEADER, AGSPDFAccess.TAIHI_LIST_H);

            // レコード用RowSet
            dbr.setRS(RS_DETAIL_S, db2.getRS("RS_DETAIL_S"));
            dbrid.setInt(RS_DETAIL_S, AGSPDFAccess.TAIHI_LIST_R1);
            dbr.setRS(RS_DETAIL_N, db2.getRS("RS_DETAIL_N"));
            dbrid.setInt(RS_DETAIL_N, AGSPDFAccess.TAIHI_LIST_R2);

            // ヘッダ用DataBeanとレコード用DataBeanを設定
            db2_pdf.setObject(DB_HEADER, dbh);
            db2_pdf.setObject(DB_DETAIL, dbr);
            db2_pdf.setObject(DB_HEADER + "_ID", dbhid);
            db2_pdf.setObject(DB_DETAIL + "_ID", dbrid);

            // PDFクラス呼び出し
            rc = AGSPDFAccess.accessPDF(AGSPDFAccess.PDF_TAIHI_LIST, db2_pdf);
            if (rc != 0) {
                logError("AGSAD15Tran#システムエラー");
                setTranId(db1, "EA999");
                return ;
            }

            // PDFの設定
            setPDF(db2_pdf, title);
        }
        catch (Exception e) {
            logException("AGSAD15Tran#execute()#Exception", e);
        }
        finally {
        }
        //------------------ PDF生成クラス呼び出し ここまで -----------------------//

        printData();
        return ;
    } // execute()

    private int accessDB() {
        ASKULUtil.log(ASKULUtil.DEBUG, "AGSAD15Tran#accessDB()");
//        ASKULDataBean db1 = getDataBean();
        ASKULDataBean db2 = getDataBean("DB2_AD14");

        int rc = -2; // Error
        try {
            ASKULDBConnector conn = connect();

            int cnt          = 0;
            String sql       = null;
            ASKULDBParam prm = null;
            ASKULRowSet rs   = null;

            /*------------------ 未突合入金 START --------------------------------------*/
            String bankkana = "";
            String subkana  = "";
            String bacsegnm = "";
            String bacno    = "";
            String[] kouza = ASKULUtil.divideToken(db2.getString("PD_NK_KOUZA"));
            if ((kouza != null) && (kouza.length == PRM_LEN)) {
                prm = getDBParam();
                prm.set("AGCD", kouza[0]);
                prm.set("AGSUBCD", kouza[1]);
                prm.set("AGACSUBNO", kouza[2]);
                rs = AGSQ.getRS(AGSQ.RS_AGBANK, conn, prm);
                if (rs.getRowCount() == 1) {
                    bankkana = AGSUtil.toMultiKana(rs.getString("BANKKANA", 0));
                    subkana = AGSUtil.toMultiKana(rs.getString("SUBKANA", 0));
                    bacsegnm = rs.getString("BACSEGNM", 0);
                    bacno = rs.getString("BACNO", 0);
                }
            }
            String dt_from = db2.getString("NK_TRANSFER_DT_FROM");
            String dt_to = db2.getString("NK_TRANSFER_DT_TO");
            dt_from = ASKULUtil.formatDate(dt_from, AGSUtil.YYYY_MM_DD);
            dt_to = ASKULUtil.formatDate(dt_to, AGSUtil.YYYY_MM_DD);

//            sql = "AGSAD_TaihiNyukinPdfListS";
//            prm = getDBParam();

			// 2004/09/25 統括支店対応 ST ITF岩佐
			prm = getDBParam();
			String agsubcd = db2.getStringNvl("PD_NK_AGSUBCD");
			
			if(agsubcd.equals(AGSUBCD_ALL_VAL)){
				sql = "AGSAD_TaihiNyukinPdfListSubcdAllS";
				prm.set("AGSUBCD", getLoginAgsubcdList(), true) ;
			}
			else{
				sql = "AGSAD_TaihiNyukinPdfListS";
				prm.set("AGSUBCD", agsubcd);
			}
			// 2004/09/25 統括支店対応 EX ITF岩佐

            prm.set("AGCD", getLoginAgcd());
//            prm.set("AGSUBCD", getLoginAgsubcd());
            prm.set("NK_PAYKBN", db2.getString("PD_NK_PAYKBN"));
            prm.set("NK_TRANSFER_DT_F", dt_from);
            prm.set("NK_TRANSFER_DT_T", dt_to);
            prm.set("BANKKANA", bankkana);
            prm.set("SUBKANA", subkana);
            prm.set("BACSEG", bacsegnm);
            prm.set("BACNO", bacno);
            String key = "OB";
            String[] nk_order_cd = AGSM.getStringArray(AGSM.RS_NK_ORDER, "NK_ORDER_CD");
            cnt = nk_order_cd.length;
            for (int i = 0; i < cnt; i++) {
                prm.set(key + nk_order_cd[i], "");
            }
            prm.set(key + db2.getString("RB_NK_ORDER"), " ");
            rs = executeQuery(sql, prm);
            cnt = rs.getRowCount();
            db2.setRS("RS_DETAIL_N", rs);
            /*------------------ 未突合入金 END ----------------------------------------*/

            /*------------------ 未突合請求 START --------------------------------------*/
            String bankcd    = "";
            String banksubcd = "";
            String bacseg    = "";
            bacno     = "";
            kouza = ASKULUtil.divideToken(db2.getString("PD_SK_KOUZA"));
            if ((kouza != null) && (kouza.length == PRM_LEN)) {
                prm = getDBParam();
                prm.set("AGCD", kouza[0]);
                prm.set("AGSUBCD", kouza[1]);
                prm.set("AGACSUBNO", kouza[2]);
                rs = AGSQ.getRS(AGSQ.RS_AGBANK, conn, prm);
                if (rs.getRowCount() == 1) {
                    bankcd = rs.getString("BANKCD", 0);
                    banksubcd = rs.getString("BANKSUBCD", 0);
                    bacseg = rs.getString("BACSEG", 0);
                    bacno = rs.getString("BACNO", 0);
                }
            }


            String dt_y_f = db2.getString("TX_SK_CHECKDATE_DT_Y_F");
            String dt_m_f = db2.getString("PD_SK_CHECKDATE_DT_M_F");
            String dt_d_f = db2.getString("PD_SK_CHECKDATE_DT_D_F");
            String dt_y_t = db2.getString("TX_SK_CHECKDATE_DT_Y_T");
            String dt_m_t = db2.getString("PD_SK_CHECKDATE_DT_M_T");
            String dt_d_t = db2.getString("PD_SK_CHECKDATE_DT_D_T");
            dt_from = AGSUtil.formatDateSP(dt_y_f, dt_m_f, dt_d_f, AGSUtil.YYYY_MM_DD);
            dt_to   = AGSUtil.formatDateSP(dt_y_t, dt_m_t, dt_d_t, AGSUtil.YYYY_MM_DD);

            String pd_agsubcd = db2.getString("PD_SK_AGSUBCD");
            String[] agsubcdList = null;
            if (pd_agsubcd.equals(AGSUBCD_ALL_VAL)) {
                agsubcdList = getLoginAgsubcdList();
            }
            else {
                agsubcdList = new String[] {pd_agsubcd};
            }

            sql = "AGSAD_TaihiSeikyuPdfListS";
            prm = getDBParam();
            prm.set("AGCD", getLoginAgcd());
            prm.set("AGSUBCD", agsubcdList, true);
            prm.set("SK_PAYSEGNO", db2.getString("PD_SK_PAYSEGNO"));
            prm.set("SK_CHECKDATE_DT_F", dt_from);
            prm.set("SK_CHECKDATE_DT_T", dt_to);
            prm.set("SK_BANKCD", bankcd);
            prm.set("SK_BANKBRANCH", banksubcd);
            prm.set("SK_BACSEGNO", bacseg);
            prm.set("SK_BACNO", bacno);
            key = "OB";
            String[] sk_order_cd = AGSM.getStringArray(AGSM.RS_SK_ORDER, "SK_ORDER_CD");
            cnt = sk_order_cd.length;
            for (int i = 0; i < cnt; i++) {
                prm.set(key + sk_order_cd[i], "");
            }
            prm.set(key + db2.getString("RB_SK_ORDER"), " ");
            rs = executeQuery(sql, prm);
            db2.setRS("RS_DETAIL_S", rs);
            /*------------------ 未突合請求 END ----------------------------------------*/

            commit();
            rc = 0;
        }
        catch (Exception e) {
            logException("AGSAD15Tran#accessDB()#Exception", e);
            rollback();
            rc = -1;
        }
        finally {
            close();
        }
        return rc;
    } // accessDB()
}

