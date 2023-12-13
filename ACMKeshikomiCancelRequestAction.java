/*
 * Project Name : ������WEB-AGS
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
 * <strong>���������@�\�^���͉��(AB08)</strong>
 * <p>
 * ���������@�\���͉�ʂ�\�����܂��B
 * 
 * @author ITF�K��i��
 * @version 1.00 2006/08/08 ITF�K��i��
 * @since �o�[�W���� 1.00
 */
public class ACMKeshikomiCancelRequestAction extends AgsActionUtility {

    /**
     * �Ǘ��Ҍ�����\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String CONST_ADMIN = "12";

    /**
     * ���������@�\�̃Z�b�V�����I�u�W�F�N�g��<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_ACM_AB08 = "DB2_ACMAB_KESHIKOMICANCEL";

    /**
     * �Ǘ��Ҍ����t���O��ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String KEY_CLCTBISFLG = "CLCTBISFLG";

    /**
     * �V�[�P���X��ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String KEY_SEQ = "SEQ";

    /**
     * ���͗��\���s����ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String KEY_CANCELROW = "CANCELROW";

    /**
     * �O����͒l�̂��₢���킹�ԍ���<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_TMP_USERCODE = "TMP_USERCODE";

    /**
     * �O����͒l�̐�������(�N)��<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_TMP_SKYEAR = "TMP_SKYEAR";

    /**
     * �O����͒l�̐�������(��)��<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_TMP_SKMONTH = "TMP_SKMONTH";

    /**
     * �O����͒l�̐�������(��)��<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_TMP_SKDAY = "TMP_SKDAY";

    /**
     * �G���[�s�ł��鎖�������l��<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_TMP_ISERROR = "TMP_ISERROR";

    // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD START
    /**
     * �O����͒l�̓s�x�J�[�h���ϕ���������������^���Ȃ��̃`�F�b�N�l��<br>
     * �ݒ�^�擾����ׂ̃}�b�s���O�L�[��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String
            KEY_TMP_CARDRELEASE = "TMP_CARDRELEASE";
    // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD END

    /**
     * �v���p�e�B�t�@�C��������͗��\���s����<br>
     * �擾����ׂ̃L�[��\��������萔
     * 
     * <pre>
     * �v���p�e�B�t�@�C��
     *   /WEB-INF/classes/ASKULResource_ja_JP.properties</pre>
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String PROPERTY_CANCELROW = "F_CANCEL_ROW";

    /**
     * �g�����U�N�V����ID:EA912��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String TID_EA912 = "EA912";

    /**
     * �g�����U�N�V����ID:AB08��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String TID_AB08 = "AB08";

    /**
     * �����m�F�{�^���őJ�ڎ��̃A�N�V�����^�C�v��\��������萔
     * 
     * @since �o�[�W���� 1.00
     */
    private static final String ACTION_CONFIRM = "CONFIRM";

    /**
     * �`�F�b�N�������s���܂��B<br>
     * 
     * <p>
     * �����^�Z�b�V�����I�u�W�F�N�g�̐������̃`�F�b�N���s���܂��B<br>
     * �`�F�b�N�Ɏ��s�����ꍇ�A�s���J�ډ��(AGSEA912)�ɑJ�ڂ��܂��B
     * 
     * @return �`�F�b�N����
     * @since �o�[�W���� 1.00
     */
    protected boolean checkInput() {
        ASKULUtil.log(ASKULUtil.DEBUG, "ACMKeshikomiCancelRequestAction#checkInput()");

        printData();

        // ��ʏ���ێ�����I�u�W�F�N�g���擾
        final ASKULDataBean db1 = getDataBean();

        // �����t���O���擾
        final String clcTbisFlg = db1.getString(KEY_CLCTBISFLG);

        // �����`�F�b�N
        if (!CONST_ADMIN.equals(clcTbisFlg)) {
            ASKULUtil.log(ASKULUtil.DEBUG, "�����̖�����ʑJ�ڂł��B");

            setTranId(db1, TID_EA912);

            return false;
        }

        // ���������@�\�̃Z�b�V�����I�u�W�F�N�g���擾
        ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // ���������@�\�̃Z�b�V�����I�u�W�F�N�g�̐������`�F�b�N
        if (!isSeq(db2, true)) {
            db2 = new ASKULDefaultDataBean();

            setDataBean(KEY_ACM_AB08, db2);

            newTemporaryData();
        }

        return true;
    }

