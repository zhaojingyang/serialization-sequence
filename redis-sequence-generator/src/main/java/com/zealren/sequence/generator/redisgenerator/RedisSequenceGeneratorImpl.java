package com.zealren.sequence.generator.redisgenerator;


import com.zealren.sequence.SequenceException;
import com.zealren.sequence.formatter.SequenceFormatter;
import com.zealren.sequence.generator.SequenceGenerator;
import com.zealren.sequence.generator.redisgenerator.util.JedisUtil;
import com.zealren.sequence.utils.ListUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisSequenceGeneratorImpl implements SequenceGenerator {

    private JedisUtil sequenceJedisUtil;

    public JedisUtil getSequenceJedisUtil() {
        return sequenceJedisUtil;
    }

    public void setSequenceJedisUtil(JedisUtil sequenceJedisUtil) {
        this.sequenceJedisUtil = sequenceJedisUtil;
    }

    @Override
    public List<String> generate(String domain, String site, String key, Long count, SequenceFormatter sequenceFormatter, Object... params) throws SequenceException {
        Date now = new Date();
        try {
            String ruleKey = SeqHelper.KEY_PRE + domain + SeqHelper.SEP + site + SeqHelper.SEP + key;
            Map<Object, Object> objRuleSets = sequenceJedisUtil.hGetAll(ruleKey);
            if (null == objRuleSets || objRuleSets.isEmpty()) {
                throw new SequenceException("未获取到配置信息" + ruleKey);
            }

            Map<String, String> ruleSets = new HashMap<>();
            for (Map.Entry<Object, Object> entry : objRuleSets.entrySet()) {
                ruleSets.put((String) entry.getKey(), (String) entry.getValue());
            }

            String seqLength = ruleSets.get(SeqHelper.SEQ_LENGTH);
            if (StringUtils.isEmpty(seqLength)) {
                throw new SequenceException("未设置length");
            }
            String type = ruleSets.get(SeqHelper.LOOP_TYPE);
            String seqKey = SeqHelper.getSeqKey(type, now);
            if (null == count || count <= 0) {
                count = 1L;
            }
            Long newSeq = sequenceJedisUtil.hIncrement(ruleKey, seqKey, count);
            if (null == count || count <= 0) {
                throw new SequenceException("序列号生成异常");
            }
            return sequenceFormatter.formatSeq(domain, site, key, count, ruleSets, newSeq, now, params);
        } catch (SequenceException e) {
            throw e;
        } catch (Exception ex) {
            throw new SequenceException("生成序列号异常");
        }
    }

    @Override
    public String generateOneSeq(String domain, String site, String key, SequenceFormatter sequenceFormatter, Object... params) throws SequenceException {
        List<String> seqList = generate(domain, site, key, 1L, sequenceFormatter, params);
        if (ListUtil.isNullOrEmpty(seqList)) {
            throw new SequenceException("生成序列号异常");
        }
        return seqList.get(0);
    }
}
