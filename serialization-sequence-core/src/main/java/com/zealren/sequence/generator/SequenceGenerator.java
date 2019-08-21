package com.zealren.sequence.generator;

import com.zealren.sequence.SequenceException;
import com.zealren.sequence.formatter.SequenceFormatter;

import java.util.List;


public interface SequenceGenerator {
    /**
     * 生成序列号
     *
     * @param site
     * @param key
     * @param count
     * @param sequenceFormatter
     * @param params
     * @return
     * @throws SequenceException
     */
    public List<String> generate(String domain, String site, String key, Long count,
                                 SequenceFormatter sequenceFormatter, Object... params) throws SequenceException;

    /**
     * 生成一个序列号
     *
     * @param domain
     * @param site
     * @param key
     * @param sequenceFormatter
     * @param params
     * @return
     * @throws SequenceException
     */
    public String generateOneSeq(String domain, String site, String key,
                                 SequenceFormatter sequenceFormatter, Object... params) throws SequenceException;
}