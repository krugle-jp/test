<%--
       2022/07/19 NI+C XXXX
--%>
<%@ page contentType="text/html; charset=Windows-31J" pageEncoding="Windows-31J" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/tags/ags" prefix="ags" %>
<jsp:useBean id="AGSAG01" class="jp.ags.acm.ag.bean.AGSAG01Bean" scope="session"/>
<jsp:useBean id="userInfo" type="jp.co.xxxxx.ags.common.app.UserInfoBean" scope="session"/>
<%@ page import="jp.co.xxxxx.*" %>
<%!
private 	static final int TYP_D = XXXXRowSet.TYP_D ;
private 	static final int TYP_M = XXXXRowSet.TYP_M ;
private 	String[] PTN_D = {XXXXUtil.YYYY_MM_DD} ;
private 	String[] PTN_M = {"\\"} ;
private 	String PTN_M2 = "&yen;" ;
private 	static final int COMMENT_MAX_LEN = 5 ;
private 	static final String FUGO_MINUS = "2" ;
private	static final String INPUT_SIZE4 = "35px";
private	static final String INPUT_SIZE2 = "25px";
%>
<%
	AGSAG01.setHeaderInfo(request, response) ;

	String sort = AGSAG01.getSort() ;
	String ascend = AGSAG01.getAscend() ;
	String ascendChar = null ;
	if (ascend.equals("ASC")) {
		ascendChar = "昇順" ;
	} else {
		ascendChar = "降順" ;
	}

	// 改ページ制御変数
	int currentPageNo = AGSAG01.getListCurrentPageNo() ;
	int firstPageNo = AGSAG01.getListFirstPageNo() ;
	int lastPageNo = AGSAG01.getListLastPageNo() ;
	int totalCnt = AGSAG01.getListTotalRowCount() ;
//	int currentRowCount = AGSAG01.getListRowCount() ;
	int previousPageNo = 0 ;
	if (firstPageNo < currentPageNo) {
		previousPageNo = currentPageNo - 1 ;
	}
	Math.max(0, currentPageNo - 1) ;
	int nextPageNo = 0 ;
	if (currentPageNo < lastPageNo) {
		nextPageNo = currentPageNo + 1 ;
	}

	// 2019/05/10 回収管理機能仕様変更対応 ADD START
	String s312 = AGSAG01.getStopreason_312_Chk();
	String s501 = AGSAG01.getStopreason_501_Chk();
	String s000 = AGSAG01.getStopreason_000_Chk();
	String s963 = AGSAG01.getStopreason_963_Chk();
	String s725 = AGSAG01.getStopreason_725_Chk();
	// 2019/05/10 回収管理機能仕様変更対応 ADD END

	// 一覧生成用の変数
	int skLen = 0 ; // 請求側の行数
	int nkLen = 0 ; // 入金側の行数
	int maxLen = 0 ; // max値(請求側の行数、入金側の行数)
	int skIndex = 0 ;
	int nkIndex = 0 ;
	int head_cnt = 0 ;
	String pk = null ;
	String pkType = null ;
	String tg_ptn = null ;
	String tgCheck = null ;
	String tgPtn = null ;
	String comment = null ;
	String tempComment = null ;
	String style = null ;
	// NOキャッシュの設定
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html lang="ja">
<head>
<meta http-equiv="content-type" content="text/html;charset=shift_jis" />
<meta http-equiv="content-style-type" content="text/css" />
<meta http-equiv="content-script-type" content="text/javascript" />
<title>回収管理業務／請求入金状況一覧</title>
<link rel="stylesheet" href="../contents/css/style.css" type="text/css" />
<script src="contents/script/script_base.js"></script>
<script language="javascript">
<!--
var currSort = '<%=sort%>';
var currAscend = '<%=ascend%>' ;

/**
 * ソート列の指定
 *
 * パラメータ:    sort ソート列
 * @since バージョン 1.00
 */
function setSort(sort) {
	var ascend = 'DESC' ;
	if (currSort == sort) {
		if (currAscend == 'DESC') {
			ascend = 'ASC' ;
		}
	}
	document._FORM1._SORT.value = sort ;
	document._FORM1._ASCEND.value = ascend ;
	return ;
} // setSort()

/**
 * ソート列の設定
 *
 * パラメータ:    sort ソート列
 * @since バージョン 1.00
 */
function setPno(pno) {
	document._FORM1._PNO.value = pno ;
} // setPno()

/**
 * シーケンスの設定
 *
 * パラメータ:    ptn シーケンスパターン
 * パラメータ:    seq シーケンス値
 * @since バージョン 1.00
 */
function setSeq(ptn, seq) {
	document._FORM1._TG_SEQ.value = '' ;
	document._FORM1._SK_SEQ.value = '' ;
	document._FORM1._NK_SEQ.value = '' ;
	switch (ptn) {
	case 0:
		document._FORM1._TG_SEQ.value = seq ;
		break ;
	case 1:
		document._FORM1._SK_SEQ.value = seq ;
		break ;
	case 2:
		document._FORM1._NK_SEQ.value = seq ;
		break ;
	}
} // setSeq()

/**
 * 検索条件のクリア
 * @since バージョン 1.00
 */
