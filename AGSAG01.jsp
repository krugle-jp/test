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
		ascendChar = "����" ;
	} else {
		ascendChar = "�~��" ;
	}

	// ���y�[�W����ϐ�
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

	String s312 = AGSAG01.getStopreason_312_Chk();
	String s501 = AGSAG01.getStopreason_501_Chk();
	String s000 = AGSAG01.getStopreason_000_Chk();
	String s963 = AGSAG01.getStopreason_963_Chk();
	String s725 = AGSAG01.getStopreason_725_Chk();

	// �ꗗ�����p�̕ϐ�
	int skLen = 0 ; // �������̍s��
	int nkLen = 0 ; // �������̍s��
	int maxLen = 0 ; // max�l(�������̍s���A�������̍s��)
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
	// NO�L���b�V���̐ݒ�
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html lang="ja">
<head>
<meta http-equiv="content-type" content="text/html;charset=shift_jis" />
<meta http-equiv="content-style-type" content="text/css" />
<meta http-equiv="content-script-type" content="text/javascript" />
<title>����Ǘ��Ɩ��^���������󋵈ꗗ</title>
<link rel="stylesheet" href="../contents/css/style.css" type="text/css" />
<script src="contents/script/script_base.js"></script>
<script language="javascript">
<!--
var currSort = '<%=sort%>';
var currAscend = '<%=ascend%>' ;

/**
 * �\�[�g��̎w��
 *
 * �p�����[�^:    sort �\�[�g��
 * @since �o�[�W���� 1.00
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
 * �\�[�g��̐ݒ�
 *
 * �p�����[�^:    sort �\�[�g��
 * @since �o�[�W���� 1.00
 */
function setPno(pno) {
	document._FORM1._PNO.value = pno ;
} // setPno()

/**
 * �V�[�P���X�̐ݒ�
 *
 * �p�����[�^:    ptn �V�[�P���X�p�^�[��
 * �p�����[�^:    seq �V�[�P���X�l
 * @since �o�[�W���� 1.00
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
 * ���������̃N���A
 * @since �o�[�W���� 1.00
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
	agsubselcheck();	// �}�ԑI���󋵕\���ύX
	// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
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
	// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END
} // clickReset()

/**
 * ���s�m�F
 * @since �o�[�W���� 1.00
 */
function execConfirm(msg, frmName, action, id, path) {
	if(confirm(msg)) {
		forward(frmName, action, id, path);
	}
} // execConfirm()

/**
 * ���s�m�F�i�����ˍ��{�^���j
 * @since �o�[�W���� 1.00
 */
function execConfirmWait(msg, frmName, action, id, path) {
	if(isReadyState()) {  //2004/11/16 �A�Ŗh�~
		if(confirm(msg)) {
			waitOpen();
			forward(frmName, action, id, path);
		}
	}
} // execConfirm()

/**
 * �����{�^���������̃{�^���񊈐���
 *
 * �p�����[�^:    formName      FORM��
 * �p�����[�^:    actionType    Action
 * �p�����[�^:    transactionId TID
 * �p�����[�^:    actionPath    Path
 * @since �o�[�W���� 1.00
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
 * �����{�^���������̃{�^���񊈐���
 *
 * �p�����[�^:    formName      FORM��
 * �p�����[�^:    actionType    Action
 * �p�����[�^:    transactionId TID
 * �p�����[�^:    actionPath    Path
 * @since �o�[�W���� 1.00
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

function subWindowClose() {
	if (waitWindow != null){
		waitWindow.close();
	}
}

/**
 * ��ʑJ��
 *
 * �p�����[�^:    formName       submit����t�H�[����
 * �p�����[�^:    target �^�[�Q�b�g�t���[��
 * @since �o�[�W���� 1.00
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

//�󒍒�~�L�����u�L�v�̏ꍇ�󒍒�~���R���������A���R�͑S�ă`�F�b�N�Ȃ�
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
						//������ɉ�ʑI��l���N���A���Ȃ�����
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
//�󒍒�~�L����onChange�C�x���g����
function checkResstpEv(){
	var elm = document.getElementsByName('_PD_RESSTP');
	for(var i=0; i<elm.length; i++){
		if(elm[i].tagName == 'SELECT'){
			if (elm[i].options.length>0) {
				if (elm[i].addEventListener) {
					elm[i].addEventListener("change", changeStopreason, false);
				} else {
					// IE����m�F�p�R�[�h
					elm[i].attachEvent("onchange", changeStopreason, false);
				}
			}
			break;
		}
	}
}

//-->
</script>
<style type="text/css">
<!-- .bluefont { color:blue } -->

  /*   Chrome��ʕ���Ή��̂��ߒǉ� */
  input[type="radio"], [type="checkbox"] {
    margin-right: 3px;
    top: 2px;
    position: relative;
  }
</style>

<!-- 2017/04/13 XXXX GoogleAnalytics�Ή� ADD START -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-88636562-1', 'auto');
  ga('send', 'pageview');

</script>
<!-- 2017/04/13 XXXX GoogleAnalytics�Ή� ADD END -->

</head>

<body marginheight="0" marginwidth="0" topmargin="0" leftmargin="0" rightmargin="0" onpagehide="subWindowClose();">

<!-- HEADER START -->
<c:import url="include/header.jsp"/>
<c:import url="../ags/include/newusercall.jsp"/>
<c:import url="../imn/include/callpage.jsp"/>
<!-- HEADER END -->


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
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AG13', 'AGSAG13Action.do'); return false;">�����f�[�^�A�b�v���[�h</a>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AG17', 'AGSAG17Action.do'); return false;">�����蓮�o�^</a>
			<td><a href="javascript:void(0);" onClick="execConfirmWait('�ˍ������s���܂��B��낵���ł����H','_FORM1', '', 'AG18', 'AGSAG18Action.do'); return false;">�����ˍ�</a>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AJ01', 'AGSAJ01Action.do'); return false;">�蓮�ˍ�</a>
			<td><a href="javascript:void(0);" onClick="execConfirm('���������s���܂��B��낵���ł����H','_FORM1', 'KESHIKOMI', 'AG01', 'AGSAG01Action.do'); return false;">����</a>
			<td><a href="javascript:void(0);" onClick="forward('_FORM1', '', 'AM01', 'AGSAM01Action.do'); return false;">�����f�[�^�ꊇ�폜</a><%-- 2018/06/12 ����Ǘ����P�Ή� XXXX ADD --%>
		</tr>
	</table>
	</div>
	<div class="bottom">&nbsp;</div>
</form>
</div>

</body>
</html>