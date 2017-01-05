package com.crs.musicapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crs.musicapp.R;
import com.crs.musicapp.adapter.CurrentSearchListViewAdapter;
import com.crs.musicapp.bean.PlaySongBean;
import com.crs.musicapp.bean.SearchContentBean;
import com.crs.musicapp.util.CommonConst;
import com.crs.musicapp.util.HttpUtil;
import com.crs.musicapp.util.JsonUtil;
import com.crs.musicapp.util.MediaUtil;
import com.crs.musicapp.util.ThreadPoolUtil;

import java.util.List;

import static com.crs.musicapp.R.id.search_footer_progressBar;

/**
 * Created by qf on 2016/11/27.
 */

public class SearchMainFrament extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private View inflateView;
    private View searchHideBtn;
    private ListView currentSearchListView;
    private CurrentSearchListViewAdapter currentSearchListViewAdapter;
    private View searchBtn;
    private EditText searchContentEditText;
    private View currentSearchListFooterView;
    private boolean isAddFooterView;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isGetNextPage;
    private TextView searchLoadingTextView;
    private View currentSearchFooterContainer;
    private boolean isMorePage;
    private static int allPages;
    private static int page;
    private String searchContent;
    private View searchProgressBar;
    private MediaUtil mediaUtil;
    private PlaySongBean playSongBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateView == null) {
//            布局解析器解析布局文件为view
            inflateView = inflater.inflate(R.layout.fragment_search_main, container, false);
//            找控件
            searchHideBtn = inflateView.findViewById(R.id.search_hide_button);
            searchBtn = inflateView.findViewById(R.id.editText_search_btn);
            searchContentEditText = (EditText) inflateView.findViewById(R.id.search_content_editText);
            currentSearchListView = (ListView) inflateView.findViewById(R.id.current_search_listview);
            currentSearchListFooterView = inflater.inflate(R.layout.current_search_content_footer, currentSearchListView, false);
            searchLoadingTextView = (TextView) currentSearchListFooterView.findViewById(R.id.search_loading_textView);
            searchProgressBar = currentSearchListFooterView.findViewById(search_footer_progressBar);
//            适配器
            currentSearchListViewAdapter = new CurrentSearchListViewAdapter(getActivity());
            currentSearchListView.setAdapter(currentSearchListViewAdapter);
            currentSearchListView.setOnItemClickListener(this);

        }
//            监听器
        searchBtn.setOnClickListener(this);
        searchHideBtn.setOnClickListener(this);
        currentSearchListView.setOnScrollListener(this);
//        初始化参数
        if (mediaUtil == null)
            mediaUtil = new MediaUtil(getContext());
        return inflateView;
    }

    //    主线程执行Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonConst.HANDLER_MSG_QUERY_RESPONSE_CODE:
                    SearchContentBean searchContentBean = (SearchContentBean) msg.obj;
//                    在搜索的时候给listview添加footer
                    while (!isAddFooterView) {
                        isAddFooterView = true;
                        currentSearchListView.addFooterView(currentSearchListFooterView);
                    }
//                    链式编程容易空指针
                    if (searchContentBean == null || searchContentBean.getShowapi_res_body() == null || searchContentBean.getShowapi_res_body().getPagebean() == null)
                        return;
//                    获得总页数
                    allPages = searchContentBean.getShowapi_res_body().getPagebean().getAllPages();
//                    查不到相关信息，就不显示listview
                    if (allPages == 0) {
                        Toast.makeText(getActivity(), "查无结果，请重新输入", Toast.LENGTH_SHORT).show();
                        currentSearchListView.setVisibility(View.GONE);
                        return;
                    }
//                    查到相关信息，显示listview
                    currentSearchListView.setVisibility(View.VISIBLE);
//                    链式编程容易空指针
                    if (searchContentBean == null || searchContentBean.getShowapi_res_body() == null || searchContentBean.getShowapi_res_body().getPagebean() == null)
                        return;
                    List<SearchContentBean.Showapi_res_body.Pagebean.QuerySong> contentlist = searchContentBean.getShowapi_res_body().getPagebean().getContentlist();
                    currentSearchListViewAdapter.updateData(contentlist);
