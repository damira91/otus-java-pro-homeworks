package ru.otus.kudaiberdieva.homework07.repositories.entityMetaInfo;

import java.util.List;

public class EntityMetaInfo {

    private EntityField idField;
    private List<EntityField> fields;

    public EntityMetaInfo() {
    }

    public EntityMetaInfo(EntityField idField, List<EntityField> fields) {
        this.idField = idField;
        this.fields = fields;
    }


    public EntityField getIdField() {
        return idField;
    }

    public void setIdField(EntityField idField) {
        this.idField = idField;
    }

    public List<EntityField> getFields() {
        return fields;
    }

    public void setFields(List<EntityField> fields) {
        this.fields = fields;
    }
}