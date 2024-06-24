package ru.otus.kudaiberdieva.homework07.repositories;

import ru.otus.kudaiberdieva.homework07.exceptions.RepositoryOperationException;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryIdField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryTable;
import ru.otus.kudaiberdieva.homework07.exceptions.EntityException;
import ru.otus.kudaiberdieva.homework07.repositories.entityMetaInfo.EntityField;
import ru.otus.kudaiberdieva.homework07.repositories.entityMetaInfo.EntityMetaInfo;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class AbstractRepository<T> {

    private final DataSource dataSource;
    private final Class<T> cls;
    private final String tableName;
    private EntityMetaInfo entityMetaInfo;

    private PreparedStatement psFindAll;
    private PreparedStatement psFindById;
    private PreparedStatement psCreate;
    private PreparedStatement psUpdate;
    private PreparedStatement psDeleteAll;
    private PreparedStatement psDeleteById;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        this.tableName = cls.getAnnotation(RepositoryTable.class).title();
        prepareRepository(cls);
    }

    private void prepareRepository(Class<T> cls) {
        try {
            cls.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RepositoryOperationException(String.format("Entity has no default constructor"));
        }

        List<EntityField> entityFieldsWithNoId = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .map(this::createEntityField).toList();

        if (entityFieldsWithNoId.isEmpty()) {
            throw new RepositoryOperationException(String.format("Entity has no other fields but identifier"));
        }

        Field idField = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst().orElseThrow(() -> new RepositoryOperationException(String.format("Entity has no necessary identifier")));

        EntityField entityFieldWithId = createEntityField(idField);

        entityMetaInfo = new EntityMetaInfo(entityFieldWithId, entityFieldsWithNoId);

        prepareStatementCreate();
        prepareStatementFindById();
        prepareStatementFindAll();
        prepareStatementUpdate();
        prepareStatementDeleteById();
        prepareStatementDeleteAll();
    }


    private EntityField createEntityField(Field field) {
        String fieldName = field.getName();
        Method getter;
        Method setter;
        try {
            getter = cls.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
        } catch (NoSuchMethodException e) {
            throw new RepositoryOperationException(String.format("There is no getter for field %s", fieldName));
        }
        try {
            setter = cls.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
        } catch (NoSuchMethodException e) {
            throw new RepositoryOperationException(String.format("There is no setter for field %s", fieldName));
        }
        return new EntityField(field, getter, setter);
    }

    public void create(T entity) {
        try {
            int i = 1;
            for (EntityField field : entityMetaInfo.getFields()) {
                try {
                    psCreate.setObject(i++, field.getGetter().invoke(entity));
                } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
                    throw new EntityException(String.format("Error setting field value for field %s: %s", field.getField().getName(), e.getMessage()), e);
                }
            }
            psCreate.executeUpdate();
        } catch (Exception e) {
            throw new EntityException(String.format("Error creating entity %s: %s", cls.getName(), e.getMessage()), e);
        }
    }

    public void update(T entity) {
        try {
            int i = 1;
            for (EntityField field : entityMetaInfo.getFields()) {
                try {
                    psUpdate.setObject(i++, field.getGetter().invoke(entity));
                } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            psUpdate.setObject(i, entityMetaInfo.getIdField().getGetter().invoke(entity));
            psUpdate.executeUpdate();
        } catch (Exception e) {
            throw new EntityException(String.format("Failed to update " + cls.getName()), e.getCause());
        }
    }

    public void deleteById(Long id) {
        try {
            psDeleteById.setLong(1, id);
            psDeleteById.executeUpdate();
        } catch (SQLException e) {
            throw new EntityException(String.format("Error deleting entity by id: %s", cls.getName(), id), e.getCause());
        }
    }

    public void deleteAll() {
        try {
            psDeleteAll.executeUpdate();
        } catch (SQLException e) {
            throw new EntityException(String.format("Error deleting all %s from table : ", cls.getName()), e.getCause());
        }
    }

    public List<T> findAll() {
        try {
            List<T> out = new ArrayList<>();
            ResultSet resultSet = psFindAll.executeQuery();

            while (resultSet.next()) {
                T newObject = cls.getConstructor().newInstance();
                setField(newObject, entityMetaInfo.getIdField(), resultSet);
                entityMetaInfo.getFields().forEach(field -> {
                    setField(newObject, field, resultSet);
                });
                out.add(newObject);
            }
            return out;
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RepositoryOperationException(String.format("Error finding all %s ", cls.getName()) + e.getCause());
        }
    }

    public T findById(Long id) {
        try {
            psFindById.setLong(1, id);
            ResultSet resultSet = psFindById.executeQuery();

            T newObject = cls.getConstructor().newInstance();

            if (resultSet.next()) {
                setField(newObject, entityMetaInfo.getIdField(), resultSet);
                entityMetaInfo.getFields().forEach(field -> {
                    setField(newObject, field, resultSet);
                });
            } else {
                throw new EntityException(String.format("Entity not found %s", id));
            }

            return newObject;
        } catch (SQLException | InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new EntityException(String.format("Error finding object by ID s%", id), e);
        }
    }

    private void setField(T newObject, EntityField entityField, ResultSet resultSet) {
        try {
            entityField.getSetter().invoke(newObject, resultSet.getObject(entityField.getField().getName(), entityField.getField().getType()));
        } catch (IllegalAccessException | InvocationTargetException | SQLException e) {
            throw new EntityException(String.format("Ошибка при установке полей для %s", cls.getName()), e.getCause());
        }
    }

    private void prepareStatementCreate() {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        entityMetaInfo.getFields().forEach(entityField -> {
            query.append(entityField.getField().getName()).append(", ");
        });
        query.setLength(query.length() - 2);
        query.append(") VALUES (");
        query.append("?, ".repeat(entityMetaInfo.getFields().size()));
        query.setLength(query.length() - 2);
        query.append(");");
        try {
            psCreate = dataSource.getConnection().prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RepositoryOperationException("Invalid prepareStatement for create. Error: " + e.getMessage());
        }
    }

    private void prepareStatementUpdate() {
        StringBuilder query = new StringBuilder(String.format("UPDATE %s SET ", tableName));

        entityMetaInfo.getFields().forEach(entityField -> {
            query.append(entityField.getField().getName()).append(" = ?, ");
        });
        query.setLength(query.length() - 2);
        query.append(String.format(" WHERE %s = ?", entityMetaInfo.getIdField().getField().getName()));

        try {
            psUpdate = dataSource.getConnection().prepareStatement(query.toString());
        } catch (SQLException e) {
            throw new RepositoryOperationException("Invalid prepareStatement for update. Error: " + e.getMessage());
        }
    }

    private void prepareStatementFindAll() {
        try {
            psFindAll = dataSource.getConnection().prepareStatement(String.format("SELECT * FROM %s", tableName));
        } catch (SQLException e) {
            throw new RepositoryOperationException("Invalid prepareStatement to findAll. Error: " + e.getMessage());
        }
    }

    private void prepareStatementFindById() {
        try {
            psFindById = dataSource.getConnection().prepareStatement(String.format("SELECT * FROM %s where %s = ?", tableName, entityMetaInfo.getIdField().getField().getName()));
        } catch (SQLException e) {
            throw new RepositoryOperationException("Invalid prepareStatement to findById. Error: " + e.getMessage());
        }
    }

    private void prepareStatementDeleteAll() {
        try {
            psDeleteAll = dataSource.getConnection().prepareStatement(String.format("DELETE FROM %s", tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatementDeleteById() {
        try {
            psDeleteById = dataSource.getConnection().prepareStatement(String.format("DELETE FROM %s where %s = ?", tableName, entityMetaInfo.getIdField().getField().getName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}