function clickReset(form) {
	document.forms[form]._PD_AGSUBCD.selectedIndex = "" ;
	document.forms[form]._PD_EXTRACTED.selectedIndex = "" ;
	document.forms[form]._PD_SK_SEG.selectedIndex = "" ;
	document.forms[form]._PD_NK_SEG.selectedIndex = "" ;
	document.forms[form]._PD_SK_HOUHOU.selectedIndex = "" ;
	document.forms[form]._PD_NK_HOUHOU.selectedIndex = "" ;
	document.forms[form]._PD_TGC.selectedIndex = "" ;
	document.forms[form]._RB_TG_PTN[0].checked = true ;
	document.forms[form]._SKFROMYEAR.value = "" ;
	document.forms[form]._SKFROMMONTH.value = "" ;
	document.forms[form]._PD_SKFROMDAY.selectedIndex = "" ;
	document.forms[form]._SKTOYEAR.value = "" ;
	document.forms[form]._SKTOMONTH.value = "" ;
	document.forms[form]._PD_SKTODAY.selectedIndex = "" ;
	document.forms[form]._NKFROMYEAR.value = "" ;
	document.forms[form]._NKFROMMONTH.value = "" ;
	document.forms[form]._NKFROMDAY.value = "" ;
	document.forms[form]._NKTOYEAR.value = "" ;
	document.forms[form]._NKTOMONTH.value = "" ;
	document.forms[form]._NKTODAY.value = "" ;
	document.forms[form]._NKINFROMYEAR.value = "" ;
	document.forms[form]._NKINFROMMONTH.value = "" ;
	document.forms[form]._NKINFROMDAY.value = "" ;
	document.forms[form]._NKINTOYEAR.value = "" ;
	document.forms[form]._NKINTOMONTH.value = "" ;
	document.forms[form]._NKINTODAY.value = "" ;
	document.forms[form]._USERCD.value = "" ;
	agsubselcheck();	// 枝番選択状況表示変更
	// 2019/05/10 回収管理機能仕様変更対応 ADD START
	document.forms[form]._NKPAYFROMYEAR.value = "" ;
	document.forms[form]._NKPAYFROMMONTH.value = "" ;
	document.forms[form]._NKPAYFROMDAY.value = "" ;
	document.forms[form]._NKPAYTOYEAR.value = "" ;
	document.forms[form]._NKPAYTOMONTH.value = "" ;
	document.forms[form]._NKPAYTODAY.value = "" ;
	document.forms[form]._PD_RESSTP.selectedIndex = "" ;
	document.forms[form]._STOPREASON_312.checked = false ;
	document.forms[form]._STOPREASON_501.checked = false ;
	document.forms[form]._STOPREASON_000.checked = false ;
	document.forms[form]._STOPREASON_963.checked = false ;
	document.forms[form]._STOPREASON_725.checked = false ;
	document.forms[form]._STOPREASON_312.disabled = true ;
	document.forms[form]._STOPREASON_501.disabled = true ;
	document.forms[form]._STOPREASON_000.disabled = true ;
	document.forms[form]._STOPREASON_963.disabled = true ;
	document.forms[form]._STOPREASON_725.disabled = true ;
	// 2019/05/10 回収管理機能仕様変更対応 ADD END
} // clickReset()

/**
 * 実行確認
 * @since バージョン 1.00
 */
function execConfirm(msg, frmName, action, id, path) {
	if(confirm(msg)) {
		forward(frmName, action, id, path);
	}
} // execConfirm()

/**
 * 実行確認（自動突合ボタン）
 * @since バージョン 1.00
 */
function execConfirmWait(msg, frmName, action, id, path) {
	if(isReadyState()) {  //2004/11/16 連打防止
		if(confirm(msg)) {
			waitOpen();
			forward(frmName, action, id, path);
		}
	}
} // execConfirm()

/**
 * 検索ボタン押下時のボタン非活性化
 *
 * パラメータ:    formName      FORM名
 * パラメータ:    actionType    Action
 * パラメータ:    transactionId TID
 * パラメータ:    actionPath    Path
 * @since バージョン 1.00
 */
function disforward(formName, actionType, transactionId, actionPath) {
	document.forms[formName].SEARCH.disabled = true;
	if (isReadyState()) { // 2004/10/20 XXXX
		document.forms[formName].action = actionPath;
		callPage(formName, actionType, transactionId);
	}
} // forward


var waitWindow;

/**
 * 検索ボタン押下時のボタン非活性化
 *
 * パラメータ:    formName      FORM名
 * パラメータ:    actionType    Action
 * パラメータ:    transactionId TID
 * パラメータ:    actionPath    Path
 * @since バージョン 1.00
 */
function waitOpen() {

	w = 400;
	h = 150;
	x = (screen.width - w) / 2;
	y = (screen.height - h) / 2;
	opt = "scrollbars=no, toolbar=no, resizable=no,"+
		  "width="+w+", height="+h+"," +
		  "screenX="+x+",screenY="+y+",left="+x+",top="+y;
	var redirectUrl = "AGSWAITAction.do";

	waitWindow = window.open(redirectUrl,"wait",opt);

}

/**
 * お待ちくださいの子画面を削除
 *
 * @since バージョン 1.00
 */
function subWindowClose() {
	if (waitWindow != null){
		waitWindow.close();
	}
}

/**
 * 画面遷移
 *
 * パラメータ:    formName       submitするフォーム名
 * パラメータ:    target ターゲットフレーム
 * @since バージョン 1.00
 */
function helpMove01() {

  var option = "menubar=no,"+
               "scrollbars=no,"+
               "toolbar=no,"+
               "location=no,"+
               "status=no,"+
               "top=0,"+
               "left=0,"+
               "width=855,"+
               "height=680,"+
               "resizable=no";

  window.open("AGSHELP01.jsp", "", option);

}
/* 2019-05-10 回収管理機能仕様変更対応 ADD START */
function helpMove02() {

  var option = "menubar=no,"+
               "scrollbars=no,"+
               "toolbar=no,"+
               "location=no,"+
               "status=no,"+
               "top=0,"+
               "left=0,"+
               "width=300,"+
               "height=130,"+
               "resizable=no";

  window.open("AGSHELP02.jsp", "", option);

}

var s312_chk = '<%=s312%>';
var s501_chk = '<%=s501%>';
var s000_chk = '<%=s000%>';
var s963_chk = '<%=s963%>';
var s725_chk = '<%=s725%>';

//受注停止有無が「有」の場合受注停止理由を活性化、理由は全てチェックなし
function changeStopreason(){
	var elm = document.getElementsByName('_PD_RESSTP');
	for(var i=0; i<elm.length; i++){
		if(elm[i].tagName == 'SELECT'){
			for(var j=0; j<elm[i].options.length; j++) {
				if (elm[i].options[j].selected) {
					if(elm[i].options[j].value == '1'){
						document._FORM1._STOPREASON_312.disabled = false ;
						document._FORM1._STOPREASON_501.disabled = false ;
						document._FORM1._STOPREASON_000.disabled = false ;
						document._FORM1._STOPREASON_963.disabled = false ;
						document._FORM1._STOPREASON_725.disabled = false ;
						//検索後に画面選択値をクリアしないため
						if (s312_chk == "") { document._FORM1._STOPREASON_312.checked = false ; }
						if (s501_chk == "") { document._FORM1._STOPREASON_501.checked = false ; }
						if (s000_chk == "") { document._FORM1._STOPREASON_000.checked = false ; }
						if (s963_chk == "") { document._FORM1._STOPREASON_963.checked = false ; }
						if (s725_chk == "") { document._FORM1._STOPREASON_725.checked = false ; }
					}else{
						document._FORM1._STOPREASON_312.disabled = true ;
						document._FORM1._STOPREASON_501.disabled = true ;
						document._FORM1._STOPREASON_000.disabled = true ;
						document._FORM1._STOPREASON_963.disabled = true ;
						document._FORM1._STOPREASON_725.disabled = true ;
						document._FORM1._STOPREASON_312.checked = false ;
						document._FORM1._STOPREASON_501.checked = false ;
						document._FORM1._STOPREASON_000.checked = false ;
						document._FORM1._STOPREASON_963.checked = false ;
						document._FORM1._STOPREASON_725.checked = false ;
					}
				}
			}
		}
	}
}
//受注停止有無のonChangeイベント処理
function checkResstpEv(){
	var elm = document.getElementsByName('_PD_RESSTP');
	for(var i=0; i<elm.length; i++){
		if(elm[i].tagName == 'SELECT'){
			if (elm[i].options.length>0) {
				if (elm[i].addEventListener) {
					elm[i].addEventListener("change", changeStopreason, false);
				} else {
					// IE動作確認用コード
					elm[i].attachEvent("onchange", changeStopreason, false);
				}
			}
			break;
		}
	}
}
/* 2019-05-10 回収管理機能仕様変更対応 ADD END */