//                    单页和多页情况
                    if (allPages <= CommonConst.DEFAULT_PAGE) {
                        searchLoadingTextView.setText("已至末尾");
                    } else {
                        searchLoadingTextView.setText("上滑加载更多");
                        isMorePage = true;
                    }
                    break;
                case CommonConst.HANDLER_MSG_LOAD_NEXT_RESPONSE_CODE:
                    SearchContentBean searchContentBeanAdd = (SearchContentBean) msg.obj;
//                    链式编程容易空指针
                    if (searchContentBeanAdd == null || searchContentBeanAdd.getShowapi_res_body() == null || searchContentBeanAdd.getShowapi_res_body().getPagebean() == null)
                        return;
                    List<SearchContentBean.Showapi_res_body.Pagebean.QuerySong> contentlistAdd = searchContentBeanAdd.getShowapi_res_body().getPagebean().getContentlist();
                    currentSearchListViewAdapter.addData(contentlistAdd);
//                    最后一页情况
                    searchProgressBar.setVisibility(View.GONE);
                    if (page == allPages) {
                        isMorePage = false;
                        searchLoadingTextView.setText("已至末尾");
                    } else {
                        isMorePage = true;
                        searchLoadingTextView.setText("上滑加载更多");
                        isGetNextPage = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
//        重置所有参数
        page = CommonConst.DEFAULT_PAGE;
        isMorePage = false;
        isGetNextPage = false;
        currentSearchListView.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.search_hide_button:
                searchContentEditText.setText("");
                if (clickListener == null)
                    return;
                clickListener.onActionBarItemClick(view);
                break;
            case R.id.editText_search_btn:
                searchContent = searchContentEditText.getText().toString().trim();
                if (searchContent == null || searchContent.equals("")) {
                    Toast.makeText(getActivity(), "输入的内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                ThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        String jsonResult = HttpUtil.search(searchContent, CommonConst.DEFAULT_PAGE);
                        SearchContentBean searchContentBean = JsonUtil.parseQueryJson(jsonResult);
                        Message msg = mHandler.obtainMessage();
                        msg.what = CommonConst.HANDLER_MSG_QUERY_RESPONSE_CODE;
                        msg.obj = searchContentBean;
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            default:
                break;
        }
    }

    private OnMainFragmentClickListener clickListener;

    public void setClickListener(OnMainFragmentClickListener clickListener) {
        this.clickListener = clickListener;
    }

    //    滑动监听事件重写方法
    @Override

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!isMorePage)
            return;
        if (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            return;
        if (firstVisibleItem + visibleItemCount != totalItemCount)
            return;
        while (!isGetNextPage) {
            isGetNextPage = true;
            searchProgressBar.setVisibility(View.VISIBLE);
            searchLoadingTextView.setText("正在载入更多...");
            getNextCurrentSearchPage();
        }

    }

    //    加载下一页
    private void getNextCurrentSearchPage() {
        if (page < allPages) {
            page++;
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    String jsonResult = HttpUtil.search(searchContent, page);
                    SearchContentBean searchContentBean = JsonUtil.parseQueryJson(jsonResult);
                    Message msg = mHandler.obtainMessage();
                    msg.what = CommonConst.HANDLER_MSG_LOAD_NEXT_RESPONSE_CODE;
                    msg.obj = searchContentBean;
                    mHandler.sendMessage(msg);
                }
            });
        }
        return;
    }

    //    滑动监听事件重写方法
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    //    liveview列表项的点击事件---播放点击歌曲
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MediaUtil.releaseMusic();
        SearchContentBean.Showapi_res_body.Pagebean.QuerySong clickItemSong = currentSearchListViewAdapter.getContentlist().get(position);
        // TODO: 2016/12/16 搜索列表：新建播放列表对象
        PlaySongBean playSongBean =  new PlaySongBean(clickItemSong.getM4a(),clickItemSong.getAlbumname(),clickItemSong.getAlbumpic_big(),clickItemSong.getAlbumpic_small(),clickItemSong.getDownUrl(),clickItemSong.getSingerid(),clickItemSong.getSingername(),clickItemSong.getSongid(),clickItemSong.getSongmid(),clickItemSong.getSongname());
        mediaUtil.playMusic(playSongBean);
    }

    //    接口
    public interface OnMainFragmentClickListener {
        void onActionBarItemClick(View view);
    }
}
