package com.data.source.hot.update;


import com.data.source.hot.update.config.ConfigItem;
import com.data.source.hot.update.config.ConfigurationChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class DataSourceConfigChangeListener implements ConfigurationChangeListener {

    protected static final Logger logger = LoggerFactory.getLogger(DataSourceConfigChangeListener.class);

    @Override
    public String[] patterns() {
        return new String[0];
    }

    @Override
    public List<ConfigItem> findMatchItems(Collection<ConfigItem> srcItems) {
        return new ArrayList<>(srcItems);
    }

    @Override
    public Collection<Pattern> getPattens() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void onConfigChange(List<ConfigItem> configItems) {
        for (ConfigItem item : configItems) {
            for (DataSourceHolder holder : DataSourceHotUpdateManager.getDataSourceHolders()) {
                if(isAddressChange(item,holder) || isPoolSizeChange(item,holder)){
                    DataSourceHotUpdateManager.updateDataSource(holder, item.getNewValue(),item.getPoolSize());
                }
            }
        }
    }


    private boolean isAddressChange(ConfigItem item,DataSourceHolder holder){
        //旧值如果和当前连接一致，认为是更新该连接
        if (!holder.getUrl().equals(item.getOldValue())) {
            return false;
        }
        //当前连接值与新值一致，不更新
        if (holder.getUrl().equals(item.getNewValue())) {
            return false;
        }
        return true;
    }

    private boolean isPoolSizeChange(ConfigItem item,DataSourceHolder holder){
        if (!holder.getUrl().equals(item.getOldValue())) {
            return false;
        }
        return item.getPoolSize()>0 && holder.getMaxPoolSize() != item.getPoolSize();

    }
}
