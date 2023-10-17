/*
 * Project Name  : ������WEB
 * Subsystem Name: ����Ǘ��@�\
 *
 *
 * Created on 2004/07/27
 */
package jp.ags.acm.ag.bean;

import jp.ags.common.AGSDefaultJSPBean;

/**
 * <strong>����Ǘ��@�\�^���������󋵈ꗗ(AG01)</strong>
 * <p>
 *
 * @author XXXX
 * @version 1.00 2004-07-27 XXXX<br>
 *          1.01 2004-09-25 XXXX<br>
 *          1.02 2004-12-15 XXXX<br>
 * @since �o�[�W���� 1.00
 */
public class AGSAG01Bean extends AGSDefaultJSPBean {
	/**
	 * ���p�ҋ敪 ����
	 */
	private static final String MASTERCD_T   = "0" ;

	/**
	 * �R���X�g���N�^
	 * @since 2004/09/25 XXXX<br>
	 */
    public AGSAG01Bean() {
        super() ;
//        XxUtil.log(XxUtil.DEBUG, "AGSAG01Bean#AGSAG01Bean()") ;
    } // AGSAG01Bean()

/**
 * �J�ڂ̏�Ԃ��擾
 *
 * @return boolean �����J�ڂ̂Ƃ�true�A����ȊOfalse
 * @since �o�[�W���� 1.00
 */
    public boolean isInit() {
        return (getPropertyInt("INIT") == 1) ;
    } // isInit()

//  2006-05-16 �x�����@�E�������@�����ǉ� START
/**
 * �}�Ԃ�html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
  //public String getPd_Agsubcd() {
  //    return getPropertyPullDown("PD_AGSUBCD") ;
  //} // getPd_Agsubcd()

    public String getPd_Agsubcd() {
        return getPropertyMultiplePullDown("PD_AGSUBCD") ;
    } // getPd_Agsubcd()
// 2006-05-16 �x�����@�E�������@�����ǉ� END


	
/**
 * ���o������html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getPd_extracted() {
        return getPropertyPullDown("PD_EXTRACTED") ;
    } // getPd_extracted()

/**
 * ������ʂ�html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getPd_sk_seg() {
        return getPropertyPullDown("PD_SK_SEG") ;
    } // getPd_sk_seg()

/**
 * ������ʂ�html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getPd_nk_seg() {
        return getPropertyPullDown("PD_NK_SEG") ;
    } // getPd_nk_seg()

// 2006-05-16 �x�����@�E�������@�����ǉ� START
/**
 * �x�����@�̃v���_�E�����擾����
 * @return String �������ꂽHtml
 * @since �o�[�W���� 2.01
 */
    public String getPd_Sk_houhou() {
        return getPropertyPullDown("PD_SK_HOUHOU") ;
    } // getSKPaySeg()

/**
 * �������@��html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 2.01
 */
    public String getPd_Nk_houhou() {
        return getPropertyPullDown("PD_NK_HOUHOU") ;
    } // getPd_Nk_houhou()
// 2006-05-16 �x�����@�E�������@�����ǉ� END


    
    
/**
 * �ˍ��������ڂ�html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getPd_tgc() {
        return getPropertyPullDown("PD_TGC") ;
    } // getPd_tgc()

/**
 * �ˍ��p�^�[���̗v�f�����擾����
 *
 * @return int �ˍ��p�^�[���̗v�f��
 * @since �o�[�W���� 1.00
 */
    public int getRb_tg_ptn_Len() {
        return getPropertyRadioButtonLength("RB_TG_PTN") ;
    } // getRb_tg_ptn_Len()

/**
 * �ˍ��p�^�[����html���W�I�{�^�����擾����
 *
 * @param  i �C���f�b�N�X
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getRb_tg_ptn(int i) {
        return getPropertyRadioButton("RB_TG_PTN", i) ;
    } // getRb_tg_ptn()

/**
 * ��������From(�N)���擾����
 *
 * @return String ��������From(�N)
 * @since �o�[�W���� 1.00
 */
    public String getSkFromYear() {
        return getPropertyString("SKFROMYEAR") ;
    } // getSkFromYear()

/**
 * ��������From(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getSkFromMonth() {
        return getPropertyString("SKFROMMONTH") ;
    } // getSkFromMonth()

/**
 * ��������From(��)��html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
 public String getPd_SkFromDay() {
     return getPropertyPullDown("PD_SKFROMDAY") ;
 } // getPd_SkFromDay()

/**
 * ��������To(�N)���擾����
 *
 * @return String ��������To(�N)
 * @since �o�[�W���� 1.00
 */
    public String getSkToYear() {
        return getPropertyString("SKTOYEAR") ;
    } // getSkToYear()

/**
 * ��������To(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getSkToMonth() {
        return getPropertyString("SKTOMONTH") ;
    } // getSkToMonth()

/**
 * ��������To(��)��html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
 public String getPd_SkToDay() {
     return getPropertyPullDown("PD_SKTODAY") ;
 } // getPd_SkToDay()

/**
 * ����From(�N)���擾����
 *
 * @return String ����From(�N)
 * @since �o�[�W���� 1.00
 */
    public String getNkFromYear() {
        return getPropertyString("NKFROMYEAR") ;
    } // getNkFromYear()

/**
 * ����From(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getNkFromMonth() {
        return getPropertyString("NKFROMMONTH") ;
    } // getNkFromMonth()

/**
 * ����From(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getNkFromDay() {
        return getPropertyString("NKFROMDAY") ;
    } // getNkFromDay()

/**
 * ����To(�N)���擾����
 *
 * @return String ����To(�N)
 * @since �o�[�W���� 1.00
 */
    public String getNkToYear() {
        return getPropertyString("NKTOYEAR") ;
    } // getNkToYear()

/**
 * ����To(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getNkToMonth() {
        return getPropertyString("NKTOMONTH") ;
    } // getNkToMonth()

/**
 * ����To(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getNkToDay() {
        return getPropertyString("NKTODAY") ;
    } // getNkToDay()

// 2004-12-15 XXXX �����o�^���ǉ� ST
/**
 * �����o�^From(�N)���擾����
 *
 * @return String ����From(�N)
 * @since �o�[�W���� 1.01
 */
    public String getNkinFromYear() {
        return getPropertyString("NKINFROMYEAR") ;
    }

/**
 * �����o�^From(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.01
 */
    public String getNkinFromMonth() {
        return getPropertyString("NKINFROMMONTH") ;
    } // getNkFromMonth()

/**
 * �����o�^From(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.01
 */
    public String getNkinFromDay() {
        return getPropertyString("NKINFROMDAY") ;
    } // getNkinFromDay()

/**
 * �����o�^To(�N)���擾����
 *
 * @return String ����To(�N)
 * @since �o�[�W���� 1.01
 */
    public String getNkinToYear() {
        return getPropertyString("NKINTOYEAR") ;
    }

/**
 * �����o�^To(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.01
 */
    public String getNkinToMonth() {
        return getPropertyString("NKINTOMONTH") ;
    } // getNkinToMonth

/**
 * �����o�^To(��)���擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.01
 */
    public String getNkinToDay() {
        return getPropertyString("NKINTODAY") ;
    } // getNkinToDay()
//  2004-12-15  �����o�^���ǉ� EX

/**
 * ���q�l�R�[�h���擾����
 *
 * @return String ���q�l�R�[�h
 * @since �o�[�W���� 1.00
 */
    public String getUsercd() {
        return getPropertyString("USERCD") ;
    } // getUsercd()

/**
 * �\��������html�v���_�E�����擾����
 *
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getPd_ListCnt() {
        return getPropertyPullDown("PD_LISTCNT") ;
    } // getPd_ListCnt()

/**
 * ���݂̕��ёւ�����擾����
 *
 * @return String ���ёւ��̗�
 * @since �o�[�W���� 1.00
 */
    public String getSort() {
        return getPropertyString("SORT") ;
    } // getSort()

/**
 * ���݂̕��ёւ������擾����
 *
 * @return String ���ёւ��� ("asc" �܂��� "desc")
 * @since �o�[�W���� 1.00
 */
    public String getAscend() {
        return getPropertyString("ASCEND") ;
    } // getAscend()

/**
 * �������z���v���擾����
 *
 * @return String �������z���v
 * @since �o�[�W���� 1.00
 */
    public String getSk_bill_total() {
        return getPropertyString("SK_BILL_TOTAL") ;
    } // getSk_bill_total()

/**
 * �������z���v���擾����
 *
 * @return String �������z���v
 * @since �o�[�W���� 1.00
 */
    public String getNk_debit_total() {
        return getPropertyString("NK_DEBIT_TOTAL") ;
    } // getNk_debit_total()

/**
 * �ˍ��I���`�F�b�N�{�b�N�X���擾����
 *
 * @param i �C���f�b�N�X 
 * @return String �������ꂽHtml
 * @since �o�[�W���� 1.00
 */
    public String getCb_Check_flg(int i) {
        return getPropertyListCheckBox("CB_CHECK_FLG", i) ;
    } // getCb_Check_flg()

/**
 * name����(�A�Ԃ�)���擾����
 *
 * @param nm name 
 * @param i �C���f�b�N�X 
 * @return String �������ꂽHtml��name����
 * @since �o�[�W���� 1.00
 */
    public String getTagName(String nm, int i) {
        return createTagListName(nm, i) ;
    } // getTagName()
/**
 * AG01��TRAN_SEQ���擾����
 *
 * @return String AG01��TRAN_SEQ
 * @since �o�[�W���� 1.00
 */
	public String getAG01TranSeq() {
		return getPropertyString("AG01_TRANSEQ") ;
	} // getAG01TranSeq()

/**
 * AG01��PNO���擾����
 *
 * @return String AG01��PNO
 * @since �o�[�W���� 1.00
 */
	public String getAG01Pno() {
		return getPropertyString("AG01_PNO") ;
	} // getAG01Pno()

/**
 * �Ώێ}�Ԃ��Q�Ɖ\���`�F�b�N����
 * @param agsubcd 
 *
 * @return boolean ���茋��
 * @since �o�[�W���� 1.00
 */
	public boolean checkAgsubcd(String agsubcd) {
		for (int i = 0 ; i < getPropertyListStringLength("CHK_AGSUBCD") ; i++){
			if (agsubcd.equals(getPropertyListString("CHK_AGSUBCD",i))){
				return true;
			}
		}
        // �}�Ԗ����̔���
		if(agsubcd.equals("&nbsp;") || agsubcd.equals("&nbsp")) {
			if(getPropertyString("MASTERCD").equals(MASTERCD_T)) {
				return true;
			}
			else{
				// �x�X�Ŏ}�ԂȂ��̏ꍇ�͓������̂��Ԃ�̂łn�j
				if(getPropertyString("OPE_AGSUBCD").length() == 0) {
					return true;
				}
			}
		}
        // �����f�[�^�����̔���
        if(agsubcd.startsWith("��")) {
            if(getPropertyString("MASTERCD").equals(MASTERCD_T)) {
                return true;
            }
        }
		return false;
	} // checkAgsubcd()
	
// 2019-05-10 ����Ǘ��@�\�d�l�ύX�Ή� ADD START
/**
 * �x����From(�N)���擾����
 *
 * @return String �x����From(�N)
 * @since �o�[�W���� 1.01
 */
    public String getNkpayFromYear() {
        return getPropertyString("NKPAYFROMYEAR") ;
    }

/**
 * �x����From(��)���擾����
 *
 * @return String �x����From(��)
 * @since �o�[�W���� 1.01
 */
    public String getNkpayFromMonth() {
        return getPropertyString("NKPAYFROMMONTH") ;
    }

/**
 * �x����From(��)���擾����
 *
 * @return String �x����From(��)
 * @since �o�[�W���� 1.01
 */
    public String getNkpayFromDay() {
        return getPropertyString("NKPAYFROMDAY") ;
    }

/**
 * �x����To(�N)���擾����
 *
 * @return String �x����To(�N)
 * @since �o�[�W���� 1.01
 */
    public String getNkpayToYear() {
        return getPropertyString("NKPAYTOYEAR") ;
    }

/**
 * �x����To(��)���擾����
 *
 * @return String �x����To(��)
 * @since �o�[�W���� 1.01
 */
    public String getNkpayToMonth() {
        return getPropertyString("NKPAYTOMONTH") ;
    }

/**
 * �x����To(��)���擾����
 *
 * @return String �x����To(��)
 * @since �o�[�W���� 1.01
 */
    public String getNkpayToDay() {
        return getPropertyString("NKPAYTODAY") ;
    }

/**
 * �󒍒�~�L���̃v���_�E�����擾����
 * @return String �������ꂽHtml
 * @since �o�[�W���� 2.01
 */
    public String getPd_resstp() {
        return getPropertyPullDown("PD_RESSTP") ;
    }

/**
 * �󒍒�~���R(�������)�̎擾����
 * 
 * @return String �����^�C�v
 * @since �o�[�W���� 4.00(SynchroAgentIT2.0�Ή�)	 
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
 * �󒍒�~���R(������s�\)�̎擾����
 * 
 * @return String �����^�C�v
 * @since �o�[�W���� 4.00(SynchroAgentIT2.0�Ή�)	 
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
 * �󒍒�~���R(���L�ȊO�S��)�̎擾����
 * 
 * @return String �����^�C�v
 * @since �o�[�W���� 4.00(SynchroAgentIT2.0�Ή�)	 
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
 * �󒍒�~���R(AG�˗����̑�)�̎擾����
 * 
 * @return String �����^�C�v
 * @since �o�[�W���� 4.00(SynchroAgentIT2.0�Ή�)	 
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
 * �󒍒�~���R(AG�˗��E��)�̎擾����
 * 
 * @return String �����^�C�v
 * @since �o�[�W���� 4.00(SynchroAgentIT2.0�Ή�)	 
 */
    public String getStopreason_725_Chk() {
		String value = getPropertyString("STOPREASON_725");
		if(value.equals("725") && value.length() != 0){
			return "checked";
		}else{
			return "";
		}
    }
// 2019-05-10 ����Ǘ��@�\�d�l�ύX�Ή� ADD END

}