//-->
</script>
<style type="text/css">
<!-- .bluefont { color:blue } -->

  /*   Chrome画面崩れ対応のため追加 */
  input[type="radio"], [type="checkbox"] {
    margin-right: 3px;
    top: 2px;
    position: relative;
  }
</style>

<!-- 2017/04/13 XXXX GoogleAnalytics対応 ADD START -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-88636562-1', 'auto');
  ga('send', 'pageview');

</script>
<!-- 2017/04/13 XXXX GoogleAnalytics対応 ADD END -->

</head>

<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0" rightmargin="0" onpagehide="subWindowClose();">

<!-- HEADER START -->
<c:import url="include/header.jsp"/>
<c:import url="../ags/include/newusercall.jsp"/>
<c:import url="../imn/include/callpage.jsp"/>
<!-- HEADER END -->

<!-- コンテンツ -->
<!-- コンテンツ -->
<div id="category-navigation">
<form name="_FORM0" action="" method="post">
	<input type="hidden" name="_P" value="AG01">
	<input type="hidden" name="_T" value="">
	<input type="hidden" name="_ACTION" value="">
	<input type="hidden" name="_SEQ" value="<%=AGSAG01.getTranSeq()%>">
	<input type="hidden" name="_SORT" value="<%=sort%>">
	<input type="hidden" name="_ASCEND" value="<%=ascend%>">
	<input type="hidden" name="_PNO" value="<%=currentPageNo%>">
	<input type="hidden" name="_TG_SEQ" value="">
	<input type="hidden" name="_SK_SEQ" value="">
	<input type="hidden" name="_NK_SEQ" value="">
	<div class="subnavi">
	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AG13', 'AGSAG13Action.do'); return false;">入金データアップロード</a>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AG17', 'AGSAG17Action.do'); return false;">入金手動登録</a>
			<td><a href="javascript:void(0);" onClick="execConfirmWait('突合を実行します。よろしいですか？','_FORM1', '', 'AG18', 'AGSAG18Action.do'); return false;">自動突合</a>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AJ01', 'AGSAJ01Action.do'); return false;">手動突合</a>
			<td><a href="javascript:void(0);" onClick="execConfirm('消込を実行します。よろしいですか？','_FORM1', 'KESHIKOMI', 'AG01', 'AGSAG01Action.do'); return false;">消込</a>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AM01', 'AGSAM01Action.do'); return false;">入金データ一括削除</a><%-- 2018/06/12 回収管理改善対応 XXXX ADD --%>
		</tr>
	</table>
	</div>
	<div class="bottom">&nbsp;</div>
</form>
</div>

<!--==========ContentBody==========-->
<div id="content-body">
<form name="_FORM1" action="" method="post">
	<input type="hidden" name="_P" value="AG01">
	<input type="hidden" name="_T" value="">
	<input type="hidden" name="_ACTION" value="">
	<input type="hidden" name="_SEQ" value="<%=AGSAG01.getTranSeq()%>">
	<input type="hidden" name="_SORT" value="<%=sort%>">
	<input type="hidden" name="_ASCEND" value="<%=ascend%>">
	<input type="hidden" name="_PNO" value="<%=currentPageNo%>">
	<input type="hidden" name="_TG_SEQ" value="">
	<input type="hidden" name="_SK_SEQ" value="">
	<input type="hidden" name="_NK_SEQ" value="">
	<div class="contents">
		<h2>請求入金状況一覧</h2>
		<table cellspacing="0">
			<tr>
				<td>
				<table cellspacing="0" style="width:100%">
					<tr>
						<td width="50%" style="height:100%">
							<table cellspacing="0" style="width:100%" style="height:100%">		<%-- 2019/05/10 回収管理機能仕様変更対応 CHG --%>
								<tr>
									<td colspan="2" class="lightgray" style="width:18%">状態</td>
									<td colspan="5" class="white_left"><%=AGSAG01.getPd_extracted()%></td>
									<td rowspan="2" class="lightgray"><span id="AGSUBTITLE">枝番</span></td><%-- 2018/06/12 回収管理改善対応 XXXX CHG --%>
									<%-- 2019/05/10 回収管理機能仕様変更対応 CHG START --%>
									<td rowspan="2" colspan="3" class="white_left"><%=AGSAG01.getPd_Agsubcd()%>
										<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<a href="#" onClick="javaScript:helpMove02();" return false;><img src="../contents/img/common/question.gif" border="0" width="18" height="18" alt="枝番選択" /></a>
									</td>
									<%-- 2019/05/10 回収管理機能仕様変更対応 CHG END --%>
								</tr>
								<tr>
									<td colspan="2" class="lightgray" style="width:18%">お客様コード</td>
									<%-- 2017/02/15 登録改善対応 CHG START--%>
									<td colspan="5" class="white_left"><input type="search" name="_USERCD" value="<%=AGSAG01.getUsercd()%>" maxlength="8">&nbsp;<font class="redfont">※半角英数８桁</font></td>
									<%-- 2017/02/15 登録改善対応 CHG END--%>
								</tr>
							</table>
						</td>
						<td width="50%" style="height:100%">
							<table cellspacing="0" style="width:100%" style="height:100%">
								<%-- 2019/05/10 回収管理機能仕様変更対応 CHG START --%>
								<tr>
									<%-- 2019/05/10 回収管理機能仕様変更対応 DEL
									<td colspan="10" class="white_left" valign="top"><font class="redfont" size="2">←Ctrl＋クリックで複数の枝番を選択できます。<br>
									&nbsp&nbsp連続した複数の枝番を選択する際にはShift＋クリックで選択できます。</font></td>--%>
									<td colspan="3" class="lightgray" width="22%">受注停止有無</td>
									<td colspan="7" class="white_left" width="78%"><%=AGSAG01.getPd_resstp()%></td>
								</tr>
								<tr>
									<td colspan="3" class="lightgray" width="22%">受注停止理由</td>
									<td colspan="7" class="white_left" width="78%">
										<input type="checkbox" name="_STOPREASON_312" value="312" <%=AGSAG01.getStopreason_312_Chk()%>>債権回収中&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" name="_STOPREASON_501" value="501" <%=AGSAG01.getStopreason_501_Chk()%>>債権回収不能&nbsp;
										<input type="checkbox" name="_STOPREASON_000" value="000" <%=AGSAG01.getStopreason_000_Chk()%>>左記以外全て<BR />
										<input type="checkbox" name="_STOPREASON_963" value="963" <%=AGSAG01.getStopreason_963_Chk()%>>AG依頼その他&nbsp;
										<input type="checkbox" name="_STOPREASON_725" value="725" <%=AGSAG01.getStopreason_725_Chk()%>>AG依頼脱会&nbsp;
									</td>
								</tr>
								<%-- 2019/05/10 回収管理機能仕様変更対応 CHG END --%>
								<tr>
									<td colspan="3" class="lightgray" width="22%">調整項目種別</td>
									<td colspan="7" class="white_left" width="78%"><%=AGSAG01.getPd_tgc()%></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<table cellspacing="0" style="width:100%" style="height:100%">
					<tr>
						<td width="50%">
							<table cellspacing="0" style="width:100%" style="height:100%">
								<tr>
									<td colspan="2" class="lightgray" style="width:18%">突合パターン</td>
									<td colspan="9" class="white_left">
									<a href="javascript:void(0);" onClick="openTotugouInfoWindow()"; return false;><img src="../contents/img/common/question.gif" border="0" width="18" height="18" alt="突合パターンとは" /></a>

