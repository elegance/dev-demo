package org.orh.memory.filter.calcite;

/**
 * MemoryColumn
 *
 * @author ouronghui
 * @since 2023/12/28 14:25
 */
public class MemoryColumn<T> {
    private String name;
    private Class<T> type;

    public MemoryColumn(String name, Class<T> type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}