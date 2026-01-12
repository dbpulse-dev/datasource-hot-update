package com.data.source.hot.update.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 监听配置刷新
 * <p/>
 *
 * @author : xieyang
 * @version : 1.0.0
 * @date : 2019/12/25
 */
public interface ConfigurationChangeListener {

    String[] patterns();

    void onConfigChange(List<ConfigItem> items);

    default List<ConfigItem> findMatchItems(Collection<ConfigItem> srcItems){
        List<ConfigItem> targetList = new ArrayList<>(srcItems.size());
        Collection<Pattern> patterns = getPattens();
        for(ConfigItem item:srcItems){
            for(Pattern pt:patterns){
                Matcher matcher = pt.matcher(item.getKey());
                if(matcher.matches()){
                    targetList.add(item);
                }
            }
        }
        return targetList;
    }

    Collection<Pattern> getPattens();
}