<%
  int len = AGSAG01.getRb_tg_ptn_Len() ;
  for (int i = 0 ; i < len ; i++) {
%>
			<%=AGSAG01.getRb_tg_ptn(i)%>
<%
  }
%>
									</td>
								</tr>
								<tr>
									<td rowspan="2" colspan="2" class="lightgray" style="width:18%">請求データ</td>
									<td colspan="9" class="white_left">支払方法：<%= AGSAG01.getPd_Sk_houhou() %>&nbsp;&nbsp;
								請求種別：<%=AGSAG01.getPd_sk_seg()%></td>
								</tr>
								<tr>
									<td colspan="9" class="white_left">請求締日：
									<input type="text" name="_SKFROMYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getSkFromYear()%>"  maxlength="4">年
									<input type="text" name="_SKFROMMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getSkFromMonth()%>"  maxlength="2">月
									<%=AGSAG01.getPd_SkFromDay()%>日
									～
  									<input type="text" name="_SKTOYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getSkToYear()%>" maxlength="4">年
  									<input type="text" name="_SKTOMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getSkToMonth()%>" maxlength="2">月
  									<%=AGSAG01.getPd_SkToDay()%>日
 									</td>
 								</tr>
 							</table>
 						</td>
 						<td width="50%">
 							<table cellspacing="0" style="width:100%" style="height:100%">
 								<tr>
 									<%-- 2019/05/10 回収管理改善対応 CHG START --%>
									<td rowspan="4" colspan="3" class="lightgray" width="22%">入金データ</td>
									<td colspan="9" class="white_left" width="78%">入金方法：<%= AGSAG01.getPd_Nk_houhou() %>&nbsp;&nbsp;
									入金種別：<%=AGSAG01.getPd_nk_seg()%></td>
									<%-- 2019/05/10 回収管理改善対応 CHG END --%>
								</tr>
								<tr>
									<td colspan="9" class="white_left">ＡＧ入金日&nbsp;：
									<input type="text" name="_NKFROMYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getNkFromYear()%>" maxlength="4">年
									<input type="text" name="_NKFROMMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkFromMonth()%>" maxlength="2">月
									<input type="text" name="_NKFROMDAY" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkFromDay()%>" maxlength="2">日
									～
									<input type="text" name="_NKTOYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getNkToYear()%>" maxlength="4">年
									<input type="text" name="_NKTOMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkToMonth()%>" maxlength="2">月
									<input type="text" name="_NKTODAY" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkToDay()%>" maxlength="2">日
									</td>
								</tr>
								<%-- 2019/05/10 回収管理機能仕様変更対応 ADD START --%>
								<tr>
									<td colspan="9" class="white_left">支払日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：
									<input type="text" name="_NKPAYFROMYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getNkpayFromYear()%>" maxlength="4">年
									<input type="text" name="_NKPAYFROMMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkpayFromMonth()%>" maxlength="2">月
									<input type="text" name="_NKPAYFROMDAY" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkpayFromDay()%>" maxlength="2">日
									～
									<input type="text" name="_NKPAYTOYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getNkpayToYear()%>" maxlength="4">年
									<input type="text" name="_NKPAYTOMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkpayToMonth()%>" maxlength="2">月
									<input type="text" name="_NKPAYTODAY" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkpayToDay()%>" maxlength="2">日
									</td>
								</tr>
								<%-- 2019/05/10 回収管理機能仕様変更対応 ADD END --%>
								<tr>
									<td colspan="9" class="white_left">入金登録日：
									<input type="text" name="_NKINFROMYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getNkinFromYear()%>" maxlength="4">年
									<input type="text" name="_NKINFROMMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkinFromMonth()%>" maxlength="2">月
									<input type="text" name="_NKINFROMDAY" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkinFromDay()%>" maxlength="2">日
									～
									<input type="text" name="_NKINTOYEAR" style="width:<%=INPUT_SIZE4%>" value="<%=AGSAG01.getNkinToYear()%>" maxlength="4">年
									<input type="text" name="_NKINTOMONTH" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkinToMonth()%>" maxlength="2">月
									<input type="text" name="_NKINTODAY" style="width:<%=INPUT_SIZE2%>" value="<%=AGSAG01.getNkinToDay()%>" maxlength="2">日
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<table border="1">
			<tr>
			<td colspan="22" class="gray">
				<input type="button" onClick="disforward('_FORM1', 'SEARCH', 'AG01', 'AGSAG01Action.do'); return false;" value="検索" style="width:100; color:revert;" name="SEARCH">
				<input type="button" onClick="clickReset('_FORM1'); return false;" value="条件クリア" style="width:100;">
			</td>
			</tr>
		</table>
	</div>
