/*
 * Project Name  : ������WEB
 * Subsystem Name: ����Ǘ��@�\
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
 * <strong>[tran]����Ǘ��@�\�^���������󋵈ꗗ(AG01)</strong>
 * <p>
 *
 * @author XXXX
 */
public class AGSAG01Action extends AgsActionUtility {
//  private static final int SEIKYU_NYUKIN_LIST_MAX_CNT = 1000 ;
    private static final int SEIKYU_NYUKIN_LIST_MAX_CNT = XXXXUtil.getPropertyInt("F_SEIKYU_NYUKIN_LIST_MAX_CNT");


    private static final String W_TYPE_DISP = "1" ;

    private static final String[] PD_EXTRACTED_DSP = {
        "�i�S�āj",
        "���ˍ�����",
        "���ˍ�����",
        "���ˍ������^���ˍ�����",
        "������(�ˍ���)",
        "������",
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

    // �\������
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
    // �\�[�g�̃J������
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
    // �\�[�g��
    private static final String[] ASCEND = {
        "ASC",
        "DESC",
    } ;
    // �A�N�V����
    private static final String[] ACTION = {
        "SEARCH",
        "SORT",
        "LISTCNT",
        "PAGEBREAK",
        "RETURN",
        "KESHIKOMI",
        "UPDATE",
    } ;
    private static final int ACT_SEARCH            = 0 ; // ����
    private static final int ACT_SORT              = 1 ; // ���ёւ�
    private static final int ACT_LISTCNT           = 2 ; // �\�������ύX
    private static final int ACT_PAGEBREAK         = 3 ; // ���y�[�W
    private static final int ACT_RETURN            = 4 ; // �߂�
    private static final int ACT_KESHIKOMI         = 5 ; // ����
    private static final int ACT_UPDATE            = 6 ; // �X�V�߂�

//    private int action_ ;

/**
 * ���̓`�F�b�N���s�Ȃ�.
 *
 * @return boolean �`�F�b�N�n�j�̂Ƃ�true�A�m�f�̂Ƃ�false
 * @since �o�[�W���� 1.00
 */
    protected boolean checkInput() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#checkInput()") ;
        printData() ;
        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

       int action_ ;
       ActionErrors errors = new ActionErrors() ;

/* 2004/10/26 �r������ */
        // ������DB2�̐��������`�F�b�N���遚����
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

/* 2004/10/26 �r������ */
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

            /*------------------ ���N�G�X�g�̎擾 --------------------------------*/
            // �A�N�V����
            action_ = AGSUtil.getIndex(ACTION, db1.getStringNvl("_ACTION")) ;

            // ��������
            // 2006-05-17 �}�ԕ����I�� START
            //String pd_agsubcd = db1.getStringNvl("_PD_AGSUBCD") ;
            String[] pd_agsubcd = db1.getStringArrayNvl("_PD_AGSUBCD",new String[0]) ;
            // 2006-05-17 �}�ԕ����I�� END
            String pd_extracted = db1.getStringNvl("_PD_EXTRACTED") ;
            String pd_sk_seg = db1.getStringNvl("_PD_SK_SEG") ;
            String pd_nk_seg = db1.getStringNvl("_PD_NK_SEG") ;
            String pd_tgc = db1.getStringNvl("_PD_TGC") ;
            String rb_tg_ptn = db1.getStringNvl("_RB_TG_PTN") ;
            String skFromYear = db1.getStringNvl("_SKFROMYEAR") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String skFromMonth = db1.getStringPad("_SKFROMMONTH","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            String pd_skFromDay = db1.getStringNvl("_PD_SKFROMDAY") ;
            String skToYear = db1.getStringNvl("_SKTOYEAR") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String skToMonth = db1.getStringPad("_SKTOMONTH","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            String pd_skToDay = db1.getStringNvl("_PD_SKTODAY") ;
            String nkFromYear = db1.getStringNvl("_NKFROMYEAR") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String nkFromMonth = db1.getStringPad("_NKFROMMONTH","0",2) ;
            String nkFromDay = db1.getStringPad("_NKFROMDAY","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            String nkToYear = db1.getStringNvl("_NKTOYEAR") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String nkToMonth = db1.getStringPad("_NKTOMONTH","0",2) ;
            String nkToDay = db1.getStringPad("_NKTODAY","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            // 2004-12-15 �����o�^���ǉ� ST
            String nkinFromYear = db1.getStringNvl("_NKINFROMYEAR") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String nkinFromMonth = db1.getStringPad("_NKINFROMMONTH","0",2) ;
            String nkinFromDay = db1.getStringPad("_NKINFROMDAY","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            String nkinToYear = db1.getStringNvl("_NKINTOYEAR") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String nkinToMonth = db1.getStringPad("_NKINTOMONTH","0",2) ;
            String nkinToDay = db1.getStringPad("_NKINTODAY","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            // 2004-12-15 �����o�^���ǉ� EX
            // 2006-05-16 �x�����@�E�������@�����ǉ� START
            String pd_nk_houhou = db1.getStringNvl("_PD_NK_HOUHOU") ;
            String pd_sk_houhou = db1.getStringNvl("_PD_SK_HOUHOU");
            // 2006-05-16 �x�����@�E�������@�����ǉ� END
            String usercd = db1.getStringNvl("_USERCD") ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
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
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

            // ���ёւ�
            String sort = db1.getStringNvl("_SORT") ;
            String ascend = db1.getStringNvl("_ASCEND") ;

            // �\������
            String pd_listCnt = db1.getStringNvl("_PD_LISTCNT") ;

            // �ˍ��I���`�F�b�N�{�b�N�X
            String[] wlh_pk = db1.getStringArrayNvl("_WLH_PK", new String[0]) ;
            String[] cb_check_flg = db1.getStringArrayNvl("_CB_CHECK_FLG", new String[0]) ;

           // ���y�[�WNo
            String pno = db1.getStringNvl("_PNO") ;

            /*------------------ ���N�G�X�g�̎擾 �����܂� ---------------------------*/

            /*------------------ ���̓`�F�b�N ----------------------------------------*/
            if(action_ == ACT_SEARCH) {
                errors = new ActionErrors();
            }
            int chk = 0 ;
            int rc = 0 ;
            if (action_ == ACT_SEARCH || action_ == ACT_UPDATE) {
                setTranId(db1, "AG01") ;
                // �}��(_PD_AGSUBCD)�̃`�F�b�N
                String[] agsubcdList = getLoginAgsubcdList() ;
                if (getMastercd().equals(MASTERCD_T)) {
                    agsubcdList = XXXXUtil.addHead(agsubcdList, AGSUBCD_ALL_VAL) ;
                }
                if (AGSUtil.getIndex(agsubcdList, pd_agsubcd) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "_PD_AGSUBCD�G���[") ;
                    errors.add("ER_AG01_PD_AGSUBCD", new ActionMessage("errors.ER_AG01_PD_AGSUBCD")) ;
                }
                // ���o����
                if (AGSUtil.getIndex(PD_EXTRACTED_VAL, pd_extracted) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "���o����error=", pd_extracted) ;
                    errors.add("ER_AG01_PD_EXTRACTED", new ActionMessage("errors.ER_AG01_PD_EXTRACTED")) ;
                }
                // �������
                if ((pd_sk_seg.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_SEIKYUSHUBETSU, "SKS_CD"), pd_sk_seg) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�������error=", pd_sk_seg) ;
                    errors.add("ER_AG01_PD_SK_SEG", new ActionMessage("errors.ER_AG01_PD_SK_SEG")) ;
                }
                // �������
                if ((pd_nk_seg.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_NYUKINSHUBETSU, "NKS_CD"), pd_nk_seg) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�������error=", pd_nk_seg) ;
                    errors.add("ER_AG01_PD_NK_SEG", new ActionMessage("errors.ER_AG01_PD_NK_SEG")) ;
                }
                // 2017/12/07 �o�^���P�t�F�[�Y2.5�Ή� CHG START
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
                // �x�����@
                if ((pd_sk_houhou.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_SK_PAYSEG_CREDIT, "SK_PAYSEG_CREDIT_CD"), pd_sk_houhou) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�x�����@error=", pd_sk_houhou) ;
                    errors.add("ER_AG01_PD_SK_HOUHOU", new ActionMessage("errors.ER_AG01_PD_SK_HOUHOU")) ;
                }
                // 2017/12/07 �o�^���P�t�F�[�Y2.5�Ή� CHG END
                // �������@
                if ((pd_nk_houhou.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_NYUKINHOUHOU, "NY_CD"), pd_nk_houhou) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�������@error=", pd_nk_houhou) ;
                    errors.add("ER_AG01_PD_NK_HOUHOU", new ActionMessage("errors.ER_AG01_PD_NK_HOUHOU")) ;
                }
                // 2006-05-16 �x�����@�E�������@�����ǉ� END
                // �ˍ������敪
                if (AGSUtil.getIndex(XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_CHOUSEIKOUMOKU_ALL, "CS_CD"), ""), pd_tgc) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�ˍ������敪error=", pd_tgc) ;
                    errors.add("ER_AG01_PD_TGC", new ActionMessage("errors.ER_AG01_PD_TGC")) ;
                }
                // �ˍ��p�^�[��
                if (AGSUtil.getIndex(XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_CD"), ""), rb_tg_ptn) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�ˍ��p�^�[��error=", rb_tg_ptn) ;
                    errors.add("ER_AG01_RB_TG_PTN", new ActionMessage("errors.ER_AG01_RB_TG_PTN")) ;
                }
                // ��������
                // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
                rc = checkDateSP(skFromYear, skFromMonth, pd_skFromDay, skToYear, skToMonth, pd_skToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "���������J�nerror=", skFromYear + "/" + skFromMonth + "/" + pd_skFromDay) ;
                    errors.add("ER_AG01_SKFROM", new ActionMessage("errors.ER_AG01_SKFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "���������I��error=", skToYear + "/" + skToMonth + "/" + pd_skToDay) ;
                    errors.add("ER_AG01_SKTO", new ActionMessage("errors.ER_AG01_SKTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "��������From��To�̑Ó����G���[") ;
                    errors.add("ER_AG01_SK_DT", new ActionMessage("errors.ER_AG01_SK_DT")) ;
                }
                // ������
                rc = checkDate(nkFromYear, nkFromMonth, nkFromDay, nkToYear, nkToMonth, nkToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�������J�nerror=", nkFromYear + "/" + nkFromMonth + "/" + nkFromDay) ;
                    errors.add("ER_AG01_NKFROM", new ActionMessage("errors.ER_AG01_NKFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�������I��error=", nkToYear + "/" + nkToMonth + "/" + nkToDay) ;
                    errors.add("ER_AG01_NKTO", new ActionMessage("errors.ER_AG01_NKTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "������From��To�̑Ó����G���[") ;
                    errors.add("ER_AG01_NK_DT", new ActionMessage("errors.ER_AG01_NK_DT")) ;
                }
                // 2004-12-15 �����o�^���ǉ� EX
                // �����o�^����
                rc = checkDate(nkinFromYear, nkinFromMonth, nkinFromDay, nkinToYear, nkinToMonth, nkinToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�����o�^���J�nerror=", nkinFromYear + "/" + nkinFromMonth + "/" + nkinFromDay) ;
                    errors.add("ER_AG01_NKINFROM", new ActionMessage("errors.ER_AG01_NKINFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�����o�^���I��error=", nkinToYear + "/" + nkinToMonth + "/" + nkinToDay) ;
                    errors.add("ER_AG01_NKINTO", new ActionMessage("errors.ER_AG01_NKINTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�����o�^��From��To�̑Ó����G���[") ;
                    errors.add("ER_AG01_NKIN_DT", new ActionMessage("errors.ER_AG01_NKIN_DT")) ;
                }
                // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
                // 2004-12-15 �����o�^���ǉ� EX
                // ���q�l�R�[�h
                chk = XXXXUtil.CHK_NUMALPH | XXXXUtil.CHK_SIZ0_OK ;// 2017/02/15 �o�^���P�Ή� CHG
                rc = XXXXUtil.checkData(usercd, chk, 8, 8) ;
                if (rc != XXXXUtil.RC_OK) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "���q�l�R�[�herror=", usercd) ;
                    errors.add("ER_AG01_USERCD", new ActionMessage("errors.ER_AG01_USERCD")) ;
                }
                // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
                // �x����
                rc = checkDate(nkpayFromYear, nkpayFromMonth, nkpayFromDay, nkpayToYear, nkpayToMonth, nkpayToDay, SIZ0_OK) ;
                if (isDateError(rc, ER_DT_FROM)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�x�����J�nerror=", nkpayFromYear + "/" + nkpayFromMonth + "/" + nkpayFromDay) ;
                    errors.add("ER_AG01_NKPAYFROM", new ActionMessage("errors.ER_AG01_NKPAYFROM")) ;
                }
                if (isDateError(rc, ER_DT_TO)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�x�����I��error=", nkpayToYear + "/" + nkpayToMonth + "/" + nkpayToDay) ;
                    errors.add("ER_AG01_NKPAYTO", new ActionMessage("errors.ER_AG01_NKPAYTO")) ;
                }
                if (isDateError(rc, ER_DT_MISSMATCH)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�x����From��To�̑Ó����G���[") ;
                    errors.add("ER_AG01_NKPAY_DT", new ActionMessage("errors.ER_AG01_NKPAY_DT")) ;
                }
                // �󒍒�~�L��
                if ((pd_resstp.length() > 0) && (AGSUtil.getIndex(AGSM.getStringArray(AGSM.RS_PD_RESSTP, "RS_PD_RESSTP_CD"), pd_resstp) == -1)) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�󒍒�~�L��error=", pd_resstp) ;
                    errors.add("ER_AG01_PD_RESSTP", new ActionMessage("errors.ER_AG01_PD_RESSTP")) ;
                }
                // �󒍒�~���R
            	if ((stopreason_312.length() != 0 && (!stopreason_312.equals("312"))) ||
                    (stopreason_501.length() != 0 && (!stopreason_501.equals("501"))) ||
                    (stopreason_000.length() != 0 && (!stopreason_000.equals("000"))) ||
                    (stopreason_963.length() != 0 && (!stopreason_963.equals("963"))) ||
                    (stopreason_725.length() != 0 && (!stopreason_725.equals("725")))) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�󒍒�~���Rerror=", pd_resstp) ;
                    errors.add("ER_AG01_STOPREASON", new ActionMessage("errors.ER_AG01_STOPREASON")) ;
                }
                // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END
            }
            if (action_ == ACT_SORT) {
                if (AGSUtil.getIndex(SORT_COLUMN, sort) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "���ёւ���error=", sort) ;
                    errors.add("ER_AG01_SORT", new ActionMessage("errors.ER_AG01_SORT")) ;
                }
                if (AGSUtil.getIndex(ASCEND, ascend) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "���ёւ���error=", ascend) ;
                    errors.add("ER_AG01_ASCEND", new ActionMessage("errors.ER_AG01_ASCEND")) ;
                }
            }
            if (action_ == ACT_LISTCNT) {
                if (AGSUtil.getIndex(PD_LISTCNT_VAL, pd_listCnt) == -1) {
                    XXXXUtil.log(XXXXUtil.DEBUG, "�\������error=", pd_listCnt) ;
                    errors.add("ER_AG01_PD_LISTCNT", new ActionMessage("errors.ER_AG01_PD_LISTCNT")) ;
                }
            }
            if (action_ == ACT_RETURN) {
                // �����������L����
                if (!db2.getStringNvl("LIST_VALID_FLG").equals("1")) {
                    action_ = -1 ;
                }
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_RETURN)) {
                // ���y�[�W�͈̓`�F�b�N
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
                    XXXXUtil.log(XXXXUtil.DEBUG, "���y�[�WNOerror=", pno) ;
                    errors.add("ER_AG01_PNO", new ActionMessage("errors.ER_AG01_PNO")) ;
                }
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_KESHIKOMI) || (action_ == ACT_LISTCNT)) {

                // �ˍ��I���`�F�b�N�{�b�N�X
                chk = XXXXUtil.CHK_NUM | XXXXUtil.CHK_SIZ0_OK ;
                int len = wlh_pk.length ;
                for (int i = 0 ; i < len ; i++) {
                    if (wlh_pk[i] != null) {
                        rc = XXXXUtil.checkData(wlh_pk[i], chk, 0, 20) ;
                        if (rc != XXXXUtil.RC_OK) {
                            XXXXUtil.log(XXXXUtil.DEBUG, "�ˍ��`�F�b�N�{�b�N�Xpk error=", wlh_pk[i]) ;
                            errors.add("ER_AG01_WLH_PK", new ActionMessage("errors.ER_AG01_WLH_PK")) ;
                            break ;
                        }
                    }
                }
                len = cb_check_flg.length ;
                for (int i = 0 ; i < len ; i++) {
                    if ((cb_check_flg[i] != null) && !cb_check_flg[i].equals("1")) {
                        XXXXUtil.log(XXXXUtil.DEBUG, "�ˍ��`�F�b�N�{�b�N�Xcb_check_flg error=", cb_check_flg[i]) ;
                        errors.add("ER_AG01_CB_CHECK_FLG", new ActionMessage("errors.ER_AG01_CB_CHECK_FLG")) ;
                        break ;
                    }
                }
            }

            /*------------------ ���̓`�F�b�N �����܂� -------------------------------*/

            /*------------------ ���p���f�[�^��DB2�ɐݒ肷�� -------------------------*/
            if (action_ == ACT_SEARCH||action_ == ACT_UPDATE) {
                // ��������
            	// 2006-05-17 �}�ԕ����I�� START
            	//db2.setString("PD_AGSUBCD", pd_agsubcd) ;
                db2.setStringArray("PD_AGSUBCD", pd_agsubcd) ;
                // 2006-05-17 �}�ԕ����I�� END
                db2.setString("PD_EXTRACTED", pd_extracted) ;
                db2.setString("PD_SK_SEG", pd_sk_seg) ;
                db2.setString("PD_NK_SEG", pd_nk_seg) ;
                db2.setString("PD_TGC", pd_tgc) ;
                db2.setString("RB_TG_PTN", rb_tg_ptn) ;
                db2.setString("SKFROMYEAR", skFromYear) ;
                // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
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
                // 2004-12-15 �����o�^���ǉ� ST
                db2.setString("NKINFROMYEAR", nkinFromYear) ;
                db2.setString("NKINFROMMONTH", nkinFromMonth) ;
                db2.setString("NKINFROMDAY", nkinFromDay) ;
                db2.setString("NKINTOYEAR", nkinToYear) ;
                db2.setString("NKINTOMONTH", nkinToMonth) ;
                db2.setString("NKINTODAY", nkinToDay) ;
                // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
                // 2004-12-15 �����o�^���ǉ� EX
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
                db2.setString("PD_SK_HOUHOU", pd_sk_houhou);
                db2.setString("PD_NK_HOUHOU", pd_nk_houhou);
                // 2006-05-16 �x�����@�E�������@�����ǉ� END
                db2.setString("USERCD", usercd) ;
                // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
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
                // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

                // ���������L���t���O�𖳌��ɂ���
                db2.setString("LIST_VALID_FLG", "0") ;

            }
            if (action_ == ACT_SORT) {
                db2.setString("SORT", sort) ;
                db2.setString("ASCEND", ascend) ;
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_RETURN)) {
                // ���y�[�W����
                db2.setString("REQUEST_PAGE_NO", pno) ;
            }
            if ((action_ == ACT_PAGEBREAK) || (action_ == ACT_KESHIKOMI) || (action_ == ACT_LISTCNT)) {
                // �ˍ��I���`�F�b�N�{�b�N�X
                db2.setStringArray("WLH_PK", wlh_pk) ;
                db2.setStringArray("CB_CHECK_FLG", cb_check_flg) ;
            }
            if (action_ == ACT_LISTCNT) {
                db2.setString("PD_LISTCNT", pd_listCnt) ;
            }
            /*------------------ ���p���f�[�^��DB2�ɐݒ肷�� �����܂� ----------------*/
        }

        db2.setInt("action_", action_) ;

        setError(db1, errors) ;

        printData() ;
        return true ;
    } // checkInput()

