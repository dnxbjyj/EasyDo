package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

public class CustomRatingBarLayout extends BaseLinearLayout {

	// ���ǵ�����
	public static final int STAR_TOTAL_NUM = 7;
	// ����Iv��id����
	public static final int[] STAR_IV_IDS = new int[] { R.id.star1, R.id.star2,
			R.id.star3, R.id.star4, R.id.star5, R.id.star6, R.id.star7 };;
	// �����������Ŀ
	private int starNum = 0;

	public CustomRatingBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.custom_rating_bar, this);
	}

	public int getStarNum() {
		return starNum;
	}

	/**
	 * ���ݱ������ü������ǵ���Ŀ
	 * 
	 * @param scale
	 *            ���������ռ�����ǵı���
	 */
	public void setStarNum(float scale) {
		int activeNum = (int) (scale * STAR_TOTAL_NUM);
		starNum = activeNum;

		for (int i = 0; i < activeNum; i++) {
			((ImageView) findViewById(STAR_IV_IDS[i]))
					.setImageResource(R.drawable.rating_bar_star_active);
		}

		for (int i = activeNum; i < STAR_TOTAL_NUM; i++) {
			((ImageView) findViewById(STAR_IV_IDS[i]))
					.setImageResource(R.drawable.rating_bar_star_inactive);
		}
	}
}
