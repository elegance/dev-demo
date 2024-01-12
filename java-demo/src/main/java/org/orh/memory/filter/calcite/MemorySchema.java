package org.orh.memory.filter.calcite;

import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MemorySchema
 *
 * @author ouronghui
 * @since 2023/12/27 19:57
 */
public class MemorySchema extends AbstractSchema {

    private Map<String, Table> tableMap;
    private List<MemoryColumn> meta;
    private List<List<Object>> source;

    public MemorySchema(List<MemoryColumn> meta, List<List<Object>> source){
        this.meta = meta;
        this.source = source;
    }

    @Override
    public Map<String, Table> getTableMap(){
        if(CollectionUtils.isEmpty(tableMap)){
            tableMap = new HashMap<>();
            tableMap.put("memory", new MemoryTable(meta, source));
        }
        return tableMap;
    }
}