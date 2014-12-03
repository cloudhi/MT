package org.fireking.app.mt.action.host.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.fireking.app.kimiralibrary.base.BaseFragment;
import org.fireking.app.kimiralibrary.utils.ViewHolder;
import org.fireking.app.kimiralibrary.views.CirclePageIndicator;
import org.fireking.app.kimiralibrary.views.NoScrollListView;
import org.fireking.app.mt.R;
import org.fireking.app.mt.action.goup.ShopPanicbuyingActivity;
import org.fireking.app.mt.model.CategoryEntity;
import org.fireking.app.mt.model.GuessForyouEntity;
import org.fireking.app.mt.model.ShopPanicBuyEntity;
import org.fireking.app.mt.service.grab_webpage.GroupGrab;
import org.jsoup.nodes.Document;

import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

/**
 * �Ź�fragment<br />
 * ��hostActivity����
 * 
 * @author join
 *
 */
public class GroupFragment extends BaseFragment implements OnClickListener {
	ImageView loading;// loading����view
	RelativeLayout loadingLayout;// loading���ܲ���
	TextView loadText;// ������ʾ����
	AnimationDrawable loadAnimation;// ���ض���
	@InjectView(R.id.pager)
	private ViewPager pager;// �����Ĺ���������Ϣ
	@InjectView(R.id.indicator)
	private CirclePageIndicator indicator;// ָʾ��
	NavgationAddapter navAdapter;// ��������������(viewpager)
	NavAdapter navAdapter2;// ������������������(gridview)

	/************************************************************/
	// --------------���������Ƽ�����
	/***********************************************************/
	@InjectView(R.id.panic_1)
	private TextView panic_1;
	@InjectView(R.id.panic_1_1)
	private TextView panic_1_1;
	@InjectView(R.id.panic_1_2)
	private TextView panic_1_2;
	@InjectView(R.id.panic_2)
	private TextView panic_2;
	@InjectView(R.id.panic_2_1)
	private TextView panic_2_1;
	@InjectView(R.id.panic_2_2)
	private TextView panic_2_2;
	@InjectView(R.id.panic_3)
	private TextView panic_3;
	@InjectView(R.id.panic_3_1)
	private TextView panic_3_1;
	@InjectView(R.id.panic_3_2)
	private TextView panic_3_2;
	@InjectView(R.id.panic_m1)
	private ImageView panic_m1;
	@InjectView(R.id.panic_m2)
	private ImageView panic_m2;
	@InjectView(R.id.panic_m3)
	private ImageView panic_m3;
	/***************************************/
	// ������������ʱ������ֱ����ʾ����ʱ���ˣ��൱��ʱ�ӹ��ܣ�
	/***************************************/
	@InjectView(R.id.hour)
	private TextView hour;// Сʱ
	@InjectView(R.id.minues)
	private TextView minues;// ����
	@InjectView(R.id.secound)
	private TextView secound;// ��

	@InjectView(R.id.panic_more)
	private LinearLayout panic_more;// ����������������
	@InjectView(R.id.panic_item1)
	private LinearLayout panic_item1;
	@InjectView(R.id.panic_item2)
	private LinearLayout panic_item2;
	@InjectView(R.id.panic_item3)
	private LinearLayout panic_item3;

	/**
	 * ����ϲ������
	 */
	@InjectView(R.id.guessforyou)
	NoScrollListView guessforYou;

	// ע������ץȡ����
	@Inject
	GroupGrab mGroupGrab;

