package org.eu.chiminori.translator;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class LanguageActivity extends AppCompatActivity {
	
	private boolean language_from = false;
	private double r = 0;
	private double length = 0;
	private String value1 = "";
	private double n = 0;
	
	private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
	
	private LinearLayout nav;
	private LinearLayout back_layout1;
	private LinearLayout layout;
	private LinearLayout toolbar;
	private LinearLayout linear_search;
	private ListView listview;
	private ImageView back;
	private TextView title;
	private ImageView imageview1;
	private EditText edittext_search;
	
	private SharedPreferences data;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.language);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		nav = findViewById(R.id.nav);
		back_layout1 = findViewById(R.id.back_layout1);
		layout = findViewById(R.id.layout);
		toolbar = findViewById(R.id.toolbar);
		linear_search = findViewById(R.id.linear_search);
		listview = findViewById(R.id.listview);
		back = findViewById(R.id.back);
		title = findViewById(R.id.title);
		imageview1 = findViewById(R.id.imageview1);
		edittext_search = findViewById(R.id.edittext_search);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				finish();
			}
		});
		
		edittext_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				try{
					java.io.InputStream inp = getAssets().open("languages.json");
					listmap = new Gson().fromJson(SketchwareUtil.copyFromInputStream(inp), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					listview.setAdapter(new ListviewAdapter(listmap));
				}catch(Exception e){
					 
				}
				length = listmap.size();
				r = length - 1;
				for(int _repeat17 = 0; _repeat17 < (int)(length); _repeat17++) {
					value1 = listmap.get((int)r).get("name").toString();
					if (value1.toLowerCase().startsWith(_charSeq.toLowerCase())) {
						
					}
					else {
						listmap.remove((int)(r));
					}
					r--;
				}
				listview.setAdapter(new ListviewAdapter(listmap));
				((BaseAdapter)listview.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
	}
	
	private void initializeLogic() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			Window w =LanguageActivity.this.getWindow();
			w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF000000);
		}
		_setCornerRadii(back_layout1, "#80F7F8FC", 40, 40, 0, 0);
		_setCornerRadii(layout, "#F7F8FC", 40, 40, 0, 0);
		linear_search.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)360, 0xFFFFFFFF));
		if (getIntent().hasExtra("type")) {
			if (getIntent().getStringExtra("type").contains("from")) {
				language_from = true;
			}
			if (getIntent().getStringExtra("type").contains("to")) {
				language_from = false;
			}
		}
		try{
			java.io.InputStream inp = getAssets().open("languages.json");
			listmap = new Gson().fromJson(SketchwareUtil.copyFromInputStream(inp), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			listview.setAdapter(new ListviewAdapter(listmap));
		}catch(Exception e){
			 
		}
		_scrollToSelectedLanguage();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	public void _setCornerRadii(final View _view, final String _color, final double _x1, final double _x2, final double _x3, final double _x4) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		gd.setColor(Color.parseColor(_color));
		gd.setCornerRadii(new float[] { (int)_x1, (int)_x1, (int)_x2, (int)_x2, (int)_x3, (int)_x3, (int)_x4, (int)_x4 }); 
		_view.setBackground(gd);
	}
	
	
	public void _scrollToSelectedLanguage() {
		n = 0;
		for(int _repeat11 = 0; _repeat11 < (int)(listmap.size()); _repeat11++) {
			if (listmap.get((int)n).containsKey("code")) {
				if (language_from) {
					if (data.contains("lang_from_code")) {
						if (listmap.get((int)n).get("code").toString().equals(data.getString("lang_from_code", ""))) {
							listview.setSelection((int)n);
							break;
						}
						else {
							
						}
					}
				}
				else {
					if (data.contains("lang_to_code")) {
						if (listmap.get((int)n).get("code").toString().equals(data.getString("lang_to_code", ""))) {
							listview.setSelection((int)n);
							break;
						}
						else {
							
						}
					}
				}
			}
			n++;
		}
	}
	
	public class ListviewAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public ListviewAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.language_item, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView lang_name = _view.findViewById(R.id.lang_name);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			
			imageview1.setVisibility(View.GONE);
			linear1.setBackgroundColor(Color.TRANSPARENT);
			imageview1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)360, 0xFFFFFFFF));
			if (_data.get((int)_position).containsKey("code")) {
				if (language_from) {
					if (listmap.get((int)_position).get("code").toString().equals(data.getString("lang_from_code", ""))) {
						imageview1.setVisibility(View.VISIBLE);
						linear1.setBackgroundColor(0xFFEAEDF6);
					}
				}
				else {
					if (listmap.get((int)_position).get("code").toString().equals(data.getString("lang_to_code", ""))) {
						imageview1.setVisibility(View.VISIBLE);
						linear1.setBackgroundColor(0xFFEAEDF6);
					}
				}
			}
			if (_data.get((int)_position).containsKey("name")) {
				lang_name.setText(listmap.get((int)_position).get("name").toString());
			}
			linear1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					notifyDataSetChanged();
					if (language_from) {
						data.edit().putString("lang_from", listmap.get((int)_position).get("name").toString()).commit();
						data.edit().putString("lang_from_code", listmap.get((int)_position).get("code").toString()).commit();
					}
					else {
						data.edit().putString("lang_to", listmap.get((int)_position).get("name").toString()).commit();
						data.edit().putString("lang_to_code", listmap.get((int)_position).get("code").toString()).commit();
					}
					finish();
				}
			});
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}