<!--==========/ContentBody==========-->

<%
	if (AGSAG01.isError()) {
	// 検索条件指定エラー
%>
<br>
<br>
  <!-- 他のエラーメッセージの表示位置指定方法修正庭合わせて、テーブルタグ囲みに修正 -->
  <table style="margin:0px auto" align="center"><tr><td><ags:errors /></td></tr></table>
<%
	} else if(AGSAG01.isInit()) {
    // 検索条件指定エラー または 初期遷移
%>
<br>
<br>
<center>上のメニューを選択するか、条件を設定して検索を行ってください。</center>

<%
	} else if (totalCnt == 0) { // 検索条件指定エラー または 初期遷移 ここまで

%>
<br>
<br>
<center>検索結果０件です。条件を設定して検索を行ってください。</center>
<%
	} else {
%>
	<div class="contents">
	<table cellspacing="0" class="kingaku">
		<tr>
		<td class="pink"><p class="t-header">請求合計：&nbsp;&nbsp;<strong><%=XXXXUtil.formatMoney(AGSAG01.getSk_bill_total(), PTN_M2)%></strong></p></td>
		<td class="green"><p class="t-header">入金合計：&nbsp;&nbsp;<strong><%=XXXXUtil.formatMoney(AGSAG01.getNk_debit_total(), PTN_M2)%></strong></p></td>
		</tr>
		</table>
		</div>
		<div class="contents">
		<table cellspacing="0" class="navi">
		<tr>
		<td align="left" width="33%">全<%=totalCnt%>件 <%=currentPageNo%>/<%=lastPageNo%>ページ
<%
		if (currentPageNo != firstPageNo) {
%>
                  <a class="pagebreakON" onClick="setPno('<%=firstPageNo%>'); forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">&lt;&lt;最初の頁</a>&nbsp;&nbsp;
<%
        }
%>
<%
		if (previousPageNo != 0) {
%>
                  <a class="pagebreakON" onClick="setPno('<%=previousPageNo%>') ;forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">&lt;前頁</a>&nbsp;&nbsp;
<%
  }
%>
<%
		if (nextPageNo != 0) {
%>
                  <a class="pagebreakON" onClick="setPno('<%=nextPageNo%>') ;forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">次頁&gt;</a>&nbsp;&nbsp;
<%
		}
%>
<%
		if (currentPageNo != lastPageNo) {
%>
			<a class="pagebreakON" onClick="setPno('<%=lastPageNo%>'); forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">最後の頁&gt;&gt;</a>
<%
		}
%>
		</td>
		<td align="right" width="34%">
		<a href="#" onClick="javaScript:helpMove01();" ><u span class="bluefont">用語の説明</span></u></a>
			表示件数<%=AGSAG01.getPd_ListCnt()%>
			<input type="button" onClick="forward('_FORM1', 'LISTCNT', 'AG01', 'AGSAG01Action.do'); return false;" value="件数変更" style="width:70px;">
		</td>
		</tr>
	</table>
	<table cellspacing="0" border="2">

<!-- 一覧ヘッダ ここから -->

		<tr>
		<td rowspan="2" class="gray">消<br />込</td>
		<td rowspan="2" class="gray">突<br />合</td>
		<th colspan="8" class="pink"><p class="t-header">請求情報</p></th><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX CHG --%>
		<th colspan="9" class="green"><p class="t-header">入金情報</p></th><%-- 2018/07/09 回収管理改善対応 XXXX CHG --%>
		<td rowspan="2" class="gray">コメント</td>
		</tr>
		<tr>
		<td class="pink">枝<br />番</td>
		<td class="pink">
			<a href="javascript:void(0);" onClick="setSort('SK_DT'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">請求締日</a>
<%
		if (sort.equals("SK_DT")) {
%>
			<br><span class="notice"><%=ascendChar%></span>
<%
		}
%>
		</td>
		<td class="pink">
			<a href="javascript:void(0);" onClick="setSort('SK_USERCD'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">お客様コード</a><br>お客様名
<%
		if (sort.equals("SK_USERCD")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="pink">
			<a href="javascript:void(0);" onClick="setSort('SK_TOTAL'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">請求<br>金額</a>
<%
		if (sort.equals("SK_TOTAL")) {
%>
			<br><span class="notice"><%=ascendChar%></span>
<%
		}
%>
		</td>
		<td class="pink">
			<a href="javascript:void(0);" onClick="setSort('SK_MISYORI'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">未処理<br>金額</a>
<%
		if (sort.equals("SK_MISYORI")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="pink">
			<a href="javascript:void(0);" onClick="setSort('SK_SUM'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">今回<br>消込額</a>
<%
		if (sort.equals("SK_SUM")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="pink">種<br>別</td>
		<td class="pink">決<br>済</td><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX ADD --%>
		<td class="green">枝<br>番</td>
		<td class="green">
			<a href="javascript:void(0);" onClick="setSort('NK_DT'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">ＡＧ入金日</a><br>------------<br>支払日
<%
		if (sort.equals("NK_DT")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="green">振込人名</td>
		<td class="green">ＡＧ口座</td>
		<td class="green">
			<a href="javascript:void(0);" onClick="setSort('NK_TOTAL'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">入金<br>金額</a>
<%
		if (sort.equals("NK_TOTAL")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="green">
			<a href="javascript:void(0);" onClick="setSort('NK_MISYORI'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">未処理<br>金額</a>
<%
		if (sort.equals("NK_MISYORI")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="green">
			<a href="javascript:void(0);" onClick="setSort('NK_SUM'); forward('_FORM1', 'SORT', 'AG01', 'AGSAG01Action.do'); return false;">今回<br>消込額</a>
<%
		if (sort.equals("NK_SUM")) {
%>
			<br><font class="redfont"><%=ascendChar%></font>
<%
		}
%>
		</td>
		<td class="green">種<br>別</td>
		<td class="green"><span class="green" style="margin:0;padding:0;border:0;display:inline-block;text-align:left;">速/<br>確&nbsp;</span></td><%-- 2018/07/09 回収管理改善対応 XXXX ADD --%>
		</tr>
<!-- 一覧ヘッダ ここまで -->

<%
		// ============================================================ //
		//                     一覧生成 ここから                        //
		// ============================================================ //

		// 初期処理
		head_cnt = AGSAG01.getRowCount("RS_WORK_HEAD") ; // 行ヘッダの数
		// 一覧生成ループ
		for (int i = 0 ; i < head_cnt ; i++) {
			skLen = Integer.parseInt(AGSAG01.getColumn("RS_WORK_HEAD", "WLH_SK_LEN", i)) ;
			nkLen = Integer.parseInt(AGSAG01.getColumn("RS_WORK_HEAD", "WLH_NK_LEN", i)) ;
			pk = AGSAG01.getColumn("RS_WORK_HEAD", "WLH_PK", i) ;
			pkType = AGSAG01.getColumn("RS_WORK_HEAD", "WLH_TYPE", i) ;
			tg_ptn = AGSAG01.getColumn("RS_WORK_HEAD", "TGP_SHORTNAME", i) ;
			// 突合選択チェックボックス
			if (pkType.equals("t")) {
				if (AGSAG01.getColumn("RS_WORK_HEAD", "WLH_TG_KESHIKOMIFLG", i).equals("0")) {
					// 突合済(未消込)
					tgCheck = AGSAG01.getCb_Check_flg(i) +
						"<input type=\"hidden\" name=\"" + AGSAG01.getTagName("_WLH_PK", i) + "\" value=\"" + pk + "\">" ;
				} else {
					// 消込済
					tgCheck = "済" ;
				}
			} else {
				tgCheck = "&nbsp;" ;
			}
			// 突合パターン
			if (tg_ptn.length() > 0) {
				// 突合済
				if (AGSAG01.getColumn("RS_WORK_HEAD", "WLH_TG_KESHIKOMIFLG", i).equals("0")) {
					tgPtn = "<a href=\"javascript:void(0);\" onClick=\"setSeq(0, '" + pk + "'); forward('_FORM1', '', 'AJ01', 'AGSAJ01Action.do'); return false;\"><b>" + tg_ptn + "</b></a>" ;
				} else {
					// 消込済
					tgPtn = "<a href=\"javascript:void(0);\" onClick=\"setSeq(0, '" + pk + "'); forward('_FORM1', '', 'AJ04', 'AGSAJ04Action.do'); return false;\"><b>" + tg_ptn + "</b></a>" ;
				}
			} else {
				// 未突合
				if (pkType.equals("s")) {
					// 未突合請求
					tgPtn = "<a href=\"javascript:void(0);\" onClick=\"setSeq(1, '" + pk + "'); forward('_FORM1', '', 'AJ01', 'AGSAJ01Action.do'); return false;\"><b>未</b></a>" ;
				} else {
					// 未突合入金
					tgPtn = "<a href=\"javascript:void(0);\" onClick=\"setSeq(2, '" + pk + "'); forward('_FORM1', '', 'AJ01', 'AGSAJ01Action.do'); return false;\"><b>未</b></a>" ;
				}
			}

			maxLen = Math.max(skLen, nkLen) ;
			// 突合セットの表示ループ
			for (int j = 0 ; j < maxLen ; j++) {
				// ============================================================ //
				//                     コメントの編集                           //
				// ============================================================ //

				comment = "" ;

				// 突合コメント
				if ((j == 0) && (AGSAG01.getColumn("RS_WORK_HEAD", "WLH_TG_COMMENT", i).length() > 0)) {
					tempComment = AGSAG01.getColumn("RS_WORK_HEAD", "WLH_TG_COMMENT", i) ;
					comment += "<b>突)</b><a href=\"javascript:void(0);\" onClick=\"alert('" + XXXXUtil.replaceString(tempComment, "\\", "&yen;") + "'); return false;\">" +
								tempComment.substring(0, Math.min(tempComment.length(), COMMENT_MAX_LEN)) + "...</a><br>" ;
				}
				if ((j < skLen) && (AGSAG01.getColumn("RS_WORK_SK", "WLSK_COMMENT", skIndex).length() > 0)) {
					tempComment = AGSAG01.getColumn("RS_WORK_SK", "WLSK_COMMENT", skIndex) ;
					comment += "<b>請)</b><a href=\"javascript:void(0);\" onClick=\"alert('" + XXXXUtil.replaceString(tempComment, "\\", "&yen;") + "'); return false;\">" +
								tempComment.substring(0, Math.min(tempComment.length(), COMMENT_MAX_LEN)) + "...</a><br>" ;
				}
				if ((j < nkLen) && (AGSAG01.getColumn("RS_WORK_NK", "WLNK_COMMENT", nkIndex).length() > 0)) {
					tempComment = AGSAG01.getColumn("RS_WORK_NK", "WLNK_COMMENT", nkIndex) ;
					comment += "<b>入)</b><a href=\"javascript:void(0);\" onClick=\"alert('" + XXXXUtil.replaceString(tempComment, "\\", "&yen;") + "'); return false;\">" +
								tempComment.substring(0, Math.min(tempComment.length(), COMMENT_MAX_LEN)) + "...</a><br>" ;
	      		}
				comment = XXXXUtil.nvl(comment, "&nbsp;") ;
%>
              <tr>
<%
			// ============================================================ //
			//              枝番、突合選択、突合パターンの表示              //
			// ============================================================ //
			// rowspan処理 先頭のみ
				if (j == 0) {
%>
		<td class="white" rowspan="<%=maxLen%>"><%=tgCheck%></td>
		<td class="white" rowspan="<%=maxLen%>"><%=tgPtn%></td>
<%
				}
				// ============================================================ //
				//                     請求側の表示 ここから                    //
				// ============================================================ //

				if (j < skLen) {
					String mishori = AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_MISYORI", skIndex, TYP_M, PTN_M);
					//String bill = AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_BILL", skIndex, TYP_M, PTN_M);
					String color = "white";
					if (mishori != null && !mishori.equals("&yen;0") ) {
						color = "lightgray";
					}
					if (!AGSAG01.getColumn("RS_WORK_SK", "WLSK_TYPE", skIndex).equals("c")) {
						// 請求データ
%>
		<td class="<%= color %>" width="27"><%=AGSAG01.getColumnSP("RS_WORK_SK", "AGSUBCD", skIndex)%></td>
		<td class="<%= color %>" width="70"><%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_CHECKDATE", skIndex, TYP_D, PTN_D)%></td>
		<td class="<%= color %>" width="150" style="white-space:normal;">
<%
						// 2019-05-10 回収管理機能仕様変更対応 ADD START
						String stopKbn = "";
						if (AGSAG01.getColumn("RS_WORK_SK", "STOPSEG", skIndex).equals("1")) {
							stopKbn = "！";
						}
						// 2019-05-10 回収管理機能仕様変更対応 ADD END

						if (AGSAG01.checkAgsubcd(AGSAG01.getColumnSP("RS_WORK_SK", "AGSUBCD", skIndex))){
%>
			<span style="color: #ff0000; font-weight: bold"><%=stopKbn%></span><br><%-- 2019-05-10 回収管理機能仕様変更対応 ADD --%>
			<a href="javascript:void(0);" onClick="openUserDetailPage('<%= userInfo.getAgCd() %>', '<%=AGSAG01.getColumn("RS_WORK_SK", "AGSUBCD", skIndex)%>', '<%=AGSAG01.getColumnSP("RS_WORK_SK", "USERCD", skIndex)%>','NYUKIN','')"; return false;><%=AGSAG01.getColumnSP("RS_WORK_SK", "USERCD", skIndex)%></a><br>
			<%=AGSAG01.getColumnSP("RS_WORK_SK", "USERNAME", skIndex)%>
		</td>
		<td class="<%= color %> align_right" width="60" style="width:50px;" nowrap>
			<a href="javascript:void(0);" onClick="setSeq(1, '<%=AGSAG01.getColumn("RS_WORK_SK", "SK_SEQ", skIndex)%>'); forward('_FORM1', '', 'AG05', 'AGSAG05Action.do'); return false;"><%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_BILL", skIndex, TYP_M, PTN_M)%></a>
<%
						} else {
%>
			<span style="color: #ff0000; font-weight: bold"><%=stopKbn%></span><br><%-- 2019-05-10 回収管理機能仕様変更対応 ADD --%>
			<%=AGSAG01.getColumnSP("RS_WORK_SK", "USERCD", skIndex)%><br>
			<%=AGSAG01.getColumnSP("RS_WORK_SK", "USERNAME", skIndex)%>
		</td>
		<td class="<%= color %> align_right" width="60" nowrap><%-- ★★★150 → 60 に変更（レイアウトずれ：幅超過を回避））★★★ --%>
			<%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_BILL", skIndex, TYP_M, PTN_M)%>
<%
						}
%>
		</td>
		<td class="<%= color %> align_right" width="60" style="width:50px;" nowrap><%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_MISYORI", skIndex, TYP_M, PTN_M)%></td>
		<td class="<%= color %> align_right" width="60" style="width:50px;" nowrap><%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_SUM", skIndex, TYP_M, PTN_M)%></td>
		<td class="<%= color %> align_cener"><%=AGSAG01.getColumnSP("RS_WORK_SK", "SKS_SHORTNAME", skIndex)%></td>
		<td class="<%= color %> align_cener"><%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_PAYSEG_SHORTNM", skIndex)%></td><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX ADD --%>
<%
					} else {
						// 突合調整項目
						if (AGSAG01.getColumn("RS_WORK_SK", "CS_FUGO", skIndex).equals(FUGO_MINUS)) {
							// マイナス項目は赤
							style = " redBold" ;
						} else {
							style = "" ;
						}
%>
		<td class="white">&nbsp;</td>
		<td class="white <%=style%>" colspan="4"><%=AGSAG01.getColumnSP("RS_WORK_SK", "CS_NAME", skIndex)%></td>
		<td class="white_right <%=style%>" nowrap><%=AGSAG01.getColumnSP("RS_WORK_SK", "WLSK_SUM", skIndex, TYP_M, PTN_M)%></td>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX ADD --%>
<%
					}
					skIndex++ ;
				} else {
%>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white_right">&nbsp;</td>
		<td class="white_right">&nbsp;</td>
		<td class="white_right">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX ADD --%>
<%
				}
				// ============================================================ //
				//                     請求側の表示 ここまで                    //
				// ============================================================ //

				// ============================================================ //
				//                     入金側の表示 ここから                    //
				// ============================================================ //
				if (j < nkLen) {
					// 入金データ
					if (!AGSAG01.getColumn("RS_WORK_NK", "WLNK_TYPE", nkIndex).equals("c")) {
%>
		<td class="white" align="Center" width="27"><%=AGSAG01.getColumnSP("RS_WORK_NK", "AGSUBCD", nkIndex)%></td>
		<td class="white" width="70"><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_TRANSFER_DT", nkIndex, TYP_D, PTN_D)%><br>----------<br>
		<%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_PAY_DT", nkIndex, TYP_D, PTN_D)%></td>
		<td class="white" width="110"><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_DEBITBACKANA", nkIndex)%></td>
		<td class="white" width="70"><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_TRANBANKKANA", nkIndex)%><br><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_TRANBSNKSUBKANA", nkIndex)%></td>
		<td class="white" align="Right" width="60" nowrap>
<%
						if (AGSAG01.checkAgsubcd(AGSAG01.getColumnSP("RS_WORK_NK", "AGSUBCD", nkIndex))){
%>
			<a href="javascript:void(0);" onClick="setSeq(2, '<%=AGSAG01.getColumn("RS_WORK_NK", "NK_SEQ", nkIndex)%>'); forward('_FORM1', '', 'AG08', 'AGSAG08Action.do'); return false;"><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_DEBIT", nkIndex, TYP_M, PTN_M)%></a>
<%
						}else{
%>
			<%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_DEBIT", nkIndex, TYP_M, PTN_M)%>
<%
						}
%>
		</td>
		<td class="white_right" width="60" nowrap><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_MISYORI", nkIndex, TYP_M, PTN_M)%></td>
		<td class="white_right" width="60" nowrap><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_SUM", nkIndex, TYP_M, PTN_M)%></td>
		<td class="white align_center"><%=AGSAG01.getColumnSP("RS_WORK_NK", "NKS_SHORTNAME", nkIndex)%></td>
		<td class="white align_center"><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_SOKUHO", nkIndex)%></td>
<%
					} else {
						// 突合調整項目
						if (AGSAG01.getColumn("RS_WORK_NK", "CS_FUGO", nkIndex).equals(FUGO_MINUS)) {
							// マイナス項目は赤
							style = " redBold" ;
						} else {
							style = "" ;
						}
%>
		<td class="white">&nbsp;</td>
		<td class="white <%=style%>" colspan="5"><%=AGSAG01.getColumnSP("RS_WORK_NK", "CS_NAME", nkIndex)%></td>
		<td class="white_right <%=style%>" nowrap><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_SUM", nkIndex, TYP_M, PTN_M)%></td>
		<td class="white">&nbsp;</td>
		<td class="white align_center"><%=AGSAG01.getColumnSP("RS_WORK_NK", "WLNK_SOKUHO", nkIndex)%></td><%-- 2018/07/09 回収管理改善対応 XXXX ADD --%>
<%
					}
					nkIndex++ ;
				} else if((j == 0) && tg_ptn.length() > 0 && nkLen == 0){
%>
		<%-- 2018/07/09 回収管理改善対応 XXXX CHG(colspan 8 -> 9) --%>
		<td class="white" colspan="9"><span style="font-size:80%">このデータは既に突合・消込されています。<br>（差額調整項目と突合した場合や、入金データと突合したが<br>表示対象期間を経過した場合、入金欄は空欄となります。）</span></td>
<%
				} else {
%>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white_right">&nbsp;</td>
		<td class="white_right">&nbsp;</td>
		<td class="white_right">&nbsp;</td>
		<td class="white">&nbsp;</td>
		<td class="white">&nbsp;</td><%-- 2018/07/09 回収管理改善対応 XXXX ADD --%>
<%
				}
				// ============================================================ //
				//                     入金側の表示 ここまで                    //
				// ============================================================ //

				// ============================================================ //
				//                     コメントの表示 ここから                  //
				// ============================================================ //
				if (j < (maxLen - 1)) {
%>
		<td class="white" nowrap><%=comment%></td>
<%
				} else {
%>
		<td class="white" nowrap><%=comment%></td>
<%
				}
				// ============================================================ //
				//                     コメントの表示 ここまで                  //
				// ============================================================ //
%>
		</tr>
<%
			}
%>
		<tr><td colspan="21" height="1" bgcolor="silver"><img src="../contents/img/common/spacer.gif" height="1" width="1"></td></tr>
<%
		}
		// ============================================================ //
		//                     一覧生成 ここまで                        //
		// ============================================================ //
%>
		<tr>
		<td rowspan="2" class="gray">消<br>込</td>
		<td rowspan="2" class="gray">突<br>合</td>
		<td class="pink">枝<br>番</td>
		<td class="pink">請求締日</td>
		<td class="pink">お客様コード<br>お客様名</td>
		<td class="pink">請求<br>金額</td>
		<td class="pink">未処理<br>金額</td>
		<td class="pink">今回<br>消込額</td>
		<td class="pink">種<br>別</td>
		<td class="pink">決<br>済</td><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX ADD --%>
		<td class="green">枝<br>番</td>
		<td class="green">ＡＧ入金日<br>------------<br>支払日</td>
		<td class="green">振込人名</td>
		<td class="green">ＡＧ口座</td>
		<td class="green">入金<br>金額</td>
		<td class="green">未処理<br>金額</td>
		<td class="green">今回<br>消込額</td>
		<td class="green">種<br>別</td>
		<td class="green"><span class="green" style="margin:0;padding:0;border:0;display:inline-block;text-align:left;">速/<br>確&nbsp;</span></td><%-- 2018/07/09 回収管理改善対応 XXXX ADD --%>
		<td rowspan="2" class="gray">コメント</td>
		</tr>
		<tr>
		<th colspan="8" class="pink"><p class="t-header">請求情報</p></th><%-- 2017/12/07 登録改善_フェーズ2.5対応 XXXX CHG --%>
		<th colspan="9" class="green"><p class="t-header">入金情報</p></th><%-- 2018/07/09 回収管理改善対応 XXXX CHG --%>
		</tr>
	</table>
	<div class="contents">
	<table cellspacing="0" class="navi">
		<tr>
		<td align="left" width="33%">全<%=totalCnt%>件  <%=currentPageNo%>/<%=lastPageNo%>ページ
<%
		if (currentPageNo != firstPageNo) {
%>
			<a class="pagebreakON" onClick="setPno('<%=firstPageNo%>'); forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">&lt;&lt;最初の頁</a>&nbsp;&nbsp;
<%
		}
%>
<%
		if (previousPageNo != 0) {
%>
                  <a class="pagebreakON" onClick="setPno('<%=previousPageNo%>') ;forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">&lt;前頁</a>&nbsp;&nbsp;
<%
		}
%>
<%
		if (nextPageNo != 0) {
%>
                   <a class="pagebreakON" onClick="setPno('<%=nextPageNo%>') ;forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">次頁&gt;</a>&nbsp;&nbsp;
<%
		}
%>
<%
		if (currentPageNo != lastPageNo) {
%>
                   <a class="pagebreakON" onClick="setPno('<%=lastPageNo%>'); forward('_FORM1', 'PAGEBREAK', 'AG01', 'AGSAG01Action.do'); return false;" onMouseover="chengeCursor(this, true);", onMouseOut="chengeCursor(this, false);">最後の頁&gt;&gt;</a>
<%
		}
%>
		</td>
		<td align="right" width="34%">　
		</td>
		</tr>
	</table>
	</div>
</div>
<%
	}
%>

</form>
</div>
<!-- // コンテンツー -->
<c:import url="include/footer.jsp"/>

<%-- 2018/08/01 回収管理改善対応 XXXX ADD START --%>
<script type="text/javascript">
function agsubselcheck(){
	var elm = document.getElementsByName('_PD_AGSUBCD');
	var selectedValue = '';
	for(var i=0; i<elm.length; i++){
		if(elm[i].tagName == 'SELECT'){
			for(var j=0; j<elm[i].options.length; j++) {
				if (elm[i].options[j].selected) {
					if(elm[i].options[j].value == 'allAgsubcd'){
						selectedValue += "(全て),";
					} else if(elm[i].options[j].value == ''){
						selectedValue += "枝番なし,";
					} else {
						selectedValue += elm[i].options[j].value + ",";
					}
				}
			}
			break;
		}
	}
	if(selectedValue == ''){
		document.getElementById('AGSUBTITLE').title = "";
	} else {
		document.getElementById('AGSUBTITLE').title = "選択済枝番＝"+selectedValue.slice(0, selectedValue.length - 1);
	}
}
// //イベント追加処理
function addEv(){
	var elm = document.getElementsByName('_PD_AGSUBCD');
	for(var i=0; i<elm.length; i++){
		if(elm[i].tagName == 'SELECT'){
			if (elm[i].options.length>0) {
				if (elm[i].addEventListener) {
					elm[i].addEventListener("change", changeStopreason, false);
				} else {
					// IE動作確認用コード
					elm[i].attachEvent("onchange", changeStopreason, false);
				}
			}
			break;
		}
	}
}
agsubselcheck();
addEv();
// 2019/05/10 回収管理機能仕様変更対応 ADD START
changeStopreason();
checkResstpEv()
// 2019/05/10 回収管理機能仕様変更対応 ADD END
</script>
<%-- 2018/08/01 回収管理改善対応 XXXX ADD END --%>
</body>
</html>