/**
 * �Ɩ��������s�Ȃ�.
 *
 * @since �o�[�W���� 1.00
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
                logError("AGSAG01Action#�V�X�e���G���[(accessDB rc:"+rc+")") ;
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
            XXXXUtil.log(XXXXUtil.DEBUG, "�����֑J��") ;
            XXXXDataBean db2_keshikomi = new XXXXDefaultDataBean() ;
            setDataBean("DB2_KESHIKOMI", db2_keshikomi) ;
            setTranId(db1, "AG19") ;
            return ;
        }

        /*------------------ ��ʕ��i�̐ݒ� --------------------------------------*/
        // �G���[
        setError(db1, errors) ;
        setError(db2, errors) ;

        // �}��
        String[] agsubcdList = getLoginAgsubcdList() ;
        String[] pd_agsubcd_val = agsubcdList ;
        String[] pd_agsubcd_dsp = agsubcdList ;
        if (getMastercd().equals(MASTERCD_T)) {
            pd_agsubcd_val = XXXXUtil.addHead(pd_agsubcd_val, AGSUBCD_ALL_VAL) ;
            pd_agsubcd_dsp = XXXXUtil.addHead(pd_agsubcd_dsp, "(�S��)") ;
        }
        db1.setString("MASTERCD", getMastercd());

		db1.setString("OPE_AGSUBCD", getLoginAgsubcd());

        db1.setStringArray("CHK_AGSUBCD",pd_agsubcd_val);

    	// 2006-05-18 �}�ԕ����I�� START
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
    	// 2006-05-18 �}�ԕ����I�� END


        // ���o����
        db1.setPullDown(
            "PD_EXTRACTED",
            PD_EXTRACTED_VAL,
            PD_EXTRACTED_DSP,
            db2.getStringNvl("PD_EXTRACTED", PD_EXTRACTED_VAL[0])
        ) ;
        // �������
        db1.setPullDown(
            "PD_SK_SEG",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_SEIKYUSHUBETSU, "SKS_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_SEIKYUSHUBETSU, "SKS_NAME"), "(�S��)"),
            db2.getStringNvl("PD_SK_SEG")
        ) ;
        // �������
        db1.setPullDown(
            "PD_NK_SEG",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINSHUBETSU, "NKS_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINSHUBETSU, "NKS_NAME"), "(�S��)"),
            db2.getStringNvl("PD_NK_SEG")
        ) ;
        // 2017/12/07 �o�^���P�t�F�[�Y2.5�Ή� CHG START
        // 2006-05-16 �x�����@�E�������@�����ǉ� START
        // �x�����@
        db1.setPullDown(
            "PD_SK_HOUHOU",
            AGSM.getStringArray(AGSM.RS_SK_PAYSEG_CREDIT, "SK_PAYSEG_CREDIT_CD"),
            AGSM.getStringArray(AGSM.RS_SK_PAYSEG_CREDIT, "SK_PAYSEG_CREDIT_NM"),
            db2.getStringNvl("PD_SK_HOUHOU")
        );
        // 2017/12/07 �o�^���P�t�F�[�Y2.5�Ή� CHG END
        // �������@
        db1.setPullDown(
            "PD_NK_HOUHOU",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINHOUHOU, "NY_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_NYUKINHOUHOU, "NY_NAME"), "(�S��)"),
            db2.getStringNvl("PD_NK_HOUHOU")
        );
        // 2006-05-16 �x�����@�E�������@�����ǉ� END
        // �����敪
        db1.setPullDown(
            "PD_TGC",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_CHOUSEIKOUMOKU_ALL, "CS_CD"), ""),
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_CHOUSEIKOUMOKU_ALL, "CS_NAME"), "(�S��)"),
            db2.getStringNvl("PD_TGC")
        ) ;
        // �ˍ��p�^�[��
        db1.setRadioButton(
            "RB_TG_PTN",
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_CD"), ""),
//            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_NAME"), "(�S��)"), //���̕\�� 2004-11-13
//            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_PD_NAME"), "(�S��)"), //���́{���̕\�� 2004-11-13
            XXXXUtil.addHead(AGSM.getStringArray(AGSM.RS_TOTSUGOPATTERN, "TGP_SHORTNAME_MOD"), "(�S��)"), //���̕\�� 2004-12-10
            db2.getStringNvl("RB_TG_PTN")
        ) ;
        // ���������J�n�N����
        // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
        db1.setString("SKFROMYEAR", db2.getStringNvl("SKFROMYEAR")) ;
        db1.setString("SKFROMMONTH", db2.getStringNvl("SKFROMMONTH")) ;
        db1.setPullDown(
                "PD_SKFROMDAY",
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL"),
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_DSP"),
                db2.getStringNvl("PD_SKFROMDAY", AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL")[0])
        ) ;
        // ���������I���N����
        db1.setString("SKTOYEAR", db2.getStringNvl("SKTOYEAR")) ;
        db1.setString("SKTOMONTH", db2.getStringNvl("SKTOMONTH")) ;
        db1.setPullDown(
                "PD_SKTODAY",
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL"),
                AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_DSP"),
                db2.getStringNvl("PD_SKTODAY", AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL")[0])
        ) ;
        // �������J�n�N����
        db1.setString("NKFROMYEAR", db2.getStringNvl("NKFROMYEAR")) ;
        db1.setString("NKFROMMONTH", db2.getStringNvl("NKFROMMONTH")) ;
        db1.setString("NKFROMDAY", db2.getStringNvl("NKFROMDAY")) ;
        // �������I���N����
        db1.setString("NKTOYEAR", db2.getStringNvl("NKTOYEAR")) ;
        db1.setString("NKTOMONTH", db2.getStringNvl("NKTOMONTH")) ;
        db1.setString("NKTODAY", db2.getStringNvl("NKTODAY")) ;
        // 2004-12-15 �����o�^���ǉ� ST
        // �����o�^���J�n�N����
        db1.setString("NKINFROMYEAR", db2.getStringNvl("NKINFROMYEAR")) ;
        db1.setString("NKINFROMMONTH", db2.getStringNvl("NKINFROMMONTH")) ;
        db1.setString("NKINFROMDAY", db2.getStringNvl("NKINFROMDAY")) ;
        // �����o�^���I���N����
        db1.setString("NKINTOYEAR", db2.getStringNvl("NKINTOYEAR")) ;
        db1.setString("NKINTOMONTH", db2.getStringNvl("NKINTOMONTH")) ;
        db1.setString("NKINTODAY", db2.getStringNvl("NKINTODAY")) ;
        // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
        // 2004-12-15 �����o�^���ǉ� EX
        // ���q�l�ԍ�
        db1.setString("USERCD", db2.getStringNvl("USERCD")) ;
        // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
        // �x�����J�n�N����
        db1.setString("NKPAYFROMYEAR", db2.getStringNvl("NKPAYFROMYEAR")) ;
        db1.setString("NKPAYFROMMONTH", db2.getStringNvl("NKPAYFROMMONTH")) ;
        db1.setString("NKPAYFROMDAY", db2.getStringNvl("NKPAYFROMDAY")) ;
        // �x�����I���N����
        db1.setString("NKPAYTOYEAR", db2.getStringNvl("NKPAYTOYEAR")) ;
        db1.setString("NKPAYTOMONTH", db2.getStringNvl("NKPAYTOMONTH")) ;
        db1.setString("NKPAYTODAY", db2.getStringNvl("NKPAYTODAY")) ;
        // �󒍒�~�L��
        db1.setPullDown(
            "PD_RESSTP",
            AGSM.getStringArray(AGSM.RS_PD_RESSTP, "RS_PD_RESSTP_CD"),
            AGSM.getStringArray(AGSM.RS_PD_RESSTP, "RS_PD_RESSTP_NM"),
            db2.getStringNvl("PD_RESSTP")
        );
    	// �󒍒�~���R
    	db1.setString("STOPREASON_312", db2.getStringNvl("STOPREASON_312")) ;
    	db1.setString("STOPREASON_501", db2.getStringNvl("STOPREASON_501")) ;
    	db1.setString("STOPREASON_000", db2.getStringNvl("STOPREASON_000")) ;
    	db1.setString("STOPREASON_963", db2.getStringNvl("STOPREASON_963")) ;
    	db1.setString("STOPREASON_725", db2.getStringNvl("STOPREASON_725")) ;
        // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END
        // �\������
        db1.setPullDown(
            "PD_LISTCNT",
            PD_LISTCNT_VAL,
            PD_LISTCNT_DSP,
            db2.getStringNvl("CURRENT_LISTCNT", PD_LISTCNT_VAL[0])
        ) ;
        // ���ёւ�
        db1.setString("SORT", db2.getStringNvl("SORT", SORT_COLUMN[0])) ;
        db1.setString("ASCEND", db2.getStringNvl("ASCEND", ASCEND[1])) ;

        // �ˍ��I���`�F�b�N�{�b�N�X
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

        /*------------------ ��ʕ��i�̐ݒ� �����܂� -----------------------------*/

		// �Z�b�V�����̃`�F�b�N��Ԃ��N���A����
		db1.setStringArray("_WLH_PK", null) ;
		db1.setStringArray("_CB_CHECK_FLG", null) ;

        // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
    	db1.setString("_STOPREASON_312", "") ;
    	db1.setString("_STOPREASON_501", "") ;
    	db1.setString("_STOPREASON_000", "") ;
    	db1.setString("_STOPREASON_963", "") ;
    	db1.setString("_STOPREASON_725", "") ;
        // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

        // �g�����V�[�P���X��ݒ肷��
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

        // �������s����ƃe�[�u���֒��o
        if ((action_ == ACT_SEARCH) || (action_ == ACT_RETURN) || (action_ == ACT_UPDATE)) {
            rc = accessDB1() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // ���ёւ�
        if ((action_ == ACT_SEARCH) ||
            (action_ == ACT_SORT) ||
            (action_ == ACT_RETURN) ||
            (action_ == ACT_UPDATE)) {
            rc = accessDB2() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // �y�[�W������
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

        // �I���t���O�̍X�V
        if ((action_ == ACT_PAGEBREAK) ||
            (action_ == ACT_KESHIKOMI) ||
            (action_ == ACT_LISTCNT)) {
            rc = accessDB4() ;
            if (rc != 0) {
                return rc ;
            }
        }

        // 1�y�[�W���̎擾
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

/**
 * �������s����ƃe�[�u���֒��o
 */
    private int accessDB1() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#accessDB1()") ;
//        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int rc = -2 ; // Error
        try {
            XXXXDBConnector conn = connect() ;
            XXXXSQLReader sr = XXXXSQLReader.getInstance() ;
            PreparedStatement stmt = null ;

            String sql     = null ;
            XXXXDBParam prm = null ;
            XXXXRowSet rs   = null ;
//            int cnt = 0 ;
            String sessionId = getSessionId() ;
            String agcd = getDataBean("DB2_SYS").getString("AGCD") ;

            boolean sk_flg = false; // ��������������ON 2004/11/26 Add
			boolean nk_flg = false; // ��������������ON 2004/11/26 Add
			boolean AGSUBCD_ALL_Flg; // �}�ԕ����I��     2006/05/31 Add
			boolean isBothCondition;  // ��ԑI��L��    2011.11.10 Add

            /*------------- �������s����ƃe�[�u���֒��o -----------------------------*/
            // 2006-05-17 �}�ԕ����I�� START
			//String agsubcd = db2.getString("PD_AGSUBCD") ;
            String[] agsubcd = db2.getStringArrayNvl("PD_AGSUBCD") ;
            // 2006-05-17 �}�ԕ����I�� END
            int extracted = db2.getIntNvl("PD_EXTRACTED", Integer.parseInt(PD_EXTRACTED_VAL[0])) ;
            String sk_seg = db2.getStringNvl("PD_SK_SEG") ;
            String nk_seg = db2.getStringNvl("PD_NK_SEG") ;
            String tgc = db2.getStringNvl("PD_TGC") ;
            String tg_ptn = db2.getStringNvl("RB_TG_PTN") ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX START
            String skFromYear = db2.getStringNvl("SKFROMYEAR") ;
            String skFromMonth = db2.getStringPad("SKFROMMONTH","0",2) ;
            String skFromDay = db2.getStringNvl("PD_SKFROMDAY", PD_DAY_VAL[0]) ;
            String skToYear = db2.getStringNvl("SKTOYEAR") ;
            String skToMonth = db2.getStringPad("SKTOMONTH","0",2) ;
            String skToDay = db2.getStringNvl("PD_SKTODAY", PD_DAY_VAL[0]) ;
            String nkFromYear = db2.getStringNvl("NKFROMYEAR") ;
            String nkFromMonth = db2.getStringPad("NKFROMMONTH","0",2) ;
            String nkFromDay = db2.getStringPad("NKFROMDAY","0",2) ;
            String nkToYear = db2.getStringNvl("NKTOYEAR") ;
            String nkToMonth = db2.getStringPad("NKTOMONTH","0",2) ;
            String nkToDay = db2.getStringPad("NKTODAY","0",2) ;
            // 2004-12-15 �����o�^���ǉ� ST
            String nkinFromYear = db2.getStringNvl("NKINFROMYEAR") ;
            String nkinFromMonth = db2.getStringPad("NKINFROMMONTH","0",2) ;
            String nkinFromDay = db2.getStringPad("NKINFROMDAY","0",2) ;
            String nkinToYear = db2.getStringNvl("NKINTOYEAR") ;
            String nkinToMonth = db2.getStringPad("NKINTOMONTH","0",2) ;
            String nkinToDay = db2.getStringPad("NKINTODAY","0",2) ;
            // 2005-10-14 ���������󋵈ꗗ�̓��t���͕����ύX END
            // 2004-12-15 �����o�^���ǉ� EX
            // 2006-05-16 �x�����@�E�������@�����ǉ� START
            String sk_houhou = db2.getStringNvl("PD_SK_HOUHOU");
            String nk_houhou = db2.getStringNvl("PD_NK_HOUHOU");
            // 2006-05-16 �x�����@�E�������@�����ǉ� END
            String usercd = db2.getStringNvl("USERCD") ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
            String nkpayFromYear = db2.getStringNvl("NKPAYFROMYEAR") ;
            String nkpayFromMonth = db2.getStringPad("NKPAYFROMMONTH","0",2) ;
            String nkpayFromDay = db2.getStringPad("NKPAYFROMDAY","0",2) ;
            String nkpayToYear = db2.getStringNvl("NKPAYTOYEAR") ;
            String nkpayToMonth = db2.getStringPad("NKPAYTOMONTH","0",2) ;
            String nkpayToDay = db2.getStringPad("NKPAYTODAY","0",2) ;
        	String resstpCd = db2.getStringNvl("PD_RESSTP");
            String resstp = toStopSeg(resstpCd);
            String stopreason = toStopReason(resstpCd,db2.getStringNvl("STOPREASON_312"),db2.getStringNvl("STOPREASON_501"),db2.getStringNvl("STOPREASON_000"),db2.getStringNvl("STOPREASON_963"),db2.getStringNvl("STOPREASON_725"));
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

			// 2004.10.30 �p�t�H�[�}���X�Ή�
			// �Ώی����i�w�b�_�P�ʁj���擾���A���[�N�e�[�u�����쐬���邩�ۂ����f����B

			int total_cnt = 0;

			// ���������̐ݒ�
			prm = getDBParam() ;
			prm.set("SESSION_ID", sessionId) ;
			prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
			prm.set("AGCD", agcd) ;
			// ������
			prm.set("SK_BILLSEG", "") ;
			prm.set("NK_PAYSEG", "") ;
        	// 2006-05-16 �x�����@�E�������@�����ǉ� START
        	prm.set("SK_HOUHOU", "") ;
        	prm.set("NK_HOUHOU", "") ;
        	// 2006-05-16 �x�����@�E�������@�����ǉ� END
			prm.set("TGC_SEG", "") ;
			prm.set("CHOSEI_VALID", "") ;
			prm.set("TG_PTN", "") ;
			prm.set("SK_CHECKDATE_FROM", "") ;
			prm.set("SK_CHECKDATE_TO", "") ;
			prm.set("NK_TRANSFER_DT_FROM", "") ;
			prm.set("NK_TRANSFER_DT_TO", "") ;
            // 2004-12-15 �����o�^���ǉ� ST
            prm.set("NK_INSERT_DT_FROM", "") ;
            prm.set("NK_INSERT_DT_TO", "") ;
            // 2004-12-15 �����o�^���ǉ� EX
			prm.set("USERCD", "") ;
			prm.set("PRM_SK_HOUHOU", "") ;		// 2018/07/26 �Ǝ����U�Ή� ADD
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
            prm.set("NK_PAY_DT_FROM", "") ;
            prm.set("NK_PAY_DT_TO", "") ;
        	prm.set("STOPSEG", "") ;
        	prm.set("STOPREASON", "") ;
        	prm.set("TYPE_OLDMST_CNT", "") ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END
			
			// �������
			prm.set("SK_BILLSEG", sk_seg) ;
			if (sk_seg.length() > 0){
				sk_flg = true;  //�����f�[�^�̌�������ON 2004/11/26
			}

			// �������
			prm.set("NK_PAYSEG", nk_seg) ;
			if (nk_seg.length() > 0){
				nk_flg = true;  //�����f�[�^�̌�������ON 2004/11/26
			}

        	// 2006-05-16 �x�����@�E�������@�����ǉ� START
        	// �x�����@
        	prm.set("SK_HOUHOU", sk_houhou) ;
			if (sk_houhou.length() > 0){
				sk_flg = true;  //�����f�[�^�̌�������ON
				
				// 2018/07/26 �Ǝ����U�Ή� ADD START
				if (sk_houhou.equals("8")) {
					prm.set("PRM_SK_HOUHOU", KIND_PAYSEGNO) ;
				} else {
					prm.set("PRM_SK_HOUHOU", sk_houhou) ;
				}
				// 2018/07/26 �Ǝ����U�Ή� ADD END
			}

        	// �������@
        	prm.set("NK_HOUHOU", nk_houhou) ;
			if (nk_houhou.length() > 0){
				nk_flg = true;  //�����f�[�^�̌�������ON
			}
        	// 2006-05-16 �x�����@�E�������@�����ǉ� END

			// �ˍ���������
			prm.set("TGC_SEG", tgc) ;
			if (tgc.length() > 0) {
				prm.set("CHOSEI_VALID", " ") ;
			}
			// �ˍ��p�^�[��
			prm.set("TG_PTN", tg_ptn) ;
			// ��������
			if (skFromYear.length() > 0) {
				prm.set("SK_CHECKDATE_FROM", AGSUtil.formatDateSP(skFromYear, skFromMonth, skFromDay, AGSUtil.YYYY_MM_DD)) ;
				prm.set("SK_CHECKDATE_TO", AGSUtil.formatDateSP(skToYear, skToMonth, skToDay, AGSUtil.YYYY_MM_DD)) ;
				sk_flg = true; //�����f�[�^�̌�������ON 2004/11/26
			}
			// ������
			if (nkFromYear.length() > 0) {
				prm.set("NK_TRANSFER_DT_FROM", XXXXUtil.formatDate(nkFromYear + nkFromMonth + nkFromDay, AGSUtil.YYYY_MM_DD)) ;
				prm.set("NK_TRANSFER_DT_TO", XXXXUtil.formatDate(nkToYear + nkToMonth + nkToDay, AGSUtil.YYYY_MM_DD)) ;
				nk_flg = true; //�����f�[�^�̌�������ON 2004/11/26
			}
            // 2004-12-15 �����o�^���ǉ� EX
            // �����o�^��
            if (nkinFromYear.length() > 0) {
                prm.set("NK_INSERT_DT_FROM", XXXXUtil.formatDate(nkinFromYear + nkinFromMonth + nkinFromDay, AGSUtil.YYYY_MM_DD)) ;
                prm.set("NK_INSERT_DT_TO", XXXXUtil.formatDate(nkinToYear + nkinToMonth + nkinToDay, AGSUtil.YYYY_MM_DD)) ;
                nk_flg = true; //�����f�[�^�̌�������ON 2004/11/26
            }
            // 2004-12-15 �����o�^���ǉ� EX
			// ���q�l�ԍ�
			prm.set("USERCD", usercd) ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
            // �x����
            if (nkpayFromYear.length() > 0) {
                prm.set("NK_PAY_DT_FROM", XXXXUtil.formatDate(nkpayFromYear + nkpayFromMonth + nkpayFromDay, AGSUtil.YYYY_MM_DD)) ;
                prm.set("NK_PAY_DT_TO", XXXXUtil.formatDate(nkpayToYear + nkpayToMonth + nkpayToDay, AGSUtil.YYYY_MM_DD)) ;
                nk_flg = true; //�����f�[�^�̌�������ON
            }
        	// �󒍒�~�敪�A�󒍒�~���R�������ł���ꍇ�p�����[�^��ON
        	if ((resstp != null && resstp.length() != 0) || (stopreason != null && stopreason.length() != 0)){
	        	prm.set("TYPE_OLDMST_CNT", " ") ;
        	}
            // �󒍒�~�L��
        	prm.set("STOPSEG", resstp) ;
        	// �󒍒�~���R
        	prm.set("STOPREASON", stopreason) ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

			// ���������ɏ]���{�e�[�u�������ƃe�[�u���փ��R�[�h���o
			// �u���ˍ������v
			// ���L�����𖞂����ꍇ�Ɏ擾
			// �E�\���f�[�^�� �S�� or ���ˍ����� or ���ˍ�����/���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�����f�[�^��ʂ� ���I��
			// �E�������ڎ�ʂ� ���I��
			// �E�������� ���I��
        	// �E�x������ ���I��   2019-05-10 ADD
			// �E�\���f�[�^�� ���ˍ�����/���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�������ڎ�ʂ� ���I��
            // �E�����o�^���� ���I�� 2004-12-15
        	// �E�������@�� ���I��   2006-05-16 �]��
			if ((((extracted & EXTRACTED_SK) != 0) &&
				 (tg_ptn.length() == 0 ) &&
				 (nk_seg.length() == 0 ) &&
				// 2006-05-16 �x�����@�E�������@�����ǉ� START
				 (nk_houhou.length() == 0 ) &&
				// 2006-05-16 �x�����@�E�������@�����ǉ� END
				 (tgc.length() == 0 ) &&
				 (nkFromYear.length() == 0 ) &&
                 (nkinFromYear.length() == 0 ) &&
				 (nkpayFromYear.length() == 0 )) ||		// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
				((extracted == EXTRACTED_SKNK) &&
				 (tg_ptn.length() == 0 ) &&
				 (tgc.length() == 0 ))) {
				XXXXUtil.log(XXXXUtil.DEBUG, "���ˍ����������J�E���g") ;
				// �w�b�_
				sql = "AGSAG_workListHeadFromSkC" ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

				rs = executeQuery(sql, prm) ;
				total_cnt = total_cnt + rs.getInt("ROWCNT",0) ;
				commit() ;
			}

			// �u���ˍ������v
			// ���L�����𖞂����ꍇ�Ɏ擾
			// �E�\���f�[�^�� �S�� or ���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�����f�[�^��ʂ� ���I��
			// �E�������ڎ�ʂ� ���I��
			// �E���������� ���I��
			// �E�󒍒�~�L���� �S��     2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
			// �E�\���f�[�^�� ���ˍ�����/���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�������ڎ�ʂ� ���I��
			// �E�󒍒�~�L���� �S��     2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
        	// �E�x�����@�� ���I�� 2006-05-16 �]��
			if ((((extracted & EXTRACTED_NK) != 0) &&
				 (tg_ptn.length() == 0 ) &&
				 (sk_seg.length() == 0 ) &&
				 // 2006-05-16 �x�����@�E�������@�����ǉ� START
				 (sk_houhou.length() == 0 ) &&
				 // 2006-05-16 �x�����@�E�������@�����ǉ� END
				 (tgc.length() == 0 ) &&
				 (skFromYear.length() == 0 ) &&
				 (resstpCd.length() == 0)) ||		// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
				((extracted == EXTRACTED_SKNK) &&
			 	 (tg_ptn.length() == 0 ) &&
			 	 (tgc.length() == 0 ) &&
				 (resstpCd.length() == 0)))  {	// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
				XXXXUtil.log(XXXXUtil.DEBUG, "���ˍ����������J�E���g") ;
				// �w�b�_
				sql = "AGSAG_workListHeadFromNkC" ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

				rs = executeQuery(sql, prm) ;
				total_cnt = total_cnt +  rs.getInt("ROWCNT",0) ;
				commit() ;
			}

			isBothCondition = (extracted & EXTRACTED_TG) != 0
								&& (extracted & EXTRACTED_KS) != 0;


			if ((extracted & EXTRACTED_TG) != 0) {
				// 2011.11.10 �p�t�H�[�}���X���P mod START
				if(isBothCondition) {
					XXXXUtil.log(XXXXUtil.DEBUG, "������(�ˍ���)�A������") ;
				} else {
					XXXXUtil.log(XXXXUtil.DEBUG, "������(�ˍ���)") ;
				}
				// 2011.11.10 �p�t�H�[�}���X���P mod END

				// �w�b�_
				// 2004/11/26 Add ST
				// �����^�����ǂ��炩����̏����̂ݎw�肳��Ă���ꍇ�͏����w�蕪�̂݌�������
				// ���������������w�肵�Ă���ꍇ�A���������������ꍇ�͗�����������
				if((!sk_flg && !nk_flg)){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI";
				}
				else if (sk_flg && !nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK";
				}
				else if (!sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_NK";
				}
				else if (sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK_NK";
				}

				// 2004/11/26 Add EX
//				sql = "AGSAG_workListHeadFromTgC" ;

				// �����p�����[�^��ON 2004/11/26
				prm.set("TYPE_CNT", " ") ;
				prm.set("TYPE_INS", "") ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

				// 2011.11.09 �s�v�\�[�X�폜 START
//				if (getMastercd().equals(MASTERCD_T)) {
//					prm.set("ALL_ON", " ") ;
//					prm.set("ALL_OFF", "") ;
//				}
//				else {
//					// �x�X�͎��g�̃f�[�^�����{���ł��Ȃ�(�ˍ��ς̏ꍇ)
//					prm.set("ALL_ON", "") ;
//					prm.set("ALL_OFF", " ") ;
//				}
				// 2011.11.09 �s�v�\�[�X�폜 END

				// 2011.11.10 �p�t�H�[�}���X���P mod START
				if(!isBothCondition) {
					prm.set("TG_KESHIKOMIFLG", "0") ;
				} else {
					prm.set("TG_KESHIKOMIFLG", "") ;
				}
				// 2011.11.10 �p�t�H�[�}���X���P mod END

				rs = executeQuery(sql, prm) ;
				total_cnt = total_cnt + rs.getInt("ROWCNT",0) ;
				commit() ;
			}

			// 2011.11.10 �p�t�H�[�}���X���P mod START
			if (!isBothCondition && (extracted & EXTRACTED_KS) != 0) {
			// 2011.11.10 �p�t�H�[�}���X���P mod END
				XXXXUtil.log(XXXXUtil.DEBUG, "������") ;
				// �w�b�_

                // 2004/11/26 Add ST
                // �����^�����ǂ��炩����̏����̂ݎw�肳��Ă���ꍇ�͏����w�蕪�̂݌�������
                // ���������������w�肵�Ă���ꍇ�A���������������ꍇ�͗�����������
				if((!sk_flg && !nk_flg)){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI" ;
				}
				else if (sk_flg && !nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK" ;
				}
				else if (!sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_NK" ;
				}
				else if (sk_flg && nk_flg){
                    XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK_NK" ;
				}

				// 2004/11/26 Add EX

//				sql = "AGSAG_workListHeadFromTgC" ;

				// �����p�����[�^��ON 2004/11/26
				prm.set("TYPE_CNT", " ") ;
				prm.set("TYPE_INS", "") ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

				// 2011.11.09 �s�v�\�[�X�폜 START
				// �����ς̏ꍇ�͑��X�̃f�[�^�������
//				prm.set("ALL_ON", " ") ;
//				prm.set("ALL_OFF", "") ;
				// 2011.11.09 �s�v�\�[�X�폜 END

				prm.set("TG_KESHIKOMIFLG", "1") ;
				rs = executeQuery(sql, prm) ;
				total_cnt = total_cnt + rs.getInt("ROWCNT",0) ;
				commit() ;
			}

			XXXXUtil.log(XXXXUtil.DEBUG, "�J�E���g�F" + total_cnt) ;

			if (total_cnt >= SEIKYU_NYUKIN_LIST_MAX_CNT) {
				rollback() ;
				rc = 1 ;
				return rc ;
			}

			// 2004.10.30 �p�t�H�[�}���X�Ή�

            // ����Z�b�V����ID�̍�ƃ��R�[�h�폜
            prm = getDBParam() ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            sql = "AGSAG_workListHeadD" ;
//            cnt = executeUpdate(sql, prm) ;
            executeUpdate(sql, prm) ;
            commit() ;
            sql = "AGSAG_workListSkD" ;
//          cnt = executeUpdate(sql, prm) ;
            executeUpdate(sql, prm) ;
            commit() ;
            sql = "AGSAG_workListNkD" ;
//          cnt = executeUpdate(sql, prm) ;
            executeUpdate(sql, prm) ;
            commit() ;

            // ���������̐ݒ�
            prm = getDBParam() ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            prm.set("AGCD", agcd) ;
            // ������
            prm.set("SK_BILLSEG", "") ;
            prm.set("NK_PAYSEG", "") ;
            // 2006-05-16 �x�����@�E�������@�����ǉ� START
        	prm.set("SK_HOUHOU", "") ;
        	prm.set("NK_HOUHOU", "") ;
        	// 2006-05-16 �x�����@�E�������@�����ǉ� END
            prm.set("TGC_SEG", "") ;
            prm.set("CHOSEI_VALID", "") ;
            prm.set("TG_PTN", "") ;
            prm.set("SK_CHECKDATE_FROM", "") ;
            prm.set("SK_CHECKDATE_TO", "") ;
            prm.set("NK_TRANSFER_DT_FROM", "") ;
            prm.set("NK_TRANSFER_DT_TO", "") ;
            // 2004-12-15 �����o�^���ǉ� ST
            prm.set("NK_INSERT_DT_FROM", "") ;
            prm.set("NK_INSERT_DT_TO", "") ;
            // 2004-12-15 �����o�^���ǉ� EX
            prm.set("USERCD", "") ;
        	prm.set("PRM_SK_HOUHOU", "") ;		// 2018/07/26 �Ǝ����U�Ή� ADD
            // �������
            prm.set("SK_BILLSEG", sk_seg) ;
            // �������
            prm.set("NK_PAYSEG", nk_seg) ;
        	// 2006-05-16 �x�����@�E�������@�����ǉ� START
        	prm.set("SK_HOUHOU", sk_houhou) ;
			// 2018/07/26 �Ǝ����U�Ή� ADD START
            if (sk_houhou.length() > 0) {
            	if (sk_houhou.equals("8")) {
            		prm.set("PRM_SK_HOUHOU", KIND_PAYSEGNO) ;
            	} else {
            		prm.set("PRM_SK_HOUHOU", sk_houhou) ;
            	}
            }
			// 2018/07/26 �Ǝ����U�Ή� ADD END
        	prm.set("NK_HOUHOU", nk_houhou) ;
        	// 2006-05-16 �x�����@�E�������@�����ǉ� END
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
            prm.set("NK_PAY_DT_FROM", "") ;
            prm.set("NK_PAY_DT_TO", "") ;
        	prm.set("STOPSEG", "") ;
        	prm.set("STOPREASON", "") ;
        	prm.set("TYPE_OLDMST_CNT", "") ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END
            // �ˍ���������
            prm.set("TGC_SEG", tgc) ;
            if (tgc.length() > 0) {
                prm.set("CHOSEI_VALID", " ") ;
            }
            // �ˍ��p�^�[��
            prm.set("TG_PTN", tg_ptn) ;
            // ��������
            if (skFromYear.length() > 0) {
                prm.set("SK_CHECKDATE_FROM", AGSUtil.formatDateSP(skFromYear, skFromMonth, skFromDay, AGSUtil.YYYY_MM_DD)) ;
                prm.set("SK_CHECKDATE_TO", AGSUtil.formatDateSP(skToYear, skToMonth, skToDay, AGSUtil.YYYY_MM_DD)) ;
            }
            // ������
            if (nkFromYear.length() > 0) {
                prm.set("NK_TRANSFER_DT_FROM", XXXXUtil.formatDate(nkFromYear + nkFromMonth + nkFromDay, AGSUtil.YYYY_MM_DD)) ;
                prm.set("NK_TRANSFER_DT_TO", XXXXUtil.formatDate(nkToYear + nkToMonth + nkToDay, AGSUtil.YYYY_MM_DD)) ;
            }
            // 2004-12-15 �����o�^���ǉ� ST
            // �����o�^��
            if (nkinFromYear.length() > 0) {
                prm.set("NK_INSERT_DT_FROM", XXXXUtil.formatDate(nkinFromYear + nkinFromMonth + nkinFromDay, AGSUtil.YYYY_MM_DD)) ;
                prm.set("NK_INSERT_DT_TO", XXXXUtil.formatDate(nkinToYear + nkinToMonth + nkinToDay, AGSUtil.YYYY_MM_DD)) ;
            }
            // 2004-12-15 �����o�^���ǉ� ST
            // ���q�l�ԍ�
            prm.set("USERCD", usercd) ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
            // �x����
            if (nkpayFromYear.length() > 0) {
                prm.set("NK_PAY_DT_FROM", XXXXUtil.formatDate(nkpayFromYear + nkpayFromMonth + nkpayFromDay, AGSUtil.YYYY_MM_DD)) ;
                prm.set("NK_PAY_DT_TO", XXXXUtil.formatDate(nkpayToYear + nkpayToMonth + nkpayToDay, AGSUtil.YYYY_MM_DD)) ;
            }
        	// �󒍒�~�敪�A�󒍒�~���R�������ł���ꍇ�p�����[�^��ON
        	if ((resstp != null && resstp.length() != 0) || (stopreason != null && stopreason.length() != 0)){
	        	prm.set("TYPE_OLDMST_CNT", " ") ;
        	}
        	// �󒍒�~�L��
        	prm.set("STOPSEG", resstp) ;
        	// �󒍒�~���R
        	prm.set("STOPREASON", stopreason) ;
            // 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

			// �u���ˍ������v
			// ���L�����𖞂����ꍇ�Ɏ擾
			// �E�\���f�[�^�� �S�� or ���ˍ����� or ���ˍ�����/���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�����f�[�^��ʂ� ���I��
			// �E�������ڎ�ʂ� ���I��
			// �E�������� ���I��
            // �E�����o�^���� ���I�� 2004-12-15
        	// �E�x������ ���I�� 2019-05-10 ADD
			// �E�\���f�[�^�� ���ˍ�����/���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�������ڎ�ʂ� ���I��
        	// �E�������@�� ���I�� 2006-05-16 �]��
			if ((((extracted & EXTRACTED_SK) != 0) &&
				 (tg_ptn.length() == 0 ) &&
				 (nk_seg.length() == 0 ) &&
				 // 2006-05-16 �x�����@�E�������@�����ǉ� START
				 (nk_houhou.length() == 0 ) &&
				 // 2006-05-16 �x�����@�E�������@�����ǉ� END
				 (tgc.length() == 0 ) &&
				 (nkFromYear.length() == 0 ) &&
                 (nkinFromYear.length() == 0 ) &&
				 (nkpayFromYear.length() == 0 )) ||		// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
				((extracted == EXTRACTED_SKNK) &&
				 (tg_ptn.length() == 0 ) &&
				 (tgc.length() == 0 ))) {
                XXXXUtil.log(XXXXUtil.DEBUG, "���ˍ���������") ;
                // �w�b�_
                sql = "AGSAG_workListHeadFromSkI" ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

//	            cnt = executeUpdate(sql, prm) ;
	            executeUpdate(sql, prm) ;
                commit() ;
                // ����
                sql = "AGSAG_workListSkI" ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
            }

			// �u���ˍ������v
			// ���L�����𖞂����ꍇ�Ɏ擾
			// �E�\���f�[�^�� �S�� or ���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�����f�[�^��ʂ� ���I��
			// �E�������ڎ�ʂ� ���I��
			// �E���������� ���I��
			// �E�󒍒�~�L���� �S��     2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
			// �E�\���f�[�^�� ���ˍ�����/���ˍ�����
			// �E�ˍ��p�^�[���� �S��
			// �E�������ڎ�ʂ� ���I��
        	// �E�x�����@�� ���I�� 2006-05-16 �]��
			// �E�󒍒�~�L���� �S��     2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
			if ((((extracted & EXTRACTED_NK) != 0) &&
				 (tg_ptn.length() == 0 ) &&
				 (sk_seg.length() == 0 ) &&
				 // 2006-05-16 �x�����@�E�������@�����ǉ� START
				 (sk_houhou.length() == 0 ) &&
				 // 2006-05-16 �x�����@�E�������@�����ǉ� END
				 (tgc.length() == 0 ) &&
				 (skFromYear.length() == 0 ) &&
				 (resstpCd.length() == 0 )) ||		// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
				((extracted == EXTRACTED_SKNK) &&
				 (tg_ptn.length() == 0 ) &&
				 (tgc.length() == 0 ) &&
				 (resstpCd.length() == 0 )))  {	// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD
                XXXXUtil.log(XXXXUtil.DEBUG, "���ˍ���������") ;
                // �w�b�_
                sql = "AGSAG_workListHeadFromNkI" ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END


//	            cnt = executeUpdate(sql, prm) ;
	            executeUpdate(sql, prm) ;
                commit() ;
                // ����
                sql = "AGSAG_workListNkI" ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
            }

            if ((extracted & EXTRACTED_TG) != 0) {
				// 2011.11.10 �p�t�H�[�}���X���P mod START
				if(isBothCondition) {
					XXXXUtil.log(XXXXUtil.DEBUG, "������(�ˍ���)�A������") ;
				} else {
					XXXXUtil.log(XXXXUtil.DEBUG, "������(�ˍ���)") ;
				}
				// 2011.11.10 �p�t�H�[�}���X���P mod END

                // �w�b�_
				// 2004/11/26 Add ST
				// �����^�����ǂ��炩����̏����̂ݎw�肳��Ă���ꍇ�͏����w�蕪�̂݌�������
				// ���������������w�肵�Ă���ꍇ�A���������������ꍇ�͗�����������
				if((!sk_flg && !nk_flg)){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI" ;
				}
				else if (sk_flg && !nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK" ;
				}
				else if (!sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_NK" ;
				}
				else if (sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK_NK" ;
				}

				// 2004/11/26 Add EX
//				sql = "AGSAG_workListHeadFromTgI" ;

				// INSERT�p�����[�^��ON 2004/11/26
				prm.set("TYPE_CNT", "") ;
				prm.set("TYPE_INS", " ") ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

				// 2011.11.09 �s�v�\�[�X�폜 START
//                if (getMastercd().equals(MASTERCD_T)) {
//                    prm.set("ALL_ON", " ") ;
//                    prm.set("ALL_OFF", "") ;
//                }
//                else {
//                    // �x�X�͎��g�̃f�[�^�����{���ł��Ȃ�(�ˍ��ς̏ꍇ)
//                    prm.set("ALL_ON", "") ;
//                    prm.set("ALL_OFF", " ") ;
//                }
				// 2011.11.09 �s�v�\�[�X�폜 END

				// 2011.11.10 �p�t�H�[�}���X���P mod START
				if(!isBothCondition) {
					prm.set("TG_KESHIKOMIFLG", "0") ;
				} else {
					prm.set("TG_KESHIKOMIFLG", "") ;
				}
				// 2011.11.10 �p�t�H�[�}���X���P mod END

//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
            }

			// 2011.11.10 �p�t�H�[�}���X���P mod START
            if (!isBothCondition && (extracted & EXTRACTED_KS) != 0) {
			// 2011.11.10 �p�t�H�[�}���X���P mod END
                XXXXUtil.log(XXXXUtil.DEBUG, "������") ;
                // �w�b�_
				// 2004/11/26 Add ST
				// �����^�����ǂ��炩����̏����̂ݎw�肳��Ă���ꍇ�͏����w�蕪�̂݌�������
				// ���������������w�肵�Ă���ꍇ�A���������������ꍇ�͗�����������
				if((!sk_flg && !nk_flg)){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI" ;
				}
				else if (sk_flg && !nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK" ;
				}
				else if (!sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_NK" ;
				}
				else if (sk_flg && nk_flg){
					XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action ��������");
					sql = "AGSAG_workListHeadFromTgI_SK_NK" ;
				}

				// 2004/11/26 Add EX
//                sql = "AGSAG_workListHeadFromTgI" ;

				// INSERT�p�����[�^��ON 2004/11/26
				prm.set("TYPE_CNT", "") ;
				prm.set("TYPE_INS", " ") ;

				// ���� �����x�X���� ����
                // 2006-05-16 �x�����@�E�������@�����ǉ� START
				//else {
				//	    prm.set("AGSUBCD", new String[]{agsubcd}, true) ;
				//}
				AGSUBCD_ALL_Flg = false;
				for(int i = 0; i < agsubcd.length; i++) {
					if (agsubcd[i].equals(AGSUBCD_ALL_VAL)) {
						prm.set("AGSUBCD", "");
						AGSUBCD_ALL_Flg = true;
						break;
					}
				}
				if(!AGSUBCD_ALL_Flg) {
					prm.set("AGSUBCD", agsubcd, true) ;
				}
                // 2006-05-16 �x�����@�E�������@�����ǉ� END

				// 2011.11.09 �s�v�\�[�X�폜 START
                // �����ς̏ꍇ�͑��X�̃f�[�^�������
//                prm.set("ALL_ON", " ") ;
//                prm.set("ALL_OFF", "") ;
				// 2011.11.09 �s�v�\�[�X�폜 END

                prm.set("TG_KESHIKOMIFLG", "1") ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
            }
            if (((extracted & EXTRACTED_TG) != 0) || ((extracted & EXTRACTED_KS) != 0)) {
                // ����
                sql = "AGSAG_workListSkFromTgI" ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
                // ����(������)
                sql = "AGSAG_workListSkFromTgcI" ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
                // ����
                sql = "AGSAG_workListNkFromTgI" ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
                // ����(������)
                sql = "AGSAG_workListNkFromTgcI" ;
//              cnt = executeUpdate(sql, prm) ;
                executeUpdate(sql, prm) ;
                commit() ;
            }


            // �ˍ����̃T�}���[����
            sql = "AGSAG_workListSumS" ;
            prm = getDBParam() ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            rs = executeQuery(sql, prm) ;
             total_cnt = rs.getRowCount() ;
            db2.setString("TOTAL_CNT", Integer.toString(total_cnt)) ;

/* �p�t�H�[�}���X�Ή�
            if (total_cnt >= SEIKYU_NYUKIN_LIST_MAX_CNT) {
                rollback() ;
                rc = 1 ;
                return rc ;
            }
*/
            commit() ;

            // PreparedStatement
            XXXXUtil.log(XXXXUtil.DEBUG, "�ˍ����̃T�}���[�X�V start") ;
            prm = getDBParam() ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            sql = sr.getSQL("AGSAG_workListHeadSumU", prm) ;
            stmt = conn.openPreparedStatement(sql) ;
            for (int i = 0 ; i < total_cnt ; i++) {
                stmt.setInt(1, rs.getInt("SK_LEN", i)) ;
                stmt.setInt(2, rs.getInt("NK_LEN", i)) ;
                stmt.setInt(3, rs.getInt("SK_COUNTSUM", i)) ;
                stmt.setLong(4, rs.getLong("SK_BILLSUM", i)) ;
                stmt.setLong(5, rs.getLong("SK_CHECKEDSUM", i)) ;
                stmt.setLong(6, rs.getLong("SK_NOCHECKEDSUM", i)) ;
                stmt.setInt(7, rs.getInt("NK_COUNTSUM", i)) ;
                stmt.setLong(8, rs.getLong("NK_DEBITSUM", i)) ;
                stmt.setLong(9, rs.getLong("NK_CHECKEDSUM", i)) ;
                stmt.setLong(10, rs.getLong("NK_NOCHECKEDSUM", i)) ;
                stmt.setInt(11, rs.getInt("WLH_PK", i)) ;
                stmt.setString(12, rs.getString("WLH_TYPE", i)) ;
//              cnt = stmt.executeUpdate() ;
                stmt.executeUpdate();
            }
            conn.closePreparedStatement() ;
            XXXXUtil.log(XXXXUtil.DEBUG, "�ˍ����̃T�}���[�X�V end") ;
            /*------------- �������s����ƃe�[�u���֒��o �����܂� --------------------*/

            commit() ;
            rc = 0 ;
        }
        catch (Exception e) {
            logException("AGSAG01Action#accessDB1()#Exception", e) ;
            rollback() ;
            rc = -1 ;
        }
        finally {
            close() ;
        }
        return rc ;
    } // accessDB1()

/**
 * ���ёւ�
 */
    private int accessDB2() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#accessDB2()") ;
//        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int rc = -2 ; // Error
        try {
            XXXXDBConnector conn = connect() ;
            XXXXSQLReader sr = XXXXSQLReader.getInstance() ;
            PreparedStatement stmt = null ;

            String sql     = null ;
            XXXXDBParam prm = null ;
            XXXXRowSet rs   = null ;
//            int cnt = 0 ;
            String sessionId = getSessionId() ;
//            String agcd = getDataBean("DB2_SYS").getString("AGCD") ;

            /*------------------ ���ёւ� --------------------------------------------*/
            String sort = db2.getStringNvl("SORT", SORT_COLUMN[0]) ;
            String ascend = db2.getStringNvl("ASCEND", ASCEND[1]) ;

            if (sort.indexOf("SK") == 0) {
                sql = "AGSAG_workListSortSk01S" ;
            }
            else {
                sql = "AGSAG_workListSortNk01S" ;
            }
            prm = getDBParam() ;
            // ����������
            int len = SORT_COLUMN.length ;
            for (int i = 0 ; i < len ; i++) {
                prm.set("SORT_" + SORT_COLUMN[i] + "_ASC", "") ;
                prm.set("SORT_" + SORT_COLUMN[i] + "_DESC", "") ;
            }
            // �����Z�b�g
            prm.set("SORT_" + sort + "_" + ascend, " ") ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            rs = executeQuery(sql, prm) ;
            len = rs.getRowCount() ;

            XXXXUtil.log(XXXXUtil.DEBUG, "����len=", len) ;

            // �\�[�g�����X�V����
            // PreparedStatement
            XXXXUtil.log(XXXXUtil.DEBUG, "�\�[�g���̍X�V start") ;
            prm = getDBParam() ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            sql = sr.getSQL("AGSAG_workListHeadSortU", prm) ;
            XXXXUtil.log(XXXXUtil.SQL, sql) ;
            stmt = conn.openPreparedStatement(sql) ;
            for (int i = 0 ; i < len ; i++) {
                stmt.setInt(1, i + 1) ;
                stmt.setInt(2, rs.getInt("WLH_PK", i)) ;
                stmt.setString(3, rs.getString("WLH_TYPE", i)) ;
//                cnt = stmt.executeUpdate() ;
                stmt.executeUpdate() ;
            }
            conn.closePreparedStatement() ;
            XXXXUtil.log(XXXXUtil.DEBUG, "�\�[�g���̍X�V end") ;
            /*------------------ ���ёւ� �����܂� -----------------------------------*/

            commit() ;
            rc = 0 ;
        }
        catch (Exception e) {
            logException("AGSAG01Action#accessDB2()#Exception", e) ;
            rollback() ;
            rc = -1 ;
        }
        finally {
            close() ;
        }
        return rc ;
    } // accessDB2()

/**
 * �y�[�W������
 */
    private int accessDB3() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#accessDB3()") ;
//        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int action_ ;
        action_ = db2.getInt("action_") ;

        /*------------------ �y�[�W������ ----------------------------------------*/
        // 1�y�[�W�ɕ\�����錏��
        int listCnt = db2.getIntNvl("PD_LISTCNT", Integer.parseInt(PD_LISTCNT_VAL[0])) ;
        int total_cnt = db2.getIntNvl("TOTAL_CNT", 0) ;

        int current_page_no = 0 ;
/* 2005-01-21 DEL ST
        int first_page_no = 0 ;
        int last_page_no = 0 ;
*/
        // 2005-01-21 ST
        int first_page_no = 1 ;
        int last_page_no = 1 ;
        // 2005-01-21 EX

        if (total_cnt > 0) {
            current_page_no = 1 ;
            first_page_no = 1 ;
            last_page_no = total_cnt / listCnt ;
            if (total_cnt % listCnt > 0) {
                last_page_no++ ;
            }
        }
        db2.setString("CURRENT_LISTCNT", Integer.toString(listCnt)) ;
        db2.setString("CURRENT_PAGE_NO", Integer.toString(current_page_no)) ;
        if (action_ != ACT_RETURN) {
            db2.setString("REQUEST_PAGE_NO", Integer.toString(first_page_no)) ;
        }
        db2.setString("FIRST_PAGE_NO", Integer.toString(first_page_no)) ;
        db2.setString("LAST_PAGE_NO", Integer.toString(last_page_no)) ;
        /*------------------ �y�[�W������ �����܂� -------------------------------*/

        return 0 ;
    } // accessDB3()

/**
 * �I���t���O�̍X�V
 */
    private int accessDB4() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#accessDB4()") ;
        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int rc = -2 ; // Error
        try {
            XXXXDBConnector conn = connect() ;
            XXXXSQLReader sr = XXXXSQLReader.getInstance() ;
            PreparedStatement stmt = null ;

            String sql     = null ;
            XXXXDBParam prm = null ;
//            XXXXRowSet rs   = null ;
//            int cnt = 0 ;
            String sessionId = getSessionId() ;
//            String agcd = getDataBean("DB2_SYS").getString("AGCD") ;

            /*------------------ �I���t���O�̍X�V ------------------------------------*/
            String[] wlh_pk = db2.getStringArrayNvl("WLH_PK", new String[0]) ;
            int len = wlh_pk.length ;
            String[] cb_check_flg = db2.getStringArrayNvl2("CB_CHECK_FLG", len, "0") ;

            prm = getDBParam() ;
            prm.set("SESSION_ID", sessionId) ;
            prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
            sql = sr.getSQL("AGSAG_workListHeadSelU", prm) ;
            XXXXUtil.log(XXXXUtil.SQL, sql) ;
            stmt = conn.openPreparedStatement(sql) ;
            for (int i = 0 ; i < len ; i++) {
                if ((wlh_pk[i] != null) && (wlh_pk[i].length() > 0)) {
                    stmt.setString(1, cb_check_flg[i]) ;
                    stmt.setInt(2, Integer.parseInt(wlh_pk[i])) ;
//                    cnt = stmt.executeUpdate() ;
                    stmt.executeUpdate() ;
                }
            }
            conn.closePreparedStatement() ;

            // �Z�b�V�����̃`�F�b�N��Ԃ��N���A����
            db2.setStringArray("WLH_PK", null) ;
            db2.setStringArray("CB_CHECK_FLG", null) ;

			db1.setStringArray("_WLH_PK", null) ;
			db1.setStringArray("_CB_CHECK_FLG", null) ;


            /*------------------ �I���t���O�̍X�V �����܂� ---------------------------*/

            commit() ;
            rc = 0 ;
        }
        catch (Exception e) {
            logException("AGSAG01Action#accessDB4()#Exception", e) ;
            rollback() ;
            rc = -1 ;
        }
        finally {
            close() ;
        }
        return rc ;
    } // accessDB4()

