package com.test.expandable;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.test.expandable.base.ViewHolder;

import java.util.List;
import java.util.Map;

public abstract class BaseExpandaleListViewAdapter extends BaseExpandableListAdapter {

    private List<Map> groupList;

    private List<List<Map>> childList;

    private Context mContext = null;

    private int groupLayoutId;

    private int childLayoutId;

    public abstract void convertParent(ViewHolder holder, Map parent, int groupPosition, boolean isExpanded);
    public abstract void convertChild(ViewHolder holder, Map child, int groupPosition, int childPosition, boolean isLastChild);

    public BaseExpandaleListViewAdapter(Context context, List<Map> groupList, List<List<Map>>childList,
                                        int groupLayoutId, int childLayoutId) {

        this.mContext = context;
        this.groupList = groupList;
        this.childList = childList;
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
    }

    @Override
    public int getGroupCount() {
        return groupList != null ? groupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childList == null || childList.size() <= groupPosition ||
                childList.get(groupPosition) == null) ? 0 : childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList == null ? null : groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (childList == null || childList.size() <= groupPosition ||
                childList.get(groupPosition) == null) ? null : childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, groupLayoutId, groupPosition);
         convertParent(viewHolder,groupList.get(groupPosition),groupPosition,isExpanded);
        return viewHolder.getConvertView();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, childLayoutId, childPosition);
        convertChild(viewHolder,childList.get(groupPosition).get(childPosition),groupPosition, childPosition,isLastChild);
        return viewHolder.getConvertView();
    }

    /**
     * 指定位置相应的组视图
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 当选择子节点的时候，调用该方法(点击二级列表)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
