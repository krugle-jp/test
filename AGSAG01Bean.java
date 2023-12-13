/*
 * Project Name  : 次世代WEB
 * Subsystem Name: 回収管理機能
 *
 *
 * Created on 2004/07/27
 */
package jp.ags.acm.ag.bean;

import jp.ags.common.AGSDefaultJSPBean;

/**
 * <strong>回収管理機能／請求入金状況一覧(AG01)</strong>
 * <p>
 *
 * @author XXXX
 * @version 1.00 2004-07-27 XXXX<br>
 *          1.01 2004-09-25 XXXX<br>
 *          1.02 2004-12-15 XXXX<br>
 * @since バージョン 1.00
 */
public class AGSAG01Bean extends AGSDefaultJSPBean {
	/**
	 * 利用者区分 統括
	 */
	private static final String MASTERCD_T   = "0" ;

	/**
	 * コンストラクタ
	 * @since 2004/09/25 XXXX<br>
	 */
    public AGSAG01Bean() {
        super() ;
//        XxUtil.log(XxUtil.DEBUG, "AGSAG01Bean#AGSAG01Bean()") ;
    } // AGSAG01Bean()

/**
 * 遷移の状態を取得
 *
 * @return boolean 初期遷移のときtrue、それ以外false
 * @since バージョン 1.00
 */
    public boolean isInit() {
        return (getPropertyInt("INIT") == 1) ;
    } // isInit()

//  2006-05-16 支払方法・入金方法条件追加 START
/**
 * 枝番のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
  //public String getPd_Agsubcd() {
  //    return getPropertyPullDown("PD_AGSUBCD") ;
  //} // getPd_Agsubcd()

    public String getPd_Agsubcd() {
        return getPropertyMultiplePullDown("PD_AGSUBCD") ;
    } // getPd_Agsubcd()
// 2006-05-16 支払方法・入金方法条件追加 END


	
/**
 * 抽出条件のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getPd_extracted() {
        return getPropertyPullDown("PD_EXTRACTED") ;
    } // getPd_extracted()

/**
 * 請求種別のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getPd_sk_seg() {
        return getPropertyPullDown("PD_SK_SEG") ;
    } // getPd_sk_seg()

/**
 * 入金種別のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getPd_nk_seg() {
        return getPropertyPullDown("PD_NK_SEG") ;
    } // getPd_nk_seg()

// 2006-05-16 支払方法・入金方法条件追加 START
/**
 * 支払方法のプルダウンを取得する
 * @return String 生成されたHtml
 * @since バージョン 2.01
 */
    public String getPd_Sk_houhou() {
        return getPropertyPullDown("PD_SK_HOUHOU") ;
    } // getSKPaySeg()

/**
 * 入金方法のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 2.01
 */
    public String getPd_Nk_houhou() {
        return getPropertyPullDown("PD_NK_HOUHOU") ;
    } // getPd_Nk_houhou()
// 2006-05-16 支払方法・入金方法条件追加 END


    
    
/**
 * 突合調整項目のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getPd_tgc() {
        return getPropertyPullDown("PD_TGC") ;
    } // getPd_tgc()

/**
 * 突合パターンの要素数を取得する
 *
 * @return int 突合パターンの要素数
 * @since バージョン 1.00
 */
    public int getRb_tg_ptn_Len() {
        return getPropertyRadioButtonLength("RB_TG_PTN") ;
    } // getRb_tg_ptn_Len()

/**
 * 突合パターンのhtmlラジオボタンを取得する
 *
 * @param  i インデックス
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getRb_tg_ptn(int i) {
        return getPropertyRadioButton("RB_TG_PTN", i) ;
    } // getRb_tg_ptn()

/**
 * 請求締日From(年)を取得する
 *
 * @return String 請求締日From(年)
 * @since バージョン 1.00
 */
    public String getSkFromYear() {
        return getPropertyString("SKFROMYEAR") ;
    } // getSkFromYear()

/**
 * 請求締日From(月)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getSkFromMonth() {
        return getPropertyString("SKFROMMONTH") ;
    } // getSkFromMonth()

/**
 * 請求締日From(日)のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
 public String getPd_SkFromDay() {
     return getPropertyPullDown("PD_SKFROMDAY") ;
 } // getPd_SkFromDay()

/**
 * 請求締日To(年)を取得する
 *
 * @return String 請求締日To(年)
 * @since バージョン 1.00
 */
    public String getSkToYear() {
        return getPropertyString("SKTOYEAR") ;
    } // getSkToYear()

/**
 * 請求締日To(月)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getSkToMonth() {
        return getPropertyString("SKTOMONTH") ;
    } // getSkToMonth()

/**
 * 請求締日To(日)のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
 public String getPd_SkToDay() {
     return getPropertyPullDown("PD_SKTODAY") ;
 } // getPd_SkToDay()

/**
 * 入金From(年)を取得する
 *
 * @return String 入金From(年)
 * @since バージョン 1.00
 */
    public String getNkFromYear() {
        return getPropertyString("NKFROMYEAR") ;
    } // getNkFromYear()

/**
 * 入金From(月)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getNkFromMonth() {
        return getPropertyString("NKFROMMONTH") ;
    } // getNkFromMonth()

/**
 * 入金From(日)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getNkFromDay() {
        return getPropertyString("NKFROMDAY") ;
    } // getNkFromDay()

/**
 * 入金To(年)を取得する
 *
 * @return String 入金To(年)
 * @since バージョン 1.00
 */
    public String getNkToYear() {
        return getPropertyString("NKTOYEAR") ;
    } // getNkToYear()

/**
 * 入金To(月)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getNkToMonth() {
        return getPropertyString("NKTOMONTH") ;
    } // getNkToMonth()

/**
 * 入金To(日)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getNkToDay() {
        return getPropertyString("NKTODAY") ;
    } // getNkToDay()

// 2004-12-15 XXXX 入金登録日追加 ST
/**
 * 入金登録From(年)を取得する
 *
 * @return String 入金From(年)
 * @since バージョン 1.01
 */
    public String getNkinFromYear() {
        return getPropertyString("NKINFROMYEAR") ;
    }

/**
 * 入金登録From(月)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.01
 */
    public String getNkinFromMonth() {
        return getPropertyString("NKINFROMMONTH") ;
    } // getNkFromMonth()

/**
 * 入金登録From(日)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.01
 */
    public String getNkinFromDay() {
        return getPropertyString("NKINFROMDAY") ;
    } // getNkinFromDay()

/**
 * 入金登録To(年)を取得する
 *
 * @return String 入金To(年)
 * @since バージョン 1.01
 */
    public String getNkinToYear() {
        return getPropertyString("NKINTOYEAR") ;
    }

/**
 * 入金登録To(月)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.01
 */
    public String getNkinToMonth() {
        return getPropertyString("NKINTOMONTH") ;
    } // getNkinToMonth

/**
 * 入金登録To(日)を取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.01
 */
    public String getNkinToDay() {
        return getPropertyString("NKINTODAY") ;
    } // getNkinToDay()
//  2004-12-15  入金登録日追加 EX

/**
 * お客様コードを取得する
 *
 * @return String お客様コード
 * @since バージョン 1.00
 */
    public String getUsercd() {
        return getPropertyString("USERCD") ;
    } // getUsercd()

/**
 * 表示件数のhtmlプルダウンを取得する
 *
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getPd_ListCnt() {
        return getPropertyPullDown("PD_LISTCNT") ;
    } // getPd_ListCnt()

/**
 * 現在の並び替え列を取得する
 *
 * @return String 並び替えの列
 * @since バージョン 1.00
 */
    public String getSort() {
        return getPropertyString("SORT") ;
    } // getSort()

/**
 * 現在の並び替え順を取得する
 *
 * @return String 並び替え順 ("asc" または "desc")
 * @since バージョン 1.00
 */
    public String getAscend() {
        return getPropertyString("ASCEND") ;
    } // getAscend()

/**
 * 請求金額合計を取得する
 *
 * @return String 請求金額合計
 * @since バージョン 1.00
 */
    public String getSk_bill_total() {
        return getPropertyString("SK_BILL_TOTAL") ;
    } // getSk_bill_total()

/**
 * 入金金額合計を取得する
 *
 * @return String 入金金額合計
 * @since バージョン 1.00
 */
    public String getNk_debit_total() {
        return getPropertyString("NK_DEBIT_TOTAL") ;
    } // getNk_debit_total()

/**
 * 突合選択チェックボックスを取得する
 *
 * @param i インデックス 
 * @return String 生成されたHtml
 * @since バージョン 1.00
 */
    public String getCb_Check_flg(int i) {
        return getPropertyListCheckBox("CB_CHECK_FLG", i) ;
    } // getCb_Check_flg()

/**
 * name属性(連番つき)を取得する
 *
 * @param nm name 
 * @param i インデックス 
 * @return String 生成されたHtmlのname属性
 * @since バージョン 1.00
 */
    public String getTagName(String nm, int i) {
        return createTagListName(nm, i) ;
    } // getTagName()
/**
 * AG01のTRAN_SEQを取得する
 *
 * @return String AG01のTRAN_SEQ
 * @since バージョン 1.00
 */
	public String getAG01TranSeq() {
		return getPropertyString("AG01_TRANSEQ") ;
	} // getAG01TranSeq()

/**
 * AG01のPNOを取得する
 *
 * @return String AG01のPNO
 * @since バージョン 1.00
 */
	public String getAG01Pno() {
		return getPropertyString("AG01_PNO") ;
	} // getAG01Pno()

/**
 * 対象枝番が参照可能かチェックする
 * @param agsubcd 
 *
 * @return boolean 判定結果
 * @since バージョン 1.00
 */
	public boolean checkAgsubcd(String agsubcd) {
		for (int i = 0 ; i < getPropertyListStringLength("CHK_AGSUBCD") ; i++){
			if (agsubcd.equals(getPropertyListString("CHK_AGSUBCD",i))){
				return true;
			}
		}
        // 枝番無しの判定
		if(agsubcd.equals("&nbsp;") || agsubcd.equals("&nbsp")) {
			if(getPropertyString("MASTERCD").equals(MASTERCD_T)) {
				return true;
			}
			else{
				// 支店で枝番なしの場合は同じものが返るのでＯＫ
				if(getPropertyString("OPE_AGSUBCD").length() == 0) {
					return true;
				}
			}
		}
        // 入金データ統括の判定
        if(agsubcd.startsWith("統")) {
            if(getPropertyString("MASTERCD").equals(MASTERCD_T)) {
                return true;
            }
        }
		return false;
	} // checkAgsubcd()
	
// 2019-05-10 回収管理機能仕様変更対応 ADD START
/**
 * 支払日From(年)を取得する
 *
 * @return String 支払日From(年)
 * @since バージョン 1.01
 */
    public String getNkpayFromYear() {
        return getPropertyString("NKPAYFROMYEAR") ;
    }

/**
 * 支払日From(月)を取得する
 *
 * @return String 支払日From(月)
 * @since バージョン 1.01
 */
    public String getNkpayFromMonth() {
        return getPropertyString("NKPAYFROMMONTH") ;
    }

/**
 * 支払日From(日)を取得する
 *
 * @return String 支払日From(日)
 * @since バージョン 1.01
 */
    public String getNkpayFromDay() {
        return getPropertyString("NKPAYFROMDAY") ;
    }

/**
 * 支払日To(年)を取得する
 *
 * @return String 支払日To(年)
 * @since バージョン 1.01
 */
    public String getNkpayToYear() {
        return getPropertyString("NKPAYTOYEAR") ;
    }

/**
 * 支払日To(月)を取得する
 *
 * @return String 支払日To(月)
 * @since バージョン 1.01
 */
    public String getNkpayToMonth() {
        return getPropertyString("NKPAYTOMONTH") ;
    }

/**
 * 支払日To(日)を取得する
 *
 * @return String 支払日To(日)
 * @since バージョン 1.01
 */
    public String getNkpayToDay() {
        return getPropertyString("NKPAYTODAY") ;
    }

/**
 * 受注停止有無のプルダウンを取得する
 * @return String 生成されたHtml
 * @since バージョン 2.01
 */
    public String getPd_resstp() {
        return getPropertyPullDown("PD_RESSTP") ;
    }

/**
 * 受注停止理由(債権回収中)の取得する
 * 
 * @return String 検索タイプ
 * @since バージョン 4.00(SynchroAgentIT2.0対応)	 
 */
    public String getStopreason_312_Chk() {
		String value = getPropertyString("STOPREASON_312");
		if(value.equals("312") && value.length() != 0){
			return "checked";
		}else{
			return "";
		}
    }

/**
 * 受注停止理由(債権回収不能)の取得する
 * 
 * @return String 検索タイプ
 * @since バージョン 4.00(SynchroAgentIT2.0対応)	 
 */
    public String getStopreason_501_Chk() {
		String value = getPropertyString("STOPREASON_501");
		if(value.equals("501") && value.length() != 0){
			return "checked";
		}else{
			return "";
		}
    }

/**
 * 受注停止理由(左記以外全て)の取得する
 * 
 * @return String 検索タイプ
 * @since バージョン 4.00(SynchroAgentIT2.0対応)	 
 */
    public String getStopreason_000_Chk() {
		String value = getPropertyString("STOPREASON_000");
		if(value.equals("000") && value.length() != 0){
			return "checked";
		}else{
			return "";
		}
    }

/**
 * 受注停止理由(AG依頼その他)の取得する
 * 
 * @return String 検索タイプ
 * @since バージョン 4.00(SynchroAgentIT2.0対応)	 
 */
    public String getStopreason_963_Chk() {
		String value = getPropertyString("STOPREASON_963");
		if(value.equals("963") && value.length() != 0){
			return "checked";
		}else{
			return "";
		}
    }

/**
 * 受注停止理由(AG依頼脱会)の取得する
 * 
 * @return String 検索タイプ
 * @since バージョン 4.00(SynchroAgentIT2.0対応)	 
 */
    public String getStopreason_725_Chk() {
		String value = getPropertyString("STOPREASON_725");
		if(value.equals("725") && value.length() != 0){
			return "checked";
		}else{
			return "";
		}
    }
// 2019-05-10 回収管理機能仕様変更対応 ADD END

}



