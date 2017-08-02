package com.easydo.layout;

import java.util.LinkedHashMap;

import com.easydo.constant.GlobalConfig;
import com.easydo.db.EasyDoDB;
import com.easydo.model.SystemConfig;
import com.easydo.util.TypefaceUtil;
import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ChooseTypefaceLayout extends BaseLinearLayout {

	private TextView typeface1Tv;
	private TextView typeface2Tv;
	private TextView typeface3Tv;

	private TypefaceUtil mTypefaceUtil;
	private String[] typefaceNames;
	private LinkedHashMap<String, String> TYPE_FACES;

	// 内容类型
	private int mContentType;
	public static final int CONTENT_TYPE_SCHEDULE = 0;
	public static final int CONTENT_TYPE_JOURNAL = 1;

	private EasyDoDB easyDoDB;

	public ChooseTypefaceLayout(Context context, AttributeSet attrs,
			int contentType) {
		super(context, attrs);
		mContext = context;
		mContentType = contentType;
		LayoutInflater.from(context).inflate(R.layout.dialog_choose_typeface,
				this);
		easyDoDB = EasyDoDB.getInstance(context);
		mTypefaceUtil = new TypefaceUtil(context, "default");

		typeface1Tv = (TextView) findViewById(R.id.typeface1_tv);
		typeface2Tv = (TextView) findViewById(R.id.typeface2_tv);
		typeface3Tv = (TextView) findViewById(R.id.typeface3_tv);

		TYPE_FACES = GlobalConfig.TYPE_FACES;
		typefaceNames = (String[]) TYPE_FACES.keySet().toArray(new String[] {});

		typeface1Tv.setText(typefaceNames[0]);
		mTypefaceUtil.setmTypeface(TYPE_FACES.get(typefaceNames[0]));
		mTypefaceUtil.setTypeface(typeface1Tv, false);

		typeface2Tv.setText(typefaceNames[1]);
		mTypefaceUtil.setmTypeface(TYPE_FACES.get(typefaceNames[1]));
		mTypefaceUtil.setTypeface(typeface2Tv, false);

		typeface3Tv.setText(typefaceNames[2]);
		mTypefaceUtil.setmTypeface(TYPE_FACES.get(typefaceNames[2]));
		mTypefaceUtil.setTypeface(typeface3Tv, false);

	}

	public int getmContentType() {
		return mContentType;
	}

	public void setmContentType(int mContentType) {
		this.mContentType = mContentType;
	}

	public interface onTypefaceChooseListener {
		public void onChoosed(String typefaceName, String ttfPath);
	}

	// 选择事件
	public void setOnChooseListenter(final Context context,
			final onTypefaceChooseListener listener) {
		final EasyDoDB db = easyDoDB;
		typeface1Tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mContentType == CONTENT_TYPE_SCHEDULE) {
					db.updateDB(SystemConfig.TABLE_NAME,
							"schedule_content_typeface",
							TYPE_FACES.get(typefaceNames[0]), "id", "1");
				} else if (mContentType == CONTENT_TYPE_JOURNAL) {
					db.updateDB(SystemConfig.TABLE_NAME,
							"journal_content_typeface",
							TYPE_FACES.get(typefaceNames[0]), "id", "1");
				}
				listener.onChoosed(typefaceNames[0],
						TYPE_FACES.get(typefaceNames[0]));
			}
		});

		typeface2Tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mContentType == CONTENT_TYPE_SCHEDULE) {
					db.updateDB(SystemConfig.TABLE_NAME,
							"schedule_content_typeface",
							TYPE_FACES.get(typefaceNames[1]), "id", "1");
				} else if (mContentType == CONTENT_TYPE_JOURNAL) {
					db.updateDB(SystemConfig.TABLE_NAME,
							"journal_content_typeface",
							TYPE_FACES.get(typefaceNames[1]), "id", "1");
				}
				listener.onChoosed(typefaceNames[1],
						TYPE_FACES.get(typefaceNames[1]));
			}
		});

		typeface3Tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mContentType == CONTENT_TYPE_SCHEDULE) {
					db.updateDB(SystemConfig.TABLE_NAME,
							"schedule_content_typeface",
							TYPE_FACES.get(typefaceNames[2]), "id", "1");
				} else if (mContentType == CONTENT_TYPE_JOURNAL) {
					db.updateDB(SystemConfig.TABLE_NAME,
							"journal_content_typeface",
							TYPE_FACES.get(typefaceNames[2]), "id", "1");
				}
				listener.onChoosed(typefaceNames[2],
						TYPE_FACES.get(typefaceNames[2]));
			}
		});

	}

}