    /**
     * �Ɩ��������s���܂��B<br>
     * 
     * <p>
     * ���������@�\(���͉��)�̏����l��ݒ肵�܂��B
     * 
     * @since �o�[�W���� 1.00
     */
    protected void execute() {
        ASKULUtil.log(ASKULUtil.DEBUG, "ACMKeshikomiCancelRequestAction#execute()");

        printData();

        // ��ʏ���ێ�����I�u�W�F�N�g���擾
        final ASKULDataBean db1 = getDataBean();

        // ���������@�\�̃Z�b�V�����I�u�W�F�N�g���擾
        final ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // �A�N�V�����^�C�v���擾
        final String actionType = db1.getStringNvl(ACMKeshikomiCancelBean.KEY_ACTION);
        
        // ���͗��\���s�����v���p�e�B�t�@�C�����擾
        final int cancelRow = ASKULUtil.getPropertyInt(PROPERTY_CANCELROW);

        /* -------------------- ���p���f�[�^��ݒ�(db2) -------------------- */

        ActionErrors errors;

        if (ACTION_CONFIRM.equals(actionType)) {
        	errors = getError(db2);
        } else {
        	errors = new ActionErrors();
        }

        setError(db2, errors);

        
        /* -------------------- ���p���f�[�^��ݒ�(db1) -------------------- */

        String[] userCode = db2.getStringArray(KEY_TMP_USERCODE);
        String[] skYear = db2.getStringArray(KEY_TMP_SKYEAR);
        String[] skMonth = db2.getStringArray(KEY_TMP_SKMONTH);
        String[] skDay = db2.getStringArray(KEY_TMP_SKDAY);
        String[] isError = db2.getStringArray(KEY_TMP_ISERROR);
        // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD START
        String[] cardReleaseChecked;
        if (db2.getStringArray(KEY_TMP_CARDRELEASE) == null) {
        	cardReleaseChecked = ASKULUtil.createStringArray("", cancelRow);
        } else {
        	cardReleaseChecked = db2.getStringArray(KEY_TMP_CARDRELEASE);
        };
        // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD END

        setNextData(
            ASKULUtil.nvl(userCode),
            ASKULUtil.nvl(skYear),
            ASKULUtil.nvl(skMonth),
            ASKULUtil.nvl(skDay),
            ASKULUtil.nvl(cardReleaseChecked),  // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD
            ASKULUtil.nvl(isError),
            getCurrSeq(db2),
            cancelRow,
            getError(db2));

        setTranId(db1, TID_AB08);
    }

    /**
     * ���p���f�[�^���ꎞ�ۑ����܂��B<br>
     * 
     * <p>
     * ���̃f�[�^�� DB2_ACMAB_KESHIKOMIRESETREQUEST ��
     * �L�[�Ƃ����I�u�W�F�N�g�ɕۑ�����܂��B<br>
     * �Ӑ}�I�ɍ폜���Ȃ�����A������ʂŎg�p�\�ł��B
     *
     * @param cancelRow - ���͗��\���s��
     * @param errors - �G���[
     * @since �o�[�W���� 1.00
     */
    /*private void setTemporaryData(
        final int cancelRow,
        final ActionErrors errors) {

    	ASKULUtil.log(
    	    ASKULUtil.DEBUG,
    	    "ACMKeshikomiCancelRequestAction#setTemporaryData(");

    	// ���������@�\�̃Z�b�V�����I�u�W�F�N�g���擾
        final ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // ���͗��\���s����ݒ�
        db2.setInt(KEY_CANCELROW, cancelRow);

        setError(db2, errors);
    }*/