	static String city = "shanghai";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// ���ù��������ϲ��������scrollviewǶ��listviewʱ������listview�������������bug��
		pager.setFocusable(true);
		pager.setFocusableInTouchMode(true);
		pager.requestFocus();
		// ��ʾ���ض���
		loadAnim();
		// ��ʼ�������ĵ���
		initFirstNavgation();
		// ������wapվ���ȡdocment�ڵ�����
		getDocumentTag();
		// ��ʾ��ǰ��������ʱ
		showDowntimes();
		panic_more.setOnClickListener(this);
		panic_item1.setOnClickListener(this);
		panic_item2.setOnClickListener(this);
		panic_item3.setOnClickListener(this);
	}

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
	 * ��ȡ�Ź����������ݽڵ�
	 */
	private void getDocumentTag() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				try {
					Document doc = mGroupGrab.getDocument(city);
					msg.what = 0x1001;
					msg.obj = doc;
					handler.sendMessage(msg);
				} catch (Exception e) {
					msg.what = 0x1002;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x1001:
				stopAnim();
				Document doc = (Document) msg.obj;
				if (doc == null) {
					return;
				}

				try {
					// ���������Ƽ�����
					setGroupPanicBuying(doc);
					// ����ϲ���Ƽ�����
					setGuessForYou(doc);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case 0x1002:
				stopAnim();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * ���ò���ϲ�����Ƽ�����
	 * 
	 * @param doc
	 */
	private void setGuessForYou(Document doc) {
		List<GuessForyouEntity> entitys = null;
		try {
			entitys = mGroupGrab.getGuessForYou(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		GuessForyouAdapter adapter = new GuessForyouAdapter(getActivity(),
				entitys);
		guessforYou.setAdapter(adapter);
	}

	/**
	 * �������������Ƽ�����
	 * 
	 * @throws Exception
	 */
	private void setGroupPanicBuying(Document doc) throws Exception {
		List<ShopPanicBuyEntity> entitys = mGroupGrab.getShop_panic_buying(doc);
		if (entitys != null && entitys.size() == 3) {
			ShopPanicBuyEntity e0 = entitys.get(0);
			panic_1.setText(e0.getName());
			Picasso.with(getActivity()).load(e0.getImagePath()).into(panic_m1);
			panic_1_1.setText(e0.getDiscount_price());
			panic_1_2.setText(e0.getOriginal_price());
			ShopPanicBuyEntity e1 = entitys.get(1);
			panic_2.setText(e1.getName());
			Picasso.with(getActivity()).load(e1.getImagePath()).into(panic_m2);
			panic_2_1.setText(e1.getDiscount_price());
			panic_2_2.setText(e1.getOriginal_price());
			ShopPanicBuyEntity e2 = entitys.get(2);
			panic_3.setText(e2.getName());
			Picasso.with(getActivity()).load(e2.getImagePath()).into(panic_m3);
			panic_3_1.setText(e2.getDiscount_price());
			panic_3_2.setText(e2.getOriginal_price());
		}
	}

	/**
	 * ��ʼ���׵���
	 */
	private void initFirstNavgation() {
		navAdapter = new NavgationAddapter(initNavViews());
		pager.setAdapter(navAdapter);
		indicator.setViewPager(pager);
	}

	/**
	 * ��ʼ���׵�����view����
	 * 
	 * @return
	 */
	private List<View> initNavViews() {
		List<View> views = new ArrayList<View>();
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		GridView view1 = (GridView) inflater.inflate(R.layout.host_nav_view1,
				null);
		navAdapter2 = new NavAdapter(getActivity(), getCategoryEntity());
		view1.setAdapter(navAdapter2);
		View view2 = inflater.inflate(R.layout.host_nav_view2, null);
		views.add(view2);
		views.add(view1);
		return views;
	}

	/**
	 * ��ʼ��category
	 * 
	 * @return
	 */
	private List<CategoryEntity> getCategoryEntity() {
		List<CategoryEntity> categorys = new ArrayList<CategoryEntity>();
		categorys.add(new CategoryEntity(0, "��ʳ", R.drawable.ic_category_0));
		categorys.add(new CategoryEntity(1, "��Ӱ", R.drawable.ic_category_1));
		categorys.add(new CategoryEntity(2, "�Ƶ�", R.drawable.ic_category_2));
		categorys.add(new CategoryEntity(3, "KTV", R.drawable.ic_category_3));
		categorys.add(new CategoryEntity(4, "����", R.drawable.ic_category_4));
		categorys.add(new CategoryEntity(5, "�������", R.drawable.ic_category_5));
		categorys.add(new CategoryEntity(6, "�����µ�", R.drawable.ic_category_6));
		categorys.add(new CategoryEntity(7, "ȫ������", R.drawable.ic_category_7));
		return categorys;
	}

	/**
	 * ���ض���
	 */
	private void loadAnim() {
		loadingLayout = (RelativeLayout) getView().findViewById(
				R.id.loading_layout);
		loadingLayout.setVisibility(View.VISIBLE);
		loading = (ImageView) getView().findViewById(R.id.loading);
		loadText = (TextView) getView().findViewById(R.id.loading_text);
		loadAnimation = (AnimationDrawable) loading.getBackground();
		loadAnimation.start();
	}

	/**
	 * ֹͣ����
	 */
	private void stopAnim() {
		loadAnimation.stop();
		loadingLayout.setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_group, null);
	}

	/**
	 * ��������view��gridview������
	 * 
	 * @author join
	 *
	 */
	class NavAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<CategoryEntity> entitys;

		public NavAdapter(Context context, List<CategoryEntity> entitys) {
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.entitys = entitys;
		}

		@Override
		public int getCount() {
			return entitys.size();
		}

		@Override
		public Object getItem(int position) {
			return entitys.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_nav_view1, null);
			}
			CategoryEntity category = (CategoryEntity) getItem(position);
			ImageView nvImageView = ViewHolder.get(convertView, R.id.nv_img);
			TextView nvTextView = ViewHolder.get(convertView, R.id.nv_text);
			nvImageView.setBackgroundResource(category.resImage);
			nvTextView.setText(category.name);
			return convertView;
		}
	}

	/**
	 * ����������������
	 * 
	 * @author join
	 *
	 */
	class NavgationAddapter extends PagerAdapter {
		List<View> views;

		public NavgationAddapter(List<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return views == null || views.size() == 0 ? 0 : views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}
	}

	/**
	 * ����ϲ������adapter
	 * 
	 * @author join
	 *
	 */
	class GuessForyouAdapter extends BaseAdapter {
		LayoutInflater inflater;
		List<GuessForyouEntity> entitys;

		public GuessForyouAdapter(Context context,
				List<GuessForyouEntity> entitys) {
			this.entitys = entitys;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return entitys == null || entitys.size() == 0 ? 0 : entitys.size();
		}

		@Override
		public Object getItem(int position) {
			return entitys.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_guessforyou, null);
			}
			GuessForyouEntity entity = (GuessForyouEntity) getItem(position);
			TextView title = ViewHolder.get(convertView, R.id.title);
			title.setText(entity.getTitle());
			TextView description = ViewHolder
					.get(convertView, R.id.description);
			description.setText(entity.getDescription());
			TextView discount_price = ViewHolder.get(convertView,
					R.id.discount_price);
			discount_price.setText(entity.getDiscount_price());
			TextView sale_count = ViewHolder.get(convertView, R.id.sale_count);
			sale_count.setText(entity.getSale_count());
			ImageView album_photo = ViewHolder.get(convertView,
					R.id.album_photo);
			Picasso.with(getActivity()).load(entity.getImage())
					.into(album_photo);
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.panic_item1:
			routerToPanicList();
			break;
		case R.id.panic_item2:
			routerToPanicList();
			break;
		case R.id.panic_item3:
			routerToPanicList();
			break;
		case R.id.panic_more:
			routerToPanicList();
			break;
		default:
			break;
		}
	}

	/**
	 * ��ת�����������б�ҳ��
	 */
	private void routerToPanicList() {
		Intent intent = new Intent(getActivity(), ShopPanicbuyingActivity.class);
		startActivity(intent);
	}

}
