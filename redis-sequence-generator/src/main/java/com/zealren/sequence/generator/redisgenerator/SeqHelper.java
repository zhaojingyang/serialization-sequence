package com.zealren.sequence.generator.redisgenerator;


import com.zealren.sequence.SequenceException;
import com.zealren.sequence.enums.SequenceLoopType;
import com.zealren.sequence.utils.DateUtils;

import java.util.Date;

public final class SeqHelper {
    public static String KEY_PRE = "SEQ:";
    public static final String SEP = ":";
    public static final String LOOP_TYPE = "loopType";
    public static final String SEQ_LENGTH = "length";
    public static final String SEQ_TEMPLATE = "template";
    public static final String SEQ_PREFIX = "prefix";


    public static final String getSeqKey(String type, Date reqDate) throws SequenceException {
        if (SequenceLoopType.NO_LOOP.getValue().equals(type)) {
            return "seqNo";
        } else if (SequenceLoopType.YEAR.getValue().equals(type)) {
            return DateUtils.formatDateStr(reqDate, "yyyy");
        } else if (SequenceLoopType.MONTH.getValue().equals(type)) {
            return DateUtils.formatDateStr(reqDate, "yyyyMM");
        } else if (SequenceLoopType.DAY.getValue().equals(type)) {
            return DateUtils.formatDateStr(reqDate, "yyyyMMdd");
        }
        throw new SequenceException("未获Loop规则");
    }
}