    /**
     * �ꎞ�ۑ��f�[�^�̏��������s���܂��B
     * 
     * @since �o�[�W���� 1.00
     */
    private void newTemporaryData() {
	    ASKULUtil.log(
	        ASKULUtil.DEBUG,
	        "ACMKeshikomiCancelRequestAction#newTemporaryData(");

	    // ���������@�\�̃Z�b�V�����I�u�W�F�N�g���擾
        final ASKULDataBean db2 = getDataBean(KEY_ACM_AB08);

        // ���͗��\���s�����v���p�e�B�t�@�C�����擾
        final int cancelRow = ASKULUtil.getPropertyInt(PROPERTY_CANCELROW);

        // ���͗��\���s����ݒ�
        db2.setInt(KEY_CANCELROW, cancelRow);

        // ���[�U�R�[�h��ݒ�
        db2.setStringArray(
            KEY_TMP_USERCODE,
            ASKULUtil.nvl(new String[cancelRow]));

        // ��������(�N)��ݒ�
        db2.setStringArray(
            KEY_TMP_SKYEAR,
            ASKULUtil.nvl(new String[cancelRow]));

        // ��������(��)��ݒ�
        db2.setStringArray(
            KEY_TMP_SKMONTH,
            ASKULUtil.nvl(new String[cancelRow]));

        // ��������(��)��ݒ�
        db2.setStringArray(
            KEY_TMP_SKDAY,
            ASKULUtil.nvl(new String[cancelRow]));

        // �G���[�s����ݒ�
        db2.setStringArray(
            KEY_TMP_ISERROR,
            ASKULUtil.nvl(new String[cancelRow]));

        // �V�[�P���X��ݒ�
        db2.setString(KEY_SEQ, getNextSeq());

        // �G���[��ݒ�
        setError(db2, new ActionErrors());
    }

    /**
     * ����ʂŎg�p������p���f�[�^��ݒ肵�܂��B<br>
     * 
     * <p>
     * ���̃f�[�^�͎���ʂł�JSP�Ŏg�p���܂��B<br>
     * �Ӑ}�I�Ɉ����p���Ȃ�����A������ʂł̎g�p�͏o���܂���B
     * 
     * @param userCode - ���₢���킹�ԍ�
     * @param skYear - ��������(�N)
     * @param skMonth - ��������(��)
     * @param skDay - ��������(��)
     * @param cardReleaseChecked - �s�x�J�[�h���ϕ���������������^���Ȃ��̃`�F�b�N�l  // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD
     * @param isError - �G���[�s
     * @param seq - �V�[�P���X
     * @param cancelRow - ���͗��\�s��
     * @param errors - �G���[
     * @since �o�[�W���� 1.00
     */
    private void setNextData(
        final String[] userCode,
        final String[] skYear,
        final String[] skMonth,
        final String[] skDay,
        final String[] cardReleaseChecked,   // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD
        final String[] isError,
        final String seq,
    	final int cancelRow,
    	final ActionErrors errors) {

    	ASKULUtil.log(ASKULUtil.DEBUG, "ACMKeshikomiCancelRequest#setNextData()");

    	// ��ʏ���ێ�����I�u�W�F�N�g���擾
        final ASKULDataBean db1 = getDataBean();

        // ���₢���킹�ԍ�
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_USERCODE,
            userCode);

        // ��������(�N)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_SKYEAR,
            skYear);

        // ��������(��)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_SKMONTH,
            skMonth);

        // ��������(��)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_SKDAY,
            skDay);

        // ��������(��)�̃v���_�E��
        db1.setPullDown(
            ACMKeshikomiCancelBean.KEY_PUL_SKDAY,
            AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_VAL"),
            AGSM.getStringArray(AGSM.RS_CHECKDATE, "CHECKDATE_DSP"),
            skDay);

        // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD START
        // ��������(��)
        db1.setStringArray(
            ACMKeshikomiCancelBean.KEY_CARDRELEASE,
            cardReleaseChecked);
        // 2017/12/11 �o�^���P�t�F�[�Y2.5�Ή� NSSOL���� ADD END

        // �G���[�s
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

        // �V�[�P���X
        db1.setString(KEY_SEQ, seq);

        // ���͗��\���s��
        db1.setInt(ACMKeshikomiCancelBean.KEY_CANCELROW, cancelRow);

        // �G���[
        setError(db1, errors);
    }
}
