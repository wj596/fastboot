package org.jsets.fastboot.generator.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 字段.
 */
@Data
@NoArgsConstructor
@ToString
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private int length;
    private int radix;
    private boolean nullable;
    private boolean primary;
    private String comment;
}
