package com.zealren.sequence.formatter;


import com.zealren.sequence.SequenceException;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface SequenceFormatter {
    /**
     * 序列号格式器
     *
     * @param site
     * @param key
     * @param count
     * @param ruleSets
     * @param seq
     * @param reqTime
     * @param params
     * @return
     * @throws SequenceException
     */
    public List<String> formatSeq(String domain, String site, String key, Long count,
                                  Map<String, String> ruleSets, Long seq, Date reqTime, Object[] params) throws SequenceException;
}
