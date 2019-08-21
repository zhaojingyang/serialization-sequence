package com.zealren.sequence.formatter.common;

import com.zealren.sequence.SequenceException;
import com.zealren.sequence.formatter.SequenceFormatter;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CommonSeqFormatter implements SequenceFormatter {
    @Override
    public List<String> formatSeq(String domain, String site, String key, Long count, Map<String, String> ruleSets,
                                  Long seq, Date reqTime, Object[] params) throws SequenceException {
        String template = ruleSets.get("template");
        String[] items = template.split("-");
        List<String> seqList = new ArrayList<String>(count.intValue());
        int paramLength = params == null ? 0 : params.length;
        for (; count > 0; count--) {
            StringBuffer seqSb = new StringBuffer();
            Long tempSeq = seq - count + 1;
            for (String item : items) {
                if (item.startsWith("rule:")) {
                    String itemKey = item.split(":")[1];
                    if (StringUtils.isEmpty(itemKey)) {
                        throw new SequenceException("未配置" + itemKey);
                    }
                    String itemValue = ruleSets.get(itemKey);
                    seqSb.append(itemValue == null ? "" : itemValue);
                } else if (item.equals("site")) {
                    seqSb.append(site);
                } else if (item.equals("seq")) {
                    String length = ruleSets.get("length");
                    if (StringUtils.isEmpty(length)) {
                        throw new SequenceException("未配置" + length);
                    }
                    seqSb.append(formatSeq(Integer.parseInt(length), tempSeq));
                } else if (item.startsWith("param:")) {
                    String itemKey = item.split(":")[1];
                    if (StringUtils.isEmpty(itemKey)) {
                        throw new SequenceException("未配置" + itemKey);
                    }
                    int index = Integer.valueOf(itemKey);
                    if (index >= paramLength) {
                        throw new SequenceException("params异常");
                    }
                    Object obj = params[index];
                    if (null == obj) {
                        throw new SequenceException("param" + index + "不存在");
                    }

                    seqSb.append(obj.toString());
                } else if (item.startsWith("time:")) {
                    String itemKey = item.split(":")[1];
                    if (StringUtils.isEmpty(itemKey)) {
                        throw new SequenceException("未配置" + itemKey);
                    }
                    SimpleDateFormat df = new SimpleDateFormat(itemKey);
                    seqSb.append(df.format(reqTime));
                } else {
                    throw new SequenceException("item异常" + item);
                }
            }
            seqList.add(seqSb.toString());
        }
        return seqList;
    }

    private String formatSeq(int length, Long seq) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumIntegerDigits(length);
        nf.setMinimumIntegerDigits(length);
        return nf.format(seq);
    }
}
