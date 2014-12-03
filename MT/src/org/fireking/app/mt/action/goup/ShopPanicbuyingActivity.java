package org.fireking.app.mt.action.goup;

import java.util.Calendar;
import java.util.List;

import org.fireking.app.kimiralibrary.base.BaseActivity;
import org.fireking.app.kimiralibrary.utils.ViewHolder;
import org.fireking.app.mt.R;
import org.fireking.app.mt.model.ShopPanicListEntity;
import org.fireking.app.mt.service.grab_webpage.GroupGrab;

import roboguice.context.event.OnCreateEvent;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

/**
 * ��������ҳ��
 * 
 * @author join
 *
 */
@ContentView(R.layout.group_panic)
public class ShopPanicbuyingActivity extends BaseActivity implements
		OnClickListener {
	/***************************************/
	// ������������ʱ������ֱ����ʾ����ʱ���ˣ��൱��ʱ�ӹ��ܣ�
	/***************************************/
	@InjectView(R.id.hour)
	private TextView hour;// Сʱ
	@InjectView(R.id.minues)
	private TextView minues;// ����
	@InjectView(R.id.secound)
	private TextView secound;// ��
	@InjectView(R.id.panic_list)
	private ListView panic_list;
	PanicBuyingAdapter adapter;
	@InjectView(R.id.loading_layout)
	RelativeLayout loading_layout;// ��������ҳ��
	@InjectView(R.id.loading)
	ImageView loading;// loading����view
	@InjectView(R.id.loading_text)
	TextView loadText;// ������ʾ����
	AnimationDrawable loadAnimation;// ���ض���

	// ��ȡ����
	static final int GET_PANIC_LIST_SUC = 0x1001;
	static final int GET_PANIC_LIST_ERR = 0x2001;

	// ��������
	@InjectView(R.id.panic_tip)
	private TextView panic_tip;

	// ע������ץȡ����
	@Inject
	GroupGrab mGroupGrab;

	@Override
	protected void doSomethingsOnCreate(OnCreateEvent onCreate) {
		super.doSomethingsOnCreate(onCreate);
		adapter = new PanicBuyingAdapter(this);
		panic_list.setAdapter(adapter);
		// ��ʾ���ض���
		loadAnim();
		// ��ʾ��ǰ��������ʱ
		showDowntimes();
		// ץȡ���������б�����
		getPanicList();
		panic_tip.setOnClickListener(this);
	}

	/**
	 * ���ض���
	 */
	private void loadAnim() {
		loading_layout.setVisibility(View.VISIBLE);
		loadAnimation = (AnimationDrawable) loading.getBackground();
		loadAnimation.start();
	}

	/**
	 * ֹͣ����
	 */
	private void stopAnim() {
		loadAnimation.stop();
		loading_layout.setVisibility(View.GONE);
	}

	/**
	 * ��ȡ���������б�
	 */
	private void getPanicList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<ShopPanicListEntity> entitys = mGroupGrab
							.getPanicList();
					Message msg = handler.obtainMessage();
					if (entitys == null || entitys.size() == 0) {
						msg.what = GET_PANIC_LIST_ERR;
					} else {
						msg.what = GET_PANIC_LIST_SUC;
						msg.obj = entitys;
					}
					msg.sendToTarget();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_PANIC_LIST_SUC:
				stopAnim();
				List<ShopPanicListEntity> entitys = (List<ShopPanicListEntity>) msg.obj;
				if (entitys == null || entitys.size() == 0) {
					return;
				}
				adapter.setData(entitys);
				break;
			case GET_PANIC_LIST_ERR:
				stopAnim();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * ��ǰ��������ʱģ��
	 */
	private void showDowntimes() {
		CountDownTimer timer = new CountDownTimer(24 * 60 * 1000 * 60, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.SECOND, 1);// ʱ������1��
				hour.setText(calendar.get(Calendar.HOUR_OF_DAY) <= 9 ? "0"
						+ calendar.get(Calendar.HOUR_OF_DAY) : calendar
						.get(Calendar.HOUR_OF_DAY) + "");// ����Сʱ
				minues.setText(calendar.get(Calendar.MINUTE) <= 9 ? "0"
						+ calendar.get(Calendar.MINUTE) : calendar
						.get(Calendar.MINUTE) + "");// ���÷���
				secound.setText(calendar.get(Calendar.SECOND) <= 9 ? "0"
						+ calendar.get(Calendar.SECOND) : calendar
						.get(Calendar.SECOND) + "");// ������
			}

			@Override
			public void onFinish() {
				this.start();
			}
		};
		timer.start();
	}

	/**
	 * ���������б�������
	 * 
	 * @author join
	 *
	 */
	class PanicBuyingAdapter extends BaseAdapter {
		LayoutInflater inflater;
		List<ShopPanicListEntity> entitys;
		Context context;

		public PanicBuyingAdapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<ShopPanicListEntity> entitys) {
			this.entitys = entitys;
		}

		@Override
		public int getCount() {
			return entitys == null || entitys.size() == 0 ? 0 : entitys.size();
		}

		@Override
		public Object getItem(int position) {
			return entitys == null || entitys.size() == 0 ? null : entitys
					.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.item_panic_buying, null);
			}

			ShopPanicListEntity entity = (ShopPanicListEntity) getItem(position);
			TextView origin_price = ViewHolder.get(convertView,
					R.id.origin_price);
			origin_price.setText(entity.getOrigin_price());
			TextView panic_price = ViewHolder
					.get(convertView, R.id.panic_price);
			panic_price.setText(entity.getPanic_price());
			TextView title = ViewHolder.get(convertView, R.id.title);
			title.setText(entity.getTitle());
			TextView description = ViewHolder
					.get(convertView, R.id.description);
			description.setText(entity.getDescription());
			ImageView panic_photo = ViewHolder.get(convertView,
					R.id.panic_photo);
			Picasso.with(context).load(entity.getImage()).into(panic_photo);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.panic_tip:

			break;

		default:
			break;
		}
	}
}
