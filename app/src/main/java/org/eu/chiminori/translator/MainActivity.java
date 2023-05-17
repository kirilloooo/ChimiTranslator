package org.eu.chiminori.translator;

import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private String text = "";
	private HashMap<String, Object> params = new HashMap<>();
	private HashMap<String, Object> map = new HashMap<>();
	private boolean bookmark_visible = false;
	private String to_lang = "";
	private String to_lang_code = "";
	private String from_lang_code = "";
	private String from_lang = "";
	
	private ArrayList<HashMap<String, Object>> listmap_bookmarks = new ArrayList<>();
	private ArrayList<String> gs = new ArrayList<>();
	
	private LinearLayout nav;
	private LinearLayout topbar;
	private LinearLayout layout;
	private ImageView btn_bookmarks;
	private TextView tv_title;
	private ScrollView vscroll;
	private CardView cardview_bookmark;
	private LinearLayout linear_scroll;
	private CardView cardview;
	private LinearLayout linear_card;
	private LinearLayout linear;
	private LinearLayout linear_translated;
	private LinearLayout linear5;
	private LinearLayout linear9;
	private EditText et_text_translate;
	private LinearLayout linear11;
	private TextView tv_from_lang;
	private ImageView btn_change_lamguage;
	private TextView tv_to_lang;
	private ImageView btn_paste;
	private ImageView btn_clear;
	private TextView tv_translated_text;
	private LinearLayout linear10;
	private ImageView btn_copy;
	private ImageView btn_bookmark;
	private LinearLayout linear_bookmarks;
	private TextView textview5;
	private GridView gridview;
	
	private RequestNetwork api;
	private RequestNetwork.RequestListener _api_request_listener;
	private TimerTask translate_timer;
	private SharedPreferences data;
	private Intent intent = new Intent();
	private ObjectAnimator oa = new ObjectAnimator();
	private Intent tg = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		nav = findViewById(R.id.nav);
		topbar = findViewById(R.id.topbar);
		layout = findViewById(R.id.layout);
		btn_bookmarks = findViewById(R.id.btn_bookmarks);
		tv_title = findViewById(R.id.tv_title);
		vscroll = findViewById(R.id.vscroll);
		cardview_bookmark = findViewById(R.id.cardview_bookmark);
		linear_scroll = findViewById(R.id.linear_scroll);
		cardview = findViewById(R.id.cardview);
		linear_card = findViewById(R.id.linear_card);
		linear = findViewById(R.id.linear);
		linear_translated = findViewById(R.id.linear_translated);
		linear5 = findViewById(R.id.linear5);
		linear9 = findViewById(R.id.linear9);
		et_text_translate = findViewById(R.id.et_text_translate);
		linear11 = findViewById(R.id.linear11);
		tv_from_lang = findViewById(R.id.tv_from_lang);
		btn_change_lamguage = findViewById(R.id.btn_change_lamguage);
		tv_to_lang = findViewById(R.id.tv_to_lang);
		btn_paste = findViewById(R.id.btn_paste);
		btn_clear = findViewById(R.id.btn_clear);
		tv_translated_text = findViewById(R.id.tv_translated_text);
		linear10 = findViewById(R.id.linear10);
		btn_copy = findViewById(R.id.btn_copy);
		btn_bookmark = findViewById(R.id.btn_bookmark);
		linear_bookmarks = findViewById(R.id.linear_bookmarks);
		textview5 = findViewById(R.id.textview5);
		gridview = findViewById(R.id.gridview);
		api = new RequestNetwork(this);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		btn_bookmarks.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (bookmark_visible) {
					_TransitionManager(nav, 200);
					bookmark_visible = false;
					cardview_bookmark.setVisibility(View.GONE);
				}
				else {
					_TransitionManager(nav, 200);
					bookmark_visible = true;
					cardview_bookmark.setVisibility(View.VISIBLE);
					_hideKeyboard();
				}
			}
		});
		
		et_text_translate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (bookmark_visible) {
					_TransitionManager(nav, 200);
					bookmark_visible = false;
					cardview_bookmark.setVisibility(View.GONE);
				}
			}
		});
		
		et_text_translate.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				try{
					translate_timer.cancel();
				}catch(Exception e){
					 
				}
				translate_timer = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (et_text_translate.getText().toString().trim().equals("")) {
									_TransitionManager(nav, 200);
									linear_translated.setVisibility(View.GONE);
								}
								else {
									_TransitionManager(nav, 200);
									bookmark_visible = false;
									cardview_bookmark.setVisibility(View.GONE);
									text = et_text_translate.getText().toString().trim();
									params = new HashMap<>();
									params.put("User-Agent", "Mozilla/5.0");
									api.setParams(params, RequestNetworkController.REQUEST_PARAM);
									try{
										api.startRequestNetwork(RequestNetworkController.GET, "https://translate.googleapis.com/translate_a/single?client=gtx&sl=".concat(data.getString("lang_from_code", "").concat("&tl=".concat(data.getString("lang_to_code", "").concat("&dt=t&q=".concat(java.net.URLEncoder.encode(text, "UTF-8")))))), "a", _api_request_listener);
									}catch(Exception e){
										_snackbar("An error occurred");
									}
								}
							}
						});
					}
				};
				_timer.schedule(translate_timer, (int)(250));
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		tv_from_lang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.putExtra("type", "from");
				intent.setClass(getApplicationContext(), LanguageActivity.class);
				startActivity(intent);
			}
		});
		
		btn_change_lamguage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				oa.setTarget(linear5);
				oa.setPropertyName("alpha");
				oa.setFloatValues((float)(0), (float)(1));
				oa.start();
				to_lang = tv_to_lang.getText().toString();
				to_lang_code = data.getString("lang_to_code", "");
				from_lang_code = data.getString("lang_from_code", "");
				from_lang = tv_from_lang.getText().toString();
				data.edit().putString("lang_to", from_lang).commit();
				data.edit().putString("lang_to_code", from_lang_code).commit();
				data.edit().putString("lang_from", to_lang).commit();
				data.edit().putString("lang_from_code", to_lang_code).commit();
				tv_from_lang.setText(data.getString("lang_from", ""));
				tv_to_lang.setText(data.getString("lang_to", ""));
				_TransitionManager(nav, 200);
				bookmark_visible = false;
				cardview_bookmark.setVisibility(View.GONE);
				if (et_text_translate.getText().toString().trim().equals("")) {
					
				}
				else {
					text = et_text_translate.getText().toString().trim();
					params = new HashMap<>();
					params.put("User-Agent", "Mozilla/5.0");
					api.setParams(params, RequestNetworkController.REQUEST_PARAM);
					try{
						api.startRequestNetwork(RequestNetworkController.GET, "https://translate.googleapis.com/translate_a/single?client=gtx&sl=".concat(data.getString("lang_from_code", "").concat("&tl=".concat(data.getString("lang_to_code", "").concat("&dt=t&q=".concat(java.net.URLEncoder.encode(text, "UTF-8")))))), "a", _api_request_listener);
					}catch(Exception e){
						_snackbar("An error occurred");
					}
				}
			}
		});
		
		tv_to_lang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.putExtra("type", "to");
				intent.setClass(getApplicationContext(), LanguageActivity.class);
				startActivity(intent);
			}
		});
		
		btn_paste.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				try{
					et_text_translate.setText(clipboard.getText().toString());
				}catch(Exception e){
					SketchwareUtil.showMessage(getApplicationContext(), "Not copied any text");
				}
			}
		});
		
		btn_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				et_text_translate.setText("");
			}
		});
		
		btn_copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", tv_translated_text.getText().toString()));
				_snackbar("Copied to clipboard");
			}
		});
		
		btn_bookmark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				map = new HashMap<>();
				map.put("text", et_text_translate.getText().toString());
				map.put("translate", tv_translated_text.getText().toString());
				listmap_bookmarks.add(map);
				gridview.setAdapter(new GridviewAdapter(listmap_bookmarks));
				_snackbar("Added to bookmarks");
				_save_bookmark_list();
			}
		});
		
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				et_text_translate.setText(listmap_bookmarks.get((int)_position).get("text").toString());
				_setCursorPosition(et_text_translate, et_text_translate.getText().toString().length());
			}
		});
		
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				_TransitionManager(nav, 200);
				listmap_bookmarks.remove((int)(_position));
				gridview.setAdapter(new GridviewAdapter(listmap_bookmarks));
				if (listmap_bookmarks.size() == -1) {
					bookmark_visible = false;
					cardview_bookmark.setVisibility(View.GONE);
				}
				_save_bookmark_list();
				_snackbar("Removed from bookmarks");
				return true;
			}
		});
		
		_api_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				tv_translated_text.setText("");
				try{
					_TransitionManager(nav, 200);
					linear_translated.setVisibility(View.VISIBLE);
					JSONArray JsonArray1 = new JSONArray(_response);
					JSONArray JsonArray2 = JsonArray1.getJSONArray(0);
					for (int i = 0; i < (int)(JsonArray2.length()); i++) {
						JSONArray JsonArray3 = JsonArray2.getJSONArray((int)i);
						tv_translated_text.setText(tv_translated_text.getText().toString().concat(JsonArray3.getString(0)));
					}
				}catch(Exception e){
					_snackbar("An error occured");
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				_snackbar("No internet connection");
			}
		};
	}
	
	private void initializeLogic() {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			Window w =MainActivity.this.getWindow();
			w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFFF7F8FC);
		}
		tv_title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/medium.ttf"), 0);
		_restore_bookmarks_list();
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		if (data.contains("lang_from")) {
			tv_from_lang.setText(data.getString("lang_from", ""));
		}
		else {
			tv_from_lang.setText("auto");
			data.edit().putString("lang_from", tv_from_lang.getText().toString()).commit();
			data.edit().putString("lang_from_code", "auto").commit();
		}
		if (data.contains("lang_to")) {
			tv_to_lang.setText(data.getString("lang_to", ""));
		}
		else {
			tv_to_lang.setText(java.util.Locale.getDefault().getDisplayLanguage());
			data.edit().putString("lang_to", tv_to_lang.getText().toString()).commit();
			data.edit().putString("lang_to_code", java.util.Locale.getDefault().getLanguage()).commit();
		}
		if (data.contains("lang_from") && data.contains("lang_to")) {
			if (et_text_translate.getText().toString().trim().equals("")) {
				_TransitionManager(nav, 200);
				linear_translated.setVisibility(View.GONE);
			}
			else {
				_TransitionManager(nav, 200);
				bookmark_visible = false;
				cardview_bookmark.setVisibility(View.GONE);
				text = et_text_translate.getText().toString().trim();
				params = new HashMap<>();
				params.put("User-Agent", "Mozilla/5.0");
				api.setParams(params, RequestNetworkController.REQUEST_PARAM);
				try{
					api.startRequestNetwork(RequestNetworkController.GET, "https://translate.googleapis.com/translate_a/single?client=gtx&sl=".concat(data.getString("lang_from_code", "").concat("&tl=".concat(data.getString("lang_to_code", "").concat("&dt=t&q=".concat(java.net.URLEncoder.encode(text, "UTF-8")))))), "a", _api_request_listener);
				}catch(Exception e){
					_snackbar("An error occurred");
				}
			}
		}
		btn_bookmarks.setVisibility(View.INVISIBLE);
		btn_bookmark.setVisibility(View.INVISIBLE);
	}
	public void _TransitionManager(final View _view, final double _duration) {
		LinearLayout viewgroup =(LinearLayout) _view;
		
		android.transition.AutoTransition autoTransition = new android.transition.AutoTransition(); autoTransition.setDuration((long)_duration); android.transition.TransitionManager.beginDelayedTransition(viewgroup, autoTransition);
	}
	
	
	public void _snackbar(final String _text) {
		Snackbar snackbar = Snackbar.make(nav, _text, Snackbar.LENGTH_LONG);
		snackbar.setDuration(2000);
		snackbar.show();
	}
	
	
	public void _hideKeyboard() {
		android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE); 
		//Find the currently focused view, so we can grab the correct window token from it. 
		imm.hideSoftInputFromWindow(et_text_translate.getWindowToken(), 0);
	}
	
	
	public void _save_bookmark_list() {
		data.edit().putString("bookmark", new Gson().toJson(listmap_bookmarks)).commit();
	}
	
	
	public void _restore_bookmarks_list() {
		if (data.contains("bookmark")) {
			listmap_bookmarks = new Gson().fromJson(data.getString("bookmark", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			gridview.setAdapter(new GridviewAdapter(listmap_bookmarks));
		}
	}
	
	
	public void _setCursorPosition(final TextView _textview, final double _position) {
		((EditText)_textview).setSelection((int)_position);
	}
	
	
	public void _setCornerRadii(final View _view, final String _color, final double _x1, final double _x2, final double _x3, final double _x4) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		gd.setColor(Color.parseColor(_color));
		gd.setCornerRadii(new float[] { (int)_x1, (int)_x1, (int)_x2, (int)_x2, (int)_x3, (int)_x3, (int)_x4, (int)_x4 }); 
		_view.setBackground(gd);
	}
	
	public class GridviewAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public GridviewAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_view = _inflater.inflate(R.layout.bookmark_item, null);
			}
			
			final LinearLayout linear = _view.findViewById(R.id.linear);
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final LinearLayout linear3 = _view.findViewById(R.id.linear3);
			final TextView tv_text = _view.findViewById(R.id.tv_text);
			final TextView tv_translated_text = _view.findViewById(R.id.tv_translated_text);
			
			if (_data.get((int)_position).containsKey("text")) {
				tv_text.setText(_data.get((int)_position).get("text").toString());
			}
			if (_data.get((int)_position).containsKey("translate")) {
				tv_translated_text.setText(_data.get((int)_position).get("translate").toString());
			}
			
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