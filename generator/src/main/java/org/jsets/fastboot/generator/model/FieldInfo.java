package org.jsets.fastboot.generator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class FieldInfo {
    private String name;
    private String type;
}