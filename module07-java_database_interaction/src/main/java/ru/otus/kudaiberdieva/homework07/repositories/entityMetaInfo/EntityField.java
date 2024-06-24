package ru.otus.kudaiberdieva.homework07.repositories.entityMetaInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EntityField {
    private Field field;
    private Method getter;
    private Method setter;

    public EntityField(Field field, Method getter, Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

    public EntityField() {
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }
}