/**
 * 1�y�[�W���̎擾
 */
    private int accessDB5() {
        XXXXUtil.log(XXXXUtil.DEBUG, "AGSAG01Action#accessDB5()") ;
        XXXXDataBean db1 = getDataBean() ;
        XXXXDataBean db2 = getDataBean("DB2_AG01") ;

        int rc = -2 ; // Error
        try {
//            XXXXDBConnector conn = connect() ;
        	connect() ;
//            XXXXSQLReader sr = XXXXSQLReader.getInstance() ;
//            PreparedStatement stmt = null ;

            String sql     = null ;
            XXXXDBParam prm = null ;
            XXXXRowSet rs   = null ;
//            int cnt = 0 ;
            String sessionId = getSessionId() ;
//            String agcd = getDataBean("DB2_SYS").getString("AGCD") ;

            /*------------------ 1�y�[�W���̎擾 -------------------------------------*/
            int current_listcnt = db2.getIntNvl("CURRENT_LISTCNT", 0) ;
//            int current_page_no = db2.getIntNvl("CURRENT_PAGE_NO", 0) ;
            int request_page_no = db2.getIntNvl("REQUEST_PAGE_NO", 0) ;
            int first_page_no = db2.getIntNvl("FIRST_PAGE_NO", 0) ;
            int last_page_no = db2.getIntNvl("LAST_PAGE_NO", 0) ;
            int total_cnt = db2.getIntNvl("TOTAL_CNT", 0) ;

            int start_no = 0 ;
            int end_no = 0 ;
            int current_row_count = 0 ;

            if (total_cnt > 0) {
                start_no = (request_page_no - 1) * current_listcnt + 1 ;
                end_no = Math.min(start_no + current_listcnt - 1, total_cnt) ;

                // 1�y�[�W�����擾(�s�w�b�_)
                sql = "AGSAG_workListHeadS" ;
                prm = getDBParam() ;
                prm.set("SESSION_ID", sessionId) ;
                prm.set("WLH_LISTTYPE", W_TYPE_DISP) ;
                prm.set("START_NO", start_no) ;
                prm.set("END_NO", end_no) ;
                rs = executeQuery(sql, prm) ;
                db1.setRS("RS_WORK_HEAD", rs) ;
				// 2004.10.30 �Z�L�����e�B�Ή�
				db2.setRS("RS_WORK_HEAD", rs) ;
				// 2004.10.30 �Z�L�����e�B�Ή�
                // ������
                sql = "AGSAG_workListSkS" ;
                rs = executeQuery(sql, prm) ;
                db1.setRS("RS_WORK_SK", rs) ;
				// 2004.10.30 �Z�L�����e�B�Ή�
				db2.setRS("RS_WORK_SK", rs) ;
				// 2004.10.30 �Z�L�����e�B�Ή�
                // ������
                sql = "AGSAG_workListNkS" ;
                rs = executeQuery(sql, prm) ;
                db1.setRS("RS_WORK_NK", rs) ;
				// 2004.10.30 �Z�L�����e�B�Ή�
				db2.setRS("RS_WORK_NK", rs) ;
				// 2004.10.30 �Z�L�����e�B�Ή�
                // �������T�}���[
                sql = "AGSAG_workListSkSumS" ;
                rs = executeQuery(sql, prm) ;
                db1.setString("SK_BILL_TOTAL", rs.getString("SK_BILL_TOTAL", 0)) ;
                db1.setString("SK_CHECKEDSUM_TOTAL", rs.getString("SK_CHECKEDSUM_TOTAL", 0)) ;
                db1.setString("SK_SUM_TOTAL", rs.getString("SK_SUM_TOTAL", 0)) ;
                db1.setString("SK_MISYORI_TOTAL", rs.getString("SK_MISYORI_TOTAL", 0)) ;
                // �������T�}���[
                sql = "AGSAG_workListNkSumS" ;
                rs = executeQuery(sql, prm) ;
                db1.setString("NK_DEBIT_TOTAL", rs.getString("NK_DEBIT_TOTAL", 0)) ;
                db1.setString("NK_CHECKEDSUM_TOTAL", rs.getString("NK_CHECKEDSUM_TOTAL", 0)) ;
                db1.setString("NK_SUM_TOTAL", rs.getString("NK_SUM_TOTAL", 0)) ;
                db1.setString("NK_MISYORI_TOTAL", rs.getString("NK_MISYORI_TOTAL", 0)) ;
            }
            else {
                rs = new XXXXDefaultRowSet() ;
                db1.setRS("RS_WORK_HEAD", rs) ;
                db1.setRS("RS_WORK_SK", rs) ;
                db1.setRS("RS_WORK_NK", rs) ;
            }

            db2.setString("CURRENT_PAGE_NO", Integer.toString(request_page_no)) ;

            db1.setInt("_LIST_MAX_ROW_IN_PAGE", current_listcnt) ;
            db1.setInt("_LIST_CURRENT_PAGE_NO", request_page_no) ;
            db1.setInt("_LIST_FIRST_PAGE_NO", first_page_no) ;
            db1.setInt("_LIST_LAST_PAGE_NO", last_page_no) ;
            db1.setInt("_LIST_TOTAL_ROW_COUNT", total_cnt) ;
            db1.setInt("_LIST_ROW_COUNT", current_row_count) ;

            // �������ʂ����邩�̃t���O
            db2.setString("LIST_VALID_FLG", "1") ;
            /*------------------ 1�y�[�W���̎擾 �����܂� ----------------------------*/

            commit() ;
            rc = 0 ;
        }
        catch (Exception e) {
            logException("AGSAG01Action#accessDB5()#Exception", e) ;
            rollback() ;
            rc = -1 ;
        }
        finally {
            close() ;
        }
        return rc ;
    } // accessDB5()
	
	// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
    /**
     * <p>�󒍒�~�敪DB����������𐶐����܂��B</p>
     * 
     * @param stop
     */
    private String toStopSeg(String stop) {
        String where = null;
        if ("1".equals(stop)) {
            where = " where a.STOPSEG = '1'";
        } else if ("2".equals(stop)) {
            where = " where a.STOPSEG = ''";
        }
        return where;
    }
    /**
     * <p>�󒍒�~���RDB����������𐶐����܂��B</p>
     * 
     * @param stop
     */
    private String toStopReason(String selresstp, String sr312, String sr501, String sr000, String sr963, String sr725) {
        String where = null;

    	//�󒍒�~�敪(���͒l)���u�S�āv�܂��́u���v�̏ꍇ�̓`�F�b�N���Ȃ�
    	if ( selresstp == null || selresstp.length() == 0 || selresstp.equals("2")) {
	        return where;
    	} else {
	    	boolean STOPREASON_ALL0 = true;		//�S�I������Ă��Ȃ�
    		boolean STOPREASON_ALL1 = true;		//�S�I������Ă���
	    	boolean STOPREASON_000 = true;
	    	String[] STOPREASON_CD = {"312","501","000","963","725"} ;
	    	String[] STOPREASON_FLG = {"0","0","0","0","0"} ;

	        if (sr312.equals(STOPREASON_CD[0]) && sr312.length() != 0) { STOPREASON_FLG[0] = "1"; }
	        if (sr501.equals(STOPREASON_CD[1]) && sr501.length() != 0) { STOPREASON_FLG[1] = "1"; }
	        if (sr000.equals(STOPREASON_CD[2]) && sr000.length() != 0) { STOPREASON_FLG[2] = "1"; }
	    	if (sr963.equals(STOPREASON_CD[3]) && sr963.length() != 0) { STOPREASON_FLG[3] = "1"; }
	        if (sr725.equals(STOPREASON_CD[4]) && sr725.length() != 0) { STOPREASON_FLG[4] = "1"; }

	        //�S�I������Ă��Ȃ����ASQL������
			for(int i = 0; i < STOPREASON_CD.length; i++) {
				if (!STOPREASON_FLG[i].equals("0")) {
					STOPREASON_ALL0 = false;
					break;
				}
			}
	        //�S�I������Ă��鎞�ASQL������
			for(int i = 0; i < STOPREASON_CD.length; i++) {
				if (!STOPREASON_FLG[i].equals("1")) {
					STOPREASON_ALL1 = false;
					break;
				}
			}
    		
    		if (STOPREASON_ALL0 || STOPREASON_ALL1)  {
	            return where;
			}

	        //�󒍒�~�敪(���͒l)���I�����Ă��邩
	    	if ( selresstp == null || selresstp.length() == 0 ) {
	    		where = " where a.STOPREASON ";
	    	} else {
	    		where = " and a.STOPREASON ";
	    	}
	    	//�u���L�ȊO�S�v�̃`�F�b�N�ɂ��N�G�������ς��̂�
	    	if ( STOPREASON_FLG[2].equals("1") ) {
	    		where = where + " not in (";

	    		//�u���L�ȊO�S�v�̂݃`�F�b�N����Ă��邩
				for(int i = 0; i < STOPREASON_CD.length; i++) {
					if (i != 2) {
						if (!STOPREASON_FLG[i].equals("1")) {
							STOPREASON_000 = false;
						}
					}
				}
	    	} else {
	    		where = where + " in (";
	    	}
			//�`�F�b�N����̒l�ŃN�G���쐬
			for(int i = 0; i < STOPREASON_CD.length; i++) {
				if (STOPREASON_000) {
					if (i != 2) {
						if (STOPREASON_FLG[i].equals("1")) {
							where = where + " '" + STOPREASON_CD[i] + "',";
						}
					}
				} else {
					if (i != 2) {
						if (STOPREASON_FLG[i].equals("0")) {
							where = where + " '" + STOPREASON_CD[i] + "',";
						}
					}
				}
			}

	   		//�Ō�́u,�v������
			where = where.substring(0, where.length()-1) + " )";
	        return where;
	    }
    }
	// 2019/05/10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